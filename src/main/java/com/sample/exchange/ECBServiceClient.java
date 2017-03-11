package com.sample.exchange;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Akos Thurzo
 */
@Component
public class ECBServiceClient {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Scheduled(fixedRate = 5000)
    public void getECBData() {
        log.debug("Scheduled method called!");
    }
}
