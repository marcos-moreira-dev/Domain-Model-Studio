package com.marcosmoreira.domainmodelstudio.presentation.shell;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíles de Tanda 29 para que el shell delegue creación/apertura de proyectos. */
class MainShellProjectLifecycleCoordinatorSourceTest {

    private static final Path SHELL_PACKAGE = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell");
    private static final Path MAIN_SHELL = SHELL_PACKAGE.resolve("MainShellCommandHandler.java");
    private static final Path CREATION = SHELL_PACKAGE.resolve("ProjectCreationCoordinator.java");
    private static final Path OPEN = SHELL_PACKAGE.resolve("ProjectOpenCoordinator.java");

    @Test
    void mainShellDelegatesCreationAndOpeningToDedicatedCoordinators() throws IOException {
        String shell = read(MAIN_SHELL);

        assertTrue(shell.contains("private final ProjectCreationCoordinator projectCreationCoordinator;"));
        assertTrue(shell.contains("private final ProjectOpenCoordinator projectOpenCoordinator;"));
        assertTrue(shell.contains("projectCreationCoordinator.requestNewProject()"));
        assertTrue(shell.contains("projectOpenCoordinator.requestOpenProject()"));
        assertFalse(shell.contains("NewProjectDialog.showAndWait"));
        assertFalse(shell.contains("applicationServices.openProjectUseCase().open"));
        assertFalse(shell.contains("chooser.showOpenDialog"));
    }

    @Test
    void projectCreationCoordinatorOwnsNewProjectDialogAndFactoryRouting() throws IOException {
        String creation = read(CREATION);

        assertTrue(creation.contains("NewProjectDialog.showAndWait"));
        assertTrue(creation.contains("CreateWorkspaceRequest"));
        assertTrue(creation.contains("WorkspaceSupportDecision.PLANNING_VIEW"));
        assertTrue(creation.contains("newProjectFactory.createDataDictionary"));
        assertTrue(creation.contains("newProjectFactory.createLogicalBusinessIntake"));
        assertTrue(creation.contains("newProjectFactory.createConceptualFallback"));
        assertTrue(creation.contains("ProjectOpenTarget"));
    }

    @Test
    void projectOpenCoordinatorOwnsDmsChooserAndWorkspaceDecision() throws IOException {
        String open = read(OPEN);

        assertTrue(open.contains("new FileChooser()"));
        assertTrue(open.contains("Proyecto Domain Model Studio (*.dms)"));
        assertTrue(open.contains("applicationServices.openProjectUseCase().open"));
        assertTrue(open.contains("WorkspaceSupportDecision.PRODUCT_VIEW"));
        assertTrue(open.contains("WorkspaceSupportDecision.PLANNING_VIEW"));
        assertTrue(open.contains("target.openProject(project, \"Proyecto abierto\", false, projectFile)"));
    }

    @Test
    void mainShellMustKeepShrinkingAfterProjectLifecycleExtraction() throws IOException {
        long lines = Files.lines(MAIN_SHELL).count();
        assertTrue(lines < 900, "MainShellCommandHandler debe quedar bajo 900 líneas tras Tanda 29; líneas actuales: " + lines);
    }

    private String read(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
