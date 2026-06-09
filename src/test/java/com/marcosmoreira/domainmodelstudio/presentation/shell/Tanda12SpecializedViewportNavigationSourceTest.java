package com.marcosmoreira.domainmodelstudio.presentation.shell;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Protege fit/center al alternar pestañas con proyectos especializados del mismo tipo. */
class Tanda12SpecializedViewportNavigationSourceTest {

    private static final Path MAIN_SHELL_COMMAND_HANDLER = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellCommandHandler.java");
    private static final Path SPECIALIZED_WORKSPACE_COORDINATOR = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/SpecializedWorkspaceCoordinator.java");

    @Test
    void shellMustPassTheActiveProjectToViewportNavigationNotOnlyTheDiagramType() throws IOException {
        String source = read(MAIN_SHELL_COMMAND_HANDLER);

        assertTrue(source.contains("specializedWorkspaces.fitActiveDiagram(activeProject.get())"));
        assertTrue(source.contains("specializedWorkspaces.centerActiveDiagram(activeProject.get())"));
        assertFalse(source.contains("fitActiveDiagram(activeProject.get().metadata().diagramTypeId())"));
        assertFalse(source.contains("centerActiveDiagram(activeProject.get().metadata().diagramTypeId())"));
    }

    @Test
    void coordinatorMustMatchTheActiveProjectBeforeRunningViewportActions() throws IOException {
        String source = read(SPECIALIZED_WORKSPACE_COORDINATOR);

        assertTrue(source.contains("boolean fitActiveDiagram(DiagramProject activeProject)"));
        assertTrue(source.contains("boolean centerActiveDiagram(DiagramProject activeProject)"));
        assertTrue(source.contains("private Optional<SpecializedWorkspaceBinding> activeViewportBinding(DiagramProject activeProject)"));
        assertTrue(source.contains(".filter(binding -> binding.supports(diagramTypeId))"));
        assertTrue(source.contains(".filter(SpecializedWorkspaceBinding::isActive)"));
        assertTrue(source.contains(".filter(SpecializedWorkspaceBinding::canNavigateViewport)"));
        assertTrue(source.contains(".filter(binding -> sameWorkspaceProject(binding.currentProject(), activeProject))"));
        assertTrue(source.contains("private static boolean sameWorkspaceProject"));
        assertTrue(source.contains("candidate.metadata().id()"));
        assertTrue(source.contains("activeProject.metadata().id()"));
        assertTrue(source.contains("candidate.metadata().diagramTypeId()"));
        assertTrue(source.contains("activeProject.metadata().diagramTypeId()"));
        assertFalse(source.contains("boolean fitActiveDiagram(DiagramTypeId diagramTypeId)"));
        assertFalse(source.contains("boolean centerActiveDiagram(DiagramTypeId diagramTypeId)"));
    }

    private static String read(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
