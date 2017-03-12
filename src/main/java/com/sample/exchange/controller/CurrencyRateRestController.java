package com.sample.exchange.controller;

import com.sample.exchange.model.CurrencyRate;
import com.sample.exchange.repository.CurrencyRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    @RequestMapping(value = "/currencyRates", method = RequestMethod.GET)
    public ResponseEntity<Iterable<CurrencyRate>> currencyRates(
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @RequestParam(value = "date") Optional<LocalDate> date,
            @RequestParam(value = "currency") Optional<String> currency) {

        Iterable<CurrencyRate> ret;

        if (date.isPresent() && currency.isPresent())
            ret = currencyRateRepository.findByDateAndCurrency(date.get(), currency.get());
        else if (!date.isPresent() && !currency.isPresent())
            ret = currencyRateRepository.findAll();
        else if (date.isPresent())
            ret = currencyRateRepository.findByDate(date.get());
        else
            ret = currencyRateRepository.findByCurrency(currency.get());

        if (ret.iterator().hasNext())
            return new ResponseEntity<Iterable<CurrencyRate>>(ret, HttpStatus.OK);

        return ResponseEntity.notFound().build();
    }
}
