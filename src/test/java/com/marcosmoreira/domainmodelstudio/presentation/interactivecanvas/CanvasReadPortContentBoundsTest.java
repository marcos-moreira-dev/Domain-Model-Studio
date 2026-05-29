package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class CanvasReadPortContentBoundsTest {

    @Test
    void defaultContentBoundsUsesVisibleNodeLayoutsWithoutKnowingDiagramFamily() {
        CanvasReadPort port = new StaticCanvasReadPort(
                List.of(
                        InteractiveCanvasNode.of("a", "A", "module"),
                        InteractiveCanvasNode.of("b", "B", "module")
                ),
                Map.of(
                        "a", NodeLayout.at("a", 20.0, 30.0, 100.0, 50.0),
                        "b", NodeLayout.at("b", 250.0, 160.0, 140.0, 70.0)
                )
        );

        CanvasBounds bounds = port.contentBounds();

        assertEquals(20.0, bounds.x(), 0.0001);
        assertEquals(30.0, bounds.y(), 0.0001);
        assertEquals(370.0, bounds.width(), 0.0001);
        assertEquals(200.0, bounds.height(), 0.0001);
    }

    @Test
    void defaultContentBoundsFallsBackToUsefulCanvasWhenThereAreNoVisibleLayouts() {
        CanvasReadPort port = new StaticCanvasReadPort(List.of(), Map.of());

        CanvasBounds bounds = port.contentBounds();

        assertEquals(0.0, bounds.x(), 0.0001);
        assertEquals(0.0, bounds.y(), 0.0001);
        assertEquals(880.0, bounds.width(), 0.0001);
        assertEquals(600.0, bounds.height(), 0.0001);
    }

    private record StaticCanvasReadPort(
            List<InteractiveCanvasNode> nodes,
            Map<String, NodeLayout> layouts
    ) implements CanvasReadPort {

        @Override
        public DiagramTypeId diagramTypeId() {
            return DiagramTypeId.ADMIN_MODULE_MAP;
        }

        @Override
        public List<InteractiveCanvasConnector> connectors() {
            return List.of();
        }

        @Override
        public Optional<NodeLayout> layoutForNode(String elementId) {
            return Optional.ofNullable(layouts.get(elementId));
        }

        @Override
        public Optional<ConnectorLayout> layoutForConnector(String connectorId) {
            return Optional.empty();
        }
    }
}
