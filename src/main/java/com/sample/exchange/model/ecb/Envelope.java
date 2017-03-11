package com.sample.exchange.model.ecb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Akos Thurzo
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Envelope", namespace = "http://www.gesmes.org/xml/2002-08-01")
public class Envelope {

    @XmlElement(name = "Cube")
    private CurrencyRatesWrapper _currencyRatesWrapper;

    public CurrencyRatesWrapper getCurrencyRatesWrapper() {
        return _currencyRatesWrapper;
    }

    public void setCurrencyRatesWrapper(CurrencyRatesWrapper currencyRatesWrapper) {
        this._currencyRatesWrapper = currencyRatesWrapper;
    }
}
