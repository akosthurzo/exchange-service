package com.sample.exchange;

import com.sample.exchange.model.ecb.CurrencyRate;
import com.sample.exchange.model.ecb.DailyCurrencyRates;
import com.sample.exchange.model.ecb.Envelope;
import com.sample.exchange.repository.CurrencyRateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author Akos Thurzo
 */
@Component
public class ECBServiceClient {

    private static boolean initialized = false;

    @Autowired
    private CurrencyRateRepository currencyRateRepository;

    @Value("${com.sample.exchange.load.history.url}")
    private String historyUrl;

    @Value("${com.sample.exchange.load.daily.url}")
    private String dailyUrl;

    @Value("${com.sample.exchange.load.retry.on.error}")
    private long retrySeconds;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @PostConstruct
    private void init() {
        try {
            load(historyUrl);

            initialized = true;
        } catch (Exception e) {
            log.error("Error during initial data load", e);

            schedule();
        }
    }

    @Scheduled(cron = "${com.sample.exchange.load.cron.schedule}")
    public void scheduled() {
        log.debug("Scheduled method called!");

        try {
            if (!initialized) {
                init();
            } else {
                load(dailyUrl);
            }
        } catch (Exception e) {
            log.error("Error while loading ECB data", e);

            schedule();

            throw new RuntimeException(e);
        }
    }

    public void schedule() {
        ScheduledExecutorService localExecutor = Executors.newSingleThreadScheduledExecutor();

        TaskScheduler scheduler = new ConcurrentTaskScheduler(localExecutor);

        LocalDateTime localDateTime = LocalDateTime.now().plusSeconds(retrySeconds);

        scheduler.schedule(
            () -> scheduled(),
            Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant())
        );

        log.debug("Load process scheduled to be run at " + localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

    public void load(String url) throws JAXBException, MalformedURLException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Envelope.class);

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        Envelope envelope = (Envelope) unmarshaller.unmarshal(new URL(url));

        for (DailyCurrencyRates dailyCurrencyRates : envelope.getCurrencyRatesWrapper().getDailyCurrencyRates()) {
            log.debug("Loading data for " + dailyCurrencyRates.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE));

            for (CurrencyRate currencyRate : dailyCurrencyRates.getCurrencyRates()) {
                List<com.sample.exchange.model.CurrencyRate> currencyRateList =
                    currencyRateRepository.findByDateAndCurrency(
                        dailyCurrencyRates.getDate(), currencyRate.getCurrency());

                if (currencyRateList == null || (currencyRateList != null && currencyRateList.isEmpty()))
                    currencyRateRepository.save(
                        new com.sample.exchange.model.CurrencyRate(
                            dailyCurrencyRates.getDate(), currencyRate.getCurrency(), currencyRate.getRate()));
            }
        }
    }
}
