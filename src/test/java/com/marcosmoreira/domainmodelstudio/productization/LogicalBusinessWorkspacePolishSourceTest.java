package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl de Tanda 10: workspace continuo, árbol natural y ayuda operativa. */
class LogicalBusinessWorkspacePolishSourceTest {

    private static final Path MAIN = Path.of("src/main/java");
    private static final Path RESOURCES = Path.of("src/main/resources");

    @Test
    void workspaceUsesContinuousDocumentSurfaceWithBlueDesktopHeader() throws IOException {
        String baseCss = readResource("css/logical-business-base.css");
        String documentCss = readResource("css/logical-business-document.css");
        String view = readJava("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessDocumentView.java");

        assertTrue(baseCss.contains("#ffffff"));
        assertTrue(baseCss.contains("linear-gradient(to bottom, #0d629b"));
        assertTrue(baseCss.contains("-fx-text-fill: #ffffff"));
        assertTrue(documentCss.contains("-fx-border-width: 1 0 0 0"));
        assertTrue(view.contains("content.setPadding(Insets.EMPTY)"));
        assertFalse(baseCss.contains("-dms-bg-workspace"));
    }

    @Test
    void treeUsesNaturalSectionOrderingSingleClickExpansionAndContextualHelp() throws IOException {
        String panel = readJava("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessStructurePanel.java");
        String factory = readJava("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessTreeModelFactory.java");
        String help = readJava("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessTreeHelpGuide.java");

        assertTrue(panel.contains("Contraer"));
        assertTrue(panel.contains("treeView.setOnMouseClicked"));
        assertTrue(panel.contains("!item.isExpanded()"));
        assertTrue(panel.contains("ClickMessageDialog.showInfo"));
        assertTrue(panel.contains("logical-business-tree-help-button"));
        assertTrue(factory.contains("LogicalBusinessSectionOrdering::index"));
        assertTrue(help.contains("precondiciones, invariantes y postcondiciones"));
        assertTrue(help.contains("reutilizar como fuente"));
    }

    @Test
    void visibleTextsHideMarkdownFencesAndSeparateAcademicAndOperationalHelpLabels() throws IOException {
        String displayText = readJava("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessDisplayText.java");
        String uiNodes = readJava("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessUiNodes.java");
        String shell = readJava("com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellView.java");
        String manual = readJava("com/marcosmoreira/domainmodelstudio/presentation/dialogs/ManualDialog.java");

        assertTrue(displayText.contains("trimmed.equals(\"```\")"));
        assertTrue(displayText.contains("matches(\"```[A-Za-z0-9_-]*\")"));
        assertTrue(uiNodes.contains("LogicalBusinessDisplayText.clean"));
        assertTrue(shell.contains("Guía académica"));
        assertTrue(manual.contains("Guía académica de Domain Model Studio"));
        assertFalse(shell.contains("Ayuda de diagramas"));
        assertFalse(shell.contains("MenuItem manual = new MenuItem(\"Guía operativa\")"));
        assertFalse(manual.contains("Guía operativa de Domain Model Studio"));
    }

    private static String readJava(String relativePath) throws IOException {
        return Files.readString(MAIN.resolve(relativePath), StandardCharsets.UTF_8);
    }

    private static String readResource(String relativePath) throws IOException {
        return Files.readString(RESOURCES.resolve(relativePath), StandardCharsets.UTF_8);
    }
}
