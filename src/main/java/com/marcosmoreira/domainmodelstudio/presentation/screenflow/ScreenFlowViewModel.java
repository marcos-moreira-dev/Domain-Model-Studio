package com.marcosmoreira.domainmodelstudio.presentation.screenflow;

import com.marcosmoreira.domainmodelstudio.application.screenflow.AddScreenTransitionUseCase;
import com.marcosmoreira.domainmodelstudio.application.screenflow.AddScreenUseCase;
import com.marcosmoreira.domainmodelstudio.application.screenflow.RemoveScreenFlowItemUseCase;
import com.marcosmoreira.domainmodelstudio.application.screenflow.ScreenFlowValidationResult;
import com.marcosmoreira.domainmodelstudio.application.screenflow.UpdateScreenTransitionUseCase;
import com.marcosmoreira.domainmodelstudio.application.screenflow.UpdateScreenUseCase;
import com.marcosmoreira.domainmodelstudio.application.screenflow.ValidateScreenFlowUseCase;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualElementLayoutIds;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualLayoutService;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualLayerOrderCommand;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.presentation.workbench.ProjectChangeSupport;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenFlowDocument;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenKind;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenNode;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenTransition;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenTransitionKind;
import com.marcosmoreira.domainmodelstudio.presentation.exportable.ExportPngAction;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.VisualProjectPatchSupport;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.VisualDiagramViewActions;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.VisualLayerOrderViewModelSupport;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.BooleanSupplier;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualNodeSizeCommand;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.VisualNodeSizeViewModelSupport;

/** ViewModel del flujo visual de pantallas. */
public final class ScreenFlowViewModel {

    private final AddScreenUseCase addScreenUseCase;
    private final AddScreenTransitionUseCase addTransitionUseCase;
    private final UpdateScreenUseCase updateScreenUseCase;
    private final UpdateScreenTransitionUseCase updateTransitionUseCase;
    private final RemoveScreenFlowItemUseCase removeItemUseCase;
    private final ValidateScreenFlowUseCase validateUseCase;
    private final VisualLayoutService visualLayoutService = new VisualLayoutService();
    private final Consumer<String> statusConsumer;
    private final ObservableList<ScreenNode> screens = FXCollections.observableArrayList();
    private final ObservableList<ScreenTransition> transitions = FXCollections.observableArrayList();
    private final ObjectProperty<ScreenNode> selectedScreen = new SimpleObjectProperty<>();
    private final ObjectProperty<ScreenTransition> selectedTransition = new SimpleObjectProperty<>();
    private DiagramProject currentProject;
    private ScreenFlowDocument currentDocument;
    private final ProjectChangeSupport projectChangeSupport = new ProjectChangeSupport();
    private final VisualDiagramViewActions viewActions;

    public ScreenFlowViewModel(
            AddScreenUseCase addScreenUseCase,
            AddScreenTransitionUseCase addTransitionUseCase,
            UpdateScreenUseCase updateScreenUseCase,
            UpdateScreenTransitionUseCase updateTransitionUseCase,
            RemoveScreenFlowItemUseCase removeItemUseCase,
            ValidateScreenFlowUseCase validateUseCase,
            Consumer<String> statusConsumer
    ) {
        this.addScreenUseCase = Objects.requireNonNull(addScreenUseCase, "addScreenUseCase");
        this.addTransitionUseCase = Objects.requireNonNull(addTransitionUseCase, "addTransitionUseCase");
        this.updateScreenUseCase = Objects.requireNonNull(updateScreenUseCase, "updateScreenUseCase");
        this.updateTransitionUseCase = Objects.requireNonNull(updateTransitionUseCase, "updateTransitionUseCase");
        this.removeItemUseCase = Objects.requireNonNull(removeItemUseCase, "removeItemUseCase");
        this.validateUseCase = Objects.requireNonNull(validateUseCase, "validateUseCase");
        this.statusConsumer = Objects.requireNonNull(statusConsumer, "statusConsumer");
        this.viewActions = VisualDiagramViewActions.forGenericDiagram(this::active, this.statusConsumer, "El flujo de pantallas todavía no tiene una vista PNG registrada.");
    }

