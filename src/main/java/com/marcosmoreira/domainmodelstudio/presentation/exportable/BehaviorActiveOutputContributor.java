package com.marcosmoreira.domainmodelstudio.presentation.exportable;

import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.presentation.behavior.BehaviorDiagramViewModel;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/** Salida exportable de diagramas de comportamiento y procesos. */
final class BehaviorActiveOutputContributor implements ActiveOutputContributor {

    private final BehaviorDiagramViewModel viewModel;

    BehaviorActiveOutputContributor(BehaviorDiagramViewModel viewModel) {
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
    }

    @Override
    public boolean supports(DiagramTypeId diagramTypeId) {
        return DiagramTypeId.BPMN_BASIC.equals(diagramTypeId)
                || DiagramTypeId.OPERATIONAL_FLOW.equals(diagramTypeId)
                || DiagramTypeId.UML_USE_CASE.equals(diagramTypeId)
                || DiagramTypeId.UML_ACTIVITY.equals(diagramTypeId)
                || DiagramTypeId.UML_SEQUENCE.equals(diagramTypeId)
                || DiagramTypeId.UML_STATE.equals(diagramTypeId);
    }

    @Override
    public Optional<ExportableOutput> resolve(
            DiagramProject activeProject,
            ProjectExportFormatPolicy exportFormatPolicy
    ) {
        if (!viewModel.active()) {
            return Optional.empty();
        }
        DiagramProject project = viewModel.currentProject();
        BehaviorDiagramDocument document = viewModel.currentDocument();
        if (!ActiveOutputContributorSupport.sameActiveProject(project, activeProject) || document == null) {
            return Optional.empty();
        }
        Set<ExportFormat> formats = exportFormatPolicy.formatsForBehavior(project, true);
        if (formats.isEmpty()) {
            return Optional.empty();
        }
        ExportableOutputDescriptor descriptor = ExportableOutputDescriptor.visualDiagram(
                project.metadata().diagramTypeId(),
                project.metadata().title(),
                project.metadata().id() + "_comportamiento",
                formats);
        return Optional.of(ExportableOutput.visual(
                descriptor,
                project,
                viewModel::exportVisualAsPng));
    }
}
