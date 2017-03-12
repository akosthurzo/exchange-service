package com.sample.exchange.controller;

import com.sample.exchange.model.CurrencyRate;
import com.sample.exchange.repository.CurrencyRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Optional;

/**
 * @author Akos Thurzo
 */
@RestController
@RequestMapping("/api")
public class CurrencyRateRestController {

    @Autowired
    private CurrencyRateRepository currencyRateRepository;

    @RequestMapping("/currencyRates")
    public Iterable<CurrencyRate> findAll(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                          @RequestParam(value = "date") Optional<LocalDate> date,
                                          @RequestParam(value = "currency") Optional<String> currency) {

        if (date.isPresent() && currency.isPresent())
            return currencyRateRepository.findByDateAndCurrency(date.get(), currency.get());

        if (!date.isPresent() && !currency.isPresent())
            return currencyRateRepository.findAll();

        if (date.isPresent())
            return currencyRateRepository.findByDate(date.get());

        return currencyRateRepository.findByCurrency(currency.get());
    }
}
