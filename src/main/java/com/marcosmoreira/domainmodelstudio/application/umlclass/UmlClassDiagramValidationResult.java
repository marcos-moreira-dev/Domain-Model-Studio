package com.marcosmoreira.domainmodelstudio.application.umlclass;

import java.util.List;

/** Resultado simple de validación del diagrama UML Clases. */
public record UmlClassDiagramValidationResult(List<String> warnings) {
    public UmlClassDiagramValidationResult {
        warnings = List.copyOf(warnings == null ? List.of() : warnings);
    }

    public boolean ok() { return warnings.isEmpty(); }

    public String summary() {
        if (ok()) { return "UML Clases sin advertencias."; }
        return warnings.size() == 1 ? "1 advertencia en UML Clases." : warnings.size() + " advertencias en UML Clases.";
    }
}
