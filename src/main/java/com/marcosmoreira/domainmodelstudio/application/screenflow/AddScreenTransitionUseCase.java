package com.marcosmoreira.domainmodelstudio.application.screenflow;

import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenFlowDocument;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenTransition;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenTransitionKind;

/** Caso de uso para conectar dos pantallas del flujo administrativo. */
public final class AddScreenTransitionUseCase {

    public ScreenFlowDocument add(ScreenFlowDocument document, String sourceScreenId, String targetScreenId) {
        ScreenTransition transition = new ScreenTransition(
                document.nextTransitionId(),
                sourceScreenId,
                targetScreenId,
                ScreenTransitionKind.NAVIGATES,
                "",
                "",
                "");

        return document.withTransition(transition);
    }
}
