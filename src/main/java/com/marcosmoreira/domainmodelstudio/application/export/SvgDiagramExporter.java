package com.marcosmoreira.domainmodelstudio.application.export;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;

/** Puerto de exportación SVG independiente de JavaFX. */
public interface SvgDiagramExporter {

    String export(DiagramProject project);
}
