package com.marcosmoreira.domainmodelstudio.presentation.datadictionary;

import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryEntity;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryField;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryStatus;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataEntityKind;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.FieldConstraint;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.FieldVisibility;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.LogicalDataType;
import com.marcosmoreira.domainmodelstudio.presentation.workbench.StructuredWorkbenchDescriptor;
import com.marcosmoreira.domainmodelstudio.presentation.workbench.StructuredWorkbenchView;
import com.marcosmoreira.domainmodelstudio.presentation.workspace.WorkspaceKind;
import com.marcosmoreira.domainmodelstudio.presentation.sidedock.SideDockCollectionSizingPolicy;
import java.util.LinkedHashSet;
import java.util.Set;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/** Vista documental editable del diccionario de datos. */
public final class DataDictionaryEditorView {

    private final DataDictionaryViewModel viewModel;
    private final BorderPane root = new BorderPane();
    private final DataDictionaryEntityIndexPanel entityIndexPanel;
    private final Label selectedEntitySummary = new Label();
    private final TabPane outputTabs = new TabPane();
    private final TableView<DataDictionaryField> fieldTable = new TableView<>();
    private final DataDictionaryDocumentPreview documentPreview;
    private final DataDictionaryDocumentForm documentForm;
    private Tab documentTab;
    private Tab tableTab;
    private TitledPane documentPropertiesPane;
    private TitledPane entityPropertiesPane;
    private TitledPane fieldPropertiesPane;
    private Button addFieldButton;
    private Button removeFieldButton;

    private final TextField entityNameField = new TextField();
    private final TextField entityTechnicalField = new TextField();
    private final TextField entityModuleField = new TextField();
    private final ComboBox<DataEntityKind> entityKindCombo = new ComboBox<>();
    private final ComboBox<DataDictionaryStatus> entityStatusCombo = new ComboBox<>();
    private final TextArea entityDescriptionArea = new TextArea();
    private final TextArea entityNotesArea = new TextArea();

    private final TextField fieldNameField = new TextField();
    private final TextField fieldTechnicalField = new TextField();
    private final ComboBox<LogicalDataType> fieldTypeCombo = new ComboBox<>();
    private final TextField fieldPhysicalSuggestionField = new TextField();
    private final TextField fieldForeignKeyField = new TextField();
    private final TextField fieldDefaultField = new TextField();
    private final TextField fieldFormatField = new TextField();
    private final TextField fieldExampleField = new TextField();
    private final TextArea fieldDescriptionArea = new TextArea();
    private final TextArea fieldBusinessRuleArea = new TextArea();
    private final TextArea fieldValidationRuleArea = new TextArea();
    private final TextArea fieldNotesArea = new TextArea();
    private final CheckBox userEditableCheck = new CheckBox("Editable por usuario");
    private final java.util.Map<FieldConstraint, CheckBox> constraintChecks = new java.util.EnumMap<>(FieldConstraint.class);
    private final java.util.Map<FieldVisibility, CheckBox> visibilityChecks = new java.util.EnumMap<>(FieldVisibility.class);

    public DataDictionaryEditorView(DataDictionaryViewModel viewModel) {
        this.viewModel = viewModel;
        this.documentPreview = new DataDictionaryDocumentPreview(viewModel);
        this.documentForm = new DataDictionaryDocumentForm(viewModel);
        this.entityIndexPanel = new DataDictionaryEntityIndexPanel(viewModel, this::focusFieldWorkflow);
        build();
        bindViewModel();
    }

    public Parent getRoot() {
        return root;
    }

    private void build() {
        StructuredWorkbenchDescriptor descriptor = StructuredWorkbenchDescriptor.document(
                WorkspaceKind.DATA_DICTIONARY_DOCUMENT,
                "data-dictionary-root",
                "Diccionario de datos",
                "Documento estructurado de entidades, campos, tipos, reglas y validaciones. No es un ERD físico.",
                "Centro documental con índice de entidades y propiedades editables."
        );
        StructuredWorkbenchView workbench = new StructuredWorkbenchView(
                descriptor,
                buildEntityPanel(),
                buildDocumentPanel(),
                buildPropertiesPanel()
        );
        root.getStyleClass().add("data-dictionary-host");
        root.setCenter(workbench.getRoot());
    }

