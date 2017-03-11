package com.sample.exchange.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sample.exchange.util.LocalDateSerializer;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

/**
 * @author Akos Thurzo
 */
@Entity
public class CurrencyRate {

    @Id
    @GeneratedValue
    private Long id;
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate day;
    private String currency;
    private Double rate;

    public CurrencyRate() {
    }

    public CurrencyRate(LocalDate day, String currency, Double rate) {
        this.day = day;
        this.currency = currency;
        this.rate = rate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }
}
