package com.marcosmoreira.domainmodelstudio.presentation.shell;
import com.marcosmoreira.domainmodelstudio.application.ApplicationServices;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCategory;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeDescriptor;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeScreenTemplateKind;
import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import com.marcosmoreira.domainmodelstudio.presentation.canvas.DiagramCanvasViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.conceptual.ConceptualCanvasLegacyBridge;
import com.marcosmoreira.domainmodelstudio.presentation.datadictionary.DataDictionaryViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.modulemap.ModuleMapViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.freegraph.FreeGraphViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness.LogicalBusinessViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.logicalbusinessgraph.LogicalBusinessGraphViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.umlclass.UmlClassDiagramViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.rolespermissions.RolesPermissionsViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.screenflow.ScreenFlowViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.wireframe.WireframeViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.behavior.BehaviorDiagramViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.architecture.ArchitectureDiagramViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.dialogs.ManualDialog;
import com.marcosmoreira.domainmodelstudio.presentation.inspector.InspectorViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.sidebar.ModelTreeViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.placeholder.PlaceholderWorkspaceViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.shell.commands.DiagramCommandHandler;
import com.marcosmoreira.domainmodelstudio.presentation.shell.commands.ExportCommandHandler;
import com.marcosmoreira.domainmodelstudio.presentation.shell.commands.ImportCommandHandler;
import com.marcosmoreira.domainmodelstudio.presentation.shell.commands.ShellCommandContext;
import com.marcosmoreira.domainmodelstudio.presentation.shell.commands.ViewCommandHandler;
import com.marcosmoreira.domainmodelstudio.presentation.sidedock.SideDockModuleId;
import com.marcosmoreira.domainmodelstudio.presentation.exportable.ActiveOutputResolver;
import com.marcosmoreira.domainmodelstudio.presentation.exportable.ExportFormat;
import com.marcosmoreira.domainmodelstudio.presentation.exportable.ProjectExportFormatPolicy;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
/**
 * Manejador de comandos globales del shell.
 *
 * <p>Esta clase permanece en presentation porque conoce interacciones de escritorio como
 * FileChooser. La lógica real se delega a application; la actualización visual se limita
 * a ViewModels pequeños.</p>
 */
