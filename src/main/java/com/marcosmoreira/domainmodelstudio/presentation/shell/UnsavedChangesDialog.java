package com.marcosmoreira.domainmodelstudio.presentation.shell;

import java.util.Collection;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

/**
 * Diálogo de confirmación para acciones que pueden descartar cambios de un proyecto abierto.
 */
final class UnsavedChangesDialog {

    private final MainShellState shellState;
    private final BooleanSupplier saveAction;

    UnsavedChangesDialog(MainShellState shellState, BooleanSupplier saveAction) {
        this.shellState = Objects.requireNonNull(shellState, "shellState");
        this.saveAction = Objects.requireNonNull(saveAction, "saveAction");
    }

    boolean confirmBeforeApplicationExit(Collection<ProjectSession> sessions) {
        boolean anyDirty = sessions.stream().anyMatch(session -> session.dirty);
        if (!anyDirty) {
            return true;
        }
        ButtonType discard = new ButtonType("Salir sin guardar", ButtonBar.ButtonData.NO);
        ButtonType cancel = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cambios sin guardar");
        alert.setHeaderText("Hay pestañas con cambios sin guardar.");
        alert.setContentText("Guarda las pestañas necesarias antes de salir o continúa sin guardar.");
        alert.getButtonTypes().setAll(discard, cancel);
        return alert.showAndWait().orElse(cancel) == discard;
    }

    boolean confirmBefore(String actionDescription) {
        if (!shellState.hasUnsavedChanges()) {
            return true;
        }
        ButtonType save = new ButtonType("Guardar", ButtonBar.ButtonData.YES);
        ButtonType discard = new ButtonType("No guardar", ButtonBar.ButtonData.NO);
        ButtonType cancel = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cambios sin guardar");
        alert.setHeaderText("¿Deseas guardar los cambios del proyecto actual?");
        alert.setContentText("Antes de " + actionDescription
                + ", puedes guardar el proyecto, continuar sin guardar o cancelar la acción.");
        alert.getButtonTypes().setAll(save, discard, cancel);

        ButtonType selected = alert.showAndWait().orElse(cancel);
        if (selected == save) {
            return saveAction.getAsBoolean();
        }
        return selected == discard;
    }
}
