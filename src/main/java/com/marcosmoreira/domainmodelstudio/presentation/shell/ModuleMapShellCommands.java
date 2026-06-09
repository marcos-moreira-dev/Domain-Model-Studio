package com.marcosmoreira.domainmodelstudio.presentation.shell;

import com.marcosmoreira.domainmodelstudio.presentation.modulemap.ModuleMapViewModel;
import java.util.Objects;

/** Comandos del mapa administrativo de módulos. */
final class ModuleMapShellCommands {

    private final MainShellState shellState;
    private final ModuleMapViewModel viewModel;
    private final ProjectValidationCoordinator validationCoordinator;

    ModuleMapShellCommands(
            MainShellState shellState,
            ModuleMapViewModel viewModel,
            ProjectValidationCoordinator validationCoordinator
    ) {
        this.shellState = Objects.requireNonNull(shellState, "shellState");
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
        this.validationCoordinator = Objects.requireNonNull(validationCoordinator, "validationCoordinator");
    }

    void requestAddModule() {
        if (!ensureActive("agregar módulo")) {
            return;
        }
        viewModel.addModule();
    }

    void requestAddSubmodule() {
        if (!ensureActive("agregar submódulo")) {
            return;
        }
        viewModel.addSubmodule();
    }

    void requestAddDependency() {
        if (!ensureActive("agregar dependencia")) {
            return;
        }
        viewModel.addDependency();
    }

    void requestRemoveItem() {
        if (!ensureActive("eliminar elementos")) {
            return;
        }
        viewModel.removeSelected();
    }

    void requestValidate() {
        validationCoordinator.validateModuleMap();
    }

    void requestRegenerateLayout() {
        viewModel.reorganizeLayout();
    }

    private boolean ensureActive(String action) {
        if (viewModel.active()) {
            return true;
        }
        shellState.updateStatus("Abre un mapa de módulos para " + action + ".");
        return false;
    }
}
