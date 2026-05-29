package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import java.util.List;
import java.util.Optional;
import javafx.geometry.Point2D;
import org.junit.jupiter.api.Test;

class CanvasConnectorLabelPositionerTest {

    @Test
    void selfLoopLabelsUseTheVisualCurveMidpoint() {
        NodeLayout node = NodeLayout.at("nodo", 100.0, 120.0, 180.0, 100.0);

        List<Point2D> route = CanvasConnectorGeometry.edgeToEdgePoints(node, node, Optional.empty(), null);
        Point2D labelPoint = CanvasConnectorLabelPositioner.labelPoint(route, null);

        assertTrue(labelPoint.getY() < node.y(), "La etiqueta de autorrelación debe quedar sobre el nodo.");
        assertTrue(labelPoint.getY() > route.get(1).getY(), "La etiqueta no debe pegarse al vértice superior del lazo.");
        assertTrue(labelPoint.getX() > node.x() && labelPoint.getX() < node.x() + node.width(),
                "La etiqueta debe quedar centrada respecto al nodo y no fuera del lazo visible.");
    }

    @Test
    void straightConnectorLabelsStayAtPolylineMidpoint() {
        List<Point2D> route = List.of(new Point2D(10.0, 20.0), new Point2D(110.0, 20.0));

        Point2D labelPoint = CanvasConnectorLabelPositioner.labelPoint(route, null);

        assertEquals(60.0, labelPoint.getX(), 0.0001);
        assertEquals(20.0, labelPoint.getY(), 0.0001);
    }
}
