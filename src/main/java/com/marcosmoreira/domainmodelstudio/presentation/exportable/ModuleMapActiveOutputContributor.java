package com.marcosmoreira.domainmodelstudio.presentation.exportable;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleMapDocument;
import com.marcosmoreira.domainmodelstudio.presentation.modulemap.ModuleMapViewModel;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/** Salida exportable del mapa de módulos. */
final class ModuleMapActiveOutputContributor implements ActiveOutputContributor {

    private final ModuleMapViewModel viewModel;

    ModuleMapActiveOutputContributor(ModuleMapViewModel viewModel) {
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
    }

    @Override
    public boolean supports(DiagramTypeId diagramTypeId) {
        return DiagramTypeId.ADMIN_MODULE_MAP.equals(diagramTypeId);
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
        ModuleMapDocument document = viewModel.currentDocument();
        if (!ActiveOutputContributorSupport.sameActiveProject(project, activeProject) || document == null) {
            return Optional.empty();
        }
        Set<ExportFormat> formats = exportFormatPolicy.formatsForModuleMap(project, true);
        if (formats.isEmpty()) {
            return Optional.empty();
        }
        ExportableOutputDescriptor descriptor = ExportableOutputDescriptor.visualDiagram(
                DiagramTypeId.ADMIN_MODULE_MAP,
                project.metadata().title(),
                project.metadata().id() + "_mapa_modulos",
                formats);
        return Optional.of(ExportableOutput.visual(
                descriptor,
                project,
                viewModel::exportVisualAsPng));
    }
}
