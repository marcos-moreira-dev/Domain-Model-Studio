package com.marcosmoreira.domainmodelstudio.presentation.dialogs;

import java.net.URL;
import java.util.Objects;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Diálogo reutilizable para mensajes abiertos por clic.
 *
 * <p>Sirve para ayuda rápida, advertencias y errores con texto largo. A diferencia
 * de los Alert estándar, usa ancho cómodo, icono del proyecto y ajuste de línea.</p>
 */
public final class ClickMessageDialog {

    public enum MessageType {
        INFO("Información", "/icons/help-book.png", "i"),
        WARNING("Advertencia", "/icons/validate.png", "!"),
        ERROR("Error", "/icons/close.png", "×");

        private final String title;
        private final String iconPath;
        private final String fallbackText;

        MessageType(String title, String iconPath, String fallbackText) {
            this.title = title;
            this.iconPath = iconPath;
            this.fallbackText = fallbackText;
        }
    }

    private ClickMessageDialog() {
    }

    public static void showInfo(String title, String body) {
        show(MessageType.INFO, title, body);
    }

    public static void showWarning(String title, String body) {
        show(MessageType.WARNING, title, body);
    }

    public static void showError(String title, String body) {
        show(MessageType.ERROR, title, body);
    }


    public static boolean confirmWarning(String title, String body, String confirmText) {
        Dialog<ButtonType> dialog = new Dialog<>();
        ButtonType confirm = new ButtonType(clean(confirmText, "Aceptar"), ButtonBar.ButtonData.OK_DONE);
        dialog.setTitle(MessageType.WARNING.title);
        dialog.getDialogPane().getButtonTypes().addAll(confirm, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(content(MessageType.WARNING, title, body));
        dialog.getDialogPane().setMinWidth(620);
        dialog.getDialogPane().setPrefWidth(760);
        dialog.getDialogPane().getStyleClass().add("click-message-dialog-pane");
        cssUrl().ifPresent(url -> dialog.getDialogPane().getStylesheets().add(url.toExternalForm()));
        return dialog.showAndWait().filter(confirm::equals).isPresent();
    }

    public static void show(MessageType type, String title, String body) {
        MessageType resolvedType = type == null ? MessageType.INFO : type;
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle(resolvedType.title);
        dialog.getDialogPane().getButtonTypes().add(new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE));
        dialog.getDialogPane().setContent(content(resolvedType, title, body));
        dialog.getDialogPane().setMinWidth(620);
        dialog.getDialogPane().setPrefWidth(760);
        dialog.getDialogPane().getStyleClass().add("click-message-dialog-pane");
        cssUrl().ifPresent(url -> dialog.getDialogPane().getStylesheets().add(url.toExternalForm()));
        dialog.showAndWait();
    }

    private static HBox content(MessageType type, String title, String body) {
        Label heading = new Label(clean(title, type.title));
        heading.setWrapText(true);
        heading.getStyleClass().add("click-message-title");

        Label message = new Label(clean(body, "Sin detalle registrado."));
        message.setWrapText(true);
        message.setMaxWidth(Double.MAX_VALUE);
        message.getStyleClass().add("click-message-body");

        ScrollPane messageScroll = new ScrollPane(message);
        messageScroll.setFitToWidth(true);
        messageScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        messageScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        messageScroll.setPrefViewportHeight(120);
        messageScroll.setMaxHeight(320);
        messageScroll.getStyleClass().add("click-message-scroll");

        VBox texts = new VBox(12, heading, messageScroll);
        HBox.setHgrow(texts, Priority.ALWAYS);
        HBox root = new HBox(18, icon(type), texts);
        root.setPadding(new Insets(20, 22, 20, 20));
        root.setAlignment(Pos.TOP_LEFT);
        root.getStyleClass().add("click-message-content");
        return root;
    }

    private static Node icon(MessageType type) {
        URL url = ClickMessageDialog.class.getResource(type.iconPath);
        if (url != null) {
            ImageView image = new ImageView(new Image(url.toExternalForm(), 46, 46, true, true));
            image.getStyleClass().add("click-message-image-icon");
            return image;
        }
        Label fallback = new Label(type.fallbackText);
        fallback.setMinSize(46, 46);
        fallback.setMaxSize(46, 46);
        fallback.setAlignment(Pos.CENTER);
        fallback.getStyleClass().add("click-message-icon");
        return fallback;
    }

    private static java.util.Optional<URL> cssUrl() {
        return java.util.Optional.ofNullable(ClickMessageDialog.class.getResource("/css/click-message-dialog.css"));
    }

    private static String clean(String value, String fallback) {
        String normalized = Objects.toString(value, "").strip();
        return normalized.isBlank() ? fallback : normalized;
    }
}
