package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class UmlClassWorkbenchMigrationContractTest {

    private static final Path UML_CLASS_PACKAGE = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass"
    );

    @Test
    void umlClassEditorMustUseWorkbenchInsteadOfLocalSplitPane() throws IOException {
        String source = Files.readString(UML_CLASS_PACKAGE.resolve("UmlClassDiagramEditorView.java"), StandardCharsets.UTF_8);

        assertTrue(source.contains("DiagramWorkbenchView"), "El editor debe ser wrapper del workbench común.");
        assertFalse(source.contains("InteractiveDiagramCanvasView"), "El editor no debe montar el canvas mínimo legado.");
        assertFalse(source.contains("new SplitPane"), "El editor no debe reconstruir un SplitPane propio.");
    }

    @Test
    void umlClassContributorMustExposeWorkbenchSlots() throws IOException {
        String source = Files.readString(
                UML_CLASS_PACKAGE.resolve("UmlClassWorkbenchContributor.java"),
                StandardCharsets.UTF_8
        );

        assertTrue(source.contains("structurePanel()"), "Debe aportar slot izquierdo al workbench.");
        assertTrue(source.contains("propertiesPanel()"), "Debe aportar slot derecho al workbench.");
        assertTrue(source.contains("migratedVisualDiagram"), "Debe usar descriptor migrado, no legacy.");
    }

    @Test
    void umlClassMustUseCanonicalSurfaceBridge() throws IOException {
        String source = Files.readString(UML_CLASS_PACKAGE.resolve("UmlClassDiagramCenter.java"), StandardCharsets.UTF_8);

        assertTrue(source.contains("ZoomableDiagramSurface"), "Debe usar la superficie zoomable canónica.");
        assertTrue(source.contains("InteractiveCanvasSurfaceView"), "Debe usar el puente normalizado adapter + surface.");
        assertFalse(source.contains("InteractiveDiagramCanvasView"), "No debe usar el canvas mínimo legado.");
    }
}
