package com.marcosmoreira.domainmodelstudio.presentation.wireframe;
import com.marcosmoreira.domainmodelstudio.application.wireframe.AddWireframeComponentUseCase;
import com.marcosmoreira.domainmodelstudio.application.wireframe.AddWireframeScreenUseCase;
import com.marcosmoreira.domainmodelstudio.application.wireframe.ApplyWireframeTemplateUseCase;
import com.marcosmoreira.domainmodelstudio.application.wireframe.RemoveWireframeItemUseCase;
import com.marcosmoreira.domainmodelstudio.application.wireframe.UpdateWireframeComponentUseCase;
import com.marcosmoreira.domainmodelstudio.application.wireframe.UpdateWireframeScreenUseCase;
import com.marcosmoreira.domainmodelstudio.application.wireframe.ValidateWireframeUseCase;
import com.marcosmoreira.domainmodelstudio.application.wireframe.WireframeValidationResult;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualElementLayoutIds;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualLayoutService;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualLayerOrderCommand;
import com.marcosmoreira.domainmodelstudio.application.visual.WireframeContainerLayoutSupport;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.presentation.workbench.ProjectChangeSupport;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeComponent;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeComponentKind;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeDocument;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeScreenTemplateKind;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeScreen;
import com.marcosmoreira.domainmodelstudio.presentation.exportable.ExportPngAction;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.VisualProjectPatchSupport;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.VisualDiagramViewActions;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.VisualLayerOrderViewModelSupport;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.BooleanSupplier;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualNodeSizeCommand;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.VisualNodeSizeViewModelSupport;
/** ViewModel del editor visual de wireframes administrativos. */
public final class WireframeViewModel {
    private final AddWireframeScreenUseCase addScreenUseCase;
    private final AddWireframeComponentUseCase addComponentUseCase;
    private final UpdateWireframeScreenUseCase updateScreenUseCase;
    private final UpdateWireframeComponentUseCase updateComponentUseCase;
    private final RemoveWireframeItemUseCase removeItemUseCase;
    private final ValidateWireframeUseCase validateUseCase;
    private final ApplyWireframeTemplateUseCase applyTemplateUseCase;
    private final VisualLayoutService visualLayoutService = new VisualLayoutService();
    private final WireframeContainerLayoutSupport containerLayoutSupport = new WireframeContainerLayoutSupport();
    private final WireframeDuplicateSelectionService duplicateSelectionService = new WireframeDuplicateSelectionService(visualLayoutService);
    private final Consumer<String> statusConsumer;
    private final ObservableList<WireframeScreen> screens = FXCollections.observableArrayList();
    private final ObservableList<WireframeComponent> components = FXCollections.observableArrayList();
    private final ObjectProperty<WireframeScreen> selectedScreen = new SimpleObjectProperty<>();
    private final ObjectProperty<WireframeComponent> selectedComponent = new SimpleObjectProperty<>();
    private DiagramProject currentProject;
    private WireframeDocument currentDocument;
    private final ProjectChangeSupport projectChangeSupport = new ProjectChangeSupport();
    private final VisualDiagramViewActions viewActions;
    public WireframeViewModel(
            AddWireframeScreenUseCase addScreenUseCase,
            AddWireframeComponentUseCase addComponentUseCase,
            UpdateWireframeScreenUseCase updateScreenUseCase,
            UpdateWireframeComponentUseCase updateComponentUseCase,
            RemoveWireframeItemUseCase removeItemUseCase,
            ValidateWireframeUseCase validateUseCase,
            ApplyWireframeTemplateUseCase applyTemplateUseCase,
            Consumer<String> statusConsumer
    ) {
        this.addScreenUseCase = Objects.requireNonNull(addScreenUseCase, "addScreenUseCase");
        this.addComponentUseCase = Objects.requireNonNull(addComponentUseCase, "addComponentUseCase");
        this.updateScreenUseCase = Objects.requireNonNull(updateScreenUseCase, "updateScreenUseCase");
        this.updateComponentUseCase = Objects.requireNonNull(updateComponentUseCase, "updateComponentUseCase");
        this.removeItemUseCase = Objects.requireNonNull(removeItemUseCase, "removeItemUseCase");
        this.validateUseCase = Objects.requireNonNull(validateUseCase, "validateUseCase");
        this.applyTemplateUseCase = Objects.requireNonNull(applyTemplateUseCase, "applyTemplateUseCase");
        this.statusConsumer = Objects.requireNonNull(statusConsumer, "statusConsumer");
        this.viewActions = VisualDiagramViewActions.forGenericDiagram(this::active, this.statusConsumer, "El wireframe todavía no tiene una vista PNG registrada.");
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
    public boolean resizeSelectedElement(VisualNodeSizeCommand command) {
        DiagramElementId layoutId = selectedComponent.get() != null
                ? VisualElementLayoutIds.wireframeComponent(selectedComponent.get().id())
                : selectedScreen.get() == null ? null : VisualElementLayoutIds.wireframeScreen(selectedScreen.get().id());
        boolean resized = VisualNodeSizeViewModelSupport.resizeSelectedNode(
                visualLayoutService, currentProject, layoutId, command,
                project -> currentProject = project, this::notifyProjectChanged, statusConsumer);
        if (resized) {
            viewActions.refreshDiagramView();
        }
        return resized;
    }

    public boolean deleteSelectedBendPoint() { return viewActions.deleteSelectedBendPoint(); }
    public void exportVisualAsPng(Path targetFile) throws IOException { viewActions.exportVisualAsPng(targetFile); }
    public ObservableList<WireframeScreen> screens() {
        return screens;
    }
    public ObservableList<WireframeComponent> components() {
        return components;
    }
    public ObjectProperty<WireframeScreen> selectedScreenProperty() {
        return selectedScreen;
    }
    public ObjectProperty<WireframeComponent> selectedComponentProperty() {
        return selectedComponent;
    }
    public DiagramProject currentProject() {
        return currentProject;
    }
    public WireframeDocument currentDocument() {
        return currentDocument;
    }
    public boolean active() {
        return currentProject != null
                && DiagramTypeId.ADMIN_WIREFRAMES.equals(currentProject.metadata().diagramTypeId())
                && currentDocument != null;
    }
    public void loadProject(DiagramProject project) {
        projectChangeSupport.runLoading(() -> {
            currentProject = visualLayoutService.ensureVisualLayout(Objects.requireNonNull(project, "project"));
            currentDocument = currentProject.wireframe().orElseGet(() -> WireframeDocument.blank(project.metadata().title()));
            refresh();
            selectedScreen.set(screens.isEmpty() ? null : screens.get(0));
            selectedComponent.set(null);
        });
    }
    public void clear() {
        projectChangeSupport.runLoading(() -> {
            currentProject = null;
            currentDocument = null;
            screens.clear();
            components.clear();
            selectedScreen.set(null);
            selectedComponent.set(null);
        });
    }
    public void addScreen() {
        if (!ensureDocument("No hay wireframe abierto.")) {
            return;
        }
        try {
            applyDocument(addScreenUseCase.add(currentDocument, "Pantalla"), "Pantalla agregada.");
            if (!screens.isEmpty()) {
                selectedScreen.set(screens.get(screens.size() - 1));
                selectedComponent.set(null);
            }
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo agregar pantalla: " + exception.getMessage());
        }
    }
    public void addComponent(WireframeComponentKind kind) {
        if (!ensureDocument("No hay wireframe abierto.")) {
            return;
        }
        if (screens.isEmpty()) {
            statusConsumer.accept("Crea una pantalla antes de agregar componentes.");
            return;
        }
        String screenId = selectedScreen.get() == null ? screens.get(0).id() : selectedScreen.get().id();
        try {
            applyDocument(addComponentUseCase.add(currentDocument, screenId, kind), "Componente agregado.");
            selectLastComponentInScreen(screenId);
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo agregar componente: " + exception.getMessage());
        }
    }
    public void applyTemplate(WireframeScreenTemplateKind templateKind) {
        if (!ensureDocument("No hay wireframe abierto.")) {
            return;
        }
        WireframeScreenTemplateKind normalized = templateKind == null ? WireframeScreenTemplateKind.CRUD_LIST : templateKind;
        try {
            WireframeDocument updatedDocument = applyTemplateUseCase.apply(currentDocument, normalized);
            applyDocument(updatedDocument, "Plantilla agregada: " + normalized.displayName() + ".");
            if (!screens.isEmpty()) {
                selectedScreen.set(screens.get(screens.size() - 1));
                selectedComponent.set(null);
            }
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo agregar plantilla: " + exception.getMessage());
        }
    }
    public void duplicateSelected() {
        if (!ensureDocument("No hay wireframe abierto.") || !ensureProjectForLayout("No hay wireframe abierto.")) {
            return;
        }
        try {
            WireframeDuplicateSelectionService.Result result = duplicateSelectionService.duplicate(
                    currentProject,
                    currentDocument,
                    selectedScreen.get(),
                    selectedComponent.get());
            currentProject = result.project();
            currentDocument = result.document();
            refresh();
            selectDuplicated(result);
            notifyProjectChanged();
            statusConsumer.accept(result.status());
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo duplicar: " + exception.getMessage());
        }
    }
    private void selectDuplicated(WireframeDuplicateSelectionService.Result result) {
        if (result.selectedComponentId() != null) {
            selectComponent(result.selectedComponentId());
            WireframeComponent component = selectedComponent.get();
            selectedScreen.set(component == null ? null : screens.stream()
                    .filter(screen -> screen.id().equals(component.screenId()))
                    .findFirst()
                    .orElse(null));
            return;
        }
        if (result.selectedScreenId() != null) {
            selectScreen(result.selectedScreenId());
            selectedComponent.set(null);
        }
    }
    public void removeItemsById(java.util.Set<String> screenIds, java.util.Set<String> componentIds) {
        if (!ensureDocument("No hay wireframe abierto.")) { return; }
        WireframeDocument updated = currentDocument;
        for (String componentId : componentIds == null ? java.util.Set.<String>of() : componentIds) {
            updated = removeItemUseCase.removeComponent(updated, componentId);
        }
        for (String screenId : screenIds == null ? java.util.Set.<String>of() : screenIds) {
            updated = removeItemUseCase.removeScreen(updated, screenId);
        }
        applyDocument(updated, "Elementos eliminados.");
        selectedScreen.set(null);
        selectedComponent.set(null);
    }
    public void removeSelected() {
        if (!ensureDocument("No hay wireframe abierto.")) {
            return;
        }
        try {
            if (selectedComponent.get() != null) {
                String removedId = selectedComponent.get().id();
                applyDocument(removeItemUseCase.removeComponent(currentDocument, removedId), "Componente eliminado.");
                selectedComponent.set(null);
                return;
            }
            if (selectedScreen.get() != null) {
                applyDocument(removeItemUseCase.removeScreen(currentDocument, selectedScreen.get().id()), "Pantalla eliminada.");
                selectedScreen.set(screens.isEmpty() ? null : screens.get(0));
                selectedComponent.set(null);
                return;
            }
            statusConsumer.accept("Selecciona pantalla o componente para eliminar.");
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo eliminar: " + exception.getMessage());
        }
    }
    public void applyScreenChanges(String name, String module, String purpose, String notes) {
        WireframeScreen screen = selectedScreen.get();
        if (screen == null || !ensureDocument("No hay pantalla seleccionada.")) {
            return;
        }
        try {
            applyDocument(updateScreenUseCase.update(currentDocument, screen.id(), name, module, purpose, notes), "Pantalla actualizada.");
            selectScreen(screen.id());
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo actualizar pantalla: " + exception.getMessage());
        }
    }
    public void applyComponentChanges(
            WireframeScreen screen,
            WireframeComponentKind kind,
            String name,
            int order,
            String binding,
            String behavior,
            String notes
    ) {
        WireframeComponent component = selectedComponent.get();
        if (component == null || !ensureDocument("No hay componente seleccionado.")) {
            return;
        }
        if (screen == null) {
            statusConsumer.accept("Selecciona una pantalla para el componente.");
            return;
        }
        try {
            applyDocument(updateComponentUseCase.update(currentDocument, component.id(), screen.id(), kind, name, order, binding, behavior, notes),
                    "Componente actualizado.");
            selectComponent(component.id());
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo actualizar componente: " + exception.getMessage());
        }
    }
    public NodeLayout layoutForScreen(WireframeScreen screen) {
        Objects.requireNonNull(screen, "screen");
        ensureCurrentLayout();
        return visualLayoutService.nodeLayout(currentProject, VisualElementLayoutIds.wireframeScreen(screen.id()))
                .orElseThrow(() -> new IllegalStateException("No existe layout para la pantalla wireframe: " + screen.id()));
    }
    public NodeLayout layoutForComponent(WireframeComponent component) {
        Objects.requireNonNull(component, "component");
        ensureCurrentLayout();
        return visualLayoutService.nodeLayout(currentProject, VisualElementLayoutIds.wireframeComponent(component.id()))
                .orElseThrow(() -> new IllegalStateException("No existe layout para el componente wireframe: " + component.id()));
    }
    public void selectScreenById(String screenId) {
        if (screenId == null || screenId.isBlank()) { return; }
        selectedScreen.set(screens.stream().filter(screen -> screen.id().equals(screenId.strip())).findFirst().orElse(null));
        if (selectedScreen.get() != null) { selectedComponent.set(null); }
    }
    public void selectComponentById(String componentId) {
        if (componentId == null || componentId.isBlank()) { return; }
        WireframeComponent component = components.stream().filter(candidate -> candidate.id().equals(componentId.strip())).findFirst().orElse(null);
        selectedComponent.set(component);
        if (component != null) { selectedScreen.set(screens.stream().filter(screen -> screen.id().equals(component.screenId())).findFirst().orElse(null)); }
    }
    public void clearPropertySelection() {
        selectedScreen.set(null);
        selectedComponent.set(null);
    }

    public boolean reorderSelectedElement(VisualLayerOrderCommand command) {
        DiagramElementId layoutId = selectedComponent.get() != null
                ? VisualElementLayoutIds.wireframeComponent(selectedComponent.get().id())
                : selectedScreen.get() == null ? null : VisualElementLayoutIds.wireframeScreen(selectedScreen.get().id());
        boolean reordered = VisualLayerOrderViewModelSupport.reorderSelectedNode(visualLayoutService, currentProject, layoutId, command,
                project -> currentProject = project, this::notifyProjectChanged, statusConsumer);
        if (reordered) {
            viewActions.refreshDiagramView();
        }
        return reordered;
    }

    public void moveScreenTo(String screenId, double x, double y) {
        if (!ensureProjectForLayout("No hay wireframe abierto.")) {
            return;
        }
        try {
            currentProject = containerLayoutSupport.moveScreenWithComponents(currentProject, screenId, x, y, components);
            notifyProjectChanged();
            statusConsumer.accept("Pantalla y componentes internos movidos.");
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo mover pantalla: " + exception.getMessage());
        }
    }

    public void moveComponentTo(String componentId, double x, double y) {
        if (!ensureProjectForLayout("No hay wireframe abierto.")) { return; }
        try {
            currentProject = visualLayoutService.moveNodeTo(currentProject, VisualElementLayoutIds.wireframeComponent(componentId), x, y);
            notifyProjectChanged();
            statusConsumer.accept("Componente movido.");
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo mover componente: " + exception.getMessage());
        }
    }
    public void resizeScreenTo(String screenId, double width, double height) { resizeNode(VisualElementLayoutIds.wireframeScreen(screenId), width, height, 180.0, 120.0, "pantalla"); }

    public void resizeComponentTo(String componentId, double width, double height) { resizeNode(VisualElementLayoutIds.wireframeComponent(componentId), width, height, 48.0, 24.0, "componente"); }

    public void fitSelectedScreenToContent() {
        WireframeScreen screen = selectedScreen.get();
        if (screen == null || !ensureProjectForLayout("No hay pantalla seleccionada para ajustar.")) { return; }
        try { currentProject = containerLayoutSupport.expandScreen(currentProject, screen.id(), components); notifyProjectChanged(); statusConsumer.accept("Pantalla ajustada al contenido por solicitud explícita."); }
        catch (IllegalArgumentException exception) { statusConsumer.accept("No se pudo ajustar pantalla: " + exception.getMessage()); }
    }

    private void resizeNode(DiagramElementId id, double width, double height, double minWidth, double minHeight, String label) {
        if (!ensureProjectForLayout("No hay wireframe abierto.")) { return; }
        try { currentProject = visualLayoutService.resizeNodeTo(currentProject, id, Math.max(minWidth, width), Math.max(minHeight, height)); notifyProjectChanged(); statusConsumer.accept("Tamaño de " + label + " actualizado."); }
        catch (IllegalArgumentException exception) { statusConsumer.accept("No se pudo redimensionar " + label + ": " + exception.getMessage()); }
    }
    public WireframeValidationResult validateDocument() {
        if (!ensureDocument("No hay wireframe abierto.")) {
            return WireframeValidationResult.warnings(List.of("No hay wireframe abierto."));
        }
        WireframeValidationResult result = validateUseCase.validate(currentDocument);
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
    private void applyDocument(WireframeDocument document, String status) {
        currentDocument = Objects.requireNonNull(document, "document");
        refresh();
        if (currentProject != null) {
            currentProject = visualLayoutService.ensureVisualLayout(currentProject.withWireframe(document));
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
        components.setAll(currentDocument == null ? List.of() : currentDocument.components());
    }
    private void selectScreen(String id) {
        selectedScreen.set(screens.stream().filter(screen -> screen.id().equals(id)).findFirst().orElse(null));
    }
    private void selectComponent(String id) {
        selectedComponent.set(components.stream().filter(component -> component.id().equals(id)).findFirst().orElse(null));
    }
    private void selectLastComponentInScreen(String screenId) {
        WireframeComponent last = components.stream()
                .filter(component -> component.screenId().equals(screenId))
                .reduce((first, second) -> second)
                .orElse(null);
        selectedComponent.set(last);
    }
}
