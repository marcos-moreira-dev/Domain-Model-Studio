package com.marcosmoreira.domainmodelstudio.application.canonization;

/** Estado de preparación de una carpeta raíz importada como flujo documental. */
public enum CanonizationFlowReadiness {
    EMPTY("No se importaron proyectos"),
    MISSING_LOGICAL_BUSINESS_INTAKE("Falta el Levantamiento lógico como fuente lógica canónica"),
    DUPLICATED_LOGICAL_BUSINESS_INTAKE("Hay más de un Levantamiento lógico"),
    READY_WITH_REJECTIONS("Listo con archivos rechazados por revisar"),
    READY("Listo para revisión documental");

    private final String displayName;

    CanonizationFlowReadiness(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}
