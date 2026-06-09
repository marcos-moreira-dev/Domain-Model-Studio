package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class InteractiveCanvasRenderBatchPolicyTest {

    @Test
    void shouldKeepSmallDiagramsSynchronous() {
        InteractiveCanvasRenderBatchPolicy policy = new InteractiveCanvasRenderBatchPolicy(220, 48, 36);

        assertFalse(policy.requiresBatchedRender(40, 60));
    }

    @Test
    void shouldBatchLargeDiagramsUsingSimpleVisualCost() {
        InteractiveCanvasRenderBatchPolicy policy = new InteractiveCanvasRenderBatchPolicy(220, 48, 36);

        assertTrue(policy.requiresBatchedRender(180, 120));
        assertTrue(policy.requiresBatchedRender(436, 387));
    }

    @Test
    void shouldRejectInvalidBatchSizes() {
        assertThrows(IllegalArgumentException.class, () -> new InteractiveCanvasRenderBatchPolicy(0, 48, 36));
        assertThrows(IllegalArgumentException.class, () -> new InteractiveCanvasRenderBatchPolicy(220, 0, 36));
        assertThrows(IllegalArgumentException.class, () -> new InteractiveCanvasRenderBatchPolicy(220, 48, 0));
    }
}
