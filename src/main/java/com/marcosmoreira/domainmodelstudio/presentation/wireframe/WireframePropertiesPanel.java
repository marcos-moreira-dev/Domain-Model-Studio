package com.marcosmoreira.domainmodelstudio.presentation.wireframe;

import com.marcosmoreira.domainmodelstudio.presentation.workbench.WorkbenchPanelSupport;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeComponent;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeComponentKind;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeScreen;
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

/** Panel derecho de propiedades de wireframes administrativos. */
final class WireframePropertiesPanel {

    private final WireframeViewModel viewModel;
    private final Runnable refreshCanvas;
    private final ScrollPane root;
    private final TextField screenName = new TextField();
    private final TextField screenModule = new TextField();
    private final TextArea screenPurpose = new TextArea();
    private final TextArea screenNotes = new TextArea();
    private final TextField screenWidth = new TextField();
    private final TextField screenHeight = new TextField();
    private final ComboBox<WireframeScreen> componentScreen = new ComboBox<>();
    private final ComboBox<WireframeComponentKind> componentKind = new ComboBox<>();
    private final TextField componentName = new TextField();
    private final TextField componentOrder = new TextField();
    private final TextField componentBinding = new TextField();
    private final TextField componentBehavior = new TextField();
    private final TextArea componentNotes = new TextArea();
    private final TextField componentWidth = new TextField();
    private final TextField componentHeight = new TextField();

    WireframePropertiesPanel(WireframeViewModel viewModel, Runnable refreshCanvas) {
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
        content.getStyleClass().addAll("wireframe-properties", "diagram-workbench-panel-content");
        content.setPadding(new Insets(10));
        content.getChildren().addAll(buildScreenForm(), buildComponentForm());
        return content;
    }

    private void configureRoot() {
        WorkbenchPanelSupport.configurePropertiesScroll(root, "wireframe-properties-scroll");
    }

    private Parent buildScreenForm() {
        VBox box = section("Pantalla seleccionada");
        configureArea(screenPurpose, 3);
        configureArea(screenNotes, 2);
        screenWidth.setPrefColumnCount(6);
        screenHeight.setPrefColumnCount(6);

        GridPane grid = grid();
        int row = 0;
        addRow(grid, row++, "Nombre", screenName);
        addRow(grid, row++, "Módulo", screenModule);
        addRow(grid, row++, "Propósito", screenPurpose);
        addRow(grid, row++, "Notas", screenNotes);
        addRow(grid, row++, "Ancho", screenWidth);
        addRow(grid, row++, "Alto", screenHeight);

        Button apply = button("Aplicar pantalla", "Actualizar datos de la pantalla seleccionada");
        apply.setOnAction(event -> applyScreenChanges());
        Button fit = button("Ajustar al contenido", "Ajustar manualmente la pantalla a sus componentes internos");
        fit.setOnAction(event -> fitScreenToContent());
        box.getChildren().addAll(grid, new HBox(8, apply, fit));
        return box;
    }

    private Parent buildComponentForm() {
        VBox box = section("Componente seleccionado");
        configureArea(componentNotes, 2);
        componentWidth.setPrefColumnCount(6);
        componentHeight.setPrefColumnCount(6);

        GridPane grid = grid();
        int row = 0;
        addRow(grid, row++, "Pantalla", componentScreen);
        addRow(grid, row++, "Tipo", componentKind);
        addRow(grid, row++, "Nombre", componentName);
        addRow(grid, row++, "Orden", componentOrder);
        addRow(grid, row++, "Dato", componentBinding);
        addRow(grid, row++, "Comportamiento", componentBehavior);
        addRow(grid, row++, "Notas", componentNotes);
        addRow(grid, row++, "Ancho", componentWidth);
        addRow(grid, row++, "Alto", componentHeight);

        Button apply = button("Aplicar componente", "Actualizar datos del componente seleccionado");
        apply.setOnAction(event -> applyComponentChanges());
        box.getChildren().addAll(grid, apply);
        return box;
    }

    private void installComboValues() {
        componentKind.getItems().setAll(WireframeComponentKind.values());
        componentKind.setValue(WireframeComponentKind.SECTION);
        componentScreen.setItems(viewModel.screens());
        componentScreen.setCellFactory(ignored -> screenCell());
        componentScreen.setButtonCell(screenCell());
    }

    private void bindViewModel() {
        viewModel.selectedScreenProperty().addListener((observable, previous, current) -> populateScreen(current));
        viewModel.selectedComponentProperty().addListener((observable, previous, current) -> populateComponent(current));
        populateScreen(viewModel.selectedScreenProperty().get());
        populateComponent(viewModel.selectedComponentProperty().get());
    }

