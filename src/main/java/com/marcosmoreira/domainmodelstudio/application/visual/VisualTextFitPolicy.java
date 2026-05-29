package com.marcosmoreira.domainmodelstudio.application.visual;

import java.util.List;

/**
 * Política transversal de tamaño mínimo para tarjetas visuales con texto.
 *
 * <p>No mide fuentes JavaFX reales. Usa una heurística estable para que la
 * importación Markdown, el autolayout y la reconciliación inicial produzcan
 * cajas donde título y descripción tengan espacio razonable. El usuario sigue
 * pudiendo redimensionar manualmente cuando el tipo de proyecto lo permite.</p>
 */
public final class VisualTextFitPolicy {

    private static final double CHARACTER_WIDTH = 6.6;
    private static final double TITLE_CHARACTER_WIDTH = 7.3;
    private static final double LINE_HEIGHT = 15.0;

    public BoxSize fitCard(BoxSize base, String title, String detail) {
        return fit(base, title, detail, Insets.card(), 190.0, 360.0, 76.0, 190.0);
    }

    public BoxSize fitCompactCard(BoxSize base, String title, String detail) {
        return fit(base, title, detail, Insets.card(), 150.0, 300.0, 68.0, 158.0);
    }

    public BoxSize fitLargeCard(BoxSize base, String title, String detail) {
        return fit(base, title, detail, Insets.card(), 210.0, 420.0, 88.0, 220.0);
    }

    public BoxSize fitDiamond(BoxSize base, String title, String detail) {
        BoxSize fitted = fit(base, title, detail, Insets.diamond(), 132.0, 240.0, 104.0, 190.0);
        double side = Math.max(fitted.width(), fitted.height());
        return new BoxSize(side, side);
    }

    private static BoxSize fit(BoxSize base, String title, String detail, Insets insets,
                               double minWidth, double maxWidth, double minHeight, double maxHeight) {
        String normalizedTitle = normalize(title);
        String normalizedDetail = normalize(detail);
        double textWidth = clamp(estimatedWidth(normalizedTitle, normalizedDetail, insets), minWidth, maxWidth);
        double width = Math.max(base.width(), textWidth);
        double usableWidth = Math.max(64.0, width - insets.horizontal());
        int lines = Math.max(1, lineCount(normalizedTitle, usableWidth, TITLE_CHARACTER_WIDTH))
                + lineCount(normalizedDetail, usableWidth, CHARACTER_WIDTH);
        double textHeight = clamp(insets.vertical() + lines * LINE_HEIGHT, minHeight, maxHeight);
        return new BoxSize(width, Math.max(base.height(), textHeight));
    }

    private static double estimatedWidth(String title, String detail, Insets insets) {
        return List.of(firstLine(title), firstLine(detail)).stream()
                .mapToDouble(value -> value.length() * CHARACTER_WIDTH + insets.horizontal())
                .max()
                .orElse(insets.horizontal() + 120.0);
    }

    private static int lineCount(String value, double usableWidth, double charWidth) {
        if (value.isBlank()) {
            return 0;
        }
        int explicitLines = value.split("\\R", -1).length;
        int wrapped = (int) Math.ceil(value.length() * charWidth / usableWidth);
        return Math.max(explicitLines, Math.max(1, wrapped));
    }

    private static double clamp(double value, double minimum, double maximum) {
        return Math.min(Math.max(minimum, value), maximum);
    }

    private static String firstLine(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }
        return value.strip().split("\\R", 2)[0];
    }

    private static String normalize(String value) {
        return value == null ? "" : value.strip();
    }

    private record Insets(double horizontal, double vertical) {
        static Insets card() {
            return new Insets(46.0, 38.0);
        }

        static Insets diamond() {
            return new Insets(76.0, 64.0);
        }
    }

    /** Tamaño visual sugerido para un nodo. */
    public record BoxSize(double width, double height) {
        public BoxSize {
            if (!Double.isFinite(width) || width <= 0.0) {
                throw new IllegalArgumentException("El ancho calculado debe ser positivo.");
            }
            if (!Double.isFinite(height) || height <= 0.0) {
                throw new IllegalArgumentException("El alto calculado debe ser positivo.");
            }
        }
    }
}
