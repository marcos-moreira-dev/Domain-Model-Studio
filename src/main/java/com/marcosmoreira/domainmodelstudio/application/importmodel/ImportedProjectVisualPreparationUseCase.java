package com.marcosmoreira.domainmodelstudio.application.importmodel;

import com.marcosmoreira.domainmodelstudio.application.layout.GenerateInitialChenLayoutUseCase;
import com.marcosmoreira.domainmodelstudio.application.layout.GenerateInitialCrowsFootLayoutUseCase;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import java.util.Objects;

/**
 * Prepara visualmente un proyecto recién importado desde Markdown sin mezclar reglas de diagramas.
 *
 * <p>El modelo conceptual necesita layout ER explícito porque Chen y pata de gallo usan posiciones y
 * conectores propios. Los demás tipos visibles ya guardan su documento especializado y sus editores
 * generan la disposición visual desde ese documento; aplicarles layout Chen sería un falso positivo
 * heredado del flujo antiguo.</p>
 */
public final class ImportedProjectVisualPreparationUseCase {

    private final GenerateInitialChenLayoutUseCase chenLayoutUseCase;
    private final GenerateInitialCrowsFootLayoutUseCase crowsFootLayoutUseCase;

    public ImportedProjectVisualPreparationUseCase(
            GenerateInitialChenLayoutUseCase chenLayoutUseCase,
            GenerateInitialCrowsFootLayoutUseCase crowsFootLayoutUseCase
    ) {
        this.chenLayoutUseCase = Objects.requireNonNull(chenLayoutUseCase, "chenLayoutUseCase");
        this.crowsFootLayoutUseCase = Objects.requireNonNull(crowsFootLayoutUseCase, "crowsFootLayoutUseCase");
    }

    public DiagramProject prepare(DiagramProject importedProject) {
        Objects.requireNonNull(importedProject, "importedProject");
        if (!requiresConceptualLayout(importedProject.metadata().diagramTypeId())) {
            return importedProject;
        }
        if (NotationType.CROWS_FOOT.equals(importedProject.metadata().activeNotation())) {
            return crowsFootLayoutUseCase.generate(importedProject);
        }
        return chenLayoutUseCase.generate(importedProject);
    }

    public boolean requiresConceptualLayout(DiagramTypeId diagramTypeId) {
        return DiagramTypeId.CONCEPTUAL_MODEL.equals(diagramTypeId);
    }
}
