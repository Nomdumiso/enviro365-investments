package com.enviro.assessment.junior.nomdumiso.repository;

import com.enviro.assessment.junior.nomdumiso.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
