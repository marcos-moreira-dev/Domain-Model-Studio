package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl Tanda 51: bendpoints visibles/seleccionables y relaciones recogidas por selección rectangular. */
class Tanda51BendPointAndAreaSelectionSourceTest {

    @Test
    void selectedBendPointUsesClearWhiteFeedback() throws Exception {
        String css = read("src/main/resources/css/interactive-canvas-selection-overrides.css");
        String renderer = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasBendPointHandleRenderer.java");

        assertTrue(css.contains(".interactive-canvas-bend-point-handle-selected"));
        assertTrue(css.contains("#ffffff"), "El bendpoint seleccionado debe verse claramente blanco/claro.");
        assertTrue(renderer.contains("bendPointController.select(connectorId, index)"),
                "Un clic sobre el bendpoint debe activar inmediatamente su selección semántica.");
    }

    @Test
    void areaSelectionShouldUseRenderedConnectorRouteNotOnlyEndpointNodes() throws Exception {
        String policy = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasConnectorSelectionHitPolicy.java");
        String moduleMap = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/modulemap/ModuleMapCanvasAdapter.java");
        String architecture = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/architecture/ArchitectureCanvasAdapter.java");

        assertTrue(policy.contains("CanvasConnectorGeometry.edgeToEdgePoints"),
                "La detección debe reutilizar la misma ruta borde-a-borde del render.");
        assertTrue(moduleMap.contains("connectorRouteTouches(connectorId, source, target, selectionBounds)"),
                "Mapa de módulos debe seleccionar relaciones cuando el rectángulo toca la flecha o sus codos.");
        assertTrue(architecture.contains("connectorRouteTouches(connectorId, source, target, selectionBounds)"),
                "Arquitectura/despliegue debe seleccionar relaciones cuando el rectángulo toca la flecha o sus codos.");
    }

    private static String read(String path) throws Exception {
        return Files.readString(Path.of(path), StandardCharsets.UTF_8);
    }
}
