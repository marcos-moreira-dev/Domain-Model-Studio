package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl de Tanda 3: el levantamiento lógico usa árbol de expediente en el SideDock. */
class LogicalBusinessTreeStructureSourceTest {

    private static final Path MAIN = Path.of("src/main/java");

    @Test
    void structurePanelUsesTreeViewAndUnifiedSelection() throws IOException {
        String panel = read("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessStructurePanel.java");
        String factory = read("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessTreeModelFactory.java");

        assertTrue(panel.contains("TreeView<LogicalBusinessTreeNode>"));
        assertTrue(panel.contains("LogicalBusinessTreeModelFactory.build"));
        assertTrue(panel.contains("applyTreeSelection"));
        assertTrue(factory.contains("LogicalBusinessSelection.group"));
        assertTrue(factory.contains("LogicalBusinessSelection.item"));
        assertTrue(factory.contains("LogicalBusinessSelection.entity"));
        assertTrue(factory.contains("LogicalBusinessSelection.attribute"));
        assertTrue(factory.contains("LogicalBusinessSelection.relationship"));
        assertTrue(factory.contains("LogicalBusinessSelection.pendingQuestion"));
        assertFalse(factory.contains("LogicalBusinessSelection.derivation"));
        assertTrue(factory.contains("LogicalBusinessSelection.maturity"));
    }

    @Test
    void structurePanelNoLongerUsesSectionButtonList() throws IOException {
        String panel = read("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessStructurePanel.java");

        assertTrue(panel.contains("Expandir"));
        assertTrue(panel.contains("Contraer"));
        assertFalse(panel.contains("logical-business-side-button"));
    }

    @Test
    void treeKeepsWorkspaceWideByLivingInsideSideDockOnly() throws IOException {
        String editor = read("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessEditorView.java");
        String panel = read("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessStructurePanel.java");

        assertTrue(editor.contains("SideDockModuleId.SECTIONS") || editor.contains("structurePanel.root()"));
        assertFalse(editor.contains("SplitPane"));
        assertFalse(panel.contains("BorderPane"));
    }

    private static String read(String relativePath) throws IOException {
        return Files.readString(MAIN.resolve(relativePath), StandardCharsets.UTF_8);
    }
}
