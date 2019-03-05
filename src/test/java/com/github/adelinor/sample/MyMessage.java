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

	@Header(converter=DateToLongConverter.class)
	private Date receiveDate;

	private int formatVersion;
	
	@Header(name="IS_VALID")
	private Boolean valid;
	
	@Header(name="IS_DUPLICATE")
	private boolean duplicate;

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	public String getMessageNumber() {
		return messageNumber;
	}

	public void setMessageNumber(String messageNumber) {
		this.messageNumber = messageNumber;
	}

	public Date getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
	}

	public int getFormatVersion() {
		return formatVersion;
	}

	public void setFormatVersion(int formatVersion) {
		this.formatVersion = formatVersion;
	}

	public Boolean getValid() {
		return valid;
	}

	public void setValid(Boolean valid) {
		this.valid = valid;
	}

	public boolean isDuplicate() {
		return duplicate;
	}

	public void setDuplicate(boolean duplicate) {
		this.duplicate = duplicate;
	}
}
