package com.marcosmoreira.domainmodelstudio.presentation.shell;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.presentation.conceptual.ConceptualEditorView;
import com.marcosmoreira.domainmodelstudio.presentation.canvas.WelcomeWorkspaceView;
import com.marcosmoreira.domainmodelstudio.presentation.datadictionary.DataDictionaryEditorView;
import com.marcosmoreira.domainmodelstudio.presentation.dialogs.AboutDialog;
import com.marcosmoreira.domainmodelstudio.presentation.dialogs.ManualDialog;
import com.marcosmoreira.domainmodelstudio.presentation.modulemap.ModuleMapEditorView;
import com.marcosmoreira.domainmodelstudio.presentation.umlclass.UmlClassDiagramEditorView;
import com.marcosmoreira.domainmodelstudio.presentation.umlclass.UmlClassCodeEditorSettingsDialog;
import com.marcosmoreira.domainmodelstudio.presentation.rolespermissions.RolesPermissionsEditorView;
import com.marcosmoreira.domainmodelstudio.presentation.screenflow.ScreenFlowEditorView;
import com.marcosmoreira.domainmodelstudio.presentation.wireframe.WireframeEditorView;
import com.marcosmoreira.domainmodelstudio.presentation.architecture.ArchitectureDiagramEditorView;
import com.marcosmoreira.domainmodelstudio.presentation.behavior.BehaviorDiagramEditorView;
import com.marcosmoreira.domainmodelstudio.presentation.freegraph.FreeGraphEditorView;
import com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness.LogicalBusinessEditorView;
import com.marcosmoreira.domainmodelstudio.presentation.logicalbusinessgraph.LogicalBusinessGraphEditorView;
import com.marcosmoreira.domainmodelstudio.presentation.statusbar.StatusBarView;
import com.marcosmoreira.domainmodelstudio.presentation.placeholder.PlaceholderWorkspaceView;
import com.marcosmoreira.domainmodelstudio.presentation.exportable.ExportFormat;
import com.marcosmoreira.domainmodelstudio.presentation.toolbar.MainToolbarView;
import com.marcosmoreira.domainmodelstudio.presentation.workspace.DefaultWorkspaceDescriptorCatalog;
import com.marcosmoreira.domainmodelstudio.presentation.workspace.WorkspaceDescriptor;
import com.marcosmoreira.domainmodelstudio.presentation.workspace.WorkspaceKind;
import com.marcosmoreira.domainmodelstudio.presentation.workspace.WorkspaceRoute;
import com.marcosmoreira.domainmodelstudio.presentation.workspace.WorkspaceRouteResolver;
import com.marcosmoreira.domainmodelstudio.presentation.workspace.WorkspaceViewRegistry;
import com.marcosmoreira.domainmodelstudio.presentation.shell.tabs.ScrollableEditorTabBarView;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanExpression;
import javafx.scene.Parent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToggleGroup;
import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * Vista principal de la aplicación JavaFX.
 *
 * <p>Compone menú superior, toolbar global/contextual, barra de pestañas, área de trabajo y
 * barra de estado. La vista no decide reglas de negocio: delega comandos al handler y monta
 * workspaces usando el registro/ruteo de presentación.</p>
 *
 * <p>Esta clase es el punto de entrada para estudiar la carcasa visual, pero no debe usarse como
 * lugar para agregar semántica de un tipo de proyecto. Los workspaces especializados se integran
 * mediante registros, coordinadores y ViewModels propios.</p>
 */
public final class MainShellView {

    private final MainShellViewModel viewModel;
    private final BorderPane root = new BorderPane();
    private final WorkspaceRouteResolver workspaceRouteResolver = new WorkspaceRouteResolver();
    private final DefaultWorkspaceDescriptorCatalog workspaceDescriptorCatalog = new DefaultWorkspaceDescriptorCatalog();
    private WorkspaceViewRegistry workspaceViewRegistry;
    private BorderPane workAreaFrame;
    private Parent welcomeRoot;
    private Parent canvasRoot;
    private Parent dataDictionaryRoot;
    private Parent moduleMapRoot;
    private Parent umlClassRoot;
    private Parent rolesPermissionsRoot;
    private Parent screenFlowRoot;
    private Parent wireframeRoot;
    private Parent architectureRoot;
    private Parent behaviorRoot;
    private Parent freeGraphRoot;
    private Parent logicalBusinessRoot;
    private Parent logicalBusinessGraphRoot;
    private Parent placeholderRoot;
    private PlaceholderWorkspaceView placeholderWorkspaceView;
    private RadioMenuItem chenNotationMenuItem;
    private RadioMenuItem crowsFootNotationMenuItem;
    private ScrollableEditorTabBarView editorTabBar;

