package com.marcosmoreira.domainmodelstudio.application.canonization;

/**
 * Rol documental de un artefacto dentro del flujo enterprise de canonización.
 *
 * <p>El rol no cambia el tipo de proyecto ni crea dependencias semánticas entre
 * documentos. Solo permite leer una carpeta raíz como sesión de trabajo: fuente
 * lógica canónica, vistas compatibles, modelos de datos, arquitectura, procesos y artefactos
 * administrativos.</p>
 */
public enum CanonizationArtifactRole {
    SOURCE_MOTHER("Fuente lógica canónica"),
    LOGICAL_VIEW("Vista lógica compatible"),
    DATA_MODEL("Modelo/datos"),
    ARCHITECTURE_VIEW("Arquitectura"),
    BEHAVIOR_VIEW("Comportamiento/procesos"),
    ADMINISTRATIVE_VIEW("Aplicación administrativa"),
    SUPPORTING_GRAPH("Grafo de apoyo"),
    UNKNOWN("Sin rol enterprise específico");

    private final String displayName;

    CanonizationArtifactRole(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}
