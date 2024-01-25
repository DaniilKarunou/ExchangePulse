package com.currencyexchangeapi.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Klasa obsługująca wyjątki związane z wymianą walut w aplikacji.
 * Jest to komponent globalny obsługujący wyjątki zgodnie z typem ExchangeException.
 */
@ControllerAdvice
public class ExchangeExceptionHandler {

    /**
     * Obsługuje niestandardowy wyjątek ExchangeException, zwracając odpowiedź HTTP.
     *
     * @param ex Obiekt ExchangeException, który został przechwycony.
     * @return Odpowiedź HTTP z odpowiednim kodem statusu i wiadomością błędu.
     */
    @ExceptionHandler(ExchangeException.class)
    public ResponseEntity<String> handleCustomExchangeException(ExchangeException ex) {
        return ResponseEntity.status(ex.getHttpStatus()).body(ex.getMessage());
    }
}
