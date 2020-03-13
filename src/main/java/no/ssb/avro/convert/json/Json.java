package no.ssb.avro.convert.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

public class Json {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * Convert JSON to Object
     */
    public static <T> T toObject(Class<T> type, String json) {
        try {
            return OBJECT_MAPPER.readValue(json, type);
        } catch (IOException e) {
            throw new JsonException("Error mapping JSON to " + type.getSimpleName() + " object", e);
        }
    }

    /**
     * Convert JSON to Object
     */
    public static <T> T toObject(TypeReference<T> type, String json) {
        try {
            return OBJECT_MAPPER.readValue(json, type);
        } catch (IOException e) {
            throw new JsonException("Error mapping JSON to " + type.getType() + " object", e);
        }
    }

    /**
     * Convert JSON to String->Object map
     */
    public static Map<String, Object> toGenericMap(String json) {
        return Json.toObject(new TypeReference<Map<String, Object>>() {
        }, json);
    }

    /**
     * Convert Object to JSON
     */
    public static String from(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (Exception e) {
            throw new JsonException("Error mapping " + object.getClass().getSimpleName() + " object to JSON", e);
        }
    }

    /**
     * Convert Object to pretty (indented) JSON
     */
    public static String prettyFrom(Object object) {
        try {
            return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (Exception e) {
            throw new JsonException("Error mapping " + object.getClass().getSimpleName() + " object to JSON", e);
        }
    }

    public static class JsonException extends RuntimeException {
        public JsonException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static String withCamelCasedKeys(String json) {
        return new String(withCamelCasedKeys(json.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * Convert all property keys of the specified JSON to camelCase
     */
    public static byte[] withCamelCasedKeys(byte[] json) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new SimpleModule()
          .addKeySerializer(String.class, new JsonSerializer<>() {
              @Override
              public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                  String key = CaseUtil.toCamelCase(value);
                  gen.writeFieldName(key);
              }
          })
        );

        try {
            Map<String, Object> jsonMap = objectMapper.readValue(json, new TypeReference<>() {
            });
            return objectMapper.writeValueAsBytes(jsonMap);
        } catch (Exception e) {
            throw new JsonException("Error transforming JSON", e);
        }
    }

    /**
     * If the json is an array, then wrap it with a root object
     * <p>
     * E.g.
     * [{"foo": 1}, {"bar": 1}] -->
     * {"root": [{"foo": 1}, {"bar": 1}]}
     */
    public static byte[] withWrappedRootArray(byte[] json, String rootElementName) {
        try {
            JsonNode jsonNode = OBJECT_MAPPER.readTree(json);
            return jsonNode.isArray()
              ? OBJECT_MAPPER.writeValueAsBytes(Collections.singletonMap(rootElementName, jsonNode))
              : json;
        } catch (Exception e) {
            throw new JsonException("Error wrapping JSON root array", e);
        }
    }

    /**
     * If the json is an array, then wrap it with a root object
     * <p>
     * E.g.
     * [{"foo": 1}, {"bar": 1}] -->
     * {"root": [{"foo": 1}, {"bar": 1}]}
     */
    public static String withWrappedRootArray(String json, String rootElementName) {
        return new String(withWrappedRootArray(json.getBytes(StandardCharsets.UTF_8), rootElementName));
    }

}