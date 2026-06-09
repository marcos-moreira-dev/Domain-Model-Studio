package com.marcosmoreira.domainmodelstudio.presentation.conceptual;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíles de Tanda 13B: limpieza visual conceptual y flechas claras de historial. */
class ConceptualVisualCleanupSourceTest {

    private static final Path DIAGRAM_CANVAS_VIEW = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/canvas/DiagramCanvasView.java");
    private static final Path CONTRIBUTOR = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/conceptual/ConceptualWorkbenchContributor.java");
    private static final Path UNDO_ICON = Path.of("src/main/resources/icons/undo.png");
    private static final Path REDO_ICON = Path.of("src/main/resources/icons/redo.png");

    @Test
    void conceptualWorkbenchDisablesTheLegacyInnerCanvasHeader() throws IOException {
        String canvas = Files.readString(DIAGRAM_CANVAS_VIEW, StandardCharsets.UTF_8);
        String contributor = Files.readString(CONTRIBUTOR, StandardCharsets.UTF_8);

        assertTrue(canvas.contains("public DiagramCanvasView(DiagramCanvasViewModel viewModel, boolean workspaceHeaderEnabled)"));
        assertTrue(canvas.contains("if (!workspaceHeaderEnabled || workspaceHeader == null)"));
        assertTrue(contributor.contains("new DiagramCanvasView(canvasViewModel, false)"),
                "El modelo conceptual integrado ya tiene cabecera de workbench; no debe mostrar el panel informativo interno del canvas legacy.");
        assertFalse(contributor.contains("new DiagramCanvasView(canvasViewModel);"));
    }

    @Test
    void undoRedoIconsAreConcretePngArrowResources() throws IOException {
        byte[] pngMagic = new byte[] {(byte) 0x89, 0x50, 0x4E, 0x47};
        byte[] undo = Files.readAllBytes(UNDO_ICON);
        byte[] redo = Files.readAllBytes(REDO_ICON);

        assertTrue(undo.length > 250, "undo.png debe ser un recurso gráfico real y no un fallback vacío.");
        assertTrue(redo.length > 250, "redo.png debe ser un recurso gráfico real y no un fallback vacío.");
        assertArrayEquals(pngMagic, new byte[] {undo[0], undo[1], undo[2], undo[3]});
        assertArrayEquals(pngMagic, new byte[] {redo[0], redo[1], redo[2], redo[3]});
    }
}
