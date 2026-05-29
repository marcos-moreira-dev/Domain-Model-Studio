package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import org.junit.jupiter.api.Test;

class InteractiveCanvasModelSnapshotTest {

    @Test
    void snapshotCopiesNodesConnectorsLayoutsSelectionAndBoundsWithoutJavaFx() {
        InMemoryInteractiveCanvasAdapter adapter = new InMemoryInteractiveCanvasAdapter(DiagramTypeId.ADMIN_MODULE_MAP)
                .addNode(InteractiveCanvasNode.of("a", "A", "module"), NodeLayout.at("a", 10, 20, 100, 50))
                .addNode(InteractiveCanvasNode.of("b", "B", "module"), NodeLayout.at("b", 200, 80, 120, 70))
                .addConnector(
                        InteractiveCanvasConnector.between("a_to_b", "a", "b", "usa"),
                        ConnectorLayout.straight("a_to_b", "a", "b")
                );
        adapter.selectNode("a", false);

        InteractiveCanvasModel model = InteractiveCanvasModel.from(adapter);

        assertEquals(2, model.nodes().size());
        assertEquals(1, model.connectors().size());
        assertTrue(model.layoutForNode("a").isPresent());
        assertTrue(model.layoutForConnector("a_to_b").isPresent());
        assertTrue(model.selection().isNodeSelected("a"));
        assertFalse(model.hasWarnings());
        CanvasBounds bounds = model.contentBounds().orElseThrow();
        assertEquals(10.0, bounds.x(), 0.0001);
        assertEquals(20.0, bounds.y(), 0.0001);
        assertEquals(310.0, bounds.width(), 0.0001);
        assertEquals(130.0, bounds.height(), 0.0001);
    }

    @Test
    void snapshotReportsMissingLayoutsWithoutFailingRenderingPipeline() {
        CanvasReadPort readPort = new CanvasReadPort() {
            @Override
            public DiagramTypeId diagramTypeId() {
                return DiagramTypeId.ADMIN_MODULE_MAP;
            }

            @Override
            public java.util.List<InteractiveCanvasNode> nodes() {
                return java.util.List.of(InteractiveCanvasNode.of("orphan", "Sin layout", "module"));
            }

            @Override
            public java.util.List<InteractiveCanvasConnector> connectors() {
                return java.util.List.of();
            }

            @Override
            public java.util.Optional<NodeLayout> layoutForNode(String elementId) {
                return java.util.Optional.empty();
            }

            @Override
            public java.util.Optional<ConnectorLayout> layoutForConnector(String connectorId) {
                return java.util.Optional.empty();
            }
        };

        InteractiveCanvasModel model = InteractiveCanvasModel.from(readPort, InteractiveCanvasSelection.empty());

        assertTrue(model.hasWarnings());
        assertEquals("orphan", model.warnings().getFirst().elementId());
        assertTrue(model.contentBounds().isEmpty());
    }
}
