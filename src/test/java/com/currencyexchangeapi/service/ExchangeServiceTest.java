package com.currencyexchangeapi.service;

import com.currencyexchangeapi.exception.ExchangeException;
import com.currencyexchangeapi.model.dto.ExchangeApiResponse;
import com.currencyexchangeapi.model.dto.ExchangeRequest;
import com.currencyexchangeapi.model.dto.ExchangeResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * Klasa testowa ExchangeServiceTest służy do testowania funkcjonalności klasy ExchangeService.
 */
@SpringBootTest
class ExchangeServiceTest {

    @Autowired
    private ExchangeService exchangeService;

    @MockBean
    private RestTemplate restTemplate;

    /**
     * Metoda testowa testPerformExchange sprawdza poprawność działania funkcji performExchange
     * w przypadku poprawnej odpowiedzi z zewnętrznego API.
     */
    @Test
    void testPerformExchange() {
        ExchangeRequest request = new ExchangeRequest();
        request.setCurrencyFrom("USD");
        request.setCurrencyTo("EUR");
        request.setAmount(100L);

        ExchangeApiResponse response = new ExchangeApiResponse();
        Map<String, Double> rates = new HashMap<>();
        rates.put("USD", 1.2);
        rates.put("EUR", 0.8);
        response.setRates(rates);

        when(restTemplate.getForObject(any(String.class), eq(ExchangeApiResponse.class))).thenReturn(response);

        ExchangeResponse exchangeResponse = exchangeService.performExchange(request);
        assertTrue(exchangeResponse.getAmount() > 0);
    }

    /**
     * Metoda testowa testPerformExchangeInvalidCurrency sprawdza obsługę wyjątku ExchangeException
     * w przypadku otrzymania niepoprawnej odpowiedzi z zewnętrznego API dotyczącej waluty.
     */
    @Test
    void testPerformExchangeInvalidCurrency() {
        ExchangeRequest request = new ExchangeRequest();
        request.setCurrencyFrom("USD");
        request.setCurrencyTo("EUR");
        request.setAmount(100L);

        ExchangeApiResponse response = new ExchangeApiResponse();
        Map<String, Double> rates = new HashMap<>();
        rates.put("USD", null);
        rates.put("EUR", null);
        response.setRates(rates);

        when(restTemplate.getForObject(any(String.class), eq(ExchangeApiResponse.class))).thenReturn(response);

        ExchangeException exception = assertThrows(ExchangeException.class, () -> {
            exchangeService.performExchange(request);
        });
        assertEquals("Invalid currency", exception.getMessage());
    }

    /**
     * Metoda testowa testPerformExchangeInvalidApiResponse sprawdza obsługę wyjątku ExchangeException
     * w przypadku otrzymania niepoprawnej odpowiedzi z zewnętrznego API.
     */
    @Test
    void testPerformExchangeInvalidApiResponse() {
        ExchangeRequest request = new ExchangeRequest();
        request.setCurrencyFrom("USD");
        request.setCurrencyTo("EUR");
        request.setAmount(100L);

        ExchangeApiResponse response = null;

        when(restTemplate.getForObject(any(String.class), eq(ExchangeApiResponse.class))).thenReturn(response);

        ExchangeException exception = assertThrows(ExchangeException.class, () -> {
            exchangeService.performExchange(request);
        });
        assertEquals("Invalid external API response", exception.getMessage());
    }
}