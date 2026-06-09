package com.marcosmoreira.domainmodelstudio.application.sourcecode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

class PreviewSourceCodeImportUseCaseTest {

    @Test
    void shouldSummarizeRootsLanguagesIgnoredPathsAndSuggestedViews() {
        SourceRoot backend = root("backend", SourceRootKind.BACKEND, SourceLanguage.JAVA);
        SourceRoot frontend = root("frontend", SourceRootKind.FRONTEND, SourceLanguage.TYPESCRIPT);
        SourceScanResult scan = new SourceScanResult(List.of(backend, frontend), List.of(
                file("backend", SourceLanguage.JAVA, "PedidoService.java"),
                file("frontend", SourceLanguage.TYPESCRIPT, "pedido.service.ts")),
                List.of(Path.of("node_modules")), List.of("scan ok"));
        PreviewSourceCodeImportUseCase useCase = new PreviewSourceCodeImportUseCase(ignored -> scan);

        SourceCodeImportPreview preview = useCase.preview(SourceCodeImportRequest.flexible(Path.of("cedro")));

        assertTrue(preview.importable());
        assertEquals(2, preview.totalFiles());
        assertEquals(1, preview.ignoredPathCount());
        assertEquals(1L, preview.fileCountByLanguage().get(SourceLanguage.JAVA));
        assertEquals(1L, preview.fileCountByLanguage().get(SourceLanguage.TYPESCRIPT));
        assertTrue(preview.suggestedViews().contains("Backend"));
        assertTrue(preview.suggestedViews().contains("Frontend"));
        assertTrue(preview.suggestedViews().contains("Integración API"));
    }

    private static SourceRoot root(String id, SourceRootKind kind, SourceLanguage language) {
        return new SourceRoot(id, id, Path.of(id), kind, List.of(SourceLanguageVersion.flexible(language)));
    }

    private static SourceFileCandidate file(String rootId, SourceLanguage language, String fileName) {
        return new SourceFileCandidate(Path.of(rootId, fileName), Path.of(fileName), language, rootId);
    }
}
