package no.ssb.avro.convert.json;

import java.util.Map;

public class JsonSettings {
    public static final String ENFORCE_CAMELCASED_KEYS = "enforceCamelCasedKeys";

    /**
     * Whether or not to enforce camel cased keys in the output.
     * If this is enabled, the datasource's keys will be formatted
     * as camelCase before conversion to avro.
     *
     * Note that all keys in the corresponding avro schema needs
     * to be on camelCase format.
     * <p>
     * Defaults to true
     */
    private boolean enforceCamelCasedKeys = true;

    public boolean enforceCamelCasedKeys() {
        return enforceCamelCasedKeys;
    }

    public JsonSettings enforceCamelCasedKeys(boolean enforceCamelCasedKeys) {
        this.enforceCamelCasedKeys = enforceCamelCasedKeys;
        return this;
    }

    /**
     * Override JsonSettings with config from Map
     */
    public JsonSettings configure(Map<String, Object> configMap) {
        if (configMap == null) {
            return this;
        }

        if (configMap.containsKey(ENFORCE_CAMELCASED_KEYS)) {
            enforceCamelCasedKeys((Boolean) configMap.get(ENFORCE_CAMELCASED_KEYS));
        }

        return this;
    }

    /**
     * Create JsonSettings using config from Map
     */
    public static JsonSettings from(Map<String,Object> configMap) {
        JsonSettings jsonSettings = new JsonSettings();
        jsonSettings.configure(configMap);
        return jsonSettings;
    }

}
