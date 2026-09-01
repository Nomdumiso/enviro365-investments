package com.enviro.assessment.junior.nomdumiso.dto;

import com.enviro.assessment.junior.nomdumiso.entity.WithdrawalNotice;
import com.enviro.assessment.junior.nomdumiso.entity.WithdrawalStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class WithdrawalResponseDto {

    private Long id;
    private Long productId;
    private String productName;
    private BigDecimal amount;
    private BigDecimal balanceBefore;
    private BigDecimal balanceAfter;
    private WithdrawalStatus status;
    private String rejectionReason;
    private LocalDateTime requestedAt;

    public WithdrawalResponseDto() {
    }

    public static WithdrawalResponseDto fromEntity(WithdrawalNotice notice) {
        WithdrawalResponseDto dto = new WithdrawalResponseDto();
        dto.id = notice.getId();
        dto.productId = notice.getProduct().getId();
        dto.productName = notice.getProduct().getName();
        dto.amount = notice.getAmount();
        dto.balanceBefore = notice.getBalanceBefore();
        dto.balanceAfter = notice.getBalanceAfter();
        dto.status = notice.getStatus();
        dto.rejectionReason = notice.getRejectionReason();
        dto.requestedAt = notice.getRequestedAt();
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public BigDecimal getBalanceBefore() { return balanceBefore; }
    public void setBalanceBefore(BigDecimal balanceBefore) { this.balanceBefore = balanceBefore; }
    public BigDecimal getBalanceAfter() { return balanceAfter; }
    public void setBalanceAfter(BigDecimal balanceAfter) { this.balanceAfter = balanceAfter; }
    public WithdrawalStatus getStatus() { return status; }
    public void setStatus(WithdrawalStatus status) { this.status = status; }
    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
    public LocalDateTime getRequestedAt() { return requestedAt; }
    public void setRequestedAt(LocalDateTime requestedAt) { this.requestedAt = requestedAt; }
}
