package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/**
 * Guardarraíl fuente de la Tanda 64: las acciones de capas deben redibujar el
 * canvas inmediatamente y el clic simple debe mostrar selección local fuerte.
 */
final class Tanda64LayerOrderRefreshAndImmediateSelectionSourceTest {

    private static final String[] VISUAL_VIEW_MODELS = {
            "architecture/ArchitectureDiagramViewModel.java",
            "behavior/BehaviorDiagramViewModel.java",
            "freegraph/FreeGraphViewModelCore.java",
            "logicalbusinessgraph/LogicalBusinessGraphViewModel.java",
            "modulemap/ModuleMapViewModel.java",
            "screenflow/ScreenFlowViewModel.java",
            "umlclass/UmlClassDiagramViewModel.java",
            "wireframe/WireframeViewModel.java"
    };

    private static final String[] VISUAL_EDITOR_VIEWS = {
            "architecture/ArchitectureDiagramEditorView.java",
            "behavior/BehaviorDiagramEditorView.java",
            "freegraph/FreeGraphEditorView.java",
            "logicalbusinessgraph/LogicalBusinessGraphEditorView.java",
            "modulemap/ModuleMapEditorView.java",
            "screenflow/ScreenFlowEditorView.java",
            "umlclass/UmlClassDiagramEditorView.java",
            "wireframe/WireframeEditorView.java"
    };

    @Test
    void layerOrderCommandsRefreshCanvasWithoutChangingViewport() throws IOException {
        String actions = readMain("VisualDiagramViewActions.java");
        assertTrue(actions.contains("registerDiagramRefreshAction"),
                "Las vistas visuales deben registrar un refresh explícito del canvas.");
        assertTrue(actions.contains("refreshDiagramView()"),
                "El ViewModel debe poder pedir redibujado sin fit/center ni cambio de viewport.");
        for (String viewModel : VISUAL_VIEW_MODELS) {
            String source = Files.readString(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation", viewModel));
            assertTrue(source.contains("viewActions.refreshDiagramView()"),
                    viewModel + " debe refrescar el canvas después de modificar zOrder.");
        }
        for (String editor : VISUAL_EDITOR_VIEWS) {
            String source = Files.readString(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation", editor));
            assertTrue(source.contains("viewModel.registerDiagramRefreshAction(contributor::refreshCanvas)"),
                    editor + " debe conectar el refresh transversal con su centro de canvas.");
        }
    }

    @Test
    void clickSelectionDecoratesShapeTargetsImmediately() throws IOException {
        String registry = readMain("CanvasNodeVisualRegistry.java");
        String css = Files.readString(Path.of("src/main/resources/css/interactive-canvas.css"));

        assertTrue(registry.contains("canvas-node-view-selected-target"),
                "El registro visual debe marcar figuras internas para selección inmediata.");
        assertTrue(registry.contains("addSelectionTargetStyle"),
                "El feedback no debe depender de reconstruir el canvas completo.");
        assertTrue(css.contains(".canvas-node-view-selected-target"),
                "La hoja de estilo debe declarar el aspecto de selección sobre figuras internas.");
        assertTrue(css.contains(".interactive-canvas-node-active-selection-target"),
                "El estado activo debe poder blanquear la figura interna real de la tarjeta.");
    }

    private static String readMain(String fileName) throws IOException {
        return Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas",
                fileName
        ));
    }
}
