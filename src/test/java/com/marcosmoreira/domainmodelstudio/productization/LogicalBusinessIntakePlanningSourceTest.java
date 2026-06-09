package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl documental del Levantamiento lógico vigente. */
class LogicalBusinessIntakePlanningSourceTest {

    private static final Path ARCHITECTURE = Path.of("docs/arquitectura/16_levantamiento_logico_negocio.md");
    private static final Path PRODUCT = Path.of("docs/productizacion/TIPO_LEVANTAMIENTO_LOGICO.md");
    private static final Path CHECKLIST = Path.of("docs/testeo/checklists/levantamiento_logico.md");
    private static final Path DECISIONS = Path.of("docs/producto/decisiones_producto.md");
    private static final Path PLAN = Path.of("docs/desarrollo/PLAN_TANDAS_ACTUAL.md");

    @Test
    void architectureShouldDefineLogicalBusinessAsStructuredDocumentNotDiagram() throws IOException {
        String architecture = read(ARCHITECTURE);

        assertTrue(architecture.contains("Levantamiento lógico no es un diagrama"));
        assertTrue(architecture.contains("proyecto documental estructurado"));
        assertTrue(architecture.contains("logical-business-intake"));
        assertTrue(architecture.contains("fuente lógica canónica"));
        assertTrue(architecture.contains("No se recomienda panel derecho fijo"));
        assertTrue(architecture.contains("SideDock modular izquierdo"));
        assertTrue(architecture.contains("No debe reutilizar `GRAPH`"));
        assertFalse(architecture.contains("fuente madre"));
    }

    @Test
    void architectureShouldKeepResponsibilitiesSeparatedByLayer() throws IOException {
        String architecture = read(ARCHITECTURE);

        assertTrue(architecture.contains("domain/logicalbusiness"));
        assertTrue(architecture.contains("application/logicalbusiness"));
        assertTrue(architecture.contains("infrastructure/markdown/logicalbusiness"));
        assertTrue(architecture.contains("presentation/logicalbusiness"));
        assertTrue(architecture.contains("Un ViewModel no debe parsear Markdown"));
        assertTrue(architecture.contains("El modelo conceptual no debe recibir dependencias"));
    }

    @Test
    void productContractShouldDeclareHonestCurrentCapabilities() throws IOException {
        String product = read(PRODUCT);
        String checklist = read(CHECKLIST);

        assertTrue(product.contains("No debe ser editor Markdown libre"));
        assertTrue(product.contains("No debe exportar SVG/PNG"));
        assertTrue(product.contains("Levantamiento lógico = fuente lógica canónica"));
        assertTrue(product.contains("UENS"));
        assertTrue(checklist.contains("Impacto y dependencias"));
        assertTrue(checklist.contains("Ayuda y glosario"));
        assertTrue(checklist.contains("No existe módulo principal de Derivaciones"));
        assertFalse(product.contains("fuente madre"));
    }

    @Test
    void productDecisionsAndPlanShouldMentionCurrentRefactorContinuity() throws IOException {
        String decisions = read(DECISIONS);
        String plan = read(PLAN);

        assertTrue(decisions.contains("Levantamiento lógico"));
        assertTrue(decisions.contains("fuente lógica canónica"));
        assertTrue(decisions.contains("logical-business-master-v1"));
        assertTrue(plan.contains("Tanda 27 — Limpieza documental viva"));
        assertTrue(plan.contains("Tanda 28 — Refactor de `ApplicationServices`"));
        assertFalse(plan.contains("Tanda L0"));
    }

    private static String read(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
