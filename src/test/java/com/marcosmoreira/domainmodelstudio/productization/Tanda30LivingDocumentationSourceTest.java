package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Guardarraíl de Tanda 30: la documentación viva no debe contradecir el estado funcional actual. */
class Tanda30LivingDocumentationSourceTest {

    private static final Path CURRENT_AUDIT = Path.of("docs", "diagnostico", "ESTADO_AUDITORIA_ACTUAL.md");
    private static final Path IMPORT_CONTRACT = Path.of("docs", "tecnico", "CONTRATO_IMPORTACION_MARKDOWN.md");
    private static final Path AI_RESOURCES = Path.of("docs", "ia", "RECURSOS_IA.md");
    private static final Path USER_IMPORT = Path.of("docs", "user-guide", "02_importar_markdown.md");
    private static final Path FAQ = Path.of("docs", "user-guide", "07_problemas_frecuentes.md");
    private static final Path LEGACY_CURRENT = Path.of("docs", "estado", "ESTADO_ACTUAL.md");
    private static final Path UI_SMOKE = Path.of("docs", "testeo", "UI_SMOKE_MINIMO_EJECUTABLE.md");
    private static final Path LOGICAL_SMOKE = Path.of("docs", "testeo", "SMOKE_LEVANTAMIENTO_LOGICO_TANDA_14.md");

    @Test
    void currentAuditShouldExistAndDeclareTruthHierarchy() throws IOException {
        String audit = read(CURRENT_AUDIT);
        for (String token : List.of(
                "Estado de auditoría actual",
                "Último estado de tests confirmado",
                "BUILD SUCCESS",
                "Zonas protegidas",
                "Pendientes antes de release")) {
            assertTrue(audit.contains(token), "Falta estado vivo de auditoría: " + token);
        }
    }

    @Test
    void liveImportDocumentationShouldListAllCurrentImportableTypes() throws IOException {
        String contract = read(IMPORT_CONTRACT);
        String guide = read(USER_IMPORT);
        for (String type : List.of(
                "conceptual-model",
                "data-dictionary",
                "logical-business-intake",
                "free-graph",
                "roles-permissions-map",
                "admin-wireframes",
                "uml-sequence",
                "technical-deployment")) {
            assertTrue(contract.contains(type), "Contrato Markdown no menciona tipo importable: " + type);
            assertTrue(guide.contains(type), "Guía de usuario no menciona tipo importable: " + type);
        }
    }

    @Test
    void dataDictionaryShouldNotBeDocumentedAsPendingImportInLiveDocs() throws IOException {
        String contract = read(IMPORT_CONTRACT);
        String guide = read(USER_IMPORT);
        String faq = read(FAQ);
        String joined = contract + "\n" + guide + "\n" + faq;

        assertTrue(joined.contains("data-dictionary"));
        assertTrue(joined.contains("sí importa Markdown estructurado") || joined.contains("importa Markdown estructurado"));
        assertFalse(joined.contains("data-dictionary` tiene salida documental PDF/Markdown dentro de la aplicación, pero su Markdown oficial sigue como referencia"));
        assertFalse(joined.contains("La importación directa de diccionario queda pendiente"));
        assertFalse(joined.contains("su Markdown oficial todavía funciona como referencia para IA y documentación"));
    }

    @Test
    void aiResourcesShouldDocumentLogicalBusinessCanonicalContract() throws IOException {
        String resources = read(AI_RESOURCES);
        for (String token : List.of("logical-business-intake", "ENT-XXX", "ATR-XXX", "REL-XXX", "fuente lógica canónica")) {
            assertTrue(resources.contains(token), "Recursos IA no documenta contrato lógico: " + token);
        }
    }

    @Test
    void currentStateDocumentShouldBeShortAndVigenteAfterDocumentationCleanup() throws IOException {
        String legacy = read(LEGACY_CURRENT);
        assertTrue(legacy.contains("vigente después de Tanda 27"));
        assertTrue(legacy.contains("documentación viva"));
        assertTrue(legacy.contains("no se conserva por acumulación") || legacy.contains("no se conserva por acumulación"));
        assertTrue(legacy.contains("ESTADO_AUDITORIA_ACTUAL.md"));
    }

    @Test
    void smokePlansShouldUseCurrentLogicalBusinessResourceNames() throws IOException {
        String ui = read(UI_SMOKE);
        String logical = read(LOGICAL_SMOKE);
        String joined = ui + "\n" + logical;

        assertTrue(joined.contains("logical_business_intake_uens_gordito.md"));
        assertTrue(joined.contains("logical_business_intake_template.md"));
        assertTrue(joined.contains("pestaña queda marcada") || joined.contains("pestaña queda marcada con `*`"));
        assertFalse(joined.contains("optica_horizonte_logical_business_intake_completo.md"));
        assertFalse(joined.contains("Derivaciones"));
        assertFalse(joined.contains("Trazabilidad"));
    }

    private static String read(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
