package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Guardarraíl documental para que el futuro grafo lógico no nazca como grafo libre renombrado. */
class Tanda7BLogicalBusinessGraphPlanSourceTest {

    private static final Path PLAN = Path.of("docs", "arquitectura", "19_plan_grafo_logico_negocio.md");
    private static final Path TANDA = Path.of("docs", "implementacion", "TANDA_07B_ESPECIFICACION_GRAFO_LOGICO.md");
    private static final Path ROOT_PLAN = Path.of("docs", "raiz", "PLAN_TANDAS_RESTANTES.md");

    @Test
    void logicalBusinessGraphPlanShouldDefineOwnSemanticContract() throws IOException {
        String plan = read(PLAN);
        assertAll(
                () -> assertTrue(plan.contains("logical-business-graph")),
                () -> assertTrue(plan.contains("Grafo lógico de negocio")),
                () -> assertTrue(plan.contains("LogicalBusinessGraphDocument")),
                () -> assertTrue(plan.contains("LogicalBusinessGraphNodeKind")),
                () -> assertTrue(plan.contains("LogicalBusinessGraphRelationKind")),
                () -> assertTrue(plan.contains("LogicalBusinessDocument")),
                () -> assertTrue(plan.contains("LogicalBusinessDerivationTarget.LOGICAL_BUSINESS_GRAPH"))
        );
    }

    @Test
    void semanticNodesAndRelationsShouldStayExplicit() throws IOException {
        String plan = read(PLAN);
        for (String token : List.of(
                "MF", "FL", "CU", "ACC", "RN", "PRE", "INV", "POST", "ENT", "EST", "REP", "RISK", "PEND",
                "contiene", "usa", "reutiliza", "ejecuta", "aplica", "requiere", "protege", "garantiza",
                "crea", "modifica", "consulta", "genera", "alimenta", "bloquea", "habilita", "depende_de", "deriva_en")) {
            assertTrue(plan.contains(token), "Falta token semantico del grafo logico: " + token);
        }
    }

    @Test
    void planShouldProtectFreeGraphAndConceptualModelBoundaries() throws IOException {
        String joined = read(PLAN) + "\n" + read(TANDA) + "\n" + read(ROOT_PLAN);
        for (String token : List.of(
                "No debe nacer como un simple grafo libre renombrado",
                "FreeGraphDocument",
                "no contaminar el grafo libre ni el modelo conceptual",
                "pantalla de inicio",
                "modelo conceptual",
                "canvas conceptual")) {
            assertTrue(joined.contains(token), "Falta frontera protegida para grafo logico: " + token);
        }
    }

    private static String read(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
