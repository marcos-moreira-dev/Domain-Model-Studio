package com.marcosmoreira.domainmodelstudio.presentation.shell;

import com.marcosmoreira.domainmodelstudio.presentation.canvas.DiagramCanvasViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.datadictionary.DataDictionaryViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.modulemap.ModuleMapViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.umlclass.UmlClassDiagramViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.rolespermissions.RolesPermissionsViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.screenflow.ScreenFlowViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.wireframe.WireframeViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.behavior.BehaviorDiagramViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.architecture.ArchitectureDiagramViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.freegraph.FreeGraphViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness.LogicalBusinessViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.logicalbusinessgraph.LogicalBusinessGraphViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.inspector.InspectorViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.sidebar.ModelTreeViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.statusbar.StatusBarViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.toolbar.MainToolbarViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.placeholder.PlaceholderWorkspaceViewModel;
import java.util.Objects;
import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.stage.WindowEvent;
import javafx.collections.ObservableList;

/** Estado de alto nivel de la ventana principal. */
public final class MainShellViewModel {

    private final MainShellState shellState;
    private final MainShellCommandHandler commandHandler;
    private final MainToolbarViewModel toolbarViewModel;
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
    private final StatusBarViewModel statusBarViewModel;

    public MainShellViewModel(
            MainShellState shellState,
            MainShellCommandHandler commandHandler,
            MainToolbarViewModel toolbarViewModel,
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
            LogicalBusinessGraphViewModel logicalBusinessGraphViewModel,
            StatusBarViewModel statusBarViewModel
    ) {
        this.shellState = Objects.requireNonNull(shellState, "shellState");
        this.commandHandler = Objects.requireNonNull(commandHandler, "commandHandler");
        this.toolbarViewModel = Objects.requireNonNull(toolbarViewModel, "toolbarViewModel");
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
        this.statusBarViewModel = Objects.requireNonNull(statusBarViewModel, "statusBarViewModel");
    }

    public StringProperty windowTitleProperty() {
        return shellState.windowTitleProperty();
    }

    public BooleanProperty projectOpenProperty() {
        return shellState.projectOpenProperty();
    }

    public BooleanExpression projectClosed() {
        return shellState.projectOpenProperty().not();
    }

    public BooleanExpression saveableProjectClosed() {
        return shellState.saveableProjectOpenProperty().not();
    }

    public ObjectProperty<NotationType> activeNotationProperty() {
        return shellState.activeNotationProperty();
    }

    public ObjectProperty<PlaceholderWorkspaceViewModel> placeholderWorkspaceProperty() {
        return shellState.placeholderWorkspaceProperty();
    }


    public ObservableList<EditorTabViewState> editorTabs() {
        return shellState.editorTabs();
    }

    public StringProperty activeEditorTabIdProperty() {
        return shellState.activeEditorTabIdProperty();
    }

    public void activateEditorTab(String tabId) {
        commandHandler.activateEditorTab(tabId);
    }

    public void closeEditorTab(String tabId) {
        commandHandler.closeEditorTab(tabId);
    }

    public void reorderEditorTabAfter(String movedTabId, String targetTabId) {
        commandHandler.reorderEditorTabAfter(movedTabId, targetTabId);
    }

    public MainToolbarViewModel toolbarViewModel() {
        return toolbarViewModel;
    }

    public ModelTreeViewModel modelTreeViewModel() {
        return modelTreeViewModel;
    }

    public DiagramCanvasViewModel canvasViewModel() {
        return canvasViewModel;
    }

    public InspectorViewModel inspectorViewModel() {
        return inspectorViewModel;
    }

    public DataDictionaryViewModel dataDictionaryViewModel() {
        return dataDictionaryViewModel;
    }

    public ModuleMapViewModel moduleMapViewModel() {
        return moduleMapViewModel;
    }

