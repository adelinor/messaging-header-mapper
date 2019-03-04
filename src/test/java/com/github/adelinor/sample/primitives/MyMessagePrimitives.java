package com.github.adelinor.sample.primitives;

import com.github.adelinor.messaging.mapper.Header;

/**
 * A plain java bean with primitive types.
 *
 * @author  Adelino Rodrigues (created by)
 * @since 4 Mar 2019 (creation date)
 */
public class MyMessagePrimitives {
	
	@Header(name="IS_DUPLICATE")
	private boolean duplicate;

	@Header(name="PRIORITY")
	private char priority;

	@Header(name="FLAGS")
	private byte flags;
	
	@Header(name="REVISION")
	private short revisionNumber;
	
	@Header(name="SEQ")
	private int sequence;
	
	@Header(name="ID")
	private long identifier;
	
	@Header(name="RATE")
	private float rate;
	
	@Header(name="INC")
	private double increase;

	public boolean isDuplicate() {
		return duplicate;
	}

	public void setDuplicate(boolean duplicate) {
		this.duplicate = duplicate;
	}

	public char getPriority() {
		return priority;
	}

	public void setPriority(char priority) {
		this.priority = priority;
	}

	public byte getFlags() {
		return flags;
	}

	public void setFlags(byte flags) {
		this.flags = flags;
	}

	public short getRevisionNumber() {
		return revisionNumber;
	}

	public void setRevisionNumber(short revisionNumber) {
		this.revisionNumber = revisionNumber;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public long getIdentifier() {
		return identifier;
	}

	public void setIdentifier(long identifier) {
		this.identifier = identifier;
	}

	public float getRate() {
		return rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

	public double getIncrease() {
		return increase;
	}

	public void setIncrease(double increase) {
		this.increase = increase;
	}

}
