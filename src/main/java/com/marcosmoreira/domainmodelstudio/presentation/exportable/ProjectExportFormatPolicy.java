package com.marcosmoreira.domainmodelstudio.presentation.exportable;

import com.marcosmoreira.domainmodelstudio.application.catalog.DefaultDiagramCapabilityCatalog;
import com.marcosmoreira.domainmodelstudio.application.catalog.DiagramCapabilityCatalog;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.util.Objects;
import java.util.Set;

/**
 * Política de formatos reales para la salida activa.
 *
 * <p>La fuente primaria de promesa es el catálogo oficial de capacidades. Esta
 * clase solo filtra esa promesa contra el contenido cargado en la pestaña activa:
 * un tipo puede declarar Markdown, SVG o PDF, pero la UI no debe ofrecerlo si el
 * documento especializado todavía no existe dentro del proyecto.</p>
 */
public final class ProjectExportFormatPolicy {

    private final DiagramCapabilityCatalog capabilityCatalog;
    private final ExportFormatCapabilityMapper formatMapper;

    public ProjectExportFormatPolicy() {
        this(new DefaultDiagramCapabilityCatalog());
    }

    public ProjectExportFormatPolicy(DiagramCapabilityCatalog capabilityCatalog) {
        this.capabilityCatalog = Objects.requireNonNull(capabilityCatalog, "capabilityCatalog");
        this.formatMapper = new ExportFormatCapabilityMapper();
    }

    public Set<ExportFormat> formatsForConceptualModel(DiagramProject project, boolean pngAvailable) {
        if (!hasType(project, DiagramTypeId.CONCEPTUAL_MODEL)) {
            return Set.of();
        }
        return declaredFormats(project.metadata().diagramTypeId(), pngAvailable);
    }

    public Set<ExportFormat> formatsForDataDictionary(DiagramProject project) {
        if (!hasType(project, DiagramTypeId.DATA_DICTIONARY) || project.dataDictionary().isEmpty()) {
            return Set.of();
        }
        return declaredFormats(project.metadata().diagramTypeId(), false);
    }

    public Set<ExportFormat> formatsForModuleMap(DiagramProject project, boolean pngAvailable) {
        if (!hasType(project, DiagramTypeId.ADMIN_MODULE_MAP) || project.moduleMap().isEmpty()) {
            return Set.of();
        }
        return declaredFormats(project.metadata().diagramTypeId(), pngAvailable);
    }

    public Set<ExportFormat> formatsForUmlClass(DiagramProject project, boolean pngAvailable) {
        if (!hasType(project, DiagramTypeId.UML_CLASS) || project.umlClassDiagram().isEmpty()) {
            return Set.of();
        }
        return declaredFormats(project.metadata().diagramTypeId(), pngAvailable);
    }

    public Set<ExportFormat> formatsForRolesPermissions(DiagramProject project, boolean pngAvailable) {
        if (!hasType(project, DiagramTypeId.ROLES_PERMISSIONS_MAP) || project.rolesPermissions().isEmpty()) {
            return Set.of();
        }
        return declaredFormats(project.metadata().diagramTypeId(), pngAvailable);
    }

    public Set<ExportFormat> formatsForScreenFlow(DiagramProject project, boolean pngAvailable) {
        if (!hasType(project, DiagramTypeId.SCREEN_FLOW) || project.screenFlow().isEmpty()) {
            return Set.of();
        }
        return declaredFormats(project.metadata().diagramTypeId(), pngAvailable);
    }

    public Set<ExportFormat> formatsForWireframe(DiagramProject project, boolean pngAvailable) {
        if (!hasType(project, DiagramTypeId.ADMIN_WIREFRAMES) || project.wireframe().isEmpty()) {
            return Set.of();
        }
        return declaredFormats(project.metadata().diagramTypeId(), pngAvailable);
    }

    public Set<ExportFormat> formatsForFreeGraph(DiagramProject project, boolean pngAvailable) {
        if (!hasType(project, DiagramTypeId.FREE_GRAPH) || project.freeGraph().isEmpty()) {
            return Set.of();
        }
        return declaredFormats(project.metadata().diagramTypeId(), pngAvailable);
    }

    public Set<ExportFormat> formatsForLogicalBusiness(DiagramProject project) {
        if (!hasType(project, DiagramTypeId.LOGICAL_BUSINESS_INTAKE) || project.logicalBusinessDocument().isEmpty()) {
            return Set.of();
        }
        return declaredFormats(project.metadata().diagramTypeId(), false);
    }

    public Set<ExportFormat> formatsForLogicalBusinessGraph(DiagramProject project) {
        return formatsForLogicalBusinessGraph(project, false);
    }

