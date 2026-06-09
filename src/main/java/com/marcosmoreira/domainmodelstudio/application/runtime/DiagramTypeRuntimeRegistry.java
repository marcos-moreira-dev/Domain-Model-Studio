package com.marcosmoreira.domainmodelstudio.application.runtime;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.List;
import java.util.Optional;

/** Registry de contratos runtime declarativos por tipo de proyecto. */
public interface DiagramTypeRuntimeRegistry {

    Optional<DiagramTypeRuntimeDescriptor> find(DiagramTypeId diagramTypeId);

    DiagramTypeRuntimeDescriptor require(DiagramTypeId diagramTypeId);

    List<DiagramTypeRuntimeDescriptor> findAll();

    List<DiagramTypeRuntimeDescriptor> availableTypes();

    List<DiagramTypeRuntimeDescriptor> importableMarkdownTypes();
}
