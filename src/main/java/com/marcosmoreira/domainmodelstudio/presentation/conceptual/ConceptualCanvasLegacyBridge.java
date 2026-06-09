package com.marcosmoreira.domainmodelstudio.presentation.conceptual;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.presentation.canvas.DiagramCanvasViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.conceptual.bridge.ConceptualCanvasCommandBridge;
import com.marcosmoreira.domainmodelstudio.presentation.conceptual.bridge.ConceptualHybridCanvasBridge;
import com.marcosmoreira.domainmodelstudio.presentation.conceptual.bridge.ConceptualLayoutBridge;
import com.marcosmoreira.domainmodelstudio.presentation.conceptual.bridge.ConceptualSelectionBridge;
import com.marcosmoreira.domainmodelstudio.presentation.conceptual.bridge.ConceptualValidationBridge;
import com.marcosmoreira.domainmodelstudio.presentation.inspector.InspectorViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.sidebar.ModelTreeViewModel;
import java.util.Objects;
import javafx.beans.value.ObservableValue;

/**
 * Puente mínimo para mantener vivo el canvas conceptual actual dentro del ciclo de workspaces.
 *
 * <p>Esta clase no migra el render Chen/Crow's Foot ni reemplaza el motor visual existente. Solo
 * encapsula la carga, limpieza y consulta del proyecto conceptual para que el shell pueda tratarlo
 * como un workspace especializado más durante la migración hacia la arquitectura común.</p>
 */
public final class ConceptualCanvasLegacyBridge {

    private final ModelTreeViewModel modelTreeViewModel;
    private final DiagramCanvasViewModel canvasViewModel;
    private final InspectorViewModel inspectorViewModel;
    private final ConceptualHybridCanvasBridge hybridCanvasBridge;

    public ConceptualCanvasLegacyBridge(
            ModelTreeViewModel modelTreeViewModel,
            DiagramCanvasViewModel canvasViewModel,
            InspectorViewModel inspectorViewModel
    ) {
        this.modelTreeViewModel = Objects.requireNonNull(modelTreeViewModel, "modelTreeViewModel");
        this.canvasViewModel = Objects.requireNonNull(canvasViewModel, "canvasViewModel");
        this.inspectorViewModel = Objects.requireNonNull(inspectorViewModel, "inspectorViewModel");
        this.hybridCanvasBridge = new ConceptualHybridCanvasBridge(this.canvasViewModel);
    }

    public void loadProject(DiagramProject project) {
        Objects.requireNonNull(project, "project");
        modelTreeViewModel.loadProject(project);
        canvasViewModel.showImportedProject(project);
        inspectorViewModel.refreshFromSelection();
    }

    public void clear() {
        modelTreeViewModel.clearProject();
        canvasViewModel.clearProject();
        inspectorViewModel.refreshFromSelection();
    }

    public ConceptualHybridCanvasBridge hybridCanvas() {
        return hybridCanvasBridge;
    }

    public ConceptualSelectionBridge selection() {
        return hybridCanvasBridge.selection();
    }

    public ConceptualCanvasCommandBridge commands() {
        return hybridCanvasBridge.commands();
    }

    public ConceptualLayoutBridge layout() {
        return hybridCanvasBridge.layout();
    }

    public ConceptualValidationBridge validation() {
        return hybridCanvasBridge.validation();
    }

    public boolean active() {
        DiagramProject current = currentProject();
        return current != null && DiagramTypeId.CONCEPTUAL_MODEL.equals(current.metadata().diagramTypeId());
    }

    public ObservableValue<DiagramProject> currentProjectObservable() {
        return canvasViewModel.currentProjectProperty();
    }

    public DiagramProject currentProject() {
        return canvasViewModel.currentProject();
    }
}
