package com.marcosmoreira.domainmodelstudio.application.sourcecode;

import java.time.Instant;
import java.util.Objects;

/** Evento liviano de auditoría para ubicar cuellos de botella en importaciones desde código fuente. */
public record SourceCodeImportAuditEvent(
        String phase,
        String message,
        long elapsedMillis,
        long usedMemoryBytes,
        Instant occurredAt) {

    public SourceCodeImportAuditEvent {
        phase = requireText(phase, "phase");
        message = message == null ? "" : message.strip();
        occurredAt = Objects.requireNonNullElseGet(occurredAt, Instant::now);
    }

    public String usedMemoryLabel() {
        if (usedMemoryBytes <= 0L) {
            return "0 MB";
        }
        double mib = usedMemoryBytes / 1024.0 / 1024.0;
        return String.format(java.util.Locale.ROOT, "%.1f MB", mib);
    }

    public String displayLine() {
        String detail = message.isBlank() ? phase : phase + " — " + message;
        return detail + " (" + elapsedMillis + " ms, memoria usada JVM: " + usedMemoryLabel() + ")";
    }

    private static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " no puede estar vacío.");
        }
        return value.strip();
    }
}
