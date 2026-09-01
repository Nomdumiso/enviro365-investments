package com.enviro.assessment.junior.nomdumiso.service;

import com.enviro.assessment.junior.nomdumiso.dto.PortfolioDto;
import com.enviro.assessment.junior.nomdumiso.entity.Portfolio;
import com.enviro.assessment.junior.nomdumiso.exception.ResourceNotFoundException;
import com.enviro.assessment.junior.nomdumiso.repository.PortfolioRepository;
import org.springframework.stereotype.Service;

@Service
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;

    public PortfolioService(PortfolioRepository portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
    }

    /**
     * Retrieves an investor's portfolio (details + products), as required
     * by the "Retrieve investor portfolio" backend requirement.
     */
    public PortfolioDto getPortfolioByInvestorId(Long investorId) {
        Portfolio portfolio = portfolioRepository.findByInvestorId(investorId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No portfolio found for investor id " + investorId));
        return PortfolioDto.fromEntity(portfolio);
    }
}
