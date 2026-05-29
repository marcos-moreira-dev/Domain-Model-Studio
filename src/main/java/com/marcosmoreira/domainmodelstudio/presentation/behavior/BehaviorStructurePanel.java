package com.marcosmoreira.domainmodelstudio.presentation.behavior;

import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramKind;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorEdge;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNode;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasSelectionSynchronizationGuard;
import com.marcosmoreira.domainmodelstudio.presentation.sidedock.SideDockCollectionSizingPolicy;
import java.util.Objects;
import javafx.collections.ListChangeListener;
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

/** Panel izquierdo de diagramas de comportamiento montado como slot común. */
final class BehaviorStructurePanel {

    private final BehaviorDiagramViewModel viewModel;
    private final Runnable refreshCanvas;
    private final VBox root = new VBox(10);
    private final CanvasSelectionSynchronizationGuard selectionSyncGuard = new CanvasSelectionSynchronizationGuard();
    private final ListView<BehaviorNode> nodeList = new ListView<>();
    private final VBox flowBox = new VBox(6);
    private final ListView<BehaviorEdge> flowList = new ListView<>();
    private final VBox sequenceTimelineBox = new VBox(6);
    private final ListView<BehaviorEdge> sequenceMessageList = new ListView<>();
    private final TabPane tabs = new TabPane();
    private Tab elementsTab;
    private Tab relationsTab;
    private Tab sequenceTab;

    BehaviorStructurePanel(BehaviorDiagramViewModel viewModel, Runnable refreshCanvas) {
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
        this.refreshCanvas = refreshCanvas == null ? () -> { } : refreshCanvas;
        build();
        bindViewModel();
        refreshContextualTabs();
    }

    Parent root() {
        return root;
    }

    private void build() {
        root.getStyleClass().addAll("behavior-structure-panel", "diagram-workbench-panel-content");
        root.setPadding(new Insets(10));
        Label help = new Label("Selecciona un elemento de comportamiento; puedes moverlo en el lienzo.");
        help.getStyleClass().add("behavior-panel-help");
        help.setWrapText(true);
        nodeList.setItems(viewModel.nodes());
        nodeList.getStyleClass().add("behavior-node-list");
        nodeList.setCellFactory(ignored -> new NodeCell());
        SideDockCollectionSizingPolicy.configureListView(nodeList);
        flowList.setItems(viewModel.edges());
        flowList.getStyleClass().add("behavior-flow-list");
        flowList.setCellFactory(ignored -> new EdgeCell(viewModel));
        SideDockCollectionSizingPolicy.configureListView(flowList);
        Label flowTitle = new Label("Flujos y transiciones");
        flowTitle.getStyleClass().add("behavior-section-title");
        Label flowHelp = new Label("Las relaciones también son estructura: selecciona un flujo para editar origen, destino o condición.");
        flowHelp.getStyleClass().add("behavior-panel-help");
        flowHelp.setWrapText(true);
        flowBox.getStyleClass().add("behavior-flow-box");
        flowBox.getChildren().addAll(flowTitle, flowHelp, flowList);
        VBox.setVgrow(nodeList, Priority.ALWAYS);

        Label timelineTitle = new Label("Mensajes temporales");
        timelineTitle.getStyleClass().add("behavior-section-title");
        Label timelineHelp = new Label("En UML Secuencia, el orden vertical de esta lista es el tiempo: 1, 2, 3...");
        timelineHelp.getStyleClass().add("behavior-panel-help");
        timelineHelp.setWrapText(true);
        sequenceMessageList.setItems(viewModel.edges());
        sequenceMessageList.getStyleClass().add("behavior-sequence-message-list");
        sequenceMessageList.setCellFactory(ignored -> new SequenceMessageCell(viewModel));
        SideDockCollectionSizingPolicy.configureListView(sequenceMessageList);
        sequenceTimelineBox.getStyleClass().add("behavior-sequence-timeline-box");
        sequenceTimelineBox.getChildren().addAll(timelineTitle, timelineHelp, sequenceMessageList);

        tabs.getStyleClass().add("behavior-structure-tabs");
        elementsTab = tab("Elementos", nodeList);
        relationsTab = tab("Relaciones", flowBox);
        sequenceTab = tab("Secuencia", sequenceTimelineBox);
        VBox.setVgrow(tabs, Priority.ALWAYS);
        root.getChildren().addAll(help, WorkbenchPanelSupport.emptySelectionCard("un elemento o una relación"), tabs);
    }

    private void bindViewModel() {
        viewModel.nodes().addListener((ListChangeListener<BehaviorNode>) change -> refreshContextualTabs());
        viewModel.edges().addListener((ListChangeListener<BehaviorEdge>) change -> refreshContextualTabs());
        nodeList.getSelectionModel().selectedItemProperty().addListener((observable, previous, current) -> {
            if (selectionSyncGuard.active() || current == null) {
                return;
            }
            viewModel.selectNodeById(current.id());
            refreshCanvas.run();
            refreshContextualTabs();
        });
        flowList.getSelectionModel().selectedItemProperty().addListener((observable, previous, current) -> {
            if (selectionSyncGuard.active() || current == null) {
                return;
            }
            viewModel.selectEdgeById(current.id());
            selectionSyncGuard.runGuarded(() -> nodeList.getSelectionModel().clearSelection());
            refreshCanvas.run();
            refreshContextualTabs();
        });
        sequenceMessageList.getSelectionModel().selectedItemProperty().addListener((observable, previous, current) -> {
            if (selectionSyncGuard.active() || current == null) {
                return;
            }
            viewModel.selectEdgeById(current.id());
            selectionSyncGuard.runGuarded(() -> nodeList.getSelectionModel().clearSelection());
            refreshCanvas.run();
            refreshContextualTabs();
        });
        viewModel.selectedNodeProperty().addListener((observable, previous, current) -> {
            selectionSyncGuard.runGuarded(() -> {
                if (current == null) {
                    nodeList.getSelectionModel().clearSelection();
                } else if (nodeList.getSelectionModel().getSelectedItem() != current) {
                    nodeList.getSelectionModel().select(current);
                }
            });
            refreshContextualTabs();
        });
        viewModel.selectedEdgeProperty().addListener((observable, previous, current) -> {
            selectionSyncGuard.runGuarded(() -> {
                if (current == null) {
                    flowList.getSelectionModel().clearSelection();
                    sequenceMessageList.getSelectionModel().clearSelection();
                } else {
                    if (flowList.getSelectionModel().getSelectedItem() != current) {
                        flowList.getSelectionModel().select(current);
                    }
                    if (sequenceMessageList.getSelectionModel().getSelectedItem() != current) {
                        sequenceMessageList.getSelectionModel().select(current);
                    }
                }
            });
            refreshContextualTabs();
        });
    }

    private void refreshContextualTabs() {
        boolean sequence = viewModel.currentKind() == BehaviorDiagramKind.UML_SEQUENCE;
        if (tabs.getTabs().isEmpty()) {
            tabs.getTabs().add(elementsTab);
        }
        if (sequence) {
            if (tabs.getTabs().contains(relationsTab)) {
                tabs.getTabs().remove(relationsTab);
            }
            if (!tabs.getTabs().contains(sequenceTab)) {
                tabs.getTabs().add(sequenceTab);
            }
        } else {
            if (tabs.getTabs().contains(sequenceTab)) {
                tabs.getTabs().remove(sequenceTab);
            }
            if (!tabs.getTabs().contains(relationsTab)) {
                tabs.getTabs().add(relationsTab);
            }
        }
    }


    private static Tab tab(String title, Parent content) {
        Tab tab = new Tab(title, content);
        tab.setClosable(false);
        return tab;
    }

    private static final class EdgeCell extends ListCell<BehaviorEdge> {
        private final BehaviorDiagramViewModel viewModel;

        private EdgeCell(BehaviorDiagramViewModel viewModel) {
            this.viewModel = viewModel;
        }

        @Override
        protected void updateItem(BehaviorEdge item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
                setTooltip(null);
                return;
            }
            String source = viewModel.nodeLabel(item.sourceNodeId());
            String target = viewModel.nodeLabel(item.targetNodeId());
            VBox graphic = new VBox(2);
            graphic.getStyleClass().add("behavior-structure-item");
            Label title = new Label(source + " → " + target);
            title.getStyleClass().add("behavior-structure-item-title");
            title.setWrapText(true);
            Label meta = new Label(item.label().isBlank() ? item.kind().displayName() : item.kind().displayName() + " · " + item.label());
            meta.getStyleClass().add("behavior-structure-item-meta");
            meta.setWrapText(true);
            graphic.getChildren().addAll(title, meta);
            setText(null);
            setGraphic(graphic);
            setTooltip(new Tooltip(title.getText() + "\n" + meta.getText()));
        }
    }

    private static final class NodeCell extends ListCell<BehaviorNode> {
        @Override
        protected void updateItem(BehaviorNode item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
                setTooltip(null);
                return;
            }
            VBox graphic = new VBox(3);
            graphic.getStyleClass().add("behavior-structure-item");
            Label title = new Label(item.displayName());
            title.getStyleClass().add("behavior-structure-item-title");
            title.setWrapText(true);
            Label meta = new Label(item.kind().displayName());
            meta.getStyleClass().add("behavior-structure-item-meta");
            meta.setWrapText(true);
            graphic.getChildren().addAll(title, meta);
            setText(null);
            setGraphic(graphic);
            setTooltip(new Tooltip(item.displayName() + "\n" + item.kind().displayName()));
        }
    }

    private static final class SequenceMessageCell extends ListCell<BehaviorEdge> {
        private final BehaviorDiagramViewModel viewModel;

        private SequenceMessageCell(BehaviorDiagramViewModel viewModel) {
            this.viewModel = viewModel;
        }

        @Override
        protected void updateItem(BehaviorEdge item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
                setTooltip(null);
                return;
            }
            int number = Math.max(1, getIndex() + 1);
            String source = viewModel.nodeLabel(item.sourceNodeId());
            String target = viewModel.nodeLabel(item.targetNodeId());
            VBox graphic = new VBox(2);
            graphic.getStyleClass().add("behavior-structure-item");
            Label title = new Label(number + ". " + source + " → " + target);
            title.getStyleClass().add("behavior-structure-item-title");
            title.setWrapText(true);
            Label meta = new Label(item.label().isBlank() ? item.kind().displayName() : item.label());
            meta.getStyleClass().add("behavior-structure-item-meta");
            meta.setWrapText(true);
            graphic.getChildren().addAll(title, meta);
            setText(null);
            setGraphic(graphic);
            setTooltip(new Tooltip(title.getText() + "\n" + meta.getText()));
        }
    }
}
