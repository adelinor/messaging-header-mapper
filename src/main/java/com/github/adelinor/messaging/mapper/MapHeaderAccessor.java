package com.github.adelinor.messaging.mapper;

import java.util.Map;

/**
 * {@link HeaderAccessor} for a Map.
 *
 * @author  Adelino Rodrigues (created by)
 * @since 9 Mar 2019 (creation date)
 */
public class MapHeaderAccessor implements HeaderAccessor<Map<String, Object>> {

	@Override
	public boolean has(Map<String, Object> headers, String headerName) {
		return headers.containsKey(headerName);
	}

	@Override
	public Object get(Map<String, Object> headers, String headerName) {
		return headers.get(headerName);
	}

	@Override
	public void put(Map<String, Object> headers, String headerName, Object value) {
		headers.put(headerName, value);
	}

}
