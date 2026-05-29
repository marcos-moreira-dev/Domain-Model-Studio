package com.marcosmoreira.domainmodelstudio.application.importmodel;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.nio.file.Path;
import java.util.Objects;

/** Resume una importación Markdown con vocabulario coherente según el tipo de diagrama. */
final class ImportedProjectSummaryFormatter {

    String summarize(Path sourceFile, DiagramProject project) {
        Objects.requireNonNull(project, "project");
        String source = sourceFile == null ? "contenido Markdown" : sourceFile.getFileName().toString();
        return source + ": " + countersFor(project);
    }

    private String countersFor(DiagramProject project) {
        DiagramTypeId typeId = project.metadata().diagramTypeId();
        if (DiagramTypeId.DATA_DICTIONARY.equals(typeId)) {
            return project.dataDictionary()
                    .map(document -> document.entityCount() + " entidades / " + document.fieldCount() + " campos")
                    .orElse("diccionario sin entidades");
        }
        if (DiagramTypeId.ADMIN_MODULE_MAP.equals(typeId)) {
            return project.moduleMap()
                    .map(document -> document.moduleCount() + " módulos / " + document.dependencyCount() + " dependencias")
                    .orElse("mapa sin módulos");
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
                    .orElse("mapa sin roles");
        }
        if (DiagramTypeId.SCREEN_FLOW.equals(typeId)) {
            return project.screenFlow()
                    .map(document -> document.screens().size() + " pantallas / "
                            + document.transitions().size() + " transiciones")
                    .orElse("flujo sin pantallas");
        }
        if (DiagramTypeId.ADMIN_WIREFRAMES.equals(typeId)) {
            return project.wireframe()
                    .map(document -> document.screens().size() + " pantallas / "
                            + document.components().size() + " componentes")
                    .orElse("wireframe sin pantallas");
        }
        if (isBehaviorDiagram(typeId)) {
            return project.behaviorDiagram()
                    .map(document -> document.nodes().size() + " elementos / "
                            + document.edges().size() + " relaciones")
                    .orElse("diagrama de comportamiento sin elementos");
        }
        if (isArchitectureDiagram(typeId)) {
            return project.architectureDiagram()
                    .map(document -> document.nodes().size() + " nodos / "
                            + document.edges().size() + " relaciones")
                    .orElse("diagrama de arquitectura sin nodos");
        }
        return project.model().entityCount() + " entidades / " + project.model().relationshipCount() + " relaciones";
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
}
