package com.currencyexchangeapi.model.dto;

import lombok.*;

/**
 * Klasa reprezentująca żądanie wymiany walut.
 * Zawiera informacje o kwocie, walucie źródłowej i docelowej.
 */
@Data
public class ExchangeRequest {
    private Long amount;
    private String currencyFrom;
    private String currencyTo;
}

