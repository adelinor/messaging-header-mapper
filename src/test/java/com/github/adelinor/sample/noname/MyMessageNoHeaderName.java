package com.github.adelinor.sample.noname;

import com.github.adelinor.messaging.mapper.Header;

/**
 * Java bean with single property having no header
 * name specified in the mapping.
 *
 * @author  Adelino Rodrigues (created by)
 * @since 5 Mar 2019 (creation date)
 */
public class MyMessageNoHeaderName {

	@Header
	private Integer status;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
