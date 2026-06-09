package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class LogicalBusinessMarkdownParserSourceTest {

    private static final Path PACKAGE = Path.of("src", "main", "java", "com", "marcosmoreira",
            "domainmodelstudio", "infrastructure", "markdown", "logicalbusiness");

    @Test
    void parserKeepsResponsibilitiesSeparatedBeforeUiAndPersistence() throws IOException {
        assertContains("LogicalBusinessMarkdownParser.java", "LogicalBusinessMarkdownScanner");
        assertContains("LogicalBusinessMarkdownParser.java", "LogicalBusinessMarkdownItemFactory");
        assertContains("LogicalBusinessMarkdownParser.java", "LogicalBusinessEntityFactory");
        assertContains("package-info.java", "No conoce JavaFX, JSON, .dms, catálogo visible ni UI");
        assertFileShorterThan("LogicalBusinessMarkdownParser.java", 120);
        assertFileShorterThan("LogicalBusinessMarkdownScanner.java", 220);
        assertFileShorterThan("LogicalBusinessEntityFactory.java", 260);
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
