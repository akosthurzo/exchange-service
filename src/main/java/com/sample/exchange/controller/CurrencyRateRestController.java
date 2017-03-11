package com.sample.exchange.controller;

import com.sample.exchange.model.CurrencyRate;
import com.sample.exchange.repository.CurrencyRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Akos Thurzo
 */
@RestController
@RequestMapping("/api")
public class CurrencyRateRestController {

    @Autowired
    private CurrencyRateRepository currencyRateRepository;

    @RequestMapping("/currencyRates")
    public Iterable<CurrencyRate> findAll() {
        return currencyRateRepository.findAll();
    }
}
