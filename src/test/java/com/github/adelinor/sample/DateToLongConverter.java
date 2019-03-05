package com.github.adelinor.sample;

import java.util.Date;

import com.github.adelinor.messaging.mapper.HeaderConverter;

public class DateToLongConverter implements HeaderConverter<Date, Long> {

	@Override
	public Long convertToHeaderValue(Date value) {
		return value.getTime();
	}

	@Override
	public Date convertToObjectValue(Long value) {
		Date result = new Date();
		result.setTime(value);
		return result;
	}

}
