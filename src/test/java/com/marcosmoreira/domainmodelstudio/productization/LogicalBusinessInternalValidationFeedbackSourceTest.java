package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl Tanda 21: validación interna y madurez como fuente revisable. */
class LogicalBusinessInternalValidationFeedbackSourceTest {

    private static final Path MAIN = Path.of("src", "main", "java", "com", "marcosmoreira",
            "domainmodelstudio");

    @Test
    void validationPanelShouldExposeInternalCoherenceNotBusinessApproval() throws IOException {
        String panel = read(Path.of("presentation", "logicalbusiness", "LogicalBusinessValidationPanel.java"));

        assertTrue(panel.contains("Coherencia interna del expediente"));
        assertTrue(panel.contains("Alcance de la validación"));
        assertTrue(panel.contains("no aprueba el negocio real"));
        assertTrue(panel.contains("no sincroniza proyectos"));
        assertTrue(panel.contains("no genera artefactos"));
        assertTrue(panel.contains("Madurez documental"));
        assertTrue(panel.contains("maturity.usableAsSource()"));
        assertFalse(panel.contains("Salud del expediente"));
        assertFalse(panel.contains("Resumen de salud"));
    }

    @Test
    void maturityShouldPreferSourceReadyVocabularyAndKeepLegacyAliasOnly() throws IOException {
        String level = read(Path.of("domain", "logicalbusiness", "LogicalBusinessMaturityLevel.java"));
        String maturity = read(Path.of("domain", "logicalbusiness", "LogicalBusinessMaturity.java"));
        String formatter = read(Path.of("presentation", "logicalbusiness", "LogicalBusinessStatusFormatter.java"));
        String mapper = read(Path.of("infrastructure", "markdown", "logicalbusiness",
                "LogicalBusinessMarkdownStatusMapper.java"));

        assertTrue(level.contains("SOURCE_READY"));
        assertTrue(level.contains("Alias histórico"));
        assertTrue(maturity.contains("usableAsSource()"));
        assertTrue(maturity.contains("Alias histórico para compatibilidad"));
        assertTrue(formatter.contains("case SOURCE_READY, DERIVABLE -> \"Usable como fuente\""));
        assertTrue(mapper.contains("LogicalBusinessMaturityLevel.SOURCE_READY"));
        assertFalse(maturity.contains("generar artefactos derivados"));
        assertFalse(maturity.contains("producir borradores derivados"));
    }

    @Test
    void assessorAndValidationServiceShouldAvoidDerivationPromises() throws IOException {
        String assessor = read(Path.of("application", "logicalbusiness", "LogicalBusinessMaturityAssessor.java"));
        String service = read(Path.of("application", "logicalbusiness", "LogicalBusinessValidationService.java"));
        String itemPolicy = read(Path.of("application", "logicalbusiness", "LogicalBusinessItemValidationPolicy.java"));
        String glossary = read(Path.of("presentation", "logicalbusiness", "LogicalBusinessGlossary.java"));

        assertTrue(assessor.contains("hasCoreForSourceUse"));
        assertTrue(assessor.contains("LogicalBusinessMaturityLevel.SOURCE_READY"));
        assertTrue(assessor.contains("Entidades candidatas justificadas por la lógica."));
        assertTrue(assessor.contains("Identificar entidades candidatas"));
        assertTrue(service.contains("validación de coherencia interna"));
        assertTrue(service.contains("no aprueba el negocio real"));
        assertTrue(itemPolicy.contains("cómo se verificará o controlará"));
        assertTrue(glossary.contains("Validación interna"));
        assertTrue(glossary.contains("Madurez documental"));
        assertFalse(assessor.contains("hasCoreForDerivation"));
        assertFalse(assessor.contains("Entidades candidatas derivadas"));
        assertFalse(assessor.contains("Derivar entidades candidatas"));
        assertFalse(service.contains("una derivación decidan"));
    }

    private static String read(Path relative) throws IOException {
        return Files.readString(MAIN.resolve(relative));
    }
}
