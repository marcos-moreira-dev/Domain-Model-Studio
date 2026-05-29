package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

/** Guardarraíl: evita que la cascada CSS vuelva a crecer sin control. */
class CssCascadeBudgetSourceTest {

    private static final Path CSS = Path.of("src", "main", "resources", "css");

    @Test
    void cssShouldStayWithinCurrentCascadeBudget() throws IOException {
        List<Path> cssFiles;
        try (var stream = Files.list(CSS)) {
            cssFiles = stream
                    .filter(path -> path.getFileName().toString().endsWith(".css"))
                    .sorted()
                    .collect(Collectors.toList());
        }

        int totalLines = 0;
        for (Path cssFile : cssFiles) {
            int lines = Files.readAllLines(cssFile, StandardCharsets.UTF_8).size();
            totalLines += lines;
            assertTrue(lines <= 600,
                    () -> cssFile.getFileName() + " supera 600 líneas; dividir antes de seguir aumentando cascada.");
        }
        int finalTotalLines = totalLines;
        assertTrue(finalTotalLines <= 8200,
                () -> "La cascada CSS total supera el presupuesto de 8200 líneas: " + finalTotalLines);
    }

    @Test
    void appLightShouldNotBecomeAnUnboundedImportHub() throws IOException {
        String appLight = Files.readString(CSS.resolve("app-light.css"), StandardCharsets.UTF_8);
        long imports = appLight.lines().filter(line -> line.strip().startsWith("@import")).count();
        assertTrue(imports <= 55,
                () -> "app-light.css tiene demasiados imports; valor actual: " + imports);
    }

    @Test
    void cssReadmeShouldExplainSharedClassPreference() throws IOException {
        String readme = Files.readString(CSS.resolve("README.md"), StandardCharsets.UTF_8);
        assertTrue(readme.contains("clases compartidas"));
        assertTrue(readme.contains("sin radius ornamental"));
    }
}
