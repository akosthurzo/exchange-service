package com.sample.exchange.model.ecb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Akos Thurzo
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Cube")
public class CurrencyRate {

    @XmlAttribute(name = "currency")
    private String currency;
    @XmlAttribute(name = "rate")
    private double rate;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
