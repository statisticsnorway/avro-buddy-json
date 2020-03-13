package no.ssb.avro.convert.json;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CaseUtilTest
{
    @Test
    void convertToCamelCase() {
        assertThat(CaseUtil.toCamelCase("  Some       text-with__miscCasing   ")).isEqualTo("someTextWithMiscCasing");
        assertThat(CaseUtil.toCamelCase(null)).isEqualTo(null);
        assertThat(CaseUtil.toCamelCase("")).isEqualTo("");
        assertThat(CaseUtil.toCamelCase("    \t        ")).isEqualTo("");
    }

}