package com.marcosmoreira.domainmodelstudio.application.wireframe;

import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeDocument;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeScreen;
import java.util.Objects;

/** Agrega una pantalla base al documento de wireframes administrativos. */
public final class AddWireframeScreenUseCase {

    public WireframeDocument add(WireframeDocument document, String name) {
        Objects.requireNonNull(document, "document");
        return document.withScreen(new WireframeScreen(document.nextScreenId(), name, "", "", ""));
    }
}
