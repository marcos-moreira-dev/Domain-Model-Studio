package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class Tanda36CanvasAdapterRefactorSourceTest {

    private static final Path MAIN = Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation");

    @Test
    void commonInteractionStateCentralizesSelectionDirtyAndBendPointMechanics() throws IOException {
        String state = Files.readString(MAIN.resolve("interactivecanvas/CanvasAdapterInteractionState.java"));

        assertTrue(state.contains("CanvasSelectionSupport"), "El estado común debe reutilizar CanvasSelectionSupport.");
        assertTrue(state.contains("CanvasDirtyState"), "El estado común debe reutilizar CanvasDirtyState.");
        assertTrue(state.contains("CanvasBendPointEditingSupport"), "El estado común debe reutilizar CanvasBendPointEditingSupport.");
        assertTrue(state.contains("syncSingleNode"), "Debe existir sincronización genérica de nodo seleccionado.");
        assertTrue(state.contains("syncSingleConnector"), "Debe existir sincronización genérica de conector seleccionado.");
    }

    @Test
    void specializedAdaptersUseCommonInteractionStateInsteadOfManualSelectionAndDirtyFields() throws IOException {
        for (String adapter : new String[] {
                "architecture/ArchitectureCanvasAdapter.java",
                "behavior/BehaviorCanvasAdapter.java",
                "behavior/SequenceCanvasAdapter.java",
                "screenflow/ScreenFlowCanvasAdapter.java",
                "umlclass/UmlClassCanvasAdapter.java",
                "wireframe/WireframeCanvasAdapter.java"
        }) {
            String source = Files.readString(MAIN.resolve(adapter));
            assertTrue(source.contains("CanvasAdapterInteractionState"), adapter + " debe usar el estado común de interacción.");
            assertFalse(source.contains("private InteractiveCanvasSelection selection ="), adapter + " no debe mantener selección manual propia.");
            assertFalse(source.contains("private boolean dirty"), adapter + " no debe mantener dirty manual propio.");
            assertFalse(source.contains("CanvasBendPointSelectionPolicy.selectBendPoint"), adapter + " debe delegar la mecánica de bendpoints.");
        }
    }

    @Test
    void conceptualCanvasRemainsProtectedAndOutsideCommonAdapterRefactor() throws IOException {
        String conceptualView = Files.readString(MAIN.resolve("canvas/DiagramCanvasView.java"));
        String conceptualViewModel = Files.readString(MAIN.resolve("canvas/DiagramCanvasViewModel.java"));

        assertFalse(conceptualView.contains("CanvasAdapterInteractionState"));
        assertFalse(conceptualViewModel.contains("CanvasAdapterInteractionState"));
    }
}