public final class MainShellCommandHandler {
    private final MainShellState shellState;
    private final ApplicationServices applicationServices;
    private final ModelTreeViewModel modelTreeViewModel;
    private final DiagramCanvasViewModel canvasViewModel;
    private final InspectorViewModel inspectorViewModel;
    private final DataDictionaryViewModel dataDictionaryViewModel;
    private final ModuleMapViewModel moduleMapViewModel;
    private final UmlClassDiagramViewModel umlClassDiagramViewModel;
    private final RolesPermissionsViewModel rolesPermissionsViewModel;
    private final ScreenFlowViewModel screenFlowViewModel;
    private final WireframeViewModel wireframeViewModel;
    private final BehaviorDiagramViewModel behaviorDiagramViewModel;
    private final ArchitectureDiagramViewModel architectureDiagramViewModel;
    private final FreeGraphViewModel freeGraphViewModel;
    private final LogicalBusinessViewModel logicalBusinessViewModel;
    private final LogicalBusinessGraphViewModel logicalBusinessGraphViewModel;
    private final ProjectSessionCoordinator projectSessionCoordinator = new ProjectSessionCoordinator();
    private final ProjectHistoryCoordinator projectHistoryCoordinator;
    private final ImportCommandHandler importCommandHandler;
    private final DiagramCommandHandler diagramCommandHandler;
    private final ExportCommandHandler exportCommandHandler;
    private final ViewCommandHandler viewCommandHandler;
    private final ConceptualModelShellCommands conceptualModelCommands;
    private final DataDictionaryShellCommands dataDictionaryCommands;
    private final ModuleMapShellCommands moduleMapCommands;
    private final UmlClassShellCommands umlClassCommands;
    private final AdministrativeWorkspaceShellCommands administrativeWorkspaceCommands;
    private final BehaviorDiagramShellCommands behaviorDiagramCommands;
    private final ArchitectureDiagramShellCommands architectureDiagramCommands;
    private final FreeGraphShellCommands freeGraphCommands;
    private final ActiveOutputResolver activeOutputResolver;
    private final SpecializedWorkspaceCoordinator specializedWorkspaces;
    private final ClientBatchExportCoordinator clientBatchExportCoordinator;
    private final NewProjectFactory newProjectFactory;
    private final ValidationDialogPresenter validationDialogPresenter;
    private final ProjectValidationCoordinator projectValidationCoordinator;
    private final EditorActivationGuard editorActivationGuard;
    private final SpecializedProjectSynchronizer specializedProjectSynchronizer;
    private final ProjectSaveCoordinator projectSaveCoordinator;
    private final ProjectCreationCoordinator projectCreationCoordinator;
    private final ProjectOpenCoordinator projectOpenCoordinator;
    private final UnsavedChangesDialog unsavedChangesDialog;
    private final VisualLayerOrderShellCommands visualLayerOrderCommands;
    private final VisualNodeSizeShellCommands visualNodeSizeCommands;
    private final VisualSelectionTransferCoordinator visualSelectionTransferCoordinator;
    public MainShellCommandHandler(
            MainShellState shellState,
            ApplicationServices applicationServices,
            ModelTreeViewModel modelTreeViewModel,
            DiagramCanvasViewModel canvasViewModel,
            InspectorViewModel inspectorViewModel,
            DataDictionaryViewModel dataDictionaryViewModel,
            ModuleMapViewModel moduleMapViewModel,
            UmlClassDiagramViewModel umlClassDiagramViewModel,
            RolesPermissionsViewModel rolesPermissionsViewModel,
            ScreenFlowViewModel screenFlowViewModel,
            WireframeViewModel wireframeViewModel,
            BehaviorDiagramViewModel behaviorDiagramViewModel,
            ArchitectureDiagramViewModel architectureDiagramViewModel,
            FreeGraphViewModel freeGraphViewModel,
            LogicalBusinessViewModel logicalBusinessViewModel,
            LogicalBusinessGraphViewModel logicalBusinessGraphViewModel
    ) {
        this.shellState = Objects.requireNonNull(shellState, "shellState");
        this.applicationServices = Objects.requireNonNull(applicationServices, "applicationServices");
        this.modelTreeViewModel = Objects.requireNonNull(modelTreeViewModel, "modelTreeViewModel");
        this.canvasViewModel = Objects.requireNonNull(canvasViewModel, "canvasViewModel");
        this.inspectorViewModel = Objects.requireNonNull(inspectorViewModel, "inspectorViewModel");
        this.dataDictionaryViewModel = Objects.requireNonNull(dataDictionaryViewModel, "dataDictionaryViewModel");
        this.moduleMapViewModel = Objects.requireNonNull(moduleMapViewModel, "moduleMapViewModel");
        this.umlClassDiagramViewModel = Objects.requireNonNull(umlClassDiagramViewModel, "umlClassDiagramViewModel");
        this.rolesPermissionsViewModel = Objects.requireNonNull(rolesPermissionsViewModel, "rolesPermissionsViewModel");
        this.screenFlowViewModel = Objects.requireNonNull(screenFlowViewModel, "screenFlowViewModel");
        this.wireframeViewModel = Objects.requireNonNull(wireframeViewModel, "wireframeViewModel");
        this.behaviorDiagramViewModel = Objects.requireNonNull(behaviorDiagramViewModel, "behaviorDiagramViewModel");
        this.architectureDiagramViewModel = Objects.requireNonNull(architectureDiagramViewModel, "architectureDiagramViewModel");
        this.freeGraphViewModel = Objects.requireNonNull(freeGraphViewModel, "freeGraphViewModel");
        this.logicalBusinessViewModel = Objects.requireNonNull(logicalBusinessViewModel, "logicalBusinessViewModel");
        this.logicalBusinessGraphViewModel = Objects.requireNonNull(logicalBusinessGraphViewModel, "logicalBusinessGraphViewModel");
        this.activeOutputResolver = new ActiveOutputResolver(
                this.canvasViewModel,
                this.dataDictionaryViewModel,
                this.moduleMapViewModel,
                this.umlClassDiagramViewModel,
                this.rolesPermissionsViewModel,
                this.screenFlowViewModel,
                this.wireframeViewModel,
                this.behaviorDiagramViewModel,
                this.architectureDiagramViewModel,
                this.freeGraphViewModel,
                this.logicalBusinessViewModel,
                this.logicalBusinessGraphViewModel,
                this::activeProjectForOutput);
        this.specializedWorkspaces = new SpecializedWorkspaceCoordinator(
                new ConceptualCanvasLegacyBridge(this.modelTreeViewModel, this.canvasViewModel, this.inspectorViewModel),
                this.dataDictionaryViewModel,
                this.moduleMapViewModel,
                this.umlClassDiagramViewModel,
                this.rolesPermissionsViewModel,
                this.screenFlowViewModel,
                this.wireframeViewModel,
                this.behaviorDiagramViewModel,
                this.architectureDiagramViewModel,
                this.freeGraphViewModel,
                this.logicalBusinessViewModel,
                this.logicalBusinessGraphViewModel);
        this.visualLayerOrderCommands = new VisualLayerOrderShellCommands(
                this.shellState, this.specializedWorkspaces, this::activeProjectForOutput);
        this.visualNodeSizeCommands = new VisualNodeSizeShellCommands(
                this.shellState, this.specializedWorkspaces, this::activeProjectForOutput);
        ShellCommandContext commandContext = new ShellCommandContext(
                this.shellState,
                this.applicationServices,
                this.modelTreeViewModel,
                this.canvasViewModel,
                this.inspectorViewModel,
                this.activeOutputResolver);
        this.importCommandHandler = new ImportCommandHandler(commandContext, this::openProjectInNewTab);
        this.diagramCommandHandler = new DiagramCommandHandler(commandContext, this::markCurrentSessionDirty);
        this.exportCommandHandler = new ExportCommandHandler(commandContext);
        this.viewCommandHandler = new ViewCommandHandler(commandContext);
        this.conceptualModelCommands = new ConceptualModelShellCommands(commandContext, this::markCurrentSessionDirty);
        this.clientBatchExportCoordinator = new ClientBatchExportCoordinator(
                this.shellState,
                this.applicationServices,
                new ProjectExportFormatPolicy(),
                this.activeOutputResolver,
                this.projectSessionCoordinator.sessionsByTabId(),
                this::currentProjectForSaving,
                this::activateProjectSession,
                this::activateHomeTab,
                this.projectSessionCoordinator::activeProjectTabId);
        this.newProjectFactory = new NewProjectFactory(this.applicationServices);
        ProjectCreationCoordinator.ProjectOpenTarget projectOpenTarget = new ProjectCreationCoordinator.ProjectOpenTarget() {
            @Override
            public void openProject(DiagramProject project, String statusLabel, boolean dirty, Path projectFile) {
                MainShellCommandHandler.this.openProjectInNewTab(project, statusLabel, dirty, projectFile);
            }

            @Override
            public void openPlaceholder(
                    DiagramProject project,
                    DiagramTypeDescriptor descriptor,
                    boolean dirty,
                    String statusLabel,
                    Path projectFile
            ) {
                MainShellCommandHandler.this.openPlaceholderInNewTab(project, descriptor, dirty, statusLabel, projectFile);
            }
        };
        this.projectCreationCoordinator = new ProjectCreationCoordinator(
                this.shellState,
                this.applicationServices,
                this.projectSessionCoordinator,
                this.newProjectFactory,
                projectOpenTarget);
        this.projectOpenCoordinator = new ProjectOpenCoordinator(
                this.shellState,
                this.applicationServices,
                projectOpenTarget);
        this.editorActivationGuard = new EditorActivationGuard(this.shellState);
        this.specializedProjectSynchronizer = new SpecializedProjectSynchronizer(this.shellState, this::activeSession);
        this.projectHistoryCoordinator = new ProjectHistoryCoordinator(
                this.shellState,
                this.projectSessionCoordinator,
                this::activeSession,
                this::currentProjectForSaving,
                this.specializedWorkspaces::replaceActiveProjectIfCompatible,
                this::showProjectInEditor,
                this::refreshSessionDirtyState,
                this::refreshActiveOutputState);
        this.projectSaveCoordinator = new ProjectSaveCoordinator(
                this.shellState,
                this.applicationServices,
                this::activeSession,
                this::currentProjectForSaving,
                this::markCurrentSessionSaved);
        this.unsavedChangesDialog = new UnsavedChangesDialog(this.shellState, this::saveCurrentProject);
        this.validationDialogPresenter = new ValidationDialogPresenter();
        this.visualSelectionTransferCoordinator = new VisualSelectionTransferCoordinator(
                this.shellState,
                this.projectSessionCoordinator,
                this.freeGraphViewModel,
                this.logicalBusinessGraphViewModel,
                this::activateProjectSession,
                this::refreshActiveOutputState);
        this.projectValidationCoordinator = new ProjectValidationCoordinator(
                this.shellState,
                this.dataDictionaryViewModel,
                this.moduleMapViewModel,
                this.umlClassDiagramViewModel,
                this.rolesPermissionsViewModel,
                this.screenFlowViewModel,
                this.wireframeViewModel,
                this.behaviorDiagramViewModel,
                this.architectureDiagramViewModel,
                this.freeGraphViewModel,
                this.logicalBusinessViewModel,
                this.logicalBusinessGraphViewModel,
                this.validationDialogPresenter);
        this.dataDictionaryCommands = new DataDictionaryShellCommands(
                this.shellState,
                this.dataDictionaryViewModel,
                this.projectValidationCoordinator,
                this.exportCommandHandler);
        this.moduleMapCommands = new ModuleMapShellCommands(
                this.shellState,
                this.moduleMapViewModel,
                this.projectValidationCoordinator);
        this.umlClassCommands = new UmlClassShellCommands(
                this.shellState,
                this.umlClassDiagramViewModel,
                this.projectValidationCoordinator,
                this.specializedProjectSynchronizer);
        this.administrativeWorkspaceCommands = new AdministrativeWorkspaceShellCommands(
                this.rolesPermissionsViewModel,
                this.screenFlowViewModel,
                this.wireframeViewModel,
                this.projectValidationCoordinator,
                this.specializedProjectSynchronizer,
                this.editorActivationGuard);
        this.behaviorDiagramCommands = new BehaviorDiagramShellCommands(
                this.shellState,
                this.behaviorDiagramViewModel,
                this.projectValidationCoordinator,
                this.specializedProjectSynchronizer);
        this.architectureDiagramCommands = new ArchitectureDiagramShellCommands(
                this.shellState,
                this.architectureDiagramViewModel,
                this.projectValidationCoordinator,
                this.specializedProjectSynchronizer);
        this.freeGraphCommands = new FreeGraphShellCommands(
                this.freeGraphViewModel,
                this.projectValidationCoordinator,
                this.specializedProjectSynchronizer,
                this.editorActivationGuard);
        installProjectSessionSync();
    }
    private void installProjectSessionSync() {
        canvasViewModel.currentProjectProperty().addListener((observable, previous, current) -> {
            if (projectSessionCoordinator.activatingProjectSession() || projectSessionCoordinator.activeProjectTabId() == null || current == null) {
                return;
            }
            ProjectSession session = projectSessionCoordinator.activeSession();
            if (session != null && !session.isPlaceholder()) {
                session.rememberEditTransition(previous, current);
                session.project = current;
                shellState.updateProjectTab(session.tabId, current.metadata().title(), session.dirty);
            }
        });
    }
    public void activateEditorTab(String tabId) {
        if (tabId == null || tabId.isBlank() || MainShellState.HOME_TAB_ID.equals(tabId)) {
            activateHomeTab();
            return;
        }
        ProjectSession session = projectSessionCoordinator.get(tabId);
        if (session == null) {
            shellState.updateStatus("La pestaña ya no está disponible.");
            activateHomeTab();
            return;
        }
        activateProjectSession(session, "Proyecto abierto");
    }
    public void closeEditorTab(String tabId) {
        if (tabId == null || MainShellState.HOME_TAB_ID.equals(tabId)) {
            activateHomeTab();
            return;
        }
        ProjectSession session = projectSessionCoordinator.get(tabId);
        if (session == null) {
            return;
        }
        if (!confirmBeforeClosingSession(session)) {
            shellState.updateStatus("Cierre de pestaña cancelado.");
            return;
        }
        boolean wasActive = tabId.equals(projectSessionCoordinator.activeProjectTabId());
        Optional<String> nextVisibleTabId = wasActive ? shellState.adjacentProjectTabIdAfterClosing(tabId) : Optional.empty();
        projectSessionCoordinator.remove(tabId);
        shellState.removeProjectTab(tabId);
        if (wasActive) {
            ProjectSession next = nextVisibleTabId.map(projectSessionCoordinator::get).orElse(null);
            if (next == null) {
                activateHomeTab();
            } else {
                activateProjectSession(next, "Proyecto abierto");
            }
        } else {
            shellState.updateStatus("Pestaña cerrada.");
        }
    }
    public void reorderEditorTabAfter(String movedTabId, String targetTabId) {
        boolean reordered = shellState.moveProjectTabAfter(movedTabId, targetTabId);
        if (reordered) {
            shellState.updateStatus("Pestañas reordenadas.");
        }
    }
    private void activateHomeTab() {
        projectSessionCoordinator.activateHome();
        projectSessionCoordinator.runActivating(() -> {
            clearAllProjectViews();
            shellState.showNoProjectState();
            refreshActiveOutputState();
            shellState.setActiveEditorTab(MainShellState.HOME_TAB_ID);
        });
    }
    private void openProjectInNewTab(DiagramProject project, String statusLabel, boolean dirty) {
        openProjectInNewTab(project, statusLabel, dirty, null);
    }
    private void openProjectInNewTab(DiagramProject project, String statusLabel, boolean dirty, Path projectFile) {
        ProjectSession session = projectSessionCoordinator.createProjectSession(project, dirty, projectFile);
        shellState.addProjectTab(session.tabId, project.metadata().title(), dirty);
        activateProjectSession(session, statusLabel);
    }
    private void activateProjectSession(ProjectSession session, String statusLabel) {
        projectSessionCoordinator.activate(session);
        projectSessionCoordinator.runActivating(() -> {
            if (session.isPlaceholder()) {
                clearAllProjectViews();
                shellState.showPlaceholderState(session.placeholder, statusLabel);
                refreshSessionDirtyState(session);
                refreshActiveOutputState();
                return;
            }
            showProjectInEditor(session.project, statusLabel);
            refreshSessionDirtyState(session);
            refreshActiveOutputState();
        });
    }
    private void showProjectInEditor(DiagramProject project, String statusLabel) {
        clearAllProjectViews();
        if (!specializedWorkspaces.loadIfSpecialized(project)) {
            shellState.updateStatus("No hay workspace registrado para "
                    + project.metadata().diagramTypeId().value() + ".");
        }
        shellState.showProjectState(project, statusLabel);
    }
    private void clearAllProjectViews() {
        clearSpecializedEditors();
    }
    private void clearSpecializedEditors() {
        specializedWorkspaces.clearAll();
    }
    private void refreshSessionDirtyState(ProjectSession session) {
        if (session.dirty) {
            shellState.markDirty();
        } else {
            shellState.markSaved();
        }
        shellState.setActiveEditorTab(session.tabId);
        shellState.updateProjectTab(session.tabId, session.title(), session.dirty);
    }
    private ProjectSession activeSession() {
        return projectSessionCoordinator.activeSession();
    }
    private Optional<DiagramProject> activeProjectForOutput() {
        ProjectSession session = activeSession();
        if (session == null || session.isPlaceholder()) {
            return Optional.empty();
        }
        Optional<DiagramProject> editorProject = currentProjectFromActiveEditor()
                .filter(project -> sameSessionProject(session, project));
        return editorProject.or(() -> Optional.ofNullable(session.project));
    }
    private void refreshActiveOutputState() {
        Set<ExportFormat> formats = activeOutputResolver.activeOutput()
                .map(output -> output.descriptor().supportedFormats())
                .orElse(Set.of());
        shellState.setActiveExportFormats(formats);
    }
    private void markCurrentSessionDirty() {
        ProjectSession session = activeSession();
        if (session != null) {
            refreshSessionProjectFromActiveEditor(session);
            session.dirty = true;
            shellState.updateProjectTab(session.tabId, session.title(), true);
        }
        shellState.markDirty();
    }
    private void markCurrentSessionSaved() {
        ProjectSession session = activeSession();
        if (session != null) {
            refreshSessionProjectFromActiveEditor(session);
            session.dirty = false;
            shellState.updateProjectTab(session.tabId, session.title(), false);
        }
        shellState.markSaved();
    }
    private void refreshSessionProjectFromActiveEditor(ProjectSession session) {
        if (session == null || session.isPlaceholder()) {
            return;
        }
        currentProjectFromActiveEditor()
                .filter(project -> sameSessionProject(session, project))
                .ifPresent(project -> session.project = project);
    }
    private boolean sameSessionProject(ProjectSession session, DiagramProject project) {
        if (session == null || project == null || session.project == null) {
            return false;
        }
        return Objects.equals(session.project.metadata().id(), project.metadata().id())
                && Objects.equals(session.project.metadata().diagramTypeId(), project.metadata().diagramTypeId());
    }
    private Optional<DiagramProject> currentProjectFromActiveEditor() {
        DiagramProject current = canvasViewModel.currentProject();
        if (current != null) {
            return Optional.of(current);
        }
        return firstActiveSpecializedProject();
    }
    private Optional<DiagramProject> firstActiveSpecializedProject() {
        return specializedWorkspaces.firstActiveProject();
    }
    private boolean confirmBeforeClosingSession(ProjectSession session) {
        if (!session.dirty) {
            return true;
        }
        if (session.tabId.equals(projectSessionCoordinator.activeProjectTabId())) {
            return confirmBeforeReplacingProject("cerrar esta pestaña");
        }
        ButtonType discard = new ButtonType("Cerrar sin guardar", ButtonBar.ButtonData.NO);
        ButtonType cancel = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cambios sin guardar");
        alert.setHeaderText("La pestaña tiene cambios sin guardar.");
        alert.setContentText("Activa la pestaña para guardarla o ciérrala sin guardar.");
        alert.getButtonTypes().setAll(discard, cancel);
        return alert.showAndWait().orElse(cancel) == discard;
    }
    public BooleanProperty projectOpenProperty() {
        return shellState.projectOpenProperty();
    }
    public BooleanProperty saveableProjectOpenProperty() {
        return shellState.saveableProjectOpenProperty();
    }
    public ObjectProperty<NotationType> activeNotationProperty() {
        return shellState.activeNotationProperty();
    }
    public void requestNewProject() { projectCreationCoordinator.requestNewProject(); }
    private void openPlaceholderInNewTab(
            DiagramProject project,
            DiagramTypeDescriptor descriptor,
            boolean dirty,
            String statusLabel,
            Path projectFile
    ) {
        PlaceholderWorkspaceViewModel placeholder = PlaceholderWorkspaceViewModel.from(
                descriptor,
                categoryDisplayName(descriptor));
        ProjectSession session = projectSessionCoordinator.createPlaceholderSession(project, placeholder, dirty, projectFile);
        shellState.addProjectTab(session.tabId, placeholder.title(), dirty);
        activateProjectSession(session, statusLabel);
    }
    private String categoryDisplayName(DiagramTypeDescriptor descriptor) {
        return applicationServices.listDiagramCategoriesUseCase().execute().stream()
                .filter(category -> category.id().equals(descriptor.categoryId()))
                .map(DiagramCategory::displayName)
                .findFirst()
                .orElse(descriptor.categoryId().value());
    }
    public void requestOpenProject() { projectOpenCoordinator.requestOpenProject(); }
    public void requestUndo() { projectHistoryCoordinator.requestUndo(); }
    public void requestRedo() { projectHistoryCoordinator.requestRedo(); }
    public void requestSaveProject() {
        saveCurrentProject();
    }
    public void requestSaveProjectAs() { saveCurrentProjectWithDialog(true); }
    public void requestCloseProject() {
        if (projectSessionCoordinator.activeProjectTabId() == null) {
            shellState.updateStatus("No hay proyecto abierto para cerrar.");
            return;
        }
        closeEditorTab(projectSessionCoordinator.activeProjectTabId());
    }
    public void requestExitApplication() {
        if (canCloseApplication()) {
            Platform.exit();
        } else {
            shellState.updateStatus("Salida cancelada.");
        }
    }
    public boolean canCloseApplication() {
        return unsavedChangesDialog.confirmBeforeApplicationExit(projectSessionCoordinator.sessions());
    }
    public ObjectProperty<DiagramTypeId> activeDiagramTypeProperty() {
        return shellState.activeDiagramTypeProperty();
    }
    public ObjectProperty<Set<ExportFormat>> activeExportFormatsProperty() {
        return shellState.activeExportFormatsProperty();
    }
    public void requestOpenManual() {
        ManualDialog.show();
        shellState.updateStatus("Guía académica abierta.");
    }
    public void requestExportAiResources() {
        importCommandHandler.requestExportAiResources();
    }
    public void requestOpenExampleProject() {
        importCommandHandler.requestOpenExampleProject();
    }
    public void requestImportMarkdown() {
        importCommandHandler.requestImportMarkdown();
    }
    public void requestImportMarkdownFolder() {
        importCommandHandler.requestImportMarkdownFolder();
    }
    public void requestImportUmlClassFromSourceCode() { importCommandHandler.requestImportUmlClassFromSourceCode(); }
    public boolean importMarkdownFile(Path markdownFile) {
        return importCommandHandler.importMarkdownFile(markdownFile);
    }
    public void synchronizeStructuralEditFromCanvas(DiagramProject project) {
        conceptualModelCommands.synchronizeStructuralEditFromCanvas(project);
    }
    public void synchronizeDataDictionaryEdit(DiagramProject project) {
        specializedProjectSynchronizer.synchronize(project, "Diccionario de datos actualizado");
    }
    public void synchronizeModuleMapEdit(DiagramProject project) {
        specializedProjectSynchronizer.synchronize(project, "Mapa de módulos actualizado");
    }
    public void synchronizeLogicalBusinessEdit(DiagramProject project) {
        if (projectSessionCoordinator.activatingProjectSession() || project == null) {
            return;
        }
        specializedProjectSynchronizer.synchronize(project, "Levantamiento lógico actualizado");
        refreshActiveOutputState();
    }

