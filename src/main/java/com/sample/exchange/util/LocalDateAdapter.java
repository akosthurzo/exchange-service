package com.sample.exchange.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author Akos Thurzo
 */
public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {

	@Override
	public String marshal(LocalDate date) throws Exception {
		return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
	}

	@Override
	public LocalDate unmarshal(String date) throws Exception {
		return LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
	}

}