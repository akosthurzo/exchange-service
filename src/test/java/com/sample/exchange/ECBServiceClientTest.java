package com.sample.exchange;

import com.sample.exchange.repository.CurrencyRateRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

/**
 * @author Akos Thurzo
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ECBServiceClientTest {

    @Autowired
    private CurrencyRateRepository currencyRateRepository;

    @Autowired
    private ECBServiceClient ecbServiceClient;

    @Before
    public void setUp() {
        currencyRateRepository.deleteAll();
    }

    @After
    public void tearDown() {
        currencyRateRepository.deleteAll();
    }

    @Test
    public void success() throws Exception {
        ecbServiceClient.load(
            new File(getClass().getClassLoader().getResource("good.xml").getFile()).toURI().toURL().toString());

        Assert.assertEquals(31, currencyRateRepository.count());
    }

    @Test(expected = Exception.class)
    public void failure() throws Exception {
        ecbServiceClient.load(
            new File(getClass().getClassLoader().getResource("bad.xml").getFile()).toURI().toURL().toString());
    }
}