    private Parent buildEntityPanel() {
        return entityIndexPanel.root();
    }

    private Parent buildDocumentPanel() {
        VBox panel = section("Documento de entrega");
        Label hint = helper("Vista principal del diccionario: primero se lee como documento técnico y luego se corrige desde tablas y propiedades.");
        HBox badges = new HBox(8);
        badges.getStyleClass().add("data-dictionary-output-badges");
        badges.getChildren().addAll(
                badge("PDF formal"),
                badge("Markdown canónico"),
                badge("No es ERD físico")
        );

        ScrollPane documentScroll = new ScrollPane(documentPreview.root());
        documentScroll.setFitToWidth(true);
        documentScroll.getStyleClass().add("data-dictionary-document-scroll");

        outputTabs.getStyleClass().add("data-dictionary-output-tabs");
        documentTab = new Tab("Documento", documentScroll);
        documentTab.setClosable(false);
        tableTab = new Tab("Campos de entidad", buildFieldTablePanel());
        tableTab.setClosable(false);
        outputTabs.getTabs().addAll(documentTab, tableTab);
        VBox.setVgrow(outputTabs, Priority.ALWAYS);

        panel.getChildren().addAll(hint, badges, outputTabs);
        return panel;
    }

    private Parent buildFieldTablePanel() {
        VBox panel = new VBox(8);
        panel.getStyleClass().add("data-dictionary-table-panel");
        panel.setPadding(new Insets(10));
        Label title = new Label("Campos de la entidad seleccionada");
        title.getStyleClass().add("data-dictionary-section-title");
        Label note = helper("Revisa nombre lógico, tipo, restricciones y visibilidad antes de exportar el documento.");
        HBox toolbar = new HBox(8);
        toolbar.getStyleClass().add("data-dictionary-field-toolbar");
        toolbar.setAlignment(Pos.CENTER_LEFT);
        selectedEntitySummary.getStyleClass().add("data-dictionary-selected-entity-summary");
        selectedEntitySummary.setWrapText(true);
        HBox.setHgrow(selectedEntitySummary, Priority.ALWAYS);
        addFieldButton = button("Agregar campo", "Agregar campo a la entidad seleccionada");
        addFieldButton.setOnAction(event -> {
            viewModel.addField();
            focusFieldWorkflow();
        });
        removeFieldButton = button("Eliminar campo", "Eliminar el campo seleccionado");
        removeFieldButton.setOnAction(event -> {
            if (viewModel.selectedFieldProperty().get() != null) {
                viewModel.removeSelected();
                focusFieldWorkflow();
            }
        });
        toolbar.getChildren().addAll(selectedEntitySummary, addFieldButton, removeFieldButton);
        fieldTable.setItems(viewModel.fields());
        fieldTable.getStyleClass().add("data-dictionary-field-table");
        fieldTable.getColumns().add(column("Campo", DataDictionaryField::displayName));
        fieldTable.getColumns().add(column("Tipo", field -> label(field.logicalType())));
        fieldTable.getColumns().add(column("Restricciones", this::constraintSummary));
        fieldTable.getColumns().add(column("Visible", this::visibilitySummary));
        SideDockCollectionSizingPolicy.configureTableView(fieldTable);
        VBox.setVgrow(fieldTable, Priority.ALWAYS);
        panel.getChildren().addAll(title, note, toolbar, fieldTable);
        return panel;
    }

