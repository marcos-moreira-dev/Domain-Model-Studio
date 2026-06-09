package com.marcosmoreira.domainmodelstudio.presentation.drawing;

import java.util.List;

/**
 * Nombres de clases CSS compartidas por los diagramas visuales.
 *
 * <p>La paleta real vive en CSS mediante tokens de la aplicación. Esta clase evita
 * que cada render kit invente nombres base distintos o duplique colores.</p>
 */
public final class DiagramPalette {

    public static final String NODE = "diagram-node";
    public static final String NODE_SELECTED = "diagram-node-selected";
    public static final String NODE_TITLE = "diagram-node-title";
    public static final String NODE_SUBTITLE = "diagram-node-subtitle";
    public static final String NODE_META = "diagram-node-meta";
    public static final String CONNECTOR = "diagram-connector";
    public static final String CONNECTOR_SELECTED = "diagram-connector-selected";
    public static final String CONNECTOR_ARROW = "diagram-connector-arrow";
    public static final String CONNECTOR_LABEL = "diagram-connector-label";
    public static final String HANDLE = "diagram-handle";
    public static final String SELECTION_HALO = "diagram-selection-halo";
    public static final String TEXT_CRITICAL = "diagram-text-critical";
    public static final String TEXT_MUTED = "diagram-text-muted";

    private DiagramPalette() {
    }

    public static List<String> nodeClasses(String domainClass, boolean selected) {
        return optionalSelected(NODE, domainClass, NODE_SELECTED, selected);
    }

    public static List<String> connectorClasses(String domainClass, boolean selected) {
        return optionalSelected(CONNECTOR, domainClass, CONNECTOR_SELECTED, selected);
    }

    private static List<String> optionalSelected(
            String baseClass,
            String domainClass,
            String selectedClass,
            boolean selected
    ) {
        String cleanDomainClass = domainClass == null ? "" : domainClass.strip();
        if (cleanDomainClass.isBlank()) {
            return selected ? List.of(baseClass, selectedClass) : List.of(baseClass);
        }
        return selected
                ? List.of(baseClass, cleanDomainClass, selectedClass)
                : List.of(baseClass, cleanDomainClass);
    }
}
