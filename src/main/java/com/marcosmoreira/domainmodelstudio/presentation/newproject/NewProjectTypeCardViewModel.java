package com.marcosmoreira.domainmodelstudio.presentation.newproject;

import java.util.List;

/** Tarjeta visible de tipo de diagrama, sin lógica de negocio ni JavaFX. */
public record NewProjectTypeCardViewModel(
        String id,
        String displayName,
        String statusLabel,
        String description,
        List<String> capabilities,
        boolean selectable
) {
}
