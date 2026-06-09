package com.marcosmoreira.domainmodelstudio.application.project;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

/** Caso de uso para guardar el proyecto editable en un archivo .dms. */
public final class SaveProjectUseCase {

    private final ProjectRepository projectRepository;

    public SaveProjectUseCase(ProjectRepository projectRepository) {
        this.projectRepository = Objects.requireNonNull(projectRepository, "projectRepository");
    }

    public void save(DiagramProject project, Path targetFile) throws IOException {
        Objects.requireNonNull(project, "El proyecto no puede ser null");
        Objects.requireNonNull(targetFile, "El archivo destino no puede ser null");
        projectRepository.save(project, ensureDmsExtension(targetFile));
    }

    private Path ensureDmsExtension(Path targetFile) {
        String name = targetFile.getFileName().toString();
        if (name.toLowerCase().endsWith(".dms")) {
            return targetFile;
        }
        return targetFile.resolveSibling(name + ".dms");
    }
}
