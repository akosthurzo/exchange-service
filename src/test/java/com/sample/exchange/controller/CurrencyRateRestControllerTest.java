package com.sample.exchange.controller;

import com.sample.exchange.BaseIntegrationTest;
import com.sample.exchange.model.CurrencyRate;
import com.sample.exchange.repository.CurrencyRateRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isIn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CurrencyRateRestControllerTest extends BaseIntegrationTest {

    private List<CurrencyRate> currencyRates;

    @Autowired
    private CurrencyRateRepository currencyRateRepository;

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        currencyRateRepository.deleteAll();

        currencyRates = new ArrayList<>();

        currencyRates.add(new CurrencyRate(LocalDate.now(), "OWN", 123.456));
        currencyRates.add(new CurrencyRate(LocalDate.now().minusDays(1), "OWN2", 234.567));

        currencyRateRepository.save(currencyRates);
    }

    @After
    public void tearDown() {
        currencyRateRepository.deleteAll();
    }

    @Test
    public void getCurrencyRateByCurrency() throws Exception {
        mockMvc.perform(get("/api/currencyRates?currency=OWN2"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(1)))
               .andExpect(jsonPath("$[0].currency", is(currencyRates.get(1).getCurrency())))
               .andExpect(jsonPath("$[0].rate", is(currencyRates.get(1).getRate())));
    }

    @Test
    public void getCurrencyRateByDay() throws Exception {
        LocalDate yesterday = LocalDate.now().minusDays(1);

        mockMvc.perform(get("/api/currencyRates?day=" + yesterday.format(DateTimeFormatter.ISO_LOCAL_DATE)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(1)))
               .andExpect(jsonPath("$[0].currency", is(currencyRates.get(1).getCurrency())))
               .andExpect(jsonPath("$[0].rate", is(currencyRates.get(1).getRate())));
    }

    @Test
    public void getCurrencyRateByDayAndCurrency() throws Exception {
        LocalDate yesterday = LocalDate.now().minusDays(1);

        mockMvc.perform(
                   get("/api/currencyRates?currency=OWN2&day=" + yesterday.format(DateTimeFormatter.ISO_LOCAL_DATE)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(1)))
               .andExpect(jsonPath("$[0].currency", is(currencyRates.get(1).getCurrency())))
               .andExpect(jsonPath("$[0].rate", is(currencyRates.get(1).getRate())));
    }

    @Test
    public void getCurrencyRates() throws Exception {
        mockMvc.perform(get("/api/currencyRates"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(2)))
               .andExpect(
                   jsonPath("$[0].currency",
                   isIn(currencyRates.stream().map(CurrencyRate::getCurrency).collect(Collectors.toList()))))
               .andExpect(
                   jsonPath("$[0].rate",
                   isIn(currencyRates.stream().map(CurrencyRate::getRate).collect(Collectors.toList()))));
    }
}