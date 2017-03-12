package com.sample.exchange;

import com.sample.exchange.repository.CurrencyRateRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;

/**
 * @author Akos Thurzo
 */
public class ECBServiceClientTest extends BaseIntegrationTest {

    private static final int GOOD_XML_RECORD_COUNT = 31;

    private static final String HISTORY_URL_VARIABLE = "historyUrl";

    private static final String DAILY_URL_VARIABLE = "dailyUrl";

    private static final String INITIALIZED_URL_VARIABLE = "initialized";

    @Autowired
    private CurrencyRateRepository currencyRateRepository;

    @Autowired
    private ECBServiceClient ecbServiceClient;

    @Value("${com.sample.exchange.load.retry.on.error}")
    private long retrySeconds;

    private String originalHistoryUrl;

    private String originalDailyUrl;

    private boolean originalInitialized;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Before
    public void setUp() throws Exception {
        currencyRateRepository.deleteAll();

        originalHistoryUrl = (String) getValue(HISTORY_URL_VARIABLE);
        originalDailyUrl = (String) getValue(DAILY_URL_VARIABLE);
        originalInitialized = (Boolean) getValue(INITIALIZED_URL_VARIABLE);
    }

    @After
    public void tearDown() throws Exception {
        currencyRateRepository.deleteAll();

        setValue(HISTORY_URL_VARIABLE, originalHistoryUrl);
        setValue(DAILY_URL_VARIABLE, originalDailyUrl);
        setValue(INITIALIZED_URL_VARIABLE, originalInitialized);
    }

    @Test
    public void success() throws Exception {
        ecbServiceClient.load(toURLString("good.xml"));

        Assert.assertEquals(GOOD_XML_RECORD_COUNT, currencyRateRepository.count());
    }

    @Test(expected = Exception.class)
    public void failure() throws Exception {
        ecbServiceClient.load(toURLString("bad.xml"));
    }

    @Test
    public void scheduled() throws Exception {
        setValue(DAILY_URL_VARIABLE, toURLString("bad.xml"));
        setValue(INITIALIZED_URL_VARIABLE, true);

        try {
            ecbServiceClient.scheduled();

            Assert.fail("Scheduled method should have failed.");
        } catch (Exception e) {
        }

        Assert.assertEquals(0, currencyRateRepository.count());

        setValue(DAILY_URL_VARIABLE, toURLString("good.xml"));

        long waitSeconds = retrySeconds + 5;

        log.debug("Wait for rescheduled load after failure... (" + waitSeconds + " seconds)");

        Thread.sleep(waitSeconds * 1000);

        Assert.assertEquals(GOOD_XML_RECORD_COUNT, currencyRateRepository.count());
    }

    @Test
    public void fallbackToInit() throws Exception {
        setValue(HISTORY_URL_VARIABLE, toURLString("initial.xml"));
        setValue(DAILY_URL_VARIABLE, toURLString("good.xml"));
        setValue(INITIALIZED_URL_VARIABLE, false);

        ecbServiceClient.scheduled();

        Assert.assertEquals(1, currencyRateRepository.count());
        Assert.assertTrue((Boolean) getValue(INITIALIZED_URL_VARIABLE));

        ecbServiceClient.scheduled();

        Assert.assertEquals(GOOD_XML_RECORD_COUNT + 1, currencyRateRepository.count());
    }

    private String toURLString(String file) throws MalformedURLException {
        return new File(getClass().getClassLoader().getResource(file).getFile()).toURI().toURL().toString();
    }

    private Object getValue(String variable) throws Exception {
        Field field = ECBServiceClient.class.getDeclaredField(variable);

        field.setAccessible(true);

        return field.get(ecbServiceClient);
    }

    private void setValue(String variable, Object value) throws Exception {
        Field field = ECBServiceClient.class.getDeclaredField(variable);

        field.setAccessible(true);

        if (Modifier.isStatic(field.getModifiers()))
            field.set(null, value);
        else
            field.set(ecbServiceClient, value);
    }
}
