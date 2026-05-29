package com.marcosmoreira.domainmodelstudio.presentation.conceptual;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíles de Tanda 8: retiro de rutas legacy visibles del modelo conceptual. */
class ConceptualLegacyRetirementSourceTest {

    private static final Path MAIN_SHELL_VIEW = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellView.java");
    private static final Path MAIN_SHELL_COMMAND_HANDLER = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellCommandHandler.java");
    private static final Path CONTRIBUTOR = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/conceptual/ConceptualWorkbenchContributor.java");
    private static final Path LEGACY_BRIDGE = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/conceptual/ConceptualCanvasLegacyBridge.java");

    @Test
    void mainShellViewDoesNotExposeLegacyConceptualSidePanelControls() throws IOException {
        String shell = read(MAIN_SHELL_VIEW);

        assertFalse(shell.contains("Mostrar estructura conceptual"));
        assertFalse(shell.contains("Mostrar propiedades conceptuales"));
        assertFalse(shell.contains("modelPanelMenuItem"));
        assertFalse(shell.contains("inspectorPanelMenuItem"));
        assertFalse(shell.contains("panel-restore-tab"));
        assertFalse(shell.contains("setModelPanelVisible"));
        assertFalse(shell.contains("setInspectorPanelVisible"));
        assertTrue(shell.contains("new ConceptualEditorView"));
        assertTrue(shell.contains("workAreaFrame.setCenter(centerRoot)"));
    }

    @Test
    void mainShellCommandHandlerDoesNotLoadConceptualWorkspaceThroughLegacyFallback() throws IOException {
        String shell = read(MAIN_SHELL_COMMAND_HANDLER);
        String method = method(shell, "private void showProjectInEditor", "private void clearAllProjectViews");
        String clearMethod = method(shell, "private void clearAllProjectViews", "private void clearSpecializedEditors");

        assertTrue(method.contains("specializedWorkspaces.loadIfSpecialized(project)"));
        assertFalse(method.contains("modelTreeViewModel.loadProject(project)"));
        assertFalse(method.contains("canvasViewModel.showImportedProject(project)"));
        assertFalse(method.contains("inspectorViewModel.refreshFromSelection()"));
        assertFalse(clearMethod.contains("modelTreeViewModel.clearProject()"));
        assertFalse(clearMethod.contains("canvasViewModel.clearProject()"));
        assertTrue(clearMethod.contains("clearSpecializedEditors()"));
    }

    @Test
    void conceptualLegacyComponentsRemainEncapsulatedInsideContributorAndBridge() throws IOException {
        String contributor = read(CONTRIBUTOR);
        String bridge = read(LEGACY_BRIDGE);

        assertTrue(contributor.contains("new ModelTreeView"));
        assertTrue(contributor.contains("new InspectorView"));
        assertTrue(contributor.contains("new DiagramCanvasView"));
        assertTrue(bridge.contains("modelTreeViewModel.loadProject(project)"));
        assertTrue(bridge.contains("canvasViewModel.showImportedProject(project)"));
        assertFalse(contributor.contains("InteractiveCanvasSurfaceView"));
        assertFalse(bridge.contains("InteractiveCanvasSurfaceView"));
    }

    private static String read(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }

    private static String method(String source, String startToken, String endToken) {
        int start = source.indexOf(startToken);
        int end = source.indexOf(endToken, start + startToken.length());
        assertTrue(start >= 0, "No se encontró inicio: " + startToken);
        assertTrue(end > start, "No se encontró cierre: " + endToken);
        return source.substring(start, end);
    }
}
