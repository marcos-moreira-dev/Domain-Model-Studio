package com.marcosmoreira.domainmodelstudio.presentation.shell;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.presentation.freegraph.FreeGraphViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.logicalbusinessgraph.LogicalBusinessGraphViewModel;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;

/** Coordina transferencia de selección visual entre pestañas abiertas compatibles. */
final class VisualSelectionTransferCoordinator {

    private final MainShellState shellState;
    private final ProjectSessionCoordinator projectSessions;
    private final FreeGraphViewModel freeGraphViewModel;
    private final LogicalBusinessGraphViewModel logicalBusinessGraphViewModel;
    private final BiConsumer<ProjectSession, String> activateProjectSession;
    private final Runnable refreshActiveOutputState;

    VisualSelectionTransferCoordinator(
            MainShellState shellState,
            ProjectSessionCoordinator projectSessions,
            FreeGraphViewModel freeGraphViewModel,
            LogicalBusinessGraphViewModel logicalBusinessGraphViewModel,
            BiConsumer<ProjectSession, String> activateProjectSession,
            Runnable refreshActiveOutputState
    ) {
        this.shellState = shellState;
        this.projectSessions = projectSessions;
        this.freeGraphViewModel = freeGraphViewModel;
        this.logicalBusinessGraphViewModel = logicalBusinessGraphViewModel;
        this.activateProjectSession = activateProjectSession;
        this.refreshActiveOutputState = refreshActiveOutputState;
    }

    void requestTransferVisualSelection() {
        DiagramTypeId diagramTypeId = shellState.activeDiagramTypeProperty().get();
        if (!supported(diagramTypeId)) {
            shellState.updateStatus("Transferir selección solo está disponible en Grafo libre y Grafo lógico.");
            return;
        }
        ProjectSession source = projectSessions.activeSession();
        if (source == null || source.isPlaceholder()) {
            shellState.updateStatus("No hay proyecto activo para transferir selección.");
            return;
        }
        List<ProjectSession> candidates = compatibleTransferTargets(diagramTypeId, source);
        if (candidates.isEmpty()) {
            shellState.updateStatus("No hay otro proyecto abierto compatible para recibir la selección.");
            showNoCompatibleTransferTargetDialog(diagramTypeId);
            return;
        }
        if (!copyActiveVisualSelection(diagramTypeId)) {
            return;
        }
        Optional<ProjectSession> selectedTarget = chooseVisualTransferTarget(diagramTypeId, candidates);
        if (selectedTarget.isEmpty()) {
            shellState.updateStatus("Transferencia de selección cancelada.");
            return;
        }
        ProjectSession target = selectedTarget.orElseThrow();
        activateProjectSession.accept(target, "Proyecto destino abierto");
        if (!pasteVisualSelectionIntoActive(diagramTypeId)) {
            shellState.updateStatus("No se pudo pegar la selección en el proyecto destino.");
            return;
        }
        refreshActiveOutputState.run();
        shellState.updateStatus("Selección transferida a " + target.title() + ".");
    }

    private boolean supported(DiagramTypeId diagramTypeId) {
        return DiagramTypeId.FREE_GRAPH.equals(diagramTypeId)
                || DiagramTypeId.LOGICAL_BUSINESS_GRAPH.equals(diagramTypeId);
    }

    private List<ProjectSession> compatibleTransferTargets(DiagramTypeId diagramTypeId, ProjectSession source) {
        return projectSessions.sessions().stream()
                .filter(session -> session != null && !session.isPlaceholder())
                .filter(session -> session != source)
                .filter(session -> session.project != null)
                .filter(session -> diagramTypeId.equals(session.project.metadata().diagramTypeId()))
                .toList();
    }

    private boolean copyActiveVisualSelection(DiagramTypeId diagramTypeId) {
        if (DiagramTypeId.FREE_GRAPH.equals(diagramTypeId)) {
            return freeGraphViewModel.copyCurrentSelectionToClipboard();
        }
        if (DiagramTypeId.LOGICAL_BUSINESS_GRAPH.equals(diagramTypeId)) {
            return logicalBusinessGraphViewModel.copyCurrentSelectionToClipboard();
        }
        return false;
    }

    private boolean pasteVisualSelectionIntoActive(DiagramTypeId diagramTypeId) {
        if (DiagramTypeId.FREE_GRAPH.equals(diagramTypeId)) {
            return freeGraphViewModel.pasteSelectionFromClipboard();
        }
        if (DiagramTypeId.LOGICAL_BUSINESS_GRAPH.equals(diagramTypeId)) {
            return logicalBusinessGraphViewModel.pasteSelectionFromClipboard();
        }
        return false;
    }

    private Optional<ProjectSession> chooseVisualTransferTarget(DiagramTypeId diagramTypeId, List<ProjectSession> candidates) {
        ChoiceDialog<ProjectSession> dialog = new ChoiceDialog<>(candidates.get(0), candidates);
        dialog.setTitle("Transferir selección");
        dialog.setHeaderText("Elige el proyecto destino compatible");
        dialog.setContentText("Destino " + diagramTypeId.value() + ":");
        return dialog.showAndWait();
    }

    private void showNoCompatibleTransferTargetDialog(DiagramTypeId diagramTypeId) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sin proyecto compatible");
        alert.setHeaderText("No hay otro proyecto abierto del mismo tipo.");
        alert.setContentText("Abre otro proyecto " + diagramTypeId.value()
                + " para transferir nodos, relaciones y layout de la selección.");
        alert.showAndWait();
    }
}
