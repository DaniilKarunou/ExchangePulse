package com.currencyexchangeapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Konfiguracja aplikacji definiująca beany wymagane przez aplikację.
 */
@Configuration
public class AppConfig {

    /**
     * Tworzy i konfiguruje instancję RestTemplate.
     *
     * @return Gotowa do użycia instancja RestTemplate.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}