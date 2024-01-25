package com.currencyexchangeapi.repository;

import com.currencyexchangeapi.model.ExchangeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repozytorium obsługujące historię wymiany walut.
 * Interfejs zapewniający dostęp do operacji pobierania i zapisywania danych związanych z historią wymiany walut.
 */
@Repository
public interface ExchangeHistoryRepository extends JpaRepository<ExchangeHistory, Long> {

    /**
     * Pobiera listę wymian walut z określonego przedziału czasowego i o danej minimalnej kwocie.
     *
     * @param startOfMonth Data początkowa okresu czasowego.
     * @param endOfMonth   Data końcowa okresu czasowego.
     * @param amount       Minimalna kwota wymiany walut.
     * @return Lista wymian walut spełniających kryteria.
     */
    @Query("SELECT e FROM ExchangeHistory e " +
            "WHERE e.timestamp BETWEEN :startOfMonth AND :endOfMonth " +
            "AND e.amount >= :amount")
    List<ExchangeHistory> findByTimestampBetweenAndAmountGreaterThanEqual(LocalDateTime startOfMonth, LocalDateTime endOfMonth, double amount);

    /**
     * Zlicza ilość wymian walut według waluty źródłowej w określonym przedziale czasowym.
     *
     * @param startOfMonth Data początkowa okresu czasowego.
     * @param endOfMonth   Data końcowa okresu czasowego.
     * @return Lista tablic obiektów z walutą źródłową i odpowiadającą jej ilością wymian walut.
     */
    @Query("SELECT currencyFrom, COUNT(currencyFrom) FROM ExchangeHistory " +
            "WHERE timestamp BETWEEN :startOfMonth AND :endOfMonth " +
            "GROUP BY currencyFrom")
    List<Object[]> countExchangesByCurrencyFrom(@Param("startOfMonth") LocalDateTime startOfMonth,
                                                @Param("endOfMonth") LocalDateTime endOfMonth);

    /**
     * Zlicza ilość wymian walut według waluty docelowej w określonym przedziale czasowym.
     *
     * @param startOfMonth Data początkowa okresu czasowego.
     * @param endOfMonth   Data końcowa okresu czasowego.
     * @return Lista tablic obiektów z walutą docelową i odpowiadającą jej ilością wymian walut.
     */
    @Query("SELECT currencyTo, COUNT(currencyTo) FROM ExchangeHistory " +
            "WHERE timestamp BETWEEN :startOfMonth AND :endOfMonth " +
            "GROUP BY currencyTo")
    List<Object[]> countExchangesByCurrencyTo(@Param("startOfMonth") LocalDateTime startOfMonth,
                                              @Param("endOfMonth") LocalDateTime endOfMonth);
}