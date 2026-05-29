package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

final class CanvasLiveDragInteractionSourceTest {

    @Test
    void nodeDragUpdatesLayoutDuringMouseDraggedAndDoesNotDoubleCommitOnRelease() throws Exception {
        String source = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasNodeInteractionCoordinator.java"));

        assertTrue(source.contains("nodeDragController.dragTo(canvasPoint.getX(), canvasPoint.getY())"));
        assertTrue(source.contains("El layout ya fue actualizado incrementalmente durante MOUSE_DRAGGED"));
        assertFalse(source.contains("commitNodeMove(node.id(), state.dragLayoutAtStart, state.dragTotal);"));
    }
}