    private void applyScreenChanges() {
        viewModel.applyScreenChanges(
                screenName.getText(),
                screenModule.getText(),
                screenPurpose.getText(),
                screenNotes.getText());
        WireframeScreen selected = viewModel.selectedScreenProperty().get();
        if (selected != null) {
            viewModel.resizeScreenTo(selected.id(), parseSize(screenWidth.getText(), viewModel.layoutForScreen(selected).width()), parseSize(screenHeight.getText(), viewModel.layoutForScreen(selected).height()));
        }
        refreshCanvas.run();
    }

    private void fitScreenToContent() {
        viewModel.fitSelectedScreenToContent();
        refreshCanvas.run();
    }

    private void applyComponentChanges() {
        viewModel.applyComponentChanges(
                componentScreen.getValue(),
                componentKind.getValue(),
                componentName.getText(),
                parseOrder(componentOrder.getText()),
                componentBinding.getText(),
                componentBehavior.getText(),
                componentNotes.getText());
        WireframeComponent selected = viewModel.selectedComponentProperty().get();
        if (selected != null) {
            viewModel.resizeComponentTo(selected.id(), parseSize(componentWidth.getText(), viewModel.layoutForComponent(selected).width()), parseSize(componentHeight.getText(), viewModel.layoutForComponent(selected).height()));
        }
        refreshCanvas.run();
    }

    private void populateScreen(WireframeScreen screen) {
        if (screen == null) {
            screenName.clear();
            screenModule.clear();
            screenPurpose.clear();
            screenNotes.clear();
            screenWidth.clear();
            screenHeight.clear();
            return;
        }
        screenName.setText(screen.displayName());
        screenModule.setText(screen.moduleName());
        screenPurpose.setText(screen.purpose());
        screenNotes.setText(screen.notes());
        setSizeFields(screenWidth, screenHeight, viewModel.layoutForScreen(screen));
    }

    private void populateComponent(WireframeComponent component) {
        if (component == null) {
            componentScreen.setValue(null);
            componentKind.setValue(WireframeComponentKind.SECTION);
            componentName.clear();
            componentOrder.clear();
            componentBinding.clear();
            componentBehavior.clear();
            componentNotes.clear();
            componentWidth.clear();
            componentHeight.clear();
            return;
        }
        componentScreen.setValue(findScreen(component.screenId()));
        componentKind.setValue(component.kind());
        componentName.setText(component.displayName());
        componentOrder.setText(Integer.toString(component.orderIndex()));
        componentBinding.setText(component.dataBinding());
        componentBehavior.setText(component.behavior());
        componentNotes.setText(component.notes());
        setSizeFields(componentWidth, componentHeight, viewModel.layoutForComponent(component));
    }

    private WireframeScreen findScreen(String id) {
        return viewModel.screens().stream()
                .filter(screen -> screen.id().equals(id))
                .findFirst()
                .orElse(null);
    }

    private int parseOrder(String value) {
        try {
            return Math.max(0, Integer.parseInt(value == null ? "0" : value.strip()));
        } catch (NumberFormatException exception) {
            return 0;
        }
    }

    private double parseSize(String value, double fallback) {
        try {
            return Math.max(1.0, Double.parseDouble(value == null ? Double.toString(fallback) : value.strip().replace(',', '.')));
        } catch (NumberFormatException exception) {
            return fallback;
        }
    }

    private void setSizeFields(TextField widthField, TextField heightField, com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout layout) {
        widthField.setText(Integer.toString((int) Math.round(layout.width())));
        heightField.setText(Integer.toString((int) Math.round(layout.height())));
    }

    private ListCell<WireframeScreen> screenCell() {
        return new ListCell<>() {
            @Override
            protected void updateItem(WireframeScreen item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "Sin pantalla" : item.displayName());
            }
        };
    }

    private VBox section(String title) {
        return WorkbenchPanelSupport.section("wireframe-section", "wireframe-section-title", title);
    }

    private GridPane grid() {
        return WorkbenchPanelSupport.grid();
    }

    private void addRow(GridPane grid, int row, String labelText, javafx.scene.Node input) {
        WorkbenchPanelSupport.addRow(grid, row, "wireframe-field-label", labelText, input);
    }

    private Button button(String text, String tooltip) {
        return WorkbenchPanelSupport.button("wireframe-action-button", text, tooltip);
    }

    private void configureArea(TextArea area, int rows) {
        WorkbenchPanelSupport.configureArea(area, rows);
    }
}
