package com.marcosmoreira.domainmodelstudio.application.project;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.io.IOException;
import java.nio.file.Path;

/** Puerto de persistencia para proyectos editables .dms. */
public interface ProjectRepository {

    void save(DiagramProject project, Path targetFile) throws IOException;

    DiagramProject open(Path sourceFile) throws IOException;
}
