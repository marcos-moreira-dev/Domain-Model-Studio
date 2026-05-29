package com.marcosmoreira.domainmodelstudio.presentation.canvas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.layout.BendPoint;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorPathKind;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayouts;
import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import com.marcosmoreira.domainmodelstudio.presentation.selection.DiagramSelectionModel;
import org.junit.jupiter.api.Test;

class DiagramCanvasViewModelBendPointSelectionTest {

    @Test
    void deleteSelectedElementRemovesBendPointBeforeDeletingWholeConnector() {
        DiagramCanvasViewModel viewModel = newViewModel();
        DiagramElementId connectorId = DiagramElementId.of("conn_cliente_matricula");
        viewModel.showImportedProject(projectWithConnectorBendPoints(connectorId));

        viewModel.selectBendPoint(connectorId, 0);
        viewModel.removeSelectedElement();

        ConnectorLayout connector = viewModel.currentProject()
                .layouts()
                .layoutFor(NotationType.CHEN)
                .orElseThrow()
                .connectorById(connectorId)
                .orElseThrow();
        assertEquals(1, connector.bendPoints().size());
        assertEquals(ConnectorPathKind.POLYLINE, connector.pathKind());
        assertNull(viewModel.selectedBendPoint());
        assertTrue(viewModel.isSelected(connectorId));
    }

    @Test
    void removingLastBendPointKeepsConnectorAndReturnsToStraightPath() {
        DiagramCanvasViewModel viewModel = newViewModel();
        DiagramElementId connectorId = DiagramElementId.of("conn_cliente_matricula");
        viewModel.showImportedProject(projectWithSingleBendPoint(connectorId));

        viewModel.selectBendPoint(connectorId, 0);
        assertTrue(viewModel.removeSelectedBendPoint());

        ConnectorLayout connector = viewModel.currentProject()
                .layouts()
                .layoutFor(NotationType.CHEN)
                .orElseThrow()
                .connectorById(connectorId)
                .orElseThrow();
        assertEquals(0, connector.bendPoints().size());
        assertEquals(ConnectorPathKind.STRAIGHT, connector.pathKind());
    }

    @Test
    void selectingRegularElementClearsBendPointSelection() {
        DiagramCanvasViewModel viewModel = newViewModel();
        DiagramElementId connectorId = DiagramElementId.of("conn_cliente_matricula");
        viewModel.showImportedProject(projectWithConnectorBendPoints(connectorId));

        viewModel.selectBendPoint(connectorId, 0);
        viewModel.selectElement(connectorId);

        assertNull(viewModel.selectedBendPoint());
    }

    private static DiagramProject projectWithConnectorBendPoints(DiagramElementId connectorId) {
        ConnectorLayout connector = ConnectorLayout.straight(connectorId.value(), "cliente", "matricula")
                .withBendPoint(BendPoint.of(120, 90))
                .withBendPoint(BendPoint.of(220, 90));
        return projectWithConnector(connector);
    }

    private static DiagramProject projectWithSingleBendPoint(DiagramElementId connectorId) {
        ConnectorLayout connector = ConnectorLayout.straight(connectorId.value(), "cliente", "matricula")
                .withBendPoint(BendPoint.of(120, 90));
        return projectWithConnector(connector);
    }

    private static DiagramProject projectWithConnector(ConnectorLayout connector) {
        DiagramLayout layout = DiagramLayout.empty(NotationType.CHEN).withConnector(connector);
        DiagramLayouts layouts = DiagramLayouts.empty().withLayout(layout);
        return DiagramProject.blank("bendpoints", "Puntos intermedios").withLayouts(layouts);
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
