package com.github.adelinor.messaging.mapper.beans;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.github.adelinor.sample.nogettersetter.MyMessageNoGetter;
import com.github.adelinor.sample.nogettersetter.MyMessageNoSetter;
import com.github.adelinor.sample.nogettersetter.MyMessageWithGetterSetter;
import com.github.adelinor.sample.noname.MyMessageNoHeaderName;

class IntrospectionMappingExtractorTest {

	@ParameterizedTest
	@ValueSource(classes = {MyMessageNoGetter.class, MyMessageNoSetter.class})
	void testExtractFor_MissingMethod(Class<?> beanClass) {
		Field[] fields = beanClass.getDeclaredFields();
		assertThat(fields).hasSize(1);
		
		IntrospectionMappingExtractor extractor = new IntrospectionMappingExtractor();
		
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
		
		IntrospectionMappingExtractor extractor = new IntrospectionMappingExtractor();
		MappingData mapping = extractor.extractFor(MyMessageWithGetterSetter.class, fields[0]);
		assertThat(mapping).isNotNull();
		assertThat(mapping.getFieldType()).isEqualTo(String.class);
		assertThat(mapping.getFieldName()).isEqualTo("sender");
		assertThat(mapping.getHeaderName()).isEqualTo("BATCH_MESSAGE_SENDER");
		assertThat(mapping.isRequired()).isFalse();
		assertThat(mapping.getGetter()).isNotNull();
		assertThat(mapping.getSetter()).isNotNull();
		assertThat(mapping.getConverter()).isNotNull();
	}

	@Test
	void testExtractFor_NoHeaderName() {
		Field[] fields = MyMessageNoHeaderName.class.getDeclaredFields();
		assertThat(fields).hasSize(1);
		
		IntrospectionMappingExtractor extractor = new IntrospectionMappingExtractor();
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

}
