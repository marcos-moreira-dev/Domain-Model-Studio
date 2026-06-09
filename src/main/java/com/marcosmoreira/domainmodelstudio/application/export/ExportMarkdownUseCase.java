package com.marcosmoreira.domainmodelstudio.application.export;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/** Caso de uso para exportar el proyecto activo a Markdown actualizado. */
/**
 * Caso de uso para exportar un proyecto como Markdown canónico.
 *
 * <p>Valida que exista un exportador para el tipo de proyecto y delega el formato concreto a
 * un puerto. No decide rutas de UI ni escribe mensajes visuales.</p>
 */
public final class ExportMarkdownUseCase {

    private final MarkdownDiagramExporter markdownDiagramExporter;
    private final ExportTargetPathPolicy targetPathPolicy;

    public ExportMarkdownUseCase(MarkdownDiagramExporter markdownDiagramExporter) {
        this(markdownDiagramExporter, new ExportTargetPathPolicy());
    }

    ExportMarkdownUseCase(MarkdownDiagramExporter markdownDiagramExporter, ExportTargetPathPolicy targetPathPolicy) {
        this.markdownDiagramExporter = Objects.requireNonNull(markdownDiagramExporter, "markdownDiagramExporter");
        this.targetPathPolicy = Objects.requireNonNull(targetPathPolicy, "targetPathPolicy");
    }

    public Path export(DiagramProject project, Path targetFile) throws IOException {
        Objects.requireNonNull(project, "El proyecto no puede ser null");
        Objects.requireNonNull(targetFile, "El archivo destino no puede ser null");
        Path normalized = targetPathPolicy.ensureMarkdownExtension(targetFile);
        Path parent = normalized.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        Files.writeString(normalized, markdownDiagramExporter.export(project), StandardCharsets.UTF_8);
        return normalized;
    }

}
