package com.marcosmoreira.domainmodelstudio.application.datadictionary;

import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryDocument;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.time.Clock;
import java.util.Objects;

/**
 * Genera un borrador editable de diccionario de datos desde un modelo conceptual.
 *
 * <p>El caso de uso no sustituye al criterio humano: solo acelera el levantamiento
 * inicial copiando entidades y atributos conceptuales hacia un documento tabular
 * que debe revisarse antes de entregarse a cliente o usarse para implementación.</p>
 */
public final class GenerateDataDictionaryDraftFromConceptualModelUseCase {

    private final ConceptualDataDictionaryDraftFactory factory;

    public GenerateDataDictionaryDraftFromConceptualModelUseCase(Clock clock) {
        this(new ConceptualDataDictionaryDraftFactory(clock));
    }

    public GenerateDataDictionaryDraftFromConceptualModelUseCase(ConceptualDataDictionaryDraftFactory factory) {
        this.factory = Objects.requireNonNull(factory, "factory");
    }

    public DataDictionaryDocument generateDocument(DiagramProject project) {
        requireConceptualContent(project);
        return factory.createDraft(project);
    }

    public DiagramProject attachDraft(DiagramProject project) {
        return project.withDataDictionary(generateDocument(project));
    }

    private static void requireConceptualContent(DiagramProject project) {
        Objects.requireNonNull(project, "project");
        if (project.model().entities().isEmpty()) {
            throw new IllegalArgumentException("No se puede generar diccionario: el modelo conceptual no tiene entidades.");
        }
    }
}
