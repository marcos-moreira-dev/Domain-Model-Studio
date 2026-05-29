package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

class InteractiveCanvasPngExporterSourceStructureTest {

    private static final Path EXPORTER = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/InteractiveCanvasPngExporter.java"
    );

    @Test
    void commonPngExporterUsesRenderKitSceneCssMeasuredBoundsAndViewport() throws IOException {
        String source = Files.readString(EXPORTER, StandardCharsets.UTF_8);

        assertTrue(source.contains("InteractiveCanvasModel.from(adapter)"),
                "El PNG común debe congelar un snapshot desde el adapter activo.");
        assertTrue(source.contains("renderKit.renderConnector"),
                "El PNG común debe reutilizar el render kit específico para conectores.");
        assertTrue(source.contains("renderKit.renderNode"),
                "El PNG común debe reutilizar el render kit específico para nodos.");
        assertTrue(source.contains("new Scene"),
                "La exportación debe montar una Scene limpia para resolver CSS de app-light.css.");
        assertTrue(source.contains("CanvasExportSurface.of(measuredBounds"),
                "El PNG común debe medir los bounds reales renderizados antes del snapshot.");
        assertTrue(source.contains("parameters.setFill(exportBackgroundColor())"),
                "La exportación PNG debe tener fondo explícito.");
        assertTrue(source.contains("parameters.setViewport"),
                "El snapshot debe usar viewport exportable, no depender del tamaño del Pane.");
        assertTrue(source.contains("SwingFXUtils.fromFXImage"),
                "La escritura PNG debe estar centralizada en el exportador común.");
    }

    @Test
    void commonPngExporterUsesHighResolutionScaleByDefault() throws IOException {
        String source = Files.readString(EXPORTER, StandardCharsets.UTF_8);

        assertTrue(source.contains("DEFAULT_PNG_SCALE = 2.0"),
                "La exportación PNG debe salir por defecto al doble de resolución lógica.");
        assertTrue(source.contains("PNG_SCALE_PROPERTY"),
                "Debe existir una propiedad técnica para subir o bajar la escala cuando haga falta.");
        assertTrue(source.contains("MAX_PNG_SCALE = 3.0"),
                "La escala debe poder llegar hasta triple resolución de forma controlada.");
        assertTrue(source.contains("Transform.scale(exportScale, exportScale)"),
                "El snapshot debe escalar la imagen, no agrandar el modelo lógico del diagrama.");
        assertTrue(source.contains("assertReasonableSnapshotSize(pixelWidth, pixelHeight)"),
                "El guardarraíl de memoria debe evaluar los píxeles reales exportados.");
    }

    @Test
    void commonPngExporterMustNotKnowConcreteDiagramFamilies() throws IOException {
        String source = Files.readString(EXPORTER, StandardCharsets.UTF_8);
        List<String> forbidden = List.of(
                ".presentation.modulemap.",
                ".presentation.screenflow.",
                ".presentation.wireframe.",
                ".presentation.umlclass.",
                ".presentation.behavior.",
                ".presentation.architecture."
        );
        for (String fragment : forbidden) {
            assertFalse(source.contains(fragment), "El exportador PNG común no debe conocer " + fragment);
        }
    }

    @Test
    void migratedDiagramCentersDelegatePngExportToCommonExporter() throws IOException {
        List<Path> centers = List.of(
                Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/modulemap/ModuleMapDiagramCenter.java"),
                Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/screenflow/ScreenFlowDiagramCenter.java"),
                Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/wireframe/WireframeDiagramCenter.java"),
                Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassDiagramCenter.java"),
                Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/behavior/BehaviorDiagramCenter.java"),
                Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/architecture/ArchitectureDiagramCenter.java")
        );

        for (Path center : centers) {
            String source = Files.readString(center, StandardCharsets.UTF_8);
            assertTrue(source.contains("InteractiveCanvasPngExporter"), center + " debe delegar PNG al exportador común.");
            assertFalse(source.contains("ImageIO.write"), center + " no debe escribir PNG directamente.");
            assertFalse(source.contains("SwingFXUtils.fromFXImage"), center + " no debe hacer snapshot/exportación duplicada.");
            assertFalse(source.contains("private Pane buildExportRoot"), center + " no debe duplicar la superficie exportable.");
        }
    }
}
