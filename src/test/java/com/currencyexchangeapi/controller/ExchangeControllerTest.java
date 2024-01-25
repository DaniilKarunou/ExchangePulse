package com.currencyexchangeapi.controller;

import com.currencyexchangeapi.model.ExchangeHistory;
import com.currencyexchangeapi.model.dto.ExchangeRequest;
import com.currencyexchangeapi.model.dto.ExchangeResponse;
import com.currencyexchangeapi.service.ExchangeHistoryService;
import com.currencyexchangeapi.service.ExchangeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * Klasa testowa ExchangeControllerTest zawiera testy integracyjne dla kontrolera ExchangeController.
 * Wykorzystuje adnotacje Spring Boota (@SpringBootTest) oraz AutoConfigureMockMvc do testowania zachowania kontrolera.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class ExchangeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ExchangeService exchangeService;

    @Mock
    private ExchangeHistoryService historyService;

    @InjectMocks
    private ExchangeController exchangeController;

    /**
     * Metoda setup() jest oznaczona adnotacją @BeforeEach i inicjalizuje test.
     * Inicjuje pole mockMvc oraz konfiguruje kontroler do testowania przy użyciu MockMvcBuilders.
     */
    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        mockMvc = MockMvcBuilders.standaloneSetup(exchangeController).build();
    }

    /**
     * Metoda testująca testPerformExchange sprawdza działanie endpointu odpowiedzialnego za wymianę walut.
     * Wykorzystuje mockMvc do wywołania żądania HTTP POST na odpowiednim adresie i sprawdza oczekiwaną odpowiedź.
     */
    @Test
    public void testPerformExchange() throws Exception {
        ExchangeRequest request = new ExchangeRequest();
        ExchangeResponse response = new ExchangeResponse();

        given(exchangeService.performExchange(request)).willReturn(response);

        mockMvc.perform(post("/api/v1/calculator/_exchange")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").isNumber());

        verify(historyService, times(1)).save(any(ExchangeHistory.class));
    }

    /**
     * Metoda testująca testGetExchangeHistory sprawdza działanie endpointu odpowiedzialnego za pobieranie historii wymiany walut.
     * Wykorzystuje mockMvc do wywołania żądania HTTP GET na odpowiednim adresie i sprawdza oczekiwaną odpowiedź.
     */
    @Test
    public void testGetExchangeHistory() throws Exception {
        Page<ExchangeHistory> exchangeHistoryPage = generateRandomExchangeHistoryPage(20);
        when(historyService.getPaginatedExchangeHistory(any(Pageable.class))).thenReturn(exchangeHistoryPage);

        mockMvc.perform(get("/api/v1/calculator/exchanges")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "20")
                        .param("sort", "timestamp,desc")
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].amount").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].currencyFrom").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].currencyTo").isString());
    }

    /**
     * Generuje stronę ExchangeHistory z losowymi danymi.
     */
    public Page<ExchangeHistory> generateRandomExchangeHistoryPage(int size) {
        List<ExchangeHistory> exchangeHistoryList = generateRandomExchangeHistoryList(size);
        PageRequest pageRequest = PageRequest.of(0, size);
        return new PageImpl<>(exchangeHistoryList, pageRequest, exchangeHistoryList.size());
    }

    /**
     * Metoda tworzy listę obiektów ExchangeHistory z losowymi danymi.
     */
    private List<ExchangeHistory> generateRandomExchangeHistoryList(int size) {
        List<ExchangeHistory> exchangeHistoryList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            ExchangeHistory exchangeHistory = createRandomExchangeHistory();
            exchangeHistoryList.add(exchangeHistory);
        }
        return exchangeHistoryList;
    }

    /**
     * Metoda tworzy obiekt ExchangeHistory z losowymi danymi.
     */
    private ExchangeHistory createRandomExchangeHistory() {
        Random random = new Random();
        ExchangeHistory exchangeHistory = new ExchangeHistory();
        exchangeHistory.setAmount(random.nextLong());
        exchangeHistory.setCurrencyFrom("USD");
        exchangeHistory.setCurrencyTo("EUR");
        return exchangeHistory;
    }

    /**
     * Konwertuje obiekt na JSON.
     */
    private String asJsonString(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
}