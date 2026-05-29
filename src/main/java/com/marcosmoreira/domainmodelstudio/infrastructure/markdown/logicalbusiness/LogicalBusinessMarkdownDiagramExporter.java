package com.marcosmoreira.domainmodelstudio.infrastructure.markdown.logicalbusiness;

import com.marcosmoreira.domainmodelstudio.application.export.MarkdownDiagramExporter;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocument;
import java.util.Objects;

/** Adapta el exporter puro del levantamiento lógico al exportador Markdown general. */
public final class LogicalBusinessMarkdownDiagramExporter implements MarkdownDiagramExporter {

    private final LogicalBusinessMarkdownExporter exporter;

    public LogicalBusinessMarkdownDiagramExporter() {
        this(new LogicalBusinessMarkdownExporter());
    }

    LogicalBusinessMarkdownDiagramExporter(LogicalBusinessMarkdownExporter exporter) {
        this.exporter = Objects.requireNonNull(exporter, "exporter");
    }

    @Override
    public String export(DiagramProject project) {
        Objects.requireNonNull(project, "project");
        LogicalBusinessDocument document = project.logicalBusinessDocument()
                .orElseThrow(() -> new IllegalArgumentException("El proyecto no contiene levantamiento lógico exportable."));
        return exporter.export(document);
    }
}
