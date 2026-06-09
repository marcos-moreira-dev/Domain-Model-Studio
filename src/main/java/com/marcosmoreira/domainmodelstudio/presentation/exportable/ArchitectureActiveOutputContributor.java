package com.marcosmoreira.domainmodelstudio.presentation.exportable;

import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.presentation.architecture.ArchitectureDiagramViewModel;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/** Salida exportable de diagramas de arquitectura. */
final class ArchitectureActiveOutputContributor implements ActiveOutputContributor {

    private final ArchitectureDiagramViewModel viewModel;

    ArchitectureActiveOutputContributor(ArchitectureDiagramViewModel viewModel) {
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
    }

    @Override
    public boolean supports(DiagramTypeId diagramTypeId) {
        return DiagramTypeId.C4_CONTEXT.equals(diagramTypeId)
                || DiagramTypeId.C4_CONTAINERS.equals(diagramTypeId)
                || DiagramTypeId.TECHNICAL_DEPLOYMENT.equals(diagramTypeId);
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
        ArchitectureDiagramDocument document = viewModel.currentDocument();
        if (!ActiveOutputContributorSupport.sameActiveProject(project, activeProject) || document == null) {
            return Optional.empty();
        }
        Set<ExportFormat> formats = exportFormatPolicy.formatsForArchitecture(project, true);
        if (formats.isEmpty()) {
            return Optional.empty();
        }
        ExportableOutputDescriptor descriptor = ExportableOutputDescriptor.visualDiagram(
                project.metadata().diagramTypeId(),
                project.metadata().title(),
                project.metadata().id() + "_arquitectura",
                formats);
        return Optional.of(ExportableOutput.visual(
                descriptor,
                project,
                viewModel::exportVisualAsPng));
    }
}
