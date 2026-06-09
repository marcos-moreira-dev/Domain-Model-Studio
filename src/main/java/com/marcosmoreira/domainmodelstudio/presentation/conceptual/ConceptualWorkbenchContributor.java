package com.marcosmoreira.domainmodelstudio.presentation.conceptual;

import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import com.marcosmoreira.domainmodelstudio.presentation.canvas.DiagramCanvasView;
import com.marcosmoreira.domainmodelstudio.presentation.canvas.DiagramCanvasViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.inspector.InspectorView;
import com.marcosmoreira.domainmodelstudio.presentation.inspector.InspectorViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.sidebar.ModelTreeView;
import com.marcosmoreira.domainmodelstudio.presentation.sidebar.ModelTreeViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.conceptual.sidedock.ConceptualSideDockModules;
import com.marcosmoreira.domainmodelstudio.presentation.sidedock.SideDockModule;
import com.marcosmoreira.domainmodelstudio.presentation.workbench.DiagramWorkbenchContributor;
import com.marcosmoreira.domainmodelstudio.presentation.workbench.DiagramWorkbenchDescriptor;
import com.marcosmoreira.domainmodelstudio.presentation.workspace.WorkspaceKind;
import java.util.Objects;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import javafx.scene.Parent;

/**
 * Contributor conceptual de transición.
 *
 * <p>Monta el dibujo conceptual existente dentro del workbench visual común. La intención de esta
 * tanda es cambiar la carcasa arquitectónica, no reescribir el render conceptual que ya funciona.</p>
 */
final class ConceptualWorkbenchContributor implements DiagramWorkbenchContributor {

    private final DiagramWorkbenchDescriptor descriptor;
    private final DiagramCanvasView canvasView;
    private final ModelTreeView structureView;
    private final InspectorView propertiesView;
    private final List<SideDockModule> sideDockModules;

    ConceptualWorkbenchContributor(
            ModelTreeViewModel modelTreeViewModel,
            DiagramCanvasViewModel canvasViewModel,
            InspectorViewModel inspectorViewModel,
            Consumer<NotationType> notationSwitchAction
    ) {
        Objects.requireNonNull(modelTreeViewModel, "modelTreeViewModel");
        Objects.requireNonNull(canvasViewModel, "canvasViewModel");
        Objects.requireNonNull(inspectorViewModel, "inspectorViewModel");
        this.canvasView = new DiagramCanvasView(canvasViewModel, false);
        this.structureView = new ModelTreeView(modelTreeViewModel, () -> { }, notationSwitchAction, false);
        this.propertiesView = new InspectorView(inspectorViewModel, () -> { }, false);
        this.sideDockModules = ConceptualSideDockModules.create(
                new ConceptualCanvasLegacyBridge(modelTreeViewModel, canvasViewModel, inspectorViewModel),
                notationSwitchAction
        );
        this.descriptor = DiagramWorkbenchDescriptor.migratedVisualDiagram(
                WorkspaceKind.CONCEPTUAL_CANVAS,
                "Modelo conceptual",
                "Lienzo ER con render Chen/Crow's Foot preservado dentro del workspace común.",
                "Área de trabajo conceptual integrada al workbench; el dibujo actual se conserva."
        );
    }

    @Override
    public DiagramWorkbenchDescriptor descriptor() {
        return descriptor;
    }

    @Override
    public Parent centerContent() {
        return canvasView.getRoot();
    }

    @Override
    public Optional<Parent> structurePanel() {
        return Optional.of(structureView.getRoot());
    }

    @Override
    public Optional<Parent> propertiesPanel() {
        return Optional.of(propertiesView.getRoot());
    }

    @Override
    public List<SideDockModule> additionalSideDockModules() {
        return sideDockModules;
    }
}

