package com.marcosmoreira.domainmodelstudio.presentation.freegraph;

import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphEdge;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphNode;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasSelectionSynchronizationGuard;
import com.marcosmoreira.domainmodelstudio.presentation.sidedock.SideDockCollectionSizingPolicy;
import java.util.Objects;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;

/** Panel izquierdo de estructura del Grafo libre. */
final class FreeGraphStructurePanel {

    private final FreeGraphViewModel viewModel;
    private final Runnable refreshCanvas;
    private final ScrollPane root;
    private final CanvasSelectionSynchronizationGuard selectionSyncGuard = new CanvasSelectionSynchronizationGuard();
    private final ListView<FreeGraphNode> nodeList = new ListView<>();
    private final ListView<FreeGraphEdge> edgeList = new ListView<>();

    FreeGraphStructurePanel(FreeGraphViewModel viewModel, Runnable refreshCanvas) {
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
        this.refreshCanvas = refreshCanvas == null ? () -> { } : refreshCanvas;
        this.root = new ScrollPane(buildContent());
        configureRoot();
        bindViewModel();
    }

    Parent root() {
        return root;
    }

    private VBox buildContent() {
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        content.getStyleClass().addAll("free-graph-structure", "diagram-workbench-panel-content");

        Label hint = new Label("Nodos y relaciones del grafo activo. Usa la barra del lienzo para seleccionar, crear nodos por clic o crear relaciones nodo a nodo.");
        hint.setWrapText(true);
        hint.getStyleClass().add("free-graph-structure-hint");

        nodeList.setItems(viewModel.nodes());
        nodeList.setCellFactory(ignored -> new NodeCell());
        SideDockCollectionSizingPolicy.configureListViewForExternalSideDockScroll(nodeList);
        nodeList.getStyleClass().add("free-graph-structure-list");
        nodeList.setPlaceholder(emptyLabel("No hay nodos registrados."));

        edgeList.setItems(viewModel.edges());
        edgeList.setCellFactory(ignored -> new EdgeCell(viewModel));
        SideDockCollectionSizingPolicy.configureListViewForExternalSideDockScroll(edgeList);
        edgeList.getStyleClass().add("free-graph-structure-list");
        edgeList.setPlaceholder(emptyLabel("No hay relaciones registradas."));

        TabPane tabs = new TabPane();
        tabs.getStyleClass().add("free-graph-structure-tabs");
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        Tab nodesTab = new Tab("Nodos", nodeList);
        Tab edgesTab = new Tab("Relaciones", edgeList);
        tabs.getTabs().setAll(nodesTab, edgesTab);
        content.getChildren().addAll(hint, tabs);
        return content;
    }

    private void configureRoot() {
        root.setFitToWidth(true);
        root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        root.setPannable(true);
        root.getStyleClass().addAll("free-graph-structure-scroll", "diagram-workbench-structure-scroll");
    }

    private void bindViewModel() {
        nodeList.getSelectionModel().selectedItemProperty().addListener((observable, previous, current) -> {
            if (selectionSyncGuard.active() || current == null) {
                return;
            }
            selectionSyncGuard.runGuarded(() -> edgeList.getSelectionModel().clearSelection());
            viewModel.selectNodeById(current.id());
            refreshCanvas.run();
        });
        edgeList.getSelectionModel().selectedItemProperty().addListener((observable, previous, current) -> {
            if (selectionSyncGuard.active() || current == null) {
                return;
            }
            selectionSyncGuard.runGuarded(() -> nodeList.getSelectionModel().clearSelection());
            viewModel.selectEdgeById(current.id());
            refreshCanvas.run();
        });
        viewModel.selectedNodeProperty().addListener((observable, previous, current) ->
                selectionSyncGuard.runGuarded(() -> {
                    if (current == null) {
                        nodeList.getSelectionModel().clearSelection();
                    } else if (!current.equals(nodeList.getSelectionModel().getSelectedItem())) {
                        nodeList.getSelectionModel().select(current);
                    }
                }));
        viewModel.selectedEdgeProperty().addListener((observable, previous, current) ->
                selectionSyncGuard.runGuarded(() -> {
                    if (current == null) {
                        edgeList.getSelectionModel().clearSelection();
                    } else if (!current.equals(edgeList.getSelectionModel().getSelectedItem())) {
                        edgeList.getSelectionModel().select(current);
                    }
                }));
    }

    private static Label emptyLabel(String text) {
        Label label = new Label(text);
        label.setWrapText(true);
        label.getStyleClass().add("free-graph-structure-empty");
        return label;
    }

    private static final class NodeCell extends ListCell<FreeGraphNode> {
        @Override
        protected void updateItem(FreeGraphNode item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
                setTooltip(null);
                return;
            }
            VBox graphic = new VBox(3);
            graphic.getStyleClass().add("free-graph-structure-item");
            Label title = new Label(item.title());
            title.getStyleClass().add("free-graph-structure-item-title");
            title.setWrapText(true);
            Label meta = new Label(item.content().isBlank() ? "Sin contenido" : clamp(item.content(), 86));
            meta.getStyleClass().add("free-graph-structure-item-meta");
            meta.setWrapText(true);
            graphic.getChildren().addAll(title, meta);
            setText(null);
            setGraphic(graphic);
            setTooltip(new Tooltip(item.title() + "\n" + meta.getText()));
        }
    }

    private static final class EdgeCell extends ListCell<FreeGraphEdge> {
        private final FreeGraphViewModel viewModel;

        private EdgeCell(FreeGraphViewModel viewModel) {
            this.viewModel = viewModel;
        }

        @Override
        protected void updateItem(FreeGraphEdge item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
                setTooltip(null);
                return;
            }
            String arrow = item.direction().name().equals("UNDIRECTED") ? " — " : " → ";
            String routeText = viewModel.nodeLabel(item.sourceNodeId()) + arrow + viewModel.nodeLabel(item.targetNodeId());
            String labelText = item.label().isBlank() ? "Sin etiqueta visible" : item.label();
            VBox graphic = new VBox(3);
            graphic.getStyleClass().add("free-graph-structure-item");

            Label route = new Label(routeText);
            route.getStyleClass().add("free-graph-structure-edge-route");
            route.setWrapText(true);

            Label relationLabel = new Label(labelText);
            relationLabel.getStyleClass().add(item.label().isBlank()
                    ? "free-graph-structure-edge-label-empty"
                    : "free-graph-structure-edge-label");
            relationLabel.setWrapText(true);

            Label meta = new Label(item.direction().displayName());
            meta.getStyleClass().add("free-graph-structure-edge-meta");
            meta.setWrapText(true);

            graphic.getChildren().addAll(route, relationLabel, meta);
            setText(null);
            setGraphic(graphic);
            setTooltip(new Tooltip(routeText + "\n" + labelText + "\n" + item.direction().displayName()));
        }
    }

    private static String clamp(String value, int maxLength) {
        String normalized = value == null ? "" : value.strip();
        if (normalized.length() <= maxLength) {
            return normalized;
        }
        return normalized.substring(0, Math.max(0, maxLength - 1)) + "…";
    }
}
