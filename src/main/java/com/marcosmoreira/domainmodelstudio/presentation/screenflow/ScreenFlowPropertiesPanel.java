package com.marcosmoreira.domainmodelstudio.presentation.screenflow;

import com.marcosmoreira.domainmodelstudio.presentation.workbench.WorkbenchPanelSupport;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenKind;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenNode;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenTransition;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenTransitionKind;
import java.util.Objects;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/** Panel derecho de propiedades del flujo de pantallas. */
final class ScreenFlowPropertiesPanel {

    private final ScreenFlowViewModel viewModel;
    private final Runnable refreshCanvas;
    private final ScrollPane root;
    private final TextField screenName = new TextField();
    private final ComboBox<ScreenKind> screenKind = new ComboBox<>();
    private final TextField screenModule = new TextField();
    private final TextField screenRoute = new TextField();
    private final TextArea screenPurpose = new TextArea();
    private final TextArea screenNotes = new TextArea();
    private final ComboBox<ScreenNode> transitionSource = new ComboBox<>();
    private final ComboBox<ScreenNode> transitionTarget = new ComboBox<>();
    private final ComboBox<ScreenTransitionKind> transitionKind = new ComboBox<>();
    private final TextField transitionTrigger = new TextField();
    private final TextField transitionCondition = new TextField();
    private final TextArea transitionNotes = new TextArea();

    ScreenFlowPropertiesPanel(ScreenFlowViewModel viewModel, Runnable refreshCanvas) {
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
        this.refreshCanvas = refreshCanvas == null ? () -> { } : refreshCanvas;
        VBox content = buildContent();
        this.root = new ScrollPane(content);
        configureRoot();
        installComboValues();
        bindViewModel();
    }

    Parent root() {
        return root;
    }

    private VBox buildContent() {
        VBox content = new VBox(12);
        content.getStyleClass().addAll("screen-flow-properties", "diagram-workbench-panel-content");
        content.setPadding(new Insets(10));
        content.getChildren().addAll(buildScreenForm(), buildTransitionForm());
        return content;
    }

    private void configureRoot() {
        WorkbenchPanelSupport.configurePropertiesScroll(root, "screen-flow-properties-scroll");
    }

    private Parent buildScreenForm() {
        VBox box = section("Pantalla seleccionada");
        configureArea(screenPurpose, 3);
        configureArea(screenNotes, 2);

        GridPane grid = grid();
        int row = 0;
        addRow(grid, row++, "Nombre", screenName);
        addRow(grid, row++, "Tipo", screenKind);
        addRow(grid, row++, "Módulo", screenModule);
        addRow(grid, row++, "Ruta", screenRoute);
        addRow(grid, row++, "Propósito", screenPurpose);
        addRow(grid, row++, "Notas", screenNotes);

        Button apply = button("Aplicar pantalla", "Actualizar datos de la pantalla seleccionada");
        apply.setOnAction(event -> applyScreenChanges());
        box.getChildren().addAll(grid, new HBox(8, apply));
        return box;
    }

    private Parent buildTransitionForm() {
        VBox box = section("Transición seleccionada");
        configureArea(transitionNotes, 2);

        GridPane grid = grid();
        int row = 0;
        addRow(grid, row++, "Origen", transitionSource);
        addRow(grid, row++, "Destino", transitionTarget);
        addRow(grid, row++, "Tipo", transitionKind);
        addRow(grid, row++, "Acción", transitionTrigger);
        addRow(grid, row++, "Condición", transitionCondition);
        addRow(grid, row++, "Notas", transitionNotes);

        Button apply = button("Aplicar navegación", "Actualizar datos de la transición seleccionada");
        apply.setOnAction(event -> applyTransitionChanges());
        box.getChildren().addAll(grid, apply);
        return box;
    }

    private void installComboValues() {
        screenKind.getItems().setAll(ScreenKind.values());
        screenKind.setValue(ScreenKind.OTHER);
        transitionKind.getItems().setAll(ScreenTransitionKind.values());
        transitionKind.setValue(ScreenTransitionKind.NAVIGATES);
        transitionSource.setItems(viewModel.screens());
        transitionTarget.setItems(viewModel.screens());
        transitionSource.setCellFactory(ignored -> screenCell());
        transitionSource.setButtonCell(screenCell());
        transitionTarget.setCellFactory(ignored -> screenCell());
        transitionTarget.setButtonCell(screenCell());
    }

    private void bindViewModel() {
        viewModel.selectedScreenProperty().addListener((observable, previous, current) -> populateScreen(current));
        viewModel.selectedTransitionProperty().addListener((observable, previous, current) -> populateTransition(current));
        populateScreen(viewModel.selectedScreenProperty().get());
        populateTransition(viewModel.selectedTransitionProperty().get());
    }

    private void applyScreenChanges() {
        viewModel.applyScreenChanges(
                screenName.getText(),
                screenKind.getValue(),
                screenModule.getText(),
                screenRoute.getText(),
                screenPurpose.getText(),
                screenNotes.getText());
        refreshCanvas.run();
    }

    private void applyTransitionChanges() {
        viewModel.applyTransitionChanges(
                transitionSource.getValue(),
                transitionTarget.getValue(),
                transitionKind.getValue(),
                transitionTrigger.getText(),
                transitionCondition.getText(),
                transitionNotes.getText());
        refreshCanvas.run();
    }

    private void populateScreen(ScreenNode screen) {
        if (screen == null) {
            screenName.clear();
            screenKind.setValue(ScreenKind.OTHER);
            screenModule.clear();
            screenRoute.clear();
            screenPurpose.clear();
            screenNotes.clear();
            return;
        }
        screenName.setText(screen.displayName());
        screenKind.setValue(screen.kind());
        screenModule.setText(screen.moduleName());
        screenRoute.setText(screen.route());
        screenPurpose.setText(screen.purpose());
        screenNotes.setText(screen.notes());
    }

    private void populateTransition(ScreenTransition transition) {
        if (transition == null) {
            transitionSource.setValue(null);
            transitionTarget.setValue(null);
            transitionKind.setValue(ScreenTransitionKind.NAVIGATES);
            transitionTrigger.clear();
            transitionCondition.clear();
            transitionNotes.clear();
            return;
        }
        transitionSource.setValue(findScreen(transition.sourceScreenId()));
        transitionTarget.setValue(findScreen(transition.targetScreenId()));
        transitionKind.setValue(transition.kind());
        transitionTrigger.setText(transition.trigger());
        transitionCondition.setText(transition.condition());
        transitionNotes.setText(transition.notes());
    }

    private ScreenNode findScreen(String id) {
        return viewModel.screens().stream()
                .filter(screen -> screen.id().equals(id))
                .findFirst()
                .orElse(null);
    }

    private ListCell<ScreenNode> screenCell() {
        return new ListCell<>() {
            @Override
            protected void updateItem(ScreenNode item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "Sin pantalla" : item.displayName());
            }
        };
    }

    private VBox section(String title) {
        return WorkbenchPanelSupport.section("screen-flow-section", "screen-flow-section-title", title);
    }

    private GridPane grid() {
        return WorkbenchPanelSupport.grid();
    }

    private void addRow(GridPane grid, int row, String labelText, javafx.scene.Node input) {
        WorkbenchPanelSupport.addRow(grid, row, "screen-flow-field-label", labelText, input);
    }

    private Button button(String text, String tooltip) {
        return WorkbenchPanelSupport.button("screen-flow-action-button", text, tooltip);
    }

    private void configureArea(TextArea area, int rows) {
        WorkbenchPanelSupport.configureArea(area, rows);
    }
}
