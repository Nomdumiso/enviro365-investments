package com.enviro.assessment.junior.nomdumiso.repository;

import com.enviro.assessment.junior.nomdumiso.entity.Investor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvestorRepository extends JpaRepository<Investor, Long> {
}
