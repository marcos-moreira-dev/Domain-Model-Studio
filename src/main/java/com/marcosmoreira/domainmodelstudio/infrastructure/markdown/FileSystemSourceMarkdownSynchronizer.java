package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import com.marcosmoreira.domainmodelstudio.application.project.SourceMarkdownSyncResult;
import com.marcosmoreira.domainmodelstudio.application.project.SourceMarkdownSynchronizer;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

/** Sincroniza el archivo Markdown fuente cuando el proyecto conserva una ruta editable. */
public final class FileSystemSourceMarkdownSynchronizer implements SourceMarkdownSynchronizer {

    @Override
    public SourceMarkdownSyncResult synchronize(DiagramProject project) {
        if (project == null) {
            return SourceMarkdownSyncResult.skipped();
        }
        String source = project.metadata().sourceMarkdownPath();
        if (source == null || source.isBlank() || !source.toLowerCase(Locale.ROOT).endsWith(".md")) {
            return SourceMarkdownSyncResult.skipped();
        }
        try {
            Path path = Path.of(source);
            if (!Files.exists(path) || !Files.isRegularFile(path)) {
                return SourceMarkdownSyncResult.skipped();
            }
            Files.writeString(path, MarkdownProjectWriter.write(project), StandardCharsets.UTF_8);
            return SourceMarkdownSyncResult.synchronizedOk("Cambios aplicados y Markdown fuente sincronizado.");
        } catch (Exception exception) {
            return SourceMarkdownSyncResult.failed(
                    "Cambios aplicados, pero no se pudo sincronizar el Markdown fuente: " + exception.getMessage());
        }
    }
}
