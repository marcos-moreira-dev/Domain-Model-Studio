package com.marcosmoreira.domainmodelstudio.presentation.logicalbusinessgraph;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphEdge;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphIssue;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphNode;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphNodeKind;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasSelectionSynchronizationGuard;
import com.marcosmoreira.domainmodelstudio.presentation.sidedock.SideDockCollectionSizingPolicy;
import java.util.Arrays;
import java.util.Objects;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;

/** Panel de estructura del Grafo lógico del negocio. */
final class LogicalBusinessGraphStructurePanel {

    private final LogicalBusinessGraphViewModel viewModel;
    private final Runnable refreshCanvas;
    private final VBox root;
    private final CanvasSelectionSynchronizationGuard selectionSyncGuard = new CanvasSelectionSynchronizationGuard();
    private final ListView<LogicalBusinessGraphNode> nodeList = new ListView<>();
    private final ListView<LogicalBusinessGraphEdge> edgeList = new ListView<>();
    private final ListView<LogicalBusinessGraphIssue> issueList = new ListView<>();

    LogicalBusinessGraphStructurePanel(LogicalBusinessGraphViewModel viewModel, Runnable refreshCanvas) {
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
        this.refreshCanvas = refreshCanvas == null ? () -> { } : refreshCanvas;
        this.root = buildContent();
        configureRoot();
        bindViewModel();
    }

    Parent root() {
        return root;
    }

    private VBox buildContent() {
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        content.getStyleClass().addAll("logical-business-graph-structure", "diagram-workbench-panel-content");

        Label hint = new Label("Navega macroflujos, microflujos, casos de uso y elementos lógicos derivados del levantamiento de negocio.");
        hint.setWrapText(true);
        hint.getStyleClass().add("logical-business-graph-panel-hint");

        nodeList.setItems(viewModel.nodes());
        nodeList.setCellFactory(ignored -> new NodeCell());
        SideDockCollectionSizingPolicy.configureListViewForExternalSideDockScroll(nodeList);
        nodeList.getStyleClass().add("logical-business-graph-list");
        nodeList.setPlaceholder(emptyLabel("No hay nodos lógicos registrados."));

        edgeList.setItems(viewModel.edges());
        edgeList.setCellFactory(ignored -> new EdgeCell(viewModel));
        SideDockCollectionSizingPolicy.configureListViewForExternalSideDockScroll(edgeList);
        edgeList.getStyleClass().add("logical-business-graph-list");
        edgeList.setPlaceholder(emptyLabel("No hay relaciones lógicas registradas."));

        issueList.setItems(viewModel.semanticIssues());
        issueList.setCellFactory(ignored -> new IssueCell());
        SideDockCollectionSizingPolicy.configureListViewForExternalSideDockScroll(issueList);
        issueList.getStyleClass().add("logical-business-graph-list");
        issueList.setPlaceholder(emptyLabel("Sin advertencias semánticas."));

        TabPane tabs = new TabPane();
        tabs.getStyleClass().add("logical-business-graph-tabs");
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabs.getTabs().setAll(
                new Tab("Nodos", nodeList),
                new Tab("Relaciones", edgeList),
                new Tab("Leyenda", legendView()),
                new Tab("Validación", issueList)
        );
        content.getChildren().addAll(hint, tabs);
        return content;
    }

    private void configureRoot() {
        root.getStyleClass().addAll(
                "logical-business-graph-structure-root",
                "diagram-workbench-structure-root"
        );
    }

    private Parent legendView() {
        VBox box = new VBox(7);
        box.setPadding(new Insets(8));
        box.getStyleClass().add("logical-business-graph-legend-box");
        Label intro = new Label("Abreviaciones visibles del grafo lógico:");
        intro.setWrapText(true);
        intro.getStyleClass().add("logical-business-graph-legend-title");
        box.getChildren().add(intro);
        Arrays.stream(LogicalBusinessGraphNodeKind.values())
                .map(kind -> legendLine(kind.legendEntry(), kind.description()))
                .forEach(box.getChildren()::add);
        return box;
    }

