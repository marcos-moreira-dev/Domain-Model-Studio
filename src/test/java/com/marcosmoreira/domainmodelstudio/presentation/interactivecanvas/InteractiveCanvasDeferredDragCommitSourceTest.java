package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class InteractiveCanvasDeferredDragCommitSourceTest {

    private static final Path NODE_INTERACTION = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasNodeInteractionCoordinator.java"
    );
    private static final Path NODE_REGISTRY = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasNodeVisualRegistry.java"
    );
    private static final Path UML_CENTER = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassDiagramCenter.java"
    );

    @Test
    void dragShouldUpdateLayoutInRealTimeAndCleanReleaseWithoutDoubleCommit() throws IOException {
        String nodeInteraction = Files.readString(NODE_INTERACTION, StandardCharsets.UTF_8);
        String nodeRegistry = Files.readString(NODE_REGISTRY, StandardCharsets.UTF_8);

        assertTrue(nodeRegistry.contains("previewNodeDrag"),
                "El registro conserva el soporte de preview JavaFX para compatibilidad con gestos futuros.");
        assertTrue(nodeInteraction.contains("nodeDragController.dragTo(canvasPoint.getX(), canvasPoint.getY())"),
                "El layout persistente debe actualizarse en tiempo real durante MOUSE_DRAGGED.");
        assertTrue(nodeInteraction.contains("El layout ya fue actualizado incrementalmente durante MOUSE_DRAGGED"),
                "Al soltar el mouse no se debe aplicar el delta una segunda vez.");
        assertFalse(nodeInteraction.contains("commitNodeMove(node.id(), state.dragLayoutAtStart, state.dragTotal);"),
                "El release no debe confirmar otra vez un movimiento ya aplicado en tiempo real.");
    }

    @Test
    void umlSelectionShouldNotForceCanvasRefreshWhileMouseGestureIsActive() throws IOException {
        String source = Files.readString(UML_CENTER, StandardCharsets.UTF_8);

        assertFalse(source.contains("selectedClassProperty().addListener((observable, previous, current) -> requestCanvasRefresh())"),
                "Seleccionar una clase desde el canvas no debe reconstruir la vista en medio del clic/arrastre.");
        assertFalse(source.contains("selectedModuleProperty().addListener((observable, previous, current) -> requestCanvasRefresh())"),
                "Seleccionar un módulo desde el canvas no debe reconstruir la vista en medio del clic/arrastre.");
    }
}
