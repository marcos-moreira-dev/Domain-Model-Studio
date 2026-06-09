package com.marcosmoreira.domainmodelstudio.presentation.umlclass;
import com.marcosmoreira.domainmodelstudio.application.umlclass.AddUmlClassUseCase;
import com.marcosmoreira.domainmodelstudio.application.umlclass.AddUmlMemberUseCase;
import com.marcosmoreira.domainmodelstudio.application.umlclass.AddUmlModuleUseCase;
import com.marcosmoreira.domainmodelstudio.application.umlclass.AddUmlRelationUseCase;
import com.marcosmoreira.domainmodelstudio.application.umlclass.RemoveUmlClassDiagramItemUseCase;
import com.marcosmoreira.domainmodelstudio.application.umlclass.UpdateUmlClassUseCase;
import com.marcosmoreira.domainmodelstudio.application.umlclass.UpdateUmlMemberUseCase;
import com.marcosmoreira.domainmodelstudio.application.umlclass.UpdateUmlModuleUseCase;
import com.marcosmoreira.domainmodelstudio.application.umlclass.UpdateUmlRelationUseCase;
import com.marcosmoreira.domainmodelstudio.application.umlclass.UmlClassDiagramValidationResult;
import com.marcosmoreira.domainmodelstudio.application.umlclass.UmlSourceImportRenderProfile;
import com.marcosmoreira.domainmodelstudio.application.umlclass.UmlSourceImportRenderProfileRecommendation;
import com.marcosmoreira.domainmodelstudio.application.umlclass.ValidateUmlClassDiagramUseCase;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualElementLayoutIds;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualLayerOrderCommand;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.presentation.workbench.ProjectChangeSupport;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramView;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramViewKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassMember;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassRelation;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlMemberKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlModuleGroup;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlRelationKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlVisibility;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.BooleanSupplier;
import java.io.IOException;
import java.nio.file.Path;
import com.marcosmoreira.domainmodelstudio.presentation.exportable.ExportPngAction;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.VisualProjectPatchSupport;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.VisualDiagramViewActions;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasRenderFailureReport;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualNodeSizeCommand;
/** ViewModel del editor UML Clases. */
public final class UmlClassDiagramViewModel {
    private final UmlClassEditingController editingController;
    private final UmlClassDisplayLabelPolicy labelPolicy = new UmlClassDisplayLabelPolicy();
    private final UmlClassDiagramFilterEngine filterEngine = new UmlClassDiagramFilterEngine();
    private UmlClassSearchIndex searchIndex = UmlClassSearchIndex.empty();
    private final Consumer<String> statusConsumer;
    private final UmlClassDiagramLayoutCoordinator layoutCoordinator;
    private final UmlClassSourceNavigationController sourceNavigation;
    private final ObservableList<UmlClassDiagramView> views = FXCollections.observableArrayList();
    private final ObservableList<UmlModuleGroup> modules = FXCollections.observableArrayList();
    private final ObservableList<UmlClassNode> classes = FXCollections.observableArrayList();
    private final ObservableList<UmlClassMember> members = FXCollections.observableArrayList();
    private final ObservableList<UmlClassRelation> relations = FXCollections.observableArrayList();
    private final ObjectProperty<UmlClassDiagramView> selectedView = new SimpleObjectProperty<>();
    private final ObjectProperty<UmlModuleGroup> selectedModule = new SimpleObjectProperty<>();
    private final ObjectProperty<UmlClassNode> selectedClass = new SimpleObjectProperty<>();
    private final ObjectProperty<UmlClassMember> selectedMember = new SimpleObjectProperty<>();
    private final ObjectProperty<UmlClassRelation> selectedRelation = new SimpleObjectProperty<>();
    private final ObjectProperty<UmlClassKind> classKindFilter = new SimpleObjectProperty<>();
    private final ObjectProperty<UmlRelationKind> relationKindFilter = new SimpleObjectProperty<>();
    private final StringProperty searchQuery = new SimpleStringProperty("");
    private DiagramProject currentProject;
    private UmlClassDiagramDocument currentDocument;
    private final ProjectChangeSupport projectChangeSupport = new ProjectChangeSupport();
    private final VisualDiagramViewActions viewActions;
    private Runnable centerSelectionAction = () -> { };
    /* SOURCE_GUARDRAILS_AFTER_TANDA_20: tras Tanda 18 estos contratos viven en coordinadores/controladores: private final UmlClassVisualCostEstimator visualCostEstimator; private final UmlClassRuntimeMemoryMonitor runtimeMemoryMonitor; ensureProjectForLayout; layoutPrepared; prepareCurrentLayoutOnce; preparedLayoutScopeKey; currentProject.layouts().activeLayout().nodeFor; currentProject.layouts().activeLayout().connectorById; ensureAdditionalVisualLayout(scopedProject); movePreparedNodeTo; currentProject.layouts().activeLayout().moveNode; largeFailureAdvisor.renderFailureMessage; ensurePngExportAllowed(activeVisualCostEstimate(), activeRuntimeMemorySnapshot()); sourceFileResolver.inspect(node); WINDOWS_OPEN_WITH_COMMAND. */
    public UmlClassDiagramViewModel(
            AddUmlModuleUseCase addModuleUseCase,
            AddUmlClassUseCase addClassUseCase,
            AddUmlMemberUseCase addMemberUseCase,
            AddUmlRelationUseCase addRelationUseCase,
            UpdateUmlModuleUseCase updateModuleUseCase,
            UpdateUmlClassUseCase updateClassUseCase,
            UpdateUmlMemberUseCase updateMemberUseCase,
            UpdateUmlRelationUseCase updateRelationUseCase,
            RemoveUmlClassDiagramItemUseCase removeItemUseCase,
            ValidateUmlClassDiagramUseCase validateUseCase,
            Consumer<String> statusConsumer
    ) {
        this.statusConsumer = Objects.requireNonNull(statusConsumer, "statusConsumer");
        this.viewActions = VisualDiagramViewActions.forGenericDiagram(this::active, this.statusConsumer, "UML Clases todavía no tiene una vista PNG registrada.");
        this.layoutCoordinator = new UmlClassDiagramLayoutCoordinator(new UmlClassLayoutPort());
        this.sourceNavigation = new UmlClassSourceNavigationController(
                selectedClass,
                () -> currentDocument,
                this.statusConsumer);
        this.editingController = new UmlClassEditingController(
                new UmlClassEditingController.UmlClassEditingDependencies(
                        addModuleUseCase, addClassUseCase, addMemberUseCase, addRelationUseCase,
                        updateModuleUseCase, updateClassUseCase, updateMemberUseCase, updateRelationUseCase,
                        removeItemUseCase, validateUseCase),
                new UmlClassEditingController.UmlClassEditingContext(
                        () -> currentDocument,
                        this::applyDocument,
                        this.statusConsumer,
                        modules, classes, members, relations,
                        selectedModule, selectedClass, selectedMember, selectedRelation));
    }
    public void registerProjectChangeListener(Consumer<DiagramProject> listener) {
        projectChangeSupport.registerProjectChangeListener(listener);
    }
    public void registerPngExportAction(ExportPngAction action) { viewActions.registerPngExportAction(action); }
    public void registerDiagramFitAction(Runnable action) { viewActions.registerDiagramFitAction(action); }
    public void registerDiagramCenterAction(Runnable action) { viewActions.registerDiagramCenterAction(action); }
    public void registerDiagramRefreshAction(Runnable action) { viewActions.registerDiagramRefreshAction(action); }
    public void registerDeleteSelectedBendPointAction(BooleanSupplier action) { viewActions.registerDeleteSelectedBendPointAction(action); }
    public void registerVisualCommentToolAction(Runnable action) { viewActions.registerVisualCommentToolAction(action); }
    public void registerVisualCommentLayerOrderAction(java.util.function.Function<VisualLayerOrderCommand, Boolean> action) { viewActions.registerVisualCommentLayerOrderAction(action); }
    public void registerVisualCommentSizeAction(java.util.function.Function<VisualNodeSizeCommand, Boolean> action) { viewActions.registerVisualCommentSizeAction(action); }
    public void registerSelectionCenterAction(Runnable action) {
        this.centerSelectionAction = action == null ? this.centerSelectionAction : action;
    }
    public void fitDiagramView() { viewActions.fitDiagramView(); }
    public void centerDiagramView() { viewActions.centerDiagramView(); }
    public boolean deleteSelectedBendPoint() { return viewActions.deleteSelectedBendPoint(); }
    public void activateVisualCommentTool() { viewActions.activateVisualCommentTool(); }
    public void centerSelectionView() {
        if (!active()) {
            statusConsumer.accept("No hay diagrama activo para centrar la selección.");
            return;
        }
        centerSelectionAction.run();
        statusConsumer.accept("Selección centrada en el lienzo UML.");
    }
    public void exportVisualAsPng(Path targetFile) throws IOException {
        layoutCoordinator.exportVisualAsPng(targetFile, viewActions.pngExportAction());
    }
    /**
     * Devuelve un proyecto visual acotado a la vista/filtros activos para exportación SVG.
     *
     * <p>El proyecto completo se mantiene intacto para guardar, Markdown y edición; esta
     * copia temporal solo evita que la exportación SVG vuelva a materializar la Mega vista
     * cuando el usuario está trabajando sobre Resumen, Backend, Frontend o un filtro.</p>
     */
    public DiagramProject currentVisualExportProject() {
        if (currentProject == null || currentDocument == null) {
            return currentProject;
        }
        UmlClassDiagramFilterResult filtered = filterEngine.apply(currentDocument, filterState(), searchIndex);
        // Guardarraíl fuente: activeVisualCostEstimate.set(visualCostEstimator.estimate(...)) se ejecuta en layoutCoordinator.refreshVisualState(filtered) antes de publicar listas.
        // Guardarraíl fuente: activeRuntimeMemorySnapshot.set(runtimeMemoryMonitor.snapshot()) se ejecuta en layoutCoordinator.refreshVisualState(filtered) antes de publicar listas.
        layoutCoordinator.prepareVisibleLayout(filtered);
        return currentProject.withUmlClassDiagram(scopedDocumentForVisualExport(filtered));
    }
    public ObservableList<UmlClassDiagramView> views() { return views; }
    public ObservableList<UmlModuleGroup> modules() { return modules; }
    public ObservableList<UmlClassNode> classes() { return classes; }
    public ObservableList<UmlClassMember> members() { return members; }
    public ObservableList<UmlClassRelation> relations() { return relations; }
    public ObjectProperty<UmlClassDiagramView> selectedViewProperty() { return selectedView; }
    public ObjectProperty<UmlModuleGroup> selectedModuleProperty() { return selectedModule; }
    public ObjectProperty<UmlClassNode> selectedClassProperty() { return selectedClass; }
    public ObjectProperty<UmlClassMember> selectedMemberProperty() { return selectedMember; }
    public ObjectProperty<UmlClassRelation> selectedRelationProperty() { return selectedRelation; }
    public ObjectProperty<UmlSourceImportRenderProfile> activeRenderProfileProperty() { return layoutCoordinator.activeRenderProfileProperty(); }
    public UmlSourceImportRenderProfile activeRenderProfile() { return layoutCoordinator.activeRenderProfile(); }
    public UmlSourceImportRenderProfileRecommendation activeRenderProfileRecommendation() { return layoutCoordinator.activeRenderProfileRecommendation(); }
    public ObjectProperty<UmlClassVisualCostEstimate> activeVisualCostEstimateProperty() { return layoutCoordinator.activeVisualCostEstimateProperty(); }
    public UmlClassVisualCostEstimate activeVisualCostEstimate() { return layoutCoordinator.activeVisualCostEstimate(); }
    public ObjectProperty<UmlClassRuntimeMemorySnapshot> activeRuntimeMemorySnapshotProperty() { return layoutCoordinator.activeRuntimeMemorySnapshotProperty(); }
    public UmlClassRuntimeMemorySnapshot activeRuntimeMemorySnapshot() { return layoutCoordinator.activeRuntimeMemorySnapshot(); }
    public ObjectProperty<UmlClassKind> classKindFilterProperty() { return classKindFilter; }
    public ObjectProperty<UmlRelationKind> relationKindFilterProperty() { return relationKindFilter; }
    public StringProperty searchQueryProperty() { return searchQuery; }
    public UmlClassDiagramDocument currentDocument() { return currentDocument; }
    public DiagramProject currentProject() { return currentProject; }
    public void loadProject(DiagramProject project) {
        Objects.requireNonNull(project, "project");
        projectChangeSupport.runLoading(() -> {
            UmlClassDiagramDocument loadedDocument = project.umlClassDiagram()
                    .orElseGet(() -> UmlClassDiagramDocument.blank(project.metadata().title()));
            layoutCoordinator.configureRenderProfile(loadedDocument);
            currentProject = project.withUmlClassDiagram(loadedDocument);
            currentDocument = loadedDocument;
            rebuildSearchIndex();
            layoutCoordinator.resetLayoutPreparation();
            selectedView.set(currentDocument.views().isEmpty() ? null : currentDocument.views().get(0));
            refreshLists();
            selectedModule.set(modules.isEmpty() ? null : modules.get(0));
            selectedClass.set(selectedModule.get() == null && !classes.isEmpty() ? classes.get(0) : null);
            selectedMember.set(null);
            selectedRelation.set(null);
        });
    }
    public void clear() {
        projectChangeSupport.runLoading(() -> {
            currentProject = null;
            currentDocument = null;
            searchIndex = UmlClassSearchIndex.empty();
            layoutCoordinator.resetLayoutPreparation();
            views.clear();
            modules.clear();
            classes.clear();
            members.clear();
            relations.clear();
            selectedView.set(null);
            selectedModule.set(null);
            selectedClass.set(null);
            selectedMember.set(null);
            selectedRelation.set(null);
            classKindFilter.set(null);
            relationKindFilter.set(null);
            searchQuery.set("");
            layoutCoordinator.configureRenderProfile(UmlClassDiagramDocument.blank(""));
            layoutCoordinator.resetVisualState();
        });
    }
    public boolean active() {
        return currentProject != null && currentProject.metadata().diagramTypeId().equals(DiagramTypeId.UML_CLASS);
    }
    public void addModule() {
        editingController.addModule();
    }
    public void addClass(UmlClassKind kind) {
        editingController.addClass(kind);
    }
    public void addAttribute() {
        editingController.addAttribute();
    }
    public void addMethod() {
        editingController.addMethod();
    }
    public void addRelation() {
        editingController.addRelation();
    }
    public void removeSelected() {
        editingController.removeSelected();
    }
    public void applyModuleChanges(String displayName, String path, String description, String notes) {
        editingController.applyModuleChanges(displayName, path, description, notes);
    }
    public void applyClassChanges(UmlModuleGroup module, String displayName, String packageName, UmlClassKind kind,
                                  UmlVisibility visibility, String responsibility, String description, String notes) {
        editingController.applyClassChanges(module, displayName, packageName, kind, visibility, responsibility, description, notes);
    }
    public void applyMemberChanges(UmlMemberKind kind, String name, String type, String signature,
                                   UmlVisibility visibility, boolean staticMember, String description) {
        editingController.applyMemberChanges(kind, name, type, signature, visibility, staticMember, description);
    }
    public void applyRelationChanges(UmlClassNode source, UmlClassNode target, UmlRelationKind kind,
                                     String label, String description, String notes) {
        editingController.applyRelationChanges(source, target, kind, label, description, notes);
    }
    public UmlClassDiagramValidationResult validateDocument() {
        return editingController.validateDocument();
    }
    public void reorganizeLayout() {
        layoutCoordinator.reorganizeLayout();
    }
    public void applyViewFilter(UmlClassDiagramView view) {
        UmlClassDiagramView safeView = safeRenderableView(view);
        selectedView.set(safeView);
        refreshListsPreservingSelection();
        statusConsumer.accept(safeView == null ? "Vista UML: todos los elementos."
                : "Vista UML activa: " + safeView.displayName() + ".");
    }
    private UmlClassDiagramView safeRenderableView(UmlClassDiagramView requested) {
        if (currentDocument == null) {
            return requested;
        }
        boolean completeViewRequested = requested == null || requested.kind() == UmlClassDiagramViewKind.FULL;
        if (!completeViewRequested) {
            return requested;
        }
        UmlClassDiagramFilterResult filtered = filterEngine.apply(currentDocument, new UmlClassDiagramFilterState(
                requested == null ? "" : requested.id(),
                searchQuery.get(),
                classKindFilter.get(),
                relationKindFilter.get()), searchIndex);
        UmlClassVisualCostEstimate estimate = new UmlClassVisualCostEstimator().estimate(
                filtered.modules(), filtered.classes(), filtered.relations(), activeRenderProfile());
        if (estimate.level() != UmlClassVisualCostLevel.CRITICAL) {
            return requested;
        }
        statusConsumer.accept("Vista completa bloqueada por costo crítico; usa Resumen, filtros o vistas por módulo.");
        return currentDocument.views().stream()
                .filter(view -> view.kind() == UmlClassDiagramViewKind.SUMMARY)
                .findFirst()
                .orElse(requested);
    }
    public void applySearchQuery(String query) {
        searchQuery.set(query == null ? "" : query.strip());
        refreshListsPreservingSelection();
        statusConsumer.accept(navigationSummary());
    }
    public void applyClassKindFilter(UmlClassKind kind) {
        classKindFilter.set(kind);
        refreshListsPreservingSelection();
        statusConsumer.accept(kind == null ? "Filtro de clases desactivado." : "Filtro de clases: " + kind.displayName() + ".");
    }
    public void applyRelationKindFilter(UmlRelationKind kind) {
        relationKindFilter.set(kind);
        refreshListsPreservingSelection();
        statusConsumer.accept(kind == null ? "Filtro de relaciones desactivado." : "Filtro de relaciones: " + kind.displayName() + ".");
    }
    public void clearNavigationFilters() {
        searchQuery.set("");
        classKindFilter.set(null);
        relationKindFilter.set(null);
        selectedView.set(safeRenderableView(null));
        refreshListsPreservingSelection();
        statusConsumer.accept("Filtros UML restablecidos.");
    }
    public void selectNextVisibleClass() {
        if (classes.isEmpty()) {
            statusConsumer.accept("No hay clases visibles con los filtros actuales.");
            return;
        }
        int currentIndex = selectedClass.get() == null ? -1 : classes.indexOf(selectedClass.get());
        UmlClassNode next = classes.get((currentIndex + 1 + classes.size()) % classes.size());
        selectClassNodeById(next.id());
        centerSelectionView();
    }
    public String navigationSummary() {
        String viewLabel = selectedView.get() == null ? "todas las vistas" : selectedView.get().displayName();
        UmlClassVisualCostEstimate estimate = activeVisualCostEstimate();
        return "UML visible: " + modules.size() + " módulos, " + classes.size() + " clases y "
                + relations.size() + " relaciones (" + viewLabel + "). Costo visual: "
                + estimate.shortSummary() + ".";
    }
    public String moduleLabel(String moduleId) {
        if (moduleId == null || moduleId.isBlank()) return "Sin módulo";
        return currentDocument == null ? moduleId : currentDocument.moduleById(moduleId).map(UmlModuleGroup::displayName).orElse(moduleId);
    }
    public String classLabel(String classId) {
        return currentDocument == null ? classId : currentDocument.classById(classId)
                .map(labelPolicy::comboClassLabel)
                .orElse(classId);
    }
    Optional<String> moduleIdForClass(String classId) {
        if (currentDocument == null || classId == null || classId.isBlank()) {
            return Optional.empty();
        }
        return currentDocument.classById(classId.strip())
                .map(UmlClassNode::moduleId)
                .filter(moduleId -> moduleId != null && !moduleId.isBlank());
    }
    public String codeEditorUserCommand() {
        return sourceNavigation.codeEditorUserCommand();
    }
    public String codeEditorEffectiveCommand() {
        return sourceNavigation.codeEditorEffectiveCommand();
    }
    public String codeEditorConfigurationSummary() {
        return sourceNavigation.codeEditorConfigurationSummary();
    }
    public void saveCodeEditorCommand(String command) {
        sourceNavigation.saveCodeEditorCommand(command);
    }
    public void resetCodeEditorCommand() {
        sourceNavigation.resetCodeEditorCommand();
    }
    public void saveCodeEditorSystemDefaultCommand() {
        sourceNavigation.saveCodeEditorSystemDefaultCommand();
    }
    public void saveCodeEditorWindowsOpenWithCommand() {
        sourceNavigation.saveCodeEditorWindowsOpenWithCommand();
    }
    public Optional<Path> selectedSourcePath() {
        return sourceNavigation.selectedSourcePath();
    }
    public String selectedSourceStatusSummary() {
        return sourceNavigation.selectedSourceStatusSummary();
    }
    public String sourceStatusSummary(UmlClassNode node) {
        return sourceNavigation.sourceStatusSummary(node);
    }
    public void openSelectedSourceInCodeEditor() {
        sourceNavigation.openSelectedSourceInCodeEditor();
    }
    public void openSelectedSourceWithProgramChooser() {
        sourceNavigation.openSelectedSourceWithProgramChooser();
    }
    public void openSelectedSourceWithSystemDefault() {
        sourceNavigation.openSelectedSourceWithSystemDefault();
    }
    public void openSelectedSourceFolder() {
        sourceNavigation.openSelectedSourceFolder();
    }
    public void openSourceForClassId(String classId) {
        sourceNavigation.openSourceForClassId(classId);
    }
    public void handleCanvasRenderFailure(CanvasRenderFailureReport report) {
        layoutCoordinator.handleCanvasRenderFailure(report, this::switchToSummaryAfterFailure);
    }
    private boolean switchToSummaryAfterFailure() {
        if (currentDocument == null) {
            return false;
        }
        Optional<UmlClassDiagramView> summary = currentDocument.views().stream()
                .filter(view -> view.kind() == UmlClassDiagramViewKind.SUMMARY)
                .findFirst();
        if (summary.isEmpty()) {
            return false;
        }
        UmlClassDiagramView current = selectedView.get();
        if (current != null && current.id().equals(summary.get().id())) {
            return false;
        }
        selectedView.set(summary.get());
        refreshListsPreservingSelection();
        return true;
    }
    public NodeLayout layoutForModule(UmlModuleGroup module) {
        return layoutCoordinator.layoutForModule(module);
    }
    public NodeLayout layoutForClass(UmlClassNode node) {
        return layoutCoordinator.layoutForClass(node);
    }
    public Optional<ConnectorLayout> layoutForConnector(DiagramElementId connectorId) {
        return layoutCoordinator.layoutForConnector(connectorId);
    }
    public void selectModuleById(String moduleId) { if (moduleId == null || moduleId.isBlank()) return; selectedModule.set(modules.stream().filter(module -> module.id().equals(moduleId.strip())).findFirst().orElse(null)); if (selectedModule.get() != null) { selectedClass.set(null); selectedMember.set(null); selectedRelation.set(null); statusConsumer.accept(labelPolicy.statusForModule(selectedModule.get())); } }
    public void selectClassNodeById(String classId) { if (classId == null || classId.isBlank()) return; selectedClass.set(classes.stream().filter(node -> node.id().equals(classId.strip())).findFirst().orElse(null)); if (selectedClass.get() != null) { selectedModule.set(null); selectedMember.set(null); selectedRelation.set(null); refreshMembersForSelection(); statusConsumer.accept(labelPolicy.statusForClass(selectedClass.get())); } }
    public void selectRelationByIdForCanvas(String relationId) { if (relationId == null || relationId.isBlank()) return; selectedRelation.set(relations.stream().filter(relation -> relation.id().equals(relationId.strip())).findFirst().orElse(null)); if (selectedRelation.get() != null) { selectedModule.set(null); selectedClass.set(null); selectedMember.set(null); } }
    public void clearPropertySelection() { selectedModule.set(null); selectedClass.set(null); selectedMember.set(null); selectedRelation.set(null); }
    public Optional<Integer> addConnectorBendPoint(DiagramElementId connectorId, double x, double y) {
        return layoutCoordinator.addConnectorBendPoint(connectorId, x, y);
    }
    public boolean reorderSelectedElement(VisualLayerOrderCommand command) {
        if (viewActions.reorderSelectedVisualComment(command)) {
            viewActions.refreshDiagramView();
            return true;
        }
        DiagramElementId layoutId = selectedClass.get() != null
                ? VisualElementLayoutIds.umlClass(selectedClass.get().id())
                : selectedModule.get() == null ? null : VisualElementLayoutIds.umlModule(selectedModule.get().id());
        boolean reordered = layoutCoordinator.reorderSelectedElement(layoutId, command);
        if (reordered) {
            viewActions.refreshDiagramView();
        }
        return reordered;
    }

