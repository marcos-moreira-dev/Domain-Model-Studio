package com.marcosmoreira.domainmodelstudio.presentation.workbench;

import com.marcosmoreira.domainmodelstudio.presentation.sidedock.SideDockContext;
import com.marcosmoreira.domainmodelstudio.presentation.sidedock.SideDockModuleRegistry;
import com.marcosmoreira.domainmodelstudio.presentation.sidedock.WorkbenchSideDock;
import java.util.Objects;
import java.util.Optional;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;

/**
 * Contenedor común para workspaces visuales de diagramas.
 *
 * <p>La vista monta header, centro y un SideDock modular. El registro de módulos,
 * la visibilidad y el montaje del SplitPane quedan delegados para mantener esta
 * clase como coordinadora del workspace activo.</p>
 */
public final class DiagramWorkbenchView {

    private final DiagramWorkbenchContributor contributor;
    private final DiagramWorkbenchDescriptor descriptor;
    private final Parent centerContent;
    private final Optional<Parent> structurePanel;
    private final Optional<Parent> propertiesPanel;
    private final BorderPane root = new BorderPane();
    private final SplitPane splitPane = new SplitPane();
    private final WorkspaceHeaderView headerView;
    private final DiagramWorkbenchSlots slots;
    private final WorkbenchSideDock sideDock = new WorkbenchSideDock();
    private final SideDockModuleRegistry sideDockRegistry = new SideDockModuleRegistry();
    private final SideDockContext sideDockContext;

    public DiagramWorkbenchView(DiagramWorkbenchContributor contributor) {
        this.contributor = Objects.requireNonNull(contributor, "contributor");
        this.descriptor = Objects.requireNonNull(contributor.descriptor(), "descriptor");
        this.centerContent = Objects.requireNonNull(contributor.centerContent(), "centerContent");
        this.structurePanel = Objects.requireNonNull(contributor.structurePanel(), "structurePanel");
        this.propertiesPanel = Objects.requireNonNull(contributor.propertiesPanel(), "propertiesPanel");
        this.headerView = new WorkspaceHeaderView(descriptor.headerState(), this::onHeaderDismissed);
        this.slots = new DiagramWorkbenchSlots(
                descriptor,
                Optional.of(headerView.getRoot()),
                structurePanel,
                centerContent,
                propertiesPanel
        );
        this.sideDockContext = SideDockContext.forWorkspace(descriptor.workspaceKind(), descriptor.title());
        registerSideDockModules();
        build();
    }

    public Parent getRoot() {
        return root;
    }

    public DiagramWorkbenchSlots slots() {
        return slots;
    }

    public void activate() {
        refreshSideDockForActiveContext();
        contributor.onActivated();
    }

    public void deactivate() {
        contributor.onDeactivated();
    }

    public void setPanelVisible(WorkbenchPanelSlot slot, boolean visible) {
        WorkbenchSideDockVisibility.setVisible(
                sideDock,
                WorkbenchSideDockModules.visualModuleId(slot),
                visible
        );
    }

    private void build() {
        root.getStyleClass().add("diagram-workbench");
        splitPane.setOrientation(Orientation.HORIZONTAL);
        splitPane.getStyleClass().add("diagram-workbench-split-pane");
        root.setTop(headerView.getRoot());
        refreshCenter();
    }

    private void registerSideDockModules() {
        WorkbenchSideDockModules.registerVisualModules(
                sideDockRegistry,
                descriptor,
                structurePanel,
                propertiesPanel,
                contributor.additionalSideDockModules()
        );
        // No se registran módulos de Vista/Ayuda como placeholder.
        // El SideDock solo muestra módulos con contenido operativo real del workspace activo.
    }

    private void refreshCenter() {
        if (!descriptor.panelPolicy().renderPanelsInsideWorkbench()) {
            root.setCenter(centerContent);
            return;
        }
        refreshSideDockForActiveContext();
        WorkbenchSideDockLayout.mount(root, splitPane, sideDock, centerContent, 0.24);
    }

    private void refreshSideDockForActiveContext() {
        sideDock.refresh(sideDockContext, sideDockRegistry.modulesFor(sideDockContext));
    }

    private void onHeaderDismissed() {
        root.getStyleClass().add("diagram-workbench-header-hidden");
    }
}
