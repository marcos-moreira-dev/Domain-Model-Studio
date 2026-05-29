package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl Tanda 22: gramática Markdown canónica sin lenguaje de derivación como promesa visible. */
class LogicalBusinessCanonicalMarkdownGrammarSourceTest {

    private static final Path CONTRACT = Path.of("docs/tecnico/CONTRATO_MARKDOWN_LEVANTAMIENTO_LOGICO.md");
    private static final Path CANONICAL_CONTRACT = Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/"
            + "infrastructure/markdown/logicalbusiness/LogicalBusinessCanonicalMarkdownContract.java");
    private static final Path ENTITY_WRITER = Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/"
            + "infrastructure/markdown/logicalbusiness/LogicalBusinessMarkdownEntityWriter.java");
    private static final Path ENTITY_FACTORY = Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/"
            + "infrastructure/markdown/logicalbusiness/LogicalBusinessEntityFactory.java");
    private static final Path OFFICIAL_TEMPLATE = Path.of("src/main/resources/ai-resources/official-markdown/"
            + "levantamiento-logico/logical_business_intake_template.md");
    private static final Path PUBLIC_TEMPLATE = Path.of("examples/markdown/plantillas/logical_business_intake.md");

    @Test
    void sectionFourteenShouldUseCandidateLanguageInContractAndExporter() throws IOException {
        String docsContract = read(CONTRACT);
        String javaContract = read(CANONICAL_CONTRACT);
        String entityWriter = read(ENTITY_WRITER);

        assertTrue(docsContract.contains("14. Entidades candidatas"));
        assertTrue(javaContract.contains("section(14, \"Entidades candidatas\")"));
        assertTrue(entityWriter.contains("## 14. Entidades candidatas"));
        assertTrue(entityWriter.contains("Fuente lógica"));
        assertFalse(entityWriter.contains("Fuente de derivación"));
    }

    @Test
    void parserShouldAcceptLegacyLabelsWhileTemplatesTeachNewLabels() throws IOException {
        String entityFactory = read(ENTITY_FACTORY);
        String official = read(OFFICIAL_TEMPLATE);
        String publicTemplate = read(PUBLIC_TEMPLATE);

        assertTrue(entityFactory.contains("fuente logica"));
        assertTrue(entityFactory.contains("sustento logico"));
        assertTrue(entityFactory.contains("justificada por"));
        assertTrue(entityFactory.contains("derivada de"));
        assertTrue(entityFactory.contains("fuente de derivacion"));

        assertTrue(official.contains("## 14. Entidades candidatas"));
        assertTrue(official.contains("Fuente lógica"));
        assertTrue(publicTemplate.contains("## 14. Entidades candidatas"));
        assertTrue(publicTemplate.contains("Fuente lógica"));
        assertFalse(official.contains("## 14. Entidades derivadas"));
        assertFalse(publicTemplate.contains("## 14. Entidades derivadas"));
    }

    @Test
    void templatesShouldAvoidAutomaticDerivationPromises() throws IOException {
        String official = read(OFFICIAL_TEMPLATE);
        String publicTemplate = read(PUBLIC_TEMPLATE);
        String joined = official + "\n" + publicTemplate;

        assertTrue(joined.contains("fuente lógica canónica"));
        assertTrue(joined.contains("otros artefactos pueden reutilizar sus IDs"));
        assertTrue(joined.contains("sin generación automática ni sincronización entre proyectos"));
        assertFalse(joined.contains("fuente madre"));
        assertFalse(joined.contains("diagramas son vistas derivadas"));
        assertFalse(joined.contains("Es la fuente lógica desde la cual se generan"));
        assertFalse(joined.contains("Listo para derivar modelos"));
    }

    private static String read(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
