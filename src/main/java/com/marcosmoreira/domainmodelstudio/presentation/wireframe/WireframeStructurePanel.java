package com.marcosmoreira.domainmodelstudio.presentation.wireframe;

import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeComponent;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeScreen;
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

/** Panel izquierdo de wireframes montado como slot del workbench común. */
final class WireframeStructurePanel {

    private final WireframeViewModel viewModel;
    private final Runnable refreshCanvas;
    private final VBox root = new VBox(10);
    private final CanvasSelectionSynchronizationGuard selectionSyncGuard = new CanvasSelectionSynchronizationGuard();
    private final ListView<WireframeScreen> screenList = new ListView<>();
    private final TableView<WireframeComponent> componentTable = new TableView<>();

    WireframeStructurePanel(WireframeViewModel viewModel, Runnable refreshCanvas) {
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
        this.refreshCanvas = refreshCanvas == null ? () -> { } : refreshCanvas;
        build();
        bindViewModel();
    }

    Parent root() {
        return root;
    }

    private void build() {
        root.getStyleClass().addAll("wireframe-structure-panel", "diagram-workbench-panel-content");
        root.setPadding(new Insets(10));
        Label help = new Label("Selecciona una pantalla o componente para editar la maqueta estructural.");
        help.getStyleClass().add("wireframe-panel-help");
        help.setWrapText(true);

        Label screensTitle = sectionTitle("Pantallas");
        screenList.setItems(viewModel.screens());
        screenList.getStyleClass().add("wireframe-screen-list");
        screenList.setCellFactory(ignored -> new ScreenListCell());
        SideDockCollectionSizingPolicy.configureListView(screenList);
        VBox.setVgrow(screenList, Priority.ALWAYS);

        Label componentsTitle = sectionTitle("Componentes");
        componentTable.setItems(viewModel.components());
        componentTable.getStyleClass().add("wireframe-component-table");
        componentTable.getColumns().add(column("Pantalla", component -> nameOfScreen(component.screenId())));
        componentTable.getColumns().add(column("Tipo", component -> component.kind().displayName()));
        componentTable.getColumns().add(column("Componente", WireframeComponent::displayName));
        SideDockCollectionSizingPolicy.configureTableView(componentTable);
        VBox.setVgrow(componentTable, Priority.SOMETIMES);

        root.getChildren().addAll(help, screensTitle, screenList, componentsTitle, componentTable);
    }

    private void bindViewModel() {
        screenList.getSelectionModel().selectedItemProperty().addListener((observable, previous, current) -> {
            if (selectionSyncGuard.active() || current == null) {
                return;
            }
            viewModel.selectedScreenProperty().set(current);
            if (viewModel.selectedComponentProperty().get() == null
                    || !viewModel.selectedComponentProperty().get().screenId().equals(current.id())) {
                viewModel.selectedComponentProperty().set(null);
                selectionSyncGuard.runGuarded(() -> componentTable.getSelectionModel().clearSelection());
            }
            refreshCanvas.run();
        });
        componentTable.getSelectionModel().selectedItemProperty().addListener((observable, previous, current) -> {
            if (selectionSyncGuard.active() || current == null) {
                return;
            }
            viewModel.selectedComponentProperty().set(current);
            viewModel.selectedScreenProperty().set(findScreen(current.screenId()));
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
        viewModel.selectedComponentProperty().addListener((observable, previous, current) ->
                selectionSyncGuard.runGuarded(() -> {
                    if (current == null) {
                        componentTable.getSelectionModel().clearSelection();
                    } else if (componentTable.getSelectionModel().getSelectedItem() != current) {
                        componentTable.getSelectionModel().select(current);
                    }
                }));
    }

    private WireframeScreen findScreen(String id) {
        return viewModel.screens().stream()
                .filter(screen -> screen.id().equals(id))
                .findFirst()
                .orElse(null);
    }

    private String nameOfScreen(String id) {
        WireframeScreen screen = findScreen(id);
        return screen == null ? id : screen.displayName();
    }

    private Label sectionTitle(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("wireframe-panel-section-title");
        return label;
    }

    private TableColumn<WireframeComponent, String> column(
            String title,
            java.util.function.Function<WireframeComponent, String> extractor
    ) {
        TableColumn<WireframeComponent, String> column = new TableColumn<>(title);
        column.setCellValueFactory(cell -> new SimpleStringProperty(extractor.apply(cell.getValue())));
        column.setPrefWidth(110);
        return column;
    }

    private static final class ScreenListCell extends ListCell<WireframeScreen> {
        @Override
        protected void updateItem(WireframeScreen item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
                setTooltip(null);
                return;
            }
            VBox graphic = new VBox(3);
            graphic.getStyleClass().add("wireframe-structure-item");
            Label title = new Label(item.displayName());
            title.getStyleClass().add("wireframe-structure-item-title");
            title.setWrapText(true);
            String module = item.moduleName().isBlank() ? "Sin módulo" : item.moduleName();
            String purpose = item.purpose().isBlank() ? "Sin propósito" : item.purpose();
            Label meta = new Label(module + " · " + purpose);
            meta.getStyleClass().add("wireframe-structure-item-meta");
            meta.setWrapText(true);
            graphic.getChildren().addAll(title, meta);
            setText(null);
            setGraphic(graphic);
            setTooltip(new Tooltip(item.displayName() + "\n" + meta.getText()));
        }
    }
}
