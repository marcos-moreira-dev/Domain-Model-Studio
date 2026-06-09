package com.marcosmoreira.domainmodelstudio.presentation.drawing;

/**
 * Catálogo semántico de símbolos visuales usados por los render kits.
 *
 * <p>El símbolo describe qué representa la figura dentro del lenguaje de
 * diagramas. No describe cómo se selecciona, arrastra, persiste o exporta.</p>
 */
public enum DiagramSymbol {
    UML_ACTOR,
    UML_USE_CASE,
    UML_SYSTEM_BOUNDARY,
    UML_CLASS,
    UML_INTERFACE,
    UML_INITIAL_NODE,
    UML_FINAL_NODE,
    UML_ACTION,
    UML_DECISION,
    UML_FORK_JOIN,
    UML_STATE,
    UML_NOTE,

    BPMN_START_EVENT,
    BPMN_END_EVENT,
    BPMN_TASK,
    BPMN_GATEWAY,
    BPMN_DOCUMENT,
    BPMN_LANE,

    OPERATIONAL_STEP,
    OPERATIONAL_RESPONSIBLE,
    OPERATIONAL_DECISION,
    OPERATIONAL_DOCUMENT,

    C4_PERSON,
    C4_CLIENT,
    C4_SYSTEM,
    C4_EXTERNAL_SYSTEM,
    C4_APPLICATION,
    C4_API,
    C4_CONTAINER,
    C4_SERVICE,
    C4_DATABASE,
    C4_BOUNDARY,
    C4_ENVIRONMENT,
    C4_NETWORK,
    C4_SERVER,
    C4_ARTIFACT,

    ADMIN_MODULE,
    ADMIN_SCREEN,
    ADMIN_PERMISSION_MATRIX,

    WIREFRAME_SCREEN,
    WIREFRAME_TABLE,
    WIREFRAME_FIELD,
    WIREFRAME_BUTTON
}
