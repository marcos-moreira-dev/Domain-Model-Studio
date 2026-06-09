package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl Tanda 15: el Levantamiento lógico no promete automatización fuera de alcance. */
class LogicalBusinessScopeLanguageSourceTest {

    private static final Path MAIN = Path.of("src/main/java");
    private static final Path RESOURCES = Path.of("src/main/resources");
    private static final Path DOCS = Path.of("docs");

    @Test
    void academicGuideShouldStateSourceScopeAndAiResponsibility() throws IOException {
        String topic = read(RESOURCES.resolve("help/topics/logical-business-intake.md"));

        assertTrue(topic.contains("fuente lógica canónica"));
        assertTrue(topic.contains("Responsabilidad de alineación semántica"));
        assertTrue(topic.contains("Cada tipo de proyecto mantiene su propio alcance"));
        assertTrue(topic.contains("No es obligatorio generar todos los tipos de proyecto"));
        assertFalse(topic.contains("generar automáticamente todos"));
        assertFalse(topic.contains("sincroniza automáticamente"));
    }

    @Test
    void visibleLogicalBusinessLabelsShouldAvoidDerivationsAsMainPromise() throws IOException {
        String editor = read(MAIN.resolve("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessEditorView.java"));
        String toolbar = read(MAIN.resolve("com/marcosmoreira/domainmodelstudio/presentation/toolbar/LogicalBusinessToolbarContributor.java"));
        String moduleIds = read(MAIN.resolve("com/marcosmoreira/domainmodelstudio/presentation/sidedock/SideDockModuleId.java"));

        assertFalse(editor.contains("Artefactos compatibles"));
        assertTrue(editor.contains("Impacto y dependencias"));
        assertFalse(toolbar.contains("Artefactos"));
        assertTrue(toolbar.contains("Impacto"));
        assertFalse(moduleIds.contains("Artefactos compatibles"));
        assertFalse(editor.contains("\"Derivaciones\""));
        assertFalse(toolbar.contains("\"Derivaciones\""));
    }

    @Test
    void markdownContractShouldExplainManualAiAlignment() throws IOException {
        String contract = read(DOCS.resolve("tecnico/CONTRATO_MARKDOWN_LEVANTAMIENTO_LOGICO.md"));
        String tanda = read(DOCS.resolve("desarrollo/TANDA_015_CONTRATO_ALCANCE_GUIA_LEVANTAMIENTO_LOGICO.md"));

        assertTrue(contract.contains("Responsabilidad de alineación semántica"));
        assertTrue(contract.contains("usuario o una IA"));
        assertTrue(contract.contains("no garantiza automáticamente la alineación"));
        assertTrue(contract.contains("Uso como fuente para otros artefactos"));
        assertTrue(tanda.contains("no generador automático universal"));
    }

    private static String read(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
