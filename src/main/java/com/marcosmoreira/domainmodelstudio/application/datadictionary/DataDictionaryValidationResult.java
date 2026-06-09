package com.marcosmoreira.domainmodelstudio.application.datadictionary;

import java.util.List;
import java.util.Objects;

/** Resultado de revisión documental del diccionario de datos. */
public record DataDictionaryValidationResult(List<String> warnings) {

    public DataDictionaryValidationResult {
        warnings = List.copyOf(Objects.requireNonNull(warnings, "warnings"));
    }

    public boolean ok() {
        return warnings.isEmpty();
    }

    public String summary() {
        if (ok()) {
            return "Diccionario de datos sin observaciones críticas.";
        }
        return warnings.size() + " observaciones en diccionario de datos.";
    }
}
