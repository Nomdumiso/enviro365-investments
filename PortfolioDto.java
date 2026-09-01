package com.enviro.assessment.junior.nomdumiso.dto;

import com.enviro.assessment.junior.nomdumiso.entity.Portfolio;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/** Combines investor details with their product holdings, as required by the task. */
public class PortfolioDto {

    private Long portfolioId;
    private Long investorId;
    private String investorName;
    private String email;
    private int age;
    private List<ProductDto> products;
    private BigDecimal totalBalance;

    public PortfolioDto() {
    }

    public static PortfolioDto fromEntity(Portfolio portfolio) {
        PortfolioDto dto = new PortfolioDto();
        dto.portfolioId = portfolio.getId();
        dto.investorId = portfolio.getInvestor().getId();
        dto.investorName = portfolio.getInvestor().getFirstName() + " " + portfolio.getInvestor().getLastName();
        dto.email = portfolio.getInvestor().getEmail();
        dto.age = portfolio.getInvestor().getAge();
        dto.products = portfolio.getProducts().stream()
                .map(ProductDto::fromEntity)
                .collect(Collectors.toList());
        dto.totalBalance = dto.products.stream()
                .map(ProductDto::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return dto;
    }

    public Long getPortfolioId() { return portfolioId; }
    public void setPortfolioId(Long portfolioId) { this.portfolioId = portfolioId; }
    public Long getInvestorId() { return investorId; }
    public void setInvestorId(Long investorId) { this.investorId = investorId; }
    public String getInvestorName() { return investorName; }
    public void setInvestorName(String investorName) { this.investorName = investorName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public List<ProductDto> getProducts() { return products; }
    public void setProducts(List<ProductDto> products) { this.products = products; }
    public BigDecimal getTotalBalance() { return totalBalance; }
    public void setTotalBalance(BigDecimal totalBalance) { this.totalBalance = totalBalance; }
}
