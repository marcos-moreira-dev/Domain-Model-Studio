package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl documental de la tanda 14: smoke manual focalizado post Tandas 10-13. */
class Tanda14FocusedSmokeDocumentationSourceTest {

    private static final Path GUIDE = Path.of("docs/testeo/SMOKE_FOCALIZADO_TANDA_14.md");
    private static final Path REPORT = Path.of("docs/testeo/reportes/REPORTE_SMOKE_FOCALIZADO_TANDA_14.md");
    private static final Path RETIRED_SCRIPT = Path.of("scripts/17-smoke-focalizado-tanda14.bat");
    private static final Path SCRIPTS_README = Path.of("scripts/README.md");
    private static final Path PLAN = Path.of("docs", "raiz", "PLAN_TANDAS_RESTANTES.md");

    @Test
    void focusedSmokeGuideShouldCoverThePostTanda13RiskAreas() throws IOException {
        String guide = read(GUIDE);

        assertTrue(guide.contains("T14-SMOKE-001"));
        assertTrue(guide.contains("T14-SMOKE-010"));
        assertTrue(guide.contains("modelo conceptual congelado"));
        assertTrue(guide.contains("workspaces especializados con SideDock"));
        assertTrue(guide.contains("cambio entre pestañas heterogéneas"));
        assertTrue(guide.contains("FreeGraph cubierto como diagrama visual activo"));
        assertTrue(guide.contains("UML Clases con filtros/búsqueda/selección consistente"));
        assertTrue(guide.contains("Technical Deployment sin promesa de autoorganizar"));
        assertTrue(guide.contains("Logical Business sin promesas de canvas visual"));
    }

    @Test
    void focusedSmokeShouldRequireConcreteExamplesAndCriticalAcceptanceCriteria() throws IOException {
        String guide = read(GUIDE);

        assertTrue(guide.contains("conceptual_model_colegio_minimo_importable.md"));
        assertTrue(guide.contains("free_graph_minimo.md"));
        assertTrue(guide.contains("free_graph_uens_gordito.md"));
        assertTrue(guide.contains("uml_class_restaurante_minimo.md"));
        assertTrue(guide.contains("uml_class_uens_gordito.md"));
        assertTrue(guide.contains("technical_deployment_piloto_minimo.md"));
        assertTrue(guide.contains("logical_business_intake_uens_gordito.md"));
        assertTrue(guide.contains("Ningún workspace especializado cayó al canvas conceptual"));
        assertTrue(guide.contains("UML conserva selección visual obsoleta"));
    }

    @Test
    void focusedSmokeReportShouldRemainDocumentedButScriptShouldBeRetired() throws IOException {
        String report = read(REPORT);
        String scriptsReadme = read(SCRIPTS_README);
        String plan = read(PLAN);

        assertTrue(report.contains("T14-SMOKE-001 Línea base y arranque"));
        assertTrue(report.contains("T14-SMOKE-010 Guardado, reapertura y cierre"));
        assertTrue(report.contains("Aprobado para pasar a Tanda 15"));
        assertTrue(report.contains("Technical Deployment no prometió autoorganización"));
        assertTrue(report.contains("Logical Business no prometió navegación/exportación visual de canvas"));

        assertFalse(Files.exists(RETIRED_SCRIPT), "El smoke Tanda 14 no debe seguir como script público vigente.");
        assertTrue(scriptsReadme.contains("scripts de tandas pasadas"));
        assertTrue(plan.contains("Tanda 38"));
        assertTrue(plan.contains("Tanda 39"));
    }

    private static String read(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
