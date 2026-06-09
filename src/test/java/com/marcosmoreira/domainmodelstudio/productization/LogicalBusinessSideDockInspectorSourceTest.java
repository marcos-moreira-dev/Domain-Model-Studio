package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl de Tanda 5: los inspectores viven como módulos del SideDock, no como columna fija. */
class LogicalBusinessSideDockInspectorSourceTest {

    private static final Path MAIN = Path.of("src/main/java");
    private static final Path RESOURCES = Path.of("src/main/resources");

    @Test
    void inspectorsRemainSideDockModulesAndDoNotCompressWorkspace() throws IOException {
        String editor = readJava("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessEditorView.java");
        String documentView = readJava("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessDocumentView.java");

        assertTrue(editor.contains("SideDockModuleId.VALIDATION"));
        assertTrue(editor.contains("SideDockModuleId.TRACEABILITY"));
        assertFalse(editor.contains("SideDockModuleId.DERIVATIONS"));
        assertFalse(editor.contains("SplitPane"));
        assertFalse(documentView.contains("BorderPane"));
        assertFalse(documentView.contains("SplitPane"));
    }

    @Test
    void inspectorPanelsUseSideDockScrollAndFilterBySelection() throws IOException {
        String validation = readJava("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessValidationPanel.java");
        String traceability = readJava("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessTraceabilityPanel.java");
        assertFalse(validation.contains("new ScrollPane"));
        assertFalse(traceability.contains("new ScrollPane"));
        assertTrue(validation.contains("issuesForFocus"));
        assertTrue(traceability.contains("selectedTraceabilityId"));
    }

    @Test
    void inspectorCssIsDocumentedAndKeepsStraightDesktopEdges() throws IOException {
        String css = Files.readString(RESOURCES.resolve("css/logical-business.css"), StandardCharsets.UTF_8);

        assertTrue(css.contains("Inspectores laterales: validación, impacto/dependencias y ayuda/glosario"));
        assertTrue(css.contains("logical-business-inspector-card"));
        assertTrue(css.contains("logical-business-inspector-blocking"));
        assertFalse(css.contains("-fx-border-radius"));
        assertFalse(css.contains("-fx-background-radius"));
    }

    private static String readJava(String relativePath) throws IOException {
        return Files.readString(MAIN.resolve(relativePath), StandardCharsets.UTF_8);
    }
}
