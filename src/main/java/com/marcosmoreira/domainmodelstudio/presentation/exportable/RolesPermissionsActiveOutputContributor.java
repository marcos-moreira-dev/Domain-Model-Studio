package com.marcosmoreira.domainmodelstudio.presentation.exportable;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.RolesPermissionsDocument;
import com.marcosmoreira.domainmodelstudio.presentation.rolespermissions.RolesPermissionsViewModel;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/** Salida exportable tabular de roles y permisos. */
final class RolesPermissionsActiveOutputContributor implements ActiveOutputContributor {

    private final RolesPermissionsViewModel viewModel;

    RolesPermissionsActiveOutputContributor(RolesPermissionsViewModel viewModel) {
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
    }

    @Override
    public boolean supports(DiagramTypeId diagramTypeId) {
        return DiagramTypeId.ROLES_PERMISSIONS_MAP.equals(diagramTypeId);
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
        RolesPermissionsDocument document = viewModel.currentDocument();
        if (!ActiveOutputContributorSupport.sameActiveProject(project, activeProject) || document == null) {
            return Optional.empty();
        }
        Set<ExportFormat> formats = exportFormatPolicy.formatsForRolesPermissions(project, true);
        if (formats.isEmpty()) {
            return Optional.empty();
        }
        ExportableOutputDescriptor descriptor = ExportableOutputDescriptor.matrix(
                DiagramTypeId.ROLES_PERMISSIONS_MAP,
                project.metadata().title(),
                project.metadata().id() + "_roles_permisos",
                formats);
        return Optional.of(ExportableOutput.matrix(
                descriptor,
                project,
                viewModel::exportVisualAsPng));
    }
}
