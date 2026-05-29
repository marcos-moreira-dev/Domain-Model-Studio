package com.marcosmoreira.domainmodelstudio.presentation.exportable;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.presentation.logicalbusinessgraph.LogicalBusinessGraphViewModel;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/** Salida visual exportable del Grafo lógico del negocio. */
final class LogicalBusinessGraphActiveOutputContributor implements ActiveOutputContributor {

    private final LogicalBusinessGraphViewModel viewModel;

    LogicalBusinessGraphActiveOutputContributor(LogicalBusinessGraphViewModel viewModel) {
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
    }

    @Override
    public boolean supports(DiagramTypeId diagramTypeId) {
        return DiagramTypeId.LOGICAL_BUSINESS_GRAPH.equals(diagramTypeId);
    }

    @Override
    public Optional<ExportableOutput> resolve(DiagramProject activeProject, ProjectExportFormatPolicy exportFormatPolicy) {
        if (!viewModel.active()) {
            return Optional.empty();
        }
        DiagramProject project = viewModel.currentProject();
        if (!ActiveOutputContributorSupport.sameActiveProject(project, activeProject)
                || project.logicalBusinessGraphDocument().isEmpty()) {
            return Optional.empty();
        }
        Set<ExportFormat> formats = exportFormatPolicy.formatsForLogicalBusinessGraph(project, true);
        if (formats.isEmpty()) {
            return Optional.empty();
        }
        ExportableOutputDescriptor descriptor = ExportableOutputDescriptor.visualDiagram(
                DiagramTypeId.LOGICAL_BUSINESS_GRAPH,
                project.metadata().title(),
                project.metadata().id() + "_grafo_logico_negocio",
                formats);
        return Optional.of(ExportableOutput.visual(descriptor, project, viewModel::exportVisualAsPng));
    }
}
