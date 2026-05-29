package com.marcosmoreira.domainmodelstudio.presentation.workbench;

import com.marcosmoreira.domainmodelstudio.presentation.sidedock.SideDockContext;
import com.marcosmoreira.domainmodelstudio.presentation.sidedock.SideDockModuleRegistry;
import com.marcosmoreira.domainmodelstudio.presentation.sidedock.WorkbenchSideDock;
import com.marcosmoreira.domainmodelstudio.presentation.sidedock.SideDockModule;
import com.marcosmoreira.domainmodelstudio.presentation.sidedock.SideDockModuleId;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;

/** Workbench común para documentos y matrices con SideDock modular contextual. */
public final class StructuredWorkbenchView {

    private final StructuredWorkbenchDescriptor descriptor;
    private final Parent centerContent;
    private final Optional<Parent> structurePanel;
    private final Optional<Parent> propertiesPanel;
    private final BorderPane root = new BorderPane();
    private final SplitPane splitPane = new SplitPane();
    private final WorkspaceHeaderView headerView;
    private final WorkbenchSideDock sideDock = new WorkbenchSideDock();
    private final SideDockModuleRegistry sideDockRegistry = new SideDockModuleRegistry();
    private final SideDockContext sideDockContext;
    private final List<SideDockModule> additionalModules;

    public StructuredWorkbenchView(
            StructuredWorkbenchDescriptor descriptor,
            Parent structurePanel,
            Parent centerContent,
            Parent propertiesPanel
    ) {
        this(descriptor, structurePanel, centerContent, propertiesPanel, List.of());
    }

    public StructuredWorkbenchView(
            StructuredWorkbenchDescriptor descriptor,
            Parent structurePanel,
            Parent centerContent,
            Parent propertiesPanel,
            List<SideDockModule> additionalModules
    ) {
        this.descriptor = Objects.requireNonNull(descriptor, "descriptor");
        this.centerContent = Objects.requireNonNull(centerContent, "centerContent");
        this.structurePanel = Optional.ofNullable(structurePanel);
        this.propertiesPanel = Optional.ofNullable(propertiesPanel);
        this.additionalModules = List.copyOf(additionalModules == null ? List.of() : additionalModules);
        this.headerView = new WorkspaceHeaderView(descriptor.headerState(), this::onHeaderDismissed);
        this.sideDockContext = SideDockContext.forWorkspace(descriptor.workspaceKind(), descriptor.headerState().title());
        registerSideDockModules();
        build();
    }

    public Parent getRoot() {
        return root;
    }

    public void activateSideDockModule(SideDockModuleId moduleId) {
        sideDock.activate(moduleId);
    }

    public void setPanelVisible(WorkbenchPanelSlot slot, boolean visible) {
        WorkbenchSideDockVisibility.setVisible(
                sideDock,
                WorkbenchSideDockModules.structuredModuleId(sideDockContext, slot),
                visible
        );
    }

    private void build() {
        root.getStyleClass().add("structured-workbench");
        if (!descriptor.rootStyleClass().isBlank()) {
            root.getStyleClass().add(descriptor.rootStyleClass());
        }
        splitPane.setOrientation(Orientation.HORIZONTAL);
        splitPane.getStyleClass().addAll("diagram-workbench-split-pane", "structured-workbench-split-pane");
        root.setTop(headerView.getRoot());
        refreshCenter();
    }

    private void registerSideDockModules() {
        WorkbenchSideDockModules.registerStructuredModules(
                sideDockRegistry,
                descriptor,
                sideDockContext,
                structurePanel,
                propertiesPanel,
                additionalModules
        );
        // El SideDock estructurado no agrega Ayuda/Vista previa/Matriz como placeholder.
    }

    private void refreshCenter() {
        sideDock.refresh(sideDockContext, sideDockRegistry.modulesFor(sideDockContext));
        WorkbenchSideDockLayout.mount(
                root,
                splitPane,
                sideDock,
                centerContent,
                sideDockContext.matrixLike() ? 0.28 : 0.25
        );
    }

    private void onHeaderDismissed() {
        root.getStyleClass().add("structured-workbench-header-hidden");
    }
}
