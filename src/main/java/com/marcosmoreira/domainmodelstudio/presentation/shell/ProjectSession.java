package com.marcosmoreira.domainmodelstudio.presentation.shell;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.presentation.placeholder.PlaceholderWorkspaceViewModel;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Estado de una pestaña de proyecto abierta en el shell.
 *
 * <p>La sesión pertenece al ciclo de vida del shell, no al modelo de dominio. Conserva
 * el proyecto activo o la vista de preparación asociada a la pestaña, junto con el
 * estado de cambios sin guardar.</p>
 */
final class ProjectSession {

    final String tabId;
    DiagramProject project;
    final PlaceholderWorkspaceViewModel placeholder;
    boolean dirty;
    Path projectFile;
    private final ProjectEditHistory editHistory = new ProjectEditHistory();

    private ProjectSession(
            String tabId,
            DiagramProject project,
            PlaceholderWorkspaceViewModel placeholder,
            boolean dirty,
            Path projectFile
    ) {
        this.tabId = Objects.requireNonNull(tabId, "tabId");
        this.project = Objects.requireNonNull(project, "project");
        this.placeholder = placeholder;
        this.dirty = dirty;
        this.projectFile = projectFile;
    }

    static ProjectSession forProject(String tabId, DiagramProject project, boolean dirty) {
        return new ProjectSession(tabId, project, null, dirty, null);
    }

    static ProjectSession forProject(String tabId, DiagramProject project, boolean dirty, Path projectFile) {
        return new ProjectSession(tabId, project, null, dirty, projectFile);
    }

    static ProjectSession forPlaceholder(
            String tabId,
            DiagramProject project,
            PlaceholderWorkspaceViewModel placeholder,
            boolean dirty
    ) {
        return new ProjectSession(
                tabId,
                project,
                Objects.requireNonNull(placeholder, "placeholder"),
                dirty,
                null);
    }

    boolean isPlaceholder() {
        return placeholder != null;
    }

    String title() {
        if (isPlaceholder()) {
            return placeholder.title();
        }
        return project.metadata().title();
    }

    void rememberEditBefore(DiagramProject updatedProject) {
        if (!isPlaceholder()) {
            editHistory.rememberBeforeEdit(project, updatedProject);
        }
    }

    void rememberEditTransition(DiagramProject previousProject, DiagramProject updatedProject) {
        if (!isPlaceholder()) {
            editHistory.rememberBeforeEdit(previousProject, updatedProject);
        }
    }

    java.util.Optional<DiagramProject> undo(DiagramProject currentProject) {
        return isPlaceholder() ? java.util.Optional.empty() : editHistory.undo(currentProject);
    }

    java.util.Optional<DiagramProject> redo(DiagramProject currentProject) {
        return isPlaceholder() ? java.util.Optional.empty() : editHistory.redo(currentProject);
    }

    boolean canUndo() {
        return !isPlaceholder() && editHistory.canUndo();
    }

    boolean canRedo() {
        return !isPlaceholder() && editHistory.canRedo();
    }

    @Override
    public String toString() {
        String type = project == null || project.metadata() == null || project.metadata().diagramTypeId() == null
                ? "sin tipo"
                : project.metadata().diagramTypeId().value();
        return title() + " — " + type + (dirty ? " *" : "");
    }
}

