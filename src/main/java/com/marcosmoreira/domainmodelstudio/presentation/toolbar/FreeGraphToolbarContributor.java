package com.marcosmoreira.domainmodelstudio.presentation.toolbar;

import static com.marcosmoreira.domainmodelstudio.presentation.toolbar.DiagramToolbarActionFactory.action;
import static com.marcosmoreira.domainmodelstudio.presentation.toolbar.DiagramToolbarActionFactory.centerDiagramAction;
import static com.marcosmoreira.domainmodelstudio.presentation.toolbar.DiagramToolbarActionFactory.fitToContentAction;
import static com.marcosmoreira.domainmodelstudio.presentation.toolbar.DiagramToolbarActionFactory.reorganizeAction;
import static com.marcosmoreira.domainmodelstudio.presentation.toolbar.DiagramToolbarActionFactory.transferSelectionAction;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.List;

/** Acciones de toolbar para Grafo libre. */
final class FreeGraphToolbarContributor implements DiagramToolbarContributor {

    @Override
    public boolean supports(DiagramTypeId diagramTypeId) {
        return DiagramTypeId.FREE_GRAPH.equals(diagramTypeId);
    }

    @Override
    public List<DiagramToolbarAction> actionsFor(DiagramTypeId diagramTypeId) {
        if (diagramTypeId == null) {
            return List.of();
        }
        if (DiagramTypeId.FREE_GRAPH.equals(diagramTypeId)) {
            return freeGraphActions();
        }
        return List.of();
    }

    private List<DiagramToolbarAction> freeGraphActions() {
        return List.of(
                action(DiagramToolbarActionId.FREE_GRAPH_ADD_NODE_TOOL, "Agregar nodo",
                        "Activar o desactivar creación de nodos haciendo clic en el lienzo",
                        ToolbarIcon.ADD_ENTITY, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.WIDE),
                action(DiagramToolbarActionId.FREE_GRAPH_ADD_EDGE_TOOL, "Agregar relación",
                        "Activar o desactivar creación de relaciones; permite autorrelación al repetir nodo",
                        ToolbarIcon.ADD_RELATIONSHIP, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.WIDE),
                action(DiagramToolbarActionId.REMOVE_FREE_GRAPH_ITEM, "Eliminar",
                        "Eliminar nodo o relación seleccionada",
                        ToolbarIcon.DELETE_ELEMENT, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                transferSelectionAction(),
                action(DiagramToolbarActionId.VALIDATE_FREE_GRAPH, "Validar",
                        "Revisar nodos aislados, relaciones y etiquetas",
                        ToolbarIcon.VALIDATE_MODEL, DiagramToolbarSection.MODEL, DiagramToolbarAction.Width.NORMAL),
                reorganizeAction("Ordenar nodos del grafo en una distribución radial"),
                fitToContentAction(), centerDiagramAction(),
                action(DiagramToolbarActionId.EXPORT_SVG, "SVG",
                        "Exportar Grafo libre como SVG vectorial documental",
                        ToolbarIcon.EXPORT_SVG, DiagramToolbarSection.EXPORT, DiagramToolbarAction.Width.SMALL),
                action(DiagramToolbarActionId.EXPORT_MARKDOWN, "Markdown",
                        "Exportar Grafo libre como Markdown",
                        ToolbarIcon.EXPORT_MARKDOWN, DiagramToolbarSection.EXPORT, DiagramToolbarAction.Width.WIDE),
                action(DiagramToolbarActionId.EXPORT_PNG, "PNG",
                        "Exportar Grafo libre como PNG",
                        ToolbarIcon.EXPORT_PNG, DiagramToolbarSection.EXPORT, DiagramToolbarAction.Width.SMALL)
        );
    }

}