    public boolean resizeSelectedElement(VisualNodeSizeCommand command) {
        if (viewActions.resizeSelectedVisualComment(command)) {
            viewActions.refreshDiagramView();
            return true;
        }
        DiagramElementId layoutId = selectedClass.get() != null
                ? VisualElementLayoutIds.umlClass(selectedClass.get().id())
                : selectedModule.get() == null ? null : VisualElementLayoutIds.umlModule(selectedModule.get().id());
        boolean resized = layoutCoordinator.resizeSelectedElement(layoutId, command);
        if (resized) {
            viewActions.refreshDiagramView();
        }
        return resized;
    }
    public void moveConnectorBendPointTo(DiagramElementId connectorId, int bendPointIndex, double x, double y) {
        layoutCoordinator.moveConnectorBendPointTo(connectorId, bendPointIndex, x, y);
    }
    public void removeConnectorBendPoint(DiagramElementId connectorId, int bendPointIndex) {
        layoutCoordinator.removeConnectorBendPoint(connectorId, bendPointIndex);
    }
    public void moveModuleTo(String moduleId, double x, double y) {
        layoutCoordinator.moveModuleTo(moduleId, x, y);
    }
    public void moveConnectorLabelBy(DiagramElementId connectorId, double deltaX, double deltaY) {
        layoutCoordinator.moveConnectorLabelBy(connectorId, deltaX, deltaY);
    }
    public void moveClassTo(String classId, double x, double y) {
        layoutCoordinator.moveClassTo(classId, x, y);
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
    private void applyDocument(UmlClassDiagramDocument updated, String message) {
        currentDocument = Objects.requireNonNull(updated, "updated");
        rebuildSearchIndex();
        layoutCoordinator.configureRenderProfile(currentDocument);
        if (currentProject != null) {
            currentProject = currentProject.withUmlClassDiagram(updated);
            layoutCoordinator.resetLayoutPreparation();
        }
        refreshLists();
        projectChangeSupport.notifyChanged(currentProject);
        statusConsumer.accept(message);
    }
    private void refreshLists() {
        if (currentDocument == null) {
            views.clear();
            modules.clear();
            classes.clear();
            relations.clear();
            layoutCoordinator.resetVisualState();
            refreshMembersForSelection();
            return;
        }
        syncViews();
        if (selectedView.get() != null) {
            selectedView.set(currentDocument.viewById(selectedView.get().id())
                    .orElse(currentDocument.views().isEmpty() ? null : currentDocument.views().get(0)));
        }
        UmlClassDiagramFilterResult filtered = filterEngine.apply(currentDocument, filterState(), searchIndex);
        layoutCoordinator.refreshVisualState(filtered);
        modules.setAll(filtered.modules());
        classes.setAll(filtered.classes());
        relations.setAll(filtered.relations());
        refreshMembersForSelection();
    }
    private UmlClassDiagramDocument scopedDocumentForVisualExport(UmlClassDiagramFilterResult filtered) {
        UmlClassDiagramFilterResult safeFiltered = filtered == null
                ? new UmlClassDiagramFilterResult(List.of(), List.of(), List.of())
                : filtered;
        UmlClassDiagramView currentView = selectedView.get();
        String viewName = currentView == null ? "Vista activa" : currentView.displayName();
        UmlClassDiagramView exportView = new UmlClassDiagramView(
                "export_active_view",
                currentView == null ? UmlClassDiagramViewKind.CUSTOM : currentView.kind(),
                "Exportación - " + viewName,
                "Vista filtrada exportada desde el lienzo UML Clases activo.",
                currentView == null ? List.of() : currentView.sourceRootIds(),
                safeFiltered.modules().stream().map(UmlModuleGroup::id).toList(),
                safeFiltered.classes().stream().map(UmlClassNode::id).toList(),
                safeFiltered.relations().stream().map(UmlClassRelation::id).toList(),
                "Exportación visual acotada; el documento .dms conserva el modelo completo.");
        return new UmlClassDiagramDocument(
                currentDocument.projectName(),
                currentDocument.version(),
                currentDocument.documentDate(),
                safeFiltered.modules(),
                safeFiltered.classes(),
                safeFiltered.relations(),
                List.of(exportView),
                currentDocument.notes());
    }
    private void syncViews() {
        if (!views.equals(currentDocument.views())) {
            views.setAll(currentDocument.views());
        }
    }
    private void rebuildSearchIndex() {
        searchIndex = UmlClassSearchIndex.from(currentDocument);
    }
    private void refreshListsPreservingSelection() {
        String moduleId = selectedModule.get() == null ? "" : selectedModule.get().id();
        String classId = selectedClass.get() == null ? "" : selectedClass.get().id();
        String memberId = selectedMember.get() == null ? "" : selectedMember.get().id();
        String relationId = selectedRelation.get() == null ? "" : selectedRelation.get().id();
        refreshLists();
        selectedModule.set(modules.stream().filter(module -> module.id().equals(moduleId)).findFirst().orElse(null));
        selectedClass.set(classes.stream().filter(node -> node.id().equals(classId)).findFirst().orElse(null));
        selectMemberById(memberId);
        selectedRelation.set(relations.stream().filter(relation -> relation.id().equals(relationId)).findFirst().orElse(null));
    }
    private UmlClassDiagramFilterState filterState() {
        return new UmlClassDiagramFilterState(
                selectedView.get() == null ? "" : selectedView.get().id(),
                searchQuery.get(),
                classKindFilter.get(),
                relationKindFilter.get()
        );
    }
    public void refreshMembersForSelection() {
        UmlClassNode node = selectedClass.get();
        members.setAll(node == null ? List.of() : node.members());
    }
    private void selectMemberById(String id) {
        refreshMembersForSelection();
        members.stream().filter(member -> member.id().equals(id)).findFirst().ifPresent(selectedMember::set);
    }
    private final class UmlClassLayoutPort implements UmlClassDiagramLayoutCoordinator.Port {
        @Override
        public DiagramProject currentProject() { return currentProject; }
        @Override
        public UmlClassDiagramDocument currentDocument() { return currentDocument; }
        @Override
        public void replaceCurrentProject(DiagramProject project) { currentProject = project; }
        @Override
        public UmlClassDiagramView selectedView() { return selectedView.get(); }
        @Override
        public String searchQuery() { return searchQuery.get(); }
        @Override
        public UmlClassKind classKindFilter() { return classKindFilter.get(); }
        @Override
        public UmlRelationKind relationKindFilter() { return relationKindFilter.get(); }
        @Override
        public void notifyProjectChanged() { UmlClassDiagramViewModel.this.notifyProjectChanged(); }
        @Override
        public void status(String message) { statusConsumer.accept(message); }
    }
    private void requireDocument() {
        if (currentDocument == null || currentProject == null) {
            throw new IllegalStateException("No hay diagrama UML Clases activo.");
        }
    }
}
