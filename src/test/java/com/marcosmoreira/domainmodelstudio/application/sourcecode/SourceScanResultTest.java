package com.marcosmoreira.domainmodelstudio.application.sourcecode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

class SourceScanResultTest {

    @Test
    void resultShouldSummarizeFilesByRootAndLanguage() {
        SourceRoot backend = root("backend", SourceRootKind.BACKEND, SourceLanguage.JAVA);
        SourceRoot frontend = root("frontend", SourceRootKind.FRONTEND, SourceLanguage.TYPESCRIPT);
        SourceFileCandidate javaFile = file("backend", SourceLanguage.JAVA, "PedidoService.java");
        SourceFileCandidate tsFile = file("frontend", SourceLanguage.TYPESCRIPT, "pedido.service.ts");

        SourceScanResult result = new SourceScanResult(List.of(backend, frontend),
                List.of(javaFile, tsFile), List.of(), List.of());

        assertTrue(result.hasFiles());
        assertEquals(List.of(javaFile), result.filesForRoot("backend"));
        assertEquals(List.of(tsFile), result.filesForLanguage(SourceLanguage.TYPESCRIPT));
        assertEquals(1L, result.fileCountByLanguage().get(SourceLanguage.JAVA));
        assertEquals(1L, result.fileCountByLanguage().get(SourceLanguage.TYPESCRIPT));
    }

    private static SourceRoot root(String id, SourceRootKind kind, SourceLanguage language) {
        return new SourceRoot(id, id, Path.of(id), kind, List.of(SourceLanguageVersion.flexible(language)));
    }

    private static SourceFileCandidate file(String rootId, SourceLanguage language, String fileName) {
        return new SourceFileCandidate(Path.of(rootId, fileName), Path.of(fileName), language, rootId);
    }
}
