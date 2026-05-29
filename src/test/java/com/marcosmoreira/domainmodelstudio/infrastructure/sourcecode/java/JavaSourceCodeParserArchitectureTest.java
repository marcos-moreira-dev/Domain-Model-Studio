package com.marcosmoreira.domainmodelstudio.infrastructure.sourcecode.java;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class JavaSourceCodeParserArchitectureTest {

    @Test
    void javaParserShouldUseJdkAstAndStayBehindSourceCodeParserPort() throws Exception {
        Path parser = Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/sourcecode/java/JavaSourceCodeParserAdapter.java");
        Path extractor = Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/sourcecode/java/JavaAstModelExtractor.java");

        String parserSource = Files.readString(parser, StandardCharsets.UTF_8);
        String extractorSource = Files.readString(extractor, StandardCharsets.UTF_8);

        assertTrue(parserSource.contains("implements SourceCodeParserPort"));
        assertTrue(parserSource.contains("JavacTask"), "Debe usar AST del JDK, no regex frágil como mecanismo principal.");
        assertTrue(extractorSource.contains("CompilationUnitTree"));
        assertTrue(extractorSource.contains("ClassTree"));
    }
}
