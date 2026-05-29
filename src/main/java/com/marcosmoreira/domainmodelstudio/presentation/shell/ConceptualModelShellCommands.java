package com.marcosmoreira.domainmodelstudio.presentation.shell;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.presentation.shell.commands.DirtyStateUpdater;
import com.marcosmoreira.domainmodelstudio.presentation.shell.commands.ShellCommandContext;
import java.util.Objects;

/** Comandos del modelo conceptual clásico usados desde la barra contextual. */
final class ConceptualModelShellCommands {

    private final ShellCommandContext context;
    private final DirtyStateUpdater dirtyStateUpdater;

    ConceptualModelShellCommands(ShellCommandContext context, DirtyStateUpdater dirtyStateUpdater) {
        this.context = Objects.requireNonNull(context, "context");
        this.dirtyStateUpdater = Objects.requireNonNull(dirtyStateUpdater, "dirtyStateUpdater");
    }

    void synchronizeStructuralEditFromCanvas(DiagramProject project) {
        if (project == null) {
            return;
        }
        context.modelTreeViewModel().loadProject(project);
        context.inspectorViewModel().refreshFromSelection();
        context.shellState().showProjectState(project, "Proyecto en edición");
        dirtyStateUpdater.markCurrentSessionDirty();
        context.shellState().updateStatus("Modelo conceptual actualizado.");
    }

    void requestAddEntityTool() {
        if (!hasOpenProject("agregar entidad")) {
            return;
        }
        context.canvasViewModel().beginAddEntityTool();
        context.shellState().updateStatus("Herramienta Entidad activa: haz clic en el lienzo.");
    }

    void requestAddAttributeToSelectedEntity() {
        if (!hasOpenProject("agregar atributo")) {
            return;
        }
        context.canvasViewModel().addAttributeToSelectedEntity();
        refreshModelViewsAfterCanvasEdit();
    }

    void requestAddRelationshipTool() {
        if (!hasOpenProject("agregar relación")) {
            return;
        }
        context.canvasViewModel().beginAddRelationshipTool();
        context.shellState().updateStatus("Herramienta Relación activa: selecciona origen y destino.");
    }

    void requestDuplicateSelectedEntity() {
        if (!hasOpenProject("duplicar entidad")) {
            return;
        }
        context.canvasViewModel().duplicateSelectedEntity();
        refreshModelViewsAfterCanvasEdit();
    }

    void requestRemoveSelectedElement() {
        if (!hasOpenProject("eliminar elementos")) {
            return;
        }
        try {
            context.canvasViewModel().removeSelectedElement();
            refreshModelViewsAfterCanvasEdit();
        } catch (IllegalArgumentException exception) {
            context.shellState().updateStatus("No se pudo eliminar: " + exception.getMessage());
        }
    }

    void requestDeleteSelectedBendPoint() {
        if (!context.canvasViewModel().removeSelectedBendPoint()) {
            context.shellState().updateStatus("Selecciona un punto intermedio de una línea para eliminarlo.");
        }
    }

    private boolean hasOpenProject(String action) {
        if (context.canvasViewModel().currentProject() != null) {
            return true;
        }
        context.shellState().updateStatus("No hay proyecto abierto para " + action + ".");
        return false;
    }

    private void refreshModelViewsAfterCanvasEdit() {
        DiagramProject project = context.canvasViewModel().currentProject();
        if (project == null) {
            return;
        }
        context.modelTreeViewModel().loadProject(project);
        context.inspectorViewModel().refreshFromSelection();
        context.shellState().showProjectState(project, "Proyecto en edición");
        dirtyStateUpdater.markCurrentSessionDirty();
    }
}
