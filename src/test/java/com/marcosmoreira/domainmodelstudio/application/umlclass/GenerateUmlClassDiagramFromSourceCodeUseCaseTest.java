package com.marcosmoreira.domainmodelstudio.application.umlclass;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeModule;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeProject;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeProjectNormalizer;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeSourceRoot;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeType;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeTypeKind;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceCodeImportRequest;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceCodeParseRequest;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceCodeParserPort;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceFileCandidate;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceCodeParserRegistry;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceCodeProjectParserUseCase;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceLanguage;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceLanguageVersion;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceRoot;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceRootKind;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceScanResult;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramDocument;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class GenerateUmlClassDiagramFromSourceCodeUseCaseTest {

    @Test
    void shouldGenerateUmlDocumentFromParsedSourceProjectUseCase() {
        SourceRoot backend = new SourceRoot("backend", "backend", Path.of("backend"), SourceRootKind.BACKEND,
                List.of(SourceLanguageVersion.flexible(SourceLanguage.JAVA)));
        SourceScanResult scan = new SourceScanResult(List.of(backend), List.of(
                new SourceFileCandidate(Path.of("backend/src/main/java/Producto.java"),
                        Path.of("src/main/java/Producto.java"), SourceLanguage.JAVA, backend.id())),
                List.of(), List.of());
        SourceCodeProjectParserUseCase parserUseCase = new SourceCodeProjectParserUseCase(
                ignored -> scan,
                new SourceCodeParserRegistry(List.of(fakeJavaParser())),
                new ParsedCodeProjectNormalizer());

        UmlClassDiagramDocument document = new GenerateUmlClassDiagramFromSourceCodeUseCase(parserUseCase,
                new SourceCodeToUmlClassDiagramMapper()).generate(SourceCodeImportRequest.flexible(Path.of("cedro")));

        assertEquals(1, document.moduleCount());
        assertEquals(1, document.classCount());
        assertTrue(document.classes().getFirst().description().contains("Producto.java"));
    }

    private SourceCodeParserPort fakeJavaParser() {
        return new SourceCodeParserPort() {
            @Override
            public boolean supports(SourceLanguage language, SourceLanguageVersion version) {
                return language == SourceLanguage.JAVA;
            }

            @Override
            public ParsedCodeProject parse(SourceCodeParseRequest request) {
                return parsedProject();
            }
        };
    }

    private ParsedCodeProject parsedProject() {
        ParsedCodeSourceRoot root = new ParsedCodeSourceRoot("backend", "backend", Path.of("backend"),
                SourceRootKind.BACKEND, List.of(SourceLanguageVersion.flexible(SourceLanguage.JAVA)));
        ParsedCodeModule module = new ParsedCodeModule("mod_productos", "backend", "com.acme.productos",
                "com.acme.productos", Path.of("com/acme/productos"));
        ParsedCodeType type = new ParsedCodeType("backend:Producto", "backend", module.id(),
                "com.acme.productos.Producto", "Producto", ParsedCodeTypeKind.CLASS, Path.of("Producto.java"),
                "com.acme.productos", List.of(), List.of(), Map.of());
        return new ParsedCodeProject("Cedro", List.of(root), List.of(module), List.of(type), List.of(), List.of());
    }
}
