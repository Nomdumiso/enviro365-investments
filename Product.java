package com.enviro.assessment.junior.nomdumiso.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "portfolio_id", nullable = false)
    private Portfolio portfolio;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductType type;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal balance;

    public Product() {
    }

    public Product(Portfolio portfolio, String name, ProductType type, BigDecimal balance) {
        this.portfolio = portfolio;
        this.name = name;
        this.type = type;
        this.balance = balance;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Portfolio getPortfolio() { return portfolio; }
    public void setPortfolio(Portfolio portfolio) { this.portfolio = portfolio; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public ProductType getType() { return type; }
    public void setType(ProductType type) { this.type = type; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
}
