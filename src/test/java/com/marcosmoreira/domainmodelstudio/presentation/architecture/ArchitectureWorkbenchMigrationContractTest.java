package com.marcosmoreira.domainmodelstudio.presentation.architecture;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class ArchitectureWorkbenchMigrationContractTest {

    private static final Path ARCHITECTURE_PACKAGE = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/architecture"
    );

    @Test
    void architectureEditorMustUseWorkbenchInsteadOfLocalSplitPane() throws IOException {
        String source = Files.readString(ARCHITECTURE_PACKAGE.resolve("ArchitectureDiagramEditorView.java"), StandardCharsets.UTF_8);

        assertTrue(source.contains("DiagramWorkbenchView"), "El editor debe ser wrapper del workbench común.");
        assertFalse(source.contains("InteractiveDiagramCanvasView"), "El editor no debe montar el canvas mínimo legado.");
        assertFalse(source.contains("new SplitPane"), "El editor no debe reconstruir un SplitPane propio.");
    }

    @Test
    void architectureContributorMustExposeWorkbenchSlots() throws IOException {
        String source = Files.readString(
                ARCHITECTURE_PACKAGE.resolve("ArchitectureWorkbenchContributor.java"),
                StandardCharsets.UTF_8
        );

        assertTrue(source.contains("structurePanel()"), "Debe aportar slot izquierdo al workbench.");
        assertTrue(source.contains("propertiesPanel()"), "Debe aportar slot derecho al workbench.");
        assertTrue(source.contains("migratedVisualDiagram"), "Debe usar descriptor migrado, no legacy.");
    }

    @Test
    void architectureMustUseCanonicalSurfaceBridge() throws IOException {
        String source = Files.readString(ARCHITECTURE_PACKAGE.resolve("ArchitectureDiagramCenter.java"), StandardCharsets.UTF_8);

        assertTrue(source.contains("ZoomableDiagramSurface"), "Debe usar la superficie zoomable canónica.");
        assertTrue(source.contains("InteractiveCanvasSurfaceView"), "Debe usar el puente normalizado adapter + surface.");
        assertFalse(source.contains("InteractiveDiagramCanvasView"), "No debe usar el canvas mínimo legado.");
    }
}
