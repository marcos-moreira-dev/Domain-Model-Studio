package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Regresión fuente: las etiquetas de conectores migrados viven en la capa común del canvas. */
class ConnectorLabelCentralizationSourceTest {

    @Test
    void migratedGraphRenderKitsMustNotDrawEmbeddedConnectorLabels() throws IOException {
        List<Path> renderKits = List.of(
                Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/modulemap/ModuleMapRenderKit.java"),
                Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/screenflow/ScreenFlowRenderKit.java"),
                Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassRenderKit.java"),
                Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/behavior/BehaviorRenderKit.java"),
                Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/architecture/ArchitectureRenderKit.java"),
                Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/freegraph/FreeGraphRenderKit.java")
        );
        for (Path renderKit : renderKits) {
            String source = read(renderKit);
            assertFalse(source.contains("polyline(points, connector.label(), style)"),
                    renderKit + " no debe duplicar etiquetas dentro del conector.");
            assertTrue(source.contains("polyline(points, \"\", style)"),
                    renderKit + " debe delegar la etiqueta al overlay común.");
        }
    }

    @Test
    void pngExporterDrawsCommonLabelsAfterNodes() throws IOException {
        String source = read(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/InteractiveCanvasPngExporter.java"));
        int connectors = source.indexOf("renderConnectors(model)");
        int nodes = source.indexOf("renderedNodes.foregroundNodes()");
        int labels = source.indexOf("renderConnectorLabels(model)");
        assertTrue(connectors >= 0 && nodes > connectors && labels > nodes,
                "PNG debe dibujar conectores, luego nodos y finalmente etiquetas overlay.");
        assertTrue(source.contains("CanvasConnectorLabelNodeFactory.createExportLabel"),
                "PNG debe usar la fábrica común de etiquetas.");
    }

    @Test
    void sequenceKeepsSpecializedMessageLabelsOutsideCommonOverlay() throws IOException {
        String profile = read(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/profile/DiagramInteractionProfile.java"));
        String labelRenderer = read(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasConnectorLabelOverlayRenderer.java"));
        assertTrue(profile.contains("supportsCommonConnectorLabelOverlay"),
                "El perfil debe distinguir etiquetas comunes de etiquetas temporales de secuencia.");
        assertTrue(labelRenderer.contains("supportsCommonConnectorLabelOverlay"),
                "El renderer de etiquetas no debe dibujar overlay común cuando el perfil lo bloquea.");
    }

    @Test
    void specializedSvgLabelsUseDedicatedLayerAndRouteLengthMidpoint() throws IOException {
        String writer = read(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/svg/specialized/SpecializedVisualSvgWriter.java"));
        assertTrue(writer.contains("id=\\\"connector-labels\\\""),
                "SVG especializado debe separar etiquetas en una capa posterior a nodos.");
        String geometry = read(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/svg/specialized/SpecializedSvgGeometry.java"));
        assertTrue(geometry.contains("double totalLength") && geometry.contains("targetLength = totalLength / 2.0"),
                "SVG especializado debe ubicar etiquetas por mitad real de la ruta, no por índice de punto.");
    }

    private static String read(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
