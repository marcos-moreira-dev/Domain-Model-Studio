package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl Tanda 49: un bendpoint recién creado queda seleccionado y listo para arrastrar. */
class Tanda49BendPointCreationSelectionSourceTest {

    @Test
    void adaptersShouldSelectTheBendPointImmediatelyAfterCreatingIt() throws Exception {
        String inMemory = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/InMemoryInteractiveCanvasAdapter.java");
        String moduleMap = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/modulemap/ModuleMapCanvasAdapter.java");
        String architecture = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/architecture/ArchitectureCanvasAdapter.java");

        assertTrue(inMemory.contains("selection = selection.withBendPoint(normalized, bendPointIndexAt(updated, point));"),
                "El adaptador de prueba debe seleccionar el punto creado para poder arrastrarlo de inmediato.");
        assertTrue(moduleMap.contains("bendPointSupport.markEditedBendPoint(normalized, index)"),
                "Mapa de módulos debe marcar como seleccionado el bendpoint creado.");
        assertTrue(architecture.contains("interactionState.selectBendPoint(normalized, index)"),
                "Arquitectura/despliegue debe seleccionar el bendpoint creado.");
    }

    @Test
    void selectedBendPointShouldHaveClearVisualFeedbackAndLiveRouteUpdate() throws Exception {
        String renderer = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasBendPointHandleRenderer.java");
        String css = read("src/main/resources/css/interactive-canvas-selection-overrides.css");
        String registry = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasConnectorVisualRegistry.java");

        assertTrue(renderer.contains("interactive-canvas-bend-point-handle-selected"));
        assertTrue(css.contains("#ffffff"), "El punto seleccionado debe verse con una tonalidad clara.");
        assertTrue(registry.contains("updateLiveRoute"),
                "La ruta del conector debe poder actualizarse en sitio durante el drag del bendpoint.");
    }

    private static String read(String path) throws Exception {
        return Files.readString(Path.of(path), StandardCharsets.UTF_8);
    }
}
