package com.marcosmoreira.domainmodelstudio.presentation.workspace;

/**
 * Familia visual/documental que debe montar el shell para un tipo de diagrama.
 *
 * <p>Este enum no representa clases JavaFX concretas; funciona como contrato estable para
 * que el shell, los menús y las pruebas hablen el mismo lenguaje de producto sin repetir
 * listas dispersas de tipos.</p>
 */
public enum WorkspaceKind {
    WELCOME_HOME,
    CONCEPTUAL_CANVAS,
    DATA_DICTIONARY_DOCUMENT,
    LOGICAL_BUSINESS_DOCUMENT,
    MODULE_MAP_DIAGRAM,
    UML_CLASS_DIAGRAM,
    ROLES_PERMISSIONS_MATRIX,
    SCREEN_FLOW_DIAGRAM,
    WIREFRAME_DIAGRAM,
    ARCHITECTURE_DIAGRAM,
    BEHAVIOR_DIAGRAM,
    FREE_GRAPH_DIAGRAM,
    LOGICAL_BUSINESS_GRAPH_DIAGRAM,
    PLACEHOLDER_GUIDE
}
