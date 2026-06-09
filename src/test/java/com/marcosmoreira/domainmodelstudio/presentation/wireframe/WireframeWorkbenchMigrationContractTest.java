package com.marcosmoreira.domainmodelstudio.presentation.wireframe;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class WireframeWorkbenchMigrationContractTest {

    private static final Path WIREFRAME_PACKAGE = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/wireframe"
    );

    @Test
    void wireframeEditorMustUseWorkbenchInsteadOfLocalSplitPane() throws IOException {
        String source = Files.readString(WIREFRAME_PACKAGE.resolve("WireframeEditorView.java"), StandardCharsets.UTF_8);

        assertTrue(source.contains("DiagramWorkbenchView"), "El editor debe ser wrapper del workbench común.");
        assertFalse(source.contains("InteractiveDiagramCanvasView"), "El editor no debe montar el canvas mínimo legado.");
        assertFalse(source.contains("new SplitPane"), "El editor no debe reconstruir un SplitPane propio.");
    }

    @Test
    void wireframeContributorMustExposeWorkbenchSlots() throws IOException {
        String source = Files.readString(
                WIREFRAME_PACKAGE.resolve("WireframeWorkbenchContributor.java"),
                StandardCharsets.UTF_8
        );

        assertTrue(source.contains("structurePanel()"), "Debe aportar slot izquierdo al workbench.");
        assertTrue(source.contains("propertiesPanel()"), "Debe aportar slot derecho al workbench.");
        assertTrue(source.contains("migratedVisualDiagram"), "Debe usar descriptor migrado, no legacy.");
    }

    @Test
    void wireframeMustUseCanonicalSurfaceBridge() throws IOException {
        String source = Files.readString(WIREFRAME_PACKAGE.resolve("WireframeDiagramCenter.java"), StandardCharsets.UTF_8);

        assertTrue(source.contains("ZoomableDiagramSurface"), "Debe usar la superficie zoomable canónica.");
        assertTrue(source.contains("InteractiveCanvasSurfaceView"), "Debe usar el puente normalizado adapter + surface.");
        assertFalse(source.contains("InteractiveDiagramCanvasView"), "No debe usar el canvas mínimo legado.");
    }

    @Test
    void wireframeCanvasMustRenderSimulatedFiguresNotRealControls() throws IOException {
        String renderKit = Files.readString(WIREFRAME_PACKAGE.resolve("WireframeRenderKit.java"), StandardCharsets.UTF_8);
        String figureFactory = Files.readString(
                WIREFRAME_PACKAGE.resolve("WireframeComponentFigureFactory.java"),
                StandardCharsets.UTF_8
        );

        assertTrue(renderKit.contains("WireframeComponentFigureFactory"), "Debe delegar figuras a una fábrica específica de wireframes.");
        assertFalse(figureFactory.contains("javafx.scene.control.Button"), "Un botón dentro del canvas debe ser figura simulada, no Button real.");
        assertFalse(figureFactory.contains("javafx.scene.control.TextField"), "Un campo dentro del canvas debe ser figura simulada, no TextField real.");
        assertTrue(figureFactory.contains("Rectangle"), "Las maquetas deben construirse con figuras vectoriales simples.");
        assertTrue(figureFactory.contains("Text"), "Las etiquetas del canvas deben ser textos de diagrama.");
    }
}
