package com.marcosmoreira.domainmodelstudio.application.datadictionary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.datadictionary.FieldConstraint;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.LogicalDataType;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramModel;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.er.AttributeElement;
import com.marcosmoreira.domainmodelstudio.domain.er.AttributeTag;
import com.marcosmoreira.domainmodelstudio.domain.er.EntityElement;
import com.marcosmoreira.domainmodelstudio.domain.er.RelationshipElement;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class GenerateDataDictionaryDraftFromConceptualModelUseCaseTest {

    private final GenerateDataDictionaryDraftFromConceptualModelUseCase useCase =
            new GenerateDataDictionaryDraftFromConceptualModelUseCase(
                    Clock.fixed(Instant.parse("2026-01-15T00:00:00Z"), ZoneOffset.UTC));

    @Test
    void shouldCreateEditableDictionaryDraftFromConceptualEntitiesAndAttributes() {
        DiagramProject project = conceptualProject();

        var document = useCase.generateDocument(project);

        assertEquals("Sistema escolar", document.projectName());
        assertEquals("borrador desde modelo conceptual", document.version());
        assertEquals(2, document.entityCount());
        assertEquals(3, document.fieldCount());
        assertTrue(document.hasIntroduction());
        assertTrue(document.notes().contains("Revisar relaciones conceptuales"));

        var estudiante = document.entityById("estudiante").orElseThrow();
        assertEquals("Estudiante", estudiante.displayName());
        assertEquals("estudiante", estudiante.technicalName());
        assertTrue(estudiante.notes().contains("Matricula"));

        var id = estudiante.fieldByName("id").orElseThrow();
        assertEquals(LogicalDataType.IDENTIFIER, id.logicalType());
        assertTrue(id.hasConstraint(FieldConstraint.PRIMARY_KEY));
        assertTrue(id.hasConstraint(FieldConstraint.REQUIRED));

        var correo = estudiante.fieldByName("correo electrónico").orElseThrow();
        assertEquals("correo_electronico", correo.technicalName());
        assertEquals(LogicalDataType.EMAIL, correo.logicalType());
        assertTrue(correo.hasConstraint(FieldConstraint.UNIQUE));
    }

    @Test
    void shouldAttachDictionaryDraftToProjectWithoutChangingConceptualModel() {
        DiagramProject project = conceptualProject();

        DiagramProject updated = useCase.attachDraft(project);

        assertEquals(project.model().entityCount(), updated.model().entityCount());
        assertTrue(updated.dataDictionary().isPresent());
        assertEquals(2, updated.dataDictionary().orElseThrow().entityCount());
    }

    @Test
    void shouldRejectEmptyConceptualModel() {
        DiagramProject empty = DiagramProject.blank("empty", "Vacío");

        assertThrows(IllegalArgumentException.class, () -> useCase.generateDocument(empty));
    }

    private static DiagramProject conceptualProject() {
        EntityElement estudiante = EntityElement.strong(
                "estudiante",
                "Estudiante",
                List.of(
                        AttributeElement.withTags("estudiante_id", "id", Set.of(AttributeTag.PRIMARY_KEY)),
                        AttributeElement.withTags("estudiante_correo", "correo electrónico", Set.of(AttributeTag.UNIQUE))
                )
        ).withDescription("Persona matriculada en la institución.");
        EntityElement matricula = EntityElement.strong(
                "matricula",
                "Matrícula",
                List.of(AttributeElement.withTags("matricula_fecha", "fecha", Set.of(AttributeTag.OPTIONAL)))
        );
        RelationshipElement relacion = RelationshipElement.between(
                "rel_matricula_estudiante",
                "Matricula",
                "estudiante",
                "matricula",
                "1",
                "0..M"
        );
        return DiagramProject.blank("uens", "Sistema escolar").withModel(
                new DiagramModel(List.of(estudiante, matricula), List.of(relacion))
        );
    }
}