    public Set<ExportFormat> formatsForLogicalBusinessGraph(DiagramProject project, boolean pngAvailable) {
        if (!hasType(project, DiagramTypeId.LOGICAL_BUSINESS_GRAPH) || project.logicalBusinessGraphDocument().isEmpty()) {
            return Set.of();
        }
        return declaredFormats(project.metadata().diagramTypeId(), pngAvailable);
    }

    public Set<ExportFormat> formatsForBehavior(DiagramProject project, boolean pngAvailable) {
        if (project == null || project.behaviorDiagram().isEmpty()) {
            return Set.of();
        }
        DiagramTypeId typeId = project.metadata().diagramTypeId();
        if (!isBehaviorType(typeId)) {
            return Set.of();
        }
        return declaredFormats(typeId, pngAvailable);
    }

    public Set<ExportFormat> formatsForArchitecture(DiagramProject project, boolean pngAvailable) {
        if (project == null || project.architectureDiagram().isEmpty()) {
            return Set.of();
        }
        DiagramTypeId typeId = project.metadata().diagramTypeId();
        if (!isArchitectureType(typeId)) {
            return Set.of();
        }
        return declaredFormats(typeId, pngAvailable);
    }

    public boolean hasAnyExportableFormat(DiagramProject project) {
        return !formatsForProject(project, false).isEmpty();
    }

    public Set<ExportFormat> formatsForProject(DiagramProject project, boolean pngAvailable) {
        if (project == null) {
            return Set.of();
        }
        DiagramTypeId typeId = project.metadata().diagramTypeId();
        if (DiagramTypeId.CONCEPTUAL_MODEL.equals(typeId)) {
            return formatsForConceptualModel(project, pngAvailable);
        }
        if (DiagramTypeId.DATA_DICTIONARY.equals(typeId)) {
            return formatsForDataDictionary(project);
        }
        if (DiagramTypeId.ADMIN_MODULE_MAP.equals(typeId)) {
            return formatsForModuleMap(project, pngAvailable);
        }
        if (DiagramTypeId.UML_CLASS.equals(typeId)) {
            return formatsForUmlClass(project, pngAvailable);
        }
        if (DiagramTypeId.ROLES_PERMISSIONS_MAP.equals(typeId)) {
            return formatsForRolesPermissions(project, pngAvailable);
        }
        if (DiagramTypeId.SCREEN_FLOW.equals(typeId)) {
            return formatsForScreenFlow(project, pngAvailable);
        }
        if (DiagramTypeId.ADMIN_WIREFRAMES.equals(typeId)) {
            return formatsForWireframe(project, pngAvailable);
        }
        if (DiagramTypeId.FREE_GRAPH.equals(typeId)) {
            return formatsForFreeGraph(project, pngAvailable);
        }
        if (DiagramTypeId.LOGICAL_BUSINESS_INTAKE.equals(typeId)) {
            return formatsForLogicalBusiness(project);
        }
        if (DiagramTypeId.LOGICAL_BUSINESS_GRAPH.equals(typeId)) {
            return formatsForLogicalBusinessGraph(project, pngAvailable);
        }
        Set<ExportFormat> behaviorFormats = formatsForBehavior(project, pngAvailable);
        if (!behaviorFormats.isEmpty()) {
            return behaviorFormats;
        }
        return formatsForArchitecture(project, pngAvailable);
    }

    private Set<ExportFormat> declaredFormats(DiagramTypeId typeId, boolean pngAvailable) {
        return formatMapper.formatsFrom(capabilityCatalog.capabilitiesOf(typeId), pngAvailable);
    }

    private boolean hasType(DiagramProject project, DiagramTypeId typeId) {
        return project != null && Objects.equals(project.metadata().diagramTypeId(), typeId);
    }

    private boolean isBehaviorType(DiagramTypeId typeId) {
        return DiagramTypeId.BPMN_BASIC.equals(typeId)
                || DiagramTypeId.OPERATIONAL_FLOW.equals(typeId)
                || DiagramTypeId.UML_USE_CASE.equals(typeId)
                || DiagramTypeId.UML_ACTIVITY.equals(typeId)
                || DiagramTypeId.UML_SEQUENCE.equals(typeId)
                || DiagramTypeId.UML_STATE.equals(typeId);
    }

    private boolean isArchitectureType(DiagramTypeId typeId) {
        return DiagramTypeId.C4_CONTEXT.equals(typeId)
                || DiagramTypeId.C4_CONTAINERS.equals(typeId)
                || DiagramTypeId.TECHNICAL_DEPLOYMENT.equals(typeId);
    }
}
