package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class CanvasConnectorGeometrySelfLoopTest {

    @Test
    void selfLoopWithoutBendPointsUsesReadableCurvedRouteAroundNode() {
        NodeLayout node = NodeLayout.at("nodo", 100.0, 120.0, 180.0, 100.0);

        var points = CanvasConnectorGeometry.edgeToEdgePoints(node, node, Optional.empty(), null);

        assertEquals(4, points.size());
        assertTrue(points.get(1).getY() < node.y(), "La autorrelación debe salir por arriba del nodo.");
        assertTrue(points.get(2).getY() < node.y(), "La autorrelación debe entrar desde una curva superior.");
        assertTrue(points.get(0).getX() > points.get(3).getX(), "La entrada y salida no deben colapsar en el mismo punto.");
    }
}
