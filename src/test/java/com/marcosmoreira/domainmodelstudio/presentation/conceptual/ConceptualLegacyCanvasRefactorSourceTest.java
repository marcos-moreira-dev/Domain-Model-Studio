package com.marcosmoreira.domainmodelstudio.presentation.conceptual;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class ConceptualLegacyCanvasRefactorSourceTest {

    private static final Path CANVAS_VM = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/canvas/DiagramCanvasViewModel.java");
    private static final Path EDIT_HISTORY = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/canvas/ConceptualCanvasEditHistory.java");
    private static final Path ANCHOR_RESOLVER = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/canvas/ConceptualAnchorResolver.java");

    @Test
    void viewModelDelegatesUndoRedoStacksToDedicatedHistorySupport() throws IOException {
        String viewModel = Files.readString(CANVAS_VM);
        String history = Files.readString(EDIT_HISTORY);

        assertTrue(viewModel.contains("ConceptualCanvasEditHistory editHistory"));
        assertTrue(viewModel.contains("beginUndoableEdit()"));
        assertFalse(viewModel.contains("Deque<DiagramProject> undoStack"));
        assertFalse(viewModel.contains("Deque<DiagramProject> redoStack"));
        assertTrue(history.contains("prepareEdit(DiagramProject currentProject)"));
        assertTrue(history.contains("undo(DiagramProject currentProject)"));
        assertTrue(history.contains("redo(DiagramProject currentProject)"));
    }

    @Test
    void viewModelDelegatesEndpointAnchorMathToResolver() throws IOException {
        String viewModel = Files.readString(CANVAS_VM);
        String resolver = Files.readString(ANCHOR_RESOLVER);

        assertTrue(viewModel.contains("ConceptualAnchorResolver.nearestAnchor"));
        assertFalse(viewModel.contains("private AnchorSide nearestAnchor"));
        assertFalse(viewModel.contains("private double squaredDistance"));
        assertTrue(resolver.contains("static AnchorSide nearestAnchor"));
        assertTrue(resolver.contains("squaredDistance"));
    }

    @Test
    void refactorDoesNotForceCanvasLegacyIntoGenericInteractiveCanvas() throws IOException {
        String viewModel = Files.readString(CANVAS_VM);

        assertFalse(viewModel.contains("extends InteractiveCanvas"));
        assertFalse(viewModel.contains("InteractiveCanvasSurfaceView"));
        assertTrue(viewModel.contains("ConceptualCanvasEditHistory"));
        assertTrue(viewModel.contains("ConceptualAnchorResolver"));
    }
}
