package com.marcosmoreira.domainmodelstudio.presentation.drawing;

import java.util.ArrayList;
import java.util.List;

/** Estilo declarativo para textos dentro del lienzo. */
public record DiagramTextStyle(
        List<String> styleClasses,
        double maxWidth,
        boolean wrapText
) {

    public DiagramTextStyle {
        styleClasses = List.copyOf(styleClasses == null ? List.of() : styleClasses);
        if (!Double.isFinite(maxWidth) || maxWidth < 0.0) {
            throw new IllegalArgumentException("maxWidth debe ser finito y no negativo.");
        }
    }

    public static DiagramTextStyle title(double maxWidth) {
        return new DiagramTextStyle(List.of(DiagramPalette.NODE_TITLE), maxWidth, true);
    }

    public static DiagramTextStyle subtitle(double maxWidth) {
        return new DiagramTextStyle(List.of(DiagramPalette.NODE_SUBTITLE), maxWidth, true);
    }

    public static DiagramTextStyle meta(double maxWidth) {
        return new DiagramTextStyle(List.of(DiagramPalette.NODE_META), maxWidth, true);
    }

    public static DiagramTextStyle connectorLabel(double maxWidth) {
        return new DiagramTextStyle(List.of(DiagramPalette.CONNECTOR_LABEL), maxWidth, true);
    }

    public DiagramTextStyle withStyleClass(String styleClass) {
        if (styleClass == null || styleClass.isBlank()) {
            return this;
        }
        List<String> classes = new ArrayList<>(styleClasses);
        classes.add(styleClass.strip());
        return new DiagramTextStyle(classes, maxWidth, wrapText);
    }
}
