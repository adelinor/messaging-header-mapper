package com.github.adelinor.messaging.mapper;

import static com.github.adelinor.messaging.mapper.DefaultHeaderConverter.canConvert;
import static com.github.adelinor.messaging.mapper.DefaultHeaderConverter.getConverter;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.Date;

import org.junit.jupiter.api.Test;

class DefaultHeaderConverterTest {
	
	private enum SampleEnum { ONE, TWO };

	@Test
	void testCanConvert() {
		assertThat(canConvert(Collection.class)).isFalse();
		assertThat(canConvert(String[].class)).isFalse();
		assertThat(canConvert(Date.class)).isFalse();
		assertThat(canConvert(Void.class)).isFalse();
		assertThat(canConvert(Void.TYPE)).isFalse();

		assertThat(canConvert(String.class)).isTrue();
		assertThat(canConvert(SampleEnum.class)).isTrue();

		assertThat(canConvert(Boolean.class)).isTrue();
		assertThat(canConvert(Character.class)).isTrue();
		assertThat(canConvert(Byte.class)).isTrue();
		assertThat(canConvert(Short.class)).isTrue();
		assertThat(canConvert(Integer.class)).isTrue();
		assertThat(canConvert(Long.class)).isTrue();
		assertThat(canConvert(Float.class)).isTrue();
		assertThat(canConvert(Double.class)).isTrue();

		assertThat(canConvert(Boolean.TYPE)).isTrue();
		assertThat(canConvert(Character.TYPE)).isTrue();
		assertThat(canConvert(Byte.TYPE)).isTrue();
		assertThat(canConvert(Short.TYPE)).isTrue();
		assertThat(canConvert(Integer.TYPE)).isTrue();
		assertThat(canConvert(Long.TYPE)).isTrue();
		assertThat(canConvert(Float.TYPE)).isTrue();
		assertThat(canConvert(Double.TYPE)).isTrue();
	}

	@Test
	void testGetConverter_String() {
		HeaderConverter<String, String> converter = getConverter(String.class);
		assertThat(converter).isNotNull();

		String value = "Hello";
		assertThat(converter.convertToHeaderValue(value)).isSameAs(value);
		assertThat(converter.convertToObjectValue(value)).isSameAs(value);
	}

	@Test
	void testGetConverter_Enum() {
		HeaderConverter<SampleEnum, String> converter = getConverter(SampleEnum.class);
		assertThat(converter).isNotNull();

		assertThat(converter.convertToHeaderValue(SampleEnum.ONE)).isEqualTo("ONE");
		assertThat(converter.convertToHeaderValue(SampleEnum.TWO)).isEqualTo("TWO");
		assertThat(converter.convertToObjectValue("ONE")).isSameAs(SampleEnum.ONE);
		assertThat(converter.convertToObjectValue("TWO")).isSameAs(SampleEnum.TWO);
	}

	@Test
	void testGetConverter_Boolean() {
		HeaderConverter<Boolean, String> converter = getConverter(Boolean.class);
		assertThat(converter).isNotNull();

		assertThat(converter.convertToHeaderValue(Boolean.TRUE)).isEqualTo("true");
		assertThat(converter.convertToHeaderValue(Boolean.FALSE)).isEqualTo("false");
		assertThat(converter.convertToObjectValue("true")).isEqualTo(Boolean.TRUE);
		assertThat(converter.convertToObjectValue("false")).isEqualTo(Boolean.FALSE);

		HeaderConverter<Boolean, String> primitiveConverter = getConverter(Boolean.TYPE);
		assertThat(primitiveConverter).isSameAs(converter);
	}

	@Test
	void testGetConverter_Character() {
		HeaderConverter<Character, String> converter = getConverter(Character.class);
		assertThat(converter).isNotNull();

		assertThat(converter.convertToHeaderValue('e')).isEqualTo("e");
		assertThat(converter.convertToObjectValue("e")).isEqualTo('e');

		HeaderConverter<Character, String> primitiveConverter = getConverter(Character.TYPE);
		assertThat(primitiveConverter).isSameAs(converter);
	}

	@Test
	void testGetConverter_Byte() {
		HeaderConverter<Byte, String> converter = getConverter(Byte.class);
		assertThat(converter).isNotNull();

		assertThat(converter.convertToHeaderValue(Byte.MAX_VALUE)).isEqualTo("127");
		assertThat(converter.convertToObjectValue("-128")).isEqualTo(Byte.MIN_VALUE);

		HeaderConverter<Byte, String> primitiveConverter = getConverter(Byte.TYPE);
		assertThat(primitiveConverter).isSameAs(converter);
	}

	@Test
	void testGetConverter_Short() {
		HeaderConverter<Short, String> converter = getConverter(Short.class);
		assertThat(converter).isNotNull();

		assertThat(converter.convertToHeaderValue(Short.MAX_VALUE)).isEqualTo("32767");
		assertThat(converter.convertToObjectValue("-32768")).isEqualTo(Short.MIN_VALUE);

		HeaderConverter<Short, String> primitiveConverter = getConverter(Short.TYPE);
		assertThat(primitiveConverter).isSameAs(converter);
	}

	@Test
	void testGetConverter_Integer() {
		HeaderConverter<Integer, String> converter = getConverter(Integer.class);
		assertThat(converter).isNotNull();

		assertThat(converter.convertToHeaderValue(1)).isEqualTo("1");
		assertThat(converter.convertToObjectValue("-1")).isEqualTo(-1);

		HeaderConverter<Integer, String> primitiveConverter = getConverter(Integer.TYPE);
		assertThat(primitiveConverter).isSameAs(converter);
	}

	@Test
	void testGetConverter_Long() {
		HeaderConverter<Long, String> converter = getConverter(Long.class);
		assertThat(converter).isNotNull();

		assertThat(converter.convertToHeaderValue(100L)).isEqualTo("100");
		assertThat(converter.convertToObjectValue("-200")).isEqualTo(-200L);

		HeaderConverter<Long, String> primitiveConverter = getConverter(Long.TYPE);
		assertThat(primitiveConverter).isSameAs(converter);
	}

	@Test
	void testGetConverter_Float() {
		HeaderConverter<Float, String> converter = getConverter(Float.class);
		assertThat(converter).isNotNull();

		assertThat(converter.convertToHeaderValue(1.000001f)).isEqualTo("1.000001");
		assertThat(converter.convertToObjectValue("-0.00999")).isEqualTo(-0.00999f);

		HeaderConverter<Float, String> primitiveConverter = getConverter(Float.TYPE);
		assertThat(primitiveConverter).isSameAs(converter);
	}

	@Test
	void testGetConverter_Double() {
		HeaderConverter<Double, String> converter = getConverter(Double.class);
		assertThat(converter).isNotNull();

		assertThat(converter.convertToHeaderValue(0.123456789d)).isEqualTo("0.123456789");
		assertThat(converter.convertToObjectValue("-10.01")).isEqualTo(-10.01d);

		HeaderConverter<Double, String> primitiveConverter = getConverter(Double.TYPE);
		assertThat(primitiveConverter).isSameAs(converter);
	}

}
