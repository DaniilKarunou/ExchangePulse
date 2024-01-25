package com.currencyexchangeapi.service;

import com.currencyexchangeapi.exception.ExchangeException;
import com.currencyexchangeapi.model.dto.ExchangeRequest;
import com.currencyexchangeapi.model.dto.ExchangeResponse;
import com.currencyexchangeapi.model.dto.ExchangeApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

/**
 * Serwis do obsługi wymiany walut.
 *
 * <p>Wykorzystuje RestTemplate do komunikacji z zewnętrznym API do przeliczania walut.
 */
@Service
public class ExchangeService {

    private final RestTemplate restTemplate;

    @Value("${external.api.url}")
    private String externalApiUrl;

    /**
     * Konstruktor klasy ExchangeService.
     *
     * @param restTemplate Obiekt RestTemplate do wykonywania zapytań HTTP.
     */
    public ExchangeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Metoda wykonująca przeliczenie walut na podstawie żądania wymiany.
     *
     * @param exchangeRequest Obiekt ExchangeRequest zawierający informacje o wymianie walut.
     * @return Obiekt ExchangeResponse zawierający przeliczoną kwotę wymiany.
     * @throws ExchangeException W przypadku błędu podczas przeliczania walut lub odpowiedzi zewnętrznego API.
     */
    public ExchangeResponse performExchange(ExchangeRequest exchangeRequest) throws ExchangeException {
        try {
            ExchangeApiResponse apiResponse = restTemplate.getForObject(externalApiUrl, ExchangeApiResponse.class);

            double exchangedAmount;

            if (apiResponse != null && apiResponse.getRates() != null) {
                Map<String, Double> rates = apiResponse.getRates();

                Double rateFrom = rates.get(exchangeRequest.getCurrencyFrom());
                Double rateTo = rates.get(exchangeRequest.getCurrencyTo());

                if (rateFrom != null && rateTo != null) {
                    exchangedAmount = (exchangeRequest.getAmount() * rateTo) / rateFrom;
                } else {
                    throw new ExchangeException("Invalid currency", HttpStatus.BAD_REQUEST);
                }
            } else {
                throw new ExchangeException("Invalid external API response", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ExchangeResponse(exchangedAmount);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            String responseBody = ex.getResponseBodyAsString();
            throw new ExchangeException("Error in performing exchange: " + responseBody, ex.getStatusCode());
        } catch (RestClientException ex) {
            throw new ExchangeException("Error in performing exchange: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}