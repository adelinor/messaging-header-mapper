[![Build Status][travis-badge]][travis-badge-url]

[travis-badge]: https://travis-ci.org/adelinor/messaging-header-mapper.svg?branch=master
[travis-badge-url]: https://travis-ci.org/adelinor/messaging-header-mapper

# messaging-header-mapper

A set of helper classes to map Message headers (JMS, Camel) to a Plain Java bean.

The mapping is done with the help of the `@Header` annotation similar to `@Column` annotation in JPA.

```java
public class MyMessage {
	
	@Header(name="MESSAGE_TYPE")
	private MessageType messageType;

	@Header(name="BATCH_NUMBER", required=true)
	private String messageNumber;

	@Header(converter=DateToLongConverter.class)
	private Date receiveDate;

	// ....
}
```

These annotations are then used by the Java bean reflection mapper:

```java
BeanHeaderMapper<MyMessage> mapper = new BeanHeaderMapper<>(MyMessage.class);
```

From headers in the form of a map...

```java
Map<String, Object> headers = new HashMap<>();
headers.put("MESSAGE_TYPE", "INBOUND");
headers.put("BATCH_NUMBER", "12345");
```

The mapper instance can then be used to populate the Java bean properties:

```java
MyMessage m = new MyMessage();
mapper.fromHeaders(headers, m);

assertThat(m.getMessageType()).isEqualTo(MessageType.INBOUND); // Enum mapped by its name
assertThat(m.getMessageNumber()).isEqualTo("12345");

```

From the Java bean properties:

```java
MyMessage m = new MyMessage();
m.setMessageNumber("12345");
m.setMessageType(MessageType.INBOUND);

```

The mapper can be used to populate the headers:

```java
Map<String, Object> headers = new HashMap<>();
mapper.toHeaders(m, headers);
assertThat(headers.get("BATCH_NUMBER")).isEqualTo("12345");
assertThat(headers.get("MESSAGE_TYPE")).isEqualTo("INBOUND");

```

Et voilà!