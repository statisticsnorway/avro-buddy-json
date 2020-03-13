# avro-buddy-json

JSON -> GenericRecord  

Depending on [avro-buddy-core](https://github.com/statisticsnorway/avro-buddy-core)

## Add dependency

```xml
<dependency>
    <groupId>no.ssb.avro.convert.json</groupId>
    <artifactId>avro-buddy-json</artifactId>
    <version>x.x.x</version>
</dependency>
```

## Usage

```java
 ToGenericRecord.from(json, avroSchema);
```
