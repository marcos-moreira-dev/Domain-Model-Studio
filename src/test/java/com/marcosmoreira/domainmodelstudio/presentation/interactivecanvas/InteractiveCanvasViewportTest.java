package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class InteractiveCanvasViewportTest {

    @Test
    void zoomAtKeepsCanvasPointUnderCursorStable() {
        InteractiveCanvasViewport viewport = new InteractiveCanvasViewport();
        double beforeX = viewport.canvasX(120.0);
        double beforeY = viewport.canvasY(80.0);

        viewport.zoomAt(120.0, 80.0, 1.10);

        assertEquals(beforeX, viewport.canvasX(120.0), 0.0001);
        assertEquals(beforeY, viewport.canvasY(80.0), 0.0001);
        assertTrue(viewport.scale() > 1.0);
    }

    @Test
    void scaleIsClampedToHumanUsableRange() {
        InteractiveCanvasViewport viewport = new InteractiveCanvasViewport();

        for (int index = 0; index < 100; index++) {
            viewport.zoomAt(0.0, 0.0, 2.0);
        }
        assertEquals(InteractiveCanvasViewport.MAX_SCALE, viewport.scale(), 0.0001);

        for (int index = 0; index < 100; index++) {
            viewport.zoomAt(0.0, 0.0, 0.5);
        }
        assertEquals(InteractiveCanvasViewport.MIN_SCALE, viewport.scale(), 0.0001);
    }
}
