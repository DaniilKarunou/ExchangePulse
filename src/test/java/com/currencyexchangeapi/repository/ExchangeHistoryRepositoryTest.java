package com.currencyexchangeapi.repository;

import com.currencyexchangeapi.model.ExchangeHistory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Klasa testowa ExchangeHistoryRepositoryTest zawiera testy jednostkowe
 * dla funkcji dostępu do bazy danych związanymi z ExchangeHistoryRepository.
 */
@DataJpaTest
public class ExchangeHistoryRepositoryTest {

    @Autowired
    private ExchangeHistoryRepository exchangeHistoryRepository;

    /**
     * Metoda testująca testFindByTimestampBetweenAndAmountGreaterThanEqual sprawdza,
     * czy metoda findByTimestampBetweenAndAmountGreaterThanEqual w repozytorium działa poprawnie.
     */
    @Test
    public void testFindByTimestampBetweenAndAmountGreaterThanEqual() {

        LocalDateTime startOfMonth = LocalDateTime.now().withYear(2023).withMonth(10).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        double amount = 10000.0;

        List<ExchangeHistory> result = exchangeHistoryRepository.findByTimestampBetweenAndAmountGreaterThanEqual(startOfMonth, endOfMonth, amount);

        assertEquals(4, result.size());
    }

    /**
     * Metoda testująca testCountExchangesByCurrencyFrom sprawdza,
     * czy metoda countExchangesByCurrencyFrom w repozytorium działa poprawnie.
     */
    @Test
    public void testCountExchangesByCurrencyFrom() {
        LocalDateTime startOfMonth = LocalDateTime.now().withYear(2023).withMonth(10).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);

        List<Object[]> result = exchangeHistoryRepository.countExchangesByCurrencyFrom(startOfMonth, endOfMonth);

        assertEquals(9L, result.get(0)[1]);
    }

    /**
     * Metoda testująca testCountExchangesByCurrencyTo sprawdza,
     * czy metoda countExchangesByCurrencyTo w repozytorium działa poprawnie.
     */
    @Test
    public void testCountExchangesByCurrencyTo() {
        LocalDateTime startOfMonth = LocalDateTime.now().withYear(2023).withMonth(10).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);

        List<Object[]> result = exchangeHistoryRepository.countExchangesByCurrencyTo(startOfMonth, endOfMonth);

        assertEquals(9L, result.get(0)[1]);
    }
}