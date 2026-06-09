package com.marcosmoreira.domainmodelstudio.presentation.exportable;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryDocument;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.presentation.datadictionary.DataDictionaryViewModel;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/** Salida exportable documental del diccionario de datos. */
final class DataDictionaryActiveOutputContributor implements ActiveOutputContributor {

    private final DataDictionaryViewModel viewModel;

    DataDictionaryActiveOutputContributor(DataDictionaryViewModel viewModel) {
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
    }

    @Override
    public boolean supports(DiagramTypeId diagramTypeId) {
        return DiagramTypeId.DATA_DICTIONARY.equals(diagramTypeId);
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
        DataDictionaryDocument document = viewModel.currentDocument();
        if (!ActiveOutputContributorSupport.sameActiveProject(project, activeProject) || document == null) {
            return Optional.empty();
        }
        Set<ExportFormat> formats = exportFormatPolicy.formatsForDataDictionary(project);
        if (formats.isEmpty()) {
            return Optional.empty();
        }
        ExportableOutputDescriptor descriptor = ExportableOutputDescriptor.document(
                DiagramTypeId.DATA_DICTIONARY,
                project.metadata().title(),
                project.metadata().id() + "_diccionario_datos",
                formats);
        return Optional.of(ExportableOutput.document(descriptor, project, document));
    }
}
