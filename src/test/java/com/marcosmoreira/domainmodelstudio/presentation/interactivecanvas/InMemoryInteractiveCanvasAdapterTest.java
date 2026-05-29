package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import org.junit.jupiter.api.Test;

class InMemoryInteractiveCanvasAdapterTest {

    @Test
    void adapterMovesSelectedNodesWithoutKnowingConcreteDiagramType() {
        InMemoryInteractiveCanvasAdapter adapter = basicAdapter();

        adapter.selectNode("a", false);
        adapter.moveSelectedNodesBy(15.0, 20.0);

        NodeLayout layout = adapter.layoutForNode("a").orElseThrow();
        assertEquals(25.0, layout.x(), 0.0001);
        assertEquals(30.0, layout.y(), 0.0001);
        assertTrue(adapter.dirty());
    }

    @Test
    void rectangularSelectionUsesLayoutBounds() {
        InMemoryInteractiveCanvasAdapter adapter = basicAdapter();

        adapter.selectNodesInside(CanvasBounds.of(0.0, 0.0, 120.0, 120.0), false);

        assertTrue(adapter.selection().isNodeSelected("a"));
        assertFalse(adapter.selection().isNodeSelected("b"));
    }

    @Test
    void bendPointsAreManipulatedThroughToolbarFriendlyContract() {
        InMemoryInteractiveCanvasAdapter adapter = basicAdapter();
        CanvasBendPointController controller = new CanvasBendPointController(adapter);

        controller.add("a_to_b", 100.0, 40.0);
        controller.move("a_to_b", 0, 120.0, 55.0);
        controller.removeSelected();

        assertTrue(adapter.layoutForConnector("a_to_b").orElseThrow().bendPoints().isEmpty());
        assertTrue(adapter.selection().isEmpty());
    }

    private static InMemoryInteractiveCanvasAdapter basicAdapter() {
        return new InMemoryInteractiveCanvasAdapter(DiagramTypeId.ADMIN_MODULE_MAP)
                .addNode(InteractiveCanvasNode.of("a", "A", "module"), NodeLayout.at("a", 10, 10, 80, 50))
                .addNode(InteractiveCanvasNode.of("b", "B", "module"), NodeLayout.at("b", 200, 10, 80, 50))
                .addConnector(
                        InteractiveCanvasConnector.between("a_to_b", "a", "b", "usa"),
                        ConnectorLayout.straight("a_to_b", "a", "b")
                );
    }
}
