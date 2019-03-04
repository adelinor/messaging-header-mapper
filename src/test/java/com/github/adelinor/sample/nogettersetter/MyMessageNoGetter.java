package com.github.adelinor.sample.nogettersetter;

import com.github.adelinor.messaging.mapper.Header;

/**
 * Java bean with single property and no getter.
 *
 * @author  Adelino Rodrigues (created by)
 * @since 4 Mar 2019 (creation date)
 */
public class MyMessageNoGetter {

	@Header(name="BATCH_MESSAGE_SENDER")
	private String sender;

	public void setSender(String sender) {
		this.sender = sender;
	}
}
