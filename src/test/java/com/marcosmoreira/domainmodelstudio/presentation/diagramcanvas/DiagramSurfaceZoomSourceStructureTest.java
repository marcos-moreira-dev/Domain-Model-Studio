package com.marcosmoreira.domainmodelstudio.presentation.diagramcanvas;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class DiagramSurfaceZoomSourceStructureTest {

    @Test
    void wheelZoomIsAnchoredToViewportPointInsteadOfTopLeftCorner() throws IOException {
        String source = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/diagramcanvas/DiagramSurfaceZoomController.java"
        ), StandardCharsets.UTF_8);

        assertTrue(source.contains("sceneToLocal(event.getSceneX(), event.getSceneY())"),
                "El zoom con rueda debe tomar el punto real del cursor dentro del viewport.");
        assertTrue(source.contains("setZoomAtViewportPoint"),
                "El zoom con rueda debe delegar a zoom anclado por punto de viewport.");
        assertTrue(source.contains("event.consume()"),
                "La rueda de zoom no debe propagarse como scroll accidental del shell.");
    }

    @Test
    void viewportKeepsMovementInScrollbarsNotPermanentTranslation() throws IOException {
        String source = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/diagramcanvas/DiagramSurfaceViewportController.java"
        ), StandardCharsets.UTF_8);

        assertTrue(source.contains("scrollPane.setHvalue"), "El paneo horizontal debe vivir en el scrollbar.");
        assertTrue(source.contains("scrollPane.setVvalue"), "El paneo vertical debe vivir en el scrollbar.");
        assertTrue(source.contains("new Scale(zoomFactor, zoomFactor, 0, 0)"),
                "La escala debe ser explícita y controlada por el viewport controller.");
        assertFalse(source.contains("setTranslateX"), "El viewport canónico no debe corregir zoom con translateX permanente.");
        assertFalse(source.contains("setTranslateY"), "El viewport canónico no debe corregir zoom con translateY permanente.");
    }
}