    public MainShellView(MainShellViewModel viewModel) {
        this.viewModel = viewModel;
        build();
    }

    public Parent getRoot() {
        return root;
    }

    private void build() {
        root.getStyleClass().add("app-root");
        root.setTop(buildTopArea());
        root.setCenter(buildWorkArea());
        root.setBottom(new StatusBarView(viewModel.statusBarViewModel()).getRoot());
        installProjectOpenPanelBehavior();
        installPlaceholderWorkspaceBehavior();
        installActiveDiagramWorkspaceBehavior();
        installActiveEditorTabWorkspaceBehavior();
        installSelectionShortcuts();
    }

    private VBox buildTopArea() {
        VBox topArea = new VBox();
        topArea.getChildren().addAll(buildMenuBar(), new MainToolbarView(viewModel.toolbarViewModel()).getRoot());
        return topArea;
    }

    private void installProjectOpenPanelBehavior() {
        viewModel.projectOpenProperty().addListener((observable, previous, current) -> {
            refreshWorkAreaPanels();
            refreshEditorTabs();
        });
    }

    private void installPlaceholderWorkspaceBehavior() {
        viewModel.placeholderWorkspaceProperty().addListener((observable, previous, current) -> {
            if (placeholderWorkspaceView != null) {
                if (current == null) {
                    placeholderWorkspaceView.clear();
                } else {
                    placeholderWorkspaceView.show(current);
                }
            }
            refreshWorkAreaPanels();
        });
    }

    private void installActiveDiagramWorkspaceBehavior() {
        viewModel.toolbarViewModel().activeDiagramTypeProperty().addListener((observable, previous, current) -> refreshWorkAreaPanels());
    }

    private void installActiveEditorTabWorkspaceBehavior() {
        viewModel.activeEditorTabIdProperty().addListener((observable, previous, current) -> refreshWorkAreaPanels());
    }

    private boolean placeholderWorkspaceActive() {
        return viewModel.placeholderWorkspaceProperty().get() != null;
    }

    private WorkspaceRoute activeWorkspaceRoute() {
        return workspaceRouteResolver.resolve(
                viewModel.toolbarViewModel().activeDiagramTypeProperty().get(),
                placeholderWorkspaceActive(),
                homeTabActive());
    }

    private boolean homeTabActive() {
        return MainShellState.HOME_TAB_ID.equals(viewModel.activeEditorTabIdProperty().get());
    }

