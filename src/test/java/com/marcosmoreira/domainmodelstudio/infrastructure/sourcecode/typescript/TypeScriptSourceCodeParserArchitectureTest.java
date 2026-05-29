package com.marcosmoreira.domainmodelstudio.infrastructure.sourcecode.typescript;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class TypeScriptSourceCodeParserArchitectureTest {

    @Test
    void typeScriptParserShouldStayBehindPortAndBeReplaceableByACompilerAdapterLater() throws Exception {
        Path parser = Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/sourcecode/typescript/TypeScriptSourceCodeParserAdapter.java");
        Path extractor = Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/sourcecode/typescript/TypeScriptModelExtractor.java");
        String parserSource = Files.readString(parser, StandardCharsets.UTF_8);
        String extractorSource = Files.readString(extractor, StandardCharsets.UTF_8);

        assertTrue(parserSource.contains("implements SourceCodeParserPort"));
        assertTrue(parserSource.contains("SourceLanguage.TYPESCRIPT"));
        assertTrue(extractorSource.contains("ParsedCodeTypeKind.TYPE_ALIAS"));
        assertFalse(parserSource.contains("UmlClassDiagram"), "El parser de código no debe depender del dominio UML.");
    }
}
