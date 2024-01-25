package com.currencyexchangeapi.controller;

import com.currencyexchangeapi.model.ExchangeHistory;
import com.currencyexchangeapi.model.dto.ExchangeRequest;
import com.currencyexchangeapi.model.dto.ExchangeResponse;
import com.currencyexchangeapi.service.ExchangeHistoryService;
import com.currencyexchangeapi.service.ExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Kontroler obsługujący operacje związane z wymianą walut oraz historią wymian.
 * Udostępnia endpointy do wykonania wymiany walut oraz pobrania historii wymian.
 */
@RestController
@RequestMapping("/api/v1/calculator")
public class ExchangeController {

    private final ExchangeService exchangeService;
    private final ExchangeHistoryService historyService;

    /**
     * Konstruktor kontrolera wymiany walut.
     *
     * @param exchangeService Serwis obsługujący operacje wymiany walut.
     * @param historyService  Serwis obsługujący historię wymian walut.
     */
    @Autowired
    public ExchangeController(ExchangeService exchangeService, ExchangeHistoryService historyService) {
        this.exchangeService = exchangeService;
        this.historyService = historyService;
    }

    /**
     * Wykonuje operację wymiany walut na podstawie żądania.
     * Dodatkowo zapisuje informacje o wymianie w historii.
     *
     * @param exchangeRequest Żądanie wymiany walut.
     * @return Odpowiedź HTTP z wynikiem wymiany walut.
     */
    @PostMapping("/_exchange")
    public ResponseEntity<ExchangeResponse> performExchange(@RequestBody ExchangeRequest exchangeRequest) {
        ExchangeResponse exchangeResponse = exchangeService.performExchange(exchangeRequest);
        ExchangeHistory exchangeHistory = new ExchangeHistory(
                exchangeRequest.getAmount(),
                exchangeRequest.getCurrencyFrom(),
                exchangeRequest.getCurrencyTo());
        historyService.save(exchangeHistory);
        return new ResponseEntity<>(exchangeResponse, HttpStatus.OK);
    }

    /**
     * Pobiera historię wymian walut w paginowanej formie.
     *
     * @param page Numer strony.
     *             Domyślnie: 0.
     * @param size Rozmiar strony.
     *             Domyślnie: 10.
     * @param sort Pole, po którym ma być posortowana historia wymian.
     *             Domyślnie: id.
     * @return Odpowiedź HTTP z paginowaną historią wymian walut.
     */
    @GetMapping("/exchanges")
    public ResponseEntity<Page<ExchangeHistory>> getExchangeHistory(@RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "10") int size,
                                                                    @RequestParam(defaultValue = "id") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<ExchangeHistory> exchangeHistoryPage = historyService.getPaginatedExchangeHistory(pageable);
        return new ResponseEntity<>(exchangeHistoryPage, HttpStatus.OK);
    }
}