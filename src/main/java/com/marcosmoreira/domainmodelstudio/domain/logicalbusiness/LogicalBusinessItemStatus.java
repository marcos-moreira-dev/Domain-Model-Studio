package com.marcosmoreira.domainmodelstudio.domain.logicalbusiness;

/** Estado de madurez de una sección, regla, acción o elemento del levantamiento. */
public enum LogicalBusinessItemStatus {
    EMPTY,
    DRAFT,
    PARTIAL,
    COMPLETE,
    VALIDATED,
    WITH_DOUBTS,
    BLOCKING,
    DERIVABLE,
    DISCARDED
}
