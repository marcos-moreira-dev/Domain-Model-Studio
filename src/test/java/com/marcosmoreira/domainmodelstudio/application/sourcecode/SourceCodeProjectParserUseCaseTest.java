package com.marcosmoreira.domainmodelstudio.application.sourcecode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class SourceCodeProjectParserUseCaseTest {

    @Test
    void shouldParseEveryDetectedSourceRootAndNormalizeTheMergedNeutralModel() {
        SourceRoot backend = root("backend", SourceRootKind.BACKEND, SourceLanguage.JAVA);
        SourceRoot frontend = root("frontend", SourceRootKind.FRONTEND, SourceLanguage.TYPESCRIPT);
        SourceCodeImportRequest request = SourceCodeImportRequest.flexible(Path.of("cedro-damasco"));
        SourceScanResult scan = new SourceScanResult(List.of(backend, frontend), List.of(
                new SourceFileCandidate(Path.of("cedro-damasco/backend/PedidoService.java"),
                        Path.of("PedidoService.java"), SourceLanguage.JAVA, backend.id()),
                new SourceFileCandidate(Path.of("cedro-damasco/frontend/producto.service.ts"),
                        Path.of("producto.service.ts"), SourceLanguage.TYPESCRIPT, frontend.id())), List.of(), List.of());
        SourceCodeProjectParserUseCase useCase = new SourceCodeProjectParserUseCase(
                ignored -> scan,
                new SourceCodeParserRegistry(List.of(fakeParser(SourceLanguage.JAVA), fakeParser(SourceLanguage.TYPESCRIPT))),
                new ParsedCodeProjectNormalizer());

        ParsedCodeProject project = useCase.parse(request);

        assertEquals("cedro-damasco", project.projectName());
        assertEquals(2, project.sourceRoots().size());
        assertEquals(2, project.types().size());
        assertEquals(2, project.modules().size());
        assertTrue(project.types().stream().anyMatch(type -> type.sourceRootId().equals("backend")
                && type.metadata().get(ParsedCodeMetadataKeys.ROLE).equals(ParsedCodeTypeRole.BACKEND_SERVICE.id())));
        assertTrue(project.types().stream().anyMatch(type -> type.sourceRootId().equals("frontend")
                && type.metadata().get(ParsedCodeMetadataKeys.ROLE).equals(ParsedCodeTypeRole.FRONTEND_SERVICE.id())));
    }

    @Test
    void shouldReportMissingParserWithoutBreakingTheWholeImport() {
        SourceRoot backend = root("backend", SourceRootKind.BACKEND, SourceLanguage.JAVA);
        SourceCodeImportRequest request = SourceCodeImportRequest.flexible(Path.of("cedro-damasco"));
        SourceScanResult scan = new SourceScanResult(List.of(backend), List.of(
                new SourceFileCandidate(Path.of("cedro-damasco/backend/PedidoService.java"),
                        Path.of("PedidoService.java"), SourceLanguage.JAVA, backend.id())), List.of(), List.of("scan ok"));
        SourceCodeProjectParserUseCase useCase = new SourceCodeProjectParserUseCase(
                ignored -> scan, new SourceCodeParserRegistry(List.of()), new ParsedCodeProjectNormalizer());

        ParsedCodeProject project = useCase.parse(request);

        assertTrue(project.types().isEmpty());
        assertTrue(project.warnings().stream().anyMatch(warning -> warning.contains("No hay parser registrado")));
        assertTrue(project.warnings().contains("scan ok"));
    }

    private SourceCodeParserPort fakeParser(SourceLanguage language) {
        return new SourceCodeParserPort() {
            @Override
            public boolean supports(SourceLanguage requestedLanguage, SourceLanguageVersion version) {
                return requestedLanguage == language;
            }

            @Override
            public ParsedCodeProject parse(SourceCodeParseRequest request) {
                String simpleName = language == SourceLanguage.JAVA ? "PedidoService" : "ProductoService";
                String moduleName = language == SourceLanguage.JAVA ? "com.acme.pedidos" : "src.app.features.productos";
                ParsedCodeModule module = new ParsedCodeModule(request.sourceRoot().id() + ":" + moduleName,
                        request.sourceRoot().id(), moduleName, moduleName, Path.of(moduleName.replace('.', '/')));
                ParsedCodeType type = new ParsedCodeType(request.sourceRoot().id() + ":" + simpleName,
                        request.sourceRoot().id(), module.id(), moduleName + "." + simpleName, simpleName,
                        ParsedCodeTypeKind.CLASS, request.files().getFirst().relativePath(), moduleName,
                        List.of(), List.of(), Map.of(ParsedCodeMetadataKeys.LANGUAGE, language.id()));
                return new ParsedCodeProject(request.sourceRoot().displayName(),
                        List.of(ParsedCodeSourceRoot.from(request.sourceRoot())), List.of(module), List.of(type),
                        List.of(), List.of());
            }
        };
    }

    private SourceRoot root(String id, SourceRootKind kind, SourceLanguage language) {
        return new SourceRoot(id, id, Path.of(id), kind, List.of(SourceLanguageVersion.flexible(language)));
    }
}
