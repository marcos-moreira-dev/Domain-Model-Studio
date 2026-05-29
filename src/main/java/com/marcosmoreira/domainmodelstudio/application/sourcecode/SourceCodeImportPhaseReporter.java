package com.marcosmoreira.domainmodelstudio.application.sourcecode;

import java.util.Objects;

/**
 * Publica mensajes de progreso enriquecidos con fase, tiempo transcurrido y memoria usada por la JVM.
 * No resuelve por sí solo el render pesado; sirve para ubicar en qué fase se congela la importación.
 */
public final class SourceCodeImportPhaseReporter {
    private final SourceCodeImportProgressListener delegate;
    private final SourceCodeImportAuditTrail auditTrail;

    private SourceCodeImportPhaseReporter(SourceCodeImportProgressListener delegate, SourceCodeImportAuditTrail auditTrail) {
        this.delegate = delegate == null ? SourceCodeImportProgressListener.NONE : delegate;
        this.auditTrail = Objects.requireNonNull(auditTrail, "auditTrail");
    }

    public static SourceCodeImportPhaseReporter start(SourceCodeImportProgressListener delegate) {
        return new SourceCodeImportPhaseReporter(delegate, new SourceCodeImportAuditTrail());
    }

    public void stage(String phase, String message) {
        SourceCodeImportAuditEvent event = auditTrail.record(phase, message);
        delegate.onProgress(event.displayLine());
    }

    public void progress(String message) {
        SourceCodeImportAuditEvent event = auditTrail.record("Progreso", message);
        delegate.onProgress(event.displayLine());
    }

    public SourceCodeImportAuditTrail auditTrail() {
        return auditTrail;
    }
}
