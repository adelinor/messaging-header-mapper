package com.github.adelinor.messaging.mapper;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Is used to specify the mapped header of JMS (or other messaging standards)
 * message.
 *
 * <blockquote><pre>
 *    Example 1: where MessageType is an Enum type
 *
 *    &#064;Header(name="MESSAGE_TYPE", required=true)
 *    private MessageType messageType;
 *
 *    Example 2:
 *
 *    &#064;Header(name="RECEIVE_DATE", converter=DateToLongConverter.class)
 *    private Date receiveDate;
 *
 *    Example 3:
 *
 *    &#064;Header(name="BATCH_NUMBER")
 *    private String messageNumber;
 *
 * </pre></blockquote>
 *
 */
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Header {
	
	/**
	 * How this header is supposed to be used.
	 */
	public enum Use {
		/**
		 * Header is copied only from header to java bean
		 * but is disregarded when copying java bean properties
		 * to headers
		 */
		READONLY,

		/**
		 * Header is copied from header to java bean and
		 * copied from java bean to headers
		 */
		READWRITE,

		/**
		 * Disregarded when copying headers to java bean but
		 * copied when copying java bean properties to headers
		 */
		WRITEONLY
	};

    /**
     * (Optional) The name of the column. Defaults to
     * the property or field name.
     */
    String name() default "";

    /**
     * (Optional) Whether the message header can be absent.
     */
    boolean required() default false;
    
    /**
     * (Optional) The converter to use. Defaults
     * to {@link DefaultHeaderConverter} which only 
     * supports very simple conversions.
     */
    Class<? extends HeaderConverter<?, ?>> converter() default DefaultHeaderConverter.class;
    
    
    /**
     * (Optional) The header is by default used in a bi-directional
     * way: i.e. READWRITE .
     */
    Use use() default Use.READWRITE;
}
