package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl R5-A: los ViewModels visuales comparten el parche transversal de apariencia. */
class VisualProjectPatchSupportRefactorSourceTest {

    private static final Path PRESENTATION = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation"
    );

    @Test
    void visualProjectPatchSupportShouldCentralizeCommonAppearancePatchFlow() throws IOException {
        String support = read("interactivecanvas/VisualProjectPatchSupport.java");

        assertTrue(support.contains("No hay proyecto activo para editar apariencia."));
        assertTrue(support.contains("Proyecto actualizado."));
        assertTrue(support.contains("Objects.requireNonNull(patch, \"patch\").apply(currentProject)"));
        assertTrue(support.contains("projectUpdater.accept(updatedProject)"));
        assertTrue(support.contains("changeNotifier.run()"));
    }

    @Test
    void mainVisualViewModelsShouldDelegatePatchCurrentProjectToSupport() throws IOException {
        assertDelegates("architecture/ArchitectureDiagramViewModel.java");
        assertDelegates("behavior/BehaviorDiagramViewModel.java");
        assertDelegates("freegraph/FreeGraphViewModelCore.java");
        assertDelegates("modulemap/ModuleMapViewModel.java");
        assertDelegates("screenflow/ScreenFlowViewModel.java");
        assertDelegates("umlclass/UmlClassDiagramViewModel.java");
        assertDelegates("wireframe/WireframeViewModel.java");
    }

    private static void assertDelegates(String relativePath) throws IOException {
        String source = read(relativePath);
        assertTrue(source.contains("VisualProjectPatchSupport.apply"),
                relativePath + " debe delegar el parche visual común.");
        assertFalse(source.contains("No hay proyecto activo para editar apariencia.\"); return;"),
                relativePath + " no debe duplicar el flujo inline de patchCurrentProject.");
    }

    private static String read(String relativePath) throws IOException {
        return Files.readString(PRESENTATION.resolve(relativePath), StandardCharsets.UTF_8);
    }
}
