package com.enviro.assessment.junior.nomdumiso.controller;

import com.enviro.assessment.junior.nomdumiso.dto.WithdrawalRequestDto;
import com.enviro.assessment.junior.nomdumiso.dto.WithdrawalResponseDto;
import com.enviro.assessment.junior.nomdumiso.entity.WithdrawalStatus;
import com.enviro.assessment.junior.nomdumiso.service.CsvExportService;
import com.enviro.assessment.junior.nomdumiso.service.WithdrawalService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/withdrawals")
public class WithdrawalController {

    private final WithdrawalService withdrawalService;
    private final CsvExportService csvExportService;

    public WithdrawalController(WithdrawalService withdrawalService, CsvExportService csvExportService) {
        this.withdrawalService = withdrawalService;
        this.csvExportService = csvExportService;
    }

    /** POST /api/withdrawals - create a withdrawal notice (validated against business rules). */
    @PostMapping
    public ResponseEntity<WithdrawalResponseDto> createWithdrawal(@Valid @RequestBody WithdrawalRequestDto request) {
        WithdrawalResponseDto response = withdrawalService.createWithdrawal(request);
        return ResponseEntity.ok(response);
    }

    /** GET /api/withdrawals - withdrawal history, filterable by investor, status and date range. */
    @GetMapping
    public ResponseEntity<List<WithdrawalResponseDto>> search(
            @RequestParam(required = false) Long investorId,
            @RequestParam(required = false) WithdrawalStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return ResponseEntity.ok(withdrawalService.search(investorId, status, from, to));
    }

    /** GET /api/withdrawals/export/csv - downloadable CSV statement, same filters as search. */
    @GetMapping("/export/csv")
    public ResponseEntity<String> exportCsv(
            @RequestParam(required = false) Long investorId,
            @RequestParam(required = false) WithdrawalStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {

        String csv = csvExportService.generateCsv(investorId, status, from, to);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"withdrawal-statement.csv\"");
        headers.setContentType(MediaType.parseMediaType("text/csv"));

        return ResponseEntity.ok().headers(headers).body(csv);
    }
}
