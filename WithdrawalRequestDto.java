package com.enviro.assessment.junior.nomdumiso.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/** Incoming payload for POST /api/withdrawals. */
public class WithdrawalRequestDto {

    @NotNull(message = "productId is required")
    private Long productId;

    @NotNull(message = "amount is required")
    @DecimalMin(value = "0.01", message = "amount must be greater than zero")
    private BigDecimal amount;

    public WithdrawalRequestDto() {
    }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}
