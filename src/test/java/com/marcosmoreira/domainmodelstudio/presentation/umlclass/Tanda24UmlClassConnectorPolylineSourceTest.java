package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíles para que UML Clases respete la edición transversal de relaciones sin polígonos rellenos. */
class Tanda24UmlClassConnectorPolylineSourceTest {

    @Test
    void connectorPolylinesMustNeverRenderFilledPolygons() throws IOException {
        String connectorFactory = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/drawing/DiagramConnectorFactory.java");
        String styleApplier = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasStyleApplier.java");

        assertTrue(connectorFactory.contains("line.setFill(Color.TRANSPARENT)"),
                "Las rutas de relación con puntos intermedios deben seguir siendo líneas abiertas, no polígonos rellenos.");
        assertTrue(connectorFactory.contains("-fx-fill: transparent"),
                "La transparencia del relleno debe quedar explícita incluso frente a CSS de selección.");
        assertTrue(styleApplier.contains("shape instanceof Line || shape instanceof Polyline"),
                "Los estilos explícitos de conectores no deben rellenar Polyline como si fuera una punta de flecha.");
        assertTrue(styleApplier.contains("shape.setFill(Color.TRANSPARENT)"),
                "Las líneas y polilíneas de conectores deben conservar relleno transparente.");
    }

    @Test
    void edgeToEdgeGeometryShouldRespectNearestBendPointForEndpointClipping() throws IOException {
        String geometry = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasConnectorGeometry.java");

        assertTrue(geometry.contains("Point2D firstWaypoint = bendPoints.isEmpty() ? targetCenter : toPoint(bendPoints.get(0))"),
                "El punto de salida debe apuntar al primer bendpoint cuando existe.");
        assertTrue(geometry.contains("Point2D lastWaypoint = bendPoints.isEmpty() ? sourceCenter : toPoint(bendPoints.get(bendPoints.size() - 1))"),
                "El punto de entrada al target debe calcularse contra el último bendpoint, no siempre contra el centro origen.");
        assertTrue(geometry.contains("points.add(edgePoint(source, firstWaypoint"));
        assertTrue(geometry.contains("points.add(edgePoint(target, lastWaypoint"));
    }

    private static String read(String path) throws IOException {
        return Files.readString(Path.of(path), StandardCharsets.UTF_8);
    }
}
