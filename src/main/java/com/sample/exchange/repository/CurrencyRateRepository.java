package com.sample.exchange.repository;

import com.sample.exchange.model.CurrencyRate;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Akos Thurzo
 */
public interface CurrencyRateRepository extends PagingAndSortingRepository<CurrencyRate, Long> {
}
