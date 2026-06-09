package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl de Tanda 25: la documentación viva se conserva; la historia obsoleta se elimina. */
class RepositoryDocumentationPolicySourceTest {

    private static final Path POLICY = Path.of(
            "docs", "documentacion", "POLITICA_DOCUMENTAL_REPOSITORIO.md");
    private static final Path LIVING_MAP = Path.of(
            "docs", "documentacion", "MAPA_DOCUMENTACION_VIVA.md");
    private static final Path BASELINE = Path.of(
            "docs", "desarrollo", "refactor", "BASELINE_REFACTOR_TANDA_025.md");

    @Test
    void documentationPolicyShouldPreferLivingDocsOverHistoricalAccumulation() throws IOException {
        String policy = read(POLICY);

        assertTrue(policy.contains("No debe conservarse historial por acumulación"));
        assertTrue(policy.contains("explica una capacidad que existe"));
        assertTrue(policy.contains("frontera arquitectónica vigente"));
        assertTrue(policy.contains("contrato técnico vigente"));
        assertTrue(policy.contains("Se elimina"));
        assertTrue(policy.contains("registra una tanda pasada"));
        assertTrue(policy.contains("hipótesis abandonada"));
        assertTrue(policy.contains("No se archiva por defecto"));
        assertFalse(policy.contains("conservar todo el histórico"));
    }

    @Test
    void livingDocumentationMapShouldPointToTheNewPolicy() throws IOException {
        String map = read(LIVING_MAP);

        assertTrue(map.contains("Rebaseline documental — Tanda 25"));
        assertTrue(map.contains("conservar Markdown si explica una capacidad vigente"));
        assertTrue(map.contains("eliminar Markdown si solo registra una etapa pasada"));
        assertTrue(map.contains("POLITICA_DOCUMENTAL_REPOSITORIO.md"));
    }

    @Test
    void refactorBaselineShouldCarryTheSameDocumentationRule() throws IOException {
        String baseline = read(BASELINE);

        assertTrue(baseline.contains("La documentación no debe acumular historial por inercia"));
        assertTrue(baseline.contains("Se conserva Markdown si explica una capacidad vigente"));
        assertTrue(baseline.contains("se elimina en la limpieza documental"));
        assertTrue(baseline.contains("README raíz"));
    }

    private static String read(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
