package com.marcosmoreira.domainmodelstudio.application.catalog.definitions;

import com.marcosmoreira.domainmodelstudio.application.catalog.DiagramTypeOfficialDefinition;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCategoryId;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramWorkspaceKind;
import java.util.List;

/** Tipos oficiales de análisis de negocio. */
public final class BusinessAnalysisDiagramTypeDefinitions {

    private BusinessAnalysisDiagramTypeDefinitions() {
    }

    public static List<DiagramTypeOfficialDefinition> all() {
        return List.of(
                DiagramTypeDefinitionFactory.logicalBusinessIntake(),
                logicalBusinessGraph()
        );
    }

    private static DiagramTypeOfficialDefinition logicalBusinessGraph() {
        return DiagramTypeDefinitionFactory.available(
                DiagramTypeId.LOGICAL_BUSINESS_GRAPH,
                "Grafo lógico del negocio",
                DiagramCategoryId.BUSINESS_ANALYSIS,
                DiagramWorkspaceKind.LOGICAL_BUSINESS_GRAPH_DIAGRAM,
                DiagramCapabilityProfiles.logicalBusinessGraphVisual(),
                "Vista visual compatible con el levantamiento lógico: macroflujos, microflujos/flujos, casos de uso, acciones, reglas, precondiciones, invariantes, postcondiciones, entidades, estados, reportes, riesgos y preguntas pendientes.",
                "grafo-logico-negocio",
                "logical-business-graph",
                "logical-business-graph",
                "logical_business_graph_minimo.md",
                "uens-grafo-logico-negocio",
                "UENS — grafo lógico del negocio escolar",
                "logical_business_graph_uens_gordito.md",
                "Macroflujos, microflujos, casos de uso, acciones, reglas, precondiciones, invariantes, postcondiciones, entidades, estados, reportes, riesgos y preguntas pendientes de la unidad educativa.",
                true);
    }
}
