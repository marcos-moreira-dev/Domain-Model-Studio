package com.marcosmoreira.domainmodelstudio.application.importmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.layout.GenerateInitialChenLayoutUseCase;
import com.marcosmoreira.domainmodelstudio.application.layout.GenerateInitialCrowsFootLayoutUseCase;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramKind;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNode;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNodeKind;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramModel;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.er.AttributeElement;
import com.marcosmoreira.domainmodelstudio.domain.er.EntityElement;
import com.marcosmoreira.domainmodelstudio.domain.er.RelationshipElement;
import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Protege la importación para que solo el modelo conceptual reciba layout ER. */
class ImportedProjectVisualPreparationUseCaseTest {

    private final ImportedProjectVisualPreparationUseCase useCase = new ImportedProjectVisualPreparationUseCase(
            new GenerateInitialChenLayoutUseCase(),
            new GenerateInitialCrowsFootLayoutUseCase());

    @Test
    void conceptualImportReceivesChenLayoutByDefault() {
        DiagramProject imported = conceptualProject();

        DiagramProject prepared = useCase.prepare(imported);

        assertTrue(prepared.layouts().layoutFor(NotationType.CHEN).orElseThrow()
                .nodeFor(DiagramElementId.of("estudiante")).isPresent());
        assertEquals(NotationType.CHEN, prepared.metadata().activeNotation());
    }

    @Test
    void conceptualImportKeepsCrowsFootWhenMarkdownRequestedThatNotation() {
        DiagramProject imported = conceptualProject()
                .withMetadata(conceptualProject().metadata().withActiveNotation(NotationType.CROWS_FOOT));

        DiagramProject prepared = useCase.prepare(imported);

        assertEquals(NotationType.CROWS_FOOT, prepared.metadata().activeNotation());
        assertTrue(prepared.layouts().layoutFor(NotationType.CROWS_FOOT).orElseThrow()
                .nodeFor(DiagramElementId.of("estudiante")).isPresent());
    }

    @Test
    void behaviorImportMustNotReceiveConceptualChenLayout() {
        BehaviorDiagramDocument document = new BehaviorDiagramDocument(
                "Casos de uso",
                "borrador",
                LocalDate.now(),
                BehaviorDiagramKind.UML_USE_CASE,
                List.of(new BehaviorNode("actor-secretaria", BehaviorNodeKind.ACTOR,
                        "Secretaría", "", "", "", 0)),
                List.of(),
                "");
        DiagramProject imported = DiagramProject.blank("usecase", "Casos de uso", DiagramTypeId.UML_USE_CASE)
                .withBehaviorDiagram(document);

        DiagramProject prepared = useCase.prepare(imported);

        assertSame(imported, prepared);
        assertTrue(prepared.behaviorDiagram().isPresent());
        assertEquals(0, prepared.layouts().activeLayout().nodeCount());
    }

    private DiagramProject conceptualProject() {
        DiagramModel model = new DiagramModel(
                List.of(
                        EntityElement.strong("estudiante", "Estudiante",
                                List.of(AttributeElement.normal("estudiante_id", "pk id"))),
                        EntityElement.strong("seccion", "Sección",
                                List.of(AttributeElement.normal("seccion_id", "pk id")))
                ),
                List.of(RelationshipElement.between("matricula", "Matrícula", "estudiante", "seccion", "0..M", "1"))
        );
        return DiagramProject.blank("colegio", "Colegio", DiagramTypeId.CONCEPTUAL_MODEL).withModel(model);
    }
}
