package com.marcosmoreira.domainmodelstudio.presentation.shell;

import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorEdgeKind;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNodeKind;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.presentation.behavior.BehaviorDiagramViewModel;
import java.util.Objects;

/** Comandos de diagramas de comportamiento: BPMN, flujo, casos de uso, actividad, secuencia y estados. */
final class BehaviorDiagramShellCommands {

    private final MainShellState shellState;
    private final BehaviorDiagramViewModel viewModel;
    private final ProjectValidationCoordinator validationCoordinator;
    private final SpecializedProjectSynchronizer projectSynchronizer;

    BehaviorDiagramShellCommands(
            MainShellState shellState,
            BehaviorDiagramViewModel viewModel,
            ProjectValidationCoordinator validationCoordinator,
            SpecializedProjectSynchronizer projectSynchronizer
    ) {
        this.shellState = Objects.requireNonNull(shellState, "shellState");
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
        this.validationCoordinator = Objects.requireNonNull(validationCoordinator, "validationCoordinator");
        this.projectSynchronizer = Objects.requireNonNull(projectSynchronizer, "projectSynchronizer");
    }

    void synchronizeEdit(DiagramProject updatedProject) {
        projectSynchronizer.synchronize(updatedProject, "Diagrama actualizado");
    }

    void requestAddBpmnStart() { requestAddNode(BehaviorNodeKind.START_EVENT); }
    void requestAddBpmnActivity() { requestAddNode(BehaviorNodeKind.ACTIVITY); }
    void requestAddBpmnDecision() { requestAddNode(BehaviorNodeKind.DECISION); }
    void requestAddBpmnEnd() { requestAddNode(BehaviorNodeKind.END_EVENT); }
    void requestAddBpmnLane() { requestAddNode(BehaviorNodeKind.LANE); }
    void requestAddUseCaseActor() { requestAddNode(BehaviorNodeKind.ACTOR); }
    void requestAddUseCase() { requestAddNode(BehaviorNodeKind.USE_CASE); }
    void requestAddUseCaseSystem() { requestAddNode(BehaviorNodeKind.SYSTEM_BOUNDARY); }
    void requestAddUmlAction() { requestAddNode(BehaviorNodeKind.ACTION); }
    void requestAddUmlDecision() { requestAddNode(BehaviorNodeKind.DECISION); }
    void requestAddUmlInitialState() { requestAddNode(BehaviorNodeKind.INITIAL_STATE); }
    void requestAddUmlFinalState() { requestAddNode(BehaviorNodeKind.FINAL_STATE); }
    void requestAddSequenceParticipant() { requestAddNode(BehaviorNodeKind.PARTICIPANT); }
    void requestAddSequenceActivation() { requestAddNode(BehaviorNodeKind.ACTIVATION); }
    void requestAddSequenceFragment() { requestAddNode(BehaviorNodeKind.FRAGMENT); }
    void requestAddState() { requestAddNode(BehaviorNodeKind.STATE); }
    void requestAddBehaviorNote() { requestAddNode(BehaviorNodeKind.NOTE); }

    void requestAddBehaviorFlow() { requestAddEdge(BehaviorEdgeKind.FLOW); }
    void requestAddUseCaseAssociation() { requestAddEdge(BehaviorEdgeKind.ASSOCIATION); }
    void requestAddUseCaseInclude() { requestAddEdge(BehaviorEdgeKind.INCLUDE); }
    void requestAddUseCaseExtend() { requestAddEdge(BehaviorEdgeKind.EXTEND); }
    void requestAddUseCaseGeneralization() { requestAddEdge(BehaviorEdgeKind.GENERALIZATION); }
    void requestAddSequenceMessage() { requestAddEdge(BehaviorEdgeKind.MESSAGE); }
    void requestAddSequenceReturnMessage() { requestAddEdge(BehaviorEdgeKind.RETURN_MESSAGE); }
    void requestAddStateTransition() { requestAddEdge(BehaviorEdgeKind.TRANSITION); }

    void requestRemoveItem() {
        if (!ensureActive("eliminar elementos")) {
            return;
        }
        viewModel.removeSelected();
    }

    void requestValidate() {
        validationCoordinator.validateBehaviorDiagram();
    }

    void requestRegenerateLayout() {
        viewModel.reorganizeLayout();
    }

    private void requestAddNode(BehaviorNodeKind kind) {
        if (!ensureActive("agregar elementos")) {
            return;
        }
        viewModel.addNode(kind);
    }

    private void requestAddEdge(BehaviorEdgeKind kind) {
        if (!ensureActive("agregar relaciones")) {
            return;
        }
        viewModel.addEdge(kind);
    }

    private boolean ensureActive(String action) {
        if (viewModel.active()) {
            return true;
        }
        shellState.updateStatus("Abre un diagrama de comportamiento para " + action + ".");
        return false;
    }
}
