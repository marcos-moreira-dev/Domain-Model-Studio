package com.marcosmoreira.domainmodelstudio.application.wireframe;

import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeComponent;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeComponentKind;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeDocument;
import java.util.Objects;

/** Actualiza los datos semánticos de un componente de wireframe. */
public final class UpdateWireframeComponentUseCase {

    public WireframeDocument update(
            WireframeDocument document,
            String id,
            String screenId,
            WireframeComponentKind kind,
            String name,
            int orderIndex,
            String binding,
            String behavior,
            String notes
    ) {
        Objects.requireNonNull(document, "document");
        return document.withUpdatedComponent(new WireframeComponent(
                id,
                screenId,
                kind,
                name,
                orderIndex,
                binding,
                behavior,
                notes));
    }
}
