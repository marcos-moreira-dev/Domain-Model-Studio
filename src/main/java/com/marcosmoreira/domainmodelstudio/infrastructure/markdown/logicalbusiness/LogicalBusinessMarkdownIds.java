package com.marcosmoreira.domainmodelstudio.infrastructure.markdown.logicalbusiness;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItemKind;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class LogicalBusinessMarkdownIds {

    private static final Pattern ID_PATTERN = Pattern.compile(
            "\\b(RN|PRE|INV|POST|ACC|CU|MF|FL|ENT|ATR|REL|REP|RISK|PEND|ACT|EST|CON|EVID|SUP|CALC)-\\d{3,}\\b",
            Pattern.CASE_INSENSITIVE);

    private LogicalBusinessMarkdownIds() {
    }

    static Optional<String> firstId(String value) {
        Matcher matcher = ID_PATTERN.matcher(value == null ? "" : value);
        if (matcher.find()) {
            return Optional.of(matcher.group().toUpperCase());
        }
        return Optional.empty();
    }

    static List<String> idsIn(String value) {
        Set<String> ids = new LinkedHashSet<>();
        Matcher matcher = ID_PATTERN.matcher(value == null ? "" : value);
        while (matcher.find()) {
            ids.add(matcher.group().toUpperCase());
        }
        return List.copyOf(ids);
    }

    static List<String> idsInExcept(String value, String excludedId) {
        String normalizedExcluded = excludedId == null ? "" : excludedId.strip().toUpperCase();
        return idsIn(value).stream()
                .filter(id -> !id.equals(normalizedExcluded))
                .toList();
    }

    static Optional<LogicalBusinessItemKind> kindFor(String id) {
        return LogicalBusinessItemKind.fromId(id == null ? "" : id.toUpperCase());
    }
}
