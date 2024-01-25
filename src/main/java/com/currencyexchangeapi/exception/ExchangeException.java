package com.currencyexchangeapi.exception;

import org.springframework.http.HttpStatusCode;

/**
 * Wyjątek rzucany w przypadku problemów z wymianą walut w aplikacji.
 * Rozszerza klasę RuntimeException i przechowuje informacje o statusie HTTP związanym z wyjątkiem.
 */
public class ExchangeException extends RuntimeException {

    /**
     * Status HTTP związany z wyjątkiem.
     */
    private final HttpStatusCode httpStatus;

    /**
     * Konstruktor wyjątku wymiany walut.
     *
     * @param message    Komunikat opisujący wyjątek.
     * @param httpStatus Status HTTP związany z wyjątkiem.
     */
    public ExchangeException(String message, HttpStatusCode httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    /**
     * Pobiera status HTTP związany z wyjątkiem.
     *
     * @return Status HTTP.
     */
    public HttpStatusCode getHttpStatus() {
        return httpStatus;
    }
}