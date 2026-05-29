package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/** Guardarraíl Tanda 26/8B: la raíz solo puede conservar el README público de GitHub. */
class Tanda26RootMarkdownCleanupSourceTest {

    @Test
    void rootFolderMustNotContainLooseMarkdownFiles() throws IOException {
        try (Stream<Path> files = Files.list(Path.of("."))) {
            List<Path> markdownFiles = files
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith(".md"))
                    .filter(path -> !"README.md".equals(path.getFileName().toString()))
                    .toList();

            assertTrue(markdownFiles.isEmpty(),
                    "La documentación Markdown viva de raíz debe vivir en docs/raiz/. Solo README.md puede existir en la raíz por GitHub: " + markdownFiles);
        }
    }

    @Test
    void livingRootDocumentationMustBeGroupedUnderDocsRaiz() {
        assertTrue(Files.exists(Path.of("docs", "raiz", "README.md")));
        assertTrue(Files.exists(Path.of("docs", "raiz", "PLAN_TANDAS_RESTANTES.md")));
        assertTrue(Files.exists(Path.of("docs", "raiz", "TANDA_26_BENDPOINT_DRAG_Y_LIMPIEZA_RAIZ.md")));
    }
}
