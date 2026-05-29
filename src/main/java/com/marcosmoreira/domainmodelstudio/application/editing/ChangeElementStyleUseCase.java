package com.marcosmoreira.domainmodelstudio.application.editing;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.style.ElementStyle;
import java.util.Objects;

/** Caso de uso para aplicar estilo visual específico a un elemento. */
public final class ChangeElementStyleUseCase {

    public DiagramProject changeStyle(DiagramProject project, DiagramElementId elementId, ElementStyle style) {
        Objects.requireNonNull(project, "El proyecto no puede ser null");
        Objects.requireNonNull(elementId, "El ID del elemento no puede ser null");
        Objects.requireNonNull(style, "El estilo no puede ser null");
        return project.withStyleSheet(project.styleSheet().withElementStyle(elementId, style));
    }
}
