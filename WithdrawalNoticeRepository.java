package com.enviro.assessment.junior.nomdumiso.repository;

import com.enviro.assessment.junior.nomdumiso.entity.WithdrawalNotice;
import com.enviro.assessment.junior.nomdumiso.entity.WithdrawalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface WithdrawalNoticeRepository extends JpaRepository<WithdrawalNotice, Long> {

    @Query("SELECT w FROM WithdrawalNotice w WHERE " +
           "(:investorId IS NULL OR w.product.portfolio.investor.id = :investorId) AND " +
           "(:status IS NULL OR w.status = :status) AND " +
           "(:from IS NULL OR w.requestedAt >= :from) AND " +
           "(:to IS NULL OR w.requestedAt <= :to) " +
           "ORDER BY w.requestedAt DESC")
    List<WithdrawalNotice> search(@Param("investorId") Long investorId,
                                   @Param("status") WithdrawalStatus status,
                                   @Param("from") LocalDateTime from,
                                   @Param("to") LocalDateTime to);
}
