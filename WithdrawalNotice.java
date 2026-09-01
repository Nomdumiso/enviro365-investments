package com.enviro.assessment.junior.nomdumiso.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "withdrawal_notices")
public class WithdrawalNotice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal balanceBefore;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal balanceAfter;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WithdrawalStatus status;

    /** Populated when status == REJECTED, explaining which business rule failed. */
    private String rejectionReason;

    @Column(nullable = false)
    private LocalDateTime requestedAt;

    public WithdrawalNotice() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
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
