package com.github.adelinor.messaging.mapper;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
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
 *    &#064;Header(name="RECEIVE_DATE", converter=LongToDate.class)
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
@Target(FIELD)
@Retention(RUNTIME)
public @interface Header {

    /**
     * (Optional) The name of the column. Defaults to
     * the property or field name.
     */
    String name() default "";

    /**
     * (Optional) Whether the message header can be absent.
     */
    boolean required() default false;
}
