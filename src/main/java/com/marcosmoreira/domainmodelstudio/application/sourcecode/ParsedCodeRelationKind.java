package com.marcosmoreira.domainmodelstudio.application.sourcecode;

/** Relación neutral inferida desde código antes de mapearla a conectores UML. */
public enum ParsedCodeRelationKind {
    EXTENDS,
    IMPLEMENTS,
    ASSOCIATION,
    AGGREGATION,
    COMPOSITION,
    DEPENDENCY,
    API_CALL,
    UNKNOWN
}
