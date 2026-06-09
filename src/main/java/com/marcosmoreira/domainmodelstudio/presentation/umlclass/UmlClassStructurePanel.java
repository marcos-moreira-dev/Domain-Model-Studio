package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramView;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassMember;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlModuleGroup;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlRelationKind;
import com.marcosmoreira.domainmodelstudio.presentation.sidedock.SideDockCollectionSizingPolicy;
import java.util.Objects;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/** Panel izquierdo de UML Clases montado como slot del workbench común. */
final class UmlClassStructurePanel {

    private static final double SEARCH_REFRESH_DELAY_MILLIS = 220.0;

    private final UmlClassDiagramViewModel viewModel;
    private final Runnable refreshCanvas;
    private final VBox root = new VBox(10);
    private final ComboBox<UmlClassDiagramView> viewCombo = new ComboBox<>();
    private final TextField searchField = new TextField();
    private final Label visualCostLabel = new Label();
    private final Label runtimeMemoryLabel = new Label();
    private final ComboBox<UmlClassKind> classKindCombo = new ComboBox<>();
    private final ComboBox<UmlRelationKind> relationKindCombo = new ComboBox<>();
    private final ListView<UmlModuleGroup> moduleList = new ListView<>();
    private final ListView<UmlClassNode> classList = new ListView<>();
    private final ListView<UmlClassMember> memberList = new ListView<>();
    private final PauseTransition searchRefreshDebounce = new PauseTransition(Duration.millis(SEARCH_REFRESH_DELAY_MILLIS));
    private boolean syncingControls;
    private String pendingSearchQuery = "";
    private boolean searchRefreshPending;

    UmlClassStructurePanel(UmlClassDiagramViewModel viewModel, Runnable refreshCanvas) {
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
        this.refreshCanvas = refreshCanvas == null ? () -> { } : refreshCanvas;
        searchRefreshDebounce.setOnFinished(event -> applyPendingSearchAndRefreshCanvas());
        build();
        bindViewModel();
    }

    Parent root() {
        return root;
    }

    private void build() {
        root.getStyleClass().addAll("uml-class-structure-panel", "diagram-workbench-panel-content");
        root.setPadding(new Insets(10));
        Label help = new Label("Selecciona módulos, clases o miembros; mueve módulos y clases desde el lienzo.");
        help.getStyleClass().add("uml-class-panel-help");
        help.setWrapText(true);
        visualCostLabel.getStyleClass().add("uml-class-visual-cost");
        visualCostLabel.setWrapText(true);
        runtimeMemoryLabel.getStyleClass().add("uml-class-runtime-memory");
        runtimeMemoryLabel.setWrapText(true);

        configureNavigationControls();

        moduleList.setItems(viewModel.modules());
        moduleList.getStyleClass().add("uml-class-module-list");
        moduleList.setCellFactory(ignored -> new ModuleCell());
        SideDockCollectionSizingPolicy.configureListView(moduleList);
        classList.setItems(viewModel.classes());
        classList.getStyleClass().add("uml-class-class-list");
        classList.setCellFactory(ignored -> new ClassCell(viewModel));
        SideDockCollectionSizingPolicy.configureListView(classList);
        memberList.setItems(viewModel.members());
        memberList.getStyleClass().add("uml-class-member-list");
        memberList.setCellFactory(ignored -> new MemberCell());
        SideDockCollectionSizingPolicy.configureListView(memberList);

        VBox.setVgrow(moduleList, Priority.ALWAYS);
        VBox.setVgrow(classList, Priority.ALWAYS);
        VBox.setVgrow(memberList, Priority.ALWAYS);
        root.getChildren().addAll(
                help,
                visualCostLabel,
                runtimeMemoryLabel,
                sublabel("Vista interna"), viewRow(),
                searchField,
                filterRow(),
                navigationRow(),
                new Separator(),
                sublabel("Módulos / carpetas"), moduleList,
                sublabel("Clases"), classList,
                sublabel("Atributos y métodos"), memberList
        );
    }

