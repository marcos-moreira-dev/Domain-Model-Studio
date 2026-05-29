package com.marcosmoreira.domainmodelstudio.application.project;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;

/**
 * Puerto para sincronizar el Markdown fuente de un proyecto sin acoplar presentation a infrastructure.
 */
public interface SourceMarkdownSynchronizer {

    SourceMarkdownSyncResult synchronize(DiagramProject project);
}
