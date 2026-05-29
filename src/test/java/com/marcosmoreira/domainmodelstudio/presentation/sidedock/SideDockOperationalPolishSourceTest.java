package com.marcosmoreira.domainmodelstudio.presentation.sidedock;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíles de pulido operativo del SideDock transversal. */
class SideDockOperationalPolishSourceTest {

    @Test
    void graphStructurePanelsShouldUseExternalSideDockScrollAsDominantScrollbar() throws IOException {
        String freeGraph = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/freegraph/FreeGraphStructurePanel.java");
        String logicalGraph = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/logicalbusinessgraph/LogicalBusinessGraphStructurePanel.java");

        assertTrue(freeGraph.contains("configureListViewForExternalSideDockScroll(nodeList)"));
        assertTrue(freeGraph.contains("configureListViewForExternalSideDockScroll(edgeList)"));
        assertTrue(logicalGraph.contains("configureListViewForExternalSideDockScroll(nodeList)"));
        assertTrue(logicalGraph.contains("configureListViewForExternalSideDockScroll(edgeList)"));
        assertTrue(logicalGraph.contains("configureListViewForExternalSideDockScroll(issueList)"));
        assertFalse(logicalGraph.contains("side-dock-content-owns-scroll"));
    }

    @Test
    void dataDictionaryIndexShouldUseSideDockWorkspaceInsteadOfManualCollapse() throws IOException {
        String source = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/datadictionary/DataDictionaryEditorView.java");

        assertTrue(source.contains("configureListViewForExternalSideDockScroll(entityList)"));
        assertFalse(source.contains("entityIndexToggleButton"));
        assertFalse(source.contains("entityIndexCollapsed"));
        assertFalse(source.contains("Contraer índice"));
        assertFalse(source.contains("Expandir índice"));
        assertFalse(source.contains("entityList.setVisible(!entityIndexCollapsed)"));
    }

    @Test
    void behaviorStructureShouldShowOnlyRelevantTabsForSequenceAndNonSequenceDiagrams() throws IOException {
        String source = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/behavior/BehaviorStructurePanel.java");

        assertTrue(source.contains("refreshContextualTabs"));
        assertTrue(source.contains("viewModel.currentKind() == BehaviorDiagramKind.UML_SEQUENCE"));
        assertTrue(source.contains("tabs.getTabs().remove(relationsTab)"));
        assertTrue(source.contains("tabs.getTabs().remove(sequenceTab)"));
        assertTrue(source.contains("tabs.getTabs().add(sequenceTab)"));
        assertTrue(source.contains("tabs.getTabs().add(relationsTab)"));
    }

    private static String read(String path) throws IOException {
        return java.nio.file.Files.readString(Path.of(path), StandardCharsets.UTF_8);
    }
}
