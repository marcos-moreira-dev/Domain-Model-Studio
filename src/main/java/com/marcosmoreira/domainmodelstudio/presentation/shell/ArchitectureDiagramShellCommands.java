package com.marcosmoreira.domainmodelstudio.presentation.shell;

import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureEdgeKind;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureNodeKind;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.presentation.architecture.ArchitectureDiagramViewModel;
import java.util.Objects;

/** Comandos de C4, arquitectura técnica y despliegue. */
final class ArchitectureDiagramShellCommands {

    private final MainShellState shellState;
    private final ArchitectureDiagramViewModel viewModel;
    private final ProjectValidationCoordinator validationCoordinator;
    private final SpecializedProjectSynchronizer projectSynchronizer;

    ArchitectureDiagramShellCommands(
            MainShellState shellState,
            ArchitectureDiagramViewModel viewModel,
            ProjectValidationCoordinator validationCoordinator,
            SpecializedProjectSynchronizer projectSynchronizer
    ) {
        this.shellState = Objects.requireNonNull(shellState, "shellState");
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
        this.validationCoordinator = Objects.requireNonNull(validationCoordinator, "validationCoordinator");
        this.projectSynchronizer = Objects.requireNonNull(projectSynchronizer, "projectSynchronizer");
    }

    void synchronizeEdit(DiagramProject updatedProject) {
        projectSynchronizer.synchronize(updatedProject, "Diagrama de arquitectura actualizado");
    }

    void requestAddC4Person() { requestAddNode(ArchitectureNodeKind.PERSON); }
    void requestAddC4System() { requestAddNode(ArchitectureNodeKind.SOFTWARE_SYSTEM); }
    void requestAddC4ExternalSystem() { requestAddNode(ArchitectureNodeKind.EXTERNAL_SYSTEM); }
    void requestAddC4Boundary() { requestAddNode(ArchitectureNodeKind.BOUNDARY); }
    void requestAddC4Container() { requestAddNode(ArchitectureNodeKind.CONTAINER); }
    void requestAddC4Application() { requestAddNode(ArchitectureNodeKind.APPLICATION); }
    void requestAddC4Api() { requestAddNode(ArchitectureNodeKind.API); }
    void requestAddArchitectureDatabase() { requestAddNode(ArchitectureNodeKind.DATABASE); }
    void requestAddArchitectureExternalService() { requestAddNode(ArchitectureNodeKind.EXTERNAL_SERVICE); }
    void requestAddDeploymentEnvironment() { requestAddNode(ArchitectureNodeKind.ENVIRONMENT); }
    void requestAddDeploymentServer() { requestAddNode(ArchitectureNodeKind.SERVER); }
    void requestAddDeploymentClient() { requestAddNode(ArchitectureNodeKind.CLIENT); }
    void requestAddDeploymentService() { requestAddNode(ArchitectureNodeKind.SERVICE); }
    void requestAddDeploymentNetwork() { requestAddNode(ArchitectureNodeKind.NETWORK); }
    void requestAddDeploymentArtifact() { requestAddNode(ArchitectureNodeKind.ARTIFACT); }

    void requestAddArchitectureUses() { requestAddEdge(ArchitectureEdgeKind.USES); }
    void requestAddArchitectureDependency() { requestAddEdge(ArchitectureEdgeKind.DEPENDS_ON); }
    void requestAddArchitectureIntegration() { requestAddEdge(ArchitectureEdgeKind.INTEGRATES_WITH); }
    void requestAddArchitectureCall() { requestAddEdge(ArchitectureEdgeKind.CALLS); }
    void requestAddArchitectureReadsWrites() { requestAddEdge(ArchitectureEdgeKind.READS_WRITES); }
    void requestAddDeploymentConnection() { requestAddEdge(ArchitectureEdgeKind.CONNECTS_TO); }
    void requestAddDeploymentHosting() { requestAddEdge(ArchitectureEdgeKind.HOSTS); }
    void requestAddDeploymentTarget() { requestAddEdge(ArchitectureEdgeKind.DEPLOYS_TO); }

    void requestRemoveItem() {
        if (!ensureActive("eliminar elementos")) {
            return;
        }
        viewModel.removeSelected();
    }

    void requestValidate() {
        validationCoordinator.validateArchitectureDiagram();
    }

    void requestRegenerateLayout() {
        viewModel.reorganizeLayout();
    }

    private void requestAddNode(ArchitectureNodeKind kind) {
        if (!ensureActive("agregar elementos")) {
            return;
        }
        viewModel.addNode(kind);
    }

    private void requestAddEdge(ArchitectureEdgeKind kind) {
        if (!ensureActive("agregar relaciones")) {
            return;
        }
        viewModel.addEdge(kind);
    }

    private boolean ensureActive(String action) {
        if (viewModel.active()) {
            return true;
        }
        shellState.updateStatus("Abre un diagrama de arquitectura para " + action + ".");
        return false;
    }
}
