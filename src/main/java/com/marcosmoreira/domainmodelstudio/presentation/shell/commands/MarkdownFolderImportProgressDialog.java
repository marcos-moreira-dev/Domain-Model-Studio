package com.marcosmoreira.domainmodelstudio.presentation.shell.commands;

import java.nio.file.Path;
import java.util.List;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/** Diálogo modal liviano para que la importación masiva no congele visualmente la ventana principal. */
final class MarkdownFolderImportProgressDialog {

    private static final ButtonType CANCEL = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

    private final Dialog<Void> dialog;
    private final Label statusLabel;
    private final ProgressIndicator progressIndicator;
    private boolean shown;

    private MarkdownFolderImportProgressDialog(Dialog<Void> dialog, Label statusLabel, ProgressIndicator progressIndicator) {
        this.dialog = dialog;
        this.statusLabel = statusLabel;
        this.progressIndicator = progressIndicator;
    }

    static MarkdownFolderImportProgressDialog forImport(Path sourceRoot) {
        return create("Importando carpeta Markdown",
                "Importación recursiva en progreso",
                "Carpeta: " + sourceRoot.toAbsolutePath());
    }

    static MarkdownFolderImportProgressDialog forFiles(List<Path> files) {
        int count = files == null ? 0 : files.size();
        String scope = count == 1
                ? "Archivo: " + files.get(0).toAbsolutePath()
                : "Selección: " + count + " archivos Markdown";
        return create("Importando Markdown", "Importación de archivos seleccionados en progreso", scope);
    }

    private static MarkdownFolderImportProgressDialog create(String title, String header, String scope) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setResizable(false);
        dialog.getDialogPane().getButtonTypes().add(CANCEL);

        ProgressIndicator indicator = new ProgressIndicator();
        indicator.setPrefSize(54, 54);
        Label status = new Label("Preparando lectura...");
        status.setWrapText(true);
        Label root = new Label(scope);
        root.setWrapText(true);
        root.getStyleClass().add("muted-label");

        VBox texts = new VBox(8, status, root);
        HBox.setHgrow(texts, Priority.ALWAYS);
        HBox content = new HBox(14, indicator, texts);
        content.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().setPrefWidth(620);
        return new MarkdownFolderImportProgressDialog(dialog, status, indicator);
    }

    void bind(Task<?> task) {
        statusLabel.textProperty().bind(task.messageProperty());
        progressIndicator.progressProperty().bind(task.progressProperty());
        Button cancelButton = (Button) dialog.getDialogPane().lookupButton(CANCEL);
        cancelButton.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
            event.consume();
            statusLabel.textProperty().unbind();
            statusLabel.setText("Cancelando importación...");
            cancelButton.setDisable(true);
            task.cancel(true);
        });
    }

    void show() {
        if (!shown) {
            shown = true;
            dialog.show();
        }
    }

    void close() {
        if (shown) {
            dialog.close();
            shown = false;
        }
    }

}
