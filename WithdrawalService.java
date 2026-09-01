package com.enviro.assessment.junior.nomdumiso.service;

import com.enviro.assessment.junior.nomdumiso.dto.WithdrawalRequestDto;
import com.enviro.assessment.junior.nomdumiso.dto.WithdrawalResponseDto;
import com.enviro.assessment.junior.nomdumiso.entity.*;
import com.enviro.assessment.junior.nomdumiso.exception.BusinessRuleException;
import com.enviro.assessment.junior.nomdumiso.exception.ResourceNotFoundException;
import com.enviro.assessment.junior.nomdumiso.repository.ProductRepository;
import com.enviro.assessment.junior.nomdumiso.repository.WithdrawalNoticeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Encapsulates the withdrawal business rules for Enviro365:
 *  1. Retirement annuity withdrawals are only allowed if the investor is over 65.
 *  2. A withdrawal may never exceed the product's current balance.
 *  3. A withdrawal may never exceed 90% of the product's current balance.
 *
 * Rule violations raise BusinessRuleException, which GlobalExceptionHandler
 * turns into a clear 422 response with a human-readable message so the
 * frontend can give the investor proper feedback.
 */
@Service
public class WithdrawalService {

    private static final BigDecimal MAX_WITHDRAWAL_RATIO = new BigDecimal("0.90");
    private static final int RETIREMENT_MIN_AGE = 65;

    private final ProductRepository productRepository;
    private final WithdrawalNoticeRepository withdrawalNoticeRepository;

    public WithdrawalService(ProductRepository productRepository,
                              WithdrawalNoticeRepository withdrawalNoticeRepository) {
        this.productRepository = productRepository;
        this.withdrawalNoticeRepository = withdrawalNoticeRepository;
    }

    @Transactional
    public WithdrawalResponseDto createWithdrawal(WithdrawalRequestDto request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No product found with id " + request.getProductId()));

        Investor investor = product.getPortfolio().getInvestor();
        BigDecimal amount = request.getAmount();
        BigDecimal balanceBefore = product.getBalance();

        validateBusinessRules(product, investor, amount);

        BigDecimal balanceAfter = balanceBefore.subtract(amount);
        product.setBalance(balanceAfter);
        productRepository.save(product);

        WithdrawalNotice notice = new WithdrawalNotice();
        notice.setProduct(product);
        notice.setAmount(amount);
        notice.setBalanceBefore(balanceBefore);
        notice.setBalanceAfter(balanceAfter);
        notice.setStatus(WithdrawalStatus.APPROVED);
        notice.setRequestedAt(LocalDateTime.now());

        WithdrawalNotice saved = withdrawalNoticeRepository.save(notice);
        return WithdrawalResponseDto.fromEntity(saved);
    }

    /**
     * Runs all three business rules in order and throws a BusinessRuleException
     * with a specific, investor-friendly message on the first one that fails.
     */
    private void validateBusinessRules(Product product, Investor investor, BigDecimal amount) {
        if (product.getType() == ProductType.RETIREMENT_ANNUITY && investor.getAge() <= RETIREMENT_MIN_AGE) {
            throw new BusinessRuleException(
                    "Retirement annuity withdrawals are only permitted for investors over the age of 65. "
                            + investor.getFirstName() + " is currently " + investor.getAge() + ".");
        }

        if (amount.compareTo(product.getBalance()) > 0) {
            throw new BusinessRuleException(
                    "Withdrawal amount (R" + amount + ") exceeds the available balance (R"
                            + product.getBalance() + ") on this product.");
        }

        BigDecimal maxAllowed = product.getBalance().multiply(MAX_WITHDRAWAL_RATIO);
        if (amount.compareTo(maxAllowed) > 0) {
            throw new BusinessRuleException(
                    "Withdrawal amount (R" + amount + ") exceeds the maximum allowed of 90% of the balance "
                            + "(R" + maxAllowed.setScale(2, java.math.RoundingMode.HALF_UP) + ").");
        }
    }

    public List<WithdrawalResponseDto> search(Long investorId, WithdrawalStatus status,
                                               LocalDateTime from, LocalDateTime to) {
        return withdrawalNoticeRepository.search(investorId, status, from, to).stream()
                .map(WithdrawalResponseDto::fromEntity)
                .collect(Collectors.toList());
    }
}
