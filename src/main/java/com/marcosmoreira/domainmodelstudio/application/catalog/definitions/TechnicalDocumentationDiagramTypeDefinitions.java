package com.marcosmoreira.domainmodelstudio.application.catalog.definitions;

import com.marcosmoreira.domainmodelstudio.application.catalog.DiagramTypeOfficialDefinition;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCategoryId;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramWorkspaceKind;
import java.util.List;

/** Tipos oficiales de documentación técnica libre. */
public final class TechnicalDocumentationDiagramTypeDefinitions {

    private TechnicalDocumentationDiagramTypeDefinitions() {
    }

    public static List<DiagramTypeOfficialDefinition> all() {
        return List.of(DiagramTypeDefinitionFactory.available(DiagramTypeId.FREE_GRAPH, "Grafo libre", DiagramCategoryId.TECHNICAL_DOCUMENTATION,
                DiagramWorkspaceKind.FREE_GRAPH_DIAGRAM, DiagramCapabilityProfiles.freeGraph(),
                "Dibuja nodos y relaciones libres con título, contenido y etiquetas opcionales.",
                "grafo-libre", "grafo-libre-gramatica", "free-graph",
                "free_graph_minimo.md", "uens-grafo-libre",
                "UENS — grafo libre de relaciones escolares", "free_graph_uens_gordito.md",
                "Relaciones generales entre estudiantes, representantes, secretaría, asignación vigente, clases, calificaciones, reportes y auditoría.", true));
    }
}
