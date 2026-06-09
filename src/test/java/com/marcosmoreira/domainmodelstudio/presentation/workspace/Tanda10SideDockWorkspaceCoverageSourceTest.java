package com.marcosmoreira.domainmodelstudio.presentation.workspace;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Guardarraíl de la tanda 10 actualizado: todos los workspaces evitan paneles conceptuales genéricos. */
class Tanda10SideDockWorkspaceCoverageSourceTest {

    private static final Path MAIN_SHELL_VIEW = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellView.java");
    private static final Path DESCRIPTOR_CATALOG = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/workspace/DefaultWorkspaceDescriptorCatalog.java");

    private static final List<Path> VISUAL_EDITORS = List.of(
            Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/modulemap/ModuleMapEditorView.java"),
            Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassDiagramEditorView.java"),
            Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/screenflow/ScreenFlowEditorView.java"),
            Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/wireframe/WireframeEditorView.java"),
            Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/architecture/ArchitectureDiagramEditorView.java"),
            Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/behavior/BehaviorDiagramEditorView.java"),
            Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/freegraph/FreeGraphEditorView.java")
    );

    private static final List<Path> STRUCTURED_EDITORS = List.of(
            Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/datadictionary/DataDictionaryEditorView.java"),
            Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/rolespermissions/RolesPermissionsEditorView.java"),
            Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessEditorView.java")
    );

    @Test
    void noWorkspaceUsesGenericConceptualSidePanelsAfterConceptualMigration() throws IOException {
        String catalog = Files.readString(DESCRIPTOR_CATALOG, StandardCharsets.UTF_8);

        for (WorkspaceKind kind : WorkspaceKind.values()) {
            assertDescriptorPanelFlag(catalog, kind, false);
        }
    }

    @Test
    void specializedVisualEditorsUseCanonicalDiagramWorkbench() throws IOException {
        for (Path editor : VISUAL_EDITORS) {
            String source = Files.readString(editor, StandardCharsets.UTF_8);
            assertTrue(source.contains("presentation.workbench.DiagramWorkbenchView"), editor + " debe importar DiagramWorkbenchView.");
            assertTrue(source.contains("new DiagramWorkbenchView"), editor + " debe montar el workbench visual canónico.");
            assertFalse(source.contains("DiagramCanvasView"), editor + " no debe caer al canvas conceptual.");
            assertFalse(source.contains("ModelTreeView"), editor + " no debe usar árbol conceptual legacy.");
            assertFalse(source.contains("InspectorView"), editor + " no debe usar inspector conceptual legacy.");
        }
    }

    @Test
    void structuredEditorsUseCanonicalStructuredWorkbench() throws IOException {
        for (Path editor : STRUCTURED_EDITORS) {
            String source = Files.readString(editor, StandardCharsets.UTF_8);
            assertTrue(source.contains("presentation.workbench.StructuredWorkbenchView"), editor + " debe importar StructuredWorkbenchView.");
            assertTrue(source.contains("new StructuredWorkbenchView"), editor + " debe montar el workbench estructurado canónico.");
            assertFalse(source.contains("DiagramCanvasView"), editor + " no debe caer al canvas conceptual.");
        }
    }

    @Test
    void shellRegistersEverySpecializedWorkspaceWithItsOwnRoot() throws IOException {
        String source = Files.readString(MAIN_SHELL_VIEW, StandardCharsets.UTF_8);

        assertTrue(source.contains(".register(workspaceDescriptor(WorkspaceKind.CONCEPTUAL_CANVAS), canvasRoot)"));
        assertTrue(source.contains("new ConceptualEditorView"));
        assertTrue(source.contains(".register(workspaceDescriptor(WorkspaceKind.FREE_GRAPH_DIAGRAM), freeGraphRoot)"));
        assertTrue(source.contains(".register(workspaceDescriptor(WorkspaceKind.LOGICAL_BUSINESS_DOCUMENT), logicalBusinessRoot)"));

        for (WorkspaceKind kind : WorkspaceKind.values()) {
            assertTrue(
                    source.contains(".register(workspaceDescriptor(WorkspaceKind." + kind.name() + ")"),
                    kind.name() + " debe estar registrado en MainShellView.");
        }
        assertFalse(source.contains(".register(workspaceDescriptor(WorkspaceKind.FREE_GRAPH_DIAGRAM), canvasRoot)"));
        assertFalse(source.contains(".register(workspaceDescriptor(WorkspaceKind.UML_CLASS_DIAGRAM), canvasRoot)"));
        assertFalse(source.contains(".register(workspaceDescriptor(WorkspaceKind.MODULE_MAP_DIAGRAM), canvasRoot)"));
    }

    private static void assertDescriptorPanelFlag(String catalog, WorkspaceKind kind, boolean expected) {
        int start = catalog.indexOf("WorkspaceKind." + kind.name());
        assertTrue(start >= 0, kind.name() + " debe existir en DefaultWorkspaceDescriptorCatalog.");
        int end = catalog.indexOf("));", start);
        assertTrue(end > start, kind.name() + " debe cerrar su descriptor.");
        String block = catalog.substring(start, end);
        assertTrue(block.contains(expected ? "true" : "false"),
                kind.name() + " debe declarar usesGenericConceptualSidePanels=" + expected + ".");
    }
}
