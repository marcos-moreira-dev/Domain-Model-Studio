package com.marcosmoreira.domainmodelstudio.application.export;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;

/** Puerto de exportación Markdown independiente de JavaFX y del sistema de archivos. */
public interface MarkdownDiagramExporter {

    String export(DiagramProject project);
}
