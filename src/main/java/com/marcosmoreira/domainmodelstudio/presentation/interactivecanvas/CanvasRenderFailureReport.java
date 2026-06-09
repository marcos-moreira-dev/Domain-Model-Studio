package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

/** Reporte pequeño y seguro cuando el lienzo no pudo materializar una vista pesada. */
public record CanvasRenderFailureReport(
        String phase,
        int connectorCount,
        int nodeCount,
        String errorType,
        String message
) {
    public CanvasRenderFailureReport {
        phase = normalize(phase, "render");
        connectorCount = Math.max(0, connectorCount);
        nodeCount = Math.max(0, nodeCount);
        errorType = normalize(errorType, "RuntimeException");
        message = normalize(message, "Sin detalle disponible.");
    }

    public static CanvasRenderFailureReport from(
            String phase,
            int connectorCount,
            int nodeCount,
            Throwable throwable
    ) {
        Throwable safeThrowable = throwable == null ? new RuntimeException("Fallo desconocido") : throwable;
        String type = safeThrowable.getClass().getSimpleName();
        String detail = safeThrowable.getMessage() == null || safeThrowable.getMessage().isBlank()
                ? type
                : safeThrowable.getMessage();
        return new CanvasRenderFailureReport(phase, connectorCount, nodeCount, type, detail);
    }

    public int totalElements() {
        return connectorCount + nodeCount;
    }

    public boolean memoryRelated() {
        String normalized = (errorType + " " + message).toLowerCase(java.util.Locale.ROOT);
        return normalized.contains("outofmemory")
                || normalized.contains("heap")
                || normalized.contains("memory")
                || normalized.contains("memoria");
    }

    public String compactSummary() {
        return errorType + " durante " + phase + " · " + nodeCount
                + " nodos, " + connectorCount + " conectores";
    }

    private static String normalize(String value, String fallback) {
        String normalized = value == null ? "" : value.strip();
        return normalized.isBlank() ? fallback : normalized;
    }
}
