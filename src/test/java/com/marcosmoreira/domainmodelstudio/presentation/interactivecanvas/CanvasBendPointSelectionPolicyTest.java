package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CanvasBendPointSelectionPolicyTest {

    @Test
    void preservesManualBendPointSelectionDuringExternalPanelSync() {
        InteractiveCanvasSelection selection = CanvasBendPointSelectionPolicy.selectBendPoint("edge:1", 2);

        assertTrue(CanvasBendPointSelectionPolicy.preserveManualBendPointSelection(selection));
        assertTrue(selection.isConnectorSelected("edge:1"));
        assertTrue(selection.selectedBendPoint().isPresent());
    }

    @Test
    void doesNotPreserveRegularNodeOrConnectorSelection() {
        assertFalse(CanvasBendPointSelectionPolicy.preserveManualBendPointSelection(InteractiveCanvasSelection.empty()));
        assertFalse(CanvasBendPointSelectionPolicy.preserveManualBendPointSelection(
                InteractiveCanvasSelection.empty().withSingleNode("node:1")));
        assertFalse(CanvasBendPointSelectionPolicy.preserveManualBendPointSelection(
                InteractiveCanvasSelection.empty().withSingleConnector("edge:1")));
    }
}
