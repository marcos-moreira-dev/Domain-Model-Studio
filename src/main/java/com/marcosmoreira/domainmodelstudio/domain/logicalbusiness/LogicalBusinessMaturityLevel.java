package com.marcosmoreira.domainmodelstudio.domain.logicalbusiness;

/** Nivel resumido de madurez documental del levantamiento lógico. */
public enum LogicalBusinessMaturityLevel {
    INITIAL,
    PARTIAL,
    CONSISTENT,
    SOURCE_READY,
    /**
     * Alias histórico conservado para compatibilidad con Markdown antiguo.
     * El lenguaje visible debe usar SOURCE_READY / "Usable como fuente".
     */
    DERIVABLE,
    VALIDATED
}
