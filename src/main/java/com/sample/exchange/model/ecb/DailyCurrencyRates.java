package com.sample.exchange.model.ecb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author Akos Thurzo
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Cube")
public class DailyCurrencyRates {

    @XmlAttribute(name = "time")
    private String day;
    @XmlElement(name = "Cube")
    private List<CurrencyRate> currencyRates;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public List<CurrencyRate> getCurrencyRates() {
        return currencyRates;
    }

    public void setCurrencyRates(List<CurrencyRate> currencyRates) {
        this.currencyRates = currencyRates;
    }
}
