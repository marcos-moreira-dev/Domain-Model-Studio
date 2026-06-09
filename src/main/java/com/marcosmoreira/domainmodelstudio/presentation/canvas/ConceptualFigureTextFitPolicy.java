package com.marcosmoreira.domainmodelstudio.presentation.canvas;

/** Política local para que los rótulos del modelo conceptual quepan en figuras Chen. */
final class ConceptualFigureTextFitPolicy {

    private static final double APPROX_CHAR_WIDTH_FACTOR = 0.58;
    private static final double MIN_FONT_SIZE = 9.5;

    private ConceptualFigureTextFitPolicy() {
    }

    static double fittedFontSize(String text, double baseFontSize, double availableWidth) {
        if (text == null || text.isBlank() || availableWidth <= 0.0) {
            return baseFontSize;
        }
        double estimatedWidth = text.length() * baseFontSize * APPROX_CHAR_WIDTH_FACTOR;
        if (estimatedWidth <= availableWidth) {
            return baseFontSize;
        }
        double fitted = availableWidth / Math.max(1.0, text.length() * APPROX_CHAR_WIDTH_FACTOR);
        return Math.max(MIN_FONT_SIZE, Math.min(baseFontSize, fitted));
    }
}
