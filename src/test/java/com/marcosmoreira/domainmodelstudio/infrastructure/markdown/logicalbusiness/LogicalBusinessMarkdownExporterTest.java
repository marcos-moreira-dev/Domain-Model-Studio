package com.marcosmoreira.domainmodelstudio.infrastructure.markdown.logicalbusiness;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessAttributeCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessEntityCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItem;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItemKind;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItemStatus;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessRelationshipCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessSection;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

class LogicalBusinessMarkdownExporterTest {

    private static final Path RESOURCES = Path.of("src", "main", "resources", "ai-resources",
            "official-markdown", "levantamiento-logico");

    private final LogicalBusinessMarkdownParser parser = new LogicalBusinessMarkdownParser();
    private final LogicalBusinessMarkdownExporter exporter = new LogicalBusinessMarkdownExporter();

    @Test
    void exportsMinimumExampleAsReimportableMarkdown() throws Exception {
        LogicalBusinessDocument original = parser.parse(RESOURCES.resolve("logical_business_intake_optica_minimo.md"));

        String markdown = exporter.export(original);
        LogicalBusinessDocument reimported = parser.parse(markdown);

        assertTrue(markdown.contains("## 0. Portada lógica del levantamiento"));
        assertTrue(markdown.contains("### RN-001"));
        assertTrue(markdown.contains("### ACC-001"));
        assertEquals(original.projectName(), reimported.projectName());
        assertEquals(original.itemById("RN-001").orElseThrow().title(),
                reimported.itemById("RN-001").orElseThrow().title());
        assertTrue(reimported.itemById("ACC-001").orElseThrow().referenceIds().contains("PRE-001"));
        assertFalse(reimported.pendingQuestions().isEmpty());
    }

    @Test
    void exportsProgrammaticEntitiesAttributesAndRelationships() {
        LogicalBusinessDocument original = documentWithEntityCatalog();

        String markdown = exporter.export(original);
        LogicalBusinessDocument reimported = parser.parse(markdown);

        assertTrue(markdown.contains("## 14. Entidades candidatas"));
        assertTrue(markdown.contains("Fuente lógica"));
        assertFalse(markdown.contains("Fuente de derivación"));
        assertTrue(markdown.contains("### Atributos candidatos"));
        assertTrue(markdown.contains("### Relaciones candidatas"));
        assertEquals(2, reimported.entityCandidates().size());
        assertEquals("saldoPendiente", reimported.entityById("ENT-001").orElseThrow().attributes().get(0).name());
        assertEquals(1, reimported.entityById("ENT-001").orElseThrow().relationships().size());
    }

    @Test
    void doesNotExportEntityCatalogTwiceWhenSectionsReferenceThoseItems() throws Exception {
        LogicalBusinessDocument original = parser.parse(RESOURCES.resolve("logical_business_intake_optica_gordito.md"));

        String markdown = exporter.export(original);

        assertEquals(1, occurrences(markdown, "## 14. Entidades candidatas"));
        assertEquals(1, occurrences(markdown, "### ENT-003"));
    }

    private LogicalBusinessDocument documentWithEntityCatalog() {
        LogicalBusinessItem rule = LogicalBusinessItem.of("RN-001", LogicalBusinessItemKind.RULE,
                "Toda cuenta por cobrar tiene origen");
        LogicalBusinessAttributeCandidate attribute = new LogicalBusinessAttributeCandidate(
                "ATR-001", "ENT-001", "saldoPendiente", "Permite saber cuánto queda por cobrar.",
                "decimal", true, "montoTotal - pagosAplicados", "saldo financiero falso",
                List.of("RN-001"), List.of("RN-001"), List.of());
        LogicalBusinessRelationshipCandidate relationship = new LogicalBusinessRelationshipCandidate(
                "REL-001", "ENT-001", "ENT-002", "Cuenta por cobrar — Pago recibido", "1 a muchos",
                "Los pagos se aplican a una cuenta por cobrar.", List.of("ACC-001"));
        LogicalBusinessEntityCandidate account = new LogicalBusinessEntityCandidate(
                "ENT-001", "CuentaPorCobrar", LogicalBusinessItemStatus.DRAFT,
                "La acción ACC-001 necesita actualizar el saldo esperado.", List.of(attribute),
                List.of(relationship), List.of("ACC-001"), List.of("RN-001"), List.of(),
                List.of(), List.of("ACC-001"), List.of(), "riesgo financiero");
        LogicalBusinessEntityCandidate payment = new LogicalBusinessEntityCandidate(
                "ENT-002", "PagoRecibido", LogicalBusinessItemStatus.DRAFT,
                "La acción ACC-001 registra dinero recibido.", List.of(), List.of(),
                List.of("ACC-001"), List.of(), List.of(), List.of("ACC-001"), List.of(), List.of(), "");
        return new LogicalBusinessDocument("Sistema de cobros", "v0.1", null, null, "prueba",
                List.of(LogicalBusinessSection.of("sec-6", "6. Reglas lógicas del negocio").withItemIds(List.of("RN-001"))),
                List.of(rule), List.of(account, payment), List.of(), null, "");
    }

    private int occurrences(String text, String token) {
        int count = 0;
        int index = 0;
        while ((index = text.indexOf(token, index)) >= 0) {
            count++;
            index += token.length();
        }
        return count;
    }
}
