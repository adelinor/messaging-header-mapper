package com.github.adelinor.messaging.mapper;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Uses the {@link Header} annotation to generating
 * the mapping between headers and a java object.
 *
 * @author  Adelino Rodrigues (created by)
 * @since 26 Feb 2019 (creation date)
 */
public class BeanHeaderMapper<T> implements MapHeaderMapper<T> {
	
	private static class Mapping {
		private Field field;
		private String headerName;
		private boolean required;
		private Method getter;
		private Method setter;

		private Mapping(Field field, String headerName, boolean required) {
			this.field = field;
			this.headerName = headerName;
			this.required = required;
		}
	}
	
	private List<Mapping> mappings = new ArrayList<>();
	
	public BeanHeaderMapper(Class<T> beanClass) {
		Field[] fields = beanClass.getDeclaredFields();

		for (Field field : fields) {
			Header header = field.getAnnotation(Header.class);

			if (header != null) {
				try {
					PropertyDescriptor desc = new PropertyDescriptor(field.getName(), beanClass);
					desc.getWriteMethod();
					Mapping mapping = new Mapping(field, header.name(), header.required());
					mappings.add(mapping);
					
					mapping.setter = desc.getWriteMethod();
					mapping.getter = desc.getReadMethod();

				} catch (IntrospectionException exc) {
					// TODO Auto-generated catch block
					exc.printStackTrace();
				}
			}
		}
	
	}

	@Override
	public void fromHeaders(Map<String, Object> headers, T target) {
		for (Mapping mapping : mappings) {
			if (headers.containsKey(mapping.headerName)) {
				Object value = headers.get(mapping.headerName);
				if (mapping.setter != null) {
					try {
						mapping.setter.invoke(target, value);
					} catch (InvocationTargetException | IllegalArgumentException | IllegalAccessException exc) {
						throw new IllegalStateException("Cannot set property " +
								mapping.field.getName() + " on object of type " +
								target.getClass(), exc);
					}
				}
			}
		}
	}

	@Override
	public void toHeaders(T source, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}

}