    private void configureNavigationControls() {
        viewCombo.setItems(viewModel.views());
        viewCombo.setCellFactory(ignored -> new ViewCell());
        viewCombo.setButtonCell(new ViewCell());
        viewCombo.setPromptText("Todas las vistas");
        viewCombo.getStyleClass().add("uml-class-filter-control");

        searchField.setPromptText("Buscar clase, paquete, ruta o miembro...");
        searchField.getStyleClass().add("uml-class-search-field");

        classKindCombo.getItems().setAll(UmlClassKind.values());
        classKindCombo.setPromptText("Tipo de clase");
        classKindCombo.getStyleClass().add("uml-class-filter-control");
        relationKindCombo.getItems().setAll(UmlRelationKind.values());
        relationKindCombo.setPromptText("Tipo de relación");
        relationKindCombo.getStyleClass().add("uml-class-filter-control");
    }

    private HBox viewRow() {
        Button allViews = smallButton("Todas");
        allViews.setOnAction(event -> {
            flushPendingSearchWithoutRefresh();
            syncingControls = true;
            try {
                viewCombo.getSelectionModel().clearSelection();
            } finally {
                syncingControls = false;
            }
            viewModel.applyViewFilter(null);
            refreshCanvas.run();
        });
        HBox row = new HBox(6, viewCombo, allViews);
        row.getStyleClass().add("uml-class-filter-row");
        return row;
    }

    private HBox filterRow() {
        Button allClasses = smallButton("Clases: todas");
        allClasses.setOnAction(event -> {
            flushPendingSearchWithoutRefresh();
            syncingControls = true;
            try {
                classKindCombo.getSelectionModel().clearSelection();
            } finally {
                syncingControls = false;
            }
            viewModel.applyClassKindFilter(null);
            refreshCanvas.run();
        });
        Button allRelations = smallButton("Rel.: todas");
        allRelations.setOnAction(event -> {
            flushPendingSearchWithoutRefresh();
            syncingControls = true;
            try {
                relationKindCombo.getSelectionModel().clearSelection();
            } finally {
                syncingControls = false;
            }
            viewModel.applyRelationKindFilter(null);
            refreshCanvas.run();
        });
        HBox row = new HBox(6, classKindCombo, allClasses, relationKindCombo, allRelations);
        row.getStyleClass().add("uml-class-filter-row");
        return row;
    }

    private HBox navigationRow() {
        Button next = smallButton("Buscar siguiente");
        next.setOnAction(event -> {
            flushPendingSearchWithoutRefresh();
            viewModel.selectNextVisibleClass();
            refreshCanvas.run();
        });
        Button center = smallButton("Centrar selección");
        center.setOnAction(event -> viewModel.centerSelectionView());
        Button clear = smallButton("Limpiar filtros");
        clear.setOnAction(event -> {
            syncingControls = true;
            try {
                clearPendingSearchRefresh();
                searchField.clear();
                classKindCombo.getSelectionModel().clearSelection();
                relationKindCombo.getSelectionModel().clearSelection();
            } finally {
                syncingControls = false;
            }
            viewModel.clearNavigationFilters();
            refreshCanvas.run();
        });
        HBox row = new HBox(6, next, center, clear);
        row.getStyleClass().add("uml-class-navigation-row");
        return row;
    }

