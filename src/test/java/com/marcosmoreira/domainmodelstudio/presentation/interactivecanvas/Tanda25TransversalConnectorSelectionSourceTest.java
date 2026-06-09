package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class Tanda25TransversalConnectorSelectionSourceTest {

    @Test
    void selectedConnectorVisualSupportMustRunAfterExplicitStyles() throws IOException {
        String source = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/InteractiveCanvasSurfaceView.java");
        assertTrue(source.contains("explicitStyleFor(connector.id()).ifPresent"),
                "Los estilos explícitos del proyecto deben seguir aplicándose.");
        assertTrue(source.contains("CanvasConnectorSelectionVisualSupport.apply(rendered)"),
                "La selección transversal debe aplicarse al final para ganar sobre estilos por familia.");
        assertTrue(source.indexOf("explicitStyleFor(connector.id()).ifPresent")
                        < source.indexOf("CanvasConnectorSelectionVisualSupport.apply(rendered)"),
                "El resaltado azul debe aplicarse después de los estilos explícitos.");
    }

    @Test
    void selectionVisualSupportMustNotStyleHitboxesOrBendPointHandlesAsConnectorLines() throws IOException {
        String source = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasConnectorSelectionVisualSupport.java");
        assertTrue(source.contains("interactive-canvas-connector-selected-root"));
        assertTrue(source.contains("interactive-canvas-connector-selected"));
        assertTrue(source.contains("styleClass.contains(\"hitbox\")"),
                "El hitbox invisible de una relación no debe pintarse como línea azul.");
        assertTrue(source.contains("styleClass.contains(\"bend-point\")"),
                "Los puntos intermedios mantienen su propio estilo seleccionado.");
        assertTrue(source.contains("-fx-stroke: -dms-accent"),
                "La relación seleccionada debe verse azul de forma transversal.");
    }

    @Test
    void finalCssOverrideMustBeImportedAfterDiagramFamilies() throws IOException {
        String app = read("src/main/resources/css/app-light.css");
        String css = read("src/main/resources/css/interactive-canvas-selection-overrides.css");
        assertTrue(app.contains("free-graph.css"));
        assertTrue(app.contains("interactive-canvas-selection-overrides.css"));
        assertTrue(app.indexOf("free-graph.css") < app.indexOf("interactive-canvas-selection-overrides.css"),
                "La hoja de selección transversal debe cargarse después de las familias visuales.");
        assertTrue(css.contains(".uml-class-canvas-connector-selected"));
        assertTrue(css.contains(".architecture-canvas-connector-selected"));
        assertTrue(css.contains(".behavior-canvas-connector-selected"));
        assertTrue(css.contains(".free-graph-canvas-connector-selected"));
    }

    private static String read(String path) throws IOException {
        return Files.readString(Path.of(path), StandardCharsets.UTF_8);
    }
}
