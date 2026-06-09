package com.marcosmoreira.domainmodelstudio.application.screenflow;

import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenFlowDocument;

/** Caso de uso para eliminar pantallas o transiciones del flujo administrativo. */
public final class RemoveScreenFlowItemUseCase {

    public ScreenFlowDocument removeScreen(ScreenFlowDocument document, String screenId) {
        return document.withoutScreen(screenId);
    }

    public ScreenFlowDocument removeTransition(ScreenFlowDocument document, String transitionId) {
        return document.withoutTransition(transitionId);
    }
}
