package com.github.adelinor.messaging.mapper;

/**
 * Abstracts the access operations required by a
 * {@link HeaderMapper} implementation. This way
 * allows to write an implementation common to
 * various header representations.
 *
 * @author  Adelino Rodrigues (created by)
 * @since 9 Mar 2019 (creation date)
 */
public interface HeaderAccessor<H> {

	/**
	 * @param headers Headers
	 * @param headerName Header name to check
	 * @return Does it contain the key headerName?
	 */
	boolean has(H headers, String headerName);
	
	/**
	 * @param headers Headers
	 * @param headerName Header name to retrieve
	 * @return Value (which could be null) for headerName or null
	 */
	Object get(H headers, String headerName);
	
	/**
	 * @param headers Headers
	 * @param headerName Header name to add or update
	 * @param value Value (which could be null) for headerName
	 */
	void put(H headers, String headerName, Object value);
}
