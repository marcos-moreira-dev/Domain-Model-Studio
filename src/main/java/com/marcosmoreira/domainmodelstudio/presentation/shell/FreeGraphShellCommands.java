package com.marcosmoreira.domainmodelstudio.presentation.shell;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.presentation.freegraph.FreeGraphViewModel;
import java.util.Objects;

/** Comandos del grafo libre. */
final class FreeGraphShellCommands {

    private final FreeGraphViewModel viewModel;
    private final ProjectValidationCoordinator validationCoordinator;
    private final SpecializedProjectSynchronizer projectSynchronizer;
    private final EditorActivationGuard activationGuard;

    FreeGraphShellCommands(
            FreeGraphViewModel viewModel,
            ProjectValidationCoordinator validationCoordinator,
            SpecializedProjectSynchronizer projectSynchronizer,
            EditorActivationGuard activationGuard
    ) {
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
        this.validationCoordinator = Objects.requireNonNull(validationCoordinator, "validationCoordinator");
        this.projectSynchronizer = Objects.requireNonNull(projectSynchronizer, "projectSynchronizer");
        this.activationGuard = Objects.requireNonNull(activationGuard, "activationGuard");
    }

    void synchronizeEdit(DiagramProject updatedProject) {
        projectSynchronizer.synchronize(updatedProject, "Grafo libre actualizado");
    }

    void requestActivateSelectTool() {
        if (!ensureActive("seleccionar elementos")) {
            return;
        }
        viewModel.activateSelectionTool();
    }

    void requestActivateAddNodeTool() {
        if (!ensureActive("crear nodos por clic")) {
            return;
        }
        viewModel.toggleAddNodeTool();
    }

    void requestActivateAddEdgeTool() {
        if (!ensureActive("crear relaciones por clic")) {
            return;
        }
        viewModel.toggleAddEdgeTool();
    }

    void requestAddNode() {
        if (!ensureActive("agregar nodos")) {
            return;
        }
        viewModel.addNode();
    }

    void requestAddEdge() {
        if (!ensureActive("agregar relaciones")) {
            return;
        }
        viewModel.addEdge();
    }

    void requestRemoveItem() {
        if (!ensureActive("eliminar elementos")) {
            return;
        }
        viewModel.removeSelected();
    }

    void requestValidate() {
        validationCoordinator.validateFreeGraph();
    }

    void requestRegenerateLayout() {
        viewModel.reorganizeLayout();
    }

    private boolean ensureActive(String action) {
        return activationGuard.ensureActive(viewModel.active(), "Grafo libre", action);
    }
}
