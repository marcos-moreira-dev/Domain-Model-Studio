package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.importmodel.MarkdownModelParsingException;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.er.AttributeTag;
import com.marcosmoreira.domainmodelstudio.domain.er.EntityKind;
import com.marcosmoreira.domainmodelstudio.domain.er.ParticipationType;
import com.marcosmoreira.domainmodelstudio.domain.er.RelationshipKind;
import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import org.junit.jupiter.api.Test;

class MarkdownDiagramParserRichTest {

    private final MarkdownDiagramParser parser = new MarkdownDiagramParser();

    @Test
    void parsesRichChenSemantics() throws Exception {
        DiagramProject project = parser.parse(richChenMarkdown(), "rich-chen.md");

        assertEquals("colegio_rico", project.metadata().id());
        assertEquals(NotationType.CHEN, project.metadata().activeNotation());
        assertEquals(2, project.model().entityCount());
        assertEquals(1, project.model().relationshipCount());

        var estudiante = project.model().entityById(com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId.of("estudiante")).orElseThrow();
        var matricula = project.model().entityById(com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId.of("matricula")).orElseThrow();
        var registra = project.model().relationshipById(com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId.of("registra")).orElseThrow();

        assertEquals(EntityKind.STRONG, estudiante.kind());
        assertEquals(EntityKind.WEAK, matricula.kind());
        assertTrue(estudiante.attributeById(com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId.of("estudiante_correo")).orElseThrow().hasTag(AttributeTag.OPTIONAL));
        assertTrue(estudiante.attributeById(com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId.of("estudiante_codigo")).orElseThrow().hasTag(AttributeTag.UNIQUE));
        assertTrue(estudiante.attributeById(com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId.of("estudiante_edad")).orElseThrow().hasTag(AttributeTag.DERIVED));
        assertTrue(matricula.attributeById(com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId.of("matricula_numero")).orElseThrow().hasTag(AttributeTag.PARTIAL_KEY));
        assertEquals("0..35", registra.toCardinality().displayText());
        assertEquals(RelationshipKind.IDENTIFYING, registra.kind());
        assertEquals(ParticipationType.TOTAL, registra.toParticipation());
    }

    @Test
    void parsesCrowsFootNotationAliasFromFrontMatter() throws Exception {
        DiagramProject project = parser.parse(richChenMarkdown().replace("notation: chen", "notation: pata_de_gallo"), "crow.md");

        assertEquals(NotationType.CROWS_FOOT, project.metadata().activeNotation());
        assertEquals(NotationType.CROWS_FOOT, project.layouts().activeNotation());
    }

    @Test
    void rejectsMarkdownWithoutEntitiesSection() {
        String markdown = """
                ---
                id: vacio
                title: Sin entidades
                ---

                # Relaciones

                ## Nada
                from: A
                to: B
                from_cardinality: 1
                to_cardinality: 1
                """;

        assertThrows(MarkdownModelParsingException.class, () -> parser.parse(markdown, "empty.md"));
    }

    @Test
    void rejectsInvalidCardinalityWithClearException() {
        String markdown = richChenMarkdown().replace("to_cardinality: 0..35", "to_cardinality: 0..X");

        assertThrows(MarkdownModelParsingException.class, () -> parser.parse(markdown, "bad-cardinality.md"));
    }

    @Test
    void rejectsIncompleteRelationship() {
        String markdown = richChenMarkdown().replace("to: Matricula", "to:   ");

        assertThrows(MarkdownModelParsingException.class, () -> parser.parse(markdown, "bad-relationship.md"));
    }

    private static String richChenMarkdown() {
        return """
                ---
                id: colegio_rico
                title: Modelo conceptual - Colegio rico
                notation: chen
                version: 1.0.0
                status: draft
                description: Ejemplo con semántica Chen rica.
                ---

                # Entidades

                ## Estudiante
                id: estudiante
                module: academico
                description: Persona inscrita en la institución.
                kind: strong

                - pk id
                - codigo [unique]
                - correo [optional]
                - edad [derived]
                - direccion [composite]
                - telefonos [multivalued]
                - documento_identidad [sensitive]

                ## Matricula
                id: matricula
                module: academico
                description: Registro dependiente del estudiante.
                kind: weak

                - numero [partial_key]
                - fecha
                - observacion [optional]

                # Relaciones

                ## Registra
                id: registra
                from: Estudiante
                to: Matricula
                from_cardinality: 1
                to_cardinality: 0..35
                kind: identifying
                from_participation: partial
                to_participation: total
                description: Un estudiante registra matrículas.
                """;
    }
}
