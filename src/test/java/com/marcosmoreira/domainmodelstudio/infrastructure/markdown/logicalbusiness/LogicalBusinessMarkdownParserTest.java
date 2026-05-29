package com.marcosmoreira.domainmodelstudio.infrastructure.markdown.logicalbusiness;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocumentStatus;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItemKind;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessQuestionPriority;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

class LogicalBusinessMarkdownParserTest {

    private static final Path RESOURCES = Path.of("src", "main", "resources", "ai-resources",
            "official-markdown", "levantamiento-logico");

    private final LogicalBusinessMarkdownParser parser = new LogicalBusinessMarkdownParser();

    @Test
    void parsesCanonicalTemplateAsStructuredEmptySource() throws Exception {
        LogicalBusinessDocument document = parser.parse(RESOURCES.resolve("logical_business_intake_template.md"));

        assertTrue(document.projectName().contains("Levantamiento lógico"));
        assertEquals(LogicalBusinessDocumentStatus.DRAFT, document.documentStatus());
        assertTrue(document.sections().size() >= 20);
        assertTrue(document.items().isEmpty(), "Los IDs RN-XXX de la plantilla son placeholders y no deben crear items reales.");
    }

    @Test
    void parsesMinimumOpticaExampleWithRulesActionsAndQuestions() throws Exception {
        LogicalBusinessDocument document = parser.parse(RESOURCES.resolve("logical_business_intake_optica_minimo.md"));

        assertTrue(document.projectName().contains("Sistema administrativo"));
        assertFalse(document.itemsByKind(LogicalBusinessItemKind.RULE).isEmpty());
        assertFalse(document.itemsByKind(LogicalBusinessItemKind.ACTION).isEmpty());
        assertTrue(document.itemById("RN-001").orElseThrow().description().contains("orden"));
        assertTrue(document.itemById("ACC-001").orElseThrow().referenceIds().contains("PRE-001"));
        assertEquals(2, document.pendingQuestions().size());
        assertEquals(LogicalBusinessQuestionPriority.HIGH, document.pendingQuestions().get(0).priority());
    }

    @Test
    void parsesGorditoExampleEntitiesAndReportsFromTables() throws Exception {
        LogicalBusinessDocument document = parser.parse(RESOURCES.resolve("logical_business_intake_optica_gordito.md"));

        assertTrue(document.itemsByKind(LogicalBusinessItemKind.ENTITY).size() >= 6);
        assertTrue(document.itemsByKind(LogicalBusinessItemKind.REPORT).size() >= 5);
        assertTrue(document.itemsByKind(LogicalBusinessItemKind.SUPPORTING_ASSUMPTION).size() >= 4);
        assertTrue(document.itemById("SUP-001").orElseThrow().description().contains("recetas externas"));
        assertTrue(document.itemById("SUP-001").orElseThrow().source().contains("entrevista"));
        assertTrue(document.itemsByKind(LogicalBusinessItemKind.CALCULATION).size() >= 1);
        assertTrue(document.itemsByKind(LogicalBusinessItemKind.ACTOR).size() >= 1);
        assertTrue(document.itemsByKind(LogicalBusinessItemKind.EVIDENCE).size() >= 1);
        assertTrue(document.itemsByKind(LogicalBusinessItemKind.STATE).size() >= 2);
        assertTrue(document.itemsByKind(LogicalBusinessItemKind.CONCEPT).size() >= 1);
        assertTrue(document.entityById("ENT-003").orElseThrow().logicalJustification()
                .contains("expediente operativo"));
        assertTrue(document.entityById("ENT-003").orElseThrow().attributes().stream()
                .anyMatch(attribute -> attribute.id().equals("ATR-002")));
        assertTrue(document.entityById("ENT-003").orElseThrow().relationships().stream()
                .anyMatch(relationship -> relationship.id().equals("REL-003")));
        assertTrue(document.itemById("REP-001").orElseThrow().title().contains("Órdenes pendientes"));
        assertTrue(document.itemById("CALC-001").orElseThrow().description().contains("saldo"));
    }

    @Test
    void parsesEntityAttributeAndRelationshipCandidatesFromCanonicalSyntax() {
        String markdown = """
                ## 14. Entidades candidatas
                ### ENT-001 — Cliente
                - Justificación lógica: La acción ACC-001 necesita identificar al cliente.
                - Fuente lógica: ACC-001.
                - Atributos candidatos:
                  - ATR-001.

                ### Atributos candidatos
                ### ATR-001 — Cliente.nombre
                - Pertenece a: ENT-001.
                - Tipo tentativo: texto.
                - Razón operativa: permite identificar al cliente en la atención.
                - ¿Es calculado?: no.

                ### Relaciones candidatas
                ### ENT-002 — OrdenOptica
                - Justificación lógica: ACC-002 crea el expediente operativo.
                - Sustento lógico: ACC-002.

                ### REL-001 — Cliente — OrdenOptica
                - Entidad origen: ENT-001.
                - Entidad destino: ENT-002.
                - Cardinalidad tentativa: 1 a muchos.
                - Justificación lógica: RN-001 obliga que toda orden pertenezca a cliente.
                """;

        LogicalBusinessDocument document = parser.parse(markdown);

        assertEquals(2, document.entityCandidates().size());
        assertEquals(1, document.entityById("ENT-001").orElseThrow().attributes().size());
        assertEquals("nombre", document.entityById("ENT-001").orElseThrow().attributes().get(0).name());
        assertEquals(1, document.entityById("ENT-001").orElseThrow().relationships().size());
    }

    @Test
    void parserAcceptsLegacyDerivationLabelsForCompatibility() {
        String markdown = """
                ## 14. Entidades derivadas
                ### ENT-001 — Cliente
                - Justificación lógica: La acción ACC-001 necesita identificar al cliente.
                - Fuente de derivación: ACC-001.

                ### ENT-002 — Orden
                - Justificación lógica: ACC-002 crea el expediente operativo.
                - Derivada de: ACC-002.

                ### REL-001 — Cliente — Orden
                - Entidad origen: ENT-001.
                - Entidad destino: ENT-002.
                - Cardinalidad tentativa: 1 a muchos.
                - Justificación lógica: RN-001 obliga que toda orden pertenezca a cliente.
                - Derivado de: RN-001.
                """;

        LogicalBusinessDocument document = parser.parse(markdown);

        assertEquals(List.of("ACC-001"), document.entityById("ENT-001").orElseThrow().sourceReferences());
        assertEquals(List.of("ACC-002"), document.entityById("ENT-002").orElseThrow().sourceReferences());
        assertEquals(List.of("RN-001"), document.entityById("ENT-001").orElseThrow().relationships().get(0).sourceReferences());
    }

}
