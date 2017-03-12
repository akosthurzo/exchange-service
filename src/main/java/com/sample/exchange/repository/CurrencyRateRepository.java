package com.sample.exchange.repository;

import com.sample.exchange.model.CurrencyRate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Akos Thurzo
 */
public interface CurrencyRateRepository extends PagingAndSortingRepository<CurrencyRate, Long> {

    public List<CurrencyRate> findByCurrency(String currency);

    public Page<CurrencyRate> findByCurrency(String currency, Pageable pageable);

    public List<CurrencyRate> findByDate(LocalDate date);

    public Page<CurrencyRate> findByDate(LocalDate date, Pageable pageable);

    public List<CurrencyRate> findByDateAndCurrency(LocalDate date, String currency);

    public Page<CurrencyRate> findByDateAndCurrency(LocalDate date, String currency, Pageable pageable);
}
