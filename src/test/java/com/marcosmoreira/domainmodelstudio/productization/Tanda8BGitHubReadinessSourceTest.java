package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl Tanda 8B: el paquete debe quedar presentable para GitHub y pre-RC. */
class Tanda8BGitHubReadinessSourceTest {

    @Test
    void rootReadmeShouldExistAndUseRepositoryRelativeAssets() throws IOException {
        Path readme = Path.of("README.md");
        assertTrue(Files.exists(readme), "GitHub necesita README.md en la raíz.");

        String content = read(readme);
        assertTrue(content.contains("Domain Model Studio"));
        assertTrue(content.contains("readme-assets/"));
        assertTrue(content.contains("docs/diagnostico/ESTADO_AUDITORIA_ACTUAL.md"));
        assertFalse(content.contains("../../readme-assets/"), "El README raíz no debe usar rutas relativas de docs/raiz.");
    }

    @Test
    void licenseShouldNotRemainPendingForPublicationPackage() throws IOException {
        String license = read(Path.of("LICENSE"));
        assertFalse(license.contains("License pending"));
        assertTrue(license.contains("All rights reserved"));
        assertTrue(license.contains("not an open-source license"));
    }

    @Test
    void pomDescriptionShouldReflectCurrentProductScope() throws IOException {
        String pom = read(Path.of("pom.xml"));
        assertTrue(pom.contains("AI-assisted business analysis"));
        assertTrue(pom.contains("administrative application design"));
        assertTrue(pom.contains("UML, C4, BPMN"));
    }

    @Test
    void currentPlanAndAuditShouldPointToTanda31AndMarkTanda8BApplied() throws IOException {
        String plan = read(Path.of("docs", "raiz", "PLAN_TANDAS_RESTANTES.md"));
        String audit = read(Path.of("docs", "diagnostico", "ESTADO_AUDITORIA_ACTUAL.md"));
        String joined = plan + "\n" + audit;

        assertTrue(joined.contains("Tanda 8B — Limpieza GitHub / RC documental mínima"));
        assertTrue(joined.contains("Tanda 31 — Validación local Windows / Release Candidate"));
        assertTrue(joined.contains("Tanda 9 — Deuda SRP focalizada"));
        assertTrue(joined.contains("README raíz"));
        assertTrue(joined.contains("LICENSE"));
        assertTrue(joined.contains("pom.xml"));
        assertFalse(plan.contains("8B — Limpieza GitHub / RC documental mínima.\n- 31"),
                "El plan no debe seguir listando 8B como pendiente.");
    }

    private static String read(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
