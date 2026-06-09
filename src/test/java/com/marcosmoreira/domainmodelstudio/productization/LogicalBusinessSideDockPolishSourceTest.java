package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl de Tanda 8: pulido UX del SideDock del Levantamiento lógico. */
class LogicalBusinessSideDockPolishSourceTest {

    private static final Path MAIN = Path.of("src/main/java");
    private static final Path RESOURCES = Path.of("src/main/resources");

    @Test
    void structurePanelOffersTreeActionsAndOwnsItsSingleScroll() throws IOException {
        String panel = readJava("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessStructurePanel.java");
        String frame = readJava("com/marcosmoreira/domainmodelstudio/presentation/sidedock/SideDockModuleFrameFactory.java");

        assertTrue(panel.contains("Expandir"));
        assertTrue(panel.contains("Contraer"));
        assertTrue(panel.contains("side-dock-content-owns-scroll"));
        assertTrue(panel.contains("markerStyleClass"));
        assertTrue(frame.contains("content instanceof ScrollPane"));
        assertTrue(frame.contains("side-dock-content-owns-scroll"));
    }

    @Test
    void propertiesAndHelpAvoidRedundantReadOnlyTextBoxes() throws IOException {
        String properties = readJava("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessPropertiesPanel.java");
        String help = readJava("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessHelpPanel.java");

        assertFalse(properties.contains("LogicalBusinessUiNodes.title(\"Propiedades\")"));
        assertFalse(help.contains("LogicalBusinessUiNodes.title(\"Ayuda\")"));
        assertFalse(properties.contains("new TextField"));
        assertFalse(properties.contains("new TextArea"));
        assertFalse(help.contains("new TextArea"));
    }

    @Test
    void cssIsSplitAndKeepsStraightColoredDesktopIndicators() throws IOException {
        String css = Files.readString(RESOURCES.resolve("css/logical-business.css"), StandardCharsets.UTF_8);
        String sideCss = Files.readString(RESOURCES.resolve("css/logical-business-side-panels.css"), StandardCharsets.UTF_8);

        assertTrue(css.contains("logical-business-side-panels.css"));
        assertTrue(css.contains("logical-business-document.css"));
        assertTrue(sideCss.contains("logical-business-marker-warning"));
        assertTrue(sideCss.contains("logical-business-list-button"));
        assertFalse(sideCss.contains("-fx-border-radius"));
        assertFalse(sideCss.contains("-fx-background-radius"));
    }

    private static String readJava(String relativePath) throws IOException {
        return Files.readString(MAIN.resolve(relativePath), StandardCharsets.UTF_8);
    }
}
