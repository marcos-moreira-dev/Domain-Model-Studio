package com.marcosmoreira.domainmodelstudio.application.modulemap;

import java.util.List;

/** Resultado simple de validación del mapa de módulos. */
public record ModuleMapValidationResult(List<String> warnings) {

    public ModuleMapValidationResult {
        warnings = List.copyOf(warnings == null ? List.of() : warnings);
    }

    public boolean ok() {
        return warnings.isEmpty();
    }

    public String summary() {
        if (ok()) {
            return "Mapa de módulos sin advertencias.";
        }
        return warnings.size() == 1 ? "1 advertencia en el mapa de módulos." : warnings.size() + " advertencias en el mapa de módulos.";
    }
}
