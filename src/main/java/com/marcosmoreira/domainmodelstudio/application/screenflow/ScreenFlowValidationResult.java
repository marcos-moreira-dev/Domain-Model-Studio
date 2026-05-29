package com.marcosmoreira.domainmodelstudio.application.screenflow;

import java.util.List;

/** Resultado de validación del flujo de pantallas. */
public record ScreenFlowValidationResult(boolean ok, String summary, List<String> warnings) {

    public ScreenFlowValidationResult {
        warnings = List.copyOf(warnings == null ? List.of() : warnings);
    }

    public static ScreenFlowValidationResult valid() {
        return new ScreenFlowValidationResult(true, "Flujo de pantallas sin advertencias.", List.of());
    }

    public static ScreenFlowValidationResult warnings(List<String> warnings) {
        return new ScreenFlowValidationResult(
                false,
                warnings.size() + " advertencias en flujo de pantallas.",
                warnings);
    }
}
