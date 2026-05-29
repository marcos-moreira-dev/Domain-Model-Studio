package com.marcosmoreira.domainmodelstudio.presentation.modulemap;

import com.marcosmoreira.domainmodelstudio.presentation.workbench.WorkbenchPanelSupport;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.DependencyKind;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleDependency;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleKind;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleNode;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleStatus;
import com.marcosmoreira.domainmodelstudio.presentation.sidedock.SideDockCollectionSizingPolicy;
import java.util.Objects;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/** Panel derecho de propiedades del mapa de módulos. */
final class ModuleMapPropertiesPanel {

    private final ModuleMapViewModel viewModel;
    private final Runnable refreshCanvas;
    private final ScrollPane root;
    private final TableView<ModuleDependency> dependencyTable = new TableView<>();
    private final TextField moduleNameField = new TextField();
    private final ComboBox<ModuleNode> parentCombo = new ComboBox<>();
    private final ComboBox<ModuleKind> kindCombo = new ComboBox<>();
    private final ComboBox<ModuleStatus> statusCombo = new ComboBox<>();
    private final TextField responsibilityField = new TextField();
    private final TextArea moduleDescriptionArea = new TextArea();
    private final TextField tagsField = new TextField();
    private final TextArea moduleNotesArea = new TextArea();
    private final ComboBox<ModuleNode> dependencySourceCombo = new ComboBox<>();
    private final ComboBox<ModuleNode> dependencyTargetCombo = new ComboBox<>();
    private final ComboBox<DependencyKind> dependencyKindCombo = new ComboBox<>();
    private final TextArea dependencyDescriptionArea = new TextArea();
    private final TextArea dependencyNotesArea = new TextArea();

    ModuleMapPropertiesPanel(ModuleMapViewModel viewModel, Runnable refreshCanvas) {
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
        this.refreshCanvas = refreshCanvas == null ? () -> { } : refreshCanvas;
        VBox content = buildContent();
        this.root = new ScrollPane(content);
        configureRoot();
        bindViewModel();
    }

    Parent root() {
        return root;
    }

    private VBox buildContent() {
        VBox content = new VBox(12);
        content.getStyleClass().addAll("module-map-properties", "diagram-workbench-panel-content");
        content.setPadding(new Insets(10));
        content.getChildren().addAll(buildModuleForm(), buildDependencyPanel(), buildDependencyForm());
        return content;
    }

    private void configureRoot() {
        WorkbenchPanelSupport.configurePropertiesScroll(root, "module-map-properties-scroll");
    }

    private Parent buildModuleForm() {
        VBox box = section("Módulo seleccionado");
        parentCombo.setItems(viewModel.rootModules());
        parentCombo.setCellFactory(ignored -> moduleCell());
        parentCombo.setButtonCell(moduleCell());
        kindCombo.getItems().setAll(ModuleKind.values());
        statusCombo.getItems().setAll(ModuleStatus.values());
        configureArea(moduleDescriptionArea, 3);
        configureArea(moduleNotesArea, 2);

        GridPane grid = grid();
        int row = 0;
        addRow(grid, row++, "Nombre", moduleNameField);
        addRow(grid, row++, "Módulo padre", parentCombo);
        addRow(grid, row++, "Tipo", kindCombo);
        addRow(grid, row++, "Estado", statusCombo);
        addRow(grid, row++, "Responsabilidad", responsibilityField);
        addRow(grid, row++, "Descripción", moduleDescriptionArea);
        addRow(grid, row++, "Etiquetas", tagsField);
        addRow(grid, row++, "Notas", moduleNotesArea);

        HBox actions = new HBox(8);
        Button clearParent = button("Sin padre", "Convertir el módulo seleccionado en módulo principal");
        clearParent.setOnAction(event -> parentCombo.setValue(null));
        Button apply = button("Aplicar", "Actualizar datos del módulo seleccionado");
        apply.setOnAction(event -> applyModuleChanges());
        actions.getChildren().addAll(clearParent, apply);
        box.getChildren().addAll(grid, actions);
        return box;
    }

    private Parent buildDependencyPanel() {
        VBox panel = section("Dependencias");
        dependencyTable.setItems(viewModel.dependencies());
        dependencyTable.getStyleClass().add("module-map-dependency-table");
        dependencyTable.getColumns().add(column("Origen", dependency -> viewModel.moduleLabel(dependency.sourceModuleId())));
        dependencyTable.getColumns().add(column("Tipo", dependency -> dependency.kind().displayName()));
        dependencyTable.getColumns().add(column("Destino", dependency -> viewModel.moduleLabel(dependency.targetModuleId())));
        dependencyTable.getColumns().add(column("Descripción", ModuleDependency::description));
        SideDockCollectionSizingPolicy.configureTableView(dependencyTable);
        panel.getChildren().add(dependencyTable);
        return panel;
    }

