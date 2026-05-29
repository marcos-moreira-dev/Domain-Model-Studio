package com.marcosmoreira.domainmodelstudio.presentation.conceptual;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíles de Tanda 4: conceptual se monta en workbench común preservando su canvas. */
class ConceptualWorkspaceCommonShellSourceTest {

    private static final Path MAIN_SHELL_VIEW = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellView.java");
    private static final Path SPECIALIZED_COORDINATOR = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/SpecializedWorkspaceCoordinator.java");
    private static final Path CONTRIBUTOR = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/conceptual/ConceptualWorkbenchContributor.java");
    private static final Path BRIDGE = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/conceptual/ConceptualCanvasLegacyBridge.java");
    private static final Path DESCRIPTOR_CATALOG = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/workspace/DefaultWorkspaceDescriptorCatalog.java");

    @Test
    void conceptualWorkspaceUsesCanonicalWorkbenchButKeepsLegacyDrawing() throws IOException {
        String contributor = Files.readString(CONTRIBUTOR, StandardCharsets.UTF_8);

        assertTrue(contributor.contains("implements DiagramWorkbenchContributor"));
        assertTrue(contributor.contains("DiagramWorkbenchDescriptor.migratedVisualDiagram"));
        assertTrue(contributor.contains("new DiagramCanvasView"),
                "Tanda 4 debe preservar el dibujo conceptual actual; no migrar todavía a InteractiveCanvasSurfaceView.");
        assertTrue(contributor.contains("new ModelTreeView"),
                "La estructura legacy se encapsula en SideDock/workbench, no se borra todavía.");
        assertTrue(contributor.contains("new InspectorView"),
                "Propiedades legacy se encapsulan en SideDock/workbench, no se borran todavía.");
        assertFalse(contributor.contains("InteractiveCanvasSurfaceView"));
    }

    @Test
    void shellRegistersConceptualEditorViewInsteadOfRawCanvasRoot() throws IOException {
        String shell = Files.readString(MAIN_SHELL_VIEW, StandardCharsets.UTF_8);

        assertTrue(shell.contains("new ConceptualEditorView"));
        assertTrue(shell.contains(".register(workspaceDescriptor(WorkspaceKind.CONCEPTUAL_CANVAS), canvasRoot)"));
        assertFalse(shell.contains("new DiagramCanvasView(viewModel.canvasViewModel())"),
                "El shell ya no debe montar el canvas conceptual crudo como root del workspace.");
    }

    @Test
    void conceptualIsLoadedThroughSpecializedWorkspaceCoordinator() throws IOException {
        String coordinator = Files.readString(SPECIALIZED_COORDINATOR, StandardCharsets.UTF_8);
        String bridge = Files.readString(BRIDGE, StandardCharsets.UTF_8);

        assertTrue(coordinator.contains("ConceptualCanvasLegacyBridge"));
        assertTrue(coordinator.contains("DiagramTypeId.CONCEPTUAL_MODEL::equals"));
        assertTrue(bridge.contains("modelTreeViewModel.loadProject(project)"));
        assertTrue(bridge.contains("canvasViewModel.showImportedProject(project)"));
        assertTrue(bridge.contains("inspectorViewModel.refreshFromSelection()"));
    }

    @Test
    void genericConceptualSidePanelsAreDisabledForTheNewRoute() throws IOException {
        String catalog = Files.readString(DESCRIPTOR_CATALOG, StandardCharsets.UTF_8);

        int start = catalog.indexOf("WorkspaceKind.CONCEPTUAL_CANVAS");
        int end = catalog.indexOf("));", start);
        String block = catalog.substring(start, end);
        assertTrue(block.contains("false"), "CONCEPTUAL_CANVAS ya no debe usar paneles genéricos del shell.");
        assertTrue(block.contains("workspace común"));
    }
}
