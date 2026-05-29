package com.marcosmoreira.domainmodelstudio.presentation.shell;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/**
 * Guardarraíles estáticos de trazabilidad para el ruteo de editores especializados.
 *
 * <p>La prueba evita que el shell vuelva a crecer con cadenas de condiciones para cargar o
 * limpiar workspaces. La coordinación debe estar en {@link SpecializedWorkspaceCoordinator}.</p>
 */
class SpecializedWorkspaceCoordinatorSourceTest {

    private static final Path MAIN_SHELL_COMMAND_HANDLER = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellCommandHandler.java");
    private static final Path COORDINATOR = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/SpecializedWorkspaceCoordinator.java");

    @Test
    void mainShellCommandHandlerMustDelegateSpecializedWorkspaceLoading() throws IOException {
        String shellSource = Files.readString(MAIN_SHELL_COMMAND_HANDLER);

        assertTrue(shellSource.contains("private final SpecializedWorkspaceCoordinator specializedWorkspaces;"));
        assertTrue(shellSource.contains("specializedWorkspaces.loadIfSpecialized(project)"));
        assertTrue(shellSource.contains("specializedWorkspaces.clearAll()"));
        assertTrue(shellSource.contains("specializedWorkspaces.firstActiveProject()"));
    }

    @Test
    void showProjectInEditorShouldNotContainSpecializedTypeChain() throws IOException {
        String shellSource = Files.readString(MAIN_SHELL_COMMAND_HANDLER);
        int start = shellSource.indexOf("private void showProjectInEditor");
        int end = shellSource.indexOf("private void clearAllProjectViews", start);
        String method = shellSource.substring(start, end);

        assertFalse(method.contains("DiagramTypeId.DATA_DICTIONARY"));
        assertFalse(method.contains("DiagramTypeId.ADMIN_MODULE_MAP"));
        assertFalse(method.contains("DiagramTypeId.UML_CLASS"));
        assertFalse(method.contains("DiagramTypeId.ADMIN_WIREFRAMES"));
        assertFalse(method.contains("isBehaviorDiagramType"));
        assertFalse(method.contains("isArchitectureDiagramType"));
    }

    @Test
    void coordinatorMustOwnBehaviorAndArchitectureFamilyRouting() throws IOException {
        String coordinatorSource = Files.readString(COORDINATOR);

        assertTrue(coordinatorSource.contains("BehaviorDiagramKind.values()"));
        assertTrue(coordinatorSource.contains("ArchitectureDiagramKind::supports"));
        assertTrue(coordinatorSource.contains("record SpecializedWorkspaceBinding"));
    }
}
