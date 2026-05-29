package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl de Tanda 4: ficha central amplia y CSS propio del levantamiento lógico. */
class LogicalBusinessFichaCentralSourceTest {

    private static final Path MAIN = Path.of("src/main/java");
    private static final Path RESOURCES = Path.of("src/main/resources");

    @Test
    void documentViewRendersFichaCentralInsteadOfFlatTextOnly() throws IOException {
        String view = readJava("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessDocumentView.java");
        String controls = readJava("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessFormControls.java");
        String itemPresentation = readJava("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessItemPresentation.java");

        assertTrue(view.contains("logical-business-expedient-header"));
        assertTrue(controls.contains("logical-business-ficha"));
        assertTrue(controls.contains("logical-business-field"));
        assertTrue(controls.contains("logical-business-chip"));
        assertTrue(controls.contains("logical-business-reference-list"));
        assertTrue(controls.contains("metricRow"));
        assertTrue(itemPresentation.contains("kindReading"));
    }

    @Test
    void cssIsDedicatedImportedAndDocumented() throws IOException {
        String appLight = Files.readString(RESOURCES.resolve("css/app-light.css"), StandardCharsets.UTF_8);
        String css = Files.readString(RESOURCES.resolve("css/logical-business.css"), StandardCharsets.UTF_8);

        assertTrue(appLight.contains("logical-business.css"));
        assertTrue(css.contains("Levantamiento lógico — base documental del workspace"));
        assertTrue(css.contains("Ficha central del resultado"));
        assertTrue(css.contains("Árbol del expediente en SideDock"));
        assertTrue(css.contains("Paneles laterales existentes del módulo"));
    }

    @Test
    void logicalBusinessCssKeepsDesktopStraightEdges() throws IOException {
        String css = Files.readString(RESOURCES.resolve("css/logical-business.css"), StandardCharsets.UTF_8);

        assertFalse(css.contains("-fx-border-radius"));
        assertFalse(css.contains("-fx-background-radius"));
    }

    @Test
    void workspaceIsStillWideAndDoesNotIntroduceRightInspector() throws IOException {
        String editor = readJava("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessEditorView.java");
        String view = readJava("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessDocumentView.java");

        assertFalse(editor.contains("SplitPane"));
        assertFalse(view.contains("SplitPane"));
        assertFalse(view.contains("BorderPane"));
    }

    private static String readJava(String relativePath) throws IOException {
        return Files.readString(MAIN.resolve(relativePath), StandardCharsets.UTF_8);
    }
}
