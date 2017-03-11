package com.sample.exchange.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

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