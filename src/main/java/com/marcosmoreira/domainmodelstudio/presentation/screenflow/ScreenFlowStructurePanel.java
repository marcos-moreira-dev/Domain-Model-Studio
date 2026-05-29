package com.marcosmoreira.domainmodelstudio.presentation.screenflow;

import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenNode;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenTransition;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasSelectionSynchronizationGuard;
import com.marcosmoreira.domainmodelstudio.presentation.sidedock.SideDockCollectionSizingPolicy;
import java.util.Objects;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/** Panel izquierdo del flujo de pantallas montado como slot del workbench común. */
final class ScreenFlowStructurePanel {

    private final ScreenFlowViewModel viewModel;
    private final Runnable refreshCanvas;
    private final VBox root = new VBox(10);
    private final CanvasSelectionSynchronizationGuard selectionSyncGuard = new CanvasSelectionSynchronizationGuard();
    private final ListView<ScreenNode> screenList = new ListView<>();
    private final TableView<ScreenTransition> transitionTable = new TableView<>();

    ScreenFlowStructurePanel(ScreenFlowViewModel viewModel, Runnable refreshCanvas) {
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
        this.refreshCanvas = refreshCanvas == null ? () -> { } : refreshCanvas;
        build();
        bindViewModel();
    }

    Parent root() {
        return root;
    }

    private void build() {
        root.getStyleClass().addAll("screen-flow-structure-panel", "diagram-workbench-panel-content");
        root.setPadding(new Insets(10));
        Label help = new Label("Selecciona pantallas o transiciones para editar sus datos y revisar el flujo de navegación.");
        help.getStyleClass().add("screen-flow-panel-help");
        help.setWrapText(true);

        Label screensTitle = sectionTitle("Pantallas");
        screenList.setItems(viewModel.screens());
        screenList.getStyleClass().add("screen-flow-screen-list");
        screenList.setCellFactory(ignored -> new ScreenListCell());
        SideDockCollectionSizingPolicy.configureListView(screenList);
        VBox.setVgrow(screenList, Priority.ALWAYS);

        Label transitionsTitle = sectionTitle("Transiciones");
        transitionTable.setItems(viewModel.transitions());
        transitionTable.getStyleClass().add("screen-flow-transition-table");
        transitionTable.getColumns().add(column("Origen", transition -> nameOfScreen(transition.sourceScreenId())));
        transitionTable.getColumns().add(column("Destino", transition -> nameOfScreen(transition.targetScreenId())));
        transitionTable.getColumns().add(column("Acción", ScreenTransition::trigger));
        SideDockCollectionSizingPolicy.configureTableView(transitionTable);
        VBox.setVgrow(transitionTable, Priority.SOMETIMES);

        root.getChildren().addAll(help, screensTitle, screenList, transitionsTitle, transitionTable);
    }

    private void bindViewModel() {
        screenList.getSelectionModel().selectedItemProperty().addListener((observable, previous, current) -> {
            if (selectionSyncGuard.active() || current == null) {
                return;
            }
            viewModel.selectedScreenProperty().set(current);
            viewModel.selectedTransitionProperty().set(null);
            selectionSyncGuard.runGuarded(() -> transitionTable.getSelectionModel().clearSelection());
            refreshCanvas.run();
        });
        transitionTable.getSelectionModel().selectedItemProperty().addListener((observable, previous, current) -> {
            if (selectionSyncGuard.active() || current == null) {
                return;
            }
            viewModel.selectedTransitionProperty().set(current);
            viewModel.selectedScreenProperty().set(null);
            selectionSyncGuard.runGuarded(() -> screenList.getSelectionModel().clearSelection());
            refreshCanvas.run();
        });
        viewModel.selectedScreenProperty().addListener((observable, previous, current) ->
                selectionSyncGuard.runGuarded(() -> {
                    if (current == null) {
                        screenList.getSelectionModel().clearSelection();
                    } else if (screenList.getSelectionModel().getSelectedItem() != current) {
                        screenList.getSelectionModel().select(current);
                    }
                }));
        viewModel.selectedTransitionProperty().addListener((observable, previous, current) ->
                selectionSyncGuard.runGuarded(() -> {
                    if (current == null) {
                        transitionTable.getSelectionModel().clearSelection();
                    } else if (transitionTable.getSelectionModel().getSelectedItem() != current) {
                        transitionTable.getSelectionModel().select(current);
                    }
                }));
    }

    private String nameOfScreen(String id) {
        return viewModel.screens().stream()
                .filter(screen -> screen.id().equals(id))
                .findFirst()
                .map(ScreenNode::displayName)
                .orElse(id);
    }

    private Label sectionTitle(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("screen-flow-panel-section-title");
        return label;
    }

    private TableColumn<ScreenTransition, String> column(
            String title,
            java.util.function.Function<ScreenTransition, String> extractor
    ) {
        TableColumn<ScreenTransition, String> column = new TableColumn<>(title);
        column.setCellValueFactory(cell -> new SimpleStringProperty(extractor.apply(cell.getValue())));
        column.setPrefWidth(110);
        return column;
    }

    private static final class ScreenListCell extends ListCell<ScreenNode> {
        @Override
        protected void updateItem(ScreenNode item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
                setTooltip(null);
                return;
            }
            VBox graphic = new VBox(3);
            graphic.getStyleClass().add("screen-flow-structure-item");
            Label title = new Label(item.displayName());
            title.getStyleClass().add("screen-flow-structure-item-title");
            title.setWrapText(true);
            String module = item.moduleName().isBlank() ? "Sin módulo" : item.moduleName();
            String route = item.route().isBlank() ? "Sin ruta" : item.route();
            Label meta = new Label(item.kind().displayName() + " · " + module + " · " + route);
            meta.getStyleClass().add("screen-flow-structure-item-meta");
            meta.setWrapText(true);
            graphic.getChildren().addAll(title, meta);
            setText(null);
            setGraphic(graphic);
            setTooltip(new Tooltip(item.displayName() + "\n" + meta.getText()));
        }
    }
}
