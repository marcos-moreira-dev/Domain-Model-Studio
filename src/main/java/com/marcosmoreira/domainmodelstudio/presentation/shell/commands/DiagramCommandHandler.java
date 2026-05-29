package com.marcosmoreira.domainmodelstudio.presentation.shell.commands;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import com.marcosmoreira.domainmodelstudio.domain.validation.ValidationResult;
import java.util.Objects;
import javafx.scene.control.Alert;

/** Comandos propios del diagrama activo: validación, ordenamiento y notación. */
public final class DiagramCommandHandler {

    private final ShellCommandContext context;
    private final DirtyStateUpdater dirtyStateUpdater;

    public DiagramCommandHandler(ShellCommandContext context, DirtyStateUpdater dirtyStateUpdater) {
        this.context = Objects.requireNonNull(context, "context");
        this.dirtyStateUpdater = Objects.requireNonNull(dirtyStateUpdater, "dirtyStateUpdater");
    }

    public void requestValidateProject() {
        DiagramProject project = context.canvasViewModel().currentProject();
        if (project == null) {
            context.shellState().updateStatus("No hay proyecto abierto para validar.");
            return;
        }
        ValidationResult result = context.applicationServices().projectServices().validateProjectUseCase().validate(project);
        String summary;
        if (result.hasErrors()) {
            summary = "Validación del modelo: " + result.errors().size()
                    + " errores y " + result.warnings().size() + " advertencias.";
        } else if (result.hasWarnings()) {
            summary = "Validación del modelo: sin errores, "
                    + result.warnings().size() + " advertencias.";
        } else {
            summary = "Validación del modelo: sin errores ni advertencias.";
        }
        context.shellState().updateStatus(summary);
        showValidationDialog(result, summary);
    }

    public void requestRegenerateLayout() {
        DiagramProject project = context.canvasViewModel().currentProject();
        if (project == null) {
            context.shellState().updateStatus("No hay proyecto abierto para reorganizar el diagrama.");
            return;
        }
        try {
            DiagramProject updated = project.metadata().activeNotation() == NotationType.CROWS_FOOT
                    ? context.applicationServices().visualServices().generateInitialCrowsFootLayoutUseCase().generate(project)
                    : context.applicationServices().visualServices().generateInitialChenLayoutUseCase().generate(project);
            context.canvasViewModel().applyEditedProject(updated);
            context.modelTreeViewModel().loadProject(updated);
            context.inspectorViewModel().refreshFromSelection();
            context.shellState().showProjectState(updated, "Proyecto en edición");
            dirtyStateUpdater.markCurrentSessionDirty();
            context.shellState().updateStatus("Diagrama reorganizado para la vista "
                    + displayNotation(updated.metadata().activeNotation()) + ".");
        } catch (IllegalArgumentException exception) {
            context.shellState().updateStatus("No se pudo reorganizar el diagrama: " + exception.getMessage());
        }
    }

    public void requestSwitchNotation(NotationType notationType) {
        DiagramProject project = context.canvasViewModel().currentProject();
        if (project == null) {
            context.shellState().updateStatus("No hay proyecto abierto para cambiar de notación.");
            return;
        }
        try {
            DiagramProject updated = context.applicationServices().visualServices().switchNotationUseCase().switchTo(project, notationType);
            boolean notationChanged = project.metadata().activeNotation() != updated.metadata().activeNotation();
            boolean layoutWasMissing = project.layouts().layoutFor(notationType).isEmpty();
            context.canvasViewModel().replaceCurrentProject(updated);
            context.modelTreeViewModel().loadProject(updated);
            context.inspectorViewModel().refreshFromSelection();
            context.shellState().showProjectState(updated, "Proyecto en edición");
            if (notationChanged || layoutWasMissing) {
                dirtyStateUpdater.markCurrentSessionDirty();
            }
            context.shellState().updateStatus("Notación activa: " + displayNotation(updated.metadata().activeNotation()) + ".");
        } catch (IllegalArgumentException exception) {
            context.shellState().updateStatus("No se pudo cambiar de notación: " + exception.getMessage());
        }
    }

    private void showValidationDialog(ValidationResult result, String summary) {
        StringBuilder details = new StringBuilder(summary).append("\n\n");
        if (!result.errors().isEmpty()) {
            details.append("Errores:\n");
            result.errors().stream().limit(8).forEach(issue -> details
                    .append("- ").append(issue.message()).append("\n"));
            if (result.errors().size() > 8) {
                details.append("- ...").append(result.errors().size() - 8).append(" errores más.\n");
            }
            details.append("\n");
        }
        if (!result.warnings().isEmpty()) {
            details.append("Advertencias:\n");
            result.warnings().stream().limit(8).forEach(issue -> details
                    .append("- ").append(issue.message()).append("\n"));
            if (result.warnings().size() > 8) {
                details.append("- ...").append(result.warnings().size() - 8).append(" advertencias más.\n");
            }
        }
        if (result.errors().isEmpty() && result.warnings().isEmpty()) {
            details.append("El modelo está listo para guardarse o exportarse.");
        }
        Alert alert = new Alert(result.hasErrors() ? Alert.AlertType.ERROR
                : result.hasWarnings() ? Alert.AlertType.WARNING
                : Alert.AlertType.INFORMATION);
        alert.setTitle("Resultado de validación");
        alert.setHeaderText(summary);
        alert.setContentText(details.toString());
        alert.showAndWait();
    }

    private String displayNotation(NotationType notationType) {
        return notationType == NotationType.CROWS_FOOT ? "Pata de gallo" : "Chen";
    }
}
