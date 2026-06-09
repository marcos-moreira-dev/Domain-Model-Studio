package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl de Tanda 9: ayuda contextual breve y cierre documental sin contaminar el workspace. */
class LogicalBusinessContextualHelpSourceTest {

    private static final Path MAIN = Path.of("src/main/java");
    private static final Path RESOURCES = Path.of("src/main/resources");
    private static final Path DOCS = Path.of("docs/desarrollo");

    @Test
    void helpPanelShouldReactToSelectionAndAvoidReadOnlyTextBoxes() throws IOException {
        String panel = readJava("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessHelpPanel.java");
        String editor = readJava("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessEditorView.java");

        assertTrue(panel.contains("LogicalBusinessContextualHelpGuide"));
        assertTrue(panel.contains("LogicalBusinessGlossary.glossarySections()"));
        assertTrue(panel.contains("Glosario obligatorio"));
        assertTrue(panel.contains("selectionProperty().addListener"));
        assertTrue(panel.contains("selectedItem().orElse(null)"));
        assertTrue(panel.contains("Cierre documental"));
        assertTrue(editor.contains("new LogicalBusinessHelpPanel(viewModel)"));
        assertFalse(panel.contains("new TextArea"));
        assertFalse(panel.contains("new TextField"));
    }

    @Test
    void guideShouldExplainKeyFocusTypesAndClosingCriteria() throws IOException {
        String guide = readJava("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessContextualHelpGuide.java");

        assertTrue(guide.contains("ACTION"));
        assertTrue(guide.contains("INVARIANT"));
        assertTrue(guide.contains("PENDING_QUESTION"));
        assertFalse(guide.contains("DERIVATION"));
        assertFalse(guide.contains("compatibleArtifactHelp"));
        assertTrue(guide.contains("Maturity" ) || guide.contains("madurez"));
        assertTrue(guide.contains("Cierre documental"));
        assertTrue(guide.contains("postcondición"));
        assertTrue(guide.contains("fuente lógica"));
        assertTrue(guide.contains("Markdown"));
        assertTrue(guide.contains("revisión humana"));
    }

    @Test
    void academicHelpAndCssShouldSupportContextualClosingWithoutRoundedPanels() throws IOException {
        String topic = Files.readString(RESOURCES.resolve("help/topics/logical-business-intake.md"), StandardCharsets.UTF_8);
        String sideCss = Files.readString(RESOURCES.resolve("css/logical-business-side-panels.css"), StandardCharsets.UTF_8);
        String doc = Files.readString(DOCS.resolve("TANDA_015_CONTRATO_ALCANCE_GUIA_LEVANTAMIENTO_LOGICO.md"), StandardCharsets.UTF_8);

        assertTrue(topic.contains("## Cierre documental y validación humana"));
        assertTrue(topic.contains("Checklist mínimo de cierre"));
        assertTrue(sideCss.contains("logical-business-help-block"));
        assertTrue(sideCss.contains("logical-business-help-closing"));
        assertTrue(doc.contains("SideDock"));
        assertTrue(doc.contains("Cierre documental") || doc.contains("validación humana"));
        assertFalse(sideCss.contains("-fx-border-radius"));
        assertFalse(sideCss.contains("-fx-background-radius"));
    }

    private static String readJava(String relativePath) throws IOException {
        return Files.readString(MAIN.resolve(relativePath), StandardCharsets.UTF_8);
    }
}
