package com.marcosmoreira.domainmodelstudio.application.wireframe;

import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeDocument;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeScreen;
import java.util.Objects;

/** Actualiza nombre, módulo y notas de una pantalla de wireframe. */
public final class UpdateWireframeScreenUseCase {

    public WireframeDocument update(
            WireframeDocument document,
            String id,
            String name,
            String module,
            String purpose,
            String notes
    ) {
        Objects.requireNonNull(document, "document");
        return document.withUpdatedScreen(new WireframeScreen(id, name, module, purpose, notes));
    }
}
