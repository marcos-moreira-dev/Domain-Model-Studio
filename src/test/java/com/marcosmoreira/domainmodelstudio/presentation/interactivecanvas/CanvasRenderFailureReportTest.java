package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CanvasRenderFailureReportTest {

    @Test
    void reportNormalizesCountsAndSummarizesFailure() {
        CanvasRenderFailureReport report = CanvasRenderFailureReport.from(
                "renderizar nodos",
                7,
                11,
                new IllegalStateException("layout faltante"));

        assertEquals(18, report.totalElements());
        assertTrue(report.compactSummary().contains("11 nodos"));
        assertTrue(report.compactSummary().contains("7 conectores"));
        assertTrue(report.compactSummary().contains("IllegalStateException"));
    }

    @Test
    void reportDetectsMemoryRelatedFailures() {
        CanvasRenderFailureReport report = CanvasRenderFailureReport.from(
                "render",
                0,
                0,
                new OutOfMemoryError("Java heap space"));

        assertTrue(report.memoryRelated());
    }
}
