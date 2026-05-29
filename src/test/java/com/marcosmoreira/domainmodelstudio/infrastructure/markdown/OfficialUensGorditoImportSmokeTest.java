package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.catalog.DefaultDiagramTypeRegistry;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/** Smoke de importación para los ejemplos oficiales UENS completos. */
class OfficialUensGorditoImportSmokeTest {

    private static final Path AI_DIAGRAMS = Path.of(
            "src", "main", "resources", "ai-resources", "official-markdown", "diagramas");
    private static final Path AI_LOGICAL_INTAKE = Path.of(
            "src", "main", "resources", "ai-resources", "official-markdown", "levantamiento-logico",
            "logical_business_intake_uens_gordito.md");

    private final DiagramMarkdownImportDispatcher dispatcher = new DiagramMarkdownImportDispatcher(
            new DefaultDiagramTypeRegistry()
    );

    @Test
    void everyUensGorditoExampleShouldImportAsEditableProject() throws Exception {
        List<Path> examples = uensImportableExamples();

        assertTrue(examples.size() >= 19, "Deben existir ejemplos UENS gorditos para los tipos oficiales actuales.");
        for (Path example : examples) {
            assertDoesNotThrow(() -> dispatcher.parse(example), "Debe importar ejemplo UENS: " + example);
        }
    }

    private static List<Path> uensImportableExamples() throws IOException {
        try (Stream<Path> diagrams = Files.list(AI_DIAGRAMS)) {
            List<Path> diagramExamples = diagrams
                    .filter(path -> path.getFileName().toString().endsWith(".md"))
                    .filter(path -> path.getFileName().toString().contains("uens_gordito"))
                    .toList();
            return Stream.concat(diagramExamples.stream(), Stream.of(AI_LOGICAL_INTAKE)).toList();
        }
    }
}
