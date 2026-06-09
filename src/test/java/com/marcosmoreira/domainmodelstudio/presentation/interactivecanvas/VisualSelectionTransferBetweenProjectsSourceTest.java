package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl fuente para la transferencia de selección entre proyectos abiertos compatibles. */
class VisualSelectionTransferBetweenProjectsSourceTest {

    private static final Path ROOT = Path.of("");

    @Test
    void toolbarShouldExposeTransferActionForSupportedVisualGraphs() throws IOException {
        String ids = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/toolbar/DiagramToolbarActionId.java");
        assertTrue(ids.contains("TRANSFER_VISUAL_SELECTION"));

        String factory = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/toolbar/DiagramToolbarActionFactory.java");
        assertTrue(factory.contains("transferSelectionAction"));
        assertTrue(factory.contains("Copia la selección visual a otro proyecto abierto compatible"));

        String free = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/toolbar/FreeGraphToolbarContributor.java");
        assertTrue(free.contains("transferSelectionAction()"));

        String logical = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/toolbar/LogicalBusinessGraphToolbarContributor.java");
        assertTrue(logical.contains("transferSelectionAction()"));
    }

    @Test
    void commandHandlerShouldChooseOnlyCompatibleOpenProjects() throws IOException {
        String handler = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellCommandHandler.java");
        assertTrue(handler.contains("requestTransferVisualSelection()"));
        assertTrue(handler.contains("visualSelectionTransferCoordinator.requestTransferVisualSelection()"));

        String coordinator = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/VisualSelectionTransferCoordinator.java");
        assertTrue(coordinator.contains("compatibleTransferTargets"));
        assertTrue(coordinator.contains("session != source"));
        assertTrue(coordinator.contains("diagramTypeId.equals(session.project.metadata().diagramTypeId())"));
        assertTrue(coordinator.contains("ChoiceDialog<ProjectSession>"));
        assertTrue(coordinator.contains("No hay otro proyecto abierto compatible"));
    }

    @Test
    void transferShouldCopyCurrentRememberedCanvasSelectionBeforePasting() throws IOException {
        String freeVm = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/freegraph/FreeGraphViewModel.java");
        assertTrue(freeVm.contains("rememberCanvasSelection"));
        assertTrue(freeVm.contains("copyCurrentSelectionToClipboard"));

        String freeAdapter = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/freegraph/FreeGraphCanvasAdapter.java");
        assertTrue(freeAdapter.contains("rememberSelection()"));
        assertTrue(freeAdapter.contains("viewModel.rememberCanvasSelection(selectionSupport.current())"));

        String logicalVm = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/logicalbusinessgraph/LogicalBusinessGraphViewModel.java");
        assertTrue(logicalVm.contains("rememberCanvasSelection"));
        assertTrue(logicalVm.contains("copyCurrentSelectionToClipboard"));

        String logicalAdapter = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/logicalbusinessgraph/LogicalBusinessGraphCanvasAdapter.java");
        assertTrue(logicalAdapter.contains("rememberSelection()"));
        assertTrue(logicalAdapter.contains("viewModel.rememberCanvasSelection(selectionSupport.current())"));
    }

    @Test
    void documentationShouldDescribeLimitsAndManualSmoke() throws IOException {
        String doc = read("docs/desarrollo/TANDA_VIS_COPY_002_TRANSFERENCIA_ENTRE_PROYECTOS.md");
        assertTrue(doc.contains("Grafo libre"));
        assertTrue(doc.contains("Grafo lógico"));
        assertTrue(doc.contains("No se permite transferir entre tipos distintos"));
        assertTrue(doc.contains("proyecto destino"));
    }

    private static String read(String relativePath) throws IOException {
        return Files.readString(ROOT.resolve(relativePath), StandardCharsets.UTF_8);
    }
}
