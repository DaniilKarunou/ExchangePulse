package com.currencyexchangeapi.model.dto;

import lombok.*;

/**
 * Klasa reprezentująca odpowiedź na żądanie wymiany walut.
 * Zawiera informację o przeliczonej kwocie.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeResponse {
    private double amount;
}
