package com.marcosmoreira.domainmodelstudio.application.batchexport;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

/** Proyecto abierto exportable dentro de una exportación conjunta. */
public record OpenProjectExportItem(
        String tabId,
        String displayName,
        DiagramTypeId diagramTypeId,
        DiagramProject project,
        Optional<Path> sourceMarkdown,
        boolean exportMarkdown,
        boolean exportSvg,
        boolean exportPngSnapshot,
        boolean exportPdf,
        DiagramProject outputProject,
        String suggestedFileStem
) {

    public OpenProjectExportItem(
            String tabId,
            String displayName,
            DiagramTypeId diagramTypeId,
            DiagramProject project,
            Optional<Path> sourceMarkdown,
            boolean exportMarkdown,
            boolean exportSvg,
            boolean exportPngSnapshot,
            boolean exportPdf
    ) {
        this(tabId, displayName, diagramTypeId, project, sourceMarkdown,
                exportMarkdown, exportSvg, exportPngSnapshot, exportPdf, project, displayName);
    }

    public OpenProjectExportItem {
        tabId = requireText(tabId, "tabId");
        displayName = requireText(displayName, "displayName");
        Objects.requireNonNull(diagramTypeId, "diagramTypeId");
        Objects.requireNonNull(project, "project");
        sourceMarkdown = sourceMarkdown == null ? Optional.empty() : sourceMarkdown;
        outputProject = outputProject == null ? project : outputProject;
        suggestedFileStem = requireText(suggestedFileStem == null ? displayName : suggestedFileStem, "suggestedFileStem");
    }

    public static OpenProjectExportItem conceptual(
            String tabId,
            String displayName,
            DiagramProject project,
            Optional<Path> sourceMarkdown
    ) {
        return new OpenProjectExportItem(
                tabId,
                displayName,
                DiagramTypeId.CONCEPTUAL_MODEL,
                project,
                sourceMarkdown,
                true,
                true,
                true,
                false);
    }

    public static OpenProjectExportItem visualDiagram(
            String tabId,
            String displayName,
            DiagramProject project,
            Optional<Path> sourceMarkdown
    ) {
        return new OpenProjectExportItem(
                tabId,
                displayName,
                project.metadata().diagramTypeId(),
                project,
                sourceMarkdown,
                true,
                true,
                true,
                false);
    }

    public static OpenProjectExportItem document(
            String tabId,
            String displayName,
            DiagramProject project,
            Optional<Path> sourceMarkdown
    ) {
        return new OpenProjectExportItem(
                tabId,
                displayName,
                project.metadata().diagramTypeId(),
                project,
                sourceMarkdown,
                true,
                false,
                false,
                true);
    }

    public static OpenProjectExportItem placeholder(
            String tabId,
            String displayName,
            DiagramProject project,
            Optional<Path> sourceMarkdown
    ) {
        return new OpenProjectExportItem(
                tabId,
                displayName,
                project.metadata().diagramTypeId(),
                project,
                sourceMarkdown,
                false,
                false,
                false,
                false);
    }

    public boolean usesScopedOutputProject() {
        return project != outputProject;
    }

    private static String requireText(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName);
        String normalized = value.strip();
        if (normalized.isBlank()) {
            throw new IllegalArgumentException(fieldName + " no puede estar vacío.");
        }
        return normalized;
    }
}
