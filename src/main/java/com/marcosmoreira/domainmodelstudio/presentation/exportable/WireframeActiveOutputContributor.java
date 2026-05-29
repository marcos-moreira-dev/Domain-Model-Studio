package com.marcosmoreira.domainmodelstudio.presentation.exportable;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeDocument;
import com.marcosmoreira.domainmodelstudio.presentation.wireframe.WireframeViewModel;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/** Salida exportable de wireframes administrativos. */
final class WireframeActiveOutputContributor implements ActiveOutputContributor {

    private final WireframeViewModel viewModel;

    WireframeActiveOutputContributor(WireframeViewModel viewModel) {
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
    }

    @Override
    public boolean supports(DiagramTypeId diagramTypeId) {
        return DiagramTypeId.ADMIN_WIREFRAMES.equals(diagramTypeId);
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
        WireframeDocument document = viewModel.currentDocument();
        if (!ActiveOutputContributorSupport.sameActiveProject(project, activeProject) || document == null) {
            return Optional.empty();
        }
        Set<ExportFormat> formats = exportFormatPolicy.formatsForWireframe(project, true);
        if (formats.isEmpty()) {
            return Optional.empty();
        }
        ExportableOutputDescriptor descriptor = ExportableOutputDescriptor.visualDiagram(
                DiagramTypeId.ADMIN_WIREFRAMES,
                project.metadata().title(),
                project.metadata().id() + "_wireframes",
                formats);
        return Optional.of(ExportableOutput.visual(
                descriptor,
                project,
                viewModel::exportVisualAsPng));
    }
}
