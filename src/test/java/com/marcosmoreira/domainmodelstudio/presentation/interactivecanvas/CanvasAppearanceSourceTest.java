package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Regresión fuente: la apariencia visual vive como capacidad transversal del canvas común. */
class CanvasAppearanceSourceTest {

    @Test
    void migratedVisualAdaptersExposeProjectBackedStylePort() throws IOException {
        List<Path> adapters = List.of(
                Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/modulemap/ModuleMapCanvasAdapter.java"),
                Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/screenflow/ScreenFlowCanvasAdapter.java"),
                Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/wireframe/WireframeCanvasAdapter.java"),
                Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassCanvasAdapter.java"),
                Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/behavior/BehaviorCanvasAdapter.java"),
                Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/behavior/SequenceCanvasAdapter.java"),
                Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/architecture/ArchitectureCanvasAdapter.java")
        );
        for (Path adapter : adapters) {
            assertTrue(read(adapter).contains("CanvasProjectStylePort"), adapter + " debe exponer estilos persistentes.");
        }
    }

    @Test
    void screenAndPngApplyExplicitStylesFromTheSamePort() throws IOException {
        String surface = read(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/InteractiveCanvasSurfaceView.java"));
        String png = read(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/InteractiveCanvasPngExporter.java"));

        assertTrue(surface.contains("CanvasStyleApplier.applyNodeStyle"));
        assertTrue(surface.contains("CanvasStyleApplier.applyConnectorStyle"));
        assertTrue(png.contains("CanvasStyleApplier.applyNodeStyle"));
        assertTrue(png.contains("CanvasStyleApplier.applyConnectorStyle"));
    }

    @Test
    void visualWorkbenchesRegisterRealAppearanceModule() throws IOException {
        List<Path> contributors = List.of(
                Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/modulemap/ModuleMapWorkbenchContributor.java"),
                Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/screenflow/ScreenFlowWorkbenchContributor.java"),
                Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/wireframe/WireframeWorkbenchContributor.java"),
                Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassWorkbenchContributor.java"),
                Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/behavior/BehaviorWorkbenchContributor.java"),
                Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/architecture/ArchitectureWorkbenchContributor.java")
        );
        for (Path contributor : contributors) {
            String source = read(contributor);
            assertTrue(source.contains("StandardSideDockModules.appearance"), contributor + " debe registrar módulo Apariencia mediante fábrica transversal.");
            assertTrue(source.contains("diagramCenter.appearancePanel()"), contributor + " debe usar panel operativo de apariencia.");
        }
    }

    private static String read(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
