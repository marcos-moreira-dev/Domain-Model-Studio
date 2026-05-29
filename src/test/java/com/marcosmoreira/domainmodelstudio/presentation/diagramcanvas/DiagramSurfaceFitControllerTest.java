package com.marcosmoreira.domainmodelstudio.presentation.diagramcanvas;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class DiagramSurfaceFitControllerTest {

    @Test
    void computesFitZoomWithPadding() {
        DiagramSurfaceConfig config = DiagramSurfaceConfig.defaults();

        double zoom = DiagramSurfaceFitController.computeFitZoom(1000, 500, 1080, 680, config);

        assertEquals(1.0, zoom, 0.0001);
    }

    @Test
    void clampsFitZoomToConfiguredRange() {
        DiagramSurfaceConfig config = DiagramSurfaceConfig.defaults();

        double tinyContentZoom = DiagramSurfaceFitController.computeFitZoom(10, 10, 4000, 4000, config);
        double hugeContentZoom = DiagramSurfaceFitController.computeFitZoom(100000, 100000, 300, 300, config);

        assertEquals(config.maxZoom(), tinyContentZoom);
        assertEquals(config.minZoom(), hugeContentZoom);
    }
}
