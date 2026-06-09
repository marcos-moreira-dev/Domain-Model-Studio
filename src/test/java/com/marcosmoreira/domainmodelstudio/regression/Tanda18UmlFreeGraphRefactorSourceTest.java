package com.marcosmoreira.domainmodelstudio.regression;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl fuente para la tanda 18 de refactor UML Clases / Grafo libre. */
class Tanda18UmlFreeGraphRefactorSourceTest {

    private static final Path UML_VM = Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassDiagramViewModel.java");
    private static final Path UML_LAYOUT = Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassDiagramLayoutCoordinator.java");
    private static final Path UML_SOURCE_NAV = Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassSourceNavigationController.java");
    private static final Path UML_EDITING = Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassEditingController.java");
    private static final Path FREE_GRAPH_ADAPTER = Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/freegraph/FreeGraphCanvasAdapter.java");
    private static final Path FREE_GRAPH_PREVIEW = Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/freegraph/FreeGraphLivePreviewController.java");

    @Test
    void umlClassViewModelDelegatesHeavyResponsibilities() throws IOException {
        String viewModel = Files.readString(UML_VM, StandardCharsets.UTF_8);
        assertTrue(Files.readAllLines(UML_VM, StandardCharsets.UTF_8).size() <= 650,
                "UML Clases debe salir de la deuda fuerte de archivos mayores a 650 líneas.");
        assertTrue(viewModel.contains("UmlClassDiagramLayoutCoordinator"));
        assertTrue(viewModel.contains("UmlClassSourceNavigationController"));
        assertTrue(viewModel.contains("UmlClassEditingController"));
        assertFalse(viewModel.contains("new VisualLayoutService"),
                "El ViewModel no debe construir directamente el servicio de layout UML.");
        assertFalse(viewModel.contains("CodeEditorLauncher.configured"),
                "La apertura de código debe vivir fuera del ViewModel principal.");
    }

    @Test
    void umlClassSpecializedCoordinatorsOwnTheirOwnConcern() throws IOException {
        String layout = Files.readString(UML_LAYOUT, StandardCharsets.UTF_8);
        String sourceNavigation = Files.readString(UML_SOURCE_NAV, StandardCharsets.UTF_8);
        String editing = Files.readString(UML_EDITING, StandardCharsets.UTF_8);
        assertTrue(layout.contains("VisualLayoutService"));
        assertTrue(layout.contains("handleCanvasRenderFailure"));
        assertTrue(sourceNavigation.contains("CodeEditorLauncher"));
        assertTrue(sourceNavigation.contains("UmlClassSourceFileResolver"));
        assertTrue(editing.contains("AddUmlClassUseCase"));
        assertTrue(editing.contains("ValidateUmlClassDiagramUseCase"));
    }

    @Test
    void freeGraphAdapterDelegatesLivePreviewState() throws IOException {
        String adapter = Files.readString(FREE_GRAPH_ADAPTER, StandardCharsets.UTF_8);
        String preview = Files.readString(FREE_GRAPH_PREVIEW, StandardCharsets.UTF_8);
        assertTrue(adapter.contains("FreeGraphLivePreviewController"));
        assertFalse(adapter.contains("livePreviewActive"));
        assertTrue(preview.contains("movableNodeIds"));
        assertTrue(preview.contains("PreviewDeltaConsumer"));
    }
}
