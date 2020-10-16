package no.ssb.avro.convert.json;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;

class ToGenericRecordTest {

    private Schema schema(String schemaFileName) throws IOException {
        return new Schema.Parser().parse(getClass().getClassLoader().getResourceAsStream(schemaFileName));
    }

    private byte[] json(String jsonFileName) throws IOException {
        return getClass().getClassLoader().getResourceAsStream(jsonFileName).readAllBytes();
    }

    @ParameterizedTest
    @ValueSource(strings = {
      "simple",
      "simple-array",
      "funky-key-casing",
      "map-with-misc-types",
      "types-example",
      "enhetsregisteret"
    })
    public void json_convertToGenericRecord(String scenario) throws Exception {
        ToGenericRecord.from(json(scenario + ".json"), schema(scenario + ".avsc"));
    }

    @Test
    public void dcManifestJson_convertToGenericRecord() throws Exception {
        String scenario = "dc-manifest";
        byte[] json = json(scenario + ".json");
        json = Json.withWrappedRootArray(json, "manifestItems");

        GenericRecord rec = ToGenericRecord.from(json, schema(scenario + ".avsc"));
    }

    /**
     * Requires test data (remabong.json) before running
     */
    @Test
    @Disabled
    public void json_convertComplexJsonToGenericRecord() throws Exception {
        String scenario = "bong";
        Schema avroSchema = schema(scenario + ".avsc");
        JsonSettings jsonSettings = new JsonSettings().enforceCamelCasedKeys(false);
        GenericRecord rec = ToGenericRecord.from(json(scenario + ".json"), avroSchema, jsonSettings);
    }
}