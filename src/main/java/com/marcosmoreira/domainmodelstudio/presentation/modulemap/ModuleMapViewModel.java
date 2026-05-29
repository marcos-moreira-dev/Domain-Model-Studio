package com.marcosmoreira.domainmodelstudio.presentation.modulemap;

import com.marcosmoreira.domainmodelstudio.application.modulemap.AddModuleMapDependencyUseCase;
import com.marcosmoreira.domainmodelstudio.application.modulemap.AddModuleMapModuleUseCase;
import com.marcosmoreira.domainmodelstudio.application.modulemap.ModuleMapValidationResult;
import com.marcosmoreira.domainmodelstudio.application.modulemap.RemoveModuleMapItemUseCase;
import com.marcosmoreira.domainmodelstudio.application.modulemap.UpdateModuleMapDependencyUseCase;
import com.marcosmoreira.domainmodelstudio.application.modulemap.UpdateModuleMapModuleUseCase;
import com.marcosmoreira.domainmodelstudio.application.modulemap.ValidateModuleMapUseCase;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualElementLayoutIds;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualLayoutService;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualLayerOrderCommand;
import com.marcosmoreira.domainmodelstudio.application.visual.ModuleMapContainerLayoutSupport;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.presentation.workbench.ProjectChangeSupport;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.DependencyKind;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleDependency;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleKind;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleMapDocument;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleNode;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleStatus;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import java.util.List;
import java.util.Optional;
import java.util.Objects;
import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.BooleanSupplier;
import com.marcosmoreira.domainmodelstudio.presentation.exportable.ExportPngAction;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.VisualProjectPatchSupport;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.VisualDiagramViewActions;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.VisualLayerOrderViewModelSupport;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualNodeSizeCommand;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.VisualNodeSizeViewModelSupport;

/** ViewModel del editor del mapa de módulos. */
public final class ModuleMapViewModel {

    private final AddModuleMapModuleUseCase addModuleUseCase;
    private final AddModuleMapDependencyUseCase addDependencyUseCase;
    private final UpdateModuleMapModuleUseCase updateModuleUseCase;
    private final UpdateModuleMapDependencyUseCase updateDependencyUseCase;
    private final RemoveModuleMapItemUseCase removeItemUseCase;
    private final ValidateModuleMapUseCase validateUseCase;
    private final VisualLayoutService visualLayoutService = new VisualLayoutService();
    private final ModuleMapContainerLayoutSupport containerLayoutSupport = new ModuleMapContainerLayoutSupport();
    private final Consumer<String> statusConsumer;
    private final ObservableList<ModuleNode> modules = FXCollections.observableArrayList();
    private final ObservableList<ModuleDependency> dependencies = FXCollections.observableArrayList();
    private final ObservableList<ModuleNode> rootModules = FXCollections.observableArrayList();
    private final ObjectProperty<ModuleNode> selectedModule = new SimpleObjectProperty<>();
    private final ObjectProperty<ModuleDependency> selectedDependency = new SimpleObjectProperty<>();
    private DiagramProject currentProject;
    private ModuleMapDocument currentDocument;
    private final ProjectChangeSupport projectChangeSupport = new ProjectChangeSupport();
    private final VisualDiagramViewActions viewActions;

