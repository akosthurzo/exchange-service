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
                                          @RequestParam(value = "day") Optional<LocalDate> day,
                                          @RequestParam(value = "currency") Optional<String> currency) {

        if (day.isPresent() && currency.isPresent())
            return currencyRateRepository.findByDayAndCurrency(day.get(), currency.get());

        if (!day.isPresent() && !currency.isPresent())
            return currencyRateRepository.findAll();

        if (day.isPresent())
            return currencyRateRepository.findByDay(day.get());

        return currencyRateRepository.findByCurrency(currency.get());
    }
}
