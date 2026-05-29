package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import com.marcosmoreira.domainmodelstudio.application.export.MarkdownDiagramExporter;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.util.Map;
import java.util.Objects;

/** Exportador Markdown que delega según el tipo real de proyecto. */
public final class DelegatingMarkdownDiagramExporter implements MarkdownDiagramExporter {

    private final Map<DiagramTypeId, MarkdownDiagramExporter> exportersByType;

    public DelegatingMarkdownDiagramExporter(Map<DiagramTypeId, MarkdownDiagramExporter> exportersByType) {
        this.exportersByType = Map.copyOf(Objects.requireNonNull(exportersByType, "exportersByType"));
    }

    @Override
    public String export(DiagramProject project) {
        Objects.requireNonNull(project, "project");
        MarkdownDiagramExporter exporter = exportersByType.get(project.metadata().diagramTypeId());
        if (exporter == null) {
            throw new IllegalArgumentException("Este tipo de proyecto todavía no tiene exportación Markdown activa.");
        }
        return exporter.export(project);
    }
}
