package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class UmlClassCanvasInteractionTanda3SourceTest {

    @Test
    void canonicalSurfaceShouldGiveImmediateSelectionFeedbackAndRefreshOnClickRelease() throws Exception {
        String registry = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasNodeVisualRegistry.java"),
                StandardCharsets.UTF_8);
        String interaction = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasNodeInteractionCoordinator.java"),
                StandardCharsets.UTF_8);
        String css = Files.readString(Path.of("src/main/resources/css/interactive-canvas.css"), StandardCharsets.UTF_8);

        assertTrue(registry.contains("setLocalSelectionStyle"),
                "El clic sobre una clase UML debe marcar selección local inmediatamente sin esperar otra operación.");
        assertTrue(registry.contains("interactive-canvas-node-pressed-selection"),
                "La selección local necesita una clase CSS visible para que el usuario note el clic.");
        assertTrue(interaction.contains("adapter.selectNode(node.id(), event.isShiftDown());"),
                "El clic directo debe enfocar semánticamente la clase o módulo seleccionado.");
        assertTrue(interaction.contains("visualRegistry.resetDragPreviewTranslations(node.id(), rendered);"),
                "Al soltar el mouse se limpian previsualizaciones sin obligar un refresh que produzca parpadeo.");
        assertTrue(css.contains(".interactive-canvas-node-pressed-selection"),
                "La clase de selección inmediata debe tener estilo visible en el CSS compartido.");
    }

    @Test
    void umlPropertiesShouldExposeOpenSourceAndProgramChooserNearSelectedClass() throws Exception {
        String panel = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassPropertiesPanel.java"),
                StandardCharsets.UTF_8);
        String viewModel = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassDiagramViewModel.java"),
                StandardCharsets.UTF_8);

        assertTrue(panel.contains("Abrir código"),
                "El inspector de clase debe permitir abrir el código desde la clase seleccionada.");
        assertTrue(panel.contains("Elegir programa..."),
                "Debe existir un camino rápido para pedir al sistema con qué programa abrir el archivo.");
        assertTrue(panel.contains("openSelectedSourceWithProgramChooser"),
                "El botón de selector de programa debe usar un comando dedicado del ViewModel.");
        assertTrue(viewModel.contains("openSelectedSourceWithProgramChooser"),
                "El ViewModel debe ofrecer apertura puntual con selector de programa sin obligar a configurar primero.");
        assertTrue(viewModel.contains("WINDOWS_OPEN_WITH_COMMAND"),
                "La apertura puntual debe reutilizar el soporte existente del diálogo Abrir con de Windows.");
    }

    @Test
    void umlDragPreviewShouldAvoidDuplicatingModuleChildren() throws Exception {
        String adapter = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassCanvasAdapter.java"),
                StandardCharsets.UTF_8);

        assertTrue(adapter.contains("previewNodeIdsForDraggedNode"),
                "El adapter UML debe controlar qué nodos se previsualizan al arrastrar módulos.");
        int first = adapter.indexOf("addClassIdsForModule(result, moduleId);");
        int second = adapter.indexOf("addClassIdsForModule(result, moduleId);", first + 1);
        assertTrue(first >= 0 && second < 0,
                "Cada módulo seleccionado debe agregar sus clases visibles una sola vez a la previsualización.");
    }
}
