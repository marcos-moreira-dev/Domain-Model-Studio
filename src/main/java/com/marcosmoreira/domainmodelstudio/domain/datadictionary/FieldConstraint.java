package com.marcosmoreira.domainmodelstudio.domain.datadictionary;

/** Restricciones lógicas documentables en un campo del diccionario. */
public enum FieldConstraint {
    REQUIRED,
    UNIQUE,
    PRIMARY_KEY,
    FOREIGN_KEY,
    DERIVED,
    READ_ONLY,
    VISIBLE_IN_FORM,
    VISIBLE_IN_REPORT
}
