package com.github.adelinor.messaging.mapper;

import java.util.Map;

/**
 * Mapper between headers in the form of a Map
 * and a java bean.
 *
 * @author  Adelino Rodrigues (created by)
 * @since 26 Feb 2019 (creation date)
 */
public interface MapHeaderMapper<T> extends HeaderMapper<Map<String, Object>, T> {

}
