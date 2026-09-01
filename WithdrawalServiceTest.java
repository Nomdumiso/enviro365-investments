package com.enviro.assessment.junior.nomdumiso.service;

import com.enviro.assessment.junior.nomdumiso.dto.WithdrawalRequestDto;
import com.enviro.assessment.junior.nomdumiso.entity.*;
import com.enviro.assessment.junior.nomdumiso.exception.BusinessRuleException;
import com.enviro.assessment.junior.nomdumiso.repository.ProductRepository;
import com.enviro.assessment.junior.nomdumiso.repository.WithdrawalNoticeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Covers the three withdrawal business rules in isolation, using Mockito so
 * no database is needed. Run with: mvn test
 */
@ExtendWith(MockitoExtension.class)
class WithdrawalServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private WithdrawalNoticeRepository withdrawalNoticeRepository;

    @InjectMocks
    private WithdrawalService withdrawalService;

    private Investor youngInvestor;
    private Investor retiredInvestor;
    private Product retirementProduct;
    private Product unitTrustProduct;

    @BeforeEach
    void setUp() {
        youngInvestor = new Investor("Lerato", "Dlamini", "lerato@example.com", LocalDate.now().minusYears(36));
        youngInvestor.setId(2L);

        retiredInvestor = new Investor("Thabo", "Mokoena", "thabo@example.com", LocalDate.now().minusYears(71));
        retiredInvestor.setId(1L);

        Portfolio retiredPortfolio = new Portfolio(retiredInvestor);
        retirementProduct = new Product(retiredPortfolio, "Retirement Annuity", ProductType.RETIREMENT_ANNUITY,
                new BigDecimal("500000.00"));
        retirementProduct.setId(1L);

        Portfolio youngPortfolio = new Portfolio(youngInvestor);
        unitTrustProduct = new Product(youngPortfolio, "Unit Trust", ProductType.UNIT_TRUST,
                new BigDecimal("100000.00"));
        unitTrustProduct.setId(2L);
    }

    @Test
    void rejectsRetirementWithdrawal_whenInvestorIsUnder65() {
        Portfolio youngPortfolio = new Portfolio(youngInvestor);
        Product youngRetirementProduct = new Product(youngPortfolio, "Retirement Annuity",
                ProductType.RETIREMENT_ANNUITY, new BigDecimal("300000.00"));
        youngRetirementProduct.setId(3L);

        WithdrawalRequestDto request = new WithdrawalRequestDto();
        request.setProductId(3L);
        request.setAmount(new BigDecimal("1000"));

        when(productRepository.findById(3L)).thenReturn(Optional.of(youngRetirementProduct));

        assertThatThrownBy(() -> withdrawalService.createWithdrawal(request))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("over the age of 65");
    }

    @Test
    void allowsRetirementWithdrawal_whenInvestorIsOver65() {
        WithdrawalRequestDto request = new WithdrawalRequestDto();
        request.setProductId(1L);
        request.setAmount(new BigDecimal("10000"));

        when(productRepository.findById(1L)).thenReturn(Optional.of(retirementProduct));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));
        when(withdrawalNoticeRepository.save(any(WithdrawalNotice.class))).thenAnswer(inv -> {
            WithdrawalNotice n = inv.getArgument(0);
            n.setId(99L);
            return n;
        });

        var response = withdrawalService.createWithdrawal(request);

        assertThat(response.getStatus()).isEqualTo(WithdrawalStatus.APPROVED);
        assertThat(response.getBalanceAfter()).isEqualByComparingTo("490000.00");
    }

    @Test
    void rejectsWithdrawal_whenAmountExceedsBalance() {
        WithdrawalRequestDto request = new WithdrawalRequestDto();
        request.setProductId(2L);
        request.setAmount(new BigDecimal("150000"));

        when(productRepository.findById(2L)).thenReturn(Optional.of(unitTrustProduct));

        assertThatThrownBy(() -> withdrawalService.createWithdrawal(request))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("exceeds the available balance");
    }

    @Test
    void rejectsWithdrawal_whenAmountExceeds90PercentOfBalance() {
        WithdrawalRequestDto request = new WithdrawalRequestDto();
        request.setProductId(2L);
        request.setAmount(new BigDecimal("95000")); // balance is 100000, 90% = 90000

        when(productRepository.findById(2L)).thenReturn(Optional.of(unitTrustProduct));

        assertThatThrownBy(() -> withdrawalService.createWithdrawal(request))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("90%");
    }

    @Test
    void allowsWithdrawal_whenAmountIsExactly90PercentOfBalance() {
        WithdrawalRequestDto request = new WithdrawalRequestDto();
        request.setProductId(2L);
        request.setAmount(new BigDecimal("90000.00")); // exactly 90%

        when(productRepository.findById(2L)).thenReturn(Optional.of(unitTrustProduct));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));
        when(withdrawalNoticeRepository.save(any(WithdrawalNotice.class))).thenAnswer(inv -> {
            WithdrawalNotice n = inv.getArgument(0);
            n.setId(100L);
            return n;
        });

        var response = withdrawalService.createWithdrawal(request);

        assertThat(response.getStatus()).isEqualTo(WithdrawalStatus.APPROVED);
    }
}
