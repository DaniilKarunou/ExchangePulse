package com.currencyexchangeapi.service;

import com.currencyexchangeapi.model.ExchangeHistory;
import com.currencyexchangeapi.repository.ExchangeHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Testy jednostkowe dla klasy ExchangeHistoryService.
 */
@RunWith(MockitoJUnitRunner.class)
public class ExchangeHistoryServiceTest {

    @Mock
    private ExchangeHistoryRepository historyRepository;

    @InjectMocks
    private ExchangeHistoryService exchangeHistoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    public Page<ExchangeHistory> generateMockExchangeHistoryPage() {
        List<ExchangeHistory> exchangeHistories = generateMockExchangeHistories(10);
        return new PageImpl<>(exchangeHistories);
    }

    /**
     * Generuje stronę symulowanych danych wymiany walut.
     *
     * @return Strona zawierająca symulowane dane wymiany walut.
     */
    public List<ExchangeHistory> generateMockExchangeHistories(int size) {

        List<ExchangeHistory> exchangeHistories = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            ExchangeHistory exchangeHistory = new ExchangeHistory();
            exchangeHistory.setAmount((long) i);
            exchangeHistory.setCurrencyFrom("USD");
            exchangeHistory.setCurrencyTo("EUR");
            exchangeHistories.add(exchangeHistory);
        }
        return exchangeHistories;
    }

    /**
     * Testuje zwracanie wymiany walut z paginacją.
     */
    @Test
    public void testGetPaginatedExchangeHistory() {

        Pageable pageable = PageRequest.of(1, 10);

        Page<ExchangeHistory> mockExchangeHistoryPage = generateMockExchangeHistoryPage();
        when(exchangeHistoryService.getPaginatedExchangeHistory(pageable)).thenReturn(mockExchangeHistoryPage);

        Page<ExchangeHistory> result = exchangeHistoryService.getPaginatedExchangeHistory(pageable);

        assertEquals(10, result.getSize());
        assertEquals(1, result.getTotalPages());
        assertEquals(10, result.getTotalElements());
        assertEquals(10, result.getContent().size());
    }

    /**
     * Testuje pobieranie wysokich kwot wymiany z ostatniego miesiąca.
     */
    @Test
    public void testGetHighAmountExchangesLastMonth() {

        List<ExchangeHistory> exchangeHistories = generateMockExchangeHistories(15);

        when(exchangeHistoryService.getHighAmountExchangesLastMonth(1500)).thenReturn(exchangeHistories);

        List<ExchangeHistory> result = exchangeHistoryService.getHighAmountExchangesLastMonth(1500);

        assertEquals(15, result.size());
    }

    /**
     * Testuje zapisywania daty przy zapisie do bazy danych ExchangeHistory.
     */
    @Test
    public void testOnSaveDate() {
        ExchangeHistory exchangeHistory = new ExchangeHistory();
        exchangeHistory.setAmount((long) 10);
        exchangeHistory.setCurrencyFrom("USD");
        exchangeHistory.setCurrencyTo("EUR");

        LocalDateTime beforeCall = LocalDateTime.now();

        when(exchangeHistoryService.save(exchangeHistory)).thenReturn(exchangeHistory);

        ExchangeHistory result = exchangeHistoryService.save(exchangeHistory);

        LocalDateTime timestamp = result.getTimestamp();

        assertEquals(beforeCall.getYear(), timestamp.getYear());
        assertEquals(beforeCall.getMonth(), timestamp.getMonth());
        assertEquals(beforeCall.getDayOfMonth(), timestamp.getDayOfMonth());
        assertEquals(beforeCall.getHour(), timestamp.getHour());
    }
}
