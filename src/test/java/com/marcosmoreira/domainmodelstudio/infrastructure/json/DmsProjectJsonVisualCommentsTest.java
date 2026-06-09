package com.marcosmoreira.domainmodelstudio.infrastructure.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.visual.VisualElementLayoutIds;
import com.marcosmoreira.domainmodelstudio.application.visualcomment.VisualCommentProjectService;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphDocument;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphKind;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class DmsProjectJsonVisualCommentsTest {

    private final DmsProjectJsonWriter writer = new DmsProjectJsonWriter();
    private final DmsProjectJsonReader reader = new DmsProjectJsonReader();
    private final VisualCommentProjectService comments = new VisualCommentProjectService();

    @Test
    void shouldRoundTripVisualCommentsAndLayouts() {
        VisualCommentProjectService.AddResult added = comments.addComment(baseFreeGraphProject(), 260.0, 140.0);
        DiagramProject project = comments.updateTitle(added.project(), added.layoutId(), "Riesgo cliente");
        project = comments.updateDescription(project, added.layoutId(), "Revisar supuestos con el cliente.");

        String json = writer.write(project);
        DiagramProject reopened = reader.read(json);

        assertTrue(json.contains("\"visualComments\""));
        assertEquals(1, reopened.visualComments().comments().size());
        assertEquals("Riesgo cliente", reopened.visualComments().comments().get(0).title());
        assertEquals("Revisar supuestos con el cliente.", reopened.visualComments().comments().get(0).description());
        DiagramElementId layoutId = VisualElementLayoutIds.visualComment(reopened.visualComments().comments().get(0).id());
        assertTrue(reopened.layouts().activeLayout().nodeFor(layoutId).isPresent());
    }

    @Test
    void shouldLoadLegacyProjectWithoutVisualCommentsSection() {
        String current = writer.write(DiagramProject.blank("legacy", "Legacy"));
        String legacy = current.replaceFirst(",\\R  \"visualComments\": \\{\\R    \"comments\": \\[\\R    \\]\\R  \\}", "");

        DiagramProject reopened = reader.read(legacy);

        assertTrue(reopened.visualComments().isEmpty());
    }

    private static DiagramProject baseFreeGraphProject() {
        FreeGraphDocument document = new FreeGraphDocument(
                "Notas libres",
                "0.1.0",
                LocalDate.of(2026, 1, 1),
                FreeGraphKind.MIXED,
                List.of(),
                List.of(),
                "");
        return DiagramProject.blank("free-notes", "Notas libres", DiagramTypeId.FREE_GRAPH)
                .withFreeGraph(document);
    }
}
