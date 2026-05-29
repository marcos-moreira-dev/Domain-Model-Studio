package com.marcosmoreira.domainmodelstudio.infrastructure.svg.specialized;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.visual.VisualLayoutService;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramKind;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorEdge;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorEdgeKind;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNode;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNodeKind;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.diagram.ProjectType;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

final class SequenceVisualSvgDiagramExporterTest {

    @Test
    void exportsSequenceAsVectorLifelinesAndMessages() {
        BehaviorNode user = new BehaviorNode("usuario", BehaviorNodeKind.PARTICIPANT, "Usuario", "", "", "", 0);
        BehaviorNode screen = new BehaviorNode("pantalla", BehaviorNodeKind.PARTICIPANT, "Pantalla", "", "", "", 1);
        BehaviorNode service = new BehaviorNode("servicio", BehaviorNodeKind.PARTICIPANT, "Servicio", "", "", "", 2);
        BehaviorDiagramDocument document = new BehaviorDiagramDocument(
                "Login",
                "borrador",
                LocalDate.now(),
                BehaviorDiagramKind.UML_SEQUENCE,
                List.of(user, screen, service),
                List.of(
                        new BehaviorEdge("m1", "usuario", "pantalla", BehaviorEdgeKind.MESSAGE, "ingresa credenciales", "", ""),
                        new BehaviorEdge("m2", "pantalla", "servicio", BehaviorEdgeKind.MESSAGE, "validar", "", ""),
                        new BehaviorEdge("m3", "servicio", "pantalla", BehaviorEdgeKind.RETURN_MESSAGE, "respuesta", "", "")
                ),
                "");
        DiagramProject project = DiagramProject.blank("login", "Login", ProjectType.CONCEPTUAL_MODEL, DiagramTypeId.UML_SEQUENCE)
                .withBehaviorDiagram(document);
        project = new VisualLayoutService().ensureVisualLayout(project);

        String svg = new SpecializedVisualSvgDiagramExporter().export(project);

        assertTrue(svg.contains("sequence-lifeline"));
        assertTrue(svg.contains("sequence-message"));
        assertTrue(svg.contains("sequence-message-return"));
        assertFalse(svg.contains("data:image"));
        assertFalse(svg.contains("<image"));
    }
}
