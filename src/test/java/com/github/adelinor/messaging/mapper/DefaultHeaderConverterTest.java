package com.github.adelinor.messaging.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.Date;

import org.junit.jupiter.api.Test;

class DefaultHeaderConverterTest {
	
	private enum SampleEnum { ONE, TWO };

	@Test
	void testCanConvert() {
		DefaultHeaderConverter converter = new DefaultHeaderConverter();

		assertThat(converter.canConvert(Collection.class)).isFalse();
		assertThat(converter.canConvert(String[].class)).isFalse();
		assertThat(converter.canConvert(Date.class)).isFalse();
		assertThat(converter.canConvert(Void.class)).isFalse();
		assertThat(converter.canConvert(Void.TYPE)).isFalse();

		assertThat(converter.canConvert(String.class)).isTrue();
		assertThat(converter.canConvert(SampleEnum.class)).isTrue();

		assertThat(converter.canConvert(Boolean.class)).isTrue();
		assertThat(converter.canConvert(Character.class)).isTrue();
		assertThat(converter.canConvert(Byte.class)).isTrue();
		assertThat(converter.canConvert(Short.class)).isTrue();
		assertThat(converter.canConvert(Integer.class)).isTrue();
		assertThat(converter.canConvert(Long.class)).isTrue();
		assertThat(converter.canConvert(Float.class)).isTrue();
		assertThat(converter.canConvert(Double.class)).isTrue();

		assertThat(converter.canConvert(Boolean.TYPE)).isTrue();
		assertThat(converter.canConvert(Character.TYPE)).isTrue();
		assertThat(converter.canConvert(Byte.TYPE)).isTrue();
		assertThat(converter.canConvert(Short.TYPE)).isTrue();
		assertThat(converter.canConvert(Integer.TYPE)).isTrue();
		assertThat(converter.canConvert(Long.TYPE)).isTrue();
		assertThat(converter.canConvert(Float.TYPE)).isTrue();
		assertThat(converter.canConvert(Double.TYPE)).isTrue();
	}

}
