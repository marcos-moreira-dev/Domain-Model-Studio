package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl de Tanda 15: el Markdown canónico manda sobre la UI. */
class LogicalBusinessCanonicalMarkdownSourceTest {

    @Test
    void officialTemplateAndPublicTemplateShareCanonicalContract() throws IOException {
        String official = read("src/main/resources/ai-resources/official-markdown/levantamiento-logico/logical_business_intake_template.md");
        String publicTemplate = read("examples/markdown/plantillas/logical_business_intake.md");

        assertTrue(official.contains("canonical_contract: \"logical-business-master-v1\""));
        assertTrue(publicTemplate.contains("canonical_contract: \"logical-business-master-v1\""));
        assertTrue(official.contains("## 22. Cierre del documento"));
        assertTrue(publicTemplate.contains("## 22. Cierre del documento"));
        assertFalse(official.contains("Marcos Moreira"));
        assertFalse(publicTemplate.contains("Marcos Moreira"));
    }

    @Test
    void contractDocumentationExplainsHybridGptWorkflow() throws IOException {
        String contract = read("docs/tecnico/CONTRATO_MARKDOWN_LEVANTAMIENTO_LOGICO.md");
        String tanda = read("docs/desarrollo/TANDA_15_CONTRATO_MARKDOWN_CANONICO.md");
        String decisions = read("docs/producto/decisiones_producto.md");

        assertTrue(contract.contains("GPT rellena la plantilla maestra"));
        assertTrue(contract.contains("Domain Model Studio importa el Markdown canónico"));
        assertTrue(contract.contains("PREFIJO-001"));
        assertTrue(contract.contains("SUP, CALC"));
        assertTrue(tanda.contains("plantilla escrita en piedra"));
        assertTrue(decisions.contains("logical-business-master-v1"));
    }

    @Test
    void codeHasTechnicalContractAndExporterUsesIt() throws IOException {
        String contract = read("src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/markdown/logicalbusiness/LogicalBusinessCanonicalMarkdownContract.java");
        String writer = read("src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/markdown/logicalbusiness/LogicalBusinessMarkdownDocumentWriter.java");
        String ids = read("src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/markdown/logicalbusiness/LogicalBusinessMarkdownIds.java");

        assertTrue(contract.contains("CONTRACT_ID"));
        assertTrue(contract.contains("Portada lógica del levantamiento"));
        assertTrue(contract.contains("Uso como fuente para otros artefactos"));
        assertTrue(writer.contains("LogicalBusinessCanonicalMarkdownContract.sections()"));
        assertTrue(writer.contains("canonical_contract"));
        assertTrue(ids.contains("SUP|CALC"));
    }

    private static String read(String relativePath) throws IOException {
        return Files.readString(Path.of(relativePath), StandardCharsets.UTF_8);
    }
}
