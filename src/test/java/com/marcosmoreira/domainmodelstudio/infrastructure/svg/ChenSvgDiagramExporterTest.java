package com.marcosmoreira.domainmodelstudio.infrastructure.svg;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.importmodel.ImportMarkdownModelUseCase;
import com.marcosmoreira.domainmodelstudio.application.layout.GenerateInitialChenLayoutUseCase;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.validation.DiagramProjectValidator;
import com.marcosmoreira.domainmodelstudio.infrastructure.markdown.MarkdownDiagramParser;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class ChenSvgDiagramExporterTest {

    @Test
    void exportsRealVectorElementsForChenDiagram() throws Exception {
        ImportMarkdownModelUseCase importUseCase = new ImportMarkdownModelUseCase(
                new MarkdownDiagramParser(),
                new DiagramProjectValidator()
        );
        DiagramProject project = new GenerateInitialChenLayoutUseCase()
                .generate(importUseCase.importFile(Path.of("examples/markdown/colegio_chen.md")).project());

        String svg = new ChenSvgDiagramExporter().export(project);

        assertTrue(svg.contains("<svg"));
        assertTrue(svg.contains("<rect"));
        assertTrue(svg.contains("<ellipse"));
        assertTrue(svg.contains("<polygon"));
        assertTrue(svg.contains("<line") || svg.contains("<polyline"));
        assertTrue(svg.contains("<text"));
    }
}
