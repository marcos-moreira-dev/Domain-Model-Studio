package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíles de la Tanda 17 para selección/arrastre real en UML Clases. */
class Tanda17UmlClassDragAndSelectionSourceTest {

    @Test
    void clickSelectionShouldPersistAfterMouseRelease() throws IOException {
        String source = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasNodeInteractionCoordinator.java");

        assertTrue(source.contains("visualRegistry.markSelectedNodeLocally"),
                "El clic simple en clase/módulo debe dejar feedback visual persistente desde el mouse press.");
        assertTrue(source.contains("Clic sin movimiento: no reconstruimos el canvas"),
                "Al soltar un clic sin movimiento no debe reconstruirse el canvas ni borrar la selección local.");
        assertTrue(source.contains("shouldPreserveSelectionGroup"),
                "El contrato debe conservar selección múltiple cuando se arrastra un nodo ya seleccionado.");
    }

    @Test
    void moduleDragShouldNotMoveInternalClassesTwice() throws IOException {
        String adapter = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassCanvasAdapter.java");
        String coordinator = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassDiagramLayoutCoordinator.java");

        assertTrue(coordinator.contains("movePreparedNodeByIfPresent(VisualElementLayoutIds.umlClass"),
                "El layout coordinator debe ser el único que traslada clases internas al mover un módulo UML.");
        assertTrue(adapter.contains("el coordinador de layout UML ya traslada las clases internas"),
                "El adapter debe documentar que no replica el movimiento de clases internas.");
        assertFalse(adapter.contains("moveVisibleClassesInsideModuleBy"),
                "El adapter no debe mover de nuevo las clases internas, porque duplica el delta del drag.");
    }

    private static String read(String path) throws IOException {
        return Files.readString(Path.of(path), StandardCharsets.UTF_8);
    }
}
