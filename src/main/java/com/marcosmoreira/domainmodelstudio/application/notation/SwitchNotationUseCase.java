package com.marcosmoreira.domainmodelstudio.application.notation;

import com.marcosmoreira.domainmodelstudio.application.layout.GenerateInitialChenLayoutUseCase;
import com.marcosmoreira.domainmodelstudio.application.layout.GenerateInitialCrowsFootLayoutUseCase;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayouts;
import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import java.util.Objects;

/**
 * Cambia la notación activa del proyecto.
 *
 * <p>Chen y Crow's Foot comparten semántica, pero conservan layouts independientes.
 * Si el layout de destino todavía no existe, este caso de uso lo genera antes de activar
 * la notación. Esta decisión evita tratar las notaciones como simples temas visuales.</p>
 */
public final class SwitchNotationUseCase {

    private final GenerateInitialChenLayoutUseCase generateInitialChenLayoutUseCase;
    private final GenerateInitialCrowsFootLayoutUseCase generateInitialCrowsFootLayoutUseCase;

    public SwitchNotationUseCase(
            GenerateInitialChenLayoutUseCase generateInitialChenLayoutUseCase,
            GenerateInitialCrowsFootLayoutUseCase generateInitialCrowsFootLayoutUseCase
    ) {
        this.generateInitialChenLayoutUseCase = Objects.requireNonNull(generateInitialChenLayoutUseCase, "generateInitialChenLayoutUseCase");
        this.generateInitialCrowsFootLayoutUseCase = Objects.requireNonNull(generateInitialCrowsFootLayoutUseCase, "generateInitialCrowsFootLayoutUseCase");
    }

    public DiagramProject switchTo(DiagramProject project, NotationType notation) {
        Objects.requireNonNull(project, "El proyecto no puede ser null");
        NotationType resolvedNotation = notation == null ? NotationType.CHEN : notation;
        DiagramProject preparedProject = ensureLayoutExists(project, resolvedNotation);
        DiagramLayouts updatedLayouts = preparedProject.layouts().withActiveNotation(resolvedNotation);
        return preparedProject
                .withLayouts(updatedLayouts)
                .withMetadata(preparedProject.metadata().withActiveNotation(resolvedNotation));
    }

    private DiagramProject ensureLayoutExists(DiagramProject project, NotationType notation) {
        if (project.layouts().layoutFor(notation).isPresent()) {
            return project;
        }
        return switch (notation) {
            case CHEN -> generateInitialChenLayoutUseCase.generate(project)
                    .withMetadata(project.metadata().withActiveNotation(NotationType.CHEN));
            case CROWS_FOOT -> generateInitialCrowsFootLayoutUseCase.generate(project);
        };
    }
}
