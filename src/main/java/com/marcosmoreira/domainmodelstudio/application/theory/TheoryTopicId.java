package com.marcosmoreira.domainmodelstudio.application.theory;

import java.util.Objects;

/** Identificador estable de un tema teórico. */
public record TheoryTopicId(String value) {

    public static final TheoryTopicId CONCEPTUAL_MODEL = new TheoryTopicId("modelo-conceptual");
    public static final TheoryTopicId DATA_DICTIONARY = new TheoryTopicId("diccionario-datos");
    public static final TheoryTopicId BPMN_BASIC = new TheoryTopicId("bpmn-basico");
    public static final TheoryTopicId OPERATIONAL_FLOW = new TheoryTopicId("flujo-operativo");
    public static final TheoryTopicId C4_CONTEXT_CONTAINERS = new TheoryTopicId("c4-contexto-contenedores");
    public static final TheoryTopicId TECHNICAL_DEPLOYMENT = new TheoryTopicId("despliegue-tecnico");
    public static final TheoryTopicId UML_USE_CASE = new TheoryTopicId("uml-casos-uso");
    public static final TheoryTopicId UML_CLASS = new TheoryTopicId("uml-clases");
    public static final TheoryTopicId UML_ACTIVITY = new TheoryTopicId("uml-actividad");
    public static final TheoryTopicId UML_SEQUENCE = new TheoryTopicId("uml-secuencia");
    public static final TheoryTopicId UML_STATE = new TheoryTopicId("uml-estados");
    public static final TheoryTopicId ADMIN_MODULE_MAP = new TheoryTopicId("mapa-modulos");
    public static final TheoryTopicId ROLES_PERMISSIONS = new TheoryTopicId("roles-permisos");
    public static final TheoryTopicId SCREEN_FLOW = new TheoryTopicId("flujo-pantallas");
    public static final TheoryTopicId ADMIN_WIREFRAMES = new TheoryTopicId("wireframes-administrativos");
    public static final TheoryTopicId FREE_GRAPH = new TheoryTopicId("grafo-libre");
    public static final TheoryTopicId LOGICAL_BUSINESS_INTAKE = new TheoryTopicId("levantamiento-logico");
    public static final TheoryTopicId LOGICAL_BUSINESS_GRAPH = new TheoryTopicId("grafo-logico-negocio");

    public TheoryTopicId {
        Objects.requireNonNull(value, "value");
        value = value.strip();
        if (value.isBlank()) {
            throw new IllegalArgumentException("El tema teórico no puede estar vacío.");
        }
    }

    public static TheoryTopicId of(String value) {
        return new TheoryTopicId(value);
    }
}
