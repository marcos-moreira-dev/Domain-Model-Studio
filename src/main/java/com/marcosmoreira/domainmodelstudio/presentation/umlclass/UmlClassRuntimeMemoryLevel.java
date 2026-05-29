package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

/** Nivel de presión de memoria observado en el runtime Java durante el trabajo con UML Clases. */
public enum UmlClassRuntimeMemoryLevel {
    LOW("Baja"),
    MODERATE("Moderada"),
    HIGH("Alta"),
    CRITICAL("Crítica");

    private final String displayName;

    UmlClassRuntimeMemoryLevel(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }

    public boolean warns() {
        return this == HIGH || this == CRITICAL;
    }
}
