package com.marcosmoreira.domainmodelstudio.domain.catalog;

/**
 * Familia de workspace declarada como parte del contrato de producto de un tipo.
 *
 * <p>No representa una clase JavaFX concreta. Es una decisión estable de producto
 * para que shell, toolbars, sidebars, exportadores y documentación no inventen
 * rutas distintas para el mismo tipo de proyecto.</p>
 */
public enum DiagramWorkspaceKind {
    CONCEPTUAL_CANVAS("Lienzo conceptual"),
    DATA_DICTIONARY_DOCUMENT("Documento de diccionario"),
    LOGICAL_BUSINESS_DOCUMENT("Documento de levantamiento lógico"),
    MODULE_MAP_DIAGRAM("Diagrama de módulos"),
    UML_CLASS_DIAGRAM("Diagrama UML de clases"),
    ROLES_PERMISSIONS_MATRIX("Matriz de roles y permisos"),
    SCREEN_FLOW_DIAGRAM("Diagrama de flujo de pantallas"),
    WIREFRAME_DIAGRAM("Maqueta administrativa"),
    ARCHITECTURE_DIAGRAM("Diagrama de arquitectura"),
    BEHAVIOR_DIAGRAM("Diagrama de comportamiento"),
    FREE_GRAPH_DIAGRAM("Grafo libre"),
    LOGICAL_BUSINESS_GRAPH_DIAGRAM("Grafo lógico del negocio"),
    PLACEHOLDER_GUIDE("Guía de preparación");

    private final String displayName;

    DiagramWorkspaceKind(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}
