package com.github.adelinor.messaging.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class MapHeaderAccessorTest {
	
	private MapHeaderAccessor accessor = new MapHeaderAccessor();

	@Test
	void testHas() {
		Map<String, Object> map = new HashMap<>();
		assertThat(accessor.has(map, "toto")).isFalse();
		
		map.put("toto", null);
		assertThat(accessor.has(map, "toto")).isTrue();
	}

	@Test
	void testGet() {
		Map<String, Object> map = new HashMap<>();
		assertThat(accessor.get(map, "toto")).isNull();
		assertThat(accessor.get(map, "titi")).isNull();

		String value = "123";
		map.put("toto", null);
		map.put("titi", value);
		assertThat(accessor.get(map, "toto")).isNull();
		assertThat(accessor.get(map, "titi")).isSameAs(value);
	}

	@Test
	void testPut() {
		Map<String, Object> map = new HashMap<>();

		assertThat(accessor.has(map, "titi")).isFalse();

		String value = "123";
		accessor.put(map, "titi", value);
		assertThat(map.get("titi")).isSameAs(value);
		assertThat(accessor.get(map, "titi")).isSameAs(value);
	}

}
