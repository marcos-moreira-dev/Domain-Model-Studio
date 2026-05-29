package com.marcosmoreira.domainmodelstudio.domain.datadictionary;

/** Clasificación operativa de una entidad lógica dentro del diccionario. */
public enum DataEntityKind {
    MAIN,
    CATALOG,
    TRANSACTIONAL,
    ASSOCIATIVE,
    AUDIT,
    SUPPORT
}
