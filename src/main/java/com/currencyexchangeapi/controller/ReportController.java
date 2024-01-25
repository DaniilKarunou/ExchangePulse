package com.currencyexchangeapi.controller;

import com.currencyexchangeapi.model.ExchangeHistory;
import com.currencyexchangeapi.model.ReportType;
import com.currencyexchangeapi.service.ExchangeHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

/**
 * Kontroler obsługujący generowanie raportów związanych z historią wymiany walut.
 * Udostępnia endpointy do generowania różnych raportów opartych na historii wymiany walut.
 */
@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

    private final ExchangeHistoryService historyService;

    /**
     * Konstruktor kontrolera raportów.
     *
     * @param historyService Serwis obsługujący historię wymiany walut.
     */
    @Autowired
    public ReportController(ExchangeHistoryService historyService) {
        this.historyService = historyService;
    }

    /**
     * Generuje raport na podstawie żądanego typu.
     *
     * @param reportName Nazwa raportu.
     * @return Odpowiedź HTTP z raportem lub komunikatem o błędzie.
     */
    @GetMapping("/{reportName}")
    public ResponseEntity<?> generateReport(@PathVariable String reportName, @Param("amount") double amount) {
        ReportType type;
        try {
            type = ReportType.valueOf(reportName);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>("Invalid report name", HttpStatus.BAD_REQUEST);
        }

        switch (type) {
            case HIGH_AMOUNT_LAST_MONTH:
                List<ExchangeHistory> highAmountExchanges = historyService.getHighAmountExchangesLastMonth(amount);
                return new ResponseEntity<>(highAmountExchanges, HttpStatus.OK);

            case GROUP_BY_CURRENCY_FROM_LAST_MONTH:
                Map<String, Long> exchangesByCurrencyFrom = historyService.getExchangesByCurrencyFromLastMonth();
                return new ResponseEntity<>(exchangesByCurrencyFrom, HttpStatus.OK);

            case GROUP_BY_CURRENCY_TO_LAST_MONTH:
                Map<String, Long> exchangesByCurrencyTo = historyService.getExchangesByCurrencyToLastMonth();
                return new ResponseEntity<>(exchangesByCurrencyTo, HttpStatus.OK);

            default:
                return new ResponseEntity<>("Invalid report name", HttpStatus.BAD_REQUEST);
        }
    }
}