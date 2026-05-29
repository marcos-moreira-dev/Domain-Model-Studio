package com.marcosmoreira.domainmodelstudio.presentation.drawing;

import java.util.ArrayList;
import java.util.List;

/** Estilo declarativo para conectores del lienzo. */
public record DiagramConnectorStyle(
        List<String> lineStyleClasses,
        List<String> arrowStyleClasses,
        List<String> labelStyleClasses,
        DiagramArrowKind arrowKind,
        boolean selected,
        boolean dashed
) {

    public DiagramConnectorStyle {
        lineStyleClasses = List.copyOf(lineStyleClasses == null ? List.of() : lineStyleClasses);
        arrowStyleClasses = List.copyOf(arrowStyleClasses == null ? List.of() : arrowStyleClasses);
        labelStyleClasses = List.copyOf(labelStyleClasses == null ? List.of() : labelStyleClasses);
        arrowKind = arrowKind == null ? DiagramArrowKind.NONE : arrowKind;
    }

    public static DiagramConnectorStyle directed(String domainClass, boolean selected) {
        return new DiagramConnectorStyle(
                DiagramPalette.connectorClasses(domainClass, selected),
                selected
                        ? List.of(DiagramPalette.CONNECTOR_ARROW, DiagramPalette.CONNECTOR_SELECTED)
                        : List.of(DiagramPalette.CONNECTOR_ARROW),
                List.of(DiagramPalette.CONNECTOR_LABEL),
                DiagramArrowKind.FILLED_TRIANGLE,
                selected,
                false
        );
    }

    public DiagramConnectorStyle withArrowKind(DiagramArrowKind kind) {
        return new DiagramConnectorStyle(lineStyleClasses, arrowStyleClasses, labelStyleClasses, kind, selected, dashed);
    }

    public DiagramConnectorStyle withDashed(boolean enabled) {
        return new DiagramConnectorStyle(lineStyleClasses, arrowStyleClasses, labelStyleClasses, arrowKind, selected, enabled);
    }

    public DiagramConnectorStyle withLineStyleClass(String styleClass) {
        if (styleClass == null || styleClass.isBlank()) {
            return this;
        }
        List<String> classes = new ArrayList<>(lineStyleClasses);
        classes.add(styleClass.strip());
        return new DiagramConnectorStyle(classes, arrowStyleClasses, labelStyleClasses, arrowKind, selected, dashed);
    }

    public DiagramConnectorStyle withArrowStyleClass(String styleClass) {
        if (styleClass == null || styleClass.isBlank()) {
            return this;
        }
        List<String> classes = new ArrayList<>(arrowStyleClasses);
        classes.add(styleClass.strip());
        return new DiagramConnectorStyle(lineStyleClasses, classes, labelStyleClasses, arrowKind, selected, dashed);
    }

    public DiagramConnectorStyle withLabelStyleClass(String styleClass) {
        if (styleClass == null || styleClass.isBlank()) {
            return this;
        }
        List<String> classes = new ArrayList<>(labelStyleClasses);
        classes.add(styleClass.strip());
        return new DiagramConnectorStyle(lineStyleClasses, arrowStyleClasses, classes, arrowKind, selected, dashed);
    }
}
