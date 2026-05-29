package com.marcosmoreira.domainmodelstudio.infrastructure.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import org.junit.jupiter.api.Test;

class JsonStringEscaperTest {

    @Test
    void escapesControlCharactersAndRoundTripsThroughParser() {
        String original = "Linea\nTab\tRetorno\rBackspace\bForm\fQuote\"Slash\\Ctrl" + (char) 0x001F;
        String quoted = JsonStringEscaper.quote(original);

        assertEquals("\"Linea\\nTab\\tRetorno\\rBackspace\\bForm\\fQuote\\\"Slash\\\\Ctrl\\u001f\"", quoted);
        @SuppressWarnings("unchecked")
        Map<String, Object> parsed = (Map<String, Object>) SimpleJsonParser.parse("{\"value\": " + quoted + "}");

        assertEquals(original, parsed.get("value"));
    }
}
