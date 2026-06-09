package com.marcosmoreira.domainmodelstudio.application.sourcecode;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/** Bitácora en memoria para diagnosticar fases lentas de importación UML desde código. */
public final class SourceCodeImportAuditTrail {
    private final long startedAtNanos = System.nanoTime();
    private final List<SourceCodeImportAuditEvent> events = new ArrayList<>();

    public SourceCodeImportAuditEvent record(String phase, String message) {
        SourceCodeImportAuditEvent event = new SourceCodeImportAuditEvent(
                phase,
                message,
                elapsedMillis(),
                usedMemoryBytes(),
                Instant.now());
        events.add(event);
        return event;
    }

    public List<SourceCodeImportAuditEvent> events() {
        return List.copyOf(events);
    }

    public long elapsedMillis() {
        return Math.max(0L, (System.nanoTime() - startedAtNanos) / 1_000_000L);
    }

    public long usedMemoryBytes() {
        Runtime runtime = Runtime.getRuntime();
        return Math.max(0L, runtime.totalMemory() - runtime.freeMemory());
    }
}
