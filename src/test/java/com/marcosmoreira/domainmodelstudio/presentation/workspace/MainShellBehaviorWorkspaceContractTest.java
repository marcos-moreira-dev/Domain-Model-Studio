package com.marcosmoreira.domainmodelstudio.presentation.workspace;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/**
 * Regresión de integración visual para la familia BPMN/UML de comportamiento.
 *
 * <p>Esta prueba evita que el shell vuelva a rutear estos diagramas a la pantalla de inicio
 * o al lienzo conceptual por olvidar registrar {@code BehaviorDiagramEditorView}.</p>
 */
class MainShellBehaviorWorkspaceContractTest {

    private static final Path MAIN_SHELL_VIEW = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellView.java");
    private static final Path DEFAULT_TOOLBAR_PROVIDER = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/toolbar/DefaultDiagramToolbarActionProvider.java");

    @Test
    void shellMustMountBehaviorDiagramEditorViewForBehaviorWorkspace() throws IOException {
        String source = Files.readString(MAIN_SHELL_VIEW);

        assertTrue(
                source.contains("new BehaviorDiagramEditorView(viewModel.behaviorDiagramViewModel())"),
                "MainShellView debe construir la vista visual de comportamiento.");
        assertTrue(
                source.contains(".register(workspaceDescriptor(WorkspaceKind.BEHAVIOR_DIAGRAM), behaviorRoot)"),
                "MainShellView debe registrar BehaviorDiagramEditorView con el descriptor BEHAVIOR_DIAGRAM.");
    }

    @Test
    void exportMenuMustUseCapabilityPolicyInsteadOfHardcodedTypeExclusions() throws IOException {
        String source = Files.readString(DEFAULT_TOOLBAR_PROVIDER);

        assertTrue(source.contains("new DiagramCapabilityPresentationPolicy()"));
        assertTrue(source.contains("capabilityPolicy.shouldExposeToolbarAction"));
    }
}
