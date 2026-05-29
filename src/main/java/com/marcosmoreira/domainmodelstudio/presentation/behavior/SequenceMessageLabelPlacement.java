package com.marcosmoreira.domainmodelstudio.presentation.behavior;

/** Calcula una posición legible para etiquetas de mensajes UML Secuencia. */
final class SequenceMessageLabelPlacement {

    private static final double AVG_CHAR_WIDTH = 6.4;
    private static final double HORIZONTAL_PADDING = 18.0;
    private static final double MAX_WIDTH = 340.0;
    private static final double MIN_WIDTH = 76.0;

    private SequenceMessageLabelPlacement() {
    }

    static double widthFor(String label) {
        String text = label == null ? "" : label.strip();
        if (text.isBlank()) {
            return MIN_WIDTH;
        }
        return Math.min(MAX_WIDTH, Math.max(MIN_WIDTH, text.length() * AVG_CHAR_WIDTH + HORIZONTAL_PADDING));
    }

    static double xFor(SequenceMessageGeometry.SequenceMessageRoute route, String label) {
        double width = widthFor(label);
        return Math.max(8.0, route.labelX() - width / 2.0);
    }

    static double yFor(SequenceMessageGeometry.SequenceMessageRoute route) {
        return route.labelY();
    }
}
