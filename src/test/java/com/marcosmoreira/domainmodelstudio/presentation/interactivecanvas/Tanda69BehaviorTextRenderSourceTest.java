package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl de Tanda 69: comportamiento usa bounds textuales y selección sin sombrear etiquetas. */
final class Tanda69BehaviorTextRenderSourceTest {

    @Test
    void activeSelectionShouldNotApplyDropShadowToWholeNodeRoot() throws IOException {
        String css = Files.readString(Path.of("src/main/resources/css/interactive-canvas.css"));

        assertTrue(css.contains("Tanda 69"));
        assertTrue(css.contains(".interactive-canvas-node-active-selection {\n    -fx-effect: none;"),
                "El foco activo no debe sombrear toda la raíz porque también ensucia el texto.");
        assertFalse(css.contains(".interactive-canvas-node-active-selection {\n    -fx-effect: dropshadow"),
                "La sombra global de raíz vuelve borrosas las etiquetas internas.");
    }

    @Test
    void behaviorRenderKitShouldConfigureLabelsFromCanvasBounds() throws IOException {
        String renderKit = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/behavior/BehaviorRenderKit.java"));

        assertTrue(renderKit.contains("double textWidth = Math.max(48.0, bounds.width() - 26.0)"));
        assertTrue(renderKit.contains("wrappedLabel("));
        assertTrue(renderKit.contains("label.setTextOverrun(OverrunStyle.CLIP)"));
        assertTrue(renderKit.contains("label.setPrefWidth(maxWidth)"));
        assertTrue(renderKit.contains("label.setMinHeight(Region.USE_PREF_SIZE)"));
    }
}
