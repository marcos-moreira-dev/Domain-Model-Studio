package com.marcosmoreira.domainmodelstudio.application.importmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.validation.DiagramProjectValidator;
import com.marcosmoreira.domainmodelstudio.infrastructure.markdown.MarkdownDiagramParser;
import org.junit.jupiter.api.Test;

class ImportMarkdownModelUseCaseTest {

    @Test
    void importsAndValidatesMarkdownContent() throws Exception {
        ImportMarkdownModelUseCase useCase = new ImportMarkdownModelUseCase(
                new MarkdownDiagramParser(),
                new DiagramProjectValidator()
        );

        ImportMarkdownModelResult result = useCase.importContent(markdown(), "inline.md");

        assertEquals(2, result.project().model().entityCount());
        assertEquals(1, result.project().model().relationshipCount());
        assertTrue(result.validationResult().isValid());
    }

    private static String markdown() {
        return """
                ---
                id: colegio_v1
                title: Modelo conceptual - Colegio
                notation: chen
                ---

                # Entidades

                ## Estudiante
                id: estudiante
                - pk id
                - nombres

                ## Seccion
                id: seccion
                - pk id
                - grado

                # Relaciones

                ## Agrupa
                id: agrupa
                from: Seccion
                to: Estudiante
                from_cardinality: 1
                to_cardinality: 0..M
                """;
    }
}
