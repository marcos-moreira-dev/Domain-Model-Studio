package com.marcosmoreira.domainmodelstudio.domain.catalog;

import java.util.Objects;

/** Identificador estable de un tipo de diagrama. */
public record DiagramTypeId(String value) {

    public static final DiagramTypeId CONCEPTUAL_MODEL = new DiagramTypeId("conceptual-model");
    public static final DiagramTypeId DATA_DICTIONARY = new DiagramTypeId("data-dictionary");
    public static final DiagramTypeId BPMN_BASIC = new DiagramTypeId("bpmn-basic");
    public static final DiagramTypeId OPERATIONAL_FLOW = new DiagramTypeId("operational-flow");
    public static final DiagramTypeId C4_CONTEXT = new DiagramTypeId("c4-context");
    public static final DiagramTypeId C4_CONTAINERS = new DiagramTypeId("c4-containers");
    public static final DiagramTypeId TECHNICAL_DEPLOYMENT = new DiagramTypeId("technical-deployment");
    public static final DiagramTypeId UML_USE_CASE = new DiagramTypeId("uml-use-case");
    public static final DiagramTypeId UML_CLASS = new DiagramTypeId("uml-class");
    public static final DiagramTypeId UML_ACTIVITY = new DiagramTypeId("uml-activity");
    public static final DiagramTypeId UML_SEQUENCE = new DiagramTypeId("uml-sequence");
    public static final DiagramTypeId UML_STATE = new DiagramTypeId("uml-state");
    public static final DiagramTypeId ADMIN_MODULE_MAP = new DiagramTypeId("admin-module-map");
    public static final DiagramTypeId ROLES_PERMISSIONS_MAP = new DiagramTypeId("roles-permissions-map");
    public static final DiagramTypeId SCREEN_FLOW = new DiagramTypeId("screen-flow");
    public static final DiagramTypeId ADMIN_WIREFRAMES = new DiagramTypeId("admin-wireframes");
    public static final DiagramTypeId FREE_GRAPH = new DiagramTypeId("free-graph");
    public static final DiagramTypeId LOGICAL_BUSINESS_INTAKE = new DiagramTypeId("logical-business-intake");
    public static final DiagramTypeId LOGICAL_BUSINESS_GRAPH = new DiagramTypeId("logical-business-graph");

    public DiagramTypeId {
        Objects.requireNonNull(value, "value");
        value = value.strip();
        if (value.isBlank()) {
            throw new IllegalArgumentException("El identificador de tipo de diagrama no puede estar vacío.");
        }
    }

    public static DiagramTypeId of(String value) {
        return new DiagramTypeId(value);
    }
}