    public void registerProjectChangeListener(Consumer<DiagramProject> listener) {
        projectChangeSupport.registerProjectChangeListener(listener);
    }
    public void registerPngExportAction(ExportPngAction action) { viewActions.registerPngExportAction(action); }
    public void registerDiagramFitAction(Runnable action) { viewActions.registerDiagramFitAction(action); }
    public void registerDiagramCenterAction(Runnable action) { viewActions.registerDiagramCenterAction(action); }
    public void registerDiagramRefreshAction(Runnable action) { viewActions.registerDiagramRefreshAction(action); }
    public void registerDeleteSelectedBendPointAction(BooleanSupplier action) { viewActions.registerDeleteSelectedBendPointAction(action); }
    public void fitDiagramView() { viewActions.fitDiagramView(); }
    public void centerDiagramView() { viewActions.centerDiagramView(); }
    public boolean deleteSelectedBendPoint() { return viewActions.deleteSelectedBendPoint(); }
    public void exportVisualAsPng(Path targetFile) throws IOException { viewActions.exportVisualAsPng(targetFile); }

    public ObservableList<ScreenNode> screens() { return screens; }
    public ObservableList<ScreenTransition> transitions() { return transitions; }
    public ObjectProperty<ScreenNode> selectedScreenProperty() { return selectedScreen; }
    public ObjectProperty<ScreenTransition> selectedTransitionProperty() { return selectedTransition; }
    public DiagramProject currentProject() { return currentProject; }
    public ScreenFlowDocument currentDocument() { return currentDocument; }

    public boolean active() {
        return currentProject != null
                && DiagramTypeId.SCREEN_FLOW.equals(currentProject.metadata().diagramTypeId())
                && currentDocument != null;
    }

    public boolean resizeSelectedElement(VisualNodeSizeCommand command) {
        ScreenNode screen = selectedScreen.get();
        DiagramElementId layoutId = screen == null ? null : VisualElementLayoutIds.screen(screen.id());
        boolean resized = VisualNodeSizeViewModelSupport.resizeSelectedNode(
                visualLayoutService, currentProject, layoutId, command,
                project -> currentProject = project, this::notifyProjectChanged, statusConsumer);
        if (resized) {
            viewActions.refreshDiagramView();
        }
        return resized;
    }

    public void loadProject(DiagramProject project) {
        projectChangeSupport.runLoading(() -> {
            currentProject = visualLayoutService.ensureVisualLayout(Objects.requireNonNull(project, "project"));
            currentDocument = currentProject.screenFlow()
                    .orElseGet(() -> ScreenFlowDocument.blank(project.metadata().title()));
            refresh();
            selectedScreen.set(screens.isEmpty() ? null : screens.get(0));
            selectedTransition.set(null);
        });
    }

    public void clear() {
        projectChangeSupport.runLoading(() -> {
            currentProject = null;
            currentDocument = null;
            screens.clear();
            transitions.clear();
            selectedScreen.set(null);
            selectedTransition.set(null);
        });
    }

    public void addScreen() {
        if (!ensureDocument("No hay flujo de pantallas abierto.")) {
            return;
        }
        try {
            applyDocument(addScreenUseCase.add(currentDocument, "Pantalla"), "Pantalla agregada.");
            if (!screens.isEmpty()) {
                selectedScreen.set(screens.get(screens.size() - 1));
                selectedTransition.set(null);
            }
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo agregar pantalla: " + exception.getMessage());
        }
    }

    public void addTransition() {
        if (!ensureDocument("No hay flujo de pantallas abierto.")) {
            return;
        }
        if (screens.size() < 2) {
            statusConsumer.accept("Crea al menos dos pantallas para agregar transición.");
            return;
        }
        ScreenNode selected = selectedScreen.get();
        String source = selected == null ? screens.get(0).id() : selected.id();
        String target = screens.stream()
                .filter(screen -> !screen.id().equals(source))
                .findFirst()
                .orElse(screens.get(0))
                .id();
        try {
            applyDocument(addTransitionUseCase.add(currentDocument, source, target), "Transición agregada.");
            if (!transitions.isEmpty()) {
                selectedTransition.set(transitions.get(transitions.size() - 1));
            }
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo agregar transición: " + exception.getMessage());
        }
    }

