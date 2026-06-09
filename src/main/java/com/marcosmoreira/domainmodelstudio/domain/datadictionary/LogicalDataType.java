package com.marcosmoreira.domainmodelstudio.domain.datadictionary;

/** Tipos lógicos independientes del motor SQL físico. */
public enum LogicalDataType {
    IDENTIFIER,
    SHORT_TEXT,
    LONG_TEXT,
    INTEGER_NUMBER,
    DECIMAL_NUMBER,
    MONEY,
    PERCENTAGE,
    BOOLEAN,
    DATE,
    TIME,
    DATE_TIME,
    EMAIL,
    PHONE,
    IDENTIFICATION,
    URL,
    STATUS,
    REFERENCE,
    FILE_ATTACHMENT,
    FLEXIBLE_STRUCTURE,
    UNKNOWN
}
