package com.marcosmoreira.domainmodelstudio.application.architecture;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

/** Generador simple y estable de IDs legibles para diagramas de arquitectura. */
final class ArchitectureDiagramIds {
    private static final AtomicInteger SEQUENCE = new AtomicInteger(1);
    private ArchitectureDiagramIds() { }

    static String next(String prefix) {
        String safePrefix = prefix == null || prefix.isBlank() ? "arquitectura" : prefix.strip().toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", "-");
        return safePrefix + "-" + SEQUENCE.getAndIncrement();
    }
}
