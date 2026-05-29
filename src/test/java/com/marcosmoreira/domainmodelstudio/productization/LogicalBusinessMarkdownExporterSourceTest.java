package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class LogicalBusinessMarkdownExporterSourceTest {

    private static final Path PACKAGE = Path.of("src", "main", "java", "com", "marcosmoreira",
            "domainmodelstudio", "infrastructure", "markdown", "logicalbusiness");

    @Test
    void exporterKeepsRoundTripResponsibilitiesSeparated() throws IOException {
        assertContains("LogicalBusinessMarkdownExporter.java", "LogicalBusinessMarkdownDocumentWriter");
        assertContains("LogicalBusinessMarkdownDocumentWriter.java", "LogicalBusinessMarkdownItemWriter");
        assertContains("LogicalBusinessMarkdownDocumentWriter.java", "LogicalBusinessMarkdownEntityWriter");
        assertContains("LogicalBusinessMarkdownDocumentWriter.java", "LogicalBusinessCanonicalMarkdownContract.sections()");
        assertContains("LogicalBusinessMarkdownEntityWriter.java", "## 14. Entidades candidatas");
        assertContains("LogicalBusinessMarkdownEntityWriter.java", "Fuente lógica");
        assertContains("LogicalBusinessMarkdownEntityWriter.java", "### Atributos candidatos");
        assertContains("LogicalBusinessMarkdownEntityWriter.java", "### Relaciones candidatas");
        assertContains("package-info.java", "Markdown reimportable");
        assertFileShorterThan("LogicalBusinessMarkdownExporter.java", 80);
        assertFileShorterThan("LogicalBusinessMarkdownDocumentWriter.java", 220);
        assertFileShorterThan("LogicalBusinessMarkdownItemWriter.java", 120);
        assertFileShorterThan("LogicalBusinessMarkdownEntityWriter.java", 220);
    }

    private static void assertContains(String fileName, String token) throws IOException {
        String source = Files.readString(PACKAGE.resolve(fileName), StandardCharsets.UTF_8);
        assertTrue(source.contains(token), fileName + " debe contener: " + token);
    }

    private static void assertFileShorterThan(String fileName, int maxLines) throws IOException {
        long lines = Files.readAllLines(PACKAGE.resolve(fileName), StandardCharsets.UTF_8).size();
        assertTrue(lines < maxLines, fileName + " debe seguir siendo pequeño; líneas: " + lines);
    }
}
