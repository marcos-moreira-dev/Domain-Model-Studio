package com.marcosmoreira.domainmodelstudio.infrastructure.json;

import com.marcosmoreira.domainmodelstudio.application.project.ProjectRepository;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Adaptador de infraestructura para guardar y abrir proyectos {@code .dms} como JSON legible.
 *
 * <p>Implementa el puerto de repositorio de aplicación. Crea carpetas de destino al
 * guardar y traduce errores de payload inválido a {@link IOException}, manteniendo la
 * frontera entre caso de uso y sistema de archivos.</p>
 */
public final class DmsProjectFileRepository implements ProjectRepository {

    private final DmsProjectJsonWriter writer = new DmsProjectJsonWriter();
    private final DmsProjectJsonReader reader = new DmsProjectJsonReader();

    @Override
    public void save(DiagramProject project, Path targetFile) throws IOException {
        Objects.requireNonNull(project, "project");
        Objects.requireNonNull(targetFile, "targetFile");
        Path parent = targetFile.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        Files.writeString(targetFile, writer.write(project), StandardCharsets.UTF_8);
    }

    @Override
    public DiagramProject open(Path sourceFile) throws IOException {
        Objects.requireNonNull(sourceFile, "sourceFile");
        String json = Files.readString(sourceFile, StandardCharsets.UTF_8);
        try {
            return reader.read(json);
        } catch (IllegalArgumentException exception) {
            throw new IOException("Archivo .dms inválido: " + exception.getMessage(), exception);
        }
    }
}
