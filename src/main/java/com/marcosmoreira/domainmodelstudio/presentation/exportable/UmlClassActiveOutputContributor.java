package com.marcosmoreira.domainmodelstudio.presentation.exportable;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramDocument;
import com.marcosmoreira.domainmodelstudio.presentation.umlclass.UmlClassDiagramViewModel;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/** Salida exportable de UML Clases, respetando la vista visual activa. */
final class UmlClassActiveOutputContributor implements ActiveOutputContributor {

    private final UmlClassDiagramViewModel viewModel;

    UmlClassActiveOutputContributor(UmlClassDiagramViewModel viewModel) {
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
    }

    @Override
    public boolean supports(DiagramTypeId diagramTypeId) {
        return DiagramTypeId.UML_CLASS.equals(diagramTypeId);
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
        UmlClassDiagramDocument document = viewModel.currentDocument();
        if (!ActiveOutputContributorSupport.sameActiveProject(project, activeProject) || document == null) {
            return Optional.empty();
        }
        Set<ExportFormat> formats = exportFormatPolicy.formatsForUmlClass(project, true);
        if (formats.isEmpty()) {
            return Optional.empty();
        }
        ExportableOutputDescriptor descriptor = ExportableOutputDescriptor.visualDiagram(
                DiagramTypeId.UML_CLASS,
                project.metadata().title(),
                project.metadata().id() + "_uml_clases",
                formats);
        return Optional.of(ExportableOutput.visualScoped(
                descriptor,
                project,
                viewModel.currentVisualExportProject(),
                viewModel::exportVisualAsPng));
    }
}
