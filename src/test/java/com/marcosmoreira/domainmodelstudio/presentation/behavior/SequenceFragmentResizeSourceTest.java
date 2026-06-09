package com.marcosmoreira.domainmodelstudio.presentation.behavior;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class SequenceFragmentResizeSourceTest {

    @Test
    void sequenceAdapterReusesCommonResizeHandlesOnlyForCombinedFragments() throws Exception {
        String source = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/behavior/SequenceCanvasAdapter.java"));
        String viewModel = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/behavior/BehaviorDiagramViewModel.java"));
        String manualLayout = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/behavior/BehaviorSequenceFragmentManualLayout.java"));

        assertTrue(source.contains("implements InteractiveCanvasAdapter, CanvasConnectorLabelPort, CanvasProjectStylePort, CanvasResizePort"));
        assertTrue(source.contains("public void resizeNode(String elementId, double width, double height)"));
        assertTrue(source.contains("node.kind() != BehaviorNodeKind.FRAGMENT"));
        assertTrue(source.contains("viewModel.resizeSequenceFragmentTo(nodeId, width, height)"));
        assertTrue(source.contains("public boolean supportsNodeResize(String elementId)"));
        assertTrue(viewModel.contains("resizeSequenceFragmentTo"));
        assertTrue(manualLayout.contains("Tamaño de fragmento UML Secuencia actualizado."));
    }

    @Test
    void sequenceFragmentsCanBeMovedManuallyAndSelectedInBlue() throws Exception {
        String adapter = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/behavior/SequenceCanvasAdapter.java"));
        String viewModel = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/behavior/BehaviorDiagramViewModel.java"));
        String manualLayout = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/behavior/BehaviorSequenceFragmentManualLayout.java"));
        String layoutPolicy = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/application/visual/SequenceTimelineLayoutPolicy.java"));
        String css = Files.readString(Path.of("src/main/resources/css/sequence-diagram.css"));
        String example = Files.readString(Path.of(
                "src/main/resources/ai-resources/official-markdown/diagramas/uml_sequence_registrar_calificacion_uens_gordito.md"));

        assertTrue(adapter.contains("viewModel.moveNodeTo(nodeId, Math.max(32.0, x), lockedY)"));
        assertTrue(viewModel.contains("BehaviorSequenceFragmentManualLayout.lockPosition"));
        assertTrue(manualLayout.contains("layout.withLocked(true)"));
        assertTrue(layoutPolicy.contains("base != null && base.locked() ? base.x() : automaticX"));
        assertTrue(layoutPolicy.contains("base != null && base.locked() ? base.y() : automaticY"));
        assertTrue(css.contains("#2563EB"));
        assertTrue(css.contains(".canvas-node-view-selected .sequence-fragment-box"));
        assertTrue(example.contains("Pantalla de calificaciones -> Pantalla de calificaciones"));
        assertTrue(example.contains("CalificacionCommandService -> CalificacionCommandService"));
    }
    @Test
    void selfMessagesShouldNotRenderAsFilledRectanglesWhenUnselected() throws Exception {
        String renderKit = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/behavior/SequenceRenderKit.java"));
        String css = Files.readString(Path.of("src/main/resources/css/sequence-diagram.css"));

        assertTrue(renderKit.contains("line.setStyle(\"-fx-fill: transparent;\")"));
        assertTrue(css.contains(".sequence-message-line {\n    -fx-stroke: #1F2937;\n    -fx-fill: transparent;"));
        assertTrue(css.contains(".sequence-message-self {\n    -fx-stroke-dash-array: 10 0;\n    -fx-fill: transparent;"));
    }

}
