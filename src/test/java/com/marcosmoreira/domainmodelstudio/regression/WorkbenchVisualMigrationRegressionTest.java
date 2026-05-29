package com.marcosmoreira.domainmodelstudio.regression;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/** Guardarraíles para que los diagramas migrados no vuelvan al canvas/layout viejo. */
class WorkbenchVisualMigrationRegressionTest {

    private static final List<Path> MIGRATED_CENTERS = List.of(
            Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/modulemap/ModuleMapDiagramCenter.java"),
            Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/screenflow/ScreenFlowDiagramCenter.java"),
            Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/wireframe/WireframeDiagramCenter.java"),
            Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassDiagramCenter.java"),
            Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/behavior/BehaviorDiagramCenter.java"),
            Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/architecture/ArchitectureDiagramCenter.java")
    );

    @Test
    void migratedCentersUseCanonicalSurfaceAndCommonExportPath() throws IOException {
        for (Path center : MIGRATED_CENTERS) {
            String source = Files.readString(center, StandardCharsets.UTF_8);
            assertTrue(source.contains("ZoomableDiagramSurface"), center + " debe usar superficie zoomable canónica.");
            assertTrue(source.contains("InteractiveCanvasSurfaceView"), center + " debe usar puente de adapter/render kit.");
            assertTrue(source.contains("InteractiveCanvasPngExporter"), center + " debe exportar PNG por ruta común.");
        }
    }

    @Test
    void migratedVisualFamiliesDoNotReintroduceOldCanvasOrInternalSplitPane() throws IOException {
        List<Path> roots = List.of(
                Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/modulemap"),
                Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/screenflow"),
                Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/wireframe"),
                Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass"),
                Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/behavior"),
                Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/architecture")
        );

        for (Path root : roots) {
            try (Stream<Path> stream = Files.walk(root)) {
                for (Path file : stream.filter(path -> path.toString().endsWith(".java")).toList()) {
                    String source = Files.readString(file, StandardCharsets.UTF_8);
                    assertFalse(source.contains("InteractiveDiagramCanvasView"), file + " no debe volver al canvas viejo.");
                    assertFalse(source.contains("new SplitPane"), file + " no debe crear SplitPane interno de editor.");
                }
            }
        }
    }
}
