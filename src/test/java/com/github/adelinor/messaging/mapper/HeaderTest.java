package com.github.adelinor.messaging.mapper;

import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.github.adelinor.sample.MyMessage;

/**
 * Tests for annotation.
 *
 * @author  Adelino Rodrigues (created by)
 * @since 24 Feb 2019 (creation date)
 */
class HeaderTest {

	/**
	 * Test method for {@link com.github.adelinor.messaging.mapper.Header#name()}.
	 */
	@Test
	void testName() {
		Class<?> beanClass = MyMessage.class;
		Field[] fields = beanClass.getDeclaredFields();
		for (Field field : fields) {
			System.out.print( field.getName() + ": ");
			Header header = field.getAnnotation(Header.class);
			
			if (header == null) {
				System.out.println("NOT annotated");
			} else {
				String name = header.name();
				if (name.isEmpty()) {
					name = field.getName();
				}
				System.out.println("maps to " + name + ", required=" +
					header.required());
			}
		}
	}

	/**
	 * Test method for {@link com.github.adelinor.messaging.mapper.Header#required()}.
	 */
	@Test
	@Disabled
	void testRequired() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.github.adelinor.messaging.mapper.Header#converter()}.
	 */
	@Test
	@Disabled
	void testConverter() {
		fail("Not yet implemented");
	}

}