    private void installSelectionShortcuts() {
        root.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                viewModel.clearSelection();
                event.consume();
            }
        });
    }

    private MenuBar buildMenuBar() {
        MenuBar menuBar = new MenuBar();
        menuBar.getStyleClass().add("app-menu-bar");
        menuBar.getMenus().addAll(
                buildFileMenu(),
                buildEditMenu(),
                buildModelMenu(),
                buildViewMenu(),
                buildExportMenu(),
                buildSettingsMenu(),
                buildHelpMenu()
        );
        return menuBar;
    }

    private Menu buildFileMenu() {
        Menu menu = new Menu("Archivo");
        menu.getItems().addAll(
                commandItem("Nuevo proyecto", viewModel::newProject, "Shortcut+N", null),
                commandItem("Abrir proyecto...", viewModel::openProject, "Shortcut+O", null),
                commandItem("Abrir ejemplo oficial...", viewModel::openExampleProject, null, null),
                new SeparatorMenuItem(),
                commandItem("Guardar proyecto", viewModel::saveProject, "Shortcut+S", viewModel.saveableProjectClosed()),
                commandItem("Guardar proyecto como...", viewModel::saveProjectAs, "Shortcut+Shift+S", viewModel.saveableProjectClosed()),
                commandItem("Cerrar proyecto", viewModel::closeProject, "Shortcut+W", viewModel.saveableProjectClosed()),
                commandItem("Importar modelo Markdown...", viewModel::importMarkdown, "Shortcut+I", null),
                commandItem("Abrir carpeta Markdown...", viewModel::importMarkdownFolder, null, null),
                commandItem("Importar UML desde código fuente...", viewModel::importUmlClassFromSourceCode, null, null),
                commandItem("Exportar recursos IA...", viewModel::exportAiResources, null, null),
                new SeparatorMenuItem(),
                commandItem("Salir", viewModel::exitApplication, null, null));
        return menu;
    }

    private Menu buildEditMenu() {
        Menu menu = new Menu("Editar");
        BooleanExpression conceptualOnly = conceptualCanvasActionUnavailable();
        menu.getItems().addAll(
                commandItem("Deshacer cambio", viewModel::undo, "Shortcut+Z", viewModel.saveableProjectClosed()),
                commandItem("Rehacer cambio", viewModel::redo, "Shortcut+Y", viewModel.saveableProjectClosed()),
                new SeparatorMenuItem(),
                commandItem("Seleccionar todo", viewModel::selectAllElements, "Shortcut+A", conceptualOnly),
                commandItem("Limpiar selección", viewModel::clearSelection, null, conceptualOnly));
        return menu;
    }

    private Menu buildModelMenu() {
        Menu menu = new Menu("Diagrama");
        ToggleGroup notationGroup = new ToggleGroup();

        chenNotationMenuItem = notationItem("Chen", NotationType.CHEN, notationGroup);
        crowsFootNotationMenuItem = notationItem("Pata de gallo", NotationType.CROWS_FOOT, notationGroup);
        refreshNotationMenuItems(viewModel.activeNotationProperty().get());
        viewModel.activeNotationProperty().addListener((observable, previous, current) -> refreshNotationMenuItems(current));

        menu.getItems().addAll(
                commandItem("Validar proyecto", viewModel::validateProject, null, viewModel.projectClosed()),
                commandItem("Reorganizar diagrama", viewModel::regenerateLayout, null, autoLayoutUnavailable()),
                new SeparatorMenuItem(),
                chenNotationMenuItem,
                crowsFootNotationMenuItem);
        return menu;
    }

    private RadioMenuItem notationItem(String text, NotationType notation, ToggleGroup group) {
        RadioMenuItem item = new RadioMenuItem(text);
        item.setToggleGroup(group);
        item.disableProperty().bind(conceptualCanvasActionUnavailable());
        item.setOnAction(event -> viewModel.switchToNotation(notation));
        return item;
    }

    private void refreshNotationMenuItems(NotationType activeNotation) {
        if (chenNotationMenuItem != null) {
            chenNotationMenuItem.setSelected(activeNotation == NotationType.CHEN);
        }
        if (crowsFootNotationMenuItem != null) {
            crowsFootNotationMenuItem.setSelected(activeNotation == NotationType.CROWS_FOOT);
        }
    }

    private Menu buildViewMenu() {
        Menu menu = new Menu("Vista");

        BooleanExpression conceptualOnly = conceptualCanvasActionUnavailable();
        menu.getItems().addAll(
                commandItem("Pantalla completa", () -> ShellFullScreenAction.toggle(root, viewModel::updateStatus), "F11", null),
                new SeparatorMenuItem(),
                commandItem("Acercar", viewModel::zoomIn, null, conceptualOnly),
                commandItem("Alejar", viewModel::zoomOut, null, conceptualOnly),
                commandItem("Tamaño real (100%)", viewModel::resetZoom, null, conceptualOnly),
                commandItem("Ajustar al contenido", viewModel::fitToContent, null, visualNavigationUnavailable()),
                commandItem("Centrar diagrama", viewModel::centerDiagram, null, visualNavigationUnavailable()),
                commandItem("Centrar selección", viewModel::centerSelection, null, conceptualOnly));
        return menu;
    }


    private Menu buildExportMenu() {
        Menu menu = new Menu("Exportar");
        menu.getItems().addAll(
                commandItem(com.marcosmoreira.domainmodelstudio.presentation.exportable.SvgExportContract.MENU_LABEL, viewModel::exportSvg, null, exportUnavailable(ExportFormat.SVG)),
                commandItem("Exportar como PNG...", viewModel::exportPng, null, exportUnavailable(ExportFormat.PNG)),
                commandItem("Exportar Markdown actualizado...", viewModel::exportMarkdown, null, exportUnavailable(ExportFormat.MARKDOWN)),
                commandItem("Exportar documento PDF...", viewModel::exportPdf, null, exportUnavailable(ExportFormat.PDF)),
                new SeparatorMenuItem(),
                commandItem("Exportar proyectos abiertos...", viewModel::exportClientBatch, null, null));
        return menu;
    }

    private Menu buildSettingsMenu() {
        Menu menu = new Menu("Configuración");
        menu.getItems().add(commandItem(
                "Editor de código...",
                () -> UmlClassCodeEditorSettingsDialog.show(viewModel.umlClassDiagramViewModel()),
                null,
                null));
        return menu;
    }

    private BooleanExpression conceptualCanvasActionUnavailable() {
        return viewModel.projectClosed().or(activeTypeUnsupported(DiagramTypeId.CONCEPTUAL_MODEL));
    }

    private BooleanExpression visualNavigationUnavailable() {
        return viewModel.projectClosed().or(Bindings.createBooleanBinding(
                () -> !DiagramWorkspaceActionPolicy.supportsVisualNavigation(activeDiagramType()),
                viewModel.toolbarViewModel().activeDiagramTypeProperty()));
    }

    private BooleanExpression autoLayoutUnavailable() {
        return viewModel.projectClosed().or(Bindings.createBooleanBinding(
                () -> !DiagramWorkspaceActionPolicy.supportsAutoLayout(activeDiagramType()),
                viewModel.toolbarViewModel().activeDiagramTypeProperty()));
    }

    private BooleanExpression exportUnavailable(ExportFormat format) {
        return viewModel.projectClosed().or(Bindings.createBooleanBinding(
                () -> !viewModel.toolbarViewModel().activeOutputSupports(format),
                viewModel.toolbarViewModel().activeExportFormatsProperty()));
    }

    private BooleanExpression activeTypeUnsupported(DiagramTypeId expectedType) {
        return Bindings.createBooleanBinding(
                () -> !expectedType.equals(activeDiagramType()),
                viewModel.toolbarViewModel().activeDiagramTypeProperty());
    }

    private DiagramTypeId activeDiagramType() {
        return viewModel.toolbarViewModel().activeDiagramTypeProperty().get();
    }

    private Menu buildHelpMenu() {
        Menu menu = new Menu("Ayuda");

        MenuItem manual = new MenuItem("Guía académica");
        manual.setAccelerator(KeyCombination.keyCombination("F1"));
        manual.setOnAction(event -> {
            ManualDialog.show();
            viewModel.updateStatus("Guía académica abierta.");
        });

        MenuItem about = new MenuItem("Acerca de Domain Model Studio");
        about.setOnAction(event -> AboutDialog.show());
        menu.getItems().addAll(manual, new SeparatorMenuItem(), about);
        return menu;
    }

    private MenuItem commandItem(String text, Runnable command, String accelerator, BooleanExpression disabledWhen) {
        MenuItem item = new MenuItem(text);
        item.setOnAction(event -> command.run());
        if (accelerator != null && !accelerator.isBlank()) {
            item.setAccelerator(KeyCombination.keyCombination(accelerator));
        }
        if (disabledWhen != null) {
            item.disableProperty().bind(disabledWhen);
        }
        return item;
    }

    private BorderPane buildWorkArea() {
        WelcomeWorkspaceView welcomeWorkspace = new WelcomeWorkspaceView(viewModel.canvasViewModel());
        ConceptualEditorView conceptualEditor = new ConceptualEditorView(
                viewModel.modelTreeViewModel(),
                viewModel.canvasViewModel(),
                viewModel.inspectorViewModel(),
                viewModel::switchToNotation
        );
        DataDictionaryEditorView dataDictionaryEditor = new DataDictionaryEditorView(viewModel.dataDictionaryViewModel());
        ModuleMapEditorView moduleMapEditor = new ModuleMapEditorView(viewModel.moduleMapViewModel());
        UmlClassDiagramEditorView umlClassEditor = new UmlClassDiagramEditorView(viewModel.umlClassDiagramViewModel());
        RolesPermissionsEditorView rolesPermissionsEditor = new RolesPermissionsEditorView(viewModel.rolesPermissionsViewModel());
        ScreenFlowEditorView screenFlowEditor = new ScreenFlowEditorView(viewModel.screenFlowViewModel());
        WireframeEditorView wireframeEditor = new WireframeEditorView(viewModel.wireframeViewModel());
        BehaviorDiagramEditorView behaviorEditor = new BehaviorDiagramEditorView(viewModel.behaviorDiagramViewModel());
        ArchitectureDiagramEditorView architectureEditor = new ArchitectureDiagramEditorView(viewModel.architectureDiagramViewModel());
        FreeGraphEditorView freeGraphEditor = new FreeGraphEditorView(viewModel.freeGraphViewModel());
        LogicalBusinessEditorView logicalBusinessEditor = new LogicalBusinessEditorView(
                viewModel.logicalBusinessViewModel(),
                viewModel::exportPdf,
                viewModel::exportMarkdown);
        LogicalBusinessGraphEditorView logicalBusinessGraphEditor = new LogicalBusinessGraphEditorView(viewModel.logicalBusinessGraphViewModel());
        placeholderWorkspaceView = new PlaceholderWorkspaceView();

        welcomeRoot = welcomeWorkspace.build();
        canvasRoot = conceptualEditor.getRoot();
        dataDictionaryRoot = dataDictionaryEditor.getRoot();
        moduleMapRoot = moduleMapEditor.getRoot();
        umlClassRoot = umlClassEditor.getRoot();
        rolesPermissionsRoot = rolesPermissionsEditor.getRoot();
        screenFlowRoot = screenFlowEditor.getRoot();
        wireframeRoot = wireframeEditor.getRoot();
        behaviorRoot = behaviorEditor.getRoot();
        architectureRoot = architectureEditor.getRoot();
        freeGraphRoot = freeGraphEditor.getRoot();
        logicalBusinessRoot = logicalBusinessEditor.getRoot();
        logicalBusinessGraphRoot = logicalBusinessGraphEditor.getRoot();
        placeholderRoot = placeholderWorkspaceView.getRoot();

        workspaceViewRegistry = new WorkspaceViewRegistry()
                .register(workspaceDescriptor(WorkspaceKind.WELCOME_HOME), welcomeRoot)
                .register(workspaceDescriptor(WorkspaceKind.CONCEPTUAL_CANVAS), canvasRoot)
                .register(workspaceDescriptor(WorkspaceKind.DATA_DICTIONARY_DOCUMENT), dataDictionaryRoot)
                .register(workspaceDescriptor(WorkspaceKind.MODULE_MAP_DIAGRAM), moduleMapRoot)
                .register(workspaceDescriptor(WorkspaceKind.UML_CLASS_DIAGRAM), umlClassRoot)
                .register(workspaceDescriptor(WorkspaceKind.ROLES_PERMISSIONS_MATRIX), rolesPermissionsRoot)
                .register(workspaceDescriptor(WorkspaceKind.SCREEN_FLOW_DIAGRAM), screenFlowRoot)
                .register(workspaceDescriptor(WorkspaceKind.WIREFRAME_DIAGRAM), wireframeRoot)
                .register(workspaceDescriptor(WorkspaceKind.BEHAVIOR_DIAGRAM), behaviorRoot)
                .register(workspaceDescriptor(WorkspaceKind.ARCHITECTURE_DIAGRAM), architectureRoot)
                .register(workspaceDescriptor(WorkspaceKind.FREE_GRAPH_DIAGRAM), freeGraphRoot)
                .register(workspaceDescriptor(WorkspaceKind.LOGICAL_BUSINESS_DOCUMENT), logicalBusinessRoot)
                .register(workspaceDescriptor(WorkspaceKind.LOGICAL_BUSINESS_GRAPH_DIAGRAM), logicalBusinessGraphRoot)
                .register(workspaceDescriptor(WorkspaceKind.PLACEHOLDER_GUIDE), placeholderRoot);

        workAreaFrame = new BorderPane();
        workAreaFrame.getStyleClass().add("work-area-frame");
        workAreaFrame.setTop(buildEditorTabBar());

        refreshWorkAreaPanels();
        return workAreaFrame;
    }

    private WorkspaceDescriptor workspaceDescriptor(WorkspaceKind kind) {
        return workspaceDescriptorCatalog.descriptorFor(kind);
    }

    private Parent buildEditorTabBar() {
        editorTabBar = new ScrollableEditorTabBarView(
                viewModel.editorTabs(),
                viewModel.activeEditorTabIdProperty(),
                viewModel::activateEditorTab,
                viewModel::closeEditorTab,
                viewModel::reorderEditorTabAfter
        );
        return editorTabBar.getRoot();
    }

    private void refreshEditorTabs() {
        if (editorTabBar != null) {
            editorTabBar.refresh();
        }
    }
    private void refreshWorkAreaPanels() {
        if (workAreaFrame == null || canvasRoot == null) {
            return;
        }
        WorkspaceRoute route = activeWorkspaceRoute();
        Parent centerRoot = workspaceViewRegistry == null
                ? canvasRoot
                : workspaceViewRegistry.rootForOrFallback(route, canvasRoot);
        workAreaFrame.setCenter(centerRoot);
    }
}
