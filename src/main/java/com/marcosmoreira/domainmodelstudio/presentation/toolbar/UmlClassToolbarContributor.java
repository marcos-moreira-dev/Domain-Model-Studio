package com.marcosmoreira.domainmodelstudio.presentation.toolbar;

import static com.marcosmoreira.domainmodelstudio.presentation.toolbar.DiagramToolbarActionFactory.action;
import static com.marcosmoreira.domainmodelstudio.presentation.toolbar.DiagramToolbarActionFactory.centerDiagramAction;
import static com.marcosmoreira.domainmodelstudio.presentation.toolbar.DiagramToolbarActionFactory.fitToContentAction;
import static com.marcosmoreira.domainmodelstudio.presentation.toolbar.DiagramToolbarActionFactory.reorganizeAction;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.List;

/** Acciones de toolbar para UML Clases. */
final class UmlClassToolbarContributor implements DiagramToolbarContributor {

    @Override
    public boolean supports(DiagramTypeId diagramTypeId) {
        return DiagramTypeId.UML_CLASS.equals(diagramTypeId);
    }

    @Override
    public List<DiagramToolbarAction> actionsFor(DiagramTypeId diagramTypeId) {
        if (diagramTypeId == null) {
            return List.of();
        }
        if (DiagramTypeId.UML_CLASS.equals(diagramTypeId)) {
            return umlClassActions();
        }
        return List.of();
    }

    private List<DiagramToolbarAction> umlClassActions() {
        return List.of(
                action(DiagramToolbarActionId.ADD_UML_MODULE, "Módulo",
                        "Agregar agrupador de módulo, carpeta o paquete",
                        ToolbarIcon.ADD_MODULE, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.ADD_UML_CLASS, "Clase",
                        "Agregar clase al módulo seleccionado",
                        ToolbarIcon.ADD_UML_CLASS, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.ADD_UML_INTERFACE, "Interfaz",
                        "Agregar interfaz al módulo seleccionado",
                        ToolbarIcon.ADD_UML_INTERFACE, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.ADD_UML_ENUM, "Enum",
                        "Agregar enum al módulo seleccionado",
                        ToolbarIcon.ADD_UML_ENUM, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.ADD_UML_ATTRIBUTE, "Atributo",
                        "Agregar atributo a la clase seleccionada",
                        ToolbarIcon.ADD_ATTRIBUTE, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.ADD_UML_METHOD, "Método",
                        "Agregar método a la clase seleccionada",
                        ToolbarIcon.ADD_UML_METHOD, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.ADD_UML_RELATION, "Relación",
                        "Agregar relación entre clases",
                        ToolbarIcon.ADD_UML_RELATION, DiagramToolbarSection.MODEL, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.IMPORT_UML_FROM_SOURCE, "Importar código", "Generar UML Clases desde una carpeta Java/TypeScript",
                        ToolbarIcon.IMPORT_MODEL, DiagramToolbarSection.MODEL, DiagramToolbarAction.Width.WIDE),
                action(DiagramToolbarActionId.OPEN_UML_SOURCE, "Abrir código",
                        "Abrir archivo fuente solo cuando la clase seleccionada tenga una ruta fuente resuelta",
                        ToolbarIcon.OPEN_PROJECT, DiagramToolbarSection.MODEL, DiagramToolbarAction.Width.WIDE),
                action(DiagramToolbarActionId.REMOVE_UML_ITEM, "Eliminar",
                        "Eliminar módulo, clase, miembro o relación seleccionada",
                        ToolbarIcon.DELETE_ELEMENT, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.VALIDATE_UML_CLASS, "Validar",
                        "Revisar módulos, clases, miembros y relaciones",
                        ToolbarIcon.VALIDATE_MODEL, DiagramToolbarSection.MODEL, DiagramToolbarAction.Width.NORMAL),
                reorganizeAction("Ordenar módulos, clases internas y relaciones principales"),
                fitToContentAction(), centerDiagramAction(),
                action(DiagramToolbarActionId.EXPORT_SVG, "SVG",
                        "Exportar UML Clases como SVG vectorial documental",
                        ToolbarIcon.EXPORT_SVG, DiagramToolbarSection.EXPORT, DiagramToolbarAction.Width.SMALL),
                action(DiagramToolbarActionId.EXPORT_MARKDOWN, "Markdown",
                        "Exportar UML Clases como Markdown",
                        ToolbarIcon.EXPORT_MARKDOWN, DiagramToolbarSection.EXPORT, DiagramToolbarAction.Width.WIDE),
                action(DiagramToolbarActionId.EXPORT_PNG, "PNG",
                        "Exportar UML Clases como PNG",
                        ToolbarIcon.EXPORT_PNG, DiagramToolbarSection.EXPORT, DiagramToolbarAction.Width.SMALL)
        );
    }

}
