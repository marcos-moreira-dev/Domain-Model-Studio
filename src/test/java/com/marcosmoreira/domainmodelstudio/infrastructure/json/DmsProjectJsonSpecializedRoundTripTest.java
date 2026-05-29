package com.marcosmoreira.domainmodelstudio.infrastructure.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramKind;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNode;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNodeKind;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Regresión para no degradar proyectos especializados a modelo conceptual al guardar/abrir .dms. */
class DmsProjectJsonSpecializedRoundTripTest {

    @Test
    void behaviorProjectKeepsDiagramTypeAndDocumentAfterRoundTrip() {
        BehaviorDiagramDocument document = new BehaviorDiagramDocument(
                "Casos de uso",
                "borrador",
                LocalDate.now(),
                BehaviorDiagramKind.UML_USE_CASE,
                List.of(new BehaviorNode("actor-secretaria", BehaviorNodeKind.ACTOR,
                        "Secretaría", "", "", "", 0)),
                List.of(),
                "");
        DiagramProject project = DiagramProject.blank("usecase", "Casos de uso", DiagramTypeId.UML_USE_CASE)
                .withBehaviorDiagram(document);

        String json = new DmsProjectJsonWriter().write(project);
        DiagramProject loaded = new DmsProjectJsonReader().read(json);

        assertEquals(DiagramTypeId.UML_USE_CASE, loaded.metadata().diagramTypeId());
        assertTrue(loaded.behaviorDiagram().isPresent());
        assertEquals(BehaviorDiagramKind.UML_USE_CASE, loaded.behaviorDiagram().get().diagramKind());
        assertEquals(1, loaded.behaviorDiagram().get().nodes().size());
    }
}
