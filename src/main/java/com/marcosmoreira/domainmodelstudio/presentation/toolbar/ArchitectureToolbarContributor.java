package com.marcosmoreira.domainmodelstudio.presentation.toolbar;

import static com.marcosmoreira.domainmodelstudio.presentation.toolbar.DiagramToolbarActionFactory.action;
import static com.marcosmoreira.domainmodelstudio.presentation.toolbar.DiagramToolbarActionFactory.centerDiagramAction;
import static com.marcosmoreira.domainmodelstudio.presentation.toolbar.DiagramToolbarActionFactory.fitToContentAction;
import static com.marcosmoreira.domainmodelstudio.presentation.toolbar.DiagramToolbarActionFactory.reorganizeAction;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.List;

/** Acciones de toolbar para diagramas de arquitectura. */
final class ArchitectureToolbarContributor implements DiagramToolbarContributor {

    @Override
    public boolean supports(DiagramTypeId diagramTypeId) {
        return DiagramTypeId.C4_CONTEXT.equals(diagramTypeId)
                || DiagramTypeId.C4_CONTAINERS.equals(diagramTypeId)
                || DiagramTypeId.TECHNICAL_DEPLOYMENT.equals(diagramTypeId);
    }

    @Override
    public List<DiagramToolbarAction> actionsFor(DiagramTypeId diagramTypeId) {
        if (diagramTypeId == null) {
            return List.of();
        }
        if (DiagramTypeId.C4_CONTEXT.equals(diagramTypeId)) {
            return c4ContextActions();
        }
        if (DiagramTypeId.C4_CONTAINERS.equals(diagramTypeId)) {
            return c4ContainersActions();
        }
        if (DiagramTypeId.TECHNICAL_DEPLOYMENT.equals(diagramTypeId)) {
            return technicalDeploymentActions();
        }
        return List.of();
    }

    private List<DiagramToolbarAction> c4ContextActions() {
        return List.of(
                action(DiagramToolbarActionId.ADD_C4_PERSON, "Persona", "Agregar persona o actor externo", ToolbarIcon.ADD_C4_PERSON, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.ADD_C4_SYSTEM, "Sistema", "Agregar sistema principal", ToolbarIcon.ADD_C4_SYSTEM, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.ADD_C4_EXTERNAL_SYSTEM, "Externo", "Agregar sistema externo", ToolbarIcon.ADD_C4_SYSTEM, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.ADD_C4_BOUNDARY, "Límite", "Agregar límite del sistema", ToolbarIcon.ADD_SYSTEM_BOUNDARY, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.ADD_ARCHITECTURE_USES, "Usa", "Relacionar elementos por uso", ToolbarIcon.ADD_TRANSITION, DiagramToolbarSection.MODEL, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.ADD_ARCHITECTURE_INTEGRATION, "Integra", "Agregar integración entre sistemas", ToolbarIcon.ADD_UML_RELATION, DiagramToolbarSection.MODEL, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.REMOVE_ARCHITECTURE_ITEM, "Eliminar", "Eliminar elemento o relación seleccionada", ToolbarIcon.DELETE_ELEMENT, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.VALIDATE_ARCHITECTURE_DIAGRAM, "Validar", "Revisar consistencia del contexto", ToolbarIcon.VALIDATE_MODEL, DiagramToolbarSection.MODEL, DiagramToolbarAction.Width.NORMAL),
                reorganizeAction("Poner sistema principal al centro, personas a la izquierda y externos a la derecha"), fitToContentAction(), centerDiagramAction(),
                action(DiagramToolbarActionId.EXPORT_SVG, "SVG", "Exportar C4 Contexto como SVG vectorial documental", ToolbarIcon.EXPORT_SVG, DiagramToolbarSection.EXPORT, DiagramToolbarAction.Width.SMALL),
                action(DiagramToolbarActionId.EXPORT_MARKDOWN, "Markdown", "Exportar C4 Contexto como Markdown", ToolbarIcon.EXPORT_MARKDOWN, DiagramToolbarSection.EXPORT, DiagramToolbarAction.Width.WIDE),
                action(DiagramToolbarActionId.EXPORT_PNG, "PNG", "Exportar C4 Contexto como PNG", ToolbarIcon.EXPORT_PNG, DiagramToolbarSection.EXPORT, DiagramToolbarAction.Width.SMALL)
        );
    }

