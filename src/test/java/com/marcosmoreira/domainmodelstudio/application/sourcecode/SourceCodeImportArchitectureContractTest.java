package com.marcosmoreira.domainmodelstudio.application.sourcecode;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class SourceCodeImportArchitectureContractTest {

    @Test
    void sourceCodeImportShouldRemainParallelToUmlMarkdownImport() throws Exception {
        Path dispatcher = Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/markdown/DiagramMarkdownImportDispatcher.java");
        Path sourcePackage = Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/application/sourcecode");

        String dispatcherSource = Files.readString(dispatcher, StandardCharsets.UTF_8);

        assertTrue(Files.isDirectory(sourcePackage), "Debe existir paquete de importación desde código fuente.");
        assertTrue(dispatcherSource.contains("UmlClassMarkdownParser"),
                "El parser Markdown de UML Clases debe seguir existiendo en paralelo.");
    }
}
