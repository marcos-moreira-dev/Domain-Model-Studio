package com.marcosmoreira.domainmodelstudio.application.sourcecode;

/** Tipo de miembro detectado dentro de una clase, interfaz o estructura similar. */
public enum ParsedCodeMemberKind {
    FIELD,
    PROPERTY,
    METHOD,
    CONSTRUCTOR,
    UNKNOWN
}
