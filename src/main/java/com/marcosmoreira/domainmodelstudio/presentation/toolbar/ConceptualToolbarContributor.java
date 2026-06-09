package com.marcosmoreira.domainmodelstudio.presentation.toolbar;

import static com.marcosmoreira.domainmodelstudio.presentation.toolbar.DiagramToolbarActionFactory.action;
import static com.marcosmoreira.domainmodelstudio.presentation.toolbar.DiagramToolbarActionFactory.centerDiagramAction;
import static com.marcosmoreira.domainmodelstudio.presentation.toolbar.DiagramToolbarActionFactory.fitToContentAction;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.presentation.conceptual.toolbar.ConceptualToolbarContract;
import java.util.List;

/** Acciones contextuales del modelo conceptual dentro del workbench común. */
final class ConceptualToolbarContributor implements DiagramToolbarContributor {

    @Override
    public boolean supports(DiagramTypeId diagramTypeId) {
        return DiagramTypeId.CONCEPTUAL_MODEL.equals(diagramTypeId);
    }

    @Override
    public List<DiagramToolbarAction> actionsFor(DiagramTypeId diagramTypeId) {
        if (!supports(diagramTypeId)) {
            return List.of();
        }
        return conceptualModelActions();
    }

    private List<DiagramToolbarAction> conceptualModelActions() {
        List<DiagramToolbarAction> actions = List.of(
                action(DiagramToolbarActionId.ADD_ENTITY, "Entidad",
                        "Agregar una entidad al modelo conceptual",
                        ToolbarIcon.ADD_ENTITY, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.ADD_ATTRIBUTE, "Atributo",
                        "Agregar un atributo a la entidad seleccionada",
                        ToolbarIcon.ADD_ATTRIBUTE, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.ADD_RELATIONSHIP, "Relación",
                        "Crear una relación seleccionando entidad origen y destino",
                        ToolbarIcon.ADD_RELATIONSHIP, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.DUPLICATE_ELEMENT, "Duplicar",
                        "Duplicar la entidad seleccionada",
                        ToolbarIcon.DUPLICATE_ELEMENT, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.DELETE_ELEMENT, "Eliminar",
                        "Eliminar el elemento seleccionado del modelo",
                        ToolbarIcon.DELETE_ELEMENT, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.DELETE_SELECTED_BEND_POINT, "Quitar punto",
                        "Eliminar el punto intermedio seleccionado de una línea; también funciona Suprimir/Backspace",
                        ToolbarIcon.DELETE_ELEMENT, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.WIDE),
                action(DiagramToolbarActionId.VALIDATE_MODEL, "Validar",
                        "Revisar entidades, relaciones, cardinalidades y atributos",
                        ToolbarIcon.VALIDATE_MODEL, DiagramToolbarSection.MODEL, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.REORGANIZE_DIAGRAM, "Reorganizar",
                        "Reorganizar automáticamente el diagrama actual",
                        ToolbarIcon.REORGANIZE_DIAGRAM, DiagramToolbarSection.MODEL, DiagramToolbarAction.Width.WIDE),
                action(DiagramToolbarActionId.SWITCH_TO_CHEN, "Chen",
                        "Mostrar el diagrama en notación Chen",
                        ToolbarIcon.NOTATION_CHEN, DiagramToolbarSection.NOTATION, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.SWITCH_TO_CROWS_FOOT, "Pata de gallo",
                        "Mostrar el diagrama en notación pata de gallo",
                        ToolbarIcon.NOTATION_CROWS_FOOT, DiagramToolbarSection.NOTATION, DiagramToolbarAction.Width.WIDE),
                action(DiagramToolbarActionId.ZOOM_IN, "",
                        "Acercar la vista del diagrama",
                        ToolbarIcon.ZOOM_IN, DiagramToolbarSection.VIEW, DiagramToolbarAction.Width.TINY),
                action(DiagramToolbarActionId.ZOOM_OUT, "",
                        "Alejar la vista del diagrama",
                        ToolbarIcon.ZOOM_OUT, DiagramToolbarSection.VIEW, DiagramToolbarAction.Width.TINY),
                action(DiagramToolbarActionId.RESET_ZOOM, "100%",
                        "Restablecer el zoom al 100%",
                        ToolbarIcon.ZOOM_ACTUAL, DiagramToolbarSection.VIEW, DiagramToolbarAction.Width.SMALL),
                fitToContentAction(),
                centerDiagramAction(),
                action(DiagramToolbarActionId.CENTER_SELECTION, "Centrar selección",
                        "Centrar la vista en la selección actual",
                        ToolbarIcon.CENTER_SELECTION, DiagramToolbarSection.VIEW, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.EXPORT_SVG, "SVG",
                        "Exportar el modelo conceptual como SVG vectorial documental",
                        ToolbarIcon.EXPORT_SVG, DiagramToolbarSection.EXPORT, DiagramToolbarAction.Width.SMALL),
                action(DiagramToolbarActionId.EXPORT_MARKDOWN, "Markdown",
                        "Generar un Markdown actualizado desde el diagrama actual",
                        ToolbarIcon.EXPORT_MARKDOWN, DiagramToolbarSection.EXPORT, DiagramToolbarAction.Width.WIDE),
                action(DiagramToolbarActionId.EXPORT_PNG, "PNG",
                        "Exportar el diagrama como PNG",
                        ToolbarIcon.EXPORT_PNG, DiagramToolbarSection.EXPORT, DiagramToolbarAction.Width.SMALL)
        );
        if (!actions.stream().map(DiagramToolbarAction::id).toList().equals(ConceptualToolbarContract.actions())) {
            throw new IllegalStateException("Las acciones conceptuales visibles deben seguir el contrato transversal conceptual.");
        }
        return actions;
    }
}
