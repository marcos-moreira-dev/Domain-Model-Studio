package com.marcosmoreira.domainmodelstudio.presentation.shell;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíles de la tanda 12: cambio de pestañas y workspace activo. */
class Tanda12WorkspaceTabSwitchSourceTest {

    private static final Path MAIN_SHELL_COMMAND_HANDLER = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellCommandHandler.java");
    private static final Path MAIN_SHELL_VIEW = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellView.java");

    @Test
    void activatingAProjectSessionMustClearResidualEditorsBeforeLoadingTheNewWorkspace() throws IOException {
        String source = read(MAIN_SHELL_COMMAND_HANDLER);
        String method = method(source, "private void showProjectInEditor", "private void clearAllProjectViews");

        assertContainsInOrder(method,
                "clearAllProjectViews();",
                "specializedWorkspaces.loadIfSpecialized(project)",
                "shellState.showProjectState(project, statusLabel)");
        org.junit.jupiter.api.Assertions.assertFalse(method.contains("modelTreeViewModel.loadProject(project)"),
                "El shell ya no debe tener fallback visible del modelo conceptual; la carga va por SpecializedWorkspaceCoordinator.");
        org.junit.jupiter.api.Assertions.assertFalse(method.contains("canvasViewModel.showImportedProject(project)"),
                "El canvas conceptual se carga mediante ConceptualCanvasLegacyBridge, no desde showProjectInEditor.");
    }

    @Test
    void activationMustRefreshDirtyTabAndExportScopeForEverySessionKind() throws IOException {
        String source = read(MAIN_SHELL_COMMAND_HANDLER);
        String method = method(source, "private void activateProjectSession", "private void showProjectInEditor");

        assertContainsInOrder(method,
                "if (session.isPlaceholder())",
                "clearAllProjectViews();",
                "shellState.showPlaceholderState(session.placeholder, statusLabel);",
                "refreshSessionDirtyState(session);",
                "refreshActiveOutputState();",
                "return;");
        assertContainsInOrder(method,
                "showProjectInEditor(session.project, statusLabel);",
                "refreshSessionDirtyState(session);",
                "refreshActiveOutputState();");
    }

    @Test
    void homeActivationMustClearEditorsAndExportFormatsBeforeSelectingHomeTab() throws IOException {
        String source = read(MAIN_SHELL_COMMAND_HANDLER);
        String method = method(source, "private void activateHomeTab", "private void openProjectInNewTab");

        assertContainsInOrder(method,
                "projectSessionCoordinator.activateHome();",
                "clearAllProjectViews();",
                "shellState.showNoProjectState();",
                "refreshActiveOutputState();",
                "shellState.setActiveEditorTab(MainShellState.HOME_TAB_ID);");
    }

    @Test
    void activeOutputMustBeResolvedFromTheActiveSessionAndRejectResidualEditors() throws IOException {
        String source = read(MAIN_SHELL_COMMAND_HANDLER);
        String outputMethod = method(source, "private Optional<DiagramProject> activeProjectForOutput", "private void refreshActiveOutputState");
        String sameProjectMethod = method(source, "private boolean sameSessionProject", "private Optional<DiagramProject> currentProjectFromActiveEditor");

        assertContainsInOrder(outputMethod,
                "ProjectSession session = activeSession();",
                "if (session == null || session.isPlaceholder())",
                "currentProjectFromActiveEditor()",
                ".filter(project -> sameSessionProject(session, project))",
                "editorProject.or(() -> Optional.ofNullable(session.project))");
        assertTrue(sameProjectMethod.contains("metadata().id()"), "Debe comparar el id del proyecto activo.");
        assertTrue(sameProjectMethod.contains("metadata().diagramTypeId()"), "Debe comparar el tipo del proyecto activo.");
    }

    @Test
    void viewMustRefreshWorkspaceWhenTheActiveTabOrRouteContextChanges() throws IOException {
        String source = read(MAIN_SHELL_VIEW);

        assertTrue(source.contains("installActiveEditorTabWorkspaceBehavior();"));
        assertTrue(source.contains("activeEditorTabIdProperty().addListener((observable, previous, current) -> refreshWorkAreaPanels())"));
        assertTrue(source.contains("placeholderWorkspaceProperty().addListener"));
        assertTrue(source.contains("activeDiagramTypeProperty().addListener"));
        assertTrue(source.contains("workspaceRouteResolver.resolve("));
        assertTrue(source.contains("placeholderWorkspaceActive()"));
        assertTrue(source.contains("homeTabActive()"));
        assertTrue(source.contains("workspaceViewRegistry.rootForOrFallback(route, canvasRoot)"));
    }

    private static String read(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }

    private static String method(String source, String startToken, String endToken) {
        int start = source.indexOf(startToken);
        int end = source.indexOf(endToken, start + startToken.length());
        assertTrue(start >= 0, "No se encontró inicio: " + startToken);
        assertTrue(end > start, "No se encontró cierre: " + endToken);
        return source.substring(start, end);
    }

    private static void assertContainsInOrder(String source, String... tokens) {
        int cursor = 0;
        for (String token : tokens) {
            int index = source.indexOf(token, cursor);
            assertTrue(index >= 0, "No se encontró en orden: " + token);
            cursor = index + token.length();
        }
    }
}
