package com.marcosmoreira.domainmodelstudio.application.architecture;

import java.util.List;

/** Resultado simple de validación para C4 y despliegue. */
public record ArchitectureDiagramValidationResult(List<String> warnings) {
    public ArchitectureDiagramValidationResult {
        warnings = warnings == null ? List.of() : List.copyOf(warnings);
    }
    public boolean ok() { return warnings.isEmpty(); }
    public String summary() { return ok() ? "Diagrama de arquitectura sin advertencias." : warnings.size() + " advertencias en el diagrama."; }
}
