package com.enviro.assessment.junior.nomdumiso.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "portfolios")
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "investor_id", nullable = false, unique = true)
    private Investor investor;

    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products = new ArrayList<>();

    public Portfolio() {
    }

    public Portfolio(Investor investor) {
        this.investor = investor;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Investor getInvestor() { return investor; }
    public void setInvestor(Investor investor) { this.investor = investor; }
    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }
}
