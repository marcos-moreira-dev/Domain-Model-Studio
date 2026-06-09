package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;

/** Ventana de configuración global para abrir archivos fuente desde UML Clases. */
public final class UmlClassCodeEditorSettingsDialog {
    private UmlClassCodeEditorSettingsDialog() {
    }

    public static void show(UmlClassDiagramViewModel viewModel) {
        Stage stage = new Stage();
        stage.setTitle("Configuración - Editor de código");
        stage.initModality(Modality.APPLICATION_MODAL);

        VBox root = new VBox(12);
        root.setPadding(new Insets(16));
        root.getStyleClass().add("uml-code-editor-settings-dialog");

        Label title = new Label("Editor de código para archivos importados");
        title.getStyleClass().add("uml-class-panel-title");

        Label summary = new Label(viewModel.codeEditorConfigurationSummary());
        summary.setWrapText(true);

        TextField commandField = new TextField(viewModel.codeEditorUserCommand());
        commandField.setPromptText("code  |  code --goto %f  |  C:\\Ruta\\Code.exe %f");
        commandField.setTooltip(new Tooltip("Usa %f como marcador de la ruta del archivo. Si no lo usas, la ruta se agrega al final."));
        HBox.setHgrow(commandField, Priority.ALWAYS);

        Button browse = new Button("Elegir ejecutable...");
        browse.setOnAction(event -> chooseExecutable(stage, commandField));

        HBox commandRow = new HBox(8, commandField, browse);

        Label help = new Label("Ejemplos: code, code --reuse-window, code --goto %f. "
                + "Si 'code' no está en el PATH, elige Code.exe desde esta ventana. "
                + "La ruta del archivo se añade automáticamente si no usas %f.");
        help.setWrapText(true);

        Button save = new Button("Guardar");
        save.setDefaultButton(true);
        save.setOnAction(event -> {
            viewModel.saveCodeEditorCommand(commandField.getText());
            stage.close();
        });

        Button systemDefault = new Button("Usar predeterminado del sistema");
        systemDefault.setOnAction(event -> {
            viewModel.saveCodeEditorSystemDefaultCommand();
            stage.close();
        });

        Button windowsOpenWith = new Button("Preguntar con Windows...");
        windowsOpenWith.setOnAction(event -> {
            viewModel.saveCodeEditorWindowsOpenWithCommand();
            stage.close();
        });

        Button reset = new Button("Restablecer");
        reset.setOnAction(event -> {
            viewModel.resetCodeEditorCommand();
            stage.close();
        });

        Button cancel = new Button("Cancelar");
        cancel.setCancelButton(true);
        cancel.setOnAction(event -> stage.close());

        HBox buttons = new HBox(8, save, systemDefault, windowsOpenWith, reset, cancel);
        root.getChildren().addAll(title, summary, commandRow, help, buttons);

        stage.setScene(new Scene(root, 720, 230));
        stage.showAndWait();
    }

    private static void chooseExecutable(Stage owner, TextField commandField) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Elegir editor de código");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Ejecutables", "*.exe", "*.cmd", "*.bat"),
                new FileChooser.ExtensionFilter("Todos los archivos", "*.*")
        );
        File selected = chooser.showOpenDialog(owner);
        if (selected != null) {
            commandField.setText('"' + selected.getAbsolutePath() + '"' + " %f");
        }
    }
}
