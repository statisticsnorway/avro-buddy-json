package no.ssb.avro.convert.json;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import tech.allegro.schema.json2avro.converter.JsonAvroConverter;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static no.ssb.avro.convert.json.Json.withCamelCasedKeys;
import static no.ssb.avro.convert.json.Json.withWrappedRootArray;

public class ToGenericRecord {

    private static final JsonAvroConverter json2Avro = new JsonAvroConverter();

    /**
     * Convert JSON -> GenericRecord
     * <p>
     * The JSON document must adhere to the supplied avro schema.
     */
    public static GenericRecord from(String json, Schema schema) {
        return from(json.getBytes(StandardCharsets.UTF_8), schema);
    }

    /**
     * Convert JSON -> GenericRecord
     * <p>
     * The JSON document must adhere to the supplied avro schema.
     */
    public static GenericRecord from(String json, Schema schema, JsonSettings jsonSettings) {
        return from(json.getBytes(StandardCharsets.UTF_8), schema, jsonSettings);
    }

    /**
     * Convert JSON -> GenericRecord
     * <p>
     * The JSON document must adhere to the supplied avro schema.
     */
    public static GenericRecord from(byte[] json, Schema schema) {
        return from(json, schema, new JsonSettings());
    }

    /**
     * Convert JSON -> GenericRecord
     * <p>
     * The JSON document must adhere to the supplied avro schema.
     */
    public static GenericRecord from(byte[] json, Schema schema, JsonSettings jsonSettings) {
        String arrayRootElement = arrayRootElementOf(schema).orElse(null);
        if (arrayRootElement != null) {
            json = withWrappedRootArray(json, arrayRootElement);
        }
        if (jsonSettings.enforceCamelCasedKeys()) {
            json = withCamelCasedKeys(json);
        }
        return json2Avro.convertToGenericDataRecord(json, schema);
    }

    // TODO: Validate that the returned field is an array type or else return empty
    private static Optional<String> arrayRootElementOf(Schema schema) {
        if (schema.getFields().size() == 1) {
            return Optional.of(schema.getFields().get(0).name());
        } else {
            return Optional.empty();
        }
    }

}
