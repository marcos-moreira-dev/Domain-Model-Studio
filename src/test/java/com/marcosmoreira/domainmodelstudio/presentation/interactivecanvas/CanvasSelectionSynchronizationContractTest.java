package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CanvasSelectionSynchronizationContractTest {

    @Test
    void synchronizerUsesGuardToAvoidRecursiveSelectionLoops() {
        CountingSelectionPort selectionPort = new CountingSelectionPort();
        CanvasSelectionSynchronizationGuard guard = new CanvasSelectionSynchronizationGuard();
        CanvasSelectionSynchronizer synchronizer = new CanvasSelectionSynchronizer(selectionPort, guard);

        guard.runGuarded(() -> synchronizer.selectNodeFromPanel("a"));

        assertEquals(0, selectionPort.nodeSelections);
        assertFalse(guard.active());
    }

    @Test
    void synchronizerSelectsFromPanelAndCanvasThroughSamePort() {
        CountingSelectionPort selectionPort = new CountingSelectionPort();
        CanvasSelectionSynchronizer synchronizer = new CanvasSelectionSynchronizer(selectionPort);

        synchronizer.selectNodeFromPanel("a");
        synchronizer.selectConnectorFromCanvas("a-b", true);

        assertTrue(selectionPort.selection().isConnectorSelected("a-b"));
        assertEquals(1, selectionPort.nodeSelections);
        assertEquals(1, selectionPort.connectorSelections);
    }

    private static final class CountingSelectionPort implements CanvasSelectionPort {

        private InteractiveCanvasSelection selection = InteractiveCanvasSelection.empty();
        private int nodeSelections;
        private int connectorSelections;

        @Override
        public InteractiveCanvasSelection selection() {
            return selection;
        }

        @Override
        public void selectNode(String elementId, boolean additive) {
            nodeSelections++;
            selection = additive ? selection.toggledNode(elementId) : selection.withSingleNode(elementId);
        }

        @Override
        public void selectConnector(String connectorId, boolean additive) {
            connectorSelections++;
            selection = selection.withSingleConnector(connectorId);
        }

        @Override
        public void selectNodesInside(CanvasBounds selectionBounds, boolean additive) {
            // No requerido para esta prueba.
        }

        @Override
        public void clearSelection() {
            selection = InteractiveCanvasSelection.empty();
        }
    }
}
