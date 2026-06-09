package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl Tanda 16: ayuda y glosario obligatorio del Levantamiento lógico. */
class LogicalBusinessHelpGlossarySourceTest {

    private static final Path MAIN = Path.of("src/main/java");
    private static final Path RESOURCES = Path.of("src/main/resources");

    @Test
    void helpPanelShouldRenderMandatoryGlossaryAlongsideContextualHelp() throws IOException {
        String panel = readJava("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessHelpPanel.java");
        String glossary = readJava("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessGlossary.java");

        assertTrue(panel.contains("LogicalBusinessContextualHelpGuide"));
        assertTrue(panel.contains("LogicalBusinessGlossary.glossarySections()"));
        assertTrue(panel.contains("Glosario obligatorio"));
        assertTrue(panel.contains("glossaryBlock"));
        assertTrue(glossary.contains("fuente lógica canónica"));
        assertTrue(glossary.contains("No sincroniza proyectos"));
        assertTrue(glossary.contains("usuario y la IA"));
    }

    @Test
    void glossaryShouldCoverCanonicalPrefixesLimitsAiUseAndMistakes() throws IOException {
        String glossary = readJava("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessGlossary.java");

        for (String token : new String[] {"MF", "FL", "CU", "ACC", "RN", "PRE", "INV", "POST", "ACT", "CON", "EVID", "SUP", "ENT", "ATR", "REL", "EST", "REP", "CALC", "RISK", "PEND"}) {
            assertTrue(glossary.contains('"' + token + '"'), "Falta prefijo canónico en glosario: " + token);
        }
        assertTrue(glossary.contains("No genera todo"));
        assertTrue(glossary.contains("No valida otros tipos"));
        assertTrue(glossary.contains("Uso con IA"));
        assertTrue(glossary.contains("Reutilizar IDs"));
        assertTrue(glossary.contains("Errores comunes"));
        assertTrue(glossary.contains("Convertir supuestos en reglas"));
        assertFalse(glossary.contains("ATTR"));
    }

    @Test
    void glossaryCssShouldUseStraightPanelsWithoutRadius() throws IOException {
        String css = Files.readString(RESOURCES.resolve("css/logical-business-side-panels-extra.css"), StandardCharsets.UTF_8);

        assertTrue(css.contains("logical-business-glossary-section"));
        assertTrue(css.contains("logical-business-glossary-entry"));
        assertTrue(css.contains("logical-business-help-boundary"));
        assertTrue(css.contains("logical-business-help-ai"));
        assertTrue(css.contains("logical-business-help-warning"));
        assertFalse(css.contains("-fx-border-radius"));
        assertFalse(css.contains("-fx-background-radius"));
    }

    @Test
    void academicGuideShouldUseCandidateLanguageForLogicalEntities() throws IOException {
        String topic = Files.readString(RESOURCES.resolve("help/topics/logical-business-intake.md"), StandardCharsets.UTF_8);

        assertTrue(topic.contains("## Entidades y atributos candidatos"));
        assertTrue(topic.contains("fuente lógica canónica"));
        assertTrue(topic.contains("Responsabilidad de alineación semántica"));
        assertFalse(topic.contains("## Entidades y atributos derivados"));
        assertFalse(topic.contains("diagramas son vistas derivadas"));
    }

    private static String readJava(String relativePath) throws IOException {
        return Files.readString(MAIN.resolve(relativePath), StandardCharsets.UTF_8);
    }
}
