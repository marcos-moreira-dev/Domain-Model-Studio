package com.marcosmoreira.domainmodelstudio.presentation.shell;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíles fuente para el historial por pestaña/proyecto abierto. */
class ProjectHistorySourceTest {

    private static final Path PROJECT_SESSION = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/ProjectSession.java");
    private static final Path PROJECT_HISTORY = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/ProjectEditHistory.java");
    private static final Path MAIN_SHELL = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellCommandHandler.java");
    private static final Path PROJECT_HISTORY_COORDINATOR = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/ProjectHistoryCoordinator.java");
    private static final Path SPECIALIZED_SYNCHRONIZER = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/SpecializedProjectSynchronizer.java");
    private static final Path GLOBAL_TOOLBAR = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/toolbar/GlobalToolbarView.java");
    private static final Path MAIN_VIEW = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellView.java");

    @Test
    void projectSessionMustOwnIndependentUndoRedoHistory() throws IOException {
        String session = Files.readString(PROJECT_SESSION);
        String history = Files.readString(PROJECT_HISTORY);

        assertTrue(session.contains("private final ProjectEditHistory editHistory"));
        assertTrue(session.contains("rememberEditBefore"));
        assertTrue(session.contains("rememberEditTransition"));
        assertTrue(session.contains("undo(DiagramProject currentProject)"));
        assertTrue(session.contains("redo(DiagramProject currentProject)"));
        assertTrue(history.contains("undoStack"));
        assertTrue(history.contains("redoStack"));
        assertTrue(history.contains("MAX_ENTRIES"));
    }

    @Test
    void shellUndoRedoMustUseActiveSessionHistoryNotOnlyConceptualCanvas() throws IOException {
        String shell = Files.readString(MAIN_SHELL);
        String historyCoordinator = Files.readString(PROJECT_HISTORY_COORDINATOR);

        assertTrue(shell.contains("projectHistoryCoordinator.requestUndo()"));
        assertTrue(shell.contains("projectHistoryCoordinator.requestRedo()"));
        assertTrue(historyCoordinator.contains("session.undo(currentProject)"));
        assertTrue(historyCoordinator.contains("session.redo(currentProject)"));
        assertTrue(historyCoordinator.contains("restoreProjectFromHistory"));
        assertFalse(shell.contains("canvasViewModel.undo()"));
        assertFalse(shell.contains("canvasViewModel.redo()"));
        assertFalse(historyCoordinator.contains("canvasViewModel.undo()"));
        assertFalse(historyCoordinator.contains("canvasViewModel.redo()"));
    }

    @Test
    void specializedEditorsMustRegisterHistoryBeforeReplacingSessionProject() throws IOException {
        String synchronizer = Files.readString(SPECIALIZED_SYNCHRONIZER);
        String shell = Files.readString(MAIN_SHELL);

        assertTrue(synchronizer.contains("session.rememberEditBefore(updatedProject)"));
        assertTrue(shell.contains("session.rememberEditTransition(previous, current)"));
    }

    @Test
    void undoRedoButtonsMustBeAvailableForEverySaveableProject() throws IOException {
        String toolbar = Files.readString(GLOBAL_TOOLBAR);
        String view = Files.readString(MAIN_VIEW);

        assertTrue(toolbar.contains("viewModel.saveableProjectClosed(), ToolbarIcon.UNDO_CHANGE"));
        assertTrue(toolbar.contains("viewModel.saveableProjectClosed(), ToolbarIcon.REDO_CHANGE"));
        assertTrue(view.contains("commandItem(\"Deshacer cambio\", viewModel::undo, \"Shortcut+Z\", viewModel.saveableProjectClosed())"));
        assertTrue(view.contains("commandItem(\"Rehacer cambio\", viewModel::redo, \"Shortcut+Y\", viewModel.saveableProjectClosed())"));
    }
}
