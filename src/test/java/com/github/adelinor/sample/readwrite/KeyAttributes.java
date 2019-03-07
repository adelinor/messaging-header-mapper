package com.github.adelinor.sample.readwrite;

import com.github.adelinor.messaging.mapper.Header;
import com.github.adelinor.messaging.mapper.Header.Use;

/**
 * Java bean with {@link Header} mappings that make use
 * of the {@link Header#use()} attribute.
 * 
 * This class illustrates that read only properties only need
 * to have a setter method defined. Write only properties only need
 * a getter method defined.
 *
 * @author  Adelino Rodrigues (created by)
 * @since 7 Mar 2019 (creation date)
 */
public class KeyAttributes {
	
	@Header(required=true, use=Use.READONLY)
	private String encoding;
	
	private byte[] payload;
	
	@Header(required=true, use=Use.WRITEONLY)
	private String sender;

	@Header(use=Use.WRITEONLY)
	private String messageNumber;

	/**
	 * This method plays the role of a process method which
	 * would use the encoding and payload to extract the relevant
	 * information. To facilitate the simulation of various scenarios
	 * values are set explicitly with parameters.
	 * 
	 * @param sender Value to set as part of process
	 * @param messageNumber Value to set as part of process
	 */
	public void process(String sender, String messageNumber) {
		this.sender = sender;
		this.messageNumber = messageNumber;
	}
	
	public byte[] getPayload() {
		return payload;
	}

	public void setPayload(byte[] payload) {
		this.payload = payload;
	}

	public String getSender() {
		return sender;
	}

	public String getMessageNumber() {
		return messageNumber;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String hiddenGetEncoding() {
		return this.encoding;
	}
}