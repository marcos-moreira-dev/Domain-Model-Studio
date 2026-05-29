package com.marcosmoreira.domainmodelstudio.presentation.datadictionary;

import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryDocument;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryStatus;
import java.io.File;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

/** Formulario de metadatos del documento de diccionario. */
final class DataDictionaryDocumentForm {

    private final DataDictionaryViewModel viewModel;
    private final TextField projectField = new TextField();
    private final TextField clientField = new TextField();
    private final TextField organizationField = new TextField();
    private final TextField authorField = new TextField();
    private final TextField versionField = new TextField();
    private final TextField logoReferenceField = new TextField();
    private final Button chooseLogoButton = new Button("Seleccionar…");
    private final ComboBox<DataDictionaryStatus> statusCombo = new ComboBox<>();
    private final TextArea introductionArea = new TextArea();
    private final TextArea notesArea = new TextArea();
    private final VBox root;

    DataDictionaryDocumentForm(DataDictionaryViewModel viewModel) {
        this.viewModel = viewModel;
        this.root = build();
    }

    Parent root() {
        return root;
    }

    void refresh(DataDictionaryDocument document) {
        boolean disabled = document == null;
        setControlsDisabled(disabled);
        if (document == null) {
            clear();
            return;
        }
        projectField.setText(document.projectName());
        clientField.setText(document.clientName());
        organizationField.setText(document.organizationName());
        authorField.setText(document.author());
        versionField.setText(document.version());
        statusCombo.setValue(document.status());
        logoReferenceField.setText(document.logoReference());
        introductionArea.setText(document.introduction());
        notesArea.setText(document.notes());
    }

    private VBox build() {
        statusCombo.getItems().setAll(DataDictionaryStatus.values());
        configureArea(introductionArea, 4);
        configureArea(notesArea, 3);
        VBox box = section("Propiedades del documento");
        GridPane grid = grid();
        int row = 0;
        addRow(grid, row++, "Proyecto", projectField);
        addRow(grid, row++, "Cliente", clientField);
        addRow(grid, row++, "Organización", organizationField);
        addRow(grid, row++, "Autor", authorField);
        addRow(grid, row++, "Versión", versionField);
        addRow(grid, row++, "Estado", statusCombo);
        addRow(grid, row++, "Logo opcional", logoReferenceControl());
        addRow(grid, row++, "Introducción", introductionArea);
        addRow(grid, row++, "Observaciones", notesArea);
        Button apply = button();
        box.getChildren().addAll(grid, apply);
        return box;
    }

    private Button button() {
        Button button = new Button("Aplicar documento");
        button.getStyleClass().add("data-dictionary-button");
        button.setTooltip(new Tooltip("Actualizar portada, introducción y metadatos del diccionario"));
        button.setOnAction(event -> viewModel.applyDocumentChanges(
                projectField.getText(),
                clientField.getText(),
                organizationField.getText(),
                authorField.getText(),
                versionField.getText(),
                statusCombo.getValue(),
                introductionArea.getText(),
                logoReferenceField.getText(),
                notesArea.getText()));
        HBox.setMargin(button, new Insets(4, 0, 0, 0));
        return button;
    }

    private void clear() {
        projectField.clear();
        clientField.clear();
        organizationField.clear();
        authorField.clear();
        versionField.clear();
        logoReferenceField.clear();
        statusCombo.setValue(DataDictionaryStatus.DRAFT);
        introductionArea.clear();
        notesArea.clear();
    }

    private void setControlsDisabled(boolean disabled) {
        projectField.setDisable(disabled);
        clientField.setDisable(disabled);
        organizationField.setDisable(disabled);
        authorField.setDisable(disabled);
        versionField.setDisable(disabled);
        logoReferenceField.setDisable(disabled);
        chooseLogoButton.setDisable(disabled);
        statusCombo.setDisable(disabled);
        introductionArea.setDisable(disabled);
        notesArea.setDisable(disabled);
    }

    private HBox logoReferenceControl() {
        HBox box = new HBox(6);
        logoReferenceField.setPromptText("Ruta de imagen PNG/JPG para la portada");
        HBox.setHgrow(logoReferenceField, Priority.ALWAYS);
        chooseLogoButton.getStyleClass().add("data-dictionary-button");
        chooseLogoButton.setTooltip(new Tooltip("Seleccionar una imagen para usarla como referencia de logo en la portada"));
        chooseLogoButton.setOnAction(event -> chooseLogoFile());
        box.getChildren().addAll(logoReferenceField, chooseLogoButton);
        return box;
    }

    private void chooseLogoFile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Seleccionar logo del documento");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg"),
                new FileChooser.ExtensionFilter("Todos los archivos", "*.*"));
        File selected = chooser.showOpenDialog(root.getScene() == null ? null : root.getScene().getWindow());
        if (selected != null) {
            logoReferenceField.setText(selected.getAbsolutePath());
        }
    }

    private static VBox section(String title) {
        VBox box = new VBox(8);
        box.getStyleClass().add("data-dictionary-section");
        box.setPadding(new Insets(10));
        Label label = new Label(title);
        label.getStyleClass().add("data-dictionary-section-title");
        box.getChildren().add(label);
        return box;
    }

    private static GridPane grid() {
        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(7);
        return grid;
    }

    private static void addRow(GridPane grid, int row, String labelText, javafx.scene.Node control) {
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

    private static void configureArea(TextArea area, int rows) {
        area.setPrefRowCount(rows);
        area.setWrapText(true);
    }
}
