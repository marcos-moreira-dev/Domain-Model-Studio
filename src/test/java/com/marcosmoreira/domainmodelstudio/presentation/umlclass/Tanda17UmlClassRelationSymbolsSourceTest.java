package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl para símbolos UML visibles: herencia, composición, agregación y dependencia. */
class Tanda17UmlClassRelationSymbolsSourceTest {

    @Test
    void relationKindsShouldMapToVisibleUmlArrowheads() throws IOException {
        String renderKit = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassRenderKit.java");
        String arrowFactory = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/drawing/DiagramArrowFactory.java");
        String css = read("src/main/resources/css/uml-class.css");

        assertTrue(renderKit.contains("DiagramArrowKind.HOLLOW_TRIANGLE"), "Herencia/implementación deben usar triángulo hueco.");
        assertTrue(renderKit.contains("DiagramArrowKind.FILLED_DIAMOND"), "Composición debe usar diamante relleno.");
        assertTrue(renderKit.contains("DiagramArrowKind.HOLLOW_DIAMOND"), "Agregación debe usar diamante hueco.");
        assertTrue(renderKit.contains("DiagramArrowKind.OPEN"), "Dependencia debe usar flecha abierta.");
        assertTrue(arrowFactory.contains("DEFAULT_LENGTH = 16.0") && arrowFactory.contains("DEFAULT_WIDTH = 11.0"),
                "Las puntas UML deben tener tamaño suficiente para ser visibles en pantalla.");
        assertTrue(css.contains(".uml-class-canvas-arrow-head.diagram-connector-arrow-hollow"),
                "Los símbolos huecos deben mantener fill claro y borde contrastado.");
    }

    private static String read(String path) throws IOException {
        return Files.readString(Path.of(path), StandardCharsets.UTF_8);
    }
}
