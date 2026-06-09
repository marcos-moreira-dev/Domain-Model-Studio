package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CanvasSelectionSupportTest {

    @Test
    void selectsNodesAndTogglesAdditiveSelection() {
        CanvasSelectionSupport support = new CanvasSelectionSupport();

        support.selectNode("node:a", false);
        support.selectNode("node:b", true);
        support.selectNode("node:a", true);

        assertFalse(support.current().isNodeSelected("node:a"));
        assertTrue(support.current().isNodeSelected("node:b"));
    }

    @Test
    void preservesManualBendPointSelection() {
        CanvasSelectionSupport support = new CanvasSelectionSupport();

        support.selectBendPoint("connector:rel", 2);

        assertTrue(support.shouldPreserveManualBendPointSelection());
    }
}
