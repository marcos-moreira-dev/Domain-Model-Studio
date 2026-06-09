package com.marcosmoreira.domainmodelstudio.application.sourcecode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

class SourceRootDetectionResultTest {

    @Test
    void resultShouldFilterRootsByLanguageAndKind() {
        SourceRoot backend = root("backend", SourceRootKind.BACKEND, SourceLanguage.JAVA);
        SourceRoot frontend = root("frontend", SourceRootKind.FRONTEND, SourceLanguage.TYPESCRIPT);

        SourceRootDetectionResult result = new SourceRootDetectionResult(Path.of("cedro"),
                List.of(backend, frontend), List.of());

        assertEquals(List.of(backend), result.rootsFor(SourceLanguage.JAVA));
        assertEquals(List.of(frontend), result.rootsForKind(SourceRootKind.FRONTEND));
        assertTrue(result.hasMultipleSourceRoots());
    }

    private static SourceRoot root(String id, SourceRootKind kind, SourceLanguage language) {
        return new SourceRoot(id, id, Path.of(id), kind, List.of(SourceLanguageVersion.flexible(language)));
    }
}
