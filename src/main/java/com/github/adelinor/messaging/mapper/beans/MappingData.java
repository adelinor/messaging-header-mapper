package com.github.adelinor.messaging.mapper.beans;

import java.lang.reflect.Method;

/**
 * Java bean to header mapping data.
 *
 * @author  Adelino Rodrigues (created by)
 * @since 4 Mar 2019 (creation date)
 */
class MappingData {

	private Class<?> fieldType;
	private String fieldName;
	private Class<?> headerType;
	private String headerName;
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
	public MappingData(Class<?> fieldType, String fieldName, Class<?> headerType,
			String headerName, boolean required, Method getter, Method setter) {
		this.fieldType = fieldType;
		this.fieldName = fieldName;
		this.headerType = headerType;
		this.headerName = headerName;
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

	public Class<?> getHeaderType() {
		return headerType;
	}
}