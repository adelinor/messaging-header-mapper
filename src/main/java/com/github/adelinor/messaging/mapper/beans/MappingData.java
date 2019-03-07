package com.github.adelinor.messaging.mapper.beans;

import java.lang.reflect.Method;

import com.github.adelinor.messaging.mapper.Header.Use;
import com.github.adelinor.messaging.mapper.HeaderConverter;

/**
 * Java bean to header mapping data.
 *
 * @author  Adelino Rodrigues (created by)
 * @since 4 Mar 2019 (creation date)
 */
class MappingData {

	private Class<?> fieldType;
	private String fieldName;
	private String headerName;
	private HeaderConverter<?, ?> converter;
	private boolean required;
	private Method getter;
	private Method setter;
	private Use use;

	
	/**
	 * @param fieldType Type of mapped header in target java bean
	 * @param fieldName Field name in target java bean
	 * @param headerName Header name
	 * @param converter Convert class to use
	 * @param required Is the header required?
	 * @param getter Java bean access method
	 * @param setter Java bean access method
	 * @param use Use of header
	 */
	MappingData(Class<?> fieldType, String fieldName, String headerName,
			HeaderConverter<?, ?> converter, boolean required,
			Method getter, Method setter, Use use) {
		this.fieldType = fieldType;
		this.fieldName = fieldName;
		this.headerName = headerName;
		this.converter = converter;
		this.required = required;
		this.getter = getter;
		this.setter = setter;
		this.use = use;
	}

	Class<?> getFieldType() {
		return fieldType;
	}

	String getFieldName() {
		return fieldName;
	}

	String getHeaderName() {
		return headerName;
	}

	boolean isRequired() {
		return required;
	}

	Method getGetter() {
		return getter;
	}

	Method getSetter() {
		return setter;
	}

	HeaderConverter<?, ?> getConverter() {
		return converter;
	}

	Use getUse() {
		return use;
	}
}