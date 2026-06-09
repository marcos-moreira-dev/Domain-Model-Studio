package com.marcosmoreira.domainmodelstudio.presentation.toolbar;

/** Secciones visuales de la barra contextual del diagrama. */
public enum DiagramToolbarSection {
    ELEMENTS("Elementos", true),
    MODEL("Modelo", true),
    NOTATION("Notación", true),
    VIEW("Vista", false),
    EXPORT("Exportar", false);

    private final String displayName;
    private final boolean primaryRow;

    DiagramToolbarSection(String displayName, boolean primaryRow) {
        this.displayName = displayName;
        this.primaryRow = primaryRow;
    }

    public String displayName() {
        return displayName;
    }

    public boolean primaryRow() {
        return primaryRow;
    }
}
