package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import com.marcosmoreira.domainmodelstudio.application.visualcomment.VisualCommentPolicy;
import com.marcosmoreira.domainmodelstudio.domain.visualcomment.VisualComment;
import java.util.Optional;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/** Dialogos compactos para editar o eliminar notas visuales. */
final class VisualCommentDialog {

    private static final ButtonType SAVE = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
    private static final ButtonType DELETE = new ButtonType("Eliminar nota", ButtonBar.ButtonData.OTHER);
    private static final ButtonType CANCEL = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

    Optional<Result> editTitle(VisualComment comment) {
        Dialog<Result> dialog = baseDialog("Editar titulo de nota");
        TextField title = new TextField(comment.visibleTitle());
        title.setPromptText(VisualComment.DEFAULT_TITLE);
        Label counter = new Label();
        counter.getStyleClass().add("visual-comment-dialog-counter");
        title.textProperty().addListener((observable, previous, current) ->
                enforceLimit(title, counter, VisualCommentPolicy.TITLE_MAX_LENGTH));
        enforceLimit(title, counter, VisualCommentPolicy.TITLE_MAX_LENGTH);
        dialog.getDialogPane().setContent(contentBox(title, counter));
        wireDelete(dialog);
        dialog.setResultConverter(button -> resultFor(button, title.getText(), comment.description()));
        return dialog.showAndWait();
    }

    Optional<Result> editDescription(VisualComment comment) {
        Dialog<Result> dialog = baseDialog("Editar descripcion de nota");
        TextArea description = new TextArea(comment.description());
        description.setPromptText(VisualComment.DEFAULT_DESCRIPTION);
        description.setWrapText(true);
        description.setPrefRowCount(7);
        Label counter = new Label();
        counter.getStyleClass().add("visual-comment-dialog-counter");
        description.textProperty().addListener((observable, previous, current) ->
                enforceLimit(description, counter, VisualCommentPolicy.DESCRIPTION_MAX_LENGTH));
        enforceLimit(description, counter, VisualCommentPolicy.DESCRIPTION_MAX_LENGTH);
        dialog.getDialogPane().setContent(contentBox(description, counter));
        wireDelete(dialog);
        dialog.setResultConverter(button -> resultFor(button, comment.title(), description.getText()));
        return dialog.showAndWait();
    }

    private Dialog<Result> baseDialog(String title) {
        Dialog<Result> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.getDialogPane().getButtonTypes().setAll(DELETE, CANCEL, SAVE);
        dialog.getDialogPane().getStyleClass().add("visual-comment-dialog");
        return dialog;
    }

    private VBox contentBox(javafx.scene.Node editor, Label counter) {
        VBox box = new VBox(8.0, editor, counter);
        box.setPadding(new Insets(8.0));
        box.setPrefWidth(380.0);
        return box;
    }

    private void wireDelete(Dialog<Result> dialog) {
        Button deleteButton = (Button) dialog.getDialogPane().lookupButton(DELETE);
        if (deleteButton != null) {
            deleteButton.getStyleClass().add("danger-button");
            deleteButton.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
                if (!confirmDelete()) {
                    event.consume();
                }
            });
        }
    }

    private boolean confirmDelete() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Eliminar nota");
        alert.setHeaderText("Eliminar nota visual");
        alert.setContentText("Esta nota se quitara del canvas.");
        ButtonType delete = new ButtonType("Eliminar", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE), delete);
        return alert.showAndWait().filter(delete::equals).isPresent();
    }

    private Result resultFor(ButtonType button, String title, String description) {
        if (button == DELETE) {
            return Result.deleted();
        }
        if (button == SAVE) {
            return Result.save(
                    VisualCommentPolicy.normalizeTitle(title),
                    VisualCommentPolicy.normalizeDescription(description));
        }
        return null;
    }

    private void enforceLimit(TextField field, Label counter, int maxLength) {
        String text = field.getText() == null ? "" : field.getText();
        if (text.length() > maxLength) {
            field.setText(text.substring(0, maxLength));
            field.positionCaret(maxLength);
            text = field.getText();
        }
        counter.setText(text.length() + " / " + maxLength);
    }

    private void enforceLimit(TextArea area, Label counter, int maxLength) {
        String text = area.getText() == null ? "" : area.getText();
        if (text.length() > maxLength) {
            area.setText(text.substring(0, maxLength));
            area.positionCaret(maxLength);
            text = area.getText();
        }
        counter.setText(text.length() + " / " + maxLength);
    }

    record Result(boolean delete, String title, String description) {
        static Result deleted() {
            return new Result(true, "", "");
        }

        static Result save(String title, String description) {
            return new Result(false, title, description);
        }
    }
}