    public void removeSelected() {
        if (!ensureDocument("No hay flujo de pantallas abierto.")) {
            return;
        }
        try {
            if (selectedTransition.get() != null) {
                String removedId = selectedTransition.get().id();
                applyDocument(removeItemUseCase.removeTransition(currentDocument, removedId), "Transición eliminada.");
                selectedTransition.set(null);
                return;
            }
            if (selectedScreen.get() != null) {
                applyDocument(removeItemUseCase.removeScreen(currentDocument, selectedScreen.get().id()), "Pantalla eliminada.");
                selectedScreen.set(screens.isEmpty() ? null : screens.get(0));
                return;
            }
            statusConsumer.accept("Selecciona pantalla o transición para eliminar.");
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo eliminar: " + exception.getMessage());
        }
    }

    public void applyScreenChanges(
            String name,
            ScreenKind kind,
            String module,
            String route,
            String purpose,
            String notes
    ) {
        ScreenNode screen = selectedScreen.get();
        if (screen == null || !ensureDocument("No hay pantalla seleccionada.")) {
            return;
        }
        try {
            applyDocument(updateScreenUseCase.update(currentDocument, screen.id(), name, kind, module, route, purpose, notes),
                    "Pantalla actualizada.");
            selectScreen(screen.id());
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo actualizar pantalla: " + exception.getMessage());
        }
    }

    public void applyTransitionChanges(
            ScreenNode source,
            ScreenNode target,
            ScreenTransitionKind kind,
            String trigger,
            String condition,
            String notes
    ) {
        ScreenTransition transition = selectedTransition.get();
        if (transition == null || !ensureDocument("No hay transición seleccionada.")) {
            return;
        }
        if (source == null || target == null) {
            statusConsumer.accept("Selecciona origen y destino para la transición.");
            return;
        }
        try {
            applyDocument(updateTransitionUseCase.update(currentDocument, transition.id(), source.id(), target.id(), kind, trigger, condition, notes),
                    "Transición actualizada.");
            selectTransition(transition.id());
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo actualizar transición: " + exception.getMessage());
        }
    }

    public NodeLayout layoutForScreen(ScreenNode screen) {
        Objects.requireNonNull(screen, "screen");
        ensureCurrentLayout();
        return visualLayoutService.nodeLayout(currentProject, VisualElementLayoutIds.screen(screen.id()))
                .orElseThrow(() -> new IllegalStateException("No existe layout para la pantalla: " + screen.id()));
    }

    public Optional<ConnectorLayout> layoutForConnector(DiagramElementId connectorId) {
        Objects.requireNonNull(connectorId, "connectorId");
        if (currentProject == null || currentDocument == null) {
            return Optional.empty();
        }
        ensureCurrentLayout();
        return visualLayoutService.connectorLayout(currentProject, connectorId);
    }

    public void selectScreenById(String screenId) {
        if (screenId == null || screenId.isBlank()) {
            return;
        }
        selectedScreen.set(screens.stream().filter(screen -> screen.id().equals(screenId.strip())).findFirst().orElse(null));
        if (selectedScreen.get() != null) {
            selectedTransition.set(null);
        }
    }

    public void selectTransitionById(String transitionId) {
        if (transitionId == null || transitionId.isBlank()) {
            return;
        }
        selectedTransition.set(transitions.stream().filter(transition -> transition.id().equals(transitionId.strip())).findFirst().orElse(null));
        if (selectedTransition.get() != null) {
            selectedScreen.set(null);
        }
    }

    public void clearPropertySelection() {
        selectedScreen.set(null);
        selectedTransition.set(null);
    }

    public Optional<Integer> addConnectorBendPoint(DiagramElementId connectorId, double x, double y) {
        if (!ensureProjectForLayout("No hay flujo de pantallas abierto.")) {
            return Optional.empty();
        }
        try {
            currentProject = visualLayoutService.addBendPoint(currentProject, connectorId, x, y);
            notifyProjectChanged();
            int index = visualLayoutService.bendPointIndexAt(currentProject, connectorId, x, y).orElse(-1);
            statusConsumer.accept("Punto intermedio agregado.");
            return index < 0 ? Optional.empty() : Optional.of(index);
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo agregar punto intermedio: " + exception.getMessage());
            return Optional.empty();
        }
    }

    public void moveConnectorBendPointTo(DiagramElementId connectorId, int bendPointIndex, double x, double y) {
        if (!ensureProjectForLayout("No hay flujo de pantallas abierto.")) {
            return;
        }
        try {
            currentProject = visualLayoutService.moveBendPointTo(currentProject, connectorId, bendPointIndex, x, y);
            notifyProjectChanged();
            statusConsumer.accept("Punto intermedio actualizado.");
        } catch (IllegalArgumentException | IndexOutOfBoundsException exception) {
            statusConsumer.accept("No se pudo mover punto intermedio: " + exception.getMessage());
        }
    }

