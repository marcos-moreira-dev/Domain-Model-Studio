package com.marcosmoreira.domainmodelstudio.presentation.toolbar;

import static com.marcosmoreira.domainmodelstudio.presentation.toolbar.DiagramToolbarActionFactory.action;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.List;

/** Acciones contextuales del expediente de Levantamiento lógico. */
final class LogicalBusinessToolbarContributor implements DiagramToolbarContributor {

    @Override
    public boolean supports(DiagramTypeId diagramTypeId) {
        return DiagramTypeId.LOGICAL_BUSINESS_INTAKE.equals(diagramTypeId);
    }

    @Override
    public List<DiagramToolbarAction> actionsFor(DiagramTypeId diagramTypeId) {
        return List.of(
                action(
                        DiagramToolbarActionId.LOGICAL_BUSINESS_SHOW_VALIDATION,
                        "Validar",
                        "Abrir el módulo lateral de validación del levantamiento lógico",
                        ToolbarIcon.VALIDATE_MODEL,
                        DiagramToolbarSection.MODEL,
                        DiagramToolbarAction.Width.NORMAL),
                action(
                        DiagramToolbarActionId.LOGICAL_BUSINESS_SHOW_TRACEABILITY,
                        "Trazas internas",
                        "Abrir conexiones internas del levantamiento activo",
                        ToolbarIcon.ADD_RELATIONSHIP,
                        DiagramToolbarSection.MODEL,
                        DiagramToolbarAction.Width.WIDE),
                action(
                        DiagramToolbarActionId.LOGICAL_BUSINESS_SHOW_STRUCTURE,
                        "Estructura",
                        "Abrir el árbol del expediente lógico en el SideDock",
                        ToolbarIcon.ADD_MODULE,
                        DiagramToolbarSection.VIEW,
                        DiagramToolbarAction.Width.NORMAL),
                action(
                        DiagramToolbarActionId.LOGICAL_BUSINESS_SHOW_PROPERTIES,
                        "Propiedades",
                        "Abrir la edición puntual de propiedades del nodo seleccionado",
                        ToolbarIcon.ADD_ATTRIBUTE,
                        DiagramToolbarSection.VIEW,
                        DiagramToolbarAction.Width.NORMAL),
                action(
                        DiagramToolbarActionId.LOGICAL_BUSINESS_SHOW_HELP,
                        "Glosario",
                        "Abrir ayuda, glosario y límites del levantamiento lógico",
                        ToolbarIcon.MANUAL,
                        DiagramToolbarSection.VIEW,
                        DiagramToolbarAction.Width.SMALL),
                action(
                        DiagramToolbarActionId.EXPORT_MARKDOWN,
                        "Markdown",
                        "Exportar el levantamiento lógico como Markdown canónico revisable",
                        ToolbarIcon.EXPORT_MARKDOWN,
                        DiagramToolbarSection.EXPORT,
                        DiagramToolbarAction.Width.WIDE));
    }
}
