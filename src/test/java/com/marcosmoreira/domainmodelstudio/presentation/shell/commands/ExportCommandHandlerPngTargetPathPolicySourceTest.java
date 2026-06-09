package com.marcosmoreira.domainmodelstudio.presentation.shell.commands;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl para evitar que la exportación PNG directa guarde archivos sin extensión. */
class ExportCommandHandlerPngTargetPathPolicySourceTest {

    private static final Path EXPORT_HANDLER = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/commands/ExportCommandHandler.java");

    @Test
    void directPngExportNormalizesTargetPathBeforeInvokingPngAction() throws Exception {
        String source = Files.readString(EXPORT_HANDLER, StandardCharsets.UTF_8);

        assertTrue(source.contains("ExportTargetPathPolicy"),
                "El handler debe reutilizar la política central de extensiones de exportación.");
        assertTrue(source.contains("targetPathPolicy.ensurePngExtension(file.toPath())"),
                "La ruta elegida por el usuario debe normalizarse a .png antes de exportar.");
        assertTrue(source.contains(".export(targetFile)"),
                "La acción PNG debe recibir la ruta normalizada, no file.toPath() directo.");
        assertTrue(source.contains("targetFile.toAbsolutePath()"),
                "El estado debe informar la ruta real finalmente exportada.");
    }
}
