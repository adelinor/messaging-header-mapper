package com.github.adelinor.messaging.mapper.beans;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.github.adelinor.messaging.mapper.Header.Use;
import com.github.adelinor.sample.nogettersetter.MyMessageNoGetter;
import com.github.adelinor.sample.nogettersetter.MyMessageNoSetter;
import com.github.adelinor.sample.nogettersetter.MyMessageWithGetterSetter;
import com.github.adelinor.sample.noname.MyMessageNoHeaderName;
import com.github.adelinor.sample.readwrite.KeyAttributes;

class IntrospectionMappingExtractorTest {

	private IntrospectionMappingExtractor extractor = new IntrospectionMappingExtractor();
	
	@ParameterizedTest
	@ValueSource(classes = {MyMessageNoGetter.class, MyMessageNoSetter.class})
	void testExtractFor_MissingMethod(Class<?> beanClass) {
		Field[] fields = beanClass.getDeclaredFields();
		assertThat(fields).hasSize(1);
		
		Assertions.assertThatExceptionOfType(BeanHeaderMappingException.class)
			.isThrownBy(() -> {
				extractor.extractFor(beanClass, fields[0]);
			})
			.withMessageContaining("Method not found: ")
			.withMessageContaining("Sender")
			.withNoCause();

	}

	@Test
	void testExtractFor() {
		Field[] fields = MyMessageWithGetterSetter.class.getDeclaredFields();
		assertThat(fields).hasSize(1);
		
		MappingData mapping = extractor.extractFor(MyMessageWithGetterSetter.class, fields[0]);
		assertThat(mapping).isNotNull();
		assertThat(mapping.getFieldType()).isEqualTo(String.class);
		assertThat(mapping.getFieldName()).isEqualTo("sender");
		assertThat(mapping.getHeaderName()).isEqualTo("BATCH_MESSAGE_SENDER");
		assertThat(mapping.isRequired()).isFalse();
		assertThat(mapping.getGetter()).isNotNull();
		assertThat(mapping.getSetter()).isNotNull();
		assertThat(mapping.getConverter()).isNotNull();
		assertThat(mapping.getUse()).isSameAs(Use.READWRITE);
	}

	@Test
	void testExtractFor_NoHeaderName() {
		Field[] fields = MyMessageNoHeaderName.class.getDeclaredFields();
		assertThat(fields).hasSize(1);
		
		MappingData mapping = extractor.extractFor(MyMessageNoHeaderName.class, fields[0]);
		assertThat(mapping).isNotNull();
		assertThat(mapping.getFieldType()).isEqualTo(Integer.class);
		assertThat(mapping.getFieldName()).isEqualTo("status");
		assertThat(mapping.getHeaderName()).isEqualTo("status");
		assertThat(mapping.isRequired()).isFalse();
		assertThat(mapping.getGetter()).isNotNull();
		assertThat(mapping.getSetter()).isNotNull();
		assertThat(mapping.getConverter()).isNotNull();
	}
	
	private Field getField(String name, Class<?> beanClass) {
		Field[] fields = beanClass.getDeclaredFields();
		assertThat(fields)
			.extracting( f -> f.getName() )
			.contains(name);

		for (Field field : fields) {
			if (name.equals(field.getName())) {
				return field;
			}
		}

		return null;
	}

	@Test
	void testWriteOnly_NoSetter() {
		Field field = getField("messageNumber", KeyAttributes.class);
		MappingData mapping = extractor.extractFor(KeyAttributes.class, field);
		assertThat(mapping).isNotNull();
		assertThat(mapping.isRequired()).isFalse();
		assertThat(mapping.getGetter()).isNotNull();
		assertThat(mapping.getSetter()).isNull();
		assertThat(mapping.getUse()).isSameAs(Use.WRITEONLY);
	}

	@Test
	void testWriteOnly_Required_NoSetter() {
		Field field = getField("sender", KeyAttributes.class);
		MappingData mapping = extractor.extractFor(KeyAttributes.class, field);
		assertThat(mapping).isNotNull();
		assertThat(mapping.isRequired()).isTrue();
		assertThat(mapping.getGetter()).isNotNull();
		assertThat(mapping.getSetter()).isNull();
		assertThat(mapping.getUse()).isSameAs(Use.WRITEONLY);
	}

	@Test
	void testReadOnly_Required_NoGetter() {
		Field field = getField("encoding", KeyAttributes.class);
		MappingData mapping = extractor.extractFor(KeyAttributes.class, field);
		assertThat(mapping).isNotNull();
		assertThat(mapping.isRequired()).isTrue();
		assertThat(mapping.getGetter()).isNull();
		assertThat(mapping.getSetter()).isNotNull();
		assertThat(mapping.getUse()).isSameAs(Use.READONLY);
	}
}
