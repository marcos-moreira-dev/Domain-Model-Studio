package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Guardarraíl de Tanda 32: frontmatter/cuerpo Markdown se separan en una utilidad común. */
class MarkdownImportDocumentRefactorSourceTest {

    private static final Path MARKDOWN_PACKAGE = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/markdown");
    private static final Path TANDA_DOC = Path.of("docs/desarrollo/TANDA_032_REFACTOR_MARKDOWN_IMPORT_DOCUMENT.md");

    private static final List<String> IMPORT_PARSERS = List.of(
            "ArchitectureMarkdownParser.java",
            "BehaviorMarkdownParser.java",
            "DataDictionaryMarkdownParser.java",
            "DiagramMarkdownImportDispatcher.java",
            "FreeGraphMarkdownParser.java",
            "LogicalBusinessGraphMarkdownParser.java",
            "MarkdownDiagramParser.java",
            "ModuleMapMarkdownParser.java",
            "RolesPermissionsMarkdownParser.java",
            "ScreenFlowMarkdownParser.java",
            "UmlClassMarkdownParser.java",
            "WireframeMarkdownParser.java"
    );

    @Test
    void importDocumentShouldOwnFrontMatterAndBodySplit() throws IOException {
        String source = text(MARKDOWN_PACKAGE.resolve("MarkdownImportDocument.java"));

        assertTrue(source.contains("final class MarkdownImportDocument"));
        assertTrue(source.contains("static MarkdownImportDocument parse(String markdownContent)"));
        assertTrue(source.contains("MarkdownFrontMatter frontMatter"));
        assertTrue(source.contains("String body"));
        assertTrue(source.contains("bodyAfter"));
    }

    @Test
    void importParsersShouldDelegateFrontMatterSplitToSharedUtility() throws IOException {
        for (String fileName : IMPORT_PARSERS) {
            String source = text(MARKDOWN_PACKAGE.resolve(fileName));
            assertTrue(source.contains("MarkdownImportDocument.parse(markdownContent)"),
                    fileName + " debe usar MarkdownImportDocument para leer frontmatter/cuerpo.");
            assertFalse(source.contains("private static MarkdownFrontMatter readFrontMatter"),
                    fileName + " no debe duplicar readFrontMatter local.");
            assertFalse(source.contains("private MarkdownFrontMatter readFrontMatter"),
                    fileName + " no debe duplicar readFrontMatter local.");
            assertFalse(source.contains("private static String stripFrontMatter"),
                    fileName + " no debe duplicar stripFrontMatter local.");
        }
    }

    @Test
    void packageDocumentationAndTandaShouldExplainNoGrammarChange() throws IOException {
        String packageInfo = text(MARKDOWN_PACKAGE.resolve("package-info.java"));
        String tanda = text(TANDA_DOC);

        assertTrue(packageInfo.contains("MarkdownImportDocument"));
        assertTrue(packageInfo.contains("sin cambiar las gramáticas"));
        assertTrue(tanda.contains("sin cambiar gramáticas"));
        assertTrue(tanda.contains("sin cambiar ejemplos oficiales"));
        assertTrue(tanda.contains("sin cambiar comportamiento visible"));
    }

    private static String text(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
