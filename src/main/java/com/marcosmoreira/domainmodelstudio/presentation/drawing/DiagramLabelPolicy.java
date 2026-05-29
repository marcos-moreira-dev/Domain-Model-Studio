package com.marcosmoreira.domainmodelstudio.presentation.drawing;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Política común para evitar labels ilegibles en el canvas.
 *
 * <p>El texto crítico puede conservarse completo con wrap. El texto secundario puede
 * recortarse por líneas, siempre dejando tooltip con el contenido completo.</p>
 */
public record DiagramLabelPolicy(
        int maxVisibleLines,
        boolean criticalText,
        boolean tooltipWhenTrimmed
) {

    public DiagramLabelPolicy {
        if (maxVisibleLines < 1) {
            throw new IllegalArgumentException("maxVisibleLines debe ser al menos 1.");
        }
    }

    public static DiagramLabelPolicy critical() {
        return new DiagramLabelPolicy(4, true, true);
    }

    public static DiagramLabelPolicy compactMeta() {
        return new DiagramLabelPolicy(2, false, true);
    }

    public static DiagramLabelPolicy connector() {
        return new DiagramLabelPolicy(2, false, true);
    }

    public String visibleText(String text) {
        String clean = clean(text);
        if (criticalText) {
            return clean;
        }
        String[] lines = clean.split("\\R");
        if (lines.length <= maxVisibleLines) {
            return clean;
        }
        return Arrays.stream(lines)
                .limit(maxVisibleLines)
                .collect(Collectors.joining(System.lineSeparator())) + "…";
    }

    public boolean shouldAttachTooltip(String originalText) {
        return tooltipWhenTrimmed && !clean(originalText).equals(visibleText(originalText));
    }

    private static String clean(String text) {
        return text == null ? "" : text.strip();
    }
}