    public ModuleMapViewModel(
            AddModuleMapModuleUseCase addModuleUseCase,
            AddModuleMapDependencyUseCase addDependencyUseCase,
            UpdateModuleMapModuleUseCase updateModuleUseCase,
            UpdateModuleMapDependencyUseCase updateDependencyUseCase,
            RemoveModuleMapItemUseCase removeItemUseCase,
            ValidateModuleMapUseCase validateUseCase,
            Consumer<String> statusConsumer
    ) {
        this.addModuleUseCase = Objects.requireNonNull(addModuleUseCase, "addModuleUseCase");
        this.addDependencyUseCase = Objects.requireNonNull(addDependencyUseCase, "addDependencyUseCase");
        this.updateModuleUseCase = Objects.requireNonNull(updateModuleUseCase, "updateModuleUseCase");
        this.updateDependencyUseCase = Objects.requireNonNull(updateDependencyUseCase, "updateDependencyUseCase");
        this.removeItemUseCase = Objects.requireNonNull(removeItemUseCase, "removeItemUseCase");
        this.validateUseCase = Objects.requireNonNull(validateUseCase, "validateUseCase");
        this.statusConsumer = Objects.requireNonNull(statusConsumer, "statusConsumer");
        this.viewActions = VisualDiagramViewActions.forGenericDiagram(this::active, this.statusConsumer, "El mapa de módulos todavía no tiene una vista PNG registrada.");
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

    public ObservableList<ModuleNode> modules() {
        return modules;
    }

    public ObservableList<ModuleDependency> dependencies() {
        return dependencies;
    }

    public ObservableList<ModuleNode> rootModules() {
        return rootModules;
    }

    public ObjectProperty<ModuleNode> selectedModuleProperty() {
        return selectedModule;
    }

    public ObjectProperty<ModuleDependency> selectedDependencyProperty() {
        return selectedDependency;
    }

    public ModuleMapDocument currentDocument() {
        return currentDocument;
    }

    public DiagramProject currentProject() {
        return currentProject;
    }

    public boolean active() {
        return currentDocument != null && currentProject != null;
    }

    public NodeLayout layoutForModule(ModuleNode module) {
        Objects.requireNonNull(module, "module");
        ensureCurrentLayout();
        return visualLayoutService.nodeLayout(currentProject, VisualElementLayoutIds.module(module.id()))
                .orElseThrow(() -> new IllegalStateException("No existe layout para el módulo: " + module.id()));
    }

    public Optional<ConnectorLayout> layoutForDependency(ModuleDependency dependency) {
        Objects.requireNonNull(dependency, "dependency");
        if (currentProject == null || currentDocument == null) {
            return Optional.empty();
        }
        ensureCurrentLayout();
        return visualLayoutService.connectorLayout(currentProject, VisualElementLayoutIds.dependency(dependency.id()));
    }

    public Optional<ConnectorLayout> layoutForConnector(DiagramElementId connectorId) {
        Objects.requireNonNull(connectorId, "connectorId");
        if (currentProject == null || currentDocument == null) {
            return Optional.empty();
        }
        ensureCurrentLayout();
        return visualLayoutService.connectorLayout(currentProject, connectorId);
    }

    public void selectModuleById(String moduleId) {
        if (moduleId == null || moduleId.isBlank()) {
            return;
        }
        for (ModuleNode module : modules) {
            if (module.id().equals(moduleId.strip())) {
                selectedModule.set(module);
                selectedDependency.set(null);
                return;
            }
        }
    }

    public void selectDependencyById(String dependencyId) {
        if (dependencyId == null || dependencyId.isBlank()) {
            return;
        }
        for (ModuleDependency dependency : dependencies) {
            if (dependency.id().equals(dependencyId.strip())) {
                selectedDependency.set(dependency);
                selectedModule.set(null);
                return;
            }
        }
    }

    public void clearPropertySelection() {
        selectedModule.set(null);
        selectedDependency.set(null);
    }

    public Optional<Integer> addConnectorBendPoint(DiagramElementId connectorId, double x, double y) {
        if (!ensureProjectForLayout("No hay mapa de módulos abierto.")) {
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
        if (!ensureProjectForLayout("No hay mapa de módulos abierto.")) {
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

    public void removeConnectorBendPoint(DiagramElementId connectorId, int bendPointIndex) {
        if (!ensureProjectForLayout("No hay mapa de módulos abierto.")) {
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

    public void moveConnectorLabelBy(DiagramElementId connectorId, double deltaX, double deltaY) {
        if (!ensureProjectForLayout("No hay mapa de módulos abierto.")) {
            return;
        }
        try {
            currentProject = visualLayoutService.moveConnectorLabelBy(currentProject, connectorId, deltaX, deltaY);
            notifyProjectChanged();
            statusConsumer.accept("Etiqueta del conector actualizada.");
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo mover la etiqueta: " + exception.getMessage());
        }
    }

    public void moveModuleTo(String moduleId, double x, double y) {
        if (!ensureProjectForLayout("No hay mapa de módulos abierto.")) {
            return;
        }
        try {
            NodeLayout currentModuleLayout = visualLayoutService.nodeLayout(currentProject, VisualElementLayoutIds.module(moduleId))
                    .orElseThrow(() -> new IllegalArgumentException("No existe layout para el módulo: " + moduleId));
            double deltaX = x - currentModuleLayout.x();
            double deltaY = y - currentModuleLayout.y();
            currentProject = visualLayoutService.moveNodeTo(currentProject, VisualElementLayoutIds.module(moduleId), x, y);
            for (ModuleNode child : descendantModules(moduleId)) {
                currentProject = visualLayoutService.moveNodeBy(
                        currentProject,
                        VisualElementLayoutIds.module(child.id()),
                        deltaX,
                        deltaY
                );
            }
            currentProject = containerLayoutSupport.expandAncestors(currentProject, moduleId, modules);
            notifyProjectChanged();
            statusConsumer.accept("Módulo, submódulos y contenedor superior actualizados.");
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo mover módulo: " + exception.getMessage());
        }
    }


    public boolean reorderSelectedElement(VisualLayerOrderCommand command) {
        ModuleNode module = selectedModule.get();
        DiagramElementId layoutId = module == null ? null : VisualElementLayoutIds.module(module.id());
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

    public boolean resizeSelectedElement(VisualNodeSizeCommand command) {
        ModuleNode module = selectedModule.get();
        DiagramElementId layoutId = module == null ? null : VisualElementLayoutIds.module(module.id());
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
            currentDocument = project.moduleMap()
                    .orElseGet(() -> ModuleMapDocument.blank(project.metadata().title()));
            refreshLists();
            selectedModule.set(modules.isEmpty() ? null : modules.get(0));
            selectedDependency.set(null);
        });
    }

    public void clear() {
        projectChangeSupport.runLoading(() -> {
            currentProject = null;
            currentDocument = null;
            modules.clear();
            rootModules.clear();
            dependencies.clear();
            selectedModule.set(null);
            selectedDependency.set(null);
        });
    }

    public void addModule() {
        if (!ensureDocument("No hay mapa de módulos abierto.")) {
            return;
        }
        try {
            applyDocument(addModuleUseCase.addRootModule(currentDocument, "Módulo"), "Módulo agregado.");
            selectLastModule();
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo agregar módulo: " + exception.getMessage());
        }
    }

    public void addSubmodule() {
        if (!ensureDocument("No hay mapa de módulos abierto.")) {
            return;
        }
        ModuleNode parent = selectedModule.get();
        if (parent == null) {
            statusConsumer.accept("Selecciona un módulo antes de agregar submódulo.");
            return;
        }
        try {
            applyDocument(addModuleUseCase.addSubmodule(currentDocument, parent.id(), "Submódulo"), "Submódulo agregado.");
            selectLastModule();
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo agregar submódulo: " + exception.getMessage());
        }
    }

    public void addDependency() {
        if (!ensureDocument("No hay mapa de módulos abierto.")) {
            return;
        }
        ModuleNode source = selectedModule.get();
        ModuleNode target = firstDifferentModule(source == null ? "" : source.id());
        if (source == null || target == null) {
            statusConsumer.accept("Crea al menos dos módulos para agregar dependencia.");
            return;
        }
        try {
            applyDocument(addDependencyUseCase.add(currentDocument, source.id(), target.id()), "Dependencia agregada.");
            selectLastDependency();
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo agregar dependencia: " + exception.getMessage());
        }
    }

    public void removeSelected() {
        if (!ensureDocument("No hay mapa de módulos abierto.")) {
            return;
        }
        ModuleDependency dependency = selectedDependency.get();
        ModuleNode module = selectedModule.get();
        try {
            if (dependency != null) {
                applyDocument(removeItemUseCase.removeDependency(currentDocument, dependency.id()), "Dependencia eliminada.");
                selectedDependency.set(null);
                return;
            }
            if (module != null) {
                applyDocument(removeItemUseCase.removeModule(currentDocument, module.id()), "Módulo eliminado.");
                selectedModule.set(modules.isEmpty() ? null : modules.get(0));
            }
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo eliminar: " + exception.getMessage());
        }
    }

    public void applyModuleChanges(
            String displayName,
            ModuleNode parent,
            ModuleKind kind,
            ModuleStatus status,
            String responsibility,
            String description,
            String rawTags,
            String notes
    ) {
        ModuleNode module = selectedModule.get();
        if (module == null || !ensureDocument("No hay módulo seleccionado.")) {
            return;
        }
        String parentId = parent == null ? "" : parent.id();
        try {
            applyDocument(updateModuleUseCase.update(currentDocument, module.id(), displayName, parentId, kind, status,
                    responsibility, description, ModuleNode.splitTags(rawTags), notes), "Módulo actualizado.");
            restoreModuleSelection(module.id());
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo actualizar módulo: " + exception.getMessage());
        }
    }

    public void applyDependencyChanges(
            ModuleNode source,
            ModuleNode target,
            DependencyKind kind,
            String description,
            String notes
    ) {
        ModuleDependency dependency = selectedDependency.get();
        if (dependency == null || !ensureDocument("No hay dependencia seleccionada.")) {
            return;
        }
        if (source == null || target == null) {
            statusConsumer.accept("Selecciona origen y destino para la dependencia.");
            return;
        }
        try {
            applyDocument(updateDependencyUseCase.update(currentDocument, dependency.id(), source.id(), target.id(), kind,
                    description, notes), "Dependencia actualizada.");
            restoreDependencySelection(dependency.id());
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo actualizar dependencia: " + exception.getMessage());
        }
    }

    public ModuleMapValidationResult validateDocument() {
        if (!ensureDocument("No hay mapa de módulos abierto para validar.")) {
            return new ModuleMapValidationResult(List.of("No hay mapa de módulos abierto."));
        }
        ModuleMapValidationResult result = validateUseCase.validate(currentDocument);
        statusConsumer.accept(result.summary());
        return result;
    }
    public void reorganizeLayout() {
        if (!ensureProjectForLayout("No hay mapa de módulos abierto para autoorganizar.")) {
            return;
        }
        currentProject = visualLayoutService.regenerateVisualLayout(currentProject.withModuleMap(currentDocument));
        notifyProjectChanged();
        statusConsumer.accept("Mapa de módulos autoorganizado por jerarquía y dependencias.");
    }


    public String moduleLabel(String moduleId) {
        return currentDocument == null ? moduleId : currentDocument.moduleById(moduleId)
                .map(ModuleNode::displayName)
                .orElse(moduleId);
    }


    private List<ModuleNode> descendantModules(String moduleId) {
        String normalizedModuleId = moduleId == null ? "" : moduleId.strip();
        java.util.ArrayList<ModuleNode> descendants = new java.util.ArrayList<>();
        collectDescendantModules(normalizedModuleId, descendants);
        return descendants;
    }

    private void collectDescendantModules(String parentId, List<ModuleNode> descendants) {
        for (ModuleNode module : modules) {
            if (module.parentId().equals(parentId)) {
                descendants.add(module);
                collectDescendantModules(module.id(), descendants);
            }
        }
    }

    private void ensureCurrentLayout() {
        if (currentProject == null) {
            throw new IllegalStateException("No hay proyecto activo para consultar layout visual.");
        }
        currentProject = visualLayoutService.ensureVisualLayout(currentProject);
    }

    private ModuleNode firstDifferentModule(String moduleId) {
        return modules.stream().filter(module -> !module.id().equals(moduleId)).findFirst().orElse(null);
    }

    private void applyDocument(ModuleMapDocument updatedDocument, String statusMessage) {
        currentDocument = updatedDocument;
        refreshLists();
        if (currentProject != null) {
            currentProject = visualLayoutService.ensureVisualLayout(currentProject.withModuleMap(updatedDocument));
            notifyProjectChanged();
        }
        statusConsumer.accept(statusMessage);
    }

    private void refreshLists() {
        modules.setAll(currentDocument == null ? List.of() : currentDocument.modules());
        rootModules.setAll(currentDocument == null ? List.of() : currentDocument.rootModules());
        dependencies.setAll(currentDocument == null ? List.of() : currentDocument.dependencies());
    }

    private void selectLastModule() {
        if (!modules.isEmpty()) {
            selectedModule.set(modules.get(modules.size() - 1));
        }
    }

    private void selectLastDependency() {
        if (!dependencies.isEmpty()) {
            selectedDependency.set(dependencies.get(dependencies.size() - 1));
        }
    }

    private boolean restoreModuleSelection(String moduleId) {
        for (ModuleNode module : modules) {
            if (module.id().equals(moduleId)) {
                selectedModule.set(module);
                return true;
            }
        }
        return false;
    }

    private boolean restoreDependencySelection(String dependencyId) {
        for (ModuleDependency dependency : dependencies) {
            if (dependency.id().equals(dependencyId)) {
                selectedDependency.set(dependency);
                return true;
            }
        }
        return false;
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

    private boolean ensureProjectForLayout(String message) {
        if (currentProject == null || currentDocument == null) {
            statusConsumer.accept(message);
            return false;
        }
        currentProject = visualLayoutService.ensureVisualLayout(currentProject);
        return true;
    }

    private boolean ensureDocument(String message) {
        if (currentDocument == null) {
            statusConsumer.accept(message);
            return false;
        }
        return true;
    }
}
