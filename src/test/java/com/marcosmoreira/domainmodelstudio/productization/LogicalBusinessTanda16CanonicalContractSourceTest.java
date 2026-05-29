package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl T16: el contrato lógico maestro debe enseñar y modelar SUP/CALC/ATR/REL sin aliases duplicados. */
class LogicalBusinessTanda16CanonicalContractSourceTest {

    private static final Path ITEM_KIND = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/domain/logicalbusiness/LogicalBusinessItemKind.java");
    private static final Path CONTRACT = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/markdown/logicalbusiness/LogicalBusinessCanonicalMarkdownContract.java");
    private static final Path TEMPLATE = Path.of(
            "src/main/resources/ai-resources/official-markdown/levantamiento-logico/logical_business_intake_template.md");
    private static final Path MINIMAL = Path.of(
            "src/main/resources/ai-resources/official-markdown/levantamiento-logico/logical_business_intake_optica_minimo.md");
    private static final Path GORDITO = Path.of(
            "src/main/resources/ai-resources/official-markdown/levantamiento-logico/logical_business_intake_optica_gordito.md");

    @Test
    void domainEnumAndCanonicalContractShouldSupportSupplementaryLogicalPrefixes() throws IOException {
        String itemKind = read(ITEM_KIND);
        String contract = read(CONTRACT);

        assertTrue(itemKind.contains("SUPPORTING_ASSUMPTION(\"SUP\")"));
        assertTrue(itemKind.contains("CALCULATION(\"CALC\")"));
        assertTrue(contract.contains("Map.entry(\"SUP\", 2)"));
        assertTrue(contract.contains("Map.entry(\"CALC\", 16)"));
        assertTrue(contract.contains("SUP|CALC"));
    }

    @Test
    void officialTemplateAndExamplesShouldTeachImportableCandidateEntitiesAttributesAndRelations() throws IOException {
        String template = read(TEMPLATE);
        String minimal = read(MINIMAL);
        String gordito = read(GORDITO);

        assertTrue(template.contains("### SUP-XXX"));
        assertTrue(template.contains("### ATR-XXX"));
        assertTrue(template.contains("### REL-XXX"));
        assertTrue(template.contains("### CALC-XXX"));
        assertTrue(minimal.contains("### ENT-001"));
        assertTrue(minimal.contains("### ATR-001"));
        assertTrue(minimal.contains("### REL-001"));
        assertTrue(gordito.contains("SUP-001"));
        assertTrue(gordito.contains("### ATR-001"));
        assertTrue(gordito.contains("### REL-001"));
        assertTrue(gordito.contains("### CALC-001"));
    }

    private static String read(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
