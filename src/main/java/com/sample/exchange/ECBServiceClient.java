package com.sample.exchange;

import com.sample.exchange.model.ecb.CurrencyRate;
import com.sample.exchange.model.ecb.DailyCurrencyRates;
import com.sample.exchange.model.ecb.Envelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author Akos Thurzo
 */
@Component
public class ECBServiceClient {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Scheduled(fixedRate = 60000)
    public void getECBData() throws JAXBException, MalformedURLException {
        log.debug("Scheduled method called!");

        JAXBContext jaxbContext = JAXBContext.newInstance(Envelope.class);

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        URL url = new URL("http://www.ecb.europa.eu/stats/eurofxref/eurofxref-hist-90d.xml");

        Envelope envelope = (Envelope) unmarshaller.unmarshal(url);

        for (DailyCurrencyRates dailyCurrencyRates : envelope.getCurrencyRatesWrapper().getDailyCurrencyRates()) {
            log.debug(dailyCurrencyRates.getDay().format(DateTimeFormatter.ISO_LOCAL_DATE));

            for (CurrencyRate currencyRate : dailyCurrencyRates.getCurrencyRates()) {
                log.debug(currencyRate.getCurrency() + "=" + currencyRate.getRate());
            }
        }
    }
}