    public void synchronizeLogicalBusinessGraphEdit(DiagramProject project) {
        if (projectSessionCoordinator.activatingProjectSession() || project == null) {
            return;
        }
        specializedProjectSynchronizer.synchronize(project, "Grafo lógico del negocio actualizado");
        refreshActiveOutputState();
    }
    public void requestAddEntityTool() { conceptualModelCommands.requestAddEntityTool(); }
    public void requestAddAttributeToSelectedEntity() { conceptualModelCommands.requestAddAttributeToSelectedEntity(); }
    public void requestAddRelationshipTool() { conceptualModelCommands.requestAddRelationshipTool(); }
    public void requestDuplicateSelectedEntity() { conceptualModelCommands.requestDuplicateSelectedEntity(); }
    public void requestRemoveSelectedElement() { conceptualModelCommands.requestRemoveSelectedElement(); }
    public void requestAddDataDictionaryEntity() { dataDictionaryCommands.requestAddEntity(); }
    public void requestAddDataDictionaryField() { dataDictionaryCommands.requestAddField(); }
    public void requestRemoveDataDictionaryItem() { dataDictionaryCommands.requestRemoveItem(); }
    public void requestValidateDataDictionary() { dataDictionaryCommands.requestValidate(); }
    public void requestExportDataDictionaryPdf() { dataDictionaryCommands.requestExportPdf(); }
    public void requestAddModuleMapModule() { moduleMapCommands.requestAddModule(); }
    public void requestAddModuleMapSubmodule() { moduleMapCommands.requestAddSubmodule(); }
    public void requestAddModuleMapDependency() { moduleMapCommands.requestAddDependency(); }
    public void requestRemoveModuleMapItem() { moduleMapCommands.requestRemoveItem(); }
    public void requestValidateModuleMap() { moduleMapCommands.requestValidate(); }
    public void requestValidateProject() { projectValidationCoordinator.validateActiveProject(diagramCommandHandler::requestValidateProject); }
    public void requestShowLogicalBusinessStructure() { logicalBusinessViewModel.requestSideDockModule(SideDockModuleId.SECTIONS); }
    public void requestShowLogicalBusinessProperties() { logicalBusinessViewModel.requestSideDockModule(SideDockModuleId.PROPERTIES); }
    public void requestShowLogicalBusinessValidation() { logicalBusinessViewModel.requestSideDockModule(SideDockModuleId.VALIDATION); }
    public void requestShowLogicalBusinessTraceability() { logicalBusinessViewModel.requestSideDockModule(SideDockModuleId.TRACEABILITY); }
    public void requestShowLogicalBusinessHelp() { logicalBusinessViewModel.requestSideDockModule(SideDockModuleId.HELP); }
    public void requestRegenerateLayout() {
        DiagramTypeId activeType = activeProjectForOutput()
                .map(project -> project.metadata().diagramTypeId())
                .orElse(null);
        if (DiagramTypeId.ADMIN_MODULE_MAP.equals(activeType) && moduleMapViewModel.active()) {
            moduleMapCommands.requestRegenerateLayout();
            return;
        }
        if (DiagramTypeId.UML_CLASS.equals(activeType) && umlClassDiagramViewModel.active()) {
            umlClassCommands.requestRegenerateLayout();
            return;
        }
        if ((DiagramTypeId.UML_USE_CASE.equals(activeType)
                || DiagramTypeId.BPMN_BASIC.equals(activeType)
                || DiagramTypeId.OPERATIONAL_FLOW.equals(activeType)
                || DiagramTypeId.UML_ACTIVITY.equals(activeType)
                || DiagramTypeId.UML_STATE.equals(activeType)
                || DiagramTypeId.UML_SEQUENCE.equals(activeType))
                && behaviorDiagramViewModel.active()) {
            behaviorDiagramCommands.requestRegenerateLayout();
            return;
        }
        if ((DiagramTypeId.C4_CONTEXT.equals(activeType) || DiagramTypeId.C4_CONTAINERS.equals(activeType))
                && architectureDiagramViewModel.active()) {
            architectureDiagramCommands.requestRegenerateLayout();
            return;
        }
        if (DiagramTypeId.FREE_GRAPH.equals(activeType) && freeGraphViewModel.active()) {
            freeGraphCommands.requestRegenerateLayout();
            return;
        }
        if (DiagramTypeId.LOGICAL_BUSINESS_GRAPH.equals(activeType) && logicalBusinessGraphViewModel.active()) {
            shellState.updateStatus("El grafo lógico ya usa auto-layout inicial; mueve los nodos manualmente para ajustar la vista.");
            return;
        }
        if (activeType != null && !DiagramTypeId.CONCEPTUAL_MODEL.equals(activeType)) {
            shellState.updateStatus("Autoorganización no disponible para el artefacto activo.");
            return;
        }
        diagramCommandHandler.requestRegenerateLayout();
    }
    public void requestDeleteSelectedBendPoint() {
        Optional<DiagramProject> activeProject = activeProjectForOutput();
        if (activeProject.isPresent() && !DiagramTypeId.CONCEPTUAL_MODEL.equals(activeProject.get().metadata().diagramTypeId())) {
            specializedWorkspaces.deleteSelectedBendPoint(activeProject.get());
            return;
        }
        conceptualModelCommands.requestDeleteSelectedBendPoint();
    }
    public void requestZoomIn() {
        viewCommandHandler.requestZoomIn();
    }
    public void requestZoomOut() {
        viewCommandHandler.requestZoomOut();
    }
    public void requestResetZoom() {
        viewCommandHandler.requestResetZoom();
    }
    public void requestFitToContent() {
        Optional<DiagramProject> activeProject = activeProjectForOutput();
        if (activeProject.isPresent()
                && !DiagramTypeId.CONCEPTUAL_MODEL.equals(activeProject.get().metadata().diagramTypeId())) {
            if (specializedWorkspaces.fitActiveDiagram(activeProject.get())) {
                shellState.updateStatus("Vista ajustada al contenido del diagrama activo.");
                return;
            }
        }
        viewCommandHandler.requestFitToContent();
    }
    public void requestCenterDiagram() {
        Optional<DiagramProject> activeProject = activeProjectForOutput();
        if (activeProject.isPresent()
                && !DiagramTypeId.CONCEPTUAL_MODEL.equals(activeProject.get().metadata().diagramTypeId())) {
            if (specializedWorkspaces.centerActiveDiagram(activeProject.get())) {
                shellState.updateStatus("Vista centrada en el diagrama activo sin cambiar el zoom.");
                return;
            }
        }
        viewCommandHandler.requestCenterDiagram();
    }

