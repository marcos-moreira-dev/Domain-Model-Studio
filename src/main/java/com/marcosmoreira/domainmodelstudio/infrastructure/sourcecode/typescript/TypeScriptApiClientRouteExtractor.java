package com.marcosmoreira.domainmodelstudio.infrastructure.sourcecode.typescript;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Extrae rutas HTTP simples usadas por servicios TypeScript/Angular sin interpretar todo el framework. */
final class TypeScriptApiClientRouteExtractor {
    private static final Pattern HTTP_CALL_PATTERN = Pattern.compile(
            "\\.(get|post|put|delete|patch)\\s*(?:<[^>]+>)?\\s*\\(\\s*([\\\"'`])([^\\\"'`$]+)\\2",
            Pattern.CASE_INSENSITIVE);

    private TypeScriptApiClientRouteExtractor() {
    }

    static String apiClientRoutesFor(String source, List<TypeScriptToken> tokens, String simpleName,
                                     int bodyStart, int bodyEnd) {
        if (source == null || source.isBlank() || bodyStart < 0 || bodyEnd <= bodyStart || !looksLikeApiClient(simpleName)) {
            return "";
        }
        int from = tokenOffset(tokens, bodyStart);
        int to = Math.min(source.length(), tokenOffset(tokens, bodyEnd));
        if (from < 0 || to <= from) {
            return "";
        }
        String classBody = source.substring(from, to);
        Matcher matcher = HTTP_CALL_PATTERN.matcher(classBody);
        ArrayList<String> routes = new ArrayList<>();
        while (matcher.find()) {
            String method = matcher.group(1).toUpperCase();
            String route = matcher.group(3).strip();
            if (!route.isBlank()) {
                routes.add(method + " " + route);
            }
        }
        return String.join(";", routes);
    }

    private static boolean looksLikeApiClient(String simpleName) {
        String normalized = simpleName == null ? "" : simpleName.toLowerCase();
        return normalized.contains("service") || normalized.contains("api") || normalized.contains("client");
    }

    private static int tokenOffset(List<TypeScriptToken> tokens, int tokenIndex) {
        if (tokens == null || tokenIndex < 0 || tokenIndex >= tokens.size()) {
            return -1;
        }
        return tokens.get(tokenIndex).index();
    }
}