    private List<DiagramToolbarAction> c4ContainersActions() {
        return List.of(
                action(DiagramToolbarActionId.ADD_C4_CONTAINER, "Contenedor", "Agregar contenedor", ToolbarIcon.ADD_C4_CONTAINER, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.WIDE),
                action(DiagramToolbarActionId.ADD_C4_APPLICATION, "Aplicación", "Agregar aplicación", ToolbarIcon.ADD_C4_CONTAINER, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.WIDE),
                action(DiagramToolbarActionId.ADD_C4_API, "API", "Agregar API", ToolbarIcon.ADD_C4_API, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.ADD_ARCHITECTURE_DATABASE, "Base de datos", "Agregar base de datos como almacén C4 principal", ToolbarIcon.ADD_C4_DATABASE, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.WIDE),
                action(DiagramToolbarActionId.ADD_ARCHITECTURE_EXTERNAL_SERVICE, "Servicio ext.", "Agregar servicio externo", ToolbarIcon.ADD_C4_SYSTEM, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.WIDE),
                action(DiagramToolbarActionId.ADD_ARCHITECTURE_CALL, "Llama", "Agregar llamada entre contenedores", ToolbarIcon.ADD_TRANSITION, DiagramToolbarSection.MODEL, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.ADD_ARCHITECTURE_READS_WRITES, "Lee/escribe", "Agregar relación con persistencia", ToolbarIcon.ADD_UML_RELATION, DiagramToolbarSection.MODEL, DiagramToolbarAction.Width.WIDE),
                action(DiagramToolbarActionId.REMOVE_ARCHITECTURE_ITEM, "Eliminar", "Eliminar elemento o relación seleccionada", ToolbarIcon.DELETE_ELEMENT, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.VALIDATE_ARCHITECTURE_DIAGRAM, "Validar", "Revisar consistencia de contenedores", ToolbarIcon.VALIDATE_MODEL, DiagramToolbarSection.MODEL, DiagramToolbarAction.Width.NORMAL),
                reorganizeAction("Ordenar aplicaciones, APIs, bases de datos y externos por capas C4"), fitToContentAction(), centerDiagramAction(),
                action(DiagramToolbarActionId.EXPORT_SVG, "SVG", "Exportar C4 Contenedores como SVG vectorial documental", ToolbarIcon.EXPORT_SVG, DiagramToolbarSection.EXPORT, DiagramToolbarAction.Width.SMALL),
                action(DiagramToolbarActionId.EXPORT_MARKDOWN, "Markdown", "Exportar C4 Contenedores como Markdown", ToolbarIcon.EXPORT_MARKDOWN, DiagramToolbarSection.EXPORT, DiagramToolbarAction.Width.WIDE),
                action(DiagramToolbarActionId.EXPORT_PNG, "PNG", "Exportar C4 Contenedores como PNG", ToolbarIcon.EXPORT_PNG, DiagramToolbarSection.EXPORT, DiagramToolbarAction.Width.SMALL)
        );
    }

    private List<DiagramToolbarAction> technicalDeploymentActions() {
        return List.of(
                action(DiagramToolbarActionId.ADD_DEPLOYMENT_ENVIRONMENT, "Ambiente", "Agregar ambiente de despliegue", ToolbarIcon.ADD_C4_CONTAINER, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.WIDE),
                action(DiagramToolbarActionId.ADD_DEPLOYMENT_SERVER, "Servidor", "Agregar servidor", ToolbarIcon.ADD_DEPLOYMENT_SERVER, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.ADD_DEPLOYMENT_CLIENT, "Cliente", "Agregar cliente o estación de trabajo", ToolbarIcon.ADD_DEPLOYMENT_CLIENT, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.ADD_DEPLOYMENT_SERVICE, "Servicio", "Agregar servicio desplegado", ToolbarIcon.ADD_C4_API, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.ADD_ARCHITECTURE_DATABASE, "Base de datos", "Agregar base de datos como componente de despliegue", ToolbarIcon.ADD_C4_DATABASE, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.WIDE),
                action(DiagramToolbarActionId.ADD_DEPLOYMENT_NETWORK, "Red", "Agregar red o segmento", ToolbarIcon.ADD_DEPLOYMENT_NETWORK, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.ADD_DEPLOYMENT_ARTIFACT, "Artefacto", "Agregar artefacto desplegable", ToolbarIcon.ADD_DEPLOYMENT_ARTIFACT, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.ADD_DEPLOYMENT_CONNECTION, "Conexión", "Agregar conexión técnica", ToolbarIcon.ADD_TRANSITION, DiagramToolbarSection.MODEL, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.ADD_DEPLOYMENT_HOSTING, "Aloja", "Marcar alojamiento de servicio", ToolbarIcon.ADD_UML_RELATION, DiagramToolbarSection.MODEL, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.ADD_DEPLOYMENT_TARGET, "Despliega", "Relacionar artefacto con ambiente", ToolbarIcon.ADD_UML_RELATION, DiagramToolbarSection.MODEL, DiagramToolbarAction.Width.WIDE),
                action(DiagramToolbarActionId.REMOVE_ARCHITECTURE_ITEM, "Eliminar", "Eliminar elemento o relación seleccionada", ToolbarIcon.DELETE_ELEMENT, DiagramToolbarSection.ELEMENTS, DiagramToolbarAction.Width.NORMAL),
                action(DiagramToolbarActionId.VALIDATE_ARCHITECTURE_DIAGRAM, "Validar", "Revisar consistencia de despliegue", ToolbarIcon.VALIDATE_MODEL, DiagramToolbarSection.MODEL, DiagramToolbarAction.Width.NORMAL),
                fitToContentAction(), centerDiagramAction(),
                action(DiagramToolbarActionId.EXPORT_SVG, "SVG", "Exportar despliegue técnico como SVG vectorial documental", ToolbarIcon.EXPORT_SVG, DiagramToolbarSection.EXPORT, DiagramToolbarAction.Width.SMALL),
                action(DiagramToolbarActionId.EXPORT_MARKDOWN, "Markdown", "Exportar despliegue técnico como Markdown", ToolbarIcon.EXPORT_MARKDOWN, DiagramToolbarSection.EXPORT, DiagramToolbarAction.Width.WIDE),
                action(DiagramToolbarActionId.EXPORT_PNG, "PNG", "Exportar despliegue técnico como PNG", ToolbarIcon.EXPORT_PNG, DiagramToolbarSection.EXPORT, DiagramToolbarAction.Width.SMALL)
        );
    }

}
