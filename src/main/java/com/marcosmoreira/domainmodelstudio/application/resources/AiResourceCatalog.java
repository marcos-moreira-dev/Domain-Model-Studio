package com.marcosmoreira.domainmodelstudio.application.resources;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.List;

/** Puerto de lectura para recursos IA registrados. */
public interface AiResourceCatalog {

    List<AiResourceDescriptor> findAllExportable();

    List<AiResourceDescriptor> findByDiagramType(DiagramTypeId diagramTypeId);
}
