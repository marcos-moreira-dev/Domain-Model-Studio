package com.marcosmoreira.domainmodelstudio.presentation.diagramcanvas;

import javafx.geometry.Bounds;

/** Puerto de navegación expuesto por una superficie zoomable. */
public interface DiagramSurfaceCommandPort {

    void zoomIn();

    void zoomOut();

    void resetZoom();

    void fitToContent();

    void fitToContent(Bounds bounds);

    void centerContent();

    void centerOn(Bounds bounds);
}
