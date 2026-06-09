package com.marcosmoreira.domainmodelstudio.presentation.drawing;

import java.util.ArrayList;
import java.util.List;

/** Estilo declarativo para figuras de diagrama. */
public record DiagramShapeStyle(
        List<String> styleClasses,
        double arcWidth,
        double arcHeight
) {

    public static final double DEFAULT_ARC = 18.0;

    public DiagramShapeStyle {
        styleClasses = List.copyOf(styleClasses == null ? List.of() : styleClasses);
        arcWidth = finiteNonNegative(arcWidth, "arcWidth");
        arcHeight = finiteNonNegative(arcHeight, "arcHeight");
    }

    public static DiagramShapeStyle node(String domainClass, boolean selected) {
        return new DiagramShapeStyle(DiagramPalette.nodeClasses(domainClass, selected), DEFAULT_ARC, DEFAULT_ARC);
    }

    public DiagramShapeStyle withStyleClass(String styleClass) {
        if (styleClass == null || styleClass.isBlank()) {
            return this;
        }
        List<String> classes = new ArrayList<>(styleClasses);
        classes.add(styleClass.strip());
        return new DiagramShapeStyle(classes, arcWidth, arcHeight);
    }

    private static double finiteNonNegative(double value, String fieldName) {
        if (!Double.isFinite(value) || value < 0.0) {
            throw new IllegalArgumentException(fieldName + " debe ser finito y no negativo.");
        }
        return value;
    }
}
