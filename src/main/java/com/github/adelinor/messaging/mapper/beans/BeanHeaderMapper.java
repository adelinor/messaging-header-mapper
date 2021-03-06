package com.github.adelinor.messaging.mapper.beans;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.github.adelinor.messaging.mapper.Header;
import com.github.adelinor.messaging.mapper.Header.Use;
import com.github.adelinor.messaging.mapper.HeaderAccessor;
import com.github.adelinor.messaging.mapper.HeaderConverter;
import com.github.adelinor.messaging.mapper.HeaderMapper;

/**
 * Uses the {@link Header} annotation to generating
 * the mapping between headers and a java object.
 *
 * @author  Adelino Rodrigues (created by)
 * @since 26 Feb 2019 (creation date)
 */
public class BeanHeaderMapper<H, T> implements HeaderMapper<H, T> {

	/**
	 * Mappings gathered from the annotated class T
	 */
	private List<MappingData> mappings;
	
	/**
	 * Accesses headers from an instance of H
	 */
	private HeaderAccessor<H> accessor;
	
	/**
	 * @param beanClass Bean class to map to
	 * @param accessor Accesses specific header format
	 */
	public BeanHeaderMapper(Class<T> beanClass, HeaderAccessor<H> accessor) {
		IntrospectionMappingExtractor extractor = new IntrospectionMappingExtractor();

		this.mappings = Arrays.stream( beanClass.getDeclaredFields() )
			.map( f -> extractor.extractFor(beanClass, f) )
			.filter( m -> m != null)
			.collect(Collectors.toList());

		this.accessor = accessor;
	}

	@Override
	public void fromHeaders(H headers, T target) {
		for (MappingData mapping : mappings) {
			if (mapping.getUse() == Use.READONLY || mapping.getUse() == Use.READWRITE) {
				setFromHeaders(headers, target, mapping);				
			}
		}
	}

	private void setFromHeaders(H headers, T target, MappingData mapping) {
		boolean hasHeader = accessor.has(headers, mapping.getHeaderName());
		if ((! hasHeader) && mapping.isRequired()) {
			throw new IllegalArgumentException(mapping.getHeaderName() + 
					" is required and was not provided in " + headers);
		}
		if (hasHeader) {
			Object value = accessor.get(headers, mapping.getHeaderName());
			
			if (value != null) {
				@SuppressWarnings("unchecked")
				HeaderConverter<Object, Object> converter = (HeaderConverter<Object, Object>) mapping.getConverter();
				Object objectValue = converter.convertToObjectValue(value);

				try {
					mapping.getSetter().invoke(target, objectValue);
				} catch (InvocationTargetException | IllegalArgumentException | IllegalAccessException exc) {
					throw new IllegalStateException("Cannot set property " +
							mapping.getFieldName() + " on object of type " +
							target.getClass(), exc);
				}

			}
		}
	}

	@Override
	public void toHeaders(T source, H headers) {
		for (MappingData mapping : mappings) {
			if (mapping.getUse() == Use.WRITEONLY || mapping.getUse() == Use.READWRITE) {
				setToHeaders(headers, source, mapping);
			}
		}
	}

	private void setToHeaders(H headers, T source, MappingData mapping) {
		Object value;
		try {
			value = mapping.getGetter().invoke(source);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException exc) {
			throw new IllegalStateException("Cannot get property " +
					mapping.getFieldName() + " on object of type " +
					source.getClass(), exc);
		}
		
		if (value == null && mapping.isRequired()) {
			throw new IllegalArgumentException(mapping.getHeaderName() + 
					" is required and field " + mapping.getFieldName() +
					" is null");
		}
		if (value != null) {
			@SuppressWarnings("unchecked")
			HeaderConverter<Object, Object> converter = (HeaderConverter<Object, Object>) mapping.getConverter();
			Object headerValue = converter.convertToHeaderValue(value);
			accessor.put(headers, mapping.getHeaderName(), headerValue);
		}
	}
}
