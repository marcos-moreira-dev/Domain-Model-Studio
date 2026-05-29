package com.marcosmoreira.domainmodelstudio.presentation.exportable;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphDocument;
import com.marcosmoreira.domainmodelstudio.presentation.freegraph.FreeGraphViewModel;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/** Salida exportable del grafo libre. */
final class FreeGraphActiveOutputContributor implements ActiveOutputContributor {

    private final FreeGraphViewModel viewModel;

    FreeGraphActiveOutputContributor(FreeGraphViewModel viewModel) {
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
    }

    @Override
    public boolean supports(DiagramTypeId diagramTypeId) {
        return DiagramTypeId.FREE_GRAPH.equals(diagramTypeId);
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
        FreeGraphDocument document = viewModel.currentDocument();
        if (!ActiveOutputContributorSupport.sameActiveProject(project, activeProject) || document == null) {
            return Optional.empty();
        }
        Set<ExportFormat> formats = exportFormatPolicy.formatsForFreeGraph(project, true);
        if (formats.isEmpty()) {
            return Optional.empty();
        }
        ExportableOutputDescriptor descriptor = ExportableOutputDescriptor.visualDiagram(
                DiagramTypeId.FREE_GRAPH,
                project.metadata().title(),
                project.metadata().id() + "_grafo_libre",
                formats);
        return Optional.of(ExportableOutput.visual(
                descriptor,
                project,
                viewModel::exportVisualAsPng));
    }
}
