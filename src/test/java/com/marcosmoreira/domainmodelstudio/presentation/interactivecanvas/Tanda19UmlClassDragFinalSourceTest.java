package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíles finales para el drag real de UML Clases post Tanda 18. */
class Tanda19UmlClassDragFinalSourceTest {

    private static final Path NODE_COORDINATOR = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasNodeInteractionCoordinator.java");
    private static final Path SURFACE_VIEW = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/InteractiveCanvasSurfaceView.java");
    private static final Path NODE_FACTORY = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasNodeViewFactory.java");

    @Test
    void nodeDragUpdatesLayoutInRealTimeAndDoesNotDoubleCommitOnRelease() throws IOException {
        String source = read(NODE_COORDINATOR);

        assertTrue(source.contains("nodeDragController.dragTo(canvasPoint.getX(), canvasPoint.getY())"),
                "El movimiento persistente debe actualizarse en tiempo real durante el drag.");
        assertTrue(source.contains("El layout ya fue actualizado incrementalmente durante MOUSE_DRAGGED"),
                "El release debe limpiar el gesto sin aplicar el movimiento una segunda vez.");
        assertTrue(source.contains("Clic sin movimiento: no reconstruimos el canvas"),
                "El clic simple debe conservar la selección sin refresh completo ni parpadeo.");
    }

    @Test
    void relationLayerDoesNotBlockUmlClassNodeDragging() throws IOException {
        String source = read(SURFACE_VIEW);

        assertTrue(source.contains("configureConnectorLayerHitTesting()"),
                "La capa de conectores debe configurarse según el adapter activo.");
        assertTrue(source.contains("connectorLayer().setMouseTransparent(!connectorLineHitTestingEnabled())"),
                "Cuando UML dibuja relaciones encima, la capa debe ser transparente al mouse.");
    }

    @Test
    void transparentHitboxIsExplicitlyPickableForModulesAndClasses() throws IOException {
        String source = read(NODE_FACTORY);

        assertTrue(source.contains("hitBox.setFill(Color.TRANSPARENT)"),
                "El hitbox transparente debe existir también antes de aplicar CSS.");
        assertTrue(source.contains("hitBox.setMouseTransparent(false)"),
                "El hitbox debe participar en picking para permitir seleccionar y arrastrar.");
    }

    private static String read(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