    private Parent buildDependencyForm() {
        VBox box = section("Dependencia seleccionada");
        dependencySourceCombo.setItems(viewModel.modules());
        dependencyTargetCombo.setItems(viewModel.modules());
        dependencySourceCombo.setCellFactory(ignored -> moduleCell());
        dependencySourceCombo.setButtonCell(moduleCell());
        dependencyTargetCombo.setCellFactory(ignored -> moduleCell());
        dependencyTargetCombo.setButtonCell(moduleCell());
        dependencyKindCombo.getItems().setAll(DependencyKind.values());
        configureArea(dependencyDescriptionArea, 3);
        configureArea(dependencyNotesArea, 2);

        GridPane grid = grid();
        int row = 0;
        addRow(grid, row++, "Origen", dependencySourceCombo);
        addRow(grid, row++, "Destino", dependencyTargetCombo);
        addRow(grid, row++, "Tipo", dependencyKindCombo);
        addRow(grid, row++, "Descripción", dependencyDescriptionArea);
        addRow(grid, row++, "Notas", dependencyNotesArea);

        Button apply = button("Aplicar", "Actualizar datos de la dependencia seleccionada");
        apply.setOnAction(event -> applyDependencyChanges());
        box.getChildren().addAll(grid, apply);
        return box;
    }

    private void bindViewModel() {
        dependencyTable.getSelectionModel().selectedItemProperty().addListener((observable, previous, current) -> {
            viewModel.selectedDependencyProperty().set(current);
            if (current != null) {
                viewModel.selectedModuleProperty().set(null);
            }
            refreshCanvas.run();
        });
        viewModel.selectedModuleProperty().addListener((observable, previous, current) -> {
            if (current != null) {
                dependencyTable.getSelectionModel().clearSelection();
            }
            populateModuleForm(current);
        });
        viewModel.selectedDependencyProperty().addListener((observable, previous, current) -> {
            if (current == null) {
                dependencyTable.getSelectionModel().clearSelection();
            } else if (dependencyTable.getSelectionModel().getSelectedItem() != current) {
                dependencyTable.getSelectionModel().select(current);
            }
            populateDependencyForm(current);
        });
    }

    private void applyModuleChanges() {
        viewModel.applyModuleChanges(
                moduleNameField.getText(),
                parentCombo.getValue(),
                kindCombo.getValue(),
                statusCombo.getValue(),
                responsibilityField.getText(),
                moduleDescriptionArea.getText(),
                tagsField.getText(),
                moduleNotesArea.getText());
        refreshCanvas.run();
    }

    private void applyDependencyChanges() {
        viewModel.applyDependencyChanges(
                dependencySourceCombo.getValue(),
                dependencyTargetCombo.getValue(),
                dependencyKindCombo.getValue(),
                dependencyDescriptionArea.getText(),
                dependencyNotesArea.getText());
        refreshCanvas.run();
    }

    private void populateModuleForm(ModuleNode module) {
        if (module == null) {
            clearModuleForm();
            return;
        }
        moduleNameField.setText(module.displayName());
        parentCombo.setValue(viewModel.modules().stream()
                .filter(candidate -> candidate.id().equals(module.parentId()))
                .findFirst()
                .orElse(null));
        kindCombo.setValue(module.kind());
        statusCombo.setValue(module.status());
        responsibilityField.setText(module.responsibility());
        moduleDescriptionArea.setText(module.description());
        tagsField.setText(module.tags().stream().collect(Collectors.joining(", ")));
        moduleNotesArea.setText(module.notes());
    }

    private void populateDependencyForm(ModuleDependency dependency) {
        if (dependency == null) {
            clearDependencyForm();
            return;
        }
        dependencySourceCombo.setValue(moduleById(dependency.sourceModuleId()));
        dependencyTargetCombo.setValue(moduleById(dependency.targetModuleId()));
        dependencyKindCombo.setValue(dependency.kind());
        dependencyDescriptionArea.setText(dependency.description());
        dependencyNotesArea.setText(dependency.notes());
    }

    private ModuleNode moduleById(String moduleId) {
        return viewModel.modules().stream()
                .filter(module -> module.id().equals(moduleId))
                .findFirst()
                .orElse(null);
    }

    private void clearModuleForm() {
        moduleNameField.clear();
        parentCombo.setValue(null);
        kindCombo.setValue(ModuleKind.MAIN);
        statusCombo.setValue(ModuleStatus.PLANNED);
        responsibilityField.clear();
        moduleDescriptionArea.clear();
        tagsField.clear();
        moduleNotesArea.clear();
    }

    private void clearDependencyForm() {
        dependencySourceCombo.setValue(null);
        dependencyTargetCombo.setValue(null);
        dependencyKindCombo.setValue(DependencyKind.USES);
        dependencyDescriptionArea.clear();
        dependencyNotesArea.clear();
    }

    private ListCell<ModuleNode> moduleCell() {
        return new ListCell<>() {
            @Override
            protected void updateItem(ModuleNode item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "Sin padre" : item.displayName());
            }
        };
    }

    private VBox section(String title) {
        return WorkbenchPanelSupport.section("module-map-section", "module-map-section-title", title);
    }

    private TableColumn<ModuleDependency, String> column(
            String title,
            java.util.function.Function<ModuleDependency, String> extractor
    ) {
        TableColumn<ModuleDependency, String> column = new TableColumn<>(title);
        column.setCellValueFactory(cell -> new SimpleStringProperty(extractor.apply(cell.getValue())));
        column.setPrefWidth(125);
        return column;
    }

    private GridPane grid() {
        return WorkbenchPanelSupport.grid();
    }

    private void addRow(GridPane grid, int row, String labelText, javafx.scene.Node input) {
        WorkbenchPanelSupport.addRow(grid, row, "module-map-field-label", labelText, input);
    }

    private Button button(String text, String tooltip) {
        return WorkbenchPanelSupport.button("module-map-action-button", text, tooltip);
    }

    private void configureArea(TextArea area, int rows) {
        WorkbenchPanelSupport.configureArea(area, rows);
    }
}
