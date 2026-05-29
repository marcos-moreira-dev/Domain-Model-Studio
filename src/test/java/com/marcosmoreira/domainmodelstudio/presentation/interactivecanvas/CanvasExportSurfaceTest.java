package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javafx.geometry.BoundingBox;
import org.junit.jupiter.api.Test;

class CanvasExportSurfaceTest {

    @Test
    void defaultExportSurfaceAddsPaddingAroundContentBounds() {
        CanvasExportSurface surface = CanvasExportSurface.of(CanvasBounds.of(100, 200, 300, 150));

        assertEquals(52.0, surface.exportX());
        assertEquals(152.0, surface.exportY());
        assertEquals(396.0, surface.exportWidth());
        assertEquals(246.0, surface.exportHeight());
    }

    @Test
    void canBuildExportSurfaceFromRenderedJavaFxBounds() {
        CanvasExportSurface surface = CanvasExportSurface.of(new BoundingBox(10, 20, 100, 80));

        assertEquals(-38.0, surface.exportX());
        assertEquals(-28.0, surface.exportY());
        assertEquals(196.0, surface.exportWidth());
        assertEquals(176.0, surface.exportHeight());
    }

    @Test
    void rejectsInvalidSurface() {
        assertThrows(IllegalArgumentException.class, () -> new CanvasExportSurface(null, 32.0));
        assertThrows(IllegalArgumentException.class, () -> new CanvasExportSurface(CanvasBounds.of(0, 0, 10, 10), -1.0));
    }
}
