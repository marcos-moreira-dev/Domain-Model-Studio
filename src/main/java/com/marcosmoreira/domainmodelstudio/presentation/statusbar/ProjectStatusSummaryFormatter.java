package com.marcosmoreira.domainmodelstudio.presentation.statusbar;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.util.Objects;

/**
 * Formatea la barra de estado con vocabulario propio del tipo de diagrama activo.
 *
 * <p>Evita que el shell trate todo proyecto como modelo conceptual y concentra los
 * conteos visibles en una clase pequeña, fácil de probar y mantener.</p>
 */
public final class ProjectStatusSummaryFormatter {

    public ProjectStatusSummary summarize(DiagramProject project) {
        Objects.requireNonNull(project, "project");
        DiagramTypeId typeId = project.metadata().diagramTypeId();
        return new ProjectStatusSummary(viewLabelFor(project), elementSummaryFor(project, typeId));
    }

    private String viewLabelFor(DiagramProject project) {
        DiagramTypeId typeId = project.metadata().diagramTypeId();
        if (DiagramTypeId.CONCEPTUAL_MODEL.equals(typeId)) {
            return displayNotation(project.metadata().activeNotation().displayName());
        }
        if (DiagramTypeId.DATA_DICTIONARY.equals(typeId)) return "Diccionario";
        if (DiagramTypeId.ADMIN_MODULE_MAP.equals(typeId)) return "Mapa de módulos";
        if (DiagramTypeId.UML_CLASS.equals(typeId)) return "UML Clases";
        if (DiagramTypeId.ROLES_PERMISSIONS_MAP.equals(typeId)) return "Roles y permisos";
        if (DiagramTypeId.SCREEN_FLOW.equals(typeId)) return "Flujo de pantallas";
        if (DiagramTypeId.ADMIN_WIREFRAMES.equals(typeId)) return "Wireframes";
        if (DiagramTypeId.BPMN_BASIC.equals(typeId)) return "BPMN básico";
        if (DiagramTypeId.OPERATIONAL_FLOW.equals(typeId)) return "Flujo operativo";
        if (DiagramTypeId.UML_USE_CASE.equals(typeId)) return "UML Casos de uso";
        if (DiagramTypeId.UML_ACTIVITY.equals(typeId)) return "UML Actividad";
        if (DiagramTypeId.UML_SEQUENCE.equals(typeId)) return "UML Secuencia";
        if (DiagramTypeId.UML_STATE.equals(typeId)) return "UML Estados";
        if (DiagramTypeId.C4_CONTEXT.equals(typeId)) return "C4 Contexto";
        if (DiagramTypeId.C4_CONTAINERS.equals(typeId)) return "C4 Contenedores";
        if (DiagramTypeId.TECHNICAL_DEPLOYMENT.equals(typeId)) return "Despliegue técnico";
        return typeId.value();
    }

    private String elementSummaryFor(DiagramProject project, DiagramTypeId typeId) {
        if (DiagramTypeId.DATA_DICTIONARY.equals(typeId)) {
            return project.dataDictionary()
                    .map(document -> document.entityCount() + " entidades / " + document.fieldCount() + " campos")
                    .orElse("Diccionario vacío");
        }
        if (DiagramTypeId.ADMIN_MODULE_MAP.equals(typeId)) {
            return project.moduleMap()
                    .map(document -> document.moduleCount() + " módulos / " + document.dependencyCount() + " dependencias")
                    .orElse("Mapa sin módulos");
        }
        if (DiagramTypeId.UML_CLASS.equals(typeId)) {
            return project.umlClassDiagram()
                    .map(document -> document.moduleCount() + " módulos / "
                            + document.classCount() + " clases / "
                            + document.relationCount() + " relaciones")
                    .orElse("UML Clases sin clases");
        }
        if (DiagramTypeId.ROLES_PERMISSIONS_MAP.equals(typeId)) {
            return project.rolesPermissions()
                    .map(document -> document.roles().size() + " roles / "
                            + document.permissions().size() + " permisos / "
                            + document.assignments().size() + " asignaciones")
                    .orElse("Mapa sin roles");
        }
        if (DiagramTypeId.SCREEN_FLOW.equals(typeId)) {
            return project.screenFlow()
                    .map(document -> document.screens().size() + " pantallas / "
                            + document.transitions().size() + " transiciones")
                    .orElse("Flujo sin pantallas");
        }
        if (DiagramTypeId.ADMIN_WIREFRAMES.equals(typeId)) {
            return project.wireframe()
                    .map(document -> document.screens().size() + " pantallas / "
                            + document.components().size() + " componentes")
                    .orElse("Wireframe sin pantallas");
        }
        if (isBehaviorDiagram(typeId)) {
            return project.behaviorDiagram()
                    .map(document -> document.nodes().size() + " elementos / "
                            + document.edges().size() + " relaciones")
                    .orElse("Diagrama sin elementos");
        }
        if (isArchitectureDiagram(typeId)) {
            return project.architectureDiagram()
                    .map(document -> document.nodes().size() + " nodos / "
                            + document.edges().size() + " relaciones")
                    .orElse("Diagrama sin nodos");
        }
        return conceptualSummary(project.model().entityCount(), project.model().relationshipCount());
    }

    private String conceptualSummary(int entityCount, int relationshipCount) {
        int total = entityCount + relationshipCount;
        if (total == 0) {
            return "Proyecto vacío";
        }
        return entityCount + " entidades / " + relationshipCount + " relaciones";
    }

    private boolean isBehaviorDiagram(DiagramTypeId typeId) {
        return DiagramTypeId.BPMN_BASIC.equals(typeId)
                || DiagramTypeId.OPERATIONAL_FLOW.equals(typeId)
                || DiagramTypeId.UML_USE_CASE.equals(typeId)
                || DiagramTypeId.UML_ACTIVITY.equals(typeId)
                || DiagramTypeId.UML_SEQUENCE.equals(typeId)
                || DiagramTypeId.UML_STATE.equals(typeId);
    }

    private boolean isArchitectureDiagram(DiagramTypeId typeId) {
        return DiagramTypeId.C4_CONTEXT.equals(typeId)
                || DiagramTypeId.C4_CONTAINERS.equals(typeId)
                || DiagramTypeId.TECHNICAL_DEPLOYMENT.equals(typeId);
    }

    private String displayNotation(String notation) {
        if (notation == null || notation.isBlank()) {
            return "Modelo conceptual";
        }
        return notation.toLowerCase(java.util.Locale.ROOT).contains("crow") ? "Pata de gallo" : notation.trim();
    }
}
