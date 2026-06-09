package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.umlclass.UmlSourceImportRenderProfile;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasRenderFailureReport;
import org.junit.jupiter.api.Test;

class UmlClassLargeFailureAdvisorTest {

    @Test
    void renderFailureMessageGuidesUserToSafeViews() {
        UmlClassLargeFailureAdvisor advisor = new UmlClassLargeFailureAdvisor();
        CanvasRenderFailureReport report = CanvasRenderFailureReport.from(
                "renderizar nodos",
                300,
                500,
                new OutOfMemoryError("Java heap space"));
        UmlClassVisualCostEstimate estimate = new UmlClassVisualCostEstimate(
                8, 500, 300, 4000, 1000, 3000, 1800,
                UmlSourceImportRenderProfile.LIGHT,
                UmlClassVisualCostLevel.CRITICAL);
        UmlClassRuntimeMemorySnapshot memory = UmlClassRuntimeMemorySnapshot.fromBytes(
                1024L * 1024L * 1024L,
                1024L * 1024L * 900L,
                1024L * 1024L * 20L,
                0L);

        String message = advisor.renderFailureMessage(report, true, estimate, memory);

        assertTrue(message.contains("Render UML detenido"));
        assertTrue(message.contains("Resumen"));
        assertTrue(message.contains("vistas internas"));
    }

    @Test
    void exportIOExceptionIncludesOperationalRecoveryAdvice() {
        UmlClassLargeFailureAdvisor advisor = new UmlClassLargeFailureAdvisor();

        String message = advisor.asExportIOException("Exportación PNG", new RuntimeException("snapshot enorme")).getMessage();

        assertTrue(message.contains("Exportación PNG detenido") || message.contains("Exportación PNG detenida"));
        assertTrue(message.contains("Resumen"));
        assertTrue(message.contains("filtros"));
    }
}
