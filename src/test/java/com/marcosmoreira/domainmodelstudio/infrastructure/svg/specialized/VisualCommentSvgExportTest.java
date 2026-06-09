package com.marcosmoreira.domainmodelstudio.infrastructure.svg.specialized;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.visualcomment.VisualCommentProjectService;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphDocument;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphKind;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class VisualCommentSvgExportTest {

    @Test
    void shouldIncludeVisualCommentsInSpecializedSvg() {
        FreeGraphDocument document = new FreeGraphDocument(
                "Grafo con notas",
                "0.1.0",
                LocalDate.of(2026, 1, 1),
                FreeGraphKind.MIXED,
                List.of(),
                List.of(),
                "");
        DiagramProject project = DiagramProject.blank("graph-notes", "Grafo con notas", DiagramTypeId.FREE_GRAPH)
                .withFreeGraph(document);
        VisualCommentProjectService service = new VisualCommentProjectService();
        VisualCommentProjectService.AddResult added = service.addComment(project, 240.0, 120.0);
        project = service.updateTitle(added.project(), added.layoutId(), "Pendiente cliente");
        project = service.updateDescription(project, added.layoutId(), "Confirmar regla manual.");

        String svg = new SpecializedVisualSvgDiagramExporter().export(project);

        assertTrue(svg.contains("node-visual-comment"));
        assertTrue(svg.contains("Pendiente cliente"));
        assertTrue(svg.contains("Confirmar regla manual."));
    }
}
