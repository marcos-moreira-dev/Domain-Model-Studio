package com.marcosmoreira.domainmodelstudio.presentation.exportable;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness.LogicalBusinessViewModel;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/** Salida documental exportable del levantamiento lógico. */
final class LogicalBusinessActiveOutputContributor implements ActiveOutputContributor {

    private final LogicalBusinessViewModel viewModel;

    LogicalBusinessActiveOutputContributor(LogicalBusinessViewModel viewModel) {
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
    }

    @Override
    public boolean supports(DiagramTypeId diagramTypeId) {
        return DiagramTypeId.LOGICAL_BUSINESS_INTAKE.equals(diagramTypeId);
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
        if (!ActiveOutputContributorSupport.sameActiveProject(project, activeProject)
                || project.logicalBusinessDocument().isEmpty()) {
            return Optional.empty();
        }
        Set<ExportFormat> formats = exportFormatPolicy.formatsForLogicalBusiness(project);
        if (formats.isEmpty()) {
            return Optional.empty();
        }
        ExportableOutputDescriptor descriptor = ExportableOutputDescriptor.document(
                DiagramTypeId.LOGICAL_BUSINESS_INTAKE,
                project.metadata().title(),
                project.metadata().id() + "_levantamiento_logico",
                formats);
        return Optional.of(ExportableOutput.projectDocument(descriptor, project));
    }
}
