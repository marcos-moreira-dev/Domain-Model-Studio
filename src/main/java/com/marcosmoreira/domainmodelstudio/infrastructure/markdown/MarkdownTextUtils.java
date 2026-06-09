package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.text.Normalizer;
import java.util.Locale;

/** Utilidades pequeñas para normalizar texto del Markdown. */
final class MarkdownTextUtils {

    private MarkdownTextUtils() {
    }

    static String valueAfterColon(String line) {
        int index = line.indexOf(':');
        if (index < 0) {
            return "";
        }
        return line.substring(index + 1).trim();
    }

    static String keyBeforeColon(String line) {
        int index = line.indexOf(':');
        if (index < 0) {
            return "";
        }
        return line.substring(0, index).trim().toLowerCase(Locale.ROOT);
    }

    static String toStableId(String value) {
        String normalized = Normalizer.normalize(value == null ? "" : value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "_")
                .replaceAll("^_+|_+$", "");
        return normalized.isBlank() ? "elemento" : normalized;
    }

    static boolean isPropertyLine(String line) {
        return line.contains(":") && !line.trim().startsWith("-");
    }

    static DiagramProject withSourceMarkdownPath(DiagramProject project, String sourceName) {
        if (project == null || sourceName == null || sourceName.isBlank()) {
            return project;
        }
        return project.withMetadata(project.metadata().withSourceMarkdownPath(sourceName));
    }
}
