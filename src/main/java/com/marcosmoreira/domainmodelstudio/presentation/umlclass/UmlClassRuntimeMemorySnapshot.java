package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

/** Lectura puntual de memoria para advertir riesgos antes de render/exportar vistas UML Clases grandes. */
public record UmlClassRuntimeMemorySnapshot(
        long maxHeapBytes,
        long totalHeapBytes,
        long freeHeapBytes,
        long usedHeapBytes,
        long availableHeapBytes,
        double usedHeapPercent,
        long systemFreeMemoryBytes,
        UmlClassRuntimeMemoryLevel level
) {
    private static final long MIB = 1024L * 1024L;
    private static final long MODERATE_AVAILABLE_HEAP = 512L * MIB;
    private static final long HIGH_AVAILABLE_HEAP = 256L * MIB;
    private static final long CRITICAL_AVAILABLE_HEAP = 128L * MIB;

    public UmlClassRuntimeMemorySnapshot {
        maxHeapBytes = Math.max(0L, maxHeapBytes);
        totalHeapBytes = Math.max(0L, totalHeapBytes);
        freeHeapBytes = Math.max(0L, freeHeapBytes);
        usedHeapBytes = Math.max(0L, usedHeapBytes);
        availableHeapBytes = Math.max(0L, availableHeapBytes);
        usedHeapPercent = Math.max(0D, Math.min(100D, usedHeapPercent));
        systemFreeMemoryBytes = Math.max(0L, systemFreeMemoryBytes);
        level = level == null ? UmlClassRuntimeMemoryLevel.LOW : level;
    }

    public static UmlClassRuntimeMemorySnapshot empty() {
        return fromBytes(0L, 0L, 0L, 0L);
    }

    static UmlClassRuntimeMemorySnapshot fromRuntime(Runtime runtime, long systemFreeMemoryBytes) {
        Runtime safeRuntime = runtime == null ? Runtime.getRuntime() : runtime;
        return fromBytes(
                safeRuntime.maxMemory(),
                safeRuntime.totalMemory(),
                safeRuntime.freeMemory(),
                systemFreeMemoryBytes
        );
    }

    static UmlClassRuntimeMemorySnapshot fromBytes(
            long maxHeapBytes,
            long totalHeapBytes,
            long freeHeapBytes,
            long systemFreeMemoryBytes
    ) {
        long safeMax = normalizeMax(maxHeapBytes);
        long safeTotal = Math.max(0L, totalHeapBytes);
        long safeFree = Math.max(0L, Math.min(Math.max(0L, freeHeapBytes), safeTotal));
        long used = Math.max(0L, safeTotal - safeFree);
        long available = safeMax <= 0L ? safeFree : Math.max(0L, safeMax - used);
        double percent = safeMax <= 0L ? 0D : (used * 100D) / safeMax;
        UmlClassRuntimeMemoryLevel level = classify(percent, available, safeMax);
        return new UmlClassRuntimeMemorySnapshot(
                safeMax,
                safeTotal,
                safeFree,
                used,
                available,
                percent,
                systemFreeMemoryBytes,
                level
        );
    }

    public boolean warns() {
        return level.warns();
    }

    public String shortSummary() {
        if (maxHeapBytes <= 0L && totalHeapBytes <= 0L) {
            return "JVM sin lectura disponible";
        }
        return "JVM " + level.displayName() + " · "
                + formatBytes(usedHeapBytes) + " usados / "
                + (maxHeapBytes <= 0L ? "máx. no reportado" : formatBytes(maxHeapBytes) + " máx.")
                + " · " + formatBytes(availableHeapBytes) + " disponibles";
    }

    public String detailSummary() {
        String system = systemFreeMemoryBytes > 0L
                ? " · sistema libre aprox. " + formatBytes(systemFreeMemoryBytes)
                : "";
        return shortSummary() + " · uso " + Math.round(usedHeapPercent) + "%" + system;
    }

    public String recommendation() {
        return switch (level) {
            case LOW -> "Memoria JVM suficiente para render normal.";
            case MODERATE -> "Memoria JVM moderada: usa Resumen o filtros si la vista crece.";
            case HIGH -> "Presión de memoria alta: evita Mega vista y exportaciones completas.";
            case CRITICAL -> "Memoria crítica: trabaja por vistas pequeñas antes de renderizar o exportar.";
        };
    }

    static String formatBytes(long bytes) {
        long safeBytes = Math.max(0L, bytes);
        if (safeBytes < 1024L) {
            return safeBytes + " B";
        }
        double kib = safeBytes / 1024D;
        if (kib < 1024D) {
            return rounded(kib) + " KiB";
        }
        double mib = kib / 1024D;
        if (mib < 1024D) {
            return rounded(mib) + " MiB";
        }
        double gib = mib / 1024D;
        return rounded(gib) + " GiB";
    }

    private static UmlClassRuntimeMemoryLevel classify(double usedPercent, long availableBytes, long maxHeapBytes) {
        if (maxHeapBytes <= 0L) {
            return UmlClassRuntimeMemoryLevel.LOW;
        }
        if (usedPercent >= 92D || availableBytes <= CRITICAL_AVAILABLE_HEAP) {
            return UmlClassRuntimeMemoryLevel.CRITICAL;
        }
        if (usedPercent >= 82D || availableBytes <= HIGH_AVAILABLE_HEAP) {
            return UmlClassRuntimeMemoryLevel.HIGH;
        }
        if (usedPercent >= 70D || availableBytes <= MODERATE_AVAILABLE_HEAP) {
            return UmlClassRuntimeMemoryLevel.MODERATE;
        }
        return UmlClassRuntimeMemoryLevel.LOW;
    }

    private static long normalizeMax(long maxHeapBytes) {
        if (maxHeapBytes == Long.MAX_VALUE) {
            return 0L;
        }
        return Math.max(0L, maxHeapBytes);
    }

    private static String rounded(double value) {
        double rounded = Math.rint(value);
        if (Math.abs(value - rounded) < 0.0001D) {
            return Long.toString(Math.round(rounded));
        }
        return String.format(java.util.Locale.ROOT, "%.1f", value);
    }
}
