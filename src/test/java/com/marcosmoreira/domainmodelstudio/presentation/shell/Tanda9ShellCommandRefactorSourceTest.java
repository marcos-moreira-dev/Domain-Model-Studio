package com.marcosmoreira.domainmodelstudio.presentation.shell;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíles de Tanda 9 para extraer estado de sesión e historial fuera del shell principal. */
class Tanda9ShellCommandRefactorSourceTest {

    private static final Path SHELL = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellCommandHandler.java");
    private static final Path SESSION_COORDINATOR = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/ProjectSessionCoordinator.java");
    private static final Path HISTORY_COORDINATOR = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/ProjectHistoryCoordinator.java");

    @Test
    void mainShellShouldDelegateSessionStateToDedicatedCoordinator() throws Exception {
        String shell = Files.readString(SHELL);
        String coordinator = Files.readString(SESSION_COORDINATOR);

        assertAll(
                () -> assertTrue(shell.contains("private final ProjectSessionCoordinator projectSessionCoordinator"),
                        "El shell debe tener un coordinador explícito de sesiones."),
                () -> assertFalse(shell.contains("new LinkedHashMap<>()"),
                        "El mapa de sesiones ya no debe vivir como detalle interno del shell principal."),
                () -> assertFalse(shell.contains("private String activeProjectTabId"),
                        "La pestaña activa debe vivir en ProjectSessionCoordinator."),
                () -> assertFalse(shell.contains("private int nextProjectTabNumber"),
                        "El contador de pestañas debe vivir en ProjectSessionCoordinator."),
                () -> assertFalse(shell.contains("private boolean activatingProjectSession"),
                        "El guard de activación debe vivir en ProjectSessionCoordinator."),
                () -> assertTrue(coordinator.contains("Map<String, ProjectSession> sessionsByTabId")),
                () -> assertTrue(coordinator.contains("createProjectSession")),
                () -> assertTrue(coordinator.contains("createPlaceholderSession")),
                () -> assertTrue(coordinator.contains("runActivating")));
    }

    @Test
    void undoRedoShouldDelegateToHistoryCoordinator() throws Exception {
        String shell = Files.readString(SHELL);
        String coordinator = Files.readString(HISTORY_COORDINATOR);

        assertAll(
                () -> assertTrue(shell.contains("private final ProjectHistoryCoordinator projectHistoryCoordinator")),
                () -> assertTrue(shell.contains("projectHistoryCoordinator.requestUndo()")),
                () -> assertTrue(shell.contains("projectHistoryCoordinator.requestRedo()")),
                () -> assertFalse(shell.contains("Optional<DiagramProject> restored = session.undo"),
                        "La mecánica de undo no debe volver al shell principal."),
                () -> assertFalse(shell.contains("Optional<DiagramProject> restored = session.redo"),
                        "La mecánica de redo no debe volver al shell principal."),
                () -> assertTrue(coordinator.contains("void requestUndo()")),
                () -> assertTrue(coordinator.contains("void requestRedo()")),
                () -> assertTrue(coordinator.contains("restoreProjectFromHistory")));
    }

    @Test
    void mainShellShouldKeepShrinkingAfterEnterpriseRefactorStart() throws Exception {
        long lines = Files.readAllLines(SHELL).size();
        assertTrue(lines < 980, "MainShellCommandHandler debe bajar de 980 líneas tras Tanda 9; líneas actuales: " + lines);
    }
}