    public void moveConnectorLabelBy(DiagramElementId connectorId, double deltaX, double deltaY) {
        Objects.requireNonNull(connectorId, "connectorId");
        if (currentProject != null) {
            currentProject = visualLayoutService.moveConnectorLabelBy(currentProject, connectorId, deltaX, deltaY);
            notifyProjectChanged();
        }
    }

    public void removeConnectorBendPoint(DiagramElementId connectorId, int bendPointIndex) {
        if (!ensureProjectForLayout("No hay flujo de pantallas abierto.")) {
            return;
        }
        try {
            currentProject = visualLayoutService.removeBendPoint(currentProject, connectorId, bendPointIndex);
            notifyProjectChanged();
            statusConsumer.accept("Punto intermedio eliminado.");
        } catch (IllegalArgumentException | IndexOutOfBoundsException exception) {
            statusConsumer.accept("No se pudo eliminar punto intermedio: " + exception.getMessage());
        }
    }

    public void moveScreenTo(String screenId, double x, double y) {
        if (!ensureProjectForLayout("No hay flujo de pantallas abierto.")) {
            return;
        }
        try {
            currentProject = visualLayoutService.moveNodeTo(currentProject, VisualElementLayoutIds.screen(screenId), x, y);
            notifyProjectChanged();
            statusConsumer.accept("Posición de pantalla actualizada.");
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo mover pantalla: " + exception.getMessage());
        }
    }

    public boolean reorderSelectedElement(VisualLayerOrderCommand command) {
        ScreenNode screen = selectedScreen.get();
        DiagramElementId layoutId = screen == null ? null : VisualElementLayoutIds.screen(screen.id());
        boolean reordered = VisualLayerOrderViewModelSupport.reorderSelectedNode(
                visualLayoutService,
                currentProject,
                layoutId,
                command,
                project -> currentProject = project,
                this::notifyProjectChanged,
                statusConsumer);
        if (reordered) {
            viewActions.refreshDiagramView();
        }
        return reordered;
    }

    public ScreenFlowValidationResult validateDocument() {
        if (!ensureDocument("No hay flujo de pantallas abierto.")) {
            return ScreenFlowValidationResult.warnings(List.of("No hay flujo de pantallas abierto."));
        }
        ScreenFlowValidationResult result = validateUseCase.validate(currentDocument);
        statusConsumer.accept(result.summary());
        return result;
    }

    private boolean ensureDocument(String message) {
        if (currentDocument == null) {
            statusConsumer.accept(message);
            return false;
        }
        return true;
    }

    private void applyDocument(ScreenFlowDocument document, String status) {
        currentDocument = Objects.requireNonNull(document, "document");
        refresh();
        if (currentProject != null) {
            currentProject = visualLayoutService.ensureVisualLayout(currentProject.withScreenFlow(document));
            projectChangeSupport.notifyChanged(currentProject);
        }
        statusConsumer.accept(status);
    }

    private void ensureCurrentLayout() {
        if (currentProject != null) {
            currentProject = visualLayoutService.ensureVisualLayout(currentProject);
        }
    }

    private boolean ensureProjectForLayout(String message) {
        if (currentProject == null || currentDocument == null) {
            statusConsumer.accept(message);
            return false;
        }
        ensureCurrentLayout();
        return true;
    }

    public void patchCurrentProject(java.util.function.UnaryOperator<DiagramProject> patch, String statusMessage) {
        VisualProjectPatchSupport.apply(
                currentProject,
                patch,
                statusConsumer,
                statusMessage,
                updatedProject -> currentProject = updatedProject,
                this::notifyProjectChanged
        );
    }
    private void notifyProjectChanged() {
        projectChangeSupport.notifyChanged(currentProject);
    }

    private void refresh() {
        screens.setAll(currentDocument == null ? List.of() : currentDocument.screens());
        transitions.setAll(currentDocument == null ? List.of() : currentDocument.transitions());
    }

    private void selectScreen(String id) {
        selectedScreen.set(screens.stream().filter(screen -> screen.id().equals(id)).findFirst().orElse(null));
    }

    private void selectTransition(String id) {
        selectedTransition.set(transitions.stream().filter(transition -> transition.id().equals(id)).findFirst().orElse(null));
    }
}
