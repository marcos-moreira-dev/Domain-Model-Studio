package com.marcosmoreira.domainmodelstudio.application.importmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramKind;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNode;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNodeKind;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.validation.ValidationResult;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class ImportMarkdownModelResultSummaryTest {

    @Test
    void summaryUsesBehaviorVocabularyForUseCaseDiagrams() {
        BehaviorDiagramDocument document = new BehaviorDiagramDocument(
                "Casos de uso",
                "borrador",
                LocalDate.now(),
                BehaviorDiagramKind.UML_USE_CASE,
                List.of(new BehaviorNode("actor-secretaria", BehaviorNodeKind.ACTOR,
                        "Secretaría", "", "", "", 0)),
                List.of(),
                "");
        DiagramProject project = DiagramProject.blank("usecase", "Casos de uso", DiagramTypeId.UML_USE_CASE)
                .withBehaviorDiagram(document);
        ImportMarkdownModelResult result = new ImportMarkdownModelResult(
                Path.of("casos_uso.md"),
                project,
                ValidationResult.ok());

        assertEquals("casos_uso.md: 1 elementos / 0 relaciones", result.summary());
    }
}
