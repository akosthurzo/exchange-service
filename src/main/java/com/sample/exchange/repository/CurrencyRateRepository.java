package com.sample.exchange.repository;

import com.sample.exchange.model.CurrencyRate;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Akos Thurzo
 */
public interface CurrencyRateRepository extends PagingAndSortingRepository<CurrencyRate, Long> {

    public List<CurrencyRate> findByCurrency(String currency);

    public List<CurrencyRate> findByDay(LocalDate day);

    public List<CurrencyRate> findByDayAndCurrency(LocalDate day, String currency);
}
