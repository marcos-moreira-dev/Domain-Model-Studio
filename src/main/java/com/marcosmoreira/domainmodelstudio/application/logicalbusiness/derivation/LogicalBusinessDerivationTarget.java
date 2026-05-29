package com.marcosmoreira.domainmodelstudio.application.logicalbusiness.derivation;

/** Destinos Markdown que pueden prepararse como borradores compatibles desde el levantamiento lógico. */
public enum LogicalBusinessDerivationTarget {
    FREE_GRAPH("free-graph", "Grafo libre", "levantamiento_grafo_libre.md"),
    LOGICAL_BUSINESS_GRAPH("logical-business-graph", "Grafo lógico del negocio", "levantamiento_grafo_logico_negocio.md"),
    CONCEPTUAL_MODEL("conceptual-model", "Modelo conceptual", "levantamiento_modelo_conceptual.md"),
    DATA_DICTIONARY("data-dictionary", "Diccionario de datos", "levantamiento_diccionario_datos.md"),
    UML_USE_CASE("uml-use-case", "UML casos de uso", "levantamiento_uml_casos_uso.md"),
    BPMN_BASIC("bpmn-basic", "BPMN básico", "levantamiento_bpmn_basico.md"),
    ROLES_PERMISSIONS("roles-permissions-map", "Roles y permisos", "levantamiento_roles_permisos.md"),
    SCREEN_FLOW("screen-flow", "Flujo de pantallas", "levantamiento_flujo_pantallas.md"),
    ADMIN_WIREFRAMES("admin-wireframes", "Wireframes administrativos", "levantamiento_wireframes.md");

    private final String diagramType;
    private final String displayName;
    private final String fileName;

    LogicalBusinessDerivationTarget(String diagramType, String displayName, String fileName) {
        this.diagramType = diagramType;
        this.displayName = displayName;
        this.fileName = fileName;
    }

    public String diagramType() {
        return diagramType;
    }

    public String displayName() {
        return displayName;
    }

    public String fileName() {
        return fileName;
    }
}
