package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl de Tanda 71: los rombos de comportamiento respetan el sizing transversal. */
final class Tanda71BehaviorDiamondTextFitSourceTest {

    @Test
    void behaviorDiamondsShouldDeriveTextBoxFromCanvasBounds() throws IOException {
        String renderKit = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/behavior/BehaviorRenderKit.java"));

        assertTrue(renderKit.contains("primitiveTextWidth(semanticKind, bounds)"),
                "El texto del nodo primitivo debe derivarse del CanvasBounds calculado por el layout transversal.");
        assertTrue(renderKit.contains("\"decision\".equals(semanticKind)"),
                "Los rombos requieren una caja textual específica para respetar su geometría diagonal.");
        assertTrue(renderKit.contains("bounds.width() * 0.58"),
                "El texto del rombo debe ocupar solo la zona central segura, no todo el ancho del cuadrado.");
        assertTrue(renderKit.contains("title.setMaxHeight(primitiveTextHeight(semanticKind, bounds))"),
                "El alto textual del rombo debe estar acotado para no invadir los vértices.");
    }

    @Test
    void behaviorDiamondCssShouldUseReadableWeight() throws IOException {
        String css = Files.readString(Path.of("src/main/resources/css/behavior-diagram.css"));

        assertTrue(css.contains(".behavior-canvas-primitive-title-decision"));
        assertTrue(css.contains("-fx-font-size: 10.2px"));
        assertTrue(css.contains("-fx-font-weight: 600"));
    }
}
