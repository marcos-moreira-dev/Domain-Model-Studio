package com.marcosmoreira.domainmodelstudio.presentation.diagramcanvas;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CanvasViewportFitPolicyTest {

    @Test
    void fitWidthUsesHorizontalRatio() {
        DiagramSurfaceConfig config = DiagramSurfaceConfig.defaults();
        CanvasViewportFitPolicy policy = CanvasViewportFitPolicy.fitWidth();

        double zoom = policy.computeZoom(1000, 4000, 1080, 680, config, 1.0);

        assertEquals(1.0, zoom, 0.0001);
    }

    @Test
    void fitHeightUsesVerticalRatio() {
        DiagramSurfaceConfig config = DiagramSurfaceConfig.defaults();
        CanvasViewportFitPolicy policy = CanvasViewportFitPolicy.fitHeight();

        double zoom = policy.computeZoom(4000, 1000, 1080, 680, config, 1.0);

        assertEquals(0.6, zoom, 0.0001);
    }

    @Test
    void centerOnlyKeepsCurrentZoom() {
        DiagramSurfaceConfig config = DiagramSurfaceConfig.defaults();
        CanvasViewportFitPolicy policy = CanvasViewportFitPolicy.centerOnly();

        double zoom = policy.computeZoom(4000, 1000, 1080, 680, config, 1.45);

        assertEquals(1.45, zoom, 0.0001);
    }
}
