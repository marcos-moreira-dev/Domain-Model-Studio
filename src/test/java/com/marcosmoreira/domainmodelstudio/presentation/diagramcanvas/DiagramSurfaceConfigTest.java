package com.marcosmoreira.domainmodelstudio.presentation.diagramcanvas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class DiagramSurfaceConfigTest {

    @Test
    void defaultsFollowCanonicalWorkspaceScale() {
        DiagramSurfaceConfig config = DiagramSurfaceConfig.defaults();

        assertEquals(12000.0, config.workspaceWidth());
        assertEquals(8000.0, config.workspaceHeight());
        assertEquals(4600.0, config.contentOriginX());
        assertEquals(3000.0, config.contentOriginY());
        assertTrue(config.showGrid());
    }

    @Test
    void clampZoomProtectsConfiguredRange() {
        DiagramSurfaceConfig config = DiagramSurfaceConfig.defaults();

        assertEquals(config.minZoom(), config.clampZoom(0.01));
        assertEquals(config.maxZoom(), config.clampZoom(999.0));
        assertEquals(1.25, config.clampZoom(1.25));
    }

    @Test
    void invalidZoomRangeIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new DiagramSurfaceConfig(
                12000,
                8000,
                0,
                0,
                2.0,
                1.0,
                1.08,
                80,
                true
        ));
    }
}
