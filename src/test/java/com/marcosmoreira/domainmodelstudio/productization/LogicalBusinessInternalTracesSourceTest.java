package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class LogicalBusinessInternalTracesSourceTest {

    private static final Path MAIN = Path.of("src", "main", "java", "com", "marcosmoreira",
            "domainmodelstudio");

    @Test
    void tracesShouldBePresentedAsInternalEvidenceNotExternalSynchronization() throws IOException {
        String panel = read(Path.of("presentation", "logicalbusiness", "LogicalBusinessTraceabilityPanel.java"));
        String labels = read(Path.of("presentation", "logicalbusiness", "LogicalBusinessTraceRelationLabels.java"));
        String glossary = read(Path.of("presentation", "logicalbusiness", "LogicalBusinessGlossary.java"));

        assertTrue(panel.contains("Trazas internas del expediente"));
        assertTrue(panel.contains("dentro del mismo levantamiento"));
        assertTrue(panel.contains("No sincronizan otros proyectos"));
        assertTrue(labels.contains("deriva_de"));
        assertTrue(labels.contains("sustentado por"));
        assertTrue(glossary.contains("no sincroniza proyectos externos"));
        assertFalse(panel.contains("derivar otros artefactos"));
    }

    @Test
    void traceServiceShouldDescribeInternalScope() throws IOException {
        String service = read(Path.of("application", "logicalbusiness", "LogicalBusinessTraceabilityService.java"));
        String link = read(Path.of("application", "logicalbusiness", "LogicalBusinessTraceLink.java"));
        String report = read(Path.of("application", "logicalbusiness", "LogicalBusinessTraceabilityReport.java"));

        assertTrue(service.contains("trazas internas navegables"));
        assertTrue(service.contains("del mismo expediente"));
        assertTrue(service.contains("sincronización externa"));
        assertTrue(link.contains("Enlace de traza interna"));
        assertTrue(report.contains("Vista filtrada de trazas internas"));
    }

    private static String read(Path relative) throws IOException {
        return Files.readString(MAIN.resolve(relative));
    }
}
