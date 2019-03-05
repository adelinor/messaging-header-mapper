package com.github.adelinor.messaging.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

import com.github.adelinor.sample.DateToLongConverter;
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
			String fieldName = field.getName();
			assertThat( fieldName ).isIn("messageType", "messageNumber",
					"receiveDate", "formatVersion", "valid", "duplicate");

			Header header = field.getAnnotation(Header.class);

			if ("messageType".equals(fieldName)) {
				assertThat(header).isNotNull();
				assertThat(header.required()).isFalse();
				assertThat(header.name()).isEqualTo("MESSAGE_TYPE");
				assertThat(header.converter()).isEqualTo(DefaultHeaderConverter.class);

			} else if ("messageNumber".equals(fieldName)) {
				assertThat(header).isNotNull();
				assertThat(header.required()).isTrue();
				assertThat(header.name()).isEqualTo("BATCH_NUMBER");
				assertThat(header.converter()).isEqualTo(DefaultHeaderConverter.class);
				
			} else if ("receiveDate".equals(fieldName)) {
				assertThat(header).isNotNull();
				assertThat(header.required()).isFalse();
				assertThat(header.name()).isEqualTo("");
				assertThat(header.converter()).isEqualTo(DateToLongConverter.class);
				
			} else if ("formatVersion".equals(fieldName)) {
				assertThat(header).isNull();

			} else if ("valid".equals(fieldName)) {
				assertThat(header).isNotNull();
				assertThat(header.required()).isFalse();
				assertThat(header.name()).isEqualTo("IS_VALID");
				assertThat(header.converter()).isEqualTo(DefaultHeaderConverter.class);

			} else if ("duplicate".equals(fieldName)) {
				assertThat(header).isNotNull();
				assertThat(header.required()).isFalse();
				assertThat(header.name()).isEqualTo("IS_DUPLICATE");
				assertThat(header.converter()).isEqualTo(DefaultHeaderConverter.class);
			}
		}
	}
}
