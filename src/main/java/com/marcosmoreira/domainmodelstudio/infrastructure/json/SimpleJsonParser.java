package com.marcosmoreira.domainmodelstudio.infrastructure.json;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Parser JSON mínimo para leer archivos .dms sin agregar dependencias externas. */
final class SimpleJsonParser {

    private final String text;
    private int index;

    private SimpleJsonParser(String text) {
        this.text = text == null ? "" : text;
    }

    static Object parse(String text) {
        SimpleJsonParser parser = new SimpleJsonParser(text);
        Object value = parser.readValue();
        parser.skipWhitespace();
        if (!parser.isAtEnd()) {
            throw parser.error("Contenido JSON inesperado");
        }
        return value;
    }

    private Object readValue() {
        skipWhitespace();
        if (isAtEnd()) {
            throw error("Se esperaba un valor JSON");
        }
        char current = peek();
        return switch (current) {
            case '{' -> readObject();
            case '[' -> readArray();
            case '"' -> readString();
            case 't' -> readLiteral("true", Boolean.TRUE);
            case 'f' -> readLiteral("false", Boolean.FALSE);
            case 'n' -> readLiteral("null", null);
            default -> readNumber();
        };
    }

    private Map<String, Object> readObject() {
        expect('{');
        Map<String, Object> object = new LinkedHashMap<>();
        skipWhitespace();
        if (consumeIf('}')) {
            return object;
        }
        while (true) {
            skipWhitespace();
            String key = readString();
            skipWhitespace();
            expect(':');
            Object value = readValue();
            object.put(key, value);
            skipWhitespace();
            if (consumeIf('}')) {
                return object;
            }
            expect(',');
        }
    }

    private List<Object> readArray() {
        expect('[');
        List<Object> array = new ArrayList<>();
        skipWhitespace();
        if (consumeIf(']')) {
            return array;
        }
        while (true) {
            array.add(readValue());
            skipWhitespace();
            if (consumeIf(']')) {
                return array;
            }
            expect(',');
        }
    }

    private String readString() {
        expect('"');
        StringBuilder value = new StringBuilder();
        while (!isAtEnd()) {
            char current = text.charAt(index++);
            if (current == '"') {
                return value.toString();
            }
            if (current != '\\') {
                value.append(current);
                continue;
            }
            if (isAtEnd()) {
                throw error("Secuencia de escape incompleta");
            }
            char escaped = text.charAt(index++);
            switch (escaped) {
                case '"' -> value.append('"');
                case '\\' -> value.append('\\');
                case '/' -> value.append('/');
                case 'b' -> value.append('\b');
                case 'f' -> value.append('\f');
                case 'n' -> value.append('\n');
                case 'r' -> value.append('\r');
                case 't' -> value.append('\t');
                case 'u' -> value.append(readUnicodeEscape());
                default -> throw error("Escape JSON no soportado: \\" + escaped);
            }
        }
        throw error("String JSON sin cierre");
    }

    private char readUnicodeEscape() {
        if (index + 4 > text.length()) {
            throw error("Escape unicode incompleto");
        }
        String hex = text.substring(index, index + 4);
        index += 4;
        return (char) Integer.parseInt(hex, 16);
    }

    private Object readNumber() {
        int start = index;
        if (peek() == '-') {
            index++;
        }
        while (!isAtEnd() && Character.isDigit(peek())) {
            index++;
        }
        if (!isAtEnd() && peek() == '.') {
            index++;
            while (!isAtEnd() && Character.isDigit(peek())) {
                index++;
            }
        }
        if (!isAtEnd() && (peek() == 'e' || peek() == 'E')) {
            index++;
            if (!isAtEnd() && (peek() == '+' || peek() == '-')) {
                index++;
            }
            while (!isAtEnd() && Character.isDigit(peek())) {
                index++;
            }
        }
        if (start == index) {
            throw error("Número JSON inválido");
        }
        return Double.parseDouble(text.substring(start, index));
    }

    private Object readLiteral(String literal, Object value) {
        if (!text.startsWith(literal, index)) {
            throw error("Literal JSON inválido");
        }
        index += literal.length();
        return value;
    }

    private void skipWhitespace() {
        while (!isAtEnd() && Character.isWhitespace(peek())) {
            index++;
        }
    }

    private void expect(char expected) {
        skipWhitespace();
        if (isAtEnd() || text.charAt(index) != expected) {
            throw error("Se esperaba '" + expected + "'");
        }
        index++;
    }

    private boolean consumeIf(char expected) {
        skipWhitespace();
        if (!isAtEnd() && text.charAt(index) == expected) {
            index++;
            return true;
        }
        return false;
    }

    private char peek() {
        return text.charAt(index);
    }

    private boolean isAtEnd() {
        return index >= text.length();
    }

    private IllegalArgumentException error(String message) {
        return new IllegalArgumentException(message + " en posición " + index);
    }
}
