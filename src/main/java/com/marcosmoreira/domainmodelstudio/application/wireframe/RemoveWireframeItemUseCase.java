package com.marcosmoreira.domainmodelstudio.application.wireframe;

import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeDocument;
import java.util.Objects;

/** Elimina pantallas o componentes del documento de wireframes. */
public final class RemoveWireframeItemUseCase {

    public WireframeDocument removeScreen(WireframeDocument document, String id) {
        Objects.requireNonNull(document, "document");
        return document.withoutScreen(id);
    }

    public WireframeDocument removeComponent(WireframeDocument document, String id) {
        Objects.requireNonNull(document, "document");
        return document.withoutComponent(id);
    }
}
