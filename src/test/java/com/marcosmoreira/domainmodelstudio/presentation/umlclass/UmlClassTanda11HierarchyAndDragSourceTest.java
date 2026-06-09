package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Regresiones fuente de la Tanda 11 para jerarquía UML y arrastre sobre clases. */
class UmlClassTanda11HierarchyAndDragSourceTest {

    @Test
    void containerModulesExposeFullHitboxButStayBehindInternalClasses() throws IOException {
        String factory = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasNodeViewFactory.java");

        assertTrue(factory.contains("root.setPickOnBounds(true)"),
                "Los contenedores UML deben poder seleccionarse y moverse desde su área visible.");
        assertTrue(factory.contains("Rectangle hitBox = new Rectangle(bounds.width(), bounds.height())"),
                "El hitbox del contenedor debe cubrir el módulo completo, no solo el encabezado.");
        assertTrue(factory.contains("content.setMouseTransparent(containerLike)"),
                "El contenido del contenedor debe ser transparente al mouse para no bloquear clases internas.");
        assertTrue(factory.contains("return 10.0;"),
                "Los contenedores deben quedar detrás de las clases internas por viewOrder.");
    }

    @Test
    void draggingFallsBackToThePressedNodeWhenSemanticSelectionIsNotReady() throws IOException {
        String surface = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasNodeInteractionCoordinator.java");

        assertTrue(surface.contains("moveDraggedNodeOrSelection"),
                "El canvas debe tener fallback directo para mover la clase presionada.");
        assertTrue(surface.contains("adapter.moveNode(draggedNodeId, layoutAtStart.x() + deltaX, layoutAtStart.y() + deltaY)"),
                "Si la selección semántica aún no está lista, se debe mover el nodo presionado.");
    }

    @Test
    void specializedSvgKeepsUmlArrowheadsVisibleAtNodeBorders() throws IOException {
        String writer = read("src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/svg/specialized/SpecializedVisualSvgWriter.java");
        String factory = read("src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/svg/specialized/SpecializedSvgModelFactory.java");

        assertTrue(writer.contains("uml-inheritance"), "SVG debe distinguir herencia/implementación con triángulo hueco.");
        assertTrue(writer.contains("uml-composition"), "SVG debe distinguir composición con diamante relleno.");
        assertTrue(writer.contains("uml-aggregation"), "SVG debe distinguir agregación con diamante hueco.");
        assertTrue(writer.contains("edgePoint(source"),
                "Las líneas SVG deben terminar en el borde del nodo para que la punta no quede escondida debajo de la clase.");
        assertTrue(factory.contains("connector-uml connector-uml-"),
                "La fábrica SVG debe propagar el tipo de relación UML como clase CSS específica.");
    }

    private static String read(String path) throws IOException {
        return Files.readString(Path.of(path), StandardCharsets.UTF_8);
    }
}
