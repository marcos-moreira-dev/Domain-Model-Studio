package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

final class Tanda18Tanda19OperationalDocsSourceTest {

    @Test
    void tanda18DefinesFocusedInteractiveSmokeAndReportTemplate() throws Exception {
        String smoke = Files.readString(Path.of("docs/testeo/SMOKE_INTERACTIVO_FOCAL_TANDA_18.md"));
        String report = Files.readString(Path.of("docs/testeo/reportes/REPORTE_SMOKE_INTERACTIVO_TANDA_18.md"));

        assertTrue(smoke.contains("segundo arrastre"));
        assertTrue(smoke.contains("SideDock"));
        assertTrue(smoke.contains("bendpoints"));
        assertTrue(report.contains("Validación por tipo"));
    }

    @Test
    void scriptsReadmeClassifiesSmallCurrentPublicSurface() throws Exception {
        String readme = Files.readString(Path.of("scripts/README.md"));
        String tanda26 = Files.readString(Path.of("docs/desarrollo/TANDA_026_LIMPIEZA_CONTROLADA_SCRIPTS.md"));

        assertTrue(readme.contains("Scripts públicos vigentes"));
        assertTrue(readme.contains("Uso diario"));
        assertTrue(readme.contains("Flujo de cierre recomendado"));
        assertTrue(readme.contains("14-app-image-completa.bat"));
        assertTrue(readme.contains("15-msi-completo.bat"));
        assertTrue(tanda26.contains("Los scripts de tandas pasadas no se conservan por defecto"));
    }
}
