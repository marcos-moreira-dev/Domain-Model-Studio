package com.marcosmoreira.domainmodelstudio.application.screenflow;

import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenFlowDocument;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenTransition;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenTransitionKind;

/** Caso de uso para actualizar una transición de navegación entre pantallas. */
public final class UpdateScreenTransitionUseCase {

    public ScreenFlowDocument update(
            ScreenFlowDocument document,
            String transitionId,
            String sourceScreenId,
            String targetScreenId,
            ScreenTransitionKind kind,
            String trigger,
            String condition,
            String notes
    ) {
        ScreenTransition transition = new ScreenTransition(
                transitionId,
                sourceScreenId,
                targetScreenId,
                kind,
                trigger,
                condition,
                notes);

        return document.withUpdatedTransition(transition);
    }
}
