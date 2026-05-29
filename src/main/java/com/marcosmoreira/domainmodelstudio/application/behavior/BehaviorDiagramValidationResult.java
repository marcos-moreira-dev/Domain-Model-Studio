package com.marcosmoreira.domainmodelstudio.application.behavior;

import java.util.List;

/** Resultado de validación de procesos y UML de comportamiento. */
public record BehaviorDiagramValidationResult(List<String> warnings) {
    public BehaviorDiagramValidationResult { warnings = List.copyOf(warnings == null ? List.of() : warnings); }
    public boolean ok() { return warnings.isEmpty(); }
    public String summary() { return ok() ? "Diagrama válido." : warnings.size() + " advertencia(s) encontradas."; }
}
