package com.enviro.assessment.junior.nomdumiso.controller;

import com.enviro.assessment.junior.nomdumiso.dto.PortfolioDto;
import com.enviro.assessment.junior.nomdumiso.service.PortfolioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/investors")
public class PortfolioController {

    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    /** GET /api/investors/{investorId}/portfolio - portfolio details + products. */
    @GetMapping("/{investorId}/portfolio")
    public ResponseEntity<PortfolioDto> getPortfolio(@PathVariable Long investorId) {
        return ResponseEntity.ok(portfolioService.getPortfolioByInvestorId(investorId));
    }
}
