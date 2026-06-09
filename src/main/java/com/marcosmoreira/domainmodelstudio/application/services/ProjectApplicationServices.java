package com.marcosmoreira.domainmodelstudio.application.services;

import com.marcosmoreira.domainmodelstudio.application.project.OpenProjectUseCase;
import com.marcosmoreira.domainmodelstudio.application.project.SaveProjectUseCase;
import com.marcosmoreira.domainmodelstudio.application.project.SourceMarkdownSynchronizer;
import com.marcosmoreira.domainmodelstudio.application.validation.ValidateProjectUseCase;
import java.util.Objects;

/**
 * Fachada de casos de uso de ciclo de vida del proyecto.
 *
 * <p>Centraliza operaciones transversales de proyecto para que nuevas piezas de
 * presentación no dependan de la fachada monolítica {@code ApplicationServices}.
 * No contiene JavaFX ni lógica de infraestructura.</p>
 */
public final class ProjectApplicationServices {

    private final SaveProjectUseCase saveProjectUseCase;
    private final OpenProjectUseCase openProjectUseCase;
    private final SourceMarkdownSynchronizer sourceMarkdownSynchronizer;
    private final ValidateProjectUseCase validateProjectUseCase;

    public ProjectApplicationServices(
            SaveProjectUseCase saveProjectUseCase,
            OpenProjectUseCase openProjectUseCase,
            SourceMarkdownSynchronizer sourceMarkdownSynchronizer,
            ValidateProjectUseCase validateProjectUseCase
    ) {
        this.saveProjectUseCase = Objects.requireNonNull(saveProjectUseCase, "saveProjectUseCase");
        this.openProjectUseCase = Objects.requireNonNull(openProjectUseCase, "openProjectUseCase");
        this.sourceMarkdownSynchronizer = Objects.requireNonNull(sourceMarkdownSynchronizer, "sourceMarkdownSynchronizer");
        this.validateProjectUseCase = Objects.requireNonNull(validateProjectUseCase, "validateProjectUseCase");
    }

    public SaveProjectUseCase saveProjectUseCase() {
        return saveProjectUseCase;
    }

    public OpenProjectUseCase openProjectUseCase() {
        return openProjectUseCase;
    }

    public SourceMarkdownSynchronizer sourceMarkdownSynchronizer() {
        return sourceMarkdownSynchronizer;
    }

    public ValidateProjectUseCase validateProjectUseCase() {
        return validateProjectUseCase;
    }
}
