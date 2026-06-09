package com.marcosmoreira.domainmodelstudio.application.visualcomment;

import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramSize;
import com.marcosmoreira.domainmodelstudio.domain.visualcomment.VisualComment;

/** Reglas de texto y tamano para notas visuales libres. */
public final class VisualCommentPolicy {

    public static final int TITLE_MAX_LENGTH = 80;
    public static final int DESCRIPTION_MAX_LENGTH = 1000;
    public static final double MIN_WIDTH = 160.0;
    public static final double MAX_WIDTH = 420.0;
    public static final double DEFAULT_HEIGHT = 96.0;
    public static final double MIN_HEIGHT = 88.0;

    private static final double TITLE_CHARACTER_WIDTH = 7.4;
    private static final double TITLE_HORIZONTAL_PADDING = 48.0;

    private VisualCommentPolicy() {
    }

    public static String normalizeTitle(String title) {
        String normalized = normalize(title);
        if (normalized.isBlank()) {
            return VisualComment.DEFAULT_TITLE;
        }
        return limit(normalized, TITLE_MAX_LENGTH);
    }

    public static String normalizeDescription(String description) {
        return limit(normalize(description), DESCRIPTION_MAX_LENGTH);
    }

    public static DiagramSize preferredSize(String title) {
        return DiagramSize.of(preferredWidth(title), DEFAULT_HEIGHT);
    }

    public static double preferredWidth(String title) {
        String visibleTitle = normalizeTitle(title);
        return clamp(visibleTitle.length() * TITLE_CHARACTER_WIDTH + TITLE_HORIZONTAL_PADDING, MIN_WIDTH, MAX_WIDTH);
    }

    public static double clampHeight(double height) {
        if (!Double.isFinite(height)) {
            return DEFAULT_HEIGHT;
        }
        return Math.max(MIN_HEIGHT, height);
    }

    private static String limit(String value, int maxLength) {
        if (value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength);
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    private static String normalize(String value) {
        return value == null ? "" : value.strip();
    }
}
