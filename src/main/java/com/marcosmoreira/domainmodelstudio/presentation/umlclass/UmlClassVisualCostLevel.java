package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

/** Nivel cualitativo del costo visual estimado para una vista UML Clases. */
public enum UmlClassVisualCostLevel {
    LOW("Ligero"),
    MODERATE("Moderado"),
    HIGH("Pesado"),
    CRITICAL("Crítico");

    private final String displayName;

    UmlClassVisualCostLevel(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }

    public boolean warns() {
        return this == HIGH || this == CRITICAL;
    }
}
