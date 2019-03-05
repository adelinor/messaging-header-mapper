package com.github.adelinor.messaging.mapper.beans;

import java.lang.reflect.Method;

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

	
	/**
	 * @param fieldType Type of mapped header in target java bean
	 * @param fieldName Field name in target java bean
	 * @param headerName Header name
	 * @param required Is the header required?
	 * @param getter Java bean access method
	 * @param setter Java bean access method
	 */
	public MappingData(Class<?> fieldType, String fieldName, String headerName,
			HeaderConverter<?, ?> converter, boolean required, Method getter, Method setter) {
		this.fieldType = fieldType;
		this.fieldName = fieldName;
		this.headerName = headerName;
		this.converter = converter;
		this.required = required;
		this.getter = getter;
		this.setter = setter;
	}

	public Class<?> getFieldType() {
		return fieldType;
	}

	public String getFieldName() {
		return fieldName;
	}

	public String getHeaderName() {
		return headerName;
	}

	public boolean isRequired() {
		return required;
	}

	public Method getGetter() {
		return getter;
	}

	public Method getSetter() {
		return setter;
	}

	public HeaderConverter<?, ?> getConverter() {
		return converter;
	}
}