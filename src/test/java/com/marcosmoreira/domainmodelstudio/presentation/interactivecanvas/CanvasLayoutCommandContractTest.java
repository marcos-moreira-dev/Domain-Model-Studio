package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import org.junit.jupiter.api.Test;

class CanvasLayoutCommandContractTest {

    @Test
    void layoutCoordinatorMovesThroughPortsAndMarksDirty() {
        InMemoryInteractiveCanvasAdapter adapter = new InMemoryInteractiveCanvasAdapter(DiagramTypeId.ADMIN_MODULE_MAP)
                .addNode(InteractiveCanvasNode.of("a", "A", "module"), NodeLayout.at("a", 10, 10, 80, 50));
        CanvasLayoutCommandCoordinator coordinator = new CanvasLayoutCommandCoordinator(adapter, adapter);

        coordinator.moveNodeTo("a", 300.0, 220.0);

        NodeLayout layout = adapter.layoutForNode("a").orElseThrow();
        assertEquals(300.0, layout.x(), 0.0001);
        assertEquals(220.0, layout.y(), 0.0001);
        assertTrue(adapter.dirty());
    }
}
