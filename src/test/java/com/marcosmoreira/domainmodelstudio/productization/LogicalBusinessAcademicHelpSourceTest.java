package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import org.junit.jupiter.api.Test;

/** Guardarraíl de L0-A para que la ayuda académica del levantamiento lógico sea prosa seria y no checklist seco. */
class LogicalBusinessAcademicHelpSourceTest {

    private static final Path TOPIC = Path.of("src/main/resources/help/topics/logical-business-intake.md");
    private static final Path THEORY_TOPIC_ID = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/application/theory/TheoryTopicId.java");
    private static final Path THEORY_CATALOG = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/application/theory/DefaultTheoryCatalog.java");
    private static final Path MANUAL_CONTENT = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/dialogs/ManualContent.java");

    private static final Set<String> REQUIRED_SECTIONS = Set.of(
            "## Qué es",
            "## Para qué sirve",
            "## Elementos principales",
            "## Relaciones y lectura",
            "## Casos especiales",
            "## Cuándo usarlo",
            "## Cuándo no usarlo",
            "## Errores comunes");

    @Test
    void logicalBusinessHelpShouldBeAcademicAndExtensive() throws IOException {
        String topic = read(TOPIC);

        assertTrue(topic.contains("# Levantamiento lógico de negocio"));
        assertTrue(topic.contains("## Fundamento algorítmico"));
        assertTrue(topic.contains("## Precondiciones, invariantes y postcondiciones"));
        assertTrue(topic.contains("## Entidades y atributos candidatos"));
        assertTrue(topic.contains("La frase clave es: el levantamiento lógico es una fuente lógica canónica"));
        assertTrue(topic.contains("estado inicial"));
        assertTrue(topic.contains("precondiciones"));
        assertTrue(topic.contains("invariantes"));
        assertTrue(topic.contains("postcondiciones"));
        assertTrue(topic.contains("acciones transformadoras"));
        assertTrue(topic.contains("atributos candidatos"));
        assertTrue(topic.contains("Responsabilidad de alineación semántica"));
        assertTrue(topic.contains("Cada tipo de proyecto mantiene su propio alcance"));
        assertTrue(topic.lines().count() >= 160, "La ayuda académica debe ser gordita y en prosa suficiente.");

        for (String section : REQUIRED_SECTIONS) {
            assertTrue(topic.contains(section), "Falta sección académica obligatoria: " + section);
        }
    }

    @Test
    void logicalBusinessHelpShouldBeRegisteredWithoutDependingOnVisibleDiagramCatalog() throws IOException {
        String topicId = read(THEORY_TOPIC_ID);
        String catalog = read(THEORY_CATALOG);
        String manual = read(MANUAL_CONTENT);

        assertTrue(topicId.contains("LOGICAL_BUSINESS_INTAKE"));
        assertTrue(catalog.contains("logical-business-intake.md"));
        assertTrue(catalog.contains("DiagramTypeId.LOGICAL_BUSINESS_INTAKE"));
        assertTrue(manual.contains("TheoryTopicId.LOGICAL_BUSINESS_INTAKE"));
        assertTrue(manual.contains("Levantamiento lógico"));
    }

    private static String read(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
