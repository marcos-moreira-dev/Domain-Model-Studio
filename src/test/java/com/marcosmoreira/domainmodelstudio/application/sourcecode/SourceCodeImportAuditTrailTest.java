package com.marcosmoreira.domainmodelstudio.application.sourcecode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class SourceCodeImportAuditTrailTest {

    @Test
    void shouldRecordHumanReadableImportAuditEvents() {
        SourceCodeImportAuditTrail trail = new SourceCodeImportAuditTrail();

        SourceCodeImportAuditEvent event = trail.record("Mapeo UML", "Construyendo documento UML.");

        assertEquals(1, trail.events().size());
        assertEquals("Mapeo UML", event.phase());
        assertTrue(event.displayLine().contains("Mapeo UML"));
        assertTrue(event.displayLine().contains("memoria usada JVM"));
        assertFalse(event.usedMemoryLabel().isBlank());
    }

    @Test
    void phaseReporterShouldForwardMessagesWithTimingAndMemory() {
        List<String> messages = new ArrayList<>();
        SourceCodeImportPhaseReporter reporter = SourceCodeImportPhaseReporter.start(messages::add);

        reporter.stage("Escaneo y parseo", "Iniciando lectura.");
        reporter.progress("Parseando Java...");

        assertEquals(2, messages.size());
        assertTrue(messages.get(0).contains("Escaneo y parseo"));
        assertTrue(messages.get(1).contains("Parseando Java"));
        assertEquals(2, reporter.auditTrail().events().size());
    }
}
