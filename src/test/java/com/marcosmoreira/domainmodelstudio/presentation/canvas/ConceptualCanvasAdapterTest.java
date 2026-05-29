package com.marcosmoreira.domainmodelstudio.presentation.canvas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.editing.AddAttributeUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.AddBendPointUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.AddEntityUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.AddRelationshipUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.ChangeConnectorAnchorsUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.DuplicateEntityUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.MoveBendPointUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.MoveConnectorLabelUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.MoveElementUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.RemoveBendPointUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.RemoveDiagramElementUseCase;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramModel;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.er.EntityElement;
import com.marcosmoreira.domainmodelstudio.domain.er.RelationshipElement;
import com.marcosmoreira.domainmodelstudio.domain.layout.BendPoint;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayouts;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasBounds;
import com.marcosmoreira.domainmodelstudio.presentation.selection.DiagramSelectionModel;
import java.util.List;
import org.junit.jupiter.api.Test;

class ConceptualCanvasAdapterTest {

    @Test
    void exposesConceptualProjectAsCommonCanvasContract() {
        DiagramCanvasViewModel viewModel = newViewModel();
        viewModel.showImportedProject(sampleProject());
        ConceptualCanvasAdapter adapter = new ConceptualCanvasAdapter(viewModel);

        assertEquals(2, adapter.nodes().size());
        assertEquals(1, adapter.connectors().size());
        assertTrue(adapter.layoutForNode("estudiante").isPresent());
        assertTrue(adapter.layoutForConnector("matricula").isPresent());

        adapter.selectNode("estudiante", false);
        assertTrue(adapter.selection().isNodeSelected("estudiante"));

        adapter.selectNodesInside(CanvasBounds.of(0, 0, 120, 90), false);
        assertTrue(adapter.selection().isNodeSelected("estudiante"));
    }

    @Test
    void delegatesBendPointOperationsToConceptualViewModel() {
        DiagramCanvasViewModel viewModel = newViewModel();
        viewModel.showImportedProject(sampleProject());
        ConceptualCanvasAdapter adapter = new ConceptualCanvasAdapter(viewModel);

        adapter.addBendPoint("matricula", 100, 30);
        assertTrue(adapter.selection().selectedBendPoint().isPresent());
        assertEquals(1, adapter.layoutForConnector("matricula").orElseThrow().bendPoints().size());

        adapter.moveBendPoint("matricula", 0, 130, 45);
        BendPoint moved = adapter.layoutForConnector("matricula").orElseThrow().bendPoints().get(0);
        assertEquals(130, moved.x());
        assertEquals(45, moved.y());

        adapter.removeSelectedBendPoint();
        assertEquals(0, adapter.layoutForConnector("matricula").orElseThrow().bendPoints().size());
    }

    private static DiagramProject sampleProject() {
        EntityElement student = EntityElement.strong("estudiante", "Estudiante", List.of());
        EntityElement section = EntityElement.strong("seccion", "Sección", List.of());
        RelationshipElement enrollment = RelationshipElement.between(
                "matricula", "Matrícula", "estudiante", "seccion", "0..M", "1"
        );
        DiagramModel model = new DiagramModel(List.of(student, section), List.of(enrollment));
        DiagramLayout layout = DiagramLayout.empty(NotationType.CHEN)
                .withNode(NodeLayout.at("estudiante", 10, 10, 90, 50))
                .withNode(NodeLayout.at("seccion", 260, 10, 90, 50))
                .withConnector(ConnectorLayout.straight("matricula", "estudiante", "seccion"));
        return DiagramProject.blank("uens", "UENS")
                .withModel(model)
                .withLayouts(DiagramLayouts.empty().withLayout(layout));
    }

    private static DiagramCanvasViewModel newViewModel() {
        return new DiagramCanvasViewModel(
                new DiagramSelectionModel(),
                new AddEntityUseCase(),
                new AddAttributeUseCase(),
                new AddRelationshipUseCase(),
                new DuplicateEntityUseCase(),
                new RemoveDiagramElementUseCase(),
                new MoveElementUseCase(),
                new AddBendPointUseCase(),
                new MoveBendPointUseCase(),
                new MoveConnectorLabelUseCase(),
                new RemoveBendPointUseCase(),
                new ChangeConnectorAnchorsUseCase(),
                ignored -> { }
        );
    }
}
