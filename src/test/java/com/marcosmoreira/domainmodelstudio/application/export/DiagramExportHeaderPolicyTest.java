package com.marcosmoreira.domainmodelstudio.application.export;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import org.junit.jupiter.api.Test;

class DiagramExportHeaderPolicyTest {

    @Test
    void projectMetadataShouldDriveVisualExportHeader() {
        DiagramProject project = DiagramProject.blank("uens", "UENS — Despliegue técnico", DiagramTypeId.TECHNICAL_DEPLOYMENT);

        DiagramExportHeaderMetadata metadata = DiagramExportHeaderPolicy.forProject(project, "Despliegue técnico");

        assertEquals("UENS — Despliegue técnico", metadata.title());
        assertEquals("technical-deployment", metadata.typeLabel());
        assertTrue(metadata.compactSubtitle().contains("technical-deployment"));
    }

    @Test
    void specializedSvgShouldPreferViewSummaryAsSubtitle() {
        DiagramExportHeaderMetadata metadata = DiagramExportHeaderPolicy.forSpecializedSvg(
                "UENS — UML Clases",
                "Diagrama UML de clases",
                "Vistas: Backend · Frontend");

        assertEquals("UENS — UML Clases", metadata.title());
        assertTrue(metadata.compactSubtitle().contains("Vistas: Backend"));
        assertTrue(metadata.compactSubtitle().contains("Diagrama UML de clases"));
    }
}
