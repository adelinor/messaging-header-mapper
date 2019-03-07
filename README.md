[![Build Status][travis-badge]][travis-badge-url]

[travis-badge]: https://travis-ci.org/adelinor/messaging-header-mapper.svg?branch=master
[travis-badge-url]: https://travis-ci.org/adelinor/messaging-header-mapper

# messaging-header-mapper

A set of helper classes to map Message headers (JMS, Camel) to a Plain Java bean.

### Introduction

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

### Use of mappings

When mapping headers to a plain Java bean, headers will be used in one of these scenarios:

* header needed as an input for the class to do processing. The class has no intention to modify the provided input
* header results from the processing of the class, and is therefore an output
* header is both an input and an output, i.e. its value is modified by the processing

For the scenarios above, the `@Header` annotation has the `use` attribute.

**Header as input only**

```java
@Header(required=true, use=Use.READONLY)
private String encoding;
```

In this scenario, the mapping for the property `encoding` is only considered in the `fromHeaders` method. Because `required` is set to *true*, the invocation of fromHeaders will fail unless headers define this value (this is illustrated by
the test testFromHeaders_MissingRequired in [BeanHeaderMapperReadWriteTest.java](src/test/java/com/github/adelinor/messaging/mapper/beans/BeanHeaderMapperReadWriteTest.java)).

**Header as output only**

```java
@Header(use=Use.WRITEONLY)
private String messageNumber;
```

In the above mapping, the property `messageNumber` is only considered in the
`toHeaders` method of the `HeaderMapper`.
To indicate that the output must be provided as an output, the `required` 
attribute can be set to *true*.

**Header as input and output**

When the `use` attribute is not specified in the `@Header` annotation, the
default behavior is treat the mapping as both an input and output. This corresponds to the value `Use.READWRITE`.

**Getter and setters on mapped properties with @Header**

For properties mapped with `@Header(use=Use.READONLY)`, i.e. input, the Java bean needs to define a setter method. The getter method for the mapped property 
is not mandatory.

For properties mapped with `@Header(use=Use.WRITEONLY)`, i.e. output, the Java bean needs to define a getter method but may not define a setter for the mapped property.

For properties mapped with `@Header` or `@Header(use=Use.READWRITE)`, both getter
and setter methods need to be defined.
