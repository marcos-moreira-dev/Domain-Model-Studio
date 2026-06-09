package com.marcosmoreira.domainmodelstudio.presentation.sidedock;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Guardarraíl UX-1C-mini: mínimo visual de ocho filas sin registros ficticios. */
class SideDockCollectionSizingPolicySourceTest {

    private static final Path POLICY = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/sidedock/SideDockCollectionSizingPolicy.java");

    private static final List<Path> PANELS = List.of(
            Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/logicalbusinessgraph/LogicalBusinessGraphStructurePanel.java"),
            Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/freegraph/FreeGraphStructurePanel.java"),
            Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassStructurePanel.java"),
            Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassPropertiesPanel.java"),
            Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/behavior/BehaviorStructurePanel.java"),
            Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/behavior/BehaviorPropertiesPanel.java"),
            Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/modulemap/ModuleMapStructurePanel.java"),
            Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/modulemap/ModuleMapPropertiesPanel.java"),
            Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/screenflow/ScreenFlowStructurePanel.java"),
            Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/wireframe/WireframeStructurePanel.java"),
            Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/datadictionary/DataDictionaryEditorView.java"),
            Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/rolespermissions/RolesPermissionsStructurePanel.java"),
            Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/rolespermissions/RolesPermissionsEditorView.java"),
            Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/architecture/ArchitectureStructurePanel.java"),
            Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/architecture/ArchitecturePropertiesPanel.java")
    );

    @Test
    void policyMustReserveEightVisibleRowsWithoutTouchingItems() throws IOException {
        String source = Files.readString(POLICY, StandardCharsets.UTF_8);

        assertTrue(source.contains("MIN_VISIBLE_ROWS = 8"));
        assertTrue(source.contains("configureListView(ListView<?> listView)"));
        assertTrue(source.contains("configureTableView(TableView<?> tableView)"));
        assertTrue(source.contains("setMinHeight"));
        assertTrue(source.contains("setPrefHeight"));
        assertTrue(source.contains("configureListViewForExternalSideDockScroll(ListView<?> listView)"));
        assertTrue(source.contains("configureTableViewForExternalSideDockScroll(TableView<?> tableView)"));
        assertTrue(source.contains("addListener"));
        assertFalse(source.contains("getItems().add("));
        assertFalse(source.contains("setItems("));
    }

    @Test
    void sideDockPanelsMustUseTheSizingPolicyInsteadOfLocalTinyHeights() throws IOException {
        for (Path panel : PANELS) {
            String source = Files.readString(panel, StandardCharsets.UTF_8);
            assertTrue(source.contains("SideDockCollectionSizingPolicy"), panel + " debe usar la política transversal.");
        }
    }
}
