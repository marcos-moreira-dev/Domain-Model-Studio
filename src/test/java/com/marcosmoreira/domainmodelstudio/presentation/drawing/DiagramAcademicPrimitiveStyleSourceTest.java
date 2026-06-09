package com.marcosmoreira.domainmodelstudio.presentation.drawing;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl visual para mantener diagramas especializados con estética académica primitiva. */
class DiagramAcademicPrimitiveStyleSourceTest {

    private static final Path APP_LIGHT = Path.of("src/main/resources/css/app-light.css");
    private static final Path PRIMITIVE_CSS = Path.of("src/main/resources/css/diagram-academic-primitives.css");

    @Test
    void primitiveDiagramContractMustBeLoadedAfterSpecializedDiagramStyles() throws IOException {
        String appLight = Files.readString(APP_LIGHT, StandardCharsets.UTF_8);

        int primitiveImport = appLight.indexOf("diagram-academic-primitives.css");
        assertTrue(primitiveImport > 0, "app-light.css debe importar el contrato visual primitivo.");

        for (String previous : new String[] {
                "behavior-diagram.css",
                "sequence-diagram.css",
                "architecture-diagram.css",
                "module-map.css",
                "uml-class.css",
                "screen-flow.css",
                "wireframe.css",
                "free-graph.css"
        }) {
            int previousImport = appLight.indexOf(previous);
            assertTrue(previousImport >= 0, "Debe existir import previo de " + previous);
            assertTrue(previousImport < primitiveImport, "El contrato primitivo debe cargar después de " + previous);
        }
    }

    @Test
    void primitiveDiagramContractMustAvoidDecorativeEffects() throws IOException {
        String css = Files.readString(PRIMITIVE_CSS, StandardCharsets.UTF_8).toLowerCase();

        assertFalse(css.contains("linear-gradient"), "Los diagramas primitivos no deben usar degradados.");
        assertFalse(css.contains("dropshadow"), "Los diagramas primitivos no deben usar sombras decorativas.");
        assertFalse(css.contains("drop-shadow"), "Los diagramas primitivos no deben usar filtros de sombra.");
        assertTrue(css.contains("-fx-effect: none;"), "Debe neutralizar efectos heredados.");
        assertTrue(css.contains("-fx-border-radius: 0;"), "Debe neutralizar bordes redondeados heredados.");
        assertTrue(css.contains("-fx-background-radius: 0;"), "Debe neutralizar fondos redondeados heredados.");
    }

    @Test
    void primitiveDiagramContractMustCoverSpecializedVisualFamiliesWithoutTouchingConceptualCanvas()
            throws IOException {
        String css = Files.readString(PRIMITIVE_CSS, StandardCharsets.UTF_8);

        for (String selector : new String[] {
                ".behavior-canvas-node",
                ".architecture-canvas-node",
                ".module-map-canvas-node",
                ".screen-flow-diagram-node",
                ".free-graph-node-body",
                ".uml-class-canvas-node",
                ".wireframe-canvas-node",
                ".sequence-message-label"
        }) {
            assertTrue(css.contains(selector), "Debe cubrir familia visual especializada: " + selector);
        }
        assertFalse(css.contains("diagram-conceptual"), "El canvas conceptual queda protegido fuera de esta tanda.");
        assertFalse(css.contains("DiagramCanvasView"), "No debe tocar implementación conceptual desde CSS.");
    }
}
