package com.marcosmoreira.domainmodelstudio.application.export;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/** Caso de uso para exportar el proyecto activo a SVG vectorial real. */
/**
 * Caso de uso para exportar diagramas visuales a SVG.
 *
 * <p>El caso de uso conserva la frontera: recibe un proyecto de dominio y usa un exportador
 * especializado para producir contenido vectorial, sin depender de JavaFX.</p>
 */
public final class ExportSvgUseCase {

    private final SvgDiagramExporter svgDiagramExporter;
    private final ExportTargetPathPolicy targetPathPolicy;

    public ExportSvgUseCase(SvgDiagramExporter svgDiagramExporter) {
        this(svgDiagramExporter, new ExportTargetPathPolicy());
    }

    ExportSvgUseCase(SvgDiagramExporter svgDiagramExporter, ExportTargetPathPolicy targetPathPolicy) {
        this.svgDiagramExporter = Objects.requireNonNull(svgDiagramExporter, "svgDiagramExporter");
        this.targetPathPolicy = Objects.requireNonNull(targetPathPolicy, "targetPathPolicy");
    }

    public Path export(DiagramProject project, Path targetFile) throws IOException {
        Objects.requireNonNull(project, "El proyecto no puede ser null");
        Objects.requireNonNull(targetFile, "El archivo destino no puede ser null");
        Path normalized = targetPathPolicy.ensureSvgExtension(targetFile);
        Path parent = normalized.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        Files.writeString(normalized, svgDiagramExporter.export(project), StandardCharsets.UTF_8);
        return normalized;
    }

}