    private Button smallButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("uml-class-small-button");
        return button;
    }

    private void bindViewModel() {
        viewCombo.getSelectionModel().selectedItemProperty().addListener((observable, previous, current) -> {
            if (syncingControls) { return; }
            flushPendingSearchWithoutRefresh();
            viewModel.applyViewFilter(current);
            refreshCanvas.run();
        });
        searchField.textProperty().addListener((observable, previous, current) -> scheduleSearchRefresh(current));
        searchField.setOnAction(event -> applyPendingSearchAndRefreshCanvas());
        classKindCombo.getSelectionModel().selectedItemProperty().addListener((observable, previous, current) -> {
            if (syncingControls) { return; }
            flushPendingSearchWithoutRefresh();
            viewModel.applyClassKindFilter(current);
            refreshCanvas.run();
        });
        relationKindCombo.getSelectionModel().selectedItemProperty().addListener((observable, previous, current) -> {
            if (syncingControls) { return; }
            flushPendingSearchWithoutRefresh();
            viewModel.applyRelationKindFilter(current);
            refreshCanvas.run();
        });
        viewModel.selectedViewProperty().addListener((observable, previous, current) -> syncViewSelection(current));
        moduleList.getSelectionModel().selectedItemProperty().addListener((observable, previous, current) -> {
            if (syncingControls) { return; }
            if (current != null) {
                viewModel.selectModuleById(current.id());
            }
            refreshCanvas.run();
        });
        classList.getSelectionModel().selectedItemProperty().addListener((observable, previous, current) -> {
            if (syncingControls) { return; }
            if (current != null) {
                viewModel.selectClassNodeById(current.id());
                viewModel.refreshMembersForSelection();
            }
            refreshCanvas.run();
        });
        memberList.getSelectionModel().selectedItemProperty().addListener((observable, previous, current) -> {
            if (syncingControls) { return; }
            viewModel.selectedMemberProperty().set(current);
            refreshCanvas.run();
        });
        viewModel.selectedModuleProperty().addListener((observable, previous, current) -> syncModuleSelection(current));
        viewModel.selectedClassProperty().addListener((observable, previous, current) -> syncClassSelection(current));
        viewModel.selectedMemberProperty().addListener((observable, previous, current) -> syncMemberSelection(current));
        viewModel.activeVisualCostEstimateProperty().addListener((observable, previous, current) -> syncVisualCost(current));
        viewModel.activeRuntimeMemorySnapshotProperty().addListener((observable, previous, current) -> syncRuntimeMemory(current));
        syncVisualCost(viewModel.activeVisualCostEstimate());
        syncRuntimeMemory(viewModel.activeRuntimeMemorySnapshot());
    }

    private void scheduleSearchRefresh(String query) {
        if (syncingControls) {
            return;
        }
        pendingSearchQuery = query == null ? "" : query;
        searchRefreshPending = true;
        searchRefreshDebounce.playFromStart();
    }

    private void applyPendingSearchAndRefreshCanvas() {
        if (!searchRefreshPending) {
            return;
        }
        flushPendingSearchWithoutRefresh();
        refreshCanvas.run();
    }

    private void flushPendingSearchWithoutRefresh() {
        if (!searchRefreshPending) {
            return;
        }
        searchRefreshDebounce.stop();
        searchRefreshPending = false;
        viewModel.applySearchQuery(pendingSearchQuery);
    }

    private void clearPendingSearchRefresh() {
        searchRefreshDebounce.stop();
        searchRefreshPending = false;
        pendingSearchQuery = "";
    }

    private void syncVisualCost(UmlClassVisualCostEstimate estimate) {
        UmlClassVisualCostEstimate safeEstimate = estimate == null
                ? UmlClassVisualCostEstimate.empty(viewModel.activeRenderProfile())
                : estimate;
        visualCostLabel.setText("Costo visual: " + safeEstimate.shortSummary() + "\n" + safeEstimate.recommendation());
        visualCostLabel.setTooltip(new Tooltip(visualCostLabel.getText()));
    }


    private void syncRuntimeMemory(UmlClassRuntimeMemorySnapshot snapshot) {
        UmlClassRuntimeMemorySnapshot safeSnapshot = snapshot == null
                ? UmlClassRuntimeMemorySnapshot.empty()
                : snapshot;
        runtimeMemoryLabel.setText("Memoria: " + safeSnapshot.detailSummary() + "\n" + safeSnapshot.recommendation());
        runtimeMemoryLabel.setTooltip(new Tooltip(runtimeMemoryLabel.getText()));
    }

    private void syncViewSelection(UmlClassDiagramView current) {
        syncingControls = true;
        try {
            if (current == null) {
                viewCombo.getSelectionModel().clearSelection();
            } else if (!current.equals(viewCombo.getSelectionModel().getSelectedItem())) {
                viewCombo.getSelectionModel().select(current);
            }
        } finally {
            syncingControls = false;
        }
    }

    private void syncModuleSelection(UmlModuleGroup current) {
        syncingControls = true;
        try {
            if (current == null) {
                moduleList.getSelectionModel().clearSelection();
            } else if (!current.equals(moduleList.getSelectionModel().getSelectedItem())) {
                moduleList.getSelectionModel().select(current);
            }
        } finally { syncingControls = false; }
    }

    private void syncClassSelection(UmlClassNode current) {
        syncingControls = true;
        try {
            if (current == null) {
                classList.getSelectionModel().clearSelection();
            } else if (!current.equals(classList.getSelectionModel().getSelectedItem())) {
                classList.getSelectionModel().select(current);
            }
        } finally { syncingControls = false; }
    }

    private void syncMemberSelection(UmlClassMember current) {
        syncingControls = true;
        try {
            if (current == null) {
                memberList.getSelectionModel().clearSelection();
            } else if (!current.equals(memberList.getSelectionModel().getSelectedItem())) {
                memberList.getSelectionModel().select(current);
            }
        } finally { syncingControls = false; }
    }

    private Label sublabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("uml-class-subsection-title");
        label.setTooltip(new Tooltip(text));
        return label;
    }

    private static final class ViewCell extends ListCell<UmlClassDiagramView> {
        @Override
        protected void updateItem(UmlClassDiagramView item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setTooltip(null);
                return;
            }
            setText(item.displayName());
            setTooltip(new Tooltip(item.displayName() + "\n" + item.description()));
        }
    }

    private static final class ModuleCell extends ListCell<UmlModuleGroup> {
        @Override
        protected void updateItem(UmlModuleGroup item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
                setTooltip(null);
                return;
            }
            VBox graphic = new VBox(3);
            graphic.getStyleClass().add("uml-class-structure-item");
            Label title = new Label("▣ " + item.displayName());
            title.getStyleClass().add("uml-class-structure-item-title");
            title.setWrapText(true);
            Label path = new Label(item.path().isBlank() ? "Sin ruta/carpeta" : item.path());
            path.getStyleClass().add("uml-class-structure-item-meta");
            path.setWrapText(true);
            graphic.getChildren().addAll(title, path);
            setText(null);
            setGraphic(graphic);
            setTooltip(new Tooltip(item.displayName() + "\n" + path.getText()));
        }
    }

    private static final class ClassCell extends ListCell<UmlClassNode> {
        private final UmlClassDiagramViewModel viewModel;

        private ClassCell(UmlClassDiagramViewModel viewModel) {
            this.viewModel = viewModel;
        }

        @Override
        protected void updateItem(UmlClassNode item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
                setTooltip(null);
                return;
            }
            VBox graphic = new VBox(3);
            graphic.getStyleClass().add("uml-class-structure-item");
            Label title = new Label(item.kind().displayName() + " · " + item.displayName());
            title.getStyleClass().add("uml-class-structure-item-title");
            title.setWrapText(true);
            Label module = new Label(viewModel.moduleLabel(item.moduleId()));
            module.getStyleClass().add("uml-class-structure-item-meta");
            module.setWrapText(true);
            graphic.getChildren().addAll(title, module);
            setText(null);
            setGraphic(graphic);
            setTooltip(new Tooltip(item.displayName() + "\n" + module.getText()));
        }
    }

    private static final class MemberCell extends ListCell<UmlClassMember> {
        @Override
        protected void updateItem(UmlClassMember item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
                setTooltip(null);
                return;
            }
            String text = item.kind().displayName() + " · " + item.displayText();
            setText(text);
            setTooltip(new Tooltip(text));
        }
    }
}
