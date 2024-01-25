package com.currencyexchangeapi.service;

import com.currencyexchangeapi.model.ExchangeHistory;
import com.currencyexchangeapi.repository.ExchangeHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Serwis obsługujący historię wymiany walut.
 * Zarządza operacjami pobierania, zapisywania oraz analizy historii wymiany walut.
 */
@Service
public class ExchangeHistoryService {

    private final ExchangeHistoryRepository historyRepository;

    /**
     * Konstruktor klasy ExchangeHistoryService.
     *
     * @param historyRepository Repozytorium obsługujące historię wymiany walut.
     */
    @Autowired
    public ExchangeHistoryService(ExchangeHistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    /**
     * Pobiera stronę z historią wymiany walut zgodnie z podaną paginacją.
     *
     * @param pageable Parametry paginacji.
     * @return Strona zawierająca historię wymiany walut.
     */
    public Page<ExchangeHistory> getPaginatedExchangeHistory(Pageable pageable) {
        return historyRepository.findAll(pageable);
    }

    /**
     * Pobiera listę wymian walut o dużej kwocie z ostatniego miesiąca.
     *
     * @return Lista wymian walut o wysokiej kwocie z ostatniego miesiąca.
     */
    public List<ExchangeHistory> getHighAmountExchangesLastMonth(double amount) {
        LocalDateTime startOfMonth = LocalDateTime.now().minusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);

        return historyRepository.findByTimestampBetweenAndAmountGreaterThanEqual(startOfMonth, endOfMonth, amount);
    }

    /**
     * Pobiera mapę z ilością wymian walut według waluty źródłowej z ostatniego miesiąca.
     *
     * @return Mapa zawierająca ilości wymian walut według waluty źródłowej z ostatniego miesiąca.
     */
    public Map<String, Long> getExchangesByCurrencyFromLastMonth() {
        LocalDateTime startOfMonth = LocalDateTime.now().minusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);

        List<Object[]> results = historyRepository.countExchangesByCurrencyFrom(startOfMonth, endOfMonth);

        Map<String, Long> exchangesByCurrencyFrom = new HashMap<>();

        for (Object[] result : results) {
            String currencyFrom = (String) result[0];
            Long count = (Long) result[1];
            exchangesByCurrencyFrom.put(currencyFrom, count);
        }

        return exchangesByCurrencyFrom;
    }

    /**
     * Pobiera mapę z ilością wymian walut według waluty docelowej z ostatniego miesiąca.
     *
     * @return Mapa zawierająca ilości wymian walut według waluty docelowej z ostatniego miesiąca.
     */
    public Map<String, Long> getExchangesByCurrencyToLastMonth() {
        LocalDateTime startOfMonth = LocalDateTime.now().minusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);

        List<Object[]> results = historyRepository.countExchangesByCurrencyTo(startOfMonth, endOfMonth);

        Map<String, Long> exchangesByCurrencyTo = new HashMap<>();

        for (Object[] result : results) {
            String currencyTo = (String) result[0];
            Long count = (Long) result[1];
            exchangesByCurrencyTo.put(currencyTo, count);
        }

        return exchangesByCurrencyTo;
    }

    /**
     * Zapisuje historię pojedynczej wymiany walut.
     *
     * @param exchangeHistory Obiekt ExchangeHistory do zapisania.
     */
    public ExchangeHistory save(ExchangeHistory exchangeHistory) {
        exchangeHistory.setTimestamp(LocalDateTime.now());
        return historyRepository.save(exchangeHistory);
    }
}