package com.enviro.assessment.junior.nomdumiso.dto;

import com.enviro.assessment.junior.nomdumiso.entity.Product;
import com.enviro.assessment.junior.nomdumiso.entity.ProductType;
import java.math.BigDecimal;

/** Read-only view of a Product returned to the frontend. */
public class ProductDto {

    private Long id;
    private String name;
    private ProductType type;
    private BigDecimal balance;

    public ProductDto() {
    }

    public static ProductDto fromEntity(Product product) {
        ProductDto dto = new ProductDto();
        dto.id = product.getId();
        dto.name = product.getName();
        dto.type = product.getType();
        dto.balance = product.getBalance();
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public ProductType getType() { return type; }
    public void setType(ProductType type) { this.type = type; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
}
