package com.marcosmoreira.domainmodelstudio.presentation.architecture;

import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureEdge;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureNode;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasSelectionSynchronizationGuard;
import com.marcosmoreira.domainmodelstudio.presentation.sidedock.SideDockCollectionSizingPolicy;
import java.util.Objects;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import com.marcosmoreira.domainmodelstudio.presentation.workbench.WorkbenchPanelSupport;

/** Panel izquierdo de C4/despliegue montado como slot común. */
final class ArchitectureStructurePanel {

    private final ArchitectureDiagramViewModel viewModel;
    private final Runnable refreshCanvas;
    private final VBox root = new VBox(10);
    private final CanvasSelectionSynchronizationGuard selectionSyncGuard = new CanvasSelectionSynchronizationGuard();
    private final ListView<ArchitectureNode> nodeList = new ListView<>();
    private final ListView<ArchitectureEdge> edgeList = new ListView<>();

    ArchitectureStructurePanel(ArchitectureDiagramViewModel viewModel, Runnable refreshCanvas) {
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
        this.refreshCanvas = refreshCanvas == null ? () -> { } : refreshCanvas;
        build();
        bindViewModel();
    }

    Parent root() {
        return root;
    }

    private void build() {
        root.getStyleClass().addAll("architecture-structure-panel", "diagram-workbench-panel-content");
        root.setPadding(new Insets(10));
        Label help = new Label("Selecciona un elemento de arquitectura; puedes moverlo en el lienzo.");
        help.getStyleClass().add("architecture-panel-help");
        help.setWrapText(true);
        nodeList.setItems(viewModel.nodes());
        nodeList.getStyleClass().add("architecture-node-list");
        nodeList.setCellFactory(ignored -> new NodeCell());
        SideDockCollectionSizingPolicy.configureListView(nodeList);
        edgeList.setItems(viewModel.edges());
        edgeList.getStyleClass().add("architecture-edge-list");
        edgeList.setCellFactory(ignored -> new EdgeCell(viewModel));
        SideDockCollectionSizingPolicy.configureListView(edgeList);
        TabPane tabs = new TabPane();
        tabs.getStyleClass().add("architecture-structure-tabs");
        tabs.getTabs().add(tab("Elementos", nodeList));
        tabs.getTabs().add(tab("Relaciones", edgeList));
        VBox.setVgrow(tabs, Priority.ALWAYS);
        root.getChildren().addAll(help, WorkbenchPanelSupport.emptySelectionCard("un nodo o una relación"), tabs);
    }

    private void bindViewModel() {
        nodeList.getSelectionModel().selectedItemProperty().addListener((observable, previous, current) -> {
            if (selectionSyncGuard.active() || current == null) {
                return;
            }
            viewModel.selectNodeById(current.id());
            selectionSyncGuard.runGuarded(() -> edgeList.getSelectionModel().clearSelection());
            refreshCanvas.run();
        });
        edgeList.getSelectionModel().selectedItemProperty().addListener((observable, previous, current) -> {
            if (selectionSyncGuard.active() || current == null) {
                return;
            }
            viewModel.selectEdgeById(current.id());
            selectionSyncGuard.runGuarded(() -> nodeList.getSelectionModel().clearSelection());
            refreshCanvas.run();
        });
        viewModel.selectedNodeProperty().addListener((observable, previous, current) ->
                selectionSyncGuard.runGuarded(() -> {
                    if (current == null) {
                        nodeList.getSelectionModel().clearSelection();
                    } else if (nodeList.getSelectionModel().getSelectedItem() != current) {
                        nodeList.getSelectionModel().select(current);
                    }
                }));
        viewModel.selectedEdgeProperty().addListener((observable, previous, current) ->
                selectionSyncGuard.runGuarded(() -> {
                    if (current == null) {
                        edgeList.getSelectionModel().clearSelection();
                    } else if (edgeList.getSelectionModel().getSelectedItem() != current) {
                        edgeList.getSelectionModel().select(current);
                    }
                }));
    }


    private static Tab tab(String title, Parent content) {
        Tab tab = new Tab(title, content);
        tab.setClosable(false);
        return tab;
    }

    private static final class EdgeCell extends ListCell<ArchitectureEdge> {
        private final ArchitectureDiagramViewModel viewModel;

        private EdgeCell(ArchitectureDiagramViewModel viewModel) {
            this.viewModel = viewModel;
        }

        @Override
        protected void updateItem(ArchitectureEdge item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
                setTooltip(null);
                return;
            }
            VBox graphic = new VBox(3);
            graphic.getStyleClass().add("architecture-structure-item");
            String titleText = viewModel.nodeLabel(item.sourceNodeId()) + " → " + viewModel.nodeLabel(item.targetNodeId());
            Label title = new Label(titleText);
            title.getStyleClass().add("architecture-structure-item-title");
            title.setWrapText(true);
            Label meta = new Label(item.kind().displayName() + (item.label().isBlank() ? "" : " · " + item.label()));
            meta.getStyleClass().add("architecture-structure-item-meta");
            meta.setWrapText(true);
            graphic.getChildren().addAll(title, meta);
            setText(null);
            setGraphic(graphic);
            setTooltip(new Tooltip(titleText + "\n" + meta.getText()));
        }
    }

    private static final class NodeCell extends ListCell<ArchitectureNode> {
        @Override
        protected void updateItem(ArchitectureNode item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
                setTooltip(null);
                return;
            }
            VBox graphic = new VBox(3);
            graphic.getStyleClass().add("architecture-structure-item");
            Label title = new Label(item.displayName());
            title.getStyleClass().add("architecture-structure-item-title");
            title.setWrapText(true);
            Label meta = new Label(item.kind().displayName() + (item.technology().isBlank() ? "" : " · " + item.technology()));
            meta.getStyleClass().add("architecture-structure-item-meta");
            meta.setWrapText(true);
            graphic.getChildren().addAll(title, meta);
            setText(null);
            setGraphic(graphic);
            setTooltip(new Tooltip(item.displayName() + "\n" + meta.getText()));
        }
    }
}
