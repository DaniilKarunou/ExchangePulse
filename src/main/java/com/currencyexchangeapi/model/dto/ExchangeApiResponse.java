package com.currencyexchangeapi.model.dto;

import lombok.*;

import java.util.Map;

/**
 * Klasa reprezentująca odpowiedź zewnętrznego API dotyczącą kursów wymiany walut.
 * Zawiera informacje o tabeli, kursach wymiany oraz ostatniej aktualizacji.
 */
@Data
public class ExchangeApiResponse {
    private String table;
    private Map<String, Double> rates;
    private String lastupdate;
}