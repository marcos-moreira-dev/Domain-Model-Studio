package com.marcosmoreira.domainmodelstudio.application.screenflow;

import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenFlowDocument;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenKind;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenNode;

/** Caso de uso para actualizar los datos de una pantalla administrativa. */
public final class UpdateScreenUseCase {

    public ScreenFlowDocument update(
            ScreenFlowDocument document,
            String screenId,
            String name,
            ScreenKind kind,
            String moduleName,
            String route,
            String purpose,
            String notes
    ) {
        ScreenNode screen = new ScreenNode(screenId, name, kind, moduleName, route, purpose, notes);
        return document.withUpdatedScreen(screen);
    }
}
