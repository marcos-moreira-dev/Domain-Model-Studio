package com.marcosmoreira.domainmodelstudio.application.wireframe;

import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeDocument;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeScreen;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Valida consistencia básica entre pantallas y componentes de wireframes. */
public final class ValidateWireframeUseCase {

    public WireframeValidationResult validate(WireframeDocument document) {
        Objects.requireNonNull(document, "document");
        List<String> warnings = new ArrayList<>();
        if (document.screens().isEmpty()) {
            warnings.add("No hay pantallas wireframe definidas.");
        }
        for (WireframeScreen screen : document.screens()) {
            boolean hasComponents = document.components().stream()
                    .anyMatch(component -> component.screenId().equals(screen.id()));
            if (!hasComponents) {
                warnings.add("La pantalla '" + screen.displayName() + "' no tiene componentes.");
            }
        }
        document.components().stream()
                .filter(component -> document.screenById(component.screenId()).isEmpty())
                .map(component -> "Componente con pantalla inexistente: " + component.screenId())
                .forEach(warnings::add);
        return warnings.isEmpty() ? WireframeValidationResult.valid() : WireframeValidationResult.warnings(warnings);
    }
}
