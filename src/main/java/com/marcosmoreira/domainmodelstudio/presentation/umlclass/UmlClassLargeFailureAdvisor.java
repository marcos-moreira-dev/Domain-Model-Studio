package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasRenderFailureReport;
import java.io.IOException;

/** Mensajes operativos para degradar vistas UML Clases grandes sin dejar al usuario a ciegas. */
public final class UmlClassLargeFailureAdvisor {

    public String renderFailureMessage(
            CanvasRenderFailureReport report,
            boolean switchedToSummary,
            UmlClassVisualCostEstimate estimate,
            UmlClassRuntimeMemorySnapshot memory
    ) {
        CanvasRenderFailureReport safeReport = report == null
                ? CanvasRenderFailureReport.from("render", 0, 0, new RuntimeException("Fallo desconocido"))
                : report;
        String action = switchedToSummary
                ? "Se cambió automáticamente a Resumen. "
                : "No se pudo degradar más la vista activa. ";
        return "Render UML detenido de forma segura (" + safeReport.compactSummary() + "). "
                + action
                + recommendationFor(estimate, memory, safeReport);
    }

    public IOException asExportIOException(String operation, Throwable throwable) {
        Throwable safeThrowable = throwable == null ? new RuntimeException("Fallo desconocido") : throwable;
        String detail = safeThrowable.getMessage() == null || safeThrowable.getMessage().isBlank()
                ? safeThrowable.getClass().getSimpleName()
                : safeThrowable.getMessage();
        return new IOException(operation + " detenido de forma segura: " + detail
                + ". Usa Resumen, filtros o una vista interna antes de intentar nuevamente.", safeThrowable);
    }

    private String recommendationFor(
            UmlClassVisualCostEstimate estimate,
            UmlClassRuntimeMemorySnapshot memory,
            CanvasRenderFailureReport report
    ) {
        String visual = estimate == null ? "" : estimate.recommendation();
        String runtime = memory == null ? "" : memory.recommendation();
        if (report.memoryRelated()) {
            return "La señal parece relacionada con memoria. " + runtime
                    + " Trabaja por vistas internas, búsqueda o filtros antes de abrir Mega vista.";
        }
        if (estimate != null && estimate.level().warns()) {
            return visual + " Reduce clases visibles, relaciones o miembros visibles.";
        }
        return "Trabaja con Resumen, búsqueda, filtros por tipo o vistas internas antes de volver a intentar Mega vista.";
    }
}
