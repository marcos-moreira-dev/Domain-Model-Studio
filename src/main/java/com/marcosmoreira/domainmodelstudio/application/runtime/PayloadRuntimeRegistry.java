package com.marcosmoreira.domainmodelstudio.application.runtime;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.util.List;
import java.util.Optional;

/** Registry de payloads esperados por tipo de proyecto. */
public interface PayloadRuntimeRegistry {

    Optional<PayloadRuntimeDescriptor> find(DiagramTypeId diagramTypeId);

    PayloadRuntimeDescriptor require(DiagramTypeId diagramTypeId);

    List<PayloadRuntimeDescriptor> findAll();

    List<PayloadRuntimeDescriptor> specializedPayloadTypes();

    /** Detecta los payloads especializados presentes en un proyecto, sin duplicar familias polimórficas. */
    List<DiagramTypeId> detectSpecializedPayloadTypeIds(DiagramProject project);
}
