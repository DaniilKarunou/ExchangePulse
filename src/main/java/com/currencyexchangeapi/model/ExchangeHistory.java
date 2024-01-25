package com.currencyexchangeapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * Encja reprezentująca historię wymiany walut.
 * Klasa przechowuje informacje o pojedynczej wymianie walutowej.
 */
@Entity
@Table(name = "exchange_history")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExchangeHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long amount;
    private String currencyFrom;
    private String currencyTo;

    @Column(name = "exchange_timestamp")
    private LocalDateTime timestamp;

    /**
     * Konstruktor klasy ExchangeHistory.
     *
     * @param amount       Kwota wymiany walutowej.
     * @param currencyFrom Waluta źródłowa.
     * @param currencyTo   Waluta docelowa.
     */
    public ExchangeHistory(Long amount, String currencyFrom, String currencyTo) {
        this.amount = amount;
        this.currencyFrom = currencyFrom;
        this.currencyTo = currencyTo;
    }
}