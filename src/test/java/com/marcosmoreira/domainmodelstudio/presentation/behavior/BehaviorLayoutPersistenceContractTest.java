package com.marcosmoreira.domainmodelstudio.presentation.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.behavior.AddBehaviorEdgeUseCase;
import com.marcosmoreira.domainmodelstudio.application.behavior.AddBehaviorNodeUseCase;
import com.marcosmoreira.domainmodelstudio.application.behavior.RemoveBehaviorItemUseCase;
import com.marcosmoreira.domainmodelstudio.application.behavior.UpdateBehaviorEdgeUseCase;
import com.marcosmoreira.domainmodelstudio.application.behavior.UpdateBehaviorNodeUseCase;
import com.marcosmoreira.domainmodelstudio.application.behavior.ValidateBehaviorDiagramUseCase;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualElementLayoutIds;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramKind;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorEdge;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorEdgeKind;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNode;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNodeKind;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class BehaviorLayoutPersistenceContractTest {

    @Test
    void viewModelMovesBehaviorNodeThroughVisualLayoutInsteadOfDomainCoordinates() {
        List<DiagramProject> changes = new ArrayList<>();
        BehaviorDiagramViewModel viewModel = newViewModel();
        viewModel.registerProjectChangeListener(changes::add);
        viewModel.loadProject(project());

        viewModel.moveNodeTo("recibir-solicitud", 430, 250);

        assertTrue(!changes.isEmpty());
        DiagramProject changed = changes.get(changes.size() - 1);
        var layout = changed.layouts().activeLayout().nodeFor(VisualElementLayoutIds.behaviorNode("recibir-solicitud")).orElseThrow();
        assertEquals(430, layout.x());
        assertEquals(250, layout.y());
        assertEquals("recibir-solicitud", changed.behaviorDiagram().orElseThrow().nodes().get(0).id());
    }

    @Test
    void viewModelPersistsBendPointForBehaviorEdge() {
        List<DiagramProject> changes = new ArrayList<>();
        BehaviorDiagramViewModel viewModel = newViewModel();
        viewModel.registerProjectChangeListener(changes::add);
        viewModel.loadProject(project());

        viewModel.addConnectorBendPoint(VisualElementLayoutIds.behaviorEdge("flujo-1"), 320, 190);

        DiagramProject changed = changes.get(changes.size() - 1);
        var layout = changed.layouts().activeLayout().connectorById(VisualElementLayoutIds.behaviorEdge("flujo-1")).orElseThrow();
        assertEquals(1, layout.bendPoints().size());
        assertEquals(320, layout.bendPoints().get(0).x());
        assertEquals(190, layout.bendPoints().get(0).y());
    }

    private static BehaviorDiagramViewModel newViewModel() {
        return new BehaviorDiagramViewModel(
                new AddBehaviorNodeUseCase(),
                new AddBehaviorEdgeUseCase(),
                new UpdateBehaviorNodeUseCase(),
                new UpdateBehaviorEdgeUseCase(),
                new RemoveBehaviorItemUseCase(),
                new ValidateBehaviorDiagramUseCase(),
                ignored -> { }
        );
    }

    private static DiagramProject project() {
        BehaviorNode start = new BehaviorNode("recibir-solicitud", BehaviorNodeKind.ACTIVITY,
                "Recibir solicitud", "Secretaría", "Entrada operativa", "", 0);
        BehaviorNode end = new BehaviorNode("validar-solicitud", BehaviorNodeKind.DECISION,
                "Validar solicitud", "Secretaría", "Revisión de datos", "", 1);
        BehaviorEdge edge = new BehaviorEdge("flujo-1", start.id(), end.id(), BehaviorEdgeKind.FLOW,
                "continúa", "", "");
        BehaviorDiagramDocument document = new BehaviorDiagramDocument(
                "UENS",
                "borrador",
                LocalDate.of(2026, 5, 1),
                BehaviorDiagramKind.OPERATIONAL_FLOW,
                List.of(start, end),
                List.of(edge),
                "");
        return DiagramProject.blank("flujo-uens", "Flujo UENS", DiagramTypeId.OPERATIONAL_FLOW)
                .withBehaviorDiagram(document);
    }
}
