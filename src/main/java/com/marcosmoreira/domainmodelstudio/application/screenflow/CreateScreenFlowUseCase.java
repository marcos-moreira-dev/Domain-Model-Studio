package com.marcosmoreira.domainmodelstudio.application.screenflow;

import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenFlowDocument;

/** Caso de uso para crear un flujo de pantallas vacío. */
public final class CreateScreenFlowUseCase {

    public ScreenFlowDocument createBlank(String projectName) {
        return ScreenFlowDocument.blank(projectName);
    }
}
