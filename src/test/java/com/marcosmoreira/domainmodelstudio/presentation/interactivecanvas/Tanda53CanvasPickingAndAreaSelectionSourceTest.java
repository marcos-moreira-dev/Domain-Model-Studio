package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

class Tanda53CanvasPickingAndAreaSelectionSourceTest {

    private static String read(String path) throws IOException {
        return Files.readString(Path.of(path), StandardCharsets.UTF_8);
    }

    @Test
    void renderedCardsKeepFullBoundsPickingButHandleOnlyZonesRemainExplicit() throws IOException {
        String coordinator = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasNodeInteractionCoordinator.java");

        assertTrue(coordinator.contains("rendered.setPickOnBounds(!handleOnlyDrag(rendered))"),
                "Las tarjetas normales deben capturar toda su caja y los contenedores handle-only deben conservar su excepción por banda/título.");
        assertTrue(coordinator.contains("private static boolean handleOnlyDrag(Node rendered)"),
                "La excepción handle-only debe quedar centralizada y explícita en el coordinador.");
    }

    @Test
    void connectorLinesAreTransparentUnlessAnAdapterOptsIn() throws IOException {
        String surface = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/InteractiveCanvasSurfaceView.java");
        String port = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasConnectorHitTestPort.java");

        assertTrue(surface.contains("adapter instanceof CanvasConnectorHitTestPort hitTestPort"));
        assertTrue(surface.contains("&& hitTestPort.connectorLineHitTestingEnabled()"),
                "La línea de conector no debe bloquear tarjetas por defecto.");
        assertTrue(port.contains("return false"),
                "El puerto debe documentar opt-in explícito para líneas capturables.");
    }

    @Test
    void visualAdaptersWithConnectorsSupportMixedAreaSelection() throws IOException {
        List<String> adapters = List.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/behavior/BehaviorCanvasAdapter.java",
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/freegraph/FreeGraphCanvasAdapter.java",
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/logicalbusinessgraph/LogicalBusinessGraphCanvasAdapter.java",
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/screenflow/ScreenFlowCanvasAdapter.java",
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassCanvasAdapter.java"
        );
        for (String adapterPath : adapters) {
            String source = read(adapterPath);
            assertTrue(source.contains("public void selectElementsInside(CanvasBounds selectionBounds, boolean additive)"),
                    adapterPath + " debe seleccionar nodos y relaciones por rectángulo.");
            assertTrue(source.contains("selectNodesAndConnectors"),
                    adapterPath + " debe preservar selección mixta, no reemplazar nodos por relaciones.");
            assertTrue(source.contains("connectorRouteTouches"),
                    adapterPath + " debe recoger relaciones cuya ruta cruza el rectángulo.");
        }
    }
}
