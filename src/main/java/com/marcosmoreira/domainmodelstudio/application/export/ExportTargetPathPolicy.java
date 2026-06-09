package com.marcosmoreira.domainmodelstudio.application.export;

import java.nio.file.Path;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

/**
 * Normaliza rutas de salida según el formato exportado.
 *
 * <p>La política vive fuera de JavaFX para que casos de uso, infraestructura y
 * exportación por lote compartan la misma regla sin depender de clases de
 * presentación: el usuario puede elegir un nombre sin extensión, pero el archivo
 * final siempre queda con la extensión correcta.</p>
 */
/**
 * Política de nombres de archivo para salidas exportadas.
 *
 * <p>Centraliza la normalización de extensiones y evita que cada pantalla genere rutas de
 * manera distinta. Es aplicación porque expresa una regla de uso, no una operación de disco.</p>
 */
public final class ExportTargetPathPolicy {

    private static final Set<String> MARKDOWN_EXTENSIONS = Set.of(".md", ".markdown");

    public Path ensureSvgExtension(Path targetFile) {
        return ensureSingleExtension(targetFile, ".svg");
    }

    public Path ensurePngExtension(Path targetFile) {
        return ensureSingleExtension(targetFile, ".png");
    }

    public Path ensurePdfExtension(Path targetFile) {
        return ensureSingleExtension(targetFile, ".pdf");
    }

    public Path ensureMarkdownExtension(Path targetFile) {
        return ensureAnyExtension(targetFile, MARKDOWN_EXTENSIONS, ".md");
    }

    private Path ensureSingleExtension(Path targetFile, String requiredExtension) {
        Objects.requireNonNull(targetFile, "targetFile");
        String fileName = targetFile.getFileName().toString();
        if (fileName.toLowerCase(Locale.ROOT).endsWith(requiredExtension)) {
            return targetFile;
        }
        return targetFile.resolveSibling(fileName + requiredExtension);
    }

    private Path ensureAnyExtension(Path targetFile, Set<String> acceptedExtensions, String defaultExtension) {
        Objects.requireNonNull(targetFile, "targetFile");
        String fileName = targetFile.getFileName().toString();
        String lower = fileName.toLowerCase(Locale.ROOT);
        for (String extension : acceptedExtensions) {
            if (lower.endsWith(extension)) {
                return targetFile;
            }
        }
        return targetFile.resolveSibling(fileName + defaultExtension);
    }
}
