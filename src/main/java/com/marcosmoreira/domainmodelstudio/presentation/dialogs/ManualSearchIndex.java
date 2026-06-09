package com.marcosmoreira.domainmodelstudio.presentation.dialogs;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/** Índice simple para búsqueda local en la ayuda académica integrada. */
final class ManualSearchIndex {

    private final List<ManualSearchResult> entries;

    ManualSearchIndex(List<ManualCategory> categories) {
        Objects.requireNonNull(categories, "categories");
        List<ManualSearchResult> indexed = new ArrayList<>();
        for (ManualCategory category : categories) {
            for (ManualSection section : category.sections()) {
                indexed.add(new ManualSearchResult(category.title(), section, 0));
            }
        }
        this.entries = List.copyOf(indexed);
    }

    List<ManualSearchResult> allAlphabetical() {
        return entries.stream()
                .sorted(Comparator.comparing(result -> normalize(result.title())))
                .toList();
    }

    List<ManualSearchResult> search(String query) {
        String normalizedQuery = normalize(query);
        if (normalizedQuery.isBlank()) {
            return allAlphabetical();
        }
        List<String> tokens = List.of(normalizedQuery.split("\\s+"));
        return entries.stream()
                .map(result -> new ManualSearchResult(result.categoryTitle(), result.section(), score(result, tokens)))
                .filter(result -> result.score() > 0)
                .sorted(Comparator.comparingInt(ManualSearchResult::score).reversed()
                        .thenComparing(result -> normalize(result.title())))
                .toList();
    }

    private static int score(ManualSearchResult result, List<String> tokens) {
        String title = normalize(result.title());
        String category = normalize(result.categoryTitle());
        String summary = normalize(result.summary());
        StringBuilder body = new StringBuilder(summary);
        for (ManualBlock block : result.section().blocks()) {
            body.append(' ').append(normalize(block.title()));
            for (String line : block.lines()) {
                body.append(' ').append(normalize(cleanTheoryLine(line)));
            }
        }
        String bodyText = body.toString();
        int score = 0;
        for (String token : tokens) {
            if (token.isBlank()) {
                continue;
            }
            if (title.contains(token)) {
                score += 12;
            }
            if (category.contains(token)) {
                score += 5;
            }
            if (summary.contains(token)) {
                score += 4;
            }
            if (bodyText.contains(token)) {
                score += 2;
            }
        }
        return score;
    }

    private static String cleanTheoryLine(String value) {
        if (value == null) {
            return "";
        }
        return value
                .replaceFirst("^(LIST|PARAGRAPH|SUBHEADING|EXAMPLE)::", "")
                .strip();
    }

    private static String normalize(String value) {
        if (value == null) {
            return "";
        }
        String lower = value.toLowerCase(Locale.ROOT).strip();
        String decomposed = Normalizer.normalize(lower, Normalizer.Form.NFD);
        return decomposed.replaceAll("\\p{M}+", "");
    }
}
