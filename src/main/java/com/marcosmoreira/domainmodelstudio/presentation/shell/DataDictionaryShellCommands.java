package com.marcosmoreira.domainmodelstudio.presentation.shell;

import com.marcosmoreira.domainmodelstudio.presentation.datadictionary.DataDictionaryViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.shell.commands.ExportCommandHandler;
import java.util.Objects;

/** Comandos del documento de diccionario de datos. */
final class DataDictionaryShellCommands {

    private final MainShellState shellState;
    private final DataDictionaryViewModel viewModel;
    private final ProjectValidationCoordinator validationCoordinator;
    private final ExportCommandHandler exportCommandHandler;

    DataDictionaryShellCommands(
            MainShellState shellState,
            DataDictionaryViewModel viewModel,
            ProjectValidationCoordinator validationCoordinator,
            ExportCommandHandler exportCommandHandler
    ) {
        this.shellState = Objects.requireNonNull(shellState, "shellState");
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
        this.validationCoordinator = Objects.requireNonNull(validationCoordinator, "validationCoordinator");
        this.exportCommandHandler = Objects.requireNonNull(exportCommandHandler, "exportCommandHandler");
    }

    void requestAddEntity() {
        if (!ensureActive("agregar entidad")) {
            return;
        }
        viewModel.addEntity();
    }

    void requestAddField() {
        if (!ensureActive("agregar campo")) {
            return;
        }
        viewModel.addField();
    }

    void requestRemoveItem() {
        if (!ensureActive("eliminar elementos")) {
            return;
        }
        viewModel.removeSelected();
    }

    void requestValidate() {
        validationCoordinator.validateDataDictionary();
    }

    void requestExportPdf() {
        exportCommandHandler.requestExportPdf();
    }

    private boolean ensureActive(String action) {
        if (viewModel.active()) {
            return true;
        }
        shellState.updateStatus("Abre un diccionario de datos para " + action + ".");
        return false;
    }
}
