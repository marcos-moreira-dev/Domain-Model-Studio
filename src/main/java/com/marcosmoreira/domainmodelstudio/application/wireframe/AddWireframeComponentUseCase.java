package com.marcosmoreira.domainmodelstudio.application.wireframe;

import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeComponent;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeComponentKind;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeDocument;
import java.util.Objects;

/** Agrega un componente semántico a una pantalla de wireframe administrativo. */
public final class AddWireframeComponentUseCase {

    public WireframeDocument add(WireframeDocument document, String screenId, WireframeComponentKind kind) {
        Objects.requireNonNull(document, "document");
        WireframeComponentKind normalizedKind = kind == null ? WireframeComponentKind.OTHER : kind;
        return document.withComponent(new WireframeComponent(
                document.nextComponentId(),
                screenId,
                normalizedKind,
                normalizedKind.displayName(),
                document.components().size() + 1,
                "",
                "",
                ""));
    }
}
