package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import javafx.geometry.Point2D;

/** Convierte coordenadas de escena JavaFX a coordenadas semánticas del lienzo. */
@FunctionalInterface
public interface CanvasPointMapper {

    Point2D toCanvasPoint(double sceneX, double sceneY);
}
