package com.marcosmoreira.domainmodelstudio.infrastructure.resources;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/**
 * Contrato de coherencia contextual UENS para que los ejemplos oficiales no deriven a
 * una escuela ficticia distinta del código/BD real usado como referencia.
 */
class OfficialUensExamplesBusinessCoherenceTest {

    private static final Path AI_DIAGRAMS = Path.of(
            "src", "main", "resources", "ai-resources", "official-markdown", "diagramas");
    private static final Path LOGICAL_INTAKE = Path.of(
            "src", "main", "resources", "ai-resources", "official-markdown", "levantamiento-logico",
            "logical_business_intake_uens_gordito.md");

    @Test
    void logicalBusinessIntakeShouldDeclareCanonicalUensContract() throws Exception {
        String content = read(LOGICAL_INTAKE);
        String lower = content.toLowerCase(Locale.ROOT);

        assertTrue(content.contains("diagram_type: \"logical-business-intake\""));
        assertTrue(content.contains("canonical_contract: \"logical-business-master-v1\""));
        assertTrue(content.contains("importable: true"));
        assertTrue(content.contains("ADMIN"));
        assertTrue(content.contains("SECRETARIA"));
        assertTrue(lower.contains("asignación vigente estudiante-sección"));
        assertTrue(lower.contains("reporte_solicitud_queue"));
        assertTrue(lower.contains("auditoria_evento"));
        assertTrue(content.contains("PENDIENTE"));
        assertTrue(content.contains("EN_PROCESO"));
        assertTrue(content.contains("COMPLETADA"));
        assertTrue(content.contains("ERROR"));
    }

    @Test
    void dataDictionaryShouldUseRealV2Tables() throws Exception {
        String dictionary = read(AI_DIAGRAMS.resolve("data_dictionary_uens_gordito.md"));
        String logical = read(LOGICAL_INTAKE);
        String combined = (dictionary + "\n" + logical).toLowerCase(Locale.ROOT);

        for (String table : List.of(
                "usuario_sistema_administrativo",
                "representante_legal",
                "seccion",
                "docente",
                "asignatura",
                "estudiante",
                "clase",
                "calificacion",
                "reporte_solicitud_queue",
                "auditoria_evento")) {
            assertTrue(combined.contains(table), "Falta tabla real V2 en ejemplos UENS: " + table);
        }
    }

    @Test
    void rolesPermissionsShouldUseOnlyImplementedRolesAsLoginRoles() throws Exception {
        String content = read(AI_DIAGRAMS.resolve("roles_permissions_uens_gordito.md"));
        String lower = content.toLowerCase(Locale.ROOT);

        assertTrue(content.contains("## ADMIN"));
        assertTrue(content.contains("## SECRETARIA"));
        assertFalse(content.contains("## DOCENTE"));
        assertFalse(content.contains("## DIRECCION"));
        assertFalse(content.contains("## DIRECCIÓN"));
        assertFalse(content.contains("## SOPORTE"));
        assertFalse(content.contains("## REPRESENTANTE"));
        assertTrue(lower.contains("stakeholders") || lower.contains("perfiles planificados"),
                "Los perfiles no implementados deben explicarse como stakeholders/planificados, no roles de login.");
    }

    @Test
    void examplesThatMentionMatriculaShouldMarkItAsOperationalOnly() throws Exception {
        for (Path example : uensMarkdownFiles()) {
            String lower = read(example).toLowerCase(Locale.ROOT);
            if (lower.contains("matrícula") || lower.contains("matricula")) {
                assertTrue(
                        lower.contains("no existe tabla")
                                || lower.contains("proceso operativo")
                                || lower.contains("trámite operativo")
                                || lower.contains("estudiante.seccion_id")
                                || lower.contains("seccionid")
                                || lower.contains("asignación vigente")
                                || lower.contains("no existe clase persistente"),
                        example + " menciona matrícula sin aclarar que no es tabla/entidad persistente.");
            }
        }
    }

    @Test
    void screenFlowShouldUseRealDesktopViews() throws Exception {
        String content = read(AI_DIAGRAMS.resolve("screen_flow_uens_gordito.md")).toUpperCase(Locale.ROOT);

        for (String screen : List.of(
                "LOGIN",
                "DASHBOARD",
                "ESTUDIANTES",
                "REPRESENTANTES",
                "DOCENTES",
                "SECCIONES",
                "ASIGNATURAS",
                "CLASES",
                "CALIFICACIONES",
                "REPORTES",
                "AUDITORIA")) {
            assertTrue(content.contains(screen), "Falta vista desktop real en flujo de pantallas UENS: " + screen);
        }
    }

    @Test
    void stateAndReportExamplesShouldUseReportQueueLifecycle() throws Exception {
        String state = read(AI_DIAGRAMS.resolve("uml_state_matricula_uens_gordito.md"));
        String logical = read(LOGICAL_INTAKE);
        String combined = state + "\n" + logical;

        assertTrue(combined.contains("reporte_solicitud_queue"));
        assertTrue(combined.contains("PENDIENTE"));
        assertTrue(combined.contains("EN_PROCESO"));
        assertTrue(combined.contains("COMPLETADA"));
        assertTrue(combined.contains("ERROR"));
    }

    @Test
    void c4ContainersShouldNotAdvertiseInstitutionalEmailAsImplementedIntegration() throws Exception {
        String lower = read(AI_DIAGRAMS.resolve("c4_containers_uens_gordito.md")).toLowerCase(Locale.ROOT);
        boolean mentionsEmail = lower.contains("correo institucional") || lower.contains("email institucional");
        if (mentionsEmail) {
            assertTrue(lower.contains("planificado") || lower.contains("planificada") || lower.contains("futuro") || lower.contains("no implementado"),
                    "El correo institucional no debe aparecer como integración implementada sin nota de planificación.");
        }
    }

    private static List<Path> uensMarkdownFiles() throws IOException {
        try (Stream<Path> diagramas = Files.list(AI_DIAGRAMS)) {
            List<Path> files = diagramas
                    .filter(path -> path.getFileName().toString().endsWith(".md"))
                    .filter(path -> path.getFileName().toString().contains("uens"))
                    .toList();
            return Stream.concat(files.stream(), Stream.of(LOGICAL_INTAKE)).toList();
        }
    }

    private static String read(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