    private Parent buildPropertiesPanel() {
        VBox content = new VBox(12);
        content.getStyleClass().add("data-dictionary-properties");
        content.setPadding(new Insets(10));
        Label note = helper("Inspector documental: los cambios actualizan el documento, no un canvas visual.");
        documentPropertiesPane = DataDictionaryDisclosure.section(
                "Documento",
                "",
                "Portada, introducción y metadatos",
                documentForm.root(),
                true,
                null,
                "data-dictionary-properties-disclosure");
        entityPropertiesPane = DataDictionaryDisclosure.section(
                "Entidad seleccionada",
                "",
                "Nombre, módulo, tipo y estado",
                buildEntityForm(),
                false,
                null,
                "data-dictionary-properties-disclosure");
        fieldPropertiesPane = DataDictionaryDisclosure.section(
                "Campo seleccionado",
                "",
                "Tipo, restricciones, visibilidad y reglas",
                buildFieldForm(),
                false,
                null,
                "data-dictionary-properties-disclosure");
        content.getChildren().addAll(note, documentPropertiesPane, entityPropertiesPane, fieldPropertiesPane);
        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.getStyleClass().addAll("data-dictionary-properties-scroll", "side-dock-content-owns-scroll");
        return scroll;
    }

    private Parent buildEntityForm() {
        VBox box = formBody();
        entityKindCombo.getItems().setAll(DataEntityKind.values());
        entityStatusCombo.getItems().setAll(DataDictionaryStatus.values());
        configureArea(entityDescriptionArea, 3);
        configureArea(entityNotesArea, 2);

        GridPane grid = grid();
        int row = 0;
        addRow(grid, row++, "Nombre", entityNameField);
        addRow(grid, row++, "Nombre técnico", entityTechnicalField);
        addRow(grid, row++, "Módulo", entityModuleField);
        addRow(grid, row++, "Tipo", entityKindCombo);
        addRow(grid, row++, "Estado", entityStatusCombo);
        addRow(grid, row++, "Descripción", entityDescriptionArea);
        addRow(grid, row++, "Notas", entityNotesArea);

        Button apply = button("Aplicar entidad", "Actualizar datos de la entidad seleccionada");
        apply.setOnAction(event -> viewModel.applyEntityChanges(
                entityNameField.getText(),
                entityTechnicalField.getText(),
                entityModuleField.getText(),
                entityKindCombo.getValue(),
                entityStatusCombo.getValue(),
                entityDescriptionArea.getText(),
                entityNotesArea.getText()));
        box.getChildren().addAll(grid, apply);
        return box;
    }

    private Parent buildFieldForm() {
        VBox box = formBody();
        fieldTypeCombo.getItems().setAll(LogicalDataType.values());
        configureArea(fieldDescriptionArea, 3);
        configureArea(fieldBusinessRuleArea, 2);
        configureArea(fieldValidationRuleArea, 2);
        configureArea(fieldNotesArea, 2);

        GridPane grid = grid();
        int row = 0;
        addRow(grid, row++, "Nombre", fieldNameField);
        addRow(grid, row++, "Nombre técnico", fieldTechnicalField);
        addRow(grid, row++, "Tipo lógico", fieldTypeCombo);
        addRow(grid, row++, "Tipo sugerido", fieldPhysicalSuggestionField);
        addRow(grid, row++, "Referencia", fieldForeignKeyField);
        addRow(grid, row++, "Valor por defecto", fieldDefaultField);
        addRow(grid, row++, "Formato", fieldFormatField);
        addRow(grid, row++, "Ejemplo", fieldExampleField);
        addRow(grid, row++, "Descripción", fieldDescriptionArea);
        addRow(grid, row++, "Regla negocio", fieldBusinessRuleArea);
        addRow(grid, row++, "Validación", fieldValidationRuleArea);
        addRow(grid, row++, "Notas", fieldNotesArea);

        VBox constraints = checkboxGroup("Restricciones", FieldConstraint.values(), constraintChecks);
        VBox visibility = checkboxGroup("Visible en", FieldVisibility.values(), visibilityChecks);
        userEditableCheck.setSelected(true);

        Button apply = button("Aplicar campo", "Actualizar datos del campo seleccionado");
        apply.setOnAction(event -> viewModel.applyFieldChanges(
                fieldNameField.getText(),
                fieldTechnicalField.getText(),
                fieldTypeCombo.getValue(),
                fieldPhysicalSuggestionField.getText(),
                selectedConstraints(),
                fieldForeignKeyField.getText(),
                fieldDefaultField.getText(),
                fieldFormatField.getText(),
                fieldDescriptionArea.getText(),
                fieldBusinessRuleArea.getText(),
                fieldValidationRuleArea.getText(),
                fieldExampleField.getText(),
                selectedVisibility(),
                userEditableCheck.isSelected(),
                fieldNotesArea.getText()));
        box.getChildren().addAll(grid, constraints, visibility, userEditableCheck, apply);
        return box;
    }

