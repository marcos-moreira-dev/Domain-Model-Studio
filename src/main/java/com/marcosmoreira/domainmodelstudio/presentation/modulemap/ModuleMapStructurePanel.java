package com.marcosmoreira.domainmodelstudio.presentation.modulemap;

import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleDependency;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleNode;
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

/** Panel izquierdo del mapa de módulos montado como slot del workbench común. */
final class ModuleMapStructurePanel {

    private final ModuleMapViewModel viewModel;
    private final Runnable refreshCanvas;
    private final VBox root = new VBox(10);
    private final CanvasSelectionSynchronizationGuard selectionSyncGuard = new CanvasSelectionSynchronizationGuard();
    private final ListView<ModuleNode> moduleList = new ListView<>();
    private final ListView<ModuleDependency> dependencyList = new ListView<>();

    ModuleMapStructurePanel(ModuleMapViewModel viewModel, Runnable refreshCanvas) {
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
        this.refreshCanvas = refreshCanvas == null ? () -> { } : refreshCanvas;
        build();
        bindViewModel();
    }

    Parent root() {
        return root;
    }

    private void build() {
        root.getStyleClass().addAll("module-map-structure-panel", "diagram-workbench-panel-content");
        root.setPadding(new Insets(10));
        Label help = new Label("Selecciona un módulo para editarlo o arrástralo en el lienzo.");
        help.getStyleClass().add("module-map-panel-help");
        help.setWrapText(true);
        moduleList.setItems(viewModel.modules());
        moduleList.getStyleClass().add("module-map-module-list");
        moduleList.setCellFactory(ignored -> new ModuleListCell());
        SideDockCollectionSizingPolicy.configureListView(moduleList);
        dependencyList.setItems(viewModel.dependencies());
        dependencyList.getStyleClass().add("module-map-dependency-list");
        dependencyList.setCellFactory(ignored -> new DependencyListCell(viewModel));
        SideDockCollectionSizingPolicy.configureListView(dependencyList);
        TabPane tabs = new TabPane();
        tabs.getStyleClass().add("module-map-structure-tabs");
        tabs.getTabs().add(tab("Módulos", moduleList));
        tabs.getTabs().add(tab("Dependencias", dependencyList));
        VBox.setVgrow(tabs, Priority.ALWAYS);
        root.getChildren().addAll(help, WorkbenchPanelSupport.emptySelectionCard("un módulo o una dependencia"), tabs);
    }

    private void bindViewModel() {
        moduleList.getSelectionModel().selectedItemProperty().addListener((observable, previous, current) -> {
            if (selectionSyncGuard.active() || current == null) {
                return;
            }
            viewModel.selectedModuleProperty().set(current);
            viewModel.selectedDependencyProperty().set(null);
            selectionSyncGuard.runGuarded(() -> dependencyList.getSelectionModel().clearSelection());
            refreshCanvas.run();
        });
        dependencyList.getSelectionModel().selectedItemProperty().addListener((observable, previous, current) -> {
            if (selectionSyncGuard.active() || current == null) {
                return;
            }
            viewModel.selectedDependencyProperty().set(current);
            viewModel.selectedModuleProperty().set(null);
            selectionSyncGuard.runGuarded(() -> moduleList.getSelectionModel().clearSelection());
            refreshCanvas.run();
        });
        viewModel.selectedModuleProperty().addListener((observable, previous, current) ->
                selectionSyncGuard.runGuarded(() -> {
                    if (current == null) {
                        moduleList.getSelectionModel().clearSelection();
                    } else if (moduleList.getSelectionModel().getSelectedItem() != current) {
                        moduleList.getSelectionModel().select(current);
                    }
                }));
        viewModel.selectedDependencyProperty().addListener((observable, previous, current) ->
                selectionSyncGuard.runGuarded(() -> {
                    if (current == null) {
                        dependencyList.getSelectionModel().clearSelection();
                    } else if (dependencyList.getSelectionModel().getSelectedItem() != current) {
                        dependencyList.getSelectionModel().select(current);
                    }
                }));
    }


    private static Tab tab(String title, Parent content) {
        Tab tab = new Tab(title, content);
        tab.setClosable(false);
        return tab;
    }

    private static final class DependencyListCell extends ListCell<ModuleDependency> {
        private final ModuleMapViewModel viewModel;

        private DependencyListCell(ModuleMapViewModel viewModel) {
            this.viewModel = viewModel;
        }

        @Override
        protected void updateItem(ModuleDependency item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
                setTooltip(null);
                return;
            }
            VBox graphic = new VBox(3);
            graphic.getStyleClass().add("module-map-structure-item-child");
            String titleText = viewModel.moduleLabel(item.sourceModuleId()) + " → " + viewModel.moduleLabel(item.targetModuleId());
            Label title = new Label(titleText);
            title.getStyleClass().add("module-map-structure-item-title");
            title.setWrapText(true);
            Label meta = new Label(item.kind().displayName() + (item.description().isBlank() ? "" : " · " + item.description()));
            meta.getStyleClass().add("module-map-structure-item-meta");
            meta.setWrapText(true);
            graphic.getChildren().addAll(title, meta);
            setText(null);
            setGraphic(graphic);
            setTooltip(new Tooltip(titleText + "\n" + meta.getText()));
        }
    }

    private static final class ModuleListCell extends ListCell<ModuleNode> {
        @Override
        protected void updateItem(ModuleNode item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
                setTooltip(null);
                return;
            }
            VBox graphic = new VBox(3);
            graphic.getStyleClass().add(item.rootModule()
                    ? "module-map-structure-item-root"
                    : "module-map-structure-item-child");
            Label title = new Label(prefix(item) + item.displayName());
            title.getStyleClass().add("module-map-structure-item-title");
            title.setWrapText(true);
            Label meta = new Label(item.kind().displayName() + " · " + item.status().displayName());
            meta.getStyleClass().add("module-map-structure-item-meta");
            meta.setWrapText(true);
            graphic.getChildren().addAll(title, meta);
            setText(null);
            setGraphic(graphic);
            setTooltip(new Tooltip(item.displayName() + "\n" + meta.getText()));
        }

        private static String prefix(ModuleNode item) {
            return item.rootModule() ? "" : "↳ ";
        }
    }
}
