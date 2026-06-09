package com.marcosmoreira.domainmodelstudio.application.editing;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.style.DiagramAppearance;
import java.util.Objects;

/** Caso de uso para cambiar la apariencia general del diagrama. */
public final class ChangeDiagramAppearanceUseCase {

    public DiagramProject changeAppearance(DiagramProject project, DiagramAppearance appearance) {
        Objects.requireNonNull(project, "El proyecto no puede ser null");
        Objects.requireNonNull(appearance, "La apariencia no puede ser null");
        return project.withStyleSheet(project.styleSheet().withAppearance(appearance));
    }
}
