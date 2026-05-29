package com.marcosmoreira.domainmodelstudio.application.runtime;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.Objects;

/** Contrato runtime declarativo de ayuda académica/recursos IA asociados a un tipo. */
public record HelpRuntimeDescriptor(
        DiagramTypeId diagramTypeId,
        String theoryTopicId,
        boolean aiResourcesAvailable
) {

    public HelpRuntimeDescriptor {
        Objects.requireNonNull(diagramTypeId, "diagramTypeId");
        theoryTopicId = theoryTopicId == null ? "" : theoryTopicId.strip();
    }
}
