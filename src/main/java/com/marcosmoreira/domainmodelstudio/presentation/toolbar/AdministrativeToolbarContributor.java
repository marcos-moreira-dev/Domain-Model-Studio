package com.marcosmoreira.domainmodelstudio.presentation.toolbar;

import static com.marcosmoreira.domainmodelstudio.presentation.toolbar.DiagramToolbarActionFactory.action;
import static com.marcosmoreira.domainmodelstudio.presentation.toolbar.DiagramToolbarActionFactory.centerDiagramAction;
import static com.marcosmoreira.domainmodelstudio.presentation.toolbar.DiagramToolbarActionFactory.fitToContentAction;
import static com.marcosmoreira.domainmodelstudio.presentation.toolbar.DiagramToolbarActionFactory.reorganizeAction;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.List;

/** Acciones de toolbar para módulos administrativos. */
final class AdministrativeToolbarContributor implements DiagramToolbarContributor {

    @Override
    public boolean supports(DiagramTypeId diagramTypeId) {
        return DiagramTypeId.ADMIN_MODULE_MAP.equals(diagramTypeId)
                || DiagramTypeId.ROLES_PERMISSIONS_MAP.equals(diagramTypeId)
                || DiagramTypeId.SCREEN_FLOW.equals(diagramTypeId)
                || DiagramTypeId.ADMIN_WIREFRAMES.equals(diagramTypeId);
    }

    @Override
    public List<DiagramToolbarAction> actionsFor(DiagramTypeId diagramTypeId) {
        if (diagramTypeId == null) {
            return List.of();
        }
        if (DiagramTypeId.ADMIN_MODULE_MAP.equals(diagramTypeId)) {
            return moduleMapActions();
        }
        if (DiagramTypeId.ROLES_PERMISSIONS_MAP.equals(diagramTypeId)) {
            return rolesPermissionsActions();
        }
        if (DiagramTypeId.SCREEN_FLOW.equals(diagramTypeId)) {
            return screenFlowActions();
        }
        if (DiagramTypeId.ADMIN_WIREFRAMES.equals(diagramTypeId)) {
            return wireframeActions();
        }
        return List.of();
    }

    private List<DiagramToolbarAction> moduleMapActions() {
        return List.of(
                action(DiagramToolbarActionId.ADD_MODULE, "Módulo",
                        "Agregar módulo principal al mapa",
                        ToolbarIcon.ADD_MODULE, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.ADD_SUBMODULE, "Submódulo",
                        "Agregar submódulo al módulo seleccionado",
                        ToolbarIcon.ADD_SUBMODULE, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.WIDE),
                action(DiagramToolbarActionId.ADD_MODULE_DEPENDENCY, "Dependencia",
                        "Agregar dependencia desde el módulo seleccionado hacia otro módulo",
                        ToolbarIcon.ADD_MODULE_DEPENDENCY, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.WIDE),
                action(DiagramToolbarActionId.REMOVE_MODULE_MAP_ITEM, "Eliminar",
                        "Eliminar módulo o dependencia seleccionada",
                        ToolbarIcon.DELETE_ELEMENT, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.VALIDATE_MODULE_MAP, "Validar",
                        "Revisar módulos duplicados, dependencias y descripciones faltantes",
                        ToolbarIcon.VALIDATE_MODEL, DiagramToolbarSection.MODEL, DiagramToolbarAction.Width.NORMAL),
                reorganizeAction("Ordenar módulos por jerarquía y dependencias funcionales"),
                fitToContentAction(), centerDiagramAction(),
                action(DiagramToolbarActionId.EXPORT_SVG, "SVG",
                        "Exportar mapa de módulos como SVG vectorial documental",
                        ToolbarIcon.EXPORT_SVG, DiagramToolbarSection.EXPORT, DiagramToolbarAction.Width.SMALL),
                action(DiagramToolbarActionId.EXPORT_MARKDOWN, "Markdown",
                        "Exportar mapa de módulos como Markdown",
                        ToolbarIcon.EXPORT_MARKDOWN, DiagramToolbarSection.EXPORT, DiagramToolbarAction.Width.WIDE),
                action(DiagramToolbarActionId.EXPORT_PNG, "PNG",
                        "Exportar mapa de módulos como PNG",
                        ToolbarIcon.EXPORT_PNG, DiagramToolbarSection.EXPORT, DiagramToolbarAction.Width.SMALL)
        );
    }

    private List<DiagramToolbarAction> rolesPermissionsActions() {
        return List.of(
                action(DiagramToolbarActionId.ADD_ROLE, "Rol", "Agregar rol operativo", ToolbarIcon.ADD_ROLE, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.ADD_PERMISSION, "Permiso", "Agregar permiso funcional", ToolbarIcon.ADD_PERMISSION, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.ADD_PERMISSION_ASSIGNMENT, "Asignar", "Asignar permiso al rol seleccionado", ToolbarIcon.ADD_ASSIGNMENT, DiagramToolbarSection.MODEL, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.REMOVE_ROLES_PERMISSIONS_ITEM, "Eliminar", "Eliminar rol, permiso o asignación seleccionada", ToolbarIcon.DELETE_ELEMENT, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.VALIDATE_ROLES_PERMISSIONS, "Validar", "Revisar roles sin permisos y asignaciones inconsistentes", ToolbarIcon.VALIDATE_MODEL, DiagramToolbarSection.MODEL, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.EXPORT_SVG, "SVG", "Exportar matriz de roles y permisos como SVG vectorial documental", ToolbarIcon.EXPORT_SVG, DiagramToolbarSection.EXPORT, DiagramToolbarAction.Width.SMALL),
                action(DiagramToolbarActionId.EXPORT_MARKDOWN, "Markdown", "Exportar roles y permisos como Markdown", ToolbarIcon.EXPORT_MARKDOWN, DiagramToolbarSection.EXPORT, DiagramToolbarAction.Width.WIDE),
                action(DiagramToolbarActionId.EXPORT_PNG, "PNG", "Exportar matriz de roles y permisos como PNG", ToolbarIcon.EXPORT_PNG, DiagramToolbarSection.EXPORT, DiagramToolbarAction.Width.SMALL)
        );
    }

