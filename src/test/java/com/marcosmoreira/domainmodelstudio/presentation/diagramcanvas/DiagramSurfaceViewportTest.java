package com.marcosmoreira.domainmodelstudio.presentation.diagramcanvas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class DiagramSurfaceViewportTest {

    @Test
    void scrollValuesAreClamped() {
        DiagramSurfaceViewport viewport = new DiagramSurfaceViewport(-1.0, 2.0, 1.0);

        assertEquals(0.0, viewport.hvalue());
        assertEquals(1.0, viewport.vvalue());
    }

    @Test
    void zoomMustBePositive() {
        assertThrows(IllegalArgumentException.class, () -> new DiagramSurfaceViewport(0.5, 0.5, 0.0));
    }

    @Test
    void withZoomKeepsScrollValues() {
        DiagramSurfaceViewport viewport = new DiagramSurfaceViewport(0.3, 0.7, 1.0).withZoom(1.6);

        assertEquals(0.3, viewport.hvalue());
        assertEquals(0.7, viewport.vvalue());
        assertEquals(1.6, viewport.zoomFactor());
    }
}
