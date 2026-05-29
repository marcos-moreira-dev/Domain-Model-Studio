package com.marcosmoreira.domainmodelstudio.application.wireframe;

import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeDocument;

/** Crea documentos vacíos de wireframes administrativos. */
public final class CreateWireframeUseCase {

    public WireframeDocument createBlank(String projectName) {
        return WireframeDocument.blank(projectName);
    }
}
