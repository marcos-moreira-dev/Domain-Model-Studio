package com.marcosmoreira.domainmodelstudio.application.screenflow;

import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenFlowDocument;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenTransition;
import java.util.ArrayList;

/** Caso de uso para validar advertencias operativas del flujo de pantallas. */
public final class ValidateScreenFlowUseCase {

    public ScreenFlowValidationResult validate(ScreenFlowDocument document) {
        var warnings = new ArrayList<String>();

        warnWhenNoScreens(document, warnings);
        warnWhenMultipleScreensHaveNoTransitions(document, warnings);
        warnWhenTransitionsReferenceMissingScreens(document, warnings);

        return warnings.isEmpty()
                ? ScreenFlowValidationResult.valid()
                : ScreenFlowValidationResult.warnings(warnings);
    }

    private static void warnWhenNoScreens(ScreenFlowDocument document, ArrayList<String> warnings) {
        if (document.screens().isEmpty()) {
            warnings.add("No hay pantallas definidas.");
        }
    }

    private static void warnWhenMultipleScreensHaveNoTransitions(
            ScreenFlowDocument document,
            ArrayList<String> warnings
    ) {
        if (document.screens().size() > 1 && document.transitions().isEmpty()) {
            warnings.add("Hay varias pantallas pero no hay transiciones.");
        }
    }

    private static void warnWhenTransitionsReferenceMissingScreens(
            ScreenFlowDocument document,
            ArrayList<String> warnings
    ) {
        for (ScreenTransition transition : document.transitions()) {
            if (document.screenById(transition.sourceScreenId()).isEmpty()) {
                warnings.add("Transición con origen inexistente: " + transition.sourceScreenId());
            }
            if (document.screenById(transition.targetScreenId()).isEmpty()) {
                warnings.add("Transición con destino inexistente: " + transition.targetScreenId());
            }
        }
    }
}
