package com.sample.exchange.model.ecb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author Akos Thurzo
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Cube")
public class CurrencyRatesWrapper {

    @XmlElement(name = "Cube")
    private List<DailyCurrencyRates> dailyCurrencyRates;

    public List<DailyCurrencyRates> getDailyCurrencyRates() {
        return dailyCurrencyRates;
    }

    public void setDailyCurrencyRates(List<DailyCurrencyRates> dailyCurrencyRates) {
        this.dailyCurrencyRates = dailyCurrencyRates;
    }
}
