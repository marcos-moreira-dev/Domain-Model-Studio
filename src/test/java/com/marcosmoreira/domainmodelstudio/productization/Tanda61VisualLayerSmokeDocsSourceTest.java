package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

final class Tanda61VisualLayerSmokeDocsSourceTest {

    @Test
    void visualLayerSmokeShouldHaveGuideChecklistAndReportTemplate() throws Exception {
        String guide = Files.readString(Path.of("docs/testeo/SMOKE_VISUAL_POST_CAPAS_TANDA_61.md"));
        String checklist = Files.readString(Path.of("docs/testeo/checklists/61_smoke_visual_post_capas.md"));
        String report = Files.readString(Path.of("docs/testeo/reportes/REPORTE_SMOKE_VISUAL_POST_CAPAS_TANDA_61.md"));

        assertTrue(guide.contains("Traer al frente"));
        assertTrue(guide.contains("Guardar como `.dms`"));
        assertTrue(guide.contains("Arquitectura / C4 / despliegue"));
        assertTrue(checklist.contains("PNG respeta el orden visual"));
        assertTrue(checklist.contains("Grafo lógico del negocio"));
        assertTrue(report.contains("Validación por tipo"));
    }

    @Test
    void developmentNoteShouldTieSmokeToTandas59And60() throws Exception {
        String note = Files.readString(Path.of("docs/desarrollo/TANDA_061_SMOKE_VISUAL_POST_CAPAS.md"));
        String checks = Files.readString(Path.of("docs/raiz/TANDA_61_CHECKS.md"));

        assertTrue(note.contains("Tandas 59 y 60"));
        assertTrue(note.contains("orden visual persistible"));
        assertTrue(note.contains("política base de capas"));
        assertTrue(checks.contains("Tanda 59"));
        assertTrue(checks.contains("Tanda 60"));
    }
}
