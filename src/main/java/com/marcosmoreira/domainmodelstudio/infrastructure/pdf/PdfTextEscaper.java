package com.marcosmoreira.domainmodelstudio.infrastructure.pdf;

import java.nio.charset.StandardCharsets;

/** Normaliza texto para streams PDF simples con fuentes estándar. */
final class PdfTextEscaper {

    private PdfTextEscaper() {
    }

    static String escape(String value) {
        String normalized = normalize(value);
        StringBuilder escaped = new StringBuilder(normalized.length() + 16);
        for (int i = 0; i < normalized.length(); i++) {
            char current = normalized.charAt(i);
            if (current == '(' || current == ')' || current == '\\') {
                escaped.append('\\');
            }
            escaped.append(current);
        }
        return escaped.toString();
    }

    static byte[] bytes(String value) {
        return value.getBytes(StandardCharsets.ISO_8859_1);
    }

    private static String normalize(String value) {
        if (value == null || value.isBlank()) {
            return "-";
        }
        String normalized = value
                .replace('–', '-')
                .replace('—', '-')
                .replace('“', '"')
                .replace('”', '"')
                .replace('‘', '\'')
                .replace('’', '\'')
                .replace('•', '-')
                .replace('\n', ' ')
                .replace('\r', ' ')
                .strip();
        StringBuilder safe = new StringBuilder(normalized.length());
        for (int i = 0; i < normalized.length(); i++) {
            char current = normalized.charAt(i);
            safe.append(canEncode(current) ? current : '?');
        }
        return safe.toString();
    }

    private static boolean canEncode(char value) {
        return StandardCharsets.ISO_8859_1.newEncoder().canEncode(value);
    }
}
