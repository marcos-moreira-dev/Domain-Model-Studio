package com.marcosmoreira.domainmodelstudio.presentation.shell.commands;

import com.marcosmoreira.domainmodelstudio.application.ApplicationServices;
import com.marcosmoreira.domainmodelstudio.presentation.canvas.DiagramCanvasViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.inspector.InspectorViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.exportable.ActiveOutputProvider;
import com.marcosmoreira.domainmodelstudio.presentation.shell.MainShellState;
import com.marcosmoreira.domainmodelstudio.presentation.sidebar.ModelTreeViewModel;
import java.util.Objects;

/**
 * Dependencias compartidas por los manejadores de comandos del shell.
 *
 * <p>Este objeto evita pasar muchas dependencias a cada handler y mantiene a
 * {@code MainShellCommandHandler} como fachada de coordinación, no como lugar donde
 * se concentra toda la lógica concreta de cada acción.</p>
 */
public record ShellCommandContext(
        MainShellState shellState,
        ApplicationServices applicationServices,
        ModelTreeViewModel modelTreeViewModel,
        DiagramCanvasViewModel canvasViewModel,
        InspectorViewModel inspectorViewModel,
        ActiveOutputProvider activeOutputProvider
) {

    public ShellCommandContext {
        Objects.requireNonNull(shellState, "shellState");
        Objects.requireNonNull(applicationServices, "applicationServices");
        Objects.requireNonNull(modelTreeViewModel, "modelTreeViewModel");
        Objects.requireNonNull(canvasViewModel, "canvasViewModel");
        Objects.requireNonNull(inspectorViewModel, "inspectorViewModel");
        Objects.requireNonNull(activeOutputProvider, "activeOutputProvider");
    }
}
