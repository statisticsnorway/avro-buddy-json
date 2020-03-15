[![Build Status](https://drone.prod-bip-ci.ssb.no/api/badges/statisticsnorway/avro-buddy-json/status.svg)](https://drone.prod-bip-ci.ssb.no/statisticsnorway/avro-buddy-json)

# avro-buddy-json

JSON -> GenericRecord  

Depending on [avro-buddy-core](https://github.com/statisticsnorway/avro-buddy-core)

## Maven coordinates

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

For more usage examples, have a look at the [tests](https://github.com/statisticsnorway/avro-buddy-json/tree/master/src/test/java/no/ssb/avro/convert/json).


## Development

From the CLI, run `make help` to see common development commands.

```
build-mvn                      Build the project and install to you local maven repo
release-dryrun                 Simulate a release in order to detect any issues
release                        Release a new version. Update POMs and tag the new version in git.
```
