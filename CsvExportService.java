package com.enviro.assessment.junior.nomdumiso.service;

import com.enviro.assessment.junior.nomdumiso.entity.WithdrawalNotice;
import com.enviro.assessment.junior.nomdumiso.entity.WithdrawalStatus;
import com.enviro.assessment.junior.nomdumiso.repository.WithdrawalNoticeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Builds CSV withdrawal statements, with optional filtering by investor,
 * status and date range, as required by "Export CSV statements with filtering".
 */
@Service
public class CsvExportService {

    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String[] HEADERS = {
            "Withdrawal ID", "Product", "Amount", "Balance Before", "Balance After",
            "Status", "Rejection Reason", "Requested At"
    };

    private final WithdrawalNoticeRepository withdrawalNoticeRepository;

    public CsvExportService(WithdrawalNoticeRepository withdrawalNoticeRepository) {
        this.withdrawalNoticeRepository = withdrawalNoticeRepository;
    }

    public String generateCsv(Long investorId, WithdrawalStatus status, LocalDateTime from, LocalDateTime to) {
        List<WithdrawalNotice> notices = withdrawalNoticeRepository.search(investorId, status, from, to);

        StringBuilder csv = new StringBuilder();
        csv.append(String.join(",", HEADERS)).append("\n");

        for (WithdrawalNotice notice : notices) {
            csv.append(escape(notice.getId())).append(",")
               .append(escape(notice.getProduct().getName())).append(",")
               .append(escape(notice.getAmount())).append(",")
               .append(escape(notice.getBalanceBefore())).append(",")
               .append(escape(notice.getBalanceAfter())).append(",")
               .append(escape(notice.getStatus())).append(",")
               .append(escape(notice.getRejectionReason() == null ? "" : notice.getRejectionReason())).append(",")
               .append(escape(notice.getRequestedAt().format(TIMESTAMP_FORMAT)))
               .append("\n");
        }

        return csv.toString();
    }

    /** Wraps a field in quotes and escapes embedded quotes to keep the CSV well formed. */
    private String escape(Object value) {
        String str = String.valueOf(value);
        if (str.contains(",") || str.contains("\"") || str.contains("\n")) {
            str = "\"" + str.replace("\"", "\"\"") + "\"";
        }
        return str;
    }
}
