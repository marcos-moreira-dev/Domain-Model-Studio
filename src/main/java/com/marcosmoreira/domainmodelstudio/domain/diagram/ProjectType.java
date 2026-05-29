package com.marcosmoreira.domainmodelstudio.domain.diagram;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;

/**
 * Tipo funcional legado de proyecto soportado por Domain Model Studio.
 *
 * <p>La versión actual solo implementa el modelo conceptual. Los nuevos tipos
 * de diagrama se identifican con {@link DiagramTypeId} para no inflar este enum
 * ni romper archivos .dms existentes.</p>
 */
public enum ProjectType {
    CONCEPTUAL_MODEL("Modelo conceptual", DiagramTypeId.CONCEPTUAL_MODEL);

    private final String displayName;
    private final DiagramTypeId diagramTypeId;

    ProjectType(String displayName, DiagramTypeId diagramTypeId) {
        this.displayName = displayName;
        this.diagramTypeId = diagramTypeId;
    }

    public String displayName() {
        return displayName;
    }

    public DiagramTypeId diagramTypeId() {
        return diagramTypeId;
    }

    public static ProjectType defaultType() {
        return CONCEPTUAL_MODEL;
    }

    public static ProjectType fromStoredValue(String value) {
        if (value == null || value.isBlank()) {
            return defaultType();
        }
        String normalized = value.trim().toUpperCase(java.util.Locale.ROOT);
        if (normalized.equals("CONCEPTUAL")
                || normalized.equals("CONCEPTUAL_MODEL")
                || normalized.equals("MODELO_CONCEPTUAL")) {
            return CONCEPTUAL_MODEL;
        }
        return defaultType();
    }
}