    private Label helper(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("data-dictionary-helper");
        label.setWrapText(true);
        return label;
    }

    private Label badge(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("data-dictionary-badge");
        label.setAlignment(Pos.CENTER);
        return label;
    }

    private void bindViewModel() {
        viewModel.entities().addListener((ListChangeListener<DataDictionaryEntity>) change -> {
            documentForm.refresh(viewModel.currentDocument());
            documentPreview.refresh();
            entityIndexPanel.refresh();
            refreshSelectionControls();
        });
        viewModel.fields().addListener((ListChangeListener<DataDictionaryField>) change -> {
            documentForm.refresh(viewModel.currentDocument());
            documentPreview.refresh();
            entityIndexPanel.refresh();
            refreshSelectionControls();
        });
        viewModel.selectedEntityProperty().addListener((observable, previous, current) -> {
            refreshEntityForm(current);
            documentPreview.refresh();
            entityIndexPanel.refresh();
            refreshSelectionControls();
            updatePropertyPanesForSelection();
        });
        fieldTable.getSelectionModel().selectedItemProperty().addListener((observable, previous, current) -> {
            viewModel.selectedFieldProperty().set(current);
            refreshFieldForm(current);
            documentPreview.refresh();
            refreshSelectionControls();
            updatePropertyPanesForSelection();
        });
        viewModel.selectedFieldProperty().addListener((observable, previous, current) -> {
            if (fieldTable.getSelectionModel().getSelectedItem() != current) {
                fieldTable.getSelectionModel().select(current);
            }
            refreshFieldForm(current);
            documentPreview.refresh();
            refreshSelectionControls();
            updatePropertyPanesForSelection();
        });
        documentForm.refresh(viewModel.currentDocument());
        documentPreview.refresh();
        entityIndexPanel.refresh();
        refreshSelectionControls();
        updatePropertyPanesForSelection();
    }

    private void refreshEntityForm(DataDictionaryEntity entity) {
        boolean disabled = entity == null;
        setEntityControlsDisabled(disabled);
        if (entity == null) {
            entityNameField.clear();
            entityTechnicalField.clear();
            entityModuleField.clear();
            entityDescriptionArea.clear();
            entityNotesArea.clear();
            entityKindCombo.setValue(DataEntityKind.MAIN);
            entityStatusCombo.setValue(DataDictionaryStatus.DRAFT);
            return;
        }
        entityNameField.setText(entity.displayName());
        entityTechnicalField.setText(entity.technicalName());
        entityModuleField.setText(entity.moduleName());
        entityKindCombo.setValue(entity.kind());
        entityStatusCombo.setValue(entity.status());
        entityDescriptionArea.setText(entity.description());
        entityNotesArea.setText(entity.notes());
    }

