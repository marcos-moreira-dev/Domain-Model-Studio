package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl fuente de Tanda 66: selección activa legible y sizing textual transversal. */
final class Tanda66ReadableActiveSelectionSourceTest {

    @Test
    void activeSelectionShouldUseAccentInsteadOfBlackOverride() throws IOException {
        String css = Files.readString(Path.of("src/main/resources/css/interactive-canvas.css"));

        assertTrue(css.contains("Tanda 66"), "La intención visual de selección activa debe quedar documentada.");
        assertTrue(css.contains("-fx-border-color: -dms-accent") || css.contains("-fx-stroke: -dms-accent"),
                "La selección activa debe usar el acento del producto, no un negro pesado.");
        assertFalse(css.contains("-fx-stroke: #0f172a"),
                "El feedback activo no debe ennegrecer contornos de tarjetas/rombos.");
        assertFalse(css.contains("-fx-border-color: #0f172a"),
                "El feedback activo no debe ennegrecer bordes de tarjetas JavaFX.");
        assertFalse(css.contains(".interactive-canvas-node-active-selection .diagram-node-title"),
                "El feedback activo no debe forzar texto negro pesado en títulos internos.");
    }

    @Test
    void visualTextSizingPolicyShouldBeSharedByVisualFamilies() throws IOException {
        String policy = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/application/visual/VisualTextFitPolicy.java"));
        String freeGraph = readVisual("FreeGraphLayoutPolicy.java");
        String logicalGraph = readVisual("LogicalBusinessGraphLayoutPolicy.java");
        String architecture = readVisual("ArchitectureLayoutPolicy.java");
        String c4 = readVisual("C4ArchitectureAutoLayoutPolicy.java");
        String behavior = readVisual("UmlBehaviorLayoutPolicy.java");

        assertTrue(policy.contains("fitCard"));
        assertTrue(policy.contains("fitDiamond"));
        assertTrue(freeGraph.contains("textFitPolicy.fitCard"));
        assertTrue(logicalGraph.contains("textFitPolicy.fitLargeCard"));
        assertTrue(architecture.contains("textFitPolicy.fitCard"));
        assertTrue(c4.contains("TEXT_FIT.fitCard"));
        assertTrue(behavior.contains("textFitPolicy.fitDiamond"),
                "Los rombos de decisión también deben beneficiarse del sizing transversal.");
    }

    private static String readVisual(String name) throws IOException {
        return Files.readString(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/application/visual", name));
    }
}
