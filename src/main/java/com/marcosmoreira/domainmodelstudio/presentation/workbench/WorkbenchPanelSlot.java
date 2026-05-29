package com.marcosmoreira.domainmodelstudio.presentation.workbench;

/**
 * Slot lateral disponible para un workbench de diagrama.
 *
 * <p>El slot describe una zona del producto, no una implementación concreta. Así el
 * workbench puede montar paneles de módulos, UML, C4 o modelo conceptual sin conocer
 * sus clases internas.</p>
 */
public enum WorkbenchPanelSlot {
    STRUCTURE("Estructura"),
    PROPERTIES("Propiedades");

    private final String displayName;

    WorkbenchPanelSlot(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}