    private void bindViewModel() {
        nodeList.getSelectionModel().selectedItemProperty().addListener((observable, previous, current) -> {
            if (selectionSyncGuard.active() || current == null) {
                return;
            }
            selectionSyncGuard.runGuarded(() -> edgeList.getSelectionModel().clearSelection());
            viewModel.selectNodeByCode(current.code());
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
        viewModel.semanticIssues().addListener((javafx.collections.ListChangeListener<LogicalBusinessGraphIssue>) change -> issueList.refresh());
    }

    private static VBox legendLine(String title, String detail) {
        VBox box = new VBox(2);
        box.getStyleClass().add("logical-business-graph-legend-item");
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("logical-business-graph-legend-item-title");
        titleLabel.setWrapText(true);
        Label detailLabel = new Label(detail);
        detailLabel.getStyleClass().add("logical-business-graph-legend-item-detail");
        detailLabel.setWrapText(true);
        box.getChildren().addAll(titleLabel, detailLabel);
        return box;
    }

    private static Label emptyLabel(String text) {
        Label label = new Label(text);
        label.setWrapText(true);
        label.getStyleClass().add("logical-business-graph-empty");
        return label;
    }

    private static final class NodeCell extends ListCell<LogicalBusinessGraphNode> {
        @Override
        protected void updateItem(LogicalBusinessGraphNode item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
                setTooltip(null);
                return;
            }
            VBox graphic = new VBox(3);
            graphic.getStyleClass().add("logical-business-graph-list-item");
            Label title = new Label(item.code() + " — " + item.title());
            title.getStyleClass().add("logical-business-graph-list-item-title");
            title.setWrapText(true);
            Label meta = new Label(item.kind().displayName() + " · " + item.status().displayName());
            meta.getStyleClass().add("logical-business-graph-list-item-meta");
            meta.setWrapText(true);
            Label description = new Label(item.description().isBlank() ? "Sin descripción" : clamp(item.description(), 92));
            description.getStyleClass().add("logical-business-graph-list-item-detail");
            description.setWrapText(true);
            graphic.getChildren().addAll(title, meta, description);
            setText(null);
            setGraphic(graphic);
            setTooltip(new Tooltip(item.compactLabel()));
        }
    }

    private static final class EdgeCell extends ListCell<LogicalBusinessGraphEdge> {
        private final LogicalBusinessGraphViewModel viewModel;

        private EdgeCell(LogicalBusinessGraphViewModel viewModel) {
            this.viewModel = viewModel;
        }

        @Override
        protected void updateItem(LogicalBusinessGraphEdge item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
                setTooltip(null);
                return;
            }
            VBox graphic = new VBox(3);
            graphic.getStyleClass().add("logical-business-graph-list-item");
            Label route = new Label(viewModel.nodeLabel(item.sourceCode()) + " → " + viewModel.nodeLabel(item.targetCode()));
            route.getStyleClass().add("logical-business-graph-list-item-title");
            route.setWrapText(true);
            Label relation = new Label(item.relationKind().code() + " — " + item.relationKind().description());
            relation.getStyleClass().add("logical-business-graph-list-item-meta");
            relation.setWrapText(true);
            Label description = new Label(item.description().isBlank() ? "Sin descripción" : clamp(item.description(), 92));
            description.getStyleClass().add("logical-business-graph-list-item-detail");
            description.setWrapText(true);
            graphic.getChildren().addAll(route, relation, description);
            setText(null);
            setGraphic(graphic);
            setTooltip(new Tooltip(item.id() + " · " + item.relationKind().code()));
        }
    }

    private static final class IssueCell extends ListCell<LogicalBusinessGraphIssue> {
        @Override
        protected void updateItem(LogicalBusinessGraphIssue item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
                setTooltip(null);
                return;
            }
            VBox graphic = new VBox(3);
            graphic.getStyleClass().add("logical-business-graph-list-item");
            Label title = new Label(item.severity().name() + (item.elementId().isBlank() ? "" : " · " + item.elementId()));
            title.getStyleClass().add("logical-business-graph-list-item-title");
            title.setWrapText(true);
            Label message = new Label(item.message());
            message.getStyleClass().add("logical-business-graph-list-item-detail");
            message.setWrapText(true);
            graphic.getChildren().addAll(title, message);
            setText(null);
            setGraphic(graphic);
            setTooltip(new Tooltip(item.message()));
        }
    }

    private static String clamp(String value, int maxLength) {
        String normalized = value == null ? "" : value.replace('\n', ' ').strip();
        if (normalized.length() <= maxLength) {
            return normalized;
        }
        return normalized.substring(0, Math.max(0, maxLength - 1)) + "…";
    }
}
