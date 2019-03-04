package com.github.adelinor.sample.nogettersetter;

import com.github.adelinor.messaging.mapper.Header;

/**
 * Java bean with single property and getter setter
 * methods defined.
 *
 * @author  Adelino Rodrigues (created by)
 * @since 4 Mar 2019 (creation date)
 */
public class MyMessageWithGetterSetter {

	@Header(name="BATCH_MESSAGE_SENDER")
	private String sender;

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}
}