    public UmlClassDiagramViewModel umlClassDiagramViewModel() {
        return umlClassDiagramViewModel;
    }

    public RolesPermissionsViewModel rolesPermissionsViewModel() {
        return rolesPermissionsViewModel;
    }

    public ScreenFlowViewModel screenFlowViewModel() {
        return screenFlowViewModel;
    }

    public WireframeViewModel wireframeViewModel() {
        return wireframeViewModel;
    }

    public BehaviorDiagramViewModel behaviorDiagramViewModel() {
        return behaviorDiagramViewModel;
    }

    public ArchitectureDiagramViewModel architectureDiagramViewModel() {
        return architectureDiagramViewModel;
    }

    public FreeGraphViewModel freeGraphViewModel() {
        return freeGraphViewModel;
    }

    public LogicalBusinessViewModel logicalBusinessViewModel() {
        return logicalBusinessViewModel;
    }

    public LogicalBusinessGraphViewModel logicalBusinessGraphViewModel() {
        return logicalBusinessGraphViewModel;
    }

    public StatusBarViewModel statusBarViewModel() {
        return statusBarViewModel;
    }

    public void updateStatus(String message) {
        shellState.updateStatus(message);
    }

    public void newProject() {
        commandHandler.requestNewProject();
    }

    public void openProject() {
        commandHandler.requestOpenProject();
    }

    public void saveProject() {
        commandHandler.requestSaveProject();
    }

    public void saveProjectAs() {
        commandHandler.requestSaveProjectAs();
    }

    public void closeProject() {
        commandHandler.requestCloseProject();
    }

    public void exitApplication() {
        commandHandler.requestExitApplication();
    }

    public void handleWindowClose(WindowEvent event) {
        if (!commandHandler.canCloseApplication()) {
            event.consume();
        }
    }


    public void undo() {
        commandHandler.requestUndo();
    }

    public void redo() {
        commandHandler.requestRedo();
    }

    public void importMarkdown() {
        commandHandler.requestImportMarkdown();
    }

    public void importMarkdownFolder() {
        commandHandler.requestImportMarkdownFolder();
    }

    public void importUmlClassFromSourceCode() {
        commandHandler.requestImportUmlClassFromSourceCode();
    }

    public void openExampleProject() {
        commandHandler.requestOpenExampleProject();
    }

    public void exportAiResources() {
        commandHandler.requestExportAiResources();
    }

    public void exportSvg() {
        commandHandler.requestExportSvg();
    }

    public void exportMarkdown() {
        commandHandler.requestExportMarkdown();
    }

    public void exportPng() {
        commandHandler.requestExportPng();
    }

    public void exportDataDictionaryPdf() {
        commandHandler.requestExportDataDictionaryPdf();
    }

    public void exportClientBatch() {
        commandHandler.requestExportClientBatch();
    }

    public void switchToChen() {
        commandHandler.requestSwitchNotation(NotationType.CHEN);
    }

    public void switchToCrowsFoot() {
        commandHandler.requestSwitchNotation(NotationType.CROWS_FOOT);
    }

    public void switchToNotation(NotationType notationType) {
        commandHandler.requestSwitchNotation(notationType);
    }

    public void validateProject() {
        commandHandler.requestValidateProject();
    }

    public void regenerateLayout() {
        commandHandler.requestRegenerateLayout();
    }

    public void zoomIn() {
        commandHandler.requestZoomIn();
    }

    public void zoomOut() {
        commandHandler.requestZoomOut();
    }

    public void resetZoom() {
        commandHandler.requestResetZoom();
    }

    public void fitToContent() {
        commandHandler.requestFitToContent();
    }

    public void centerDiagram() {
        commandHandler.requestCenterDiagram();
    }

    public void centerSelection() {
        commandHandler.requestCenterSelection();
    }

    public void selectAllElements() {
        commandHandler.requestSelectAllElements();
    }

    public void clearSelection() {
        commandHandler.requestClearSelection();
    }
}
