package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import com.marcosmoreira.domainmodelstudio.application.export.MarkdownDiagramExporter;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.util.Objects;

/** Exportador Markdown para el modelo conceptual compatible con la gramática importable actual. */
public final class ConceptualModelMarkdownExporter implements MarkdownDiagramExporter {

    @Override
    public String export(DiagramProject project) {
        Objects.requireNonNull(project, "project");
        if (!DiagramTypeId.CONCEPTUAL_MODEL.equals(project.metadata().diagramTypeId())) {
            throw new IllegalArgumentException("La exportación Markdown actual está disponible para modelos conceptuales.");
        }
        return MarkdownProjectWriter.write(project);
    }
}
