package com.marcosmoreira.domainmodelstudio.presentation.exportable;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.presentation.canvas.DiagramCanvasViewModel;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/** Salida exportable del modelo conceptual clásico. */
final class ConceptualActiveOutputContributor implements ActiveOutputContributor {

    private final DiagramCanvasViewModel canvasViewModel;

    ConceptualActiveOutputContributor(DiagramCanvasViewModel canvasViewModel) {
        this.canvasViewModel = Objects.requireNonNull(canvasViewModel, "canvasViewModel");
    }

    @Override
    public boolean supports(DiagramTypeId diagramTypeId) {
        return DiagramTypeId.CONCEPTUAL_MODEL.equals(diagramTypeId);
    }

    @Override
    public Optional<ExportableOutput> resolve(
            DiagramProject activeProject,
            ProjectExportFormatPolicy exportFormatPolicy
    ) {
        DiagramProject project = canvasViewModel.currentProject();
        if (!ActiveOutputContributorSupport.sameActiveProject(project, activeProject)) {
            return Optional.empty();
        }
        Set<ExportFormat> formats = exportFormatPolicy.formatsForConceptualModel(project, true);
        if (formats.isEmpty()) {
            return Optional.empty();
        }
        ExportableOutputDescriptor descriptor = ExportableOutputDescriptor.visualDiagram(
                project.metadata().diagramTypeId(),
                project.metadata().title(),
                project.metadata().id(),
                formats);
        return Optional.of(ExportableOutput.visual(
                descriptor,
                project,
                canvasViewModel::exportVisibleCanvasAsPng));
    }
}
