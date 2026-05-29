package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl Tanda 15: Levantamiento lógico sincroniza cambios y validación con el shell. */
class LogicalBusinessShellSynchronizationSourceTest {

    private static final Path MAIN = Path.of("src/main/java/com/marcosmoreira/domainmodelstudio");

    @Test
    void compositionRootConnectsLogicalBusinessProjectChangesToShellSession() throws IOException {
        String composition = read("presentation/PresentationCompositionRoot.java");

        assertTrue(composition.contains("logicalBusinessViewModel.registerProjectChangeListener"));
        assertTrue(composition.contains("commandHandler::synchronizeLogicalBusinessEdit"));
    }

    @Test
    void logicalBusinessViewModelPublishesEditsInsteadOfRelyingOnExternalPropertyMutation() throws IOException {
        String viewModel = read("presentation/logicalbusiness/LogicalBusinessViewModel.java");
        String crud = read("presentation/logicalbusiness/LogicalBusinessCrudOperations.java");

        assertTrue(viewModel.contains("public void registerProjectChangeListener(Consumer<DiagramProject> listener)"));
        assertTrue(viewModel.contains("projectChangeListener.accept(updatedProject)"));
        assertTrue(crud.contains("viewModel.replaceDocumentFromCrud(updated"));
        assertTrue(crud.contains("Levantamiento lógico actualizado."));
    }

    @Test
    void shellSynchronizesLogicalBusinessEditsWithoutMarkingLoadsDirty() throws IOException {
        String shell = read("presentation/shell/MainShellCommandHandler.java");

        assertTrue(shell.contains("public void synchronizeLogicalBusinessEdit(DiagramProject project)"));
        assertTrue(shell.contains("if (projectSessionCoordinator.activatingProjectSession() || project == null)"));
        assertTrue(shell.contains("specializedProjectSynchronizer.synchronize(project, \"Levantamiento lógico actualizado\")"));
        assertTrue(shell.contains("refreshActiveOutputState()"));
    }

    @Test
    void globalValidationRoutesLogicalBusinessBeforeConceptualFallback() throws IOException {
        String coordinator = read("presentation/shell/ProjectValidationCoordinator.java");

        assertTrue(coordinator.contains("private final LogicalBusinessViewModel logicalBusinessViewModel;"));
        assertTrue(coordinator.contains("if (logicalBusinessViewModel.active())"));
        assertTrue(coordinator.contains("validateLogicalBusiness();"));
        assertTrue(coordinator.contains("Validación del levantamiento lógico"));
        assertTrue(coordinator.contains("logicalBusinessViewModel.validationIssues()"));
    }

    private static String read(String relativePath) throws IOException {
        return Files.readString(MAIN.resolve(relativePath), StandardCharsets.UTF_8);
    }
}