    private List<DiagramToolbarAction> screenFlowActions() {
        return List.of(
                action(DiagramToolbarActionId.ADD_SCREEN, "Pantalla", "Agregar pantalla al flujo", ToolbarIcon.ADD_SCREEN, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.ADD_SCREEN_TRANSITION, "Transición", "Agregar transición entre pantallas", ToolbarIcon.ADD_TRANSITION, DiagramToolbarSection.MODEL, DiagramToolbarAction.Width.WIDE),
                action(DiagramToolbarActionId.REMOVE_SCREEN_FLOW_ITEM, "Eliminar", "Eliminar pantalla o transición seleccionada", ToolbarIcon.DELETE_ELEMENT, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.VALIDATE_SCREEN_FLOW, "Validar", "Revisar pantallas y transiciones", ToolbarIcon.VALIDATE_MODEL, DiagramToolbarSection.MODEL, DiagramToolbarAction.Width.NORMAL),
                fitToContentAction(), centerDiagramAction(),
                action(DiagramToolbarActionId.EXPORT_SVG, "SVG", "Exportar flujo de pantallas como SVG vectorial documental", ToolbarIcon.EXPORT_SVG, DiagramToolbarSection.EXPORT, DiagramToolbarAction.Width.SMALL),
                action(DiagramToolbarActionId.EXPORT_MARKDOWN, "Markdown", "Exportar flujo de pantallas como Markdown", ToolbarIcon.EXPORT_MARKDOWN, DiagramToolbarSection.EXPORT, DiagramToolbarAction.Width.WIDE),
                action(DiagramToolbarActionId.EXPORT_PNG, "PNG", "Exportar flujo de pantallas como PNG", ToolbarIcon.EXPORT_PNG, DiagramToolbarSection.EXPORT, DiagramToolbarAction.Width.SMALL)
        );
    }

    private List<DiagramToolbarAction> wireframeActions() {
        return List.of(
                action(DiagramToolbarActionId.ADD_WIREFRAME_SCREEN, "Pantalla", "Agregar pantalla de wireframe", ToolbarIcon.ADD_WIREFRAME_SCREEN, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.ADD_WIREFRAME_SECTION, "Sección", "Agregar sección a la pantalla seleccionada", ToolbarIcon.ADD_WIREFRAME_SECTION, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.ADD_WIREFRAME_FORM, "Formulario", "Agregar formulario", ToolbarIcon.ADD_WIREFRAME_FORM, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.WIDE),
                action(DiagramToolbarActionId.ADD_WIREFRAME_TABLE, "Tabla", "Agregar tabla", ToolbarIcon.ADD_WIREFRAME_TABLE, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.ADD_WIREFRAME_FIELD, "Campo", "Agregar campo", ToolbarIcon.ADD_WIREFRAME_FIELD, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.ADD_WIREFRAME_BUTTON, "Botón", "Agregar botón", ToolbarIcon.ADD_WIREFRAME_BUTTON, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.APPLY_WIREFRAME_TEMPLATE, "Insertar",
                        "Insertar una plantilla de pantalla desde la barra contextual",
                        ToolbarIcon.ADD_WIREFRAME_SCREEN, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.WIDE),
                action(DiagramToolbarActionId.REMOVE_WIREFRAME_ITEM, "Eliminar", "Eliminar pantalla o componente seleccionado", ToolbarIcon.DELETE_ELEMENT, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.VALIDATE_WIREFRAME, "Validar", "Revisar pantallas sin componentes", ToolbarIcon.VALIDATE_MODEL, DiagramToolbarSection.MODEL, DiagramToolbarAction.Width.NORMAL),
                fitToContentAction(), centerDiagramAction(),
                action(DiagramToolbarActionId.EXPORT_SVG, "SVG", "Exportar wireframes como SVG vectorial documental", ToolbarIcon.EXPORT_SVG, DiagramToolbarSection.EXPORT, DiagramToolbarAction.Width.SMALL),
                action(DiagramToolbarActionId.EXPORT_MARKDOWN, "Markdown", "Exportar wireframes como Markdown", ToolbarIcon.EXPORT_MARKDOWN, DiagramToolbarSection.EXPORT, DiagramToolbarAction.Width.WIDE),
                action(DiagramToolbarActionId.EXPORT_PNG, "PNG", "Exportar wireframes como PNG", ToolbarIcon.EXPORT_PNG, DiagramToolbarSection.EXPORT, DiagramToolbarAction.Width.SMALL)
        );
    }

}
