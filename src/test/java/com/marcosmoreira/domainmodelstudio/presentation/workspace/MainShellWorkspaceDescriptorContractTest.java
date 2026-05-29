package com.marcosmoreira.domainmodelstudio.presentation.workspace;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/**
 * Regresión estática para impedir que el shell vuelva a registrar roots sin contrato común.
 */
class MainShellWorkspaceDescriptorContractTest {

    private static final Path MAIN_SHELL_VIEW = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellView.java");

    @Test
    void shellMustUseDescriptorCatalogForWorkspaceRegistration() throws IOException {
        String source = Files.readString(MAIN_SHELL_VIEW);

        assertTrue(source.contains("new DefaultWorkspaceDescriptorCatalog()"));
        assertTrue(source.contains("workspaceDescriptorCatalog.descriptorFor(kind)"));
    }

    @Test
    void everyMountedWorkspaceMustBeRegisteredWithDescriptor() throws IOException {
        String source = Files.readString(MAIN_SHELL_VIEW);

        for (WorkspaceKind kind : WorkspaceKind.values()) {
            assertTrue(
                    source.contains(".register(workspaceDescriptor(WorkspaceKind." + kind.name() + ")"),
                    kind.name() + " debe registrarse con WorkspaceDescriptor, no solo con Parent.");
        }
    }

    @Test
    void homeTabMustMountWelcomeWorkspaceInsteadOfPlaceholder() throws IOException {
        String source = Files.readString(MAIN_SHELL_VIEW);

        assertTrue(source.contains("new WelcomeWorkspaceView(viewModel.canvasViewModel())"));
        assertTrue(source.contains(".register(workspaceDescriptor(WorkspaceKind.WELCOME_HOME), welcomeRoot)"));
        assertTrue(source.contains("homeTabActive()"));
    }
}
