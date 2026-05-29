package com.marcosmoreira.domainmodelstudio.infrastructure.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/** Protege el espejo entre los ejemplos públicos y los recursos IA oficiales. */
class OfficialMarkdownExamplesMirrorSyncTest {

    private static final Path EXAMPLES_DIAGRAMS = Path.of("examples", "markdown", "diagramas");
    private static final Path AI_DIAGRAMS = Path.of("src", "main", "resources", "ai-resources", "official-markdown", "diagramas");
    private static final Path EXAMPLES_LOGICAL_INTAKE = Path.of(
            "examples", "markdown", "levantamiento-logico", "logical_business_intake_uens_gordito.md");
    private static final Path AI_LOGICAL_INTAKE = Path.of(
            "src", "main", "resources", "ai-resources", "official-markdown", "levantamiento-logico",
            "logical_business_intake_uens_gordito.md");

    @Test
    void diagramExamplesShouldMirrorAiResourcesByFileNameAndContent() throws Exception {
        Set<String> publicNames = markdownNames(EXAMPLES_DIAGRAMS);
        Set<String> aiNames = markdownNames(AI_DIAGRAMS);

        assertEquals(aiNames, publicNames,
                "Los ejemplos públicos y los recursos IA oficiales deben tener los mismos Markdown de diagramas.");

        for (String name : publicNames) {
            assertEquals(
                    normalized(Files.readString(AI_DIAGRAMS.resolve(name), StandardCharsets.UTF_8)),
                    normalized(Files.readString(EXAMPLES_DIAGRAMS.resolve(name), StandardCharsets.UTF_8)),
                    "Contenido desincronizado entre examples/markdown y ai-resources: " + name);
        }
    }

    @Test
    void uensLogicalBusinessIntakeShouldMirrorAiResource() throws Exception {
        assertTrue(Files.isRegularFile(EXAMPLES_LOGICAL_INTAKE));
        assertTrue(Files.isRegularFile(AI_LOGICAL_INTAKE));
        assertEquals(
                normalized(Files.readString(AI_LOGICAL_INTAKE, StandardCharsets.UTF_8)),
                normalized(Files.readString(EXAMPLES_LOGICAL_INTAKE, StandardCharsets.UTF_8)),
                "El levantamiento lógico UENS debe estar sincronizado entre ejemplo público y recurso IA.");
    }

    private static Set<String> markdownNames(Path dir) throws IOException {
        try (Stream<Path> files = Files.list(dir)) {
            return files
                    .filter(path -> path.getFileName().toString().endsWith(".md"))
                    .filter(path -> !"README.md".equals(path.getFileName().toString()))
                    .map(path -> path.getFileName().toString())
                    .collect(Collectors.toSet());
        }
    }

    private static String normalized(String value) {
        return value.replace("\r\n", "\n").stripTrailing();
    }
}
