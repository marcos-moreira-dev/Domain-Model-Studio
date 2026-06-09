package com.marcosmoreira.domainmodelstudio.presentation.diagramcanvas;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Protege que las capas físicas mantengan la política base del canvas especializado. */
class Tanda17ConnectorLayeringSourceTest {

    @Test
    void connectorsShouldStayBetweenBackgroundNodesAndForegroundNodes() throws IOException {
        String source = Files.readString(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/diagramcanvas/ZoomableDiagramSurface.java"), StandardCharsets.UTF_8);
        String setAllBlock = source.substring(source.indexOf("workspaceRoot.getChildren().setAll"));
        int backgroundNodeLayer = setAllBlock.indexOf("layers.backgroundNodeLayer(),");
        int connectorLayer = setAllBlock.indexOf("layers.connectorLayer(),");
        int nodeLayer = setAllBlock.indexOf("layers.nodeLayer(),");
        int overlayLayer = setAllBlock.indexOf("layers.overlayLayer(),");

        assertTrue(backgroundNodeLayer > 0 && connectorLayer > backgroundNodeLayer,
                "Los conectores deben dibujarse sobre zonas, límites y contenedores de fondo.");
        assertTrue(nodeLayer > connectorLayer,
                "Las tarjetas/nodos operables deben quedar por encima de las relaciones.");
        assertTrue(overlayLayer > nodeLayer,
                "Labels, bendpoints, handles y selección deben seguir sobre nodos y conectores.");
        assertTrue(source.contains("Tanda 60") && source.contains("zonas/fondos"),
                "La razón visual del orden de capas debe quedar documentada.");
    }
}
