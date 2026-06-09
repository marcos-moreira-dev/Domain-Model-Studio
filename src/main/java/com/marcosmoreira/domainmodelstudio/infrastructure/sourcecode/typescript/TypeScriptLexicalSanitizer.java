package com.marcosmoreira.domainmodelstudio.infrastructure.sourcecode.typescript;

import java.util.ArrayList;
import java.util.List;

/** Limpia comentarios/cadenas y tokeniza TypeScript sin depender de una versión concreta del lenguaje. */
final class TypeScriptLexicalSanitizer {
    private TypeScriptLexicalSanitizer() {
    }

    static List<TypeScriptToken> tokenize(String source) {
        String sanitized = stripCommentsAndStrings(source == null ? "" : source);
        List<TypeScriptToken> tokens = new ArrayList<>();
        int index = 0;
        while (index < sanitized.length()) {
            char current = sanitized.charAt(index);
            if (Character.isWhitespace(current)) {
                index++;
            } else if (isIdentifierStart(current)) {
                int start = index++;
                while (index < sanitized.length() && isIdentifierPart(sanitized.charAt(index))) {
                    index++;
                }
                tokens.add(new TypeScriptToken(sanitized.substring(start, index), start));
            } else {
                tokens.add(new TypeScriptToken(String.valueOf(current), index));
                index++;
            }
        }
        return tokens;
    }

    private static String stripCommentsAndStrings(String source) {
        StringBuilder out = new StringBuilder(source.length());
        for (int i = 0; i < source.length(); i++) {
            char current = source.charAt(i);
            char next = i + 1 < source.length() ? source.charAt(i + 1) : '\0';
            if (current == '/' && next == '/') {
                i = skipLineComment(source, out, i + 2);
            } else if (current == '/' && next == '*') {
                i = skipBlockComment(source, out, i + 2);
            } else if (current == '\'' || current == '"' || current == '`') {
                i = skipStringLike(source, out, i, current);
            } else {
                out.append(current);
            }
        }
        return out.toString();
    }

    private static int skipLineComment(String source, StringBuilder out, int index) {
        out.append("  ");
        while (index < source.length() && source.charAt(index) != '\n') {
            out.append(' ');
            index++;
        }
        if (index < source.length()) {
            out.append('\n');
        }
        return index;
    }

    private static int skipBlockComment(String source, StringBuilder out, int index) {
        out.append("  ");
        while (index < source.length()) {
            char current = source.charAt(index);
            char next = index + 1 < source.length() ? source.charAt(index + 1) : '\0';
            if (current == '*' && next == '/') {
                out.append("  ");
                return index + 1;
            }
            out.append(current == '\n' ? '\n' : ' ');
            index++;
        }
        return index;
    }

    private static int skipStringLike(String source, StringBuilder out, int index, char quote) {
        out.append(' ');
        index++;
        while (index < source.length()) {
            char current = source.charAt(index);
            if (current == '\\') {
                out.append("  ");
                index += 2;
            } else if (current == quote) {
                out.append(' ');
                return index;
            } else {
                out.append(current == '\n' ? '\n' : ' ');
                index++;
            }
        }
        return index;
    }

    private static boolean isIdentifierStart(char value) {
        return Character.isJavaIdentifierStart(value) || value == '_' || value == '$';
    }

    private static boolean isIdentifierPart(char value) {
        return Character.isJavaIdentifierPart(value) || value == '_' || value == '$' || value == '-';
    }
}
