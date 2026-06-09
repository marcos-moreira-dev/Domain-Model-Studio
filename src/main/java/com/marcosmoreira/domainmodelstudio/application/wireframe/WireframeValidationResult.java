package com.marcosmoreira.domainmodelstudio.application.wireframe;

import java.util.List;

/** Resultado de validación del documento de wireframes administrativos. */
public record WireframeValidationResult(boolean ok, String summary, List<String> warnings) {

    public WireframeValidationResult {
        warnings = List.copyOf(warnings == null ? List.of() : warnings);
    }

    public static WireframeValidationResult valid() {
        return new WireframeValidationResult(true, "Wireframes administrativos sin advertencias.", List.of());
    }

    public static WireframeValidationResult warnings(List<String> warnings) {
        return new WireframeValidationResult(false, warnings.size() + " advertencias en wireframes.", warnings);
    }
}
