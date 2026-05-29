package com.marcosmoreira.domainmodelstudio.infrastructure.json;

/**
 * Escapador JSON mínimo compartido por los writers .dms.
 *
 * <p>El proyecto escribe JSON manualmente para conservar un formato estable y
 * legible. Esta clase centraliza el escape de strings para evitar diferencias
 * entre writers y para cubrir caracteres de control que no son válidos dentro
 * de un string JSON sin escape.</p>
 */
final class JsonStringEscaper {

    private JsonStringEscaper() {
    }

    static String quote(String value) {
        return "\"" + escape(value == null ? "" : value) + "\"";
    }

    static String escape(String value) {
        StringBuilder escaped = new StringBuilder(value.length() + 16);
        for (int i = 0; i < value.length(); i++) {
            char character = value.charAt(i);
            appendEscaped(escaped, character);
        }
        return escaped.toString();
    }

    private static void appendEscaped(StringBuilder escaped, char character) {
        switch (character) {
            case '"' -> escaped.append("\\\"");
            case '\\' -> escaped.append("\\\\");
            case '\b' -> escaped.append("\\b");
            case '\f' -> escaped.append("\\f");
            case '\n' -> escaped.append("\\n");
            case '\r' -> escaped.append("\\r");
            case '\t' -> escaped.append("\\t");
            default -> {
                if (character < 0x20) {
                    escaped.append(String.format("\\u%04x", (int) character));
                } else {
                    escaped.append(character);
                }
            }
        }
    }
}
