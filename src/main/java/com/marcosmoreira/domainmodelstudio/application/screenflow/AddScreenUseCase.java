package com.marcosmoreira.domainmodelstudio.application.screenflow;

import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenFlowDocument;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenKind;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenNode;

/** Caso de uso para agregar una pantalla al flujo administrativo. */
public final class AddScreenUseCase {

    public ScreenFlowDocument add(ScreenFlowDocument document, String name) {
        ScreenNode screen = new ScreenNode(
                document.nextScreenId(),
                name,
                ScreenKind.OTHER,
                "",
                "",
                "",
                "");

        return document.withScreen(screen);
    }
}
