package com.marcosmoreira.domainmodelstudio.application.canonization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class CanonizationFlowArchitectureSourceTest {

    private static final Path ROOT = Path.of("src/main/java/com/marcosmoreira/domainmodelstudio");

    @Test
    void canonizationApplicationPackageShouldStayFreeOfJavaFxAndPresentation() throws IOException {
        String source = readPackage("application/canonization");

        assertFalse(source.contains("javafx."));
        assertFalse(source.contains("presentation."));
        assertFalse(source.contains("infrastructure."));
    }

    @Test
    void documentationServicesShouldExposeCanonizationFlowReportUseCase() throws IOException {
        String source = Files.readString(ROOT.resolve("application/services/DocumentationApplicationServices.java"));

        assertTrue(source.contains("CanonizationFlowReportUseCase"));
        assertTrue(source.contains("canonizationFlowReportUseCase()"));
    }

    @Test
    void folderImportSummaryShouldUseCanonizationReportWithoutParsingFilesAgain() throws IOException {
        String source = Files.readString(ROOT.resolve("presentation/shell/commands/MarkdownFolderImportCoordinator.java"));

        assertTrue(source.contains("canonizationFlowReportUseCase()"));
        assertTrue(source.contains("Canonización documental"));
        assertFalse(source.contains("Files.walk"));
        assertFalse(source.contains("DiagramMarkdownImportDispatcher"));
    }

    private static String readPackage(String packagePath) throws IOException {
        StringBuilder source = new StringBuilder();
        try (var files = Files.walk(ROOT.resolve(packagePath))) {
            for (Path path : files.filter(path -> path.toString().endsWith(".java")).toList()) {
                source.append(Files.readString(path)).append('\n');
            }
        }
        return source.toString();
    }
}
