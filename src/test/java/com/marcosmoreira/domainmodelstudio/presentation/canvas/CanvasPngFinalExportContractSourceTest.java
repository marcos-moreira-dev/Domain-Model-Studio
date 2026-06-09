package com.marcosmoreira.domainmodelstudio.presentation.canvas;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class CanvasPngFinalExportContractSourceTest {

    @Test
    void conceptualPngExporterShouldNormalizeTargetAndProtectHugeSnapshots() throws IOException {
        String source = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/canvas/CanvasPngExporter.java"));

        assertTrue(source.contains("normalizeTarget(targetFile)"),
                "PNG conceptual debe normalizar destino y crear carpeta padre antes de escribir.");
        assertTrue(source.contains("Files.createDirectories(parent)"),
                "PNG conceptual debe crear carpeta destino si no existe.");
        assertTrue(source.contains("MAX_RAW_SNAPSHOT_BYTES"),
                "PNG conceptual debe tener límite de seguridad ante snapshots extremos.");
        assertTrue(source.contains("assertReasonableSnapshotSize(width, height)"),
                "PNG conceptual debe validar tamaño antes de crear WritableImage.");
        assertTrue(source.contains("usa SVG"),
                "El mensaje de seguridad debe orientar al usuario hacia SVG si la imagen es extrema.");
    }
}
