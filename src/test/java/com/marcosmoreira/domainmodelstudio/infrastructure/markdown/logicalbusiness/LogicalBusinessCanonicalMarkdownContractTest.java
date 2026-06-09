package com.marcosmoreira.domainmodelstudio.infrastructure.markdown.logicalbusiness;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItemKind;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class LogicalBusinessCanonicalMarkdownContractTest {

    private static final Path TEMPLATE = Path.of("src", "main", "resources", "ai-resources",
            "official-markdown", "levantamiento-logico", "logical_business_intake_template.md");

    private final LogicalBusinessMarkdownParser parser = new LogicalBusinessMarkdownParser();
    private final LogicalBusinessMarkdownExporter exporter = new LogicalBusinessMarkdownExporter();

    @Test
    void templateContainsCanonicalContractAndAllSections() throws Exception {
        String markdown = Files.readString(TEMPLATE);

        assertTrue(markdown.contains("canonical_contract: \"logical-business-master-v1\""));
        for (LogicalBusinessCanonicalMarkdownContract.CanonicalSection section
                : LogicalBusinessCanonicalMarkdownContract.sections()) {
            assertTrue(markdown.contains("## " + section.heading()), section.heading());
        }
        assertFalse(markdown.contains("Marcos Moreira"));
    }

    @Test
    void parserKeepsMasterTemplateAsEmptyCanonicalDocument() throws Exception {
        LogicalBusinessDocument document = parser.parse(TEMPLATE);

        assertEquals(23, document.sections().size());
        assertTrue(document.items().isEmpty());
        assertTrue(document.sections().stream().anyMatch(section -> section.title().contains("Entidades candidatas")));
    }

    @Test
    void exporterEmitsCanonicalContractAndStableSectionOrder() throws Exception {
        LogicalBusinessDocument original = parser.parse(Path.of("src", "main", "resources", "ai-resources",
                "official-markdown", "levantamiento-logico", "logical_business_intake_optica_minimo.md"));

        String markdown = exporter.export(original);
        LogicalBusinessDocument reimported = parser.parse(markdown);

        assertTrue(markdown.contains("canonical_contract: \"logical-business-master-v1\""));
        assertTrue(indexOf(markdown, "## 13. Grafo lógico del negocio")
                < indexOf(markdown, "## 14. Entidades candidatas"));
        assertTrue(indexOf(markdown, "## 14. Entidades candidatas")
                < indexOf(markdown, "## 15. Estados y transiciones"));
        assertEquals(original.projectName(), reimported.projectName());
        assertTrue(reimported.itemById("RN-001").isPresent());
    }

    @Test
    void contractAcceptsAllCanonicalPrefixesWithoutTurningPlaceholdersIntoItems() {
        assertTrue(LogicalBusinessCanonicalMarkdownContract.isCanonicalId("SUP-001"));
        assertTrue(LogicalBusinessCanonicalMarkdownContract.isCanonicalId("CALC-001"));
        assertEquals(LogicalBusinessItemKind.SUPPORTING_ASSUMPTION,
                LogicalBusinessMarkdownIds.kindFor("SUP-001").orElseThrow());
        assertEquals(LogicalBusinessItemKind.CALCULATION,
                LogicalBusinessMarkdownIds.kindFor("CALC-001").orElseThrow());
        assertTrue(LogicalBusinessCanonicalMarkdownContract.isPlaceholderId("RN-XXX"));
        assertTrue(LogicalBusinessCanonicalMarkdownContract.isDocumentState("validado parcialmente"));
        assertTrue(LogicalBusinessCanonicalMarkdownContract.isItemState("pendiente de validar"));
        assertTrue(LogicalBusinessCanonicalMarkdownContract.isItemState("usable como fuente"));
    }

    private static int indexOf(String markdown, String token) {
        int index = markdown.indexOf(token);
        assertTrue(index >= 0, token);
        return index;
    }
}
