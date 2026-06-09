package com.marcosmoreira.domainmodelstudio.presentation.shell;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíles fuente para evitar que el shell vuelva a absorber responsabilidades extraídas. */
class MainShellSrpRefactorSourceTest {

    private static final Path MAIN_SHELL = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellCommandHandler.java");
    private static final Path NEW_PROJECT_FACTORY = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/NewProjectFactory.java");
    private static final Path PROJECT_CREATION_COORDINATOR = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/ProjectCreationCoordinator.java");
    private static final Path PROJECT_OPEN_COORDINATOR = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/ProjectOpenCoordinator.java");
    private static final Path PROJECT_SESSION = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/ProjectSession.java");
    private static final Path VALIDATION_PRESENTER = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/ValidationDialogPresenter.java");
    private static final Path CLIENT_BATCH_EXPORT_COORDINATOR = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/ClientBatchExportCoordinator.java");
    private static final Path PROJECT_VALIDATION_COORDINATOR = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/ProjectValidationCoordinator.java");

    @Test
    void mainShellMustDelegateNewProjectConstruction() throws IOException {
        String shell = Files.readString(MAIN_SHELL);
        String factory = Files.readString(NEW_PROJECT_FACTORY);
        String creationCoordinator = Files.readString(PROJECT_CREATION_COORDINATOR);

        assertTrue(shell.contains("private final NewProjectFactory newProjectFactory;"));
        assertTrue(shell.contains("private final ProjectCreationCoordinator projectCreationCoordinator;"));
        assertTrue(shell.contains("projectCreationCoordinator.requestNewProject()"));
        assertFalse(shell.contains("newProjectFactory.createDataDictionary"));
        assertFalse(shell.contains("newProjectFactory.createModuleMap"));
        assertFalse(shell.contains("newProjectFactory.createConceptualFallback"));

        assertFalse(shell.contains("createDataDictionaryProject("));
        assertFalse(shell.contains("createModuleMapProject("));
        assertFalse(shell.contains("createBehaviorDiagramProject("));
        assertFalse(shell.contains("stableProjectId("));

        assertTrue(creationCoordinator.contains("newProjectFactory.createDataDictionary"));
        assertTrue(creationCoordinator.contains("newProjectFactory.createModuleMap"));
        assertTrue(creationCoordinator.contains("newProjectFactory.createConceptualFallback"));
        assertTrue(factory.contains("createDataDictionary"));
        assertTrue(factory.contains("createBehaviorDiagram"));
        assertTrue(factory.contains("createArchitectureDiagram"));
        assertFalse(factory.contains("javafx."));
    }

    @Test
    void openProjectFlowMustStayInDedicatedCoordinator() throws IOException {
        String shell = Files.readString(MAIN_SHELL);
        String openCoordinator = Files.readString(PROJECT_OPEN_COORDINATOR);

        assertTrue(shell.contains("private final ProjectOpenCoordinator projectOpenCoordinator;"));
        assertTrue(shell.contains("projectOpenCoordinator.requestOpenProject()"));
        assertFalse(shell.contains("applicationServices.openProjectUseCase().open"));
        assertFalse(shell.contains("chooser.showOpenDialog"));
        assertTrue(openCoordinator.contains("applicationServices.openProjectUseCase().open"));
        assertTrue(openCoordinator.contains("chooser.showOpenDialog"));
        assertTrue(openCoordinator.contains("WorkspaceSupportDecision.PRODUCT_VIEW"));
    }

    @Test
    void projectSessionMustBeDedicatedShellStateClass() throws IOException {
        String shell = Files.readString(MAIN_SHELL);
        String session = Files.readString(PROJECT_SESSION);

        assertFalse(shell.contains("private static final class ProjectSession"));
        assertTrue(session.contains("final class ProjectSession"));
        assertTrue(session.contains("static ProjectSession forProject"));
        assertTrue(session.contains("static ProjectSession forPlaceholder"));
    }

    @Test
    void validationDialogConstructionMustStayOutOfMainShellMethods() throws IOException {
        String shell = Files.readString(MAIN_SHELL);
        String presenter = Files.readString(VALIDATION_PRESENTER);

        assertTrue(shell.contains("private final ValidationDialogPresenter validationDialogPresenter;"));
        assertTrue(shell.contains("private final ProjectValidationCoordinator projectValidationCoordinator;"));
        assertTrue(shell.contains("projectValidationCoordinator.validateActiveProject"));
        assertFalse(shell.contains("private void showValidationWarnings"));
        assertTrue(presenter.contains("new Alert(Alert.AlertType.INFORMATION)"));
    }

    @Test
    void batchExportMustStayOutsideMainShellCommandHandler() throws IOException {
        String shell = Files.readString(MAIN_SHELL);
        String coordinator = Files.readString(CLIENT_BATCH_EXPORT_COORDINATOR);

        assertTrue(shell.contains("private final ClientBatchExportCoordinator clientBatchExportCoordinator;"));
        assertTrue(shell.contains("clientBatchExportCoordinator.requestExportClientBatch()"));
        assertFalse(shell.contains("ClientBatchExportRequest request = new ClientBatchExportRequest"));
        assertFalse(shell.contains("DirectoryChooser chooser = new DirectoryChooser"));
        assertTrue(coordinator.contains("ClientBatchExportRequest request = new ClientBatchExportRequest"));
        assertTrue(coordinator.contains("exportPendingPngSnapshots"));
    }

    @Test
    void mainShellMustStayBelowSrpLineGuardrailAfterThisTanda() throws IOException {
        long lines = Files.readAllLines(MAIN_SHELL).size();
        assertTrue(lines < 1300, "MainShellCommandHandler debe seguir bajando; líneas actuales: " + lines);
    }

    @Test
    void validationCoordinatorMustOwnSpecializedValidationChain() throws IOException {
        String shell = Files.readString(MAIN_SHELL);
        String coordinator = Files.readString(PROJECT_VALIDATION_COORDINATOR);

        assertTrue(coordinator.contains("validateActiveProject"));
        assertTrue(coordinator.contains("validateDataDictionary"));
        assertTrue(coordinator.contains("validateArchitectureDiagram"));
        assertFalse(shell.contains("if (dataDictionaryViewModel.active()) {\n            requestValidateDataDictionary();"));
    }
}
