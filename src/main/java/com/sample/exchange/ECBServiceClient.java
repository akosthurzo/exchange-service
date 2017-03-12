package com.sample.exchange;

import com.sample.exchange.model.ecb.CurrencyRate;
import com.sample.exchange.model.ecb.DailyCurrencyRates;
import com.sample.exchange.model.ecb.Envelope;
import com.sample.exchange.repository.CurrencyRateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private CurrencyRateRepository currencyRateRepository;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @PostConstruct
    private void init() throws JAXBException, MalformedURLException {
        load("http://www.ecb.europa.eu/stats/eurofxref/eurofxref-hist-90d.xml");
    }

    @Scheduled(initialDelay = 60000, cron = "0 3 * * *") // every weekday at 03:00:00 UTC
    public void scheduled() {
        log.debug("Scheduled method called!");

        try {
            if (currencyRateRepository.count() == 0)
                init();
            else
                load("http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml");
        } catch (Exception e) {
            log.error("Error while loading ECB data", e);

            ScheduledExecutorService localExecutor = Executors.newSingleThreadScheduledExecutor();

            TaskScheduler scheduler = new ConcurrentTaskScheduler(localExecutor);

            scheduler.schedule(
                () -> scheduled(),
                Date.from(LocalDateTime.now().plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant())
            );
        }
    }

    public void load(String url) throws JAXBException, MalformedURLException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Envelope.class);

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        Envelope envelope = (Envelope) unmarshaller.unmarshal(new URL(url));

        for (DailyCurrencyRates dailyCurrencyRates : envelope.getCurrencyRatesWrapper().getDailyCurrencyRates()) {
            log.debug(dailyCurrencyRates.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE));

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
