package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class LogicalBusinessValidationTraceabilitySourceTest {

    private static final Path MAIN = Path.of("src", "main", "java", "com", "marcosmoreira",
            "domainmodelstudio");

    @Test
    void validationAndTraceabilityStayInApplicationServices() throws IOException {
        String viewModel = read(MAIN.resolve(Path.of("presentation", "logicalbusiness", "LogicalBusinessViewModel.java")));
        String validationService = read(MAIN.resolve(Path.of("application", "logicalbusiness",
                "LogicalBusinessValidationService.java")));
        String traceabilityService = read(MAIN.resolve(Path.of("application", "logicalbusiness",
                "LogicalBusinessTraceabilityService.java")));

        assertTrue(viewModel.contains("LogicalBusinessValidationService"));
        assertTrue(viewModel.contains("LogicalBusinessTraceabilityService"));
        assertTrue(validationService.contains("LogicalBusinessItemValidationPolicy"));
        assertTrue(validationService.contains("LogicalBusinessEntityValidationPolicy"));
        assertTrue(traceabilityService.contains("LogicalBusinessTraceLink"));
    }

    @Test
    void traceabilityPanelShouldExposeInternalTracesWithoutDerivationPromises() throws IOException {
        String panel = read(MAIN.resolve(Path.of("presentation", "logicalbusiness",
                "LogicalBusinessTraceabilityPanel.java")));
        String service = read(MAIN.resolve(Path.of("application", "logicalbusiness",
                "LogicalBusinessTraceabilityService.java")));
        String labels = read(MAIN.resolve(Path.of("presentation", "logicalbusiness",
                "LogicalBusinessTraceRelationLabels.java")));
        String treeHelp = read(MAIN.resolve(Path.of("presentation", "logicalbusiness",
                "LogicalBusinessTreeHelpGuide.java")));

        assertTrue(panel.contains("Impacto y dependencias del expediente"));
        assertTrue(panel.contains("Depende de"));
        assertTrue(panel.contains("Impacta a"));
        assertTrue(panel.contains("selectReference"));
        assertTrue(panel.contains("No sincronizan otros proyectos"));
        assertTrue(panel.contains("no generan artefactos"));
        assertTrue(labels.contains("sustentado por"));
        assertTrue(service.contains("sincronización externa"));
        assertTrue(treeHelp.contains("No sincroniza proyectos ni genera artefactos"));
    }

    @Test
    void filesRemainReviewable() throws IOException {
        assertTrue(lineCount(MAIN.resolve(Path.of("application", "logicalbusiness",
                "LogicalBusinessItemValidationPolicy.java"))) < 160);
        assertTrue(lineCount(MAIN.resolve(Path.of("application", "logicalbusiness",
                "LogicalBusinessTraceabilityService.java"))) < 160);
        assertTrue(lineCount(MAIN.resolve(Path.of("presentation", "logicalbusiness",
                "LogicalBusinessValidationPanel.java"))) < 150);
        assertTrue(lineCount(MAIN.resolve(Path.of("presentation", "logicalbusiness",
                "LogicalBusinessTraceabilityPanel.java"))) < 120);
    }

    private String read(Path path) throws IOException {
        return Files.readString(path);
    }

    private long lineCount(Path path) throws IOException {
        return Files.lines(path).count();
    }
}
