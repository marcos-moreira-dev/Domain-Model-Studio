package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl R5-B: las acciones visuales comunes no vuelven a duplicarse en cada ViewModel. */
class VisualDiagramViewActionsRefactorSourceTest {

    private static final Path PRESENTATION = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation"
    );

    @Test
    void visualDiagramViewActionsShouldCentralizeFitCenterAndPngRegistration() throws IOException {
        String source = read("interactivecanvas/VisualDiagramViewActions.java");

        assertTrue(source.contains("forGenericDiagram"));
        assertTrue(source.contains("forFreeGraph"));
        assertTrue(source.contains("registerPngExportAction"));
        assertTrue(source.contains("registerDiagramFitAction"));
        assertTrue(source.contains("registerDiagramCenterAction"));
        assertTrue(source.contains("runWhenActive"));
        assertTrue(source.contains("pngExportAction()"));
    }

    @Test
    void visualViewModelsShouldDelegateCommonViewActions() throws IOException {
        assertDelegates("architecture/ArchitectureDiagramViewModel.java");
        assertDelegates("behavior/BehaviorDiagramViewModel.java");
        assertDelegates("freegraph/FreeGraphViewModelCore.java");
        assertDelegates("modulemap/ModuleMapViewModel.java");
        assertDelegates("screenflow/ScreenFlowViewModel.java");
        assertDelegates("umlclass/UmlClassDiagramViewModel.java");
        assertDelegates("wireframe/WireframeViewModel.java");
    }

    @Test
    void filesFlaggedByR5AShouldReturnBelowHumanReviewLimits() throws IOException {
        assertBelow("behavior/BehaviorDiagramViewModel.java", 450);
        assertBelow("screenflow/ScreenFlowViewModel.java", 450);
        assertBelow("wireframe/WireframeViewModel.java", 450);
        assertBelow("umlclass/UmlClassDiagramViewModel.java", 650);
    }

    private static void assertDelegates(String relativePath) throws IOException {
        String source = read(relativePath);
        assertTrue(source.contains("VisualDiagramViewActions"), relativePath + " debe usar el soporte común.");
        assertTrue(source.contains("viewActions.registerPngExportAction"));
        assertTrue(source.contains("viewActions.fitDiagramView"));
        assertTrue(source.contains("viewActions.centerDiagramView"));
        assertFalse(source.contains("fitDiagramAction.run()"), relativePath + " no debe duplicar ajuste de vista.");
        assertFalse(source.contains("centerDiagramAction.run()"), relativePath + " no debe duplicar centrado de vista.");
    }

    private static void assertBelow(String relativePath, int maxLines) throws IOException {
        long lines = Files.lines(PRESENTATION.resolve(relativePath), StandardCharsets.UTF_8).count();
        assertTrue(lines <= maxLines, relativePath + " tiene " + lines + " líneas; límite: " + maxLines);
    }

    private static String read(String relativePath) throws IOException {
        return Files.readString(PRESENTATION.resolve(relativePath), StandardCharsets.UTF_8);
    }
}
