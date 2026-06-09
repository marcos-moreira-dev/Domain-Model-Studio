package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

/** Guardarraíl de Tanda 23 para mantener el ejemplo oficial UENS alineado con la app real. */
class OfficialUensLogicalBusinessExampleSourceTest {

    private static final Path OFFICIAL_UENS = Path.of(
            "src/main/resources/ai-resources/official-markdown/levantamiento-logico/"
                    + "logical_business_intake_uens_gordito.md");
    private static final Path PUBLIC_UENS = Path.of(
            "examples/markdown/levantamiento-logico/logical_business_intake_uens_gordito.md");
    private static final Path TYPE_DEFINITIONS = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/application/catalog/definitions/DiagramTypeDefinitionFactory.java");
    private static final Path BUSINESS_ANALYSIS_DEFINITIONS = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/application/catalog/definitions/BusinessAnalysisDiagramTypeDefinitions.java");
    private static final Path AI_DESCRIPTORS = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/resources/definitions/CoreAiResourceDescriptors.java");
    private static final Path AI_RESOURCES_DOC = Path.of("docs/ia/RECURSOS_IA.md");

    @Test
    void officialAndPublicUensLogicalBusinessExamplesShouldStaySynchronized() throws IOException {
        assertEquals(read(OFFICIAL_UENS), read(PUBLIC_UENS),
                "El ejemplo oficial UENS y su espejo público deben mantenerse sincronizados.");
    }

    @Test
    void uensLogicalBusinessExampleShouldReflectRealAdministrativeApplication() throws IOException {
        String example = read(OFFICIAL_UENS);

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
            assertTrue(example.contains(table), "Falta tabla real V2 en el levantamiento UENS: " + table);
        }

        for (String module : List.of(
                "`auth`",
                "`usuario`",
                "`system`",
                "`dashboard`",
                "`consultaacademica`",
                "`representante`",
                "`seccion`",
                "`docente`",
                "`asignatura`",
                "`estudiante`",
                "`clase`",
                "`calificacion`",
                "`reporte`",
                "`auditoria`")) {
            assertTrue(example.contains(module), "Falta módulo backend real en el levantamiento UENS: " + module);
        }

        for (String screen : List.of(
                "`LOGIN`",
                "`DASHBOARD`",
                "`ESTUDIANTES`",
                "`REPRESENTANTES`",
                "`DOCENTES`",
                "`SECCIONES`",
                "`ASIGNATURAS`",
                "`CLASES`",
                "`CALIFICACIONES`",
                "`REPORTES`",
                "`AUDITORIA`")) {
            assertTrue(example.contains(screen), "Falta pantalla desktop real en el levantamiento UENS: " + screen);
        }

        assertTrue(example.contains("`ADMIN`"));
        assertTrue(example.contains("`SECRETARIA`"));
        assertTrue(example.contains("Docente, representante y estudiante son actores/entidades del negocio, no roles de login de fase 1."));
        assertTrue(example.contains("`PENDIENTE`"));
        assertTrue(example.contains("`EN_PROCESO`"));
        assertTrue(example.contains("`COMPLETADA`"));
        assertTrue(example.contains("`ERROR`"));
        assertTrue(example.contains("`XLSX`"));
        assertTrue(example.contains("`PDF`"));
        assertTrue(example.contains("`DOCX`"));
        assertTrue(example.contains("`LISTADO_ESTUDIANTES_POR_SECCION`"));
        assertTrue(example.contains("`CALIFICACIONES_POR_SECCION_Y_PARCIAL`"));
        assertTrue(example.contains("`AUDITORIA_ADMIN_OPERACIONES`"));
    }

    @Test
    void uensLogicalBusinessExampleShouldUseCurrentCanonicalScopeLanguage() throws IOException {
        String example = read(OFFICIAL_UENS);

        assertTrue(example.contains("Responsabilidad de alineación semántica"));
        assertTrue(example.contains("## 14. Entidades candidatas"));
        assertTrue(example.contains("## 19. Uso como fuente para otros artefactos"));
        assertTrue(example.contains("fuente lógica canónica"));
        assertTrue(example.contains("No es obligatorio generar todos los tipos de proyecto"));
        assertTrue(example.contains("servir como fuente lógica revisable"));

        assertFalse(example.contains("fuente madre"));
        assertFalse(example.contains("vistas derivadas"));
        assertFalse(example.contains("Entidades derivadas"));
        assertFalse(example.contains("Derivación futura"));
        assertFalse(example.toLowerCase().contains("deriv"),
                "El ejemplo UENS vigente no debe enseñar lenguaje de derivación como contrato rector.");
    }

    @Test
    void uensLogicalBusinessExampleShouldNotReintroducePersistentMatriculaOrUnsupportedRoles() throws IOException {
        String example = read(OFFICIAL_UENS);

        assertTrue(example.contains("En UENS fase 1 no existe tabla matricula."));
        assertTrue(example.contains("`matricula` no debe aparecer como tabla en fase 1"));
        assertTrue(example.contains("asignación vigente estudiante-sección"));
        assertFalse(example.toLowerCase().contains("matriculaoperativa"));
        assertFalse(Pattern.compile("(?m)^###\\s+ENT-\\d+\\s+—\\s+Matr", Pattern.CASE_INSENSITIVE)
                .matcher(example)
                .find(), "Matrícula no debe reaparecer como entidad candidata persistente.");
        assertFalse(example.contains("rol `DOCENTE`"));
        assertFalse(example.contains("rol `REPRESENTANTE`"));
        assertFalse(example.contains("rol `ESTUDIANTE`"));
    }

    @Test
    void catalogsAndAiDocsShouldDescribeUensAsCanonicalSourceNotAutomaticDerivation() throws IOException {
        String definitions = read(TYPE_DEFINITIONS) + "\n" + read(BUSINESS_ANALYSIS_DEFINITIONS);
        String descriptors = read(AI_DESCRIPTORS);
        String resourcesDoc = read(AI_RESOURCES_DOC);
        String joined = definitions + "\n" + descriptors + "\n" + resourcesDoc;

        assertTrue(definitions.contains("Documento estructurado para convertir entrevistas y observaciones en reglas, estados, acciones, entidades candidatas, atributos, relaciones, reportes, riesgos y preguntas pendientes."));
        assertTrue(definitions.contains("Ejemplo completo de la unidad educativa UENS"));
        assertTrue(definitions.contains("Vista visual compatible con el levantamiento lógico"));
        assertTrue(descriptors.contains("expediente lógico canónico"));
        assertTrue(resourcesDoc.contains("fuente lógica canónica"));

        assertFalse(joined.contains("fuente madre"));
        assertFalse(joined.contains("derivaciones revisables"));
        assertFalse(joined.contains("Vista visual derivada"));
        assertFalse(joined.contains("vistas derivadas"));
    }

    private static String read(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
