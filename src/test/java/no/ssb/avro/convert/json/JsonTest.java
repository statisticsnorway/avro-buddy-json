package no.ssb.avro.convert.json;

import avro.shaded.com.google.common.collect.ImmutableList;
import avro.shaded.com.google.common.collect.ImmutableMap;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JsonTest {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class SimplePojo {
        private Integer someInt;
        private String someString;
        private Double someDouble;
        private boolean someBool;
        private String[] someArray;
    }

    @Test
    void nestedMap_shouldConvertToAndFromJson() throws Exception {
        Map<String, Object> obj = ImmutableMap.of(
          "someInt", 1,
          "someString", "blah",
          "someBool", true,
          "someList", ImmutableList.of(1, 2, 3),
          "someMap", ImmutableMap.of("one", 1)
        );

        String json = Json.from(obj);
        JSONAssert.assertEquals(
          "{\"someList\":[1,2,3],\"someString\":\"blah\",\"someInt\":1,\"someMap\":{\"one\":1},\"someBool\":true}",
          json, JSONCompareMode.LENIENT
        );

        Map<String, Object> obj2 = Json.toObject(new TypeReference<Map<String, Object>>() {
        }, json);
        assertThat(obj).isEqualTo(obj2);
    }

    @Test
    void arrayOfNestedMaps_shouldConvertToAndFromJson() throws Exception {
        List<Map<String, Object>> obj = ImmutableList.of(
          ImmutableMap.of("foo", "foo val"),
          ImmutableMap.of("bar", "bar val")
        );

        String json = Json.from(obj);
        JSONAssert.assertEquals(
          "[{\"foo\": \"foo val\"}, {\"bar\": \"bar val\"}]",
          json, JSONCompareMode.LENIENT
        );

        List<Map<String, Object>> obj2 = Json.toObject(new TypeReference<List<Map<String, Object>>>() {
        }, json);
        assertThat(obj).isEqualTo(obj2);
    }

    @Test
    void incompatibleObject_shouldThrowJsonException() throws Exception {
        @Data
        class Something {
            private String blah;
        }

        Json.JsonException e = assertThrows(Json.JsonException.class, () -> {
            Json.toObject(Something.class, "{\"foo\": \"bar\"}");
        });
        assertThat(e.getMessage()).isEqualTo("Error mapping JSON to Something object");
    }

    @Test
    void jsonWithMiscCasedPropKeys_withCamelCasedKeys() throws Exception {
        String inputJson =
            "{\"kebab-prop\": \"kebab\"," +
            "\"snake_prop\": \"snake\"," +
            "\"PascalProp\": \"pascal\"," +
            "\"camelCasedProp\": \"camel\"}";
        String expectedJson =
          "{\"kebabProp\": \"kebab\"," +
            "\"snakeProp\": \"snake\"," +
            "\"pascalProp\": \"pascal\"," +
            "\"camelCasedProp\": \"camel\"}";
        String actualJson = Json.withCamelCasedKeys(inputJson);

        JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.LENIENT);
    }

    @Test
    void jsonArray_wrapRootArray_shouldBeWrappedWithObject() throws Exception {
        String inputJson = "[{\"foo\": 13}, {\"bar\": 42}]";
        String expectedJson = "{\"root\": [{\"foo\": 13}, {\"bar\": 42}]}";
        String actualJson = Json.withWrappedRootArray(inputJson, "root");

        JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.LENIENT);
    }

    @Test
    void jsonObject_wrapRootArray_shouldNotBeWrappedWithObject() throws Exception {
        String inputJson = "{\"foo\": 13}";
        String expectedJson = inputJson;
        String actualJson = Json.withWrappedRootArray(inputJson, "root");

        JSONAssert.assertEquals(expectedJson, actualJson, true);
    }

}