package com.marcosmoreira.domainmodelstudio.infrastructure.markdown.batch;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.importbatch.MarkdownBatchImportPolicy;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FileSystemMarkdownBatchReaderRecursiveTest {

    @TempDir
    Path tempDir;

    @Test
    void defaultPolicyShouldReadMarkdownFilesInsideNestedCategoryFolders() throws Exception {
        Path nested = tempDir.resolve("01-comercial").resolve("clientes");
        Files.createDirectories(nested);
        Files.writeString(nested.resolve("01_diccionario.md"), """
                ---
                diagram_type: "data-dictionary"
                importable: true
                sample_kind: "project"
                ---
                # Diccionario
                """);
        Files.writeString(tempDir.resolve("README.md"), "# paquete");

        var candidates = new FileSystemMarkdownBatchReader().readCandidates(tempDir, MarkdownBatchImportPolicy.defaultPolicy());

        assertTrue(candidates.stream().anyMatch(candidate -> candidate.sourceFile().endsWith("01_diccionario.md")),
                "El lector debe encontrar Markdown importables dentro de subcarpetas.");
        assertTrue(candidates.stream().anyMatch(candidate -> candidate.sourceFile().endsWith("README.md")),
                "El reporte debe conservar también omitidos para auditoría.");
    }
}