    private void refreshFieldForm(DataDictionaryField field) {
        boolean disabled = field == null;
        setFieldControlsDisabled(disabled);
        if (field == null) {
            fieldNameField.clear();
            fieldTechnicalField.clear();
            fieldTypeCombo.setValue(LogicalDataType.SHORT_TEXT);
            fieldPhysicalSuggestionField.clear();
            fieldForeignKeyField.clear();
            fieldDefaultField.clear();
            fieldFormatField.clear();
            fieldExampleField.clear();
            fieldDescriptionArea.clear();
            fieldBusinessRuleArea.clear();
            fieldValidationRuleArea.clear();
            fieldNotesArea.clear();
            constraintChecks.values().forEach(check -> check.setSelected(false));
            visibilityChecks.values().forEach(check -> check.setSelected(false));
            userEditableCheck.setSelected(true);
            return;
        }
        fieldNameField.setText(field.displayName());
        fieldTechnicalField.setText(field.technicalName());
        fieldTypeCombo.setValue(field.logicalType());
        fieldPhysicalSuggestionField.setText(field.physicalTypeSuggestion());
        fieldForeignKeyField.setText(field.foreignKeyReference());
        fieldDefaultField.setText(field.defaultValue());
        fieldFormatField.setText(field.expectedFormat());
        fieldExampleField.setText(field.example());
        fieldDescriptionArea.setText(field.description());
        fieldBusinessRuleArea.setText(field.businessRule());
        fieldValidationRuleArea.setText(field.validationRule());
        fieldNotesArea.setText(field.notes());
        constraintChecks.forEach((constraint, check) -> check.setSelected(field.constraints().contains(constraint)));
        visibilityChecks.forEach((visibility, check) -> check.setSelected(field.visibility().contains(visibility)));
        userEditableCheck.setSelected(field.userEditable());
    }

    private void refreshSelectionControls() {
        DataDictionaryEntity entity = viewModel.selectedEntityProperty().get();
        DataDictionaryField field = viewModel.selectedFieldProperty().get();
        if (entity == null) {
            selectedEntitySummary.setText("Selecciona una entidad para revisar o agregar campos.");
        } else {
            selectedEntitySummary.setText(entity.displayName() + " · " + entity.fieldCount() + " campos · "
                    + DataDictionaryLabels.optional(entity.moduleName()));
        }
        if (addFieldButton != null) {
            addFieldButton.setDisable(entity == null);
        }
        if (removeFieldButton != null) {
            removeFieldButton.setDisable(field == null);
        }
    }

    private void updatePropertyPanesForSelection() {
        if (documentPropertiesPane == null || entityPropertiesPane == null || fieldPropertiesPane == null) {
            return;
        }
        boolean hasEntity = viewModel.selectedEntityProperty().get() != null;
        boolean hasField = viewModel.selectedFieldProperty().get() != null;
        documentPropertiesPane.setExpanded(!hasEntity);
        entityPropertiesPane.setExpanded(hasEntity);
        fieldPropertiesPane.setExpanded(hasField);
    }

    private void focusFieldWorkflow() {
        if (outputTabs != null && tableTab != null) {
            outputTabs.getSelectionModel().select(tableTab);
        }
        if (viewModel.selectedFieldProperty().get() != null && fieldPropertiesPane != null) {
            fieldPropertiesPane.setExpanded(true);
        } else if (viewModel.selectedEntityProperty().get() != null && entityPropertiesPane != null) {
            entityPropertiesPane.setExpanded(true);
        }
    }

    private void setEntityControlsDisabled(boolean disabled) {
        entityNameField.setDisable(disabled);
        entityTechnicalField.setDisable(disabled);
        entityModuleField.setDisable(disabled);
        entityKindCombo.setDisable(disabled);
        entityStatusCombo.setDisable(disabled);
        entityDescriptionArea.setDisable(disabled);
        entityNotesArea.setDisable(disabled);
    }

