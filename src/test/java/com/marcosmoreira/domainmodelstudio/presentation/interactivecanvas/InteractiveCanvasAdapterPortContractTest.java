package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import org.junit.jupiter.api.Test;

class InteractiveCanvasAdapterPortContractTest {

    @Test
    void adapterIsStillCompatibleButCanBeConsumedBySmallPorts() {
        InMemoryInteractiveCanvasAdapter adapter = sampleAdapter();

        assertInstanceOf(CanvasReadPort.class, adapter);
        assertInstanceOf(CanvasSelectionPort.class, adapter);
        assertInstanceOf(CanvasLayoutCommandPort.class, adapter);
        assertInstanceOf(CanvasBendPointPort.class, adapter);
        assertInstanceOf(CanvasDirtyPort.class, adapter);

        CanvasSelectionPort selectionPort = adapter;
        CanvasLayoutCommandPort layoutPort = adapter;
        CanvasDirtyPort dirtyPort = adapter;

        selectionPort.selectNode("module:a", false);
        layoutPort.moveSelectedNodesBy(20.0, 30.0);
        dirtyPort.markDirty();

        NodeLayout layout = adapter.layoutForNode("module:a").orElseThrow();
        assertEquals(120.0, layout.x(), 0.0001);
        assertEquals(130.0, layout.y(), 0.0001);
        assertTrue(adapter.dirty());
    }

    private static InMemoryInteractiveCanvasAdapter sampleAdapter() {
        return new InMemoryInteractiveCanvasAdapter(DiagramTypeId.ADMIN_MODULE_MAP)
                .addNode(InteractiveCanvasNode.of("module:a", "A", "module"), NodeLayout.at("module:a", 100, 100, 80, 50))
                .addNode(InteractiveCanvasNode.of("module:b", "B", "module"), NodeLayout.at("module:b", 260, 100, 80, 50))
                .addConnector(
                        InteractiveCanvasConnector.between("dependency:a-b", "module:a", "module:b", "usa"),
                        ConnectorLayout.straight("dependency:a-b", "module:a", "module:b")
                );
    }
}
