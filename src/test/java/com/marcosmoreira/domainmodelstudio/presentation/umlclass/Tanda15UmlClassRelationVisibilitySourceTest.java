package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl Tanda 15: las categorías de relación UML deben ser visibles en runtime. */
class Tanda15UmlClassRelationVisibilitySourceTest {

    @Test
    void renderKitMustMapRelationKindsToArrowCategories() throws IOException {
        String source = Files.readString(
                Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassRenderKit.java"),
                StandardCharsets.UTF_8);

        assertTrue(source.contains("uml-relation-inheritance") && source.contains("DiagramArrowKind.HOLLOW_TRIANGLE"));
        assertTrue(source.contains("uml-relation-implementation") && source.contains("DiagramArrowKind.HOLLOW_TRIANGLE"));
        assertTrue(source.contains("uml-relation-composition") && source.contains("DiagramArrowKind.FILLED_DIAMOND"));
        assertTrue(source.contains("uml-relation-aggregation") && source.contains("DiagramArrowKind.HOLLOW_DIAMOND"));
        assertTrue(source.contains("uml-relation-dependency") && source.contains("DiagramArrowKind.OPEN"));
    }

    @Test
    void cssMustKeepRelationsVisibleInsideTransparentModules() throws IOException {
        String css = Files.readString(Path.of("src/main/resources/css/uml-class.css"), StandardCharsets.UTF_8);

        assertTrue(css.contains("Tanda 15 — relaciones UML visibles dentro de módulos/contenedores"));
        assertTrue(css.contains(".uml-class-canvas-module") && css.contains("rgba(255, 255, 255, 0.16)"));
        assertTrue(css.contains(".uml-class-canvas-connector-uml-relation-composition"));
        assertTrue(css.contains(".uml-class-canvas-connector-uml-relation-aggregation"));
        assertTrue(css.contains(".uml-class-canvas-arrow-head.diagram-connector-arrow-hollow"));
        assertTrue(css.contains(".interactive-canvas-connector-label-uml-relation-composition"));
        assertTrue(css.contains("-fx-stroke-width: 3.0"));
    }
}
