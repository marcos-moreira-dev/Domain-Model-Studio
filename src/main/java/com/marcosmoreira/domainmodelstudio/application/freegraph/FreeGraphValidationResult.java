package com.marcosmoreira.domainmodelstudio.application.freegraph;

import java.util.List;

/** Resultado de validación funcional del Grafo libre. */
public record FreeGraphValidationResult(List<String> warnings) {

    public FreeGraphValidationResult {
        warnings = List.copyOf(warnings == null ? List.of() : warnings);
    }

    public boolean ok() {
        return warnings.isEmpty();
    }

    public String summary() {
        if (ok()) {
            return "Grafo libre sin advertencias.";
        }
        return warnings.size() == 1 ? "1 advertencia en el grafo libre." : warnings.size() + " advertencias en el grafo libre.";
    }
}
