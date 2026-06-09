package com.marcosmoreira.domainmodelstudio.infrastructure.pdf;

import java.util.ArrayList;
import java.util.List;

/** Ajuste de texto para PDF, incluyendo tokens técnicos largos en tablas. */
final class PdfTextWrapper {
    private static final double PAGE_WIDTH = 595.0;
    private static final double MARGIN_LEFT = 50.0;
    private static final double MARGIN_RIGHT = 50.0;

    private PdfTextWrapper() {}

    static List<String> wrap(String text, double fontSize) {
        return wrap(text, fontSize, 0);
    }

    static List<String> wrap(String text, double fontSize, int hangingIndentCharacters) {
        String normalized = normalized(text);
        int maxChars = maxCharsPerLine(fontSize) - hangingIndentCharacters;
        return wrapWords(normalized, maxChars);
    }

    static List<String> wrap(String text, double fontSize, double maxWidth) {
        int maxChars = Math.max(12, (int) Math.floor(maxWidth / (fontSize * 0.52)));
        return wrapWords(normalized(text), maxChars);
    }

    private static List<String> wrapWords(String normalized, int maxChars) {
        List<String> lines = new ArrayList<>();
        StringBuilder line = new StringBuilder();
        for (String word : normalized.split("\\s+")) {
            appendWrappedWord(lines, line, word, maxChars);
        }
        flushLine(lines, line);
        return lines.isEmpty() ? List.of("-") : lines;
    }

    private static void appendWrappedWord(List<String> lines, StringBuilder line, String word, int maxChars) {
        int safeMaxChars = Math.max(8, maxChars);
        if (word.length() > safeMaxChars) {
            flushLine(lines, line);
            appendLongWordChunks(lines, line, word, safeMaxChars);
            return;
        }
        if (line.isEmpty()) {
            line.append(word);
        } else if (line.length() + 1 + word.length() <= safeMaxChars) {
            line.append(' ').append(word);
        } else {
            flushLine(lines, line);
            line.append(word);
        }
    }

    private static void appendLongWordChunks(List<String> lines, StringBuilder line, String word, int maxChars) {
        int minTail = Math.min(10, maxChars);
        int offset = 0;
        while (word.length() - offset > maxChars) {
            int next = offset + maxChars;
            int tail = word.length() - next;
            if (tail > 0 && tail < minTail) {
                next = Math.max(offset + 1, word.length() - minTail);
            }
            lines.add(word.substring(offset, next));
            offset = next;
        }
        if (offset < word.length()) {
            line.append(word.substring(offset));
        }
    }

    private static void flushLine(List<String> lines, StringBuilder line) {
        if (!line.isEmpty()) {
            lines.add(line.toString());
            line.setLength(0);
        }
    }

    private static int maxCharsPerLine(double fontSize) {
        double usableWidth = PAGE_WIDTH - MARGIN_LEFT - MARGIN_RIGHT;
        return Math.max(32, (int) Math.floor(usableWidth / (fontSize * 0.52)));
    }

    private static String normalized(String text) {
        return text == null || text.isBlank() ? "-" : text.strip();
    }
}