    public void requestBringSelectionToFront() { visualLayerOrderCommands.bringSelectionToFront(); }
    public void requestSendSelectionToBack() { visualLayerOrderCommands.sendSelectionToBack(); }
    public void requestRaiseSelectionLayer() { visualLayerOrderCommands.raiseSelectionLayer(); }
    public void requestLowerSelectionLayer() { visualLayerOrderCommands.lowerSelectionLayer(); }
    public void requestGrowSelectedVisualElement() { visualNodeSizeCommands.growSelection(); }
    public void requestShrinkSelectedVisualElement() { visualNodeSizeCommands.shrinkSelection(); }

    public void requestCenterSelection() {
        viewCommandHandler.requestCenterSelection();
    }
    public void requestSelectAllElements() {
        viewCommandHandler.requestSelectAllElements();
    }
    public void requestClearSelection() {
        viewCommandHandler.requestClearSelection();
    }
    public void requestSwitchNotation(NotationType notationType) {
        diagramCommandHandler.requestSwitchNotation(notationType);
    }
    private boolean confirmBeforeReplacingProject(String actionDescription) {
        return unsavedChangesDialog.confirmBefore(actionDescription);
    }
    private boolean saveCurrentProject() {
        return projectSaveCoordinator.saveCurrentProject();
    }
    private boolean saveCurrentProjectWithDialog(boolean saveAs) {
        return projectSaveCoordinator.saveCurrentProjectWithDialog(saveAs);
    }
    private DiagramProject currentProjectForSaving(ProjectSession session) {
        if (session == null || session.isPlaceholder()) {
            return null;
        }
        if (session.tabId.equals(projectSessionCoordinator.activeProjectTabId())) {
            return currentProjectFromActiveEditor().orElse(session.project);
        }
        return session.project;
    }
    public void synchronizeUmlClassDiagramEdit(DiagramProject updatedProject) {
        umlClassCommands.synchronizeEdit(updatedProject);
    }
    public void requestAddUmlModule() { umlClassCommands.requestAddModule(); }
    public void requestAddUmlClass() { umlClassCommands.requestAddClass(); }
    public void requestAddUmlInterface() { umlClassCommands.requestAddInterface(); }
    public void requestAddUmlEnum() { umlClassCommands.requestAddEnum(); }
    public void requestAddUmlAttribute() { umlClassCommands.requestAddAttribute(); }
    public void requestAddUmlMethod() { umlClassCommands.requestAddMethod(); }
    public void requestAddUmlRelation() { umlClassCommands.requestAddRelation(); }
    public void requestRemoveUmlItem() { umlClassCommands.requestRemoveItem(); }
    public void requestOpenSelectedUmlSourceFile() { umlClassCommands.requestOpenSelectedSourceFile(); }
    public void requestValidateUmlClassDiagram() { umlClassCommands.requestValidate(); }
    public void synchronizeRolesPermissionsEdit(DiagramProject updatedProject) {
        administrativeWorkspaceCommands.synchronizeRolesPermissionsEdit(updatedProject);
    }
    public void synchronizeScreenFlowEdit(DiagramProject updatedProject) {
        administrativeWorkspaceCommands.synchronizeScreenFlowEdit(updatedProject);
    }
    public void synchronizeWireframeEdit(DiagramProject updatedProject) {
        administrativeWorkspaceCommands.synchronizeWireframeEdit(updatedProject);
    }
    public void requestAddRole() { administrativeWorkspaceCommands.requestAddRole(); }
    public void requestAddPermission() { administrativeWorkspaceCommands.requestAddPermission(); }
    public void requestAddPermissionAssignment() { administrativeWorkspaceCommands.requestAddPermissionAssignment(); }
    public void requestRemoveRolesPermissionsItem() { administrativeWorkspaceCommands.requestRemoveRolesPermissionsItem(); }
    public void requestValidateRolesPermissions() { administrativeWorkspaceCommands.requestValidateRolesPermissions(); }
    public void requestAddScreen() { administrativeWorkspaceCommands.requestAddScreen(); }
    public void requestAddScreenTransition() { administrativeWorkspaceCommands.requestAddScreenTransition(); }
    public void requestRemoveScreenFlowItem() { administrativeWorkspaceCommands.requestRemoveScreenFlowItem(); }
    public void requestValidateScreenFlow() { administrativeWorkspaceCommands.requestValidateScreenFlow(); }
    public void requestAddWireframeScreen() { administrativeWorkspaceCommands.requestAddWireframeScreen(); }
    public void requestAddWireframeSection() { administrativeWorkspaceCommands.requestAddWireframeSection(); }
    public void requestAddWireframeForm() { administrativeWorkspaceCommands.requestAddWireframeForm(); }
    public void requestAddWireframeTable() { administrativeWorkspaceCommands.requestAddWireframeTable(); }
    public void requestAddWireframeField() { administrativeWorkspaceCommands.requestAddWireframeField(); }
    public void requestAddWireframeButton() { administrativeWorkspaceCommands.requestAddWireframeButton(); }
    public void requestRemoveWireframeItem() { administrativeWorkspaceCommands.requestRemoveWireframeItem(); }
    public void requestValidateWireframe() { administrativeWorkspaceCommands.requestValidateWireframe(); }
    public void requestApplyWireframeTemplate(WireframeScreenTemplateKind templateKind) {
        administrativeWorkspaceCommands.requestApplyWireframeTemplate(templateKind);
    }
    public void synchronizeBehaviorDiagramEdit(DiagramProject updatedProject) {
        behaviorDiagramCommands.synchronizeEdit(updatedProject);
    }
    public void requestAddBpmnStart() { behaviorDiagramCommands.requestAddBpmnStart(); }
    public void requestAddBpmnActivity() { behaviorDiagramCommands.requestAddBpmnActivity(); }
    public void requestAddBpmnDecision() { behaviorDiagramCommands.requestAddBpmnDecision(); }
    public void requestAddBpmnEnd() { behaviorDiagramCommands.requestAddBpmnEnd(); }
    public void requestAddBpmnLane() { behaviorDiagramCommands.requestAddBpmnLane(); }
    public void requestAddUseCaseActor() { behaviorDiagramCommands.requestAddUseCaseActor(); }
    public void requestAddUseCase() { behaviorDiagramCommands.requestAddUseCase(); }
    public void requestAddUseCaseSystem() { behaviorDiagramCommands.requestAddUseCaseSystem(); }
    public void requestAddUmlAction() { behaviorDiagramCommands.requestAddUmlAction(); }
    public void requestAddUmlDecision() { behaviorDiagramCommands.requestAddUmlDecision(); }
    public void requestAddUmlInitialState() { behaviorDiagramCommands.requestAddUmlInitialState(); }
    public void requestAddUmlFinalState() { behaviorDiagramCommands.requestAddUmlFinalState(); }
    public void requestAddSequenceParticipant() { behaviorDiagramCommands.requestAddSequenceParticipant(); }
    public void requestAddSequenceActivation() { behaviorDiagramCommands.requestAddSequenceActivation(); }
    public void requestAddSequenceFragment() { behaviorDiagramCommands.requestAddSequenceFragment(); }
    public void requestAddState() { behaviorDiagramCommands.requestAddState(); }
    public void requestAddBehaviorFlow() { behaviorDiagramCommands.requestAddBehaviorFlow(); }
    public void requestAddBehaviorNote() { behaviorDiagramCommands.requestAddBehaviorNote(); }
    public void requestAddUseCaseAssociation() { behaviorDiagramCommands.requestAddUseCaseAssociation(); }
    public void requestAddUseCaseInclude() { behaviorDiagramCommands.requestAddUseCaseInclude(); }
    public void requestAddUseCaseExtend() { behaviorDiagramCommands.requestAddUseCaseExtend(); }
    public void requestAddUseCaseGeneralization() { behaviorDiagramCommands.requestAddUseCaseGeneralization(); }
    public void requestAddSequenceMessage() { behaviorDiagramCommands.requestAddSequenceMessage(); }
    public void requestAddSequenceReturnMessage() { behaviorDiagramCommands.requestAddSequenceReturnMessage(); }
    public void requestAddStateTransition() { behaviorDiagramCommands.requestAddStateTransition(); }
    public void requestRemoveBehaviorItem() { behaviorDiagramCommands.requestRemoveItem(); }
    public void requestValidateBehaviorDiagram() { behaviorDiagramCommands.requestValidate(); }
    public void synchronizeArchitectureDiagramEdit(DiagramProject updatedProject) {
        architectureDiagramCommands.synchronizeEdit(updatedProject);
    }
    public void requestAddC4Person() { architectureDiagramCommands.requestAddC4Person(); }
    public void requestAddC4System() { architectureDiagramCommands.requestAddC4System(); }
    public void requestAddC4ExternalSystem() { architectureDiagramCommands.requestAddC4ExternalSystem(); }
    public void requestAddC4Boundary() { architectureDiagramCommands.requestAddC4Boundary(); }
    public void requestAddC4Container() { architectureDiagramCommands.requestAddC4Container(); }
    public void requestAddC4Application() { architectureDiagramCommands.requestAddC4Application(); }
    public void requestAddC4Api() { architectureDiagramCommands.requestAddC4Api(); }
    public void requestAddArchitectureDatabase() { architectureDiagramCommands.requestAddArchitectureDatabase(); }
    public void requestAddArchitectureExternalService() { architectureDiagramCommands.requestAddArchitectureExternalService(); }
    public void requestAddDeploymentEnvironment() { architectureDiagramCommands.requestAddDeploymentEnvironment(); }
    public void requestAddDeploymentServer() { architectureDiagramCommands.requestAddDeploymentServer(); }
    public void requestAddDeploymentClient() { architectureDiagramCommands.requestAddDeploymentClient(); }
    public void requestAddDeploymentService() { architectureDiagramCommands.requestAddDeploymentService(); }
    public void requestAddDeploymentNetwork() { architectureDiagramCommands.requestAddDeploymentNetwork(); }
    public void requestAddDeploymentArtifact() { architectureDiagramCommands.requestAddDeploymentArtifact(); }
    public void requestAddArchitectureUses() { architectureDiagramCommands.requestAddArchitectureUses(); }
    public void requestAddArchitectureDependency() { architectureDiagramCommands.requestAddArchitectureDependency(); }
    public void requestAddArchitectureIntegration() { architectureDiagramCommands.requestAddArchitectureIntegration(); }
    public void requestAddArchitectureCall() { architectureDiagramCommands.requestAddArchitectureCall(); }
    public void requestAddArchitectureReadsWrites() { architectureDiagramCommands.requestAddArchitectureReadsWrites(); }
    public void requestAddDeploymentConnection() { architectureDiagramCommands.requestAddDeploymentConnection(); }
    public void requestAddDeploymentHosting() { architectureDiagramCommands.requestAddDeploymentHosting(); }
    public void requestAddDeploymentTarget() { architectureDiagramCommands.requestAddDeploymentTarget(); }
    public void requestRemoveArchitectureItem() { architectureDiagramCommands.requestRemoveItem(); }
    public void requestValidateArchitectureDiagram() { architectureDiagramCommands.requestValidate(); }
    public void synchronizeFreeGraphEdit(DiagramProject updatedProject) {
        freeGraphCommands.synchronizeEdit(updatedProject);
    }
    public void requestActivateFreeGraphSelectTool() { freeGraphCommands.requestActivateSelectTool(); }
    public void requestActivateFreeGraphAddNodeTool() { freeGraphCommands.requestActivateAddNodeTool(); }
    public void requestActivateFreeGraphAddEdgeTool() { freeGraphCommands.requestActivateAddEdgeTool(); }
    public void requestAddFreeGraphNode() { freeGraphCommands.requestAddNode(); }
    public void requestAddFreeGraphEdge() { freeGraphCommands.requestAddEdge(); }
    public void requestRemoveFreeGraphItem() { freeGraphCommands.requestRemoveItem(); }
    public void requestValidateFreeGraph() { freeGraphCommands.requestValidate(); }

    public void requestTransferVisualSelection() {
        visualSelectionTransferCoordinator.requestTransferVisualSelection();
    }

    public void requestExportSvg() { exportCommandHandler.requestExportSvg(); }
    public void requestExportMarkdown() { exportCommandHandler.requestExportMarkdown(); }
    public void requestExportClientBatch() { clientBatchExportCoordinator.requestExportClientBatch(); }
    public void requestExportPng() { exportCommandHandler.requestExportPng(); }
    public void showPlaceholder(String message) { shellState.updateStatus(message); }
}
