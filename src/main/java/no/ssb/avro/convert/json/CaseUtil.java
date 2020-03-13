package no.ssb.avro.convert.json;

import java.util.Arrays;
import java.util.stream.Collectors;

public class CaseUtil {

    public static String toCamelCase(String s) {
        if (s == null) {
            return null;
        }
        else if (s.isBlank()) {
            return "";
        }

        return decapitaliseFirstLetter(
          String.join("", Arrays.stream(s.split("[-_\\s]"))
          .map(CaseUtil::capitaliseFirstLetter)
          .collect(Collectors.toList()))
        );
    }

    private static String capitaliseFirstLetter(String s) {
        return (s.length() > 0)
          ? s.substring(0, 1).toUpperCase() + s.substring(1)
          : s;
    }

    private static String decapitaliseFirstLetter(String s) {
        return (s.length() > 0)
          ? s.substring(0, 1).toLowerCase() + s.substring(1)
          : s;
    }

}
