package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import org.junit.jupiter.api.Test;

final class CanvasNodeDragControllerTest {

    @Test
    void dragRestoresPressedNodeWhenExternalSelectionSynchronizationClearsIt() {
        InMemoryInteractiveCanvasAdapter adapter = new InMemoryInteractiveCanvasAdapter(DiagramTypeId.FREE_GRAPH)
                .addNode(InteractiveCanvasNode.of("N1", "Nodo", "node"), NodeLayout.at("N1", 10, 20, 120, 80));
        CanvasNodeDragController controller = new CanvasNodeDragController(adapter);

        controller.begin("N1", 10, 20, false);
        adapter.clearSelection();

        controller.dragTo(35, 45);

        NodeLayout updated = adapter.layoutForNode("N1").orElseThrow();
        assertEquals(35, updated.x(), 0.0001);
        assertEquals(45, updated.y(), 0.0001);
        assertTrue(adapter.selection().isNodeSelected("N1"));
        assertTrue(adapter.dirty());
    }
}
