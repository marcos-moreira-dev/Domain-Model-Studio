package com.marcosmoreira.domainmodelstudio.presentation.shell.commands;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.util.Objects;

/** Comandos de navegación visual y selección del lienzo. */
public final class ViewCommandHandler {

    private final ShellCommandContext context;

    public ViewCommandHandler(ShellCommandContext context) {
        this.context = Objects.requireNonNull(context, "context");
    }

    public void requestZoomIn() {
        context.canvasViewModel().requestZoomIn();
        context.shellState().updateStatus("Zoom aumentado.");
    }

    public void requestZoomOut() {
        context.canvasViewModel().requestZoomOut();
        context.shellState().updateStatus("Zoom reducido.");
    }

    public void requestResetZoom() {
        context.canvasViewModel().requestResetZoom();
        context.shellState().updateStatus("Zoom restablecido a 100%.");
    }

    public void requestFitToContent() {
        if (context.canvasViewModel().currentProject() == null) {
            context.shellState().updateStatus("No hay diagrama abierto para ajustar la vista.");
            return;
        }
        context.canvasViewModel().requestFitToContent();
        context.shellState().updateStatus("Vista ajustada al contenido del diagrama.");
    }

    public void requestCenterDiagram() {
        if (context.canvasViewModel().currentProject() == null) {
            context.shellState().updateStatus("No hay diagrama abierto para centrar la vista.");
            return;
        }
        context.canvasViewModel().requestCenterDiagram();
        context.shellState().updateStatus("Vista centrada en el diagrama sin cambiar el zoom.");
    }

    public void requestCenterSelection() {
        if (context.canvasViewModel().currentProject() == null) {
            context.shellState().updateStatus("No hay diagrama abierto para centrar una selección.");
            return;
        }
        if (context.canvasViewModel().selectedElementId() == null) {
            context.shellState().updateStatus("Selecciona una entidad, atributo, relación o conector para centrar la vista.");
            return;
        }
        context.canvasViewModel().requestCenterSelection();
        context.shellState().updateStatus("Vista centrada en la selección actual.");
    }

    public void requestSelectAllElements() {
        DiagramProject project = context.canvasViewModel().currentProject();
        if (project == null) {
            context.shellState().updateStatus("No hay proyecto abierto para seleccionar elementos.");
            return;
        }
        context.canvasViewModel().selectAllVisibleElements();
        context.inspectorViewModel().refreshFromSelection();
        int count = context.canvasViewModel().selectionCount();
        if (count == 0) {
            context.shellState().updateStatus("No hay elementos visibles para seleccionar.");
        } else if (count == 1) {
            context.shellState().updateStatus("1 elemento seleccionado.");
        } else {
            context.shellState().updateStatus(count + " elementos seleccionados.");
        }
    }

    public void requestClearSelection() {
        if (context.canvasViewModel().currentProject() == null) {
            context.shellState().updateStatus("No hay proyecto abierto para limpiar la selección.");
            return;
        }
        if (context.canvasViewModel().selectionCount() == 0) {
            context.shellState().updateStatus("No hay elementos seleccionados.");
            return;
        }
        context.canvasViewModel().clearSelection();
        context.inspectorViewModel().refreshFromSelection();
        context.shellState().updateStatus("Selección limpiada.");
    }
}
