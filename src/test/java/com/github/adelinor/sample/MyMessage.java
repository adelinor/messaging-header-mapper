package com.github.adelinor.sample;

import java.util.Date;

import com.github.adelinor.messaging.mapper.Header;

/**
 * Plain Java bean used as a target and source for testing
 * the mapper.
 *
 * @author  Adelino Rodrigues (created by)
 * @since 24 Feb 2019 (creation date)
 */
public class MyMessage {
	
	@Header(name="MESSAGE_TYPE")
	private MessageType messageType;

	@Header(name="BATCH_NUMBER", required=true)
	private String messageNumber;

	@Header
	private Date timestamp;

	private int formatVersion;
}