    private void setFieldControlsDisabled(boolean disabled) {
        fieldNameField.setDisable(disabled);
        fieldTechnicalField.setDisable(disabled);
        fieldTypeCombo.setDisable(disabled);
        fieldPhysicalSuggestionField.setDisable(disabled);
        fieldForeignKeyField.setDisable(disabled);
        fieldDefaultField.setDisable(disabled);
        fieldFormatField.setDisable(disabled);
        fieldExampleField.setDisable(disabled);
        fieldDescriptionArea.setDisable(disabled);
        fieldBusinessRuleArea.setDisable(disabled);
        fieldValidationRuleArea.setDisable(disabled);
        fieldNotesArea.setDisable(disabled);
        constraintChecks.values().forEach(check -> check.setDisable(disabled));
        visibilityChecks.values().forEach(check -> check.setDisable(disabled));
        userEditableCheck.setDisable(disabled);
    }

    private Set<FieldConstraint> selectedConstraints() {
        Set<FieldConstraint> selected = new LinkedHashSet<>();
        constraintChecks.forEach((constraint, check) -> {
            if (check.isSelected()) selected.add(constraint);
        });
        return selected;
    }

    private Set<FieldVisibility> selectedVisibility() {
        Set<FieldVisibility> selected = new LinkedHashSet<>();
        visibilityChecks.forEach((visibility, check) -> {
            if (check.isSelected()) selected.add(visibility);
        });
        return selected;
    }

    private <E extends Enum<E>> VBox checkboxGroup(String title, E[] values, java.util.Map<E, CheckBox> target) {
        VBox box = new VBox(4);
        Label label = new Label(title);
        label.getStyleClass().add("data-dictionary-form-label");
        box.getChildren().add(label);
        for (E value : values) {
            CheckBox checkBox = new CheckBox(label(value));
            target.put(value, checkBox);
            box.getChildren().add(checkBox);
        }
        return box;
    }

    private TableColumn<DataDictionaryField, String> column(String title, java.util.function.Function<DataDictionaryField, String> extractor) {
        TableColumn<DataDictionaryField, String> column = new TableColumn<>(title);
        column.setCellValueFactory(data -> new SimpleStringProperty(extractor.apply(data.getValue())));
        column.setPrefWidth(140);
        return column;
    }

    private VBox section(String title) {
        VBox box = new VBox(8);
        box.getStyleClass().add("data-dictionary-section");
        box.setPadding(new Insets(10));
        Label label = new Label(title);
        label.getStyleClass().add("data-dictionary-section-title");
        box.getChildren().add(label);
        return box;
    }

    private VBox formBody() {
        VBox box = new VBox(8);
        box.getStyleClass().add("data-dictionary-form-body");
        return box;
    }

    private GridPane grid() {
        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(7);
        return grid;
    }

    private void addRow(GridPane grid, int row, String labelText, javafx.scene.Node control) {
        Label label = new Label(labelText);
        label.getStyleClass().add("data-dictionary-form-label");
        grid.add(label, 0, row);
        grid.add(control, 1, row);
        GridPane.setHgrow(control, Priority.ALWAYS);
        if (control instanceof TextField textField) {
            textField.setMaxWidth(Double.MAX_VALUE);
        }
        if (control instanceof ComboBox<?> comboBox) {
            comboBox.setMaxWidth(Double.MAX_VALUE);
        }
        if (control instanceof TextArea textArea) {
            textArea.setMaxWidth(Double.MAX_VALUE);
        }
    }

    private Button button(String text, String tooltip) {
        Button button = new Button(text);
        button.getStyleClass().add("data-dictionary-button");
        button.setTooltip(new Tooltip(tooltip));
        HBox.setMargin(button, new Insets(4, 0, 0, 0));
        return button;
    }

    private void configureArea(TextArea area, int rows) {
        area.setPrefRowCount(rows);
        area.setWrapText(true);
    }

    private String constraintSummary(DataDictionaryField field) {
        return DataDictionaryLabels.constraintSummary(field);
    }

    private String visibilitySummary(DataDictionaryField field) {
        return DataDictionaryLabels.visibilitySummary(field);
    }

    private String label(Enum<?> value) {
        return DataDictionaryLabels.label(value);
    }
}
