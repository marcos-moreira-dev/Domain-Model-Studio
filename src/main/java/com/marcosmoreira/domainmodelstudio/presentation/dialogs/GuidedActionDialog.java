package com.marcosmoreira.domainmodelstudio.presentation.dialogs;

import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Window;

/**
 * Diálogo transversal para confirmar acciones masivas con una guía previa.
 *
 * <p>Sirve para importaciones, reparaciones, validaciones u operaciones que
 * conviene explicar antes de ejecutarlas. El contenido largo se mantiene dentro
 * de áreas con scroll para no depender de {@code Alert} ni crecer fuera de la
 * pantalla.</p>
 */
public final class GuidedActionDialog {

    private static final double MIN_WIDTH = 720;
    private static final double MAX_WIDTH = 1040;
    private static final double MIN_HEIGHT = 480;
    private static final double MAX_HEIGHT = 720;

    private GuidedActionDialog() {
    }

    public static Optional<Boolean> confirm(Window owner, Request request) {
        return new Presenter(owner, request).showAndWait();
    }

    public record Request(
            String title,
            String header,
            String summary,
            String detailLabel,
            String detail,
            String actionButtonText,
            String cancelButtonText
    ) {
        public Request {
            title = clean(title, "Confirmar acción");
            header = clean(header, title);
            summary = clean(summary, "Revisa la información antes de continuar.");
            detailLabel = clean(detailLabel, "Detalle:");
            detail = clean(detail, "Sin detalle disponible.");
            actionButtonText = clean(actionButtonText, "Aceptar");
            cancelButtonText = clean(cancelButtonText, "Cancelar");
        }
    }

    private static final class Presenter {
        private final Dialog<Boolean> dialog = new Dialog<>();
        private final Request request;
        private final ButtonType actionButtonType;
        private final ButtonType cancelButtonType;

        private Presenter(Window owner, Request request) {
            this.request = Objects.requireNonNull(request, "request");
            this.actionButtonType = new ButtonType(request.actionButtonText(), ButtonBar.ButtonData.OK_DONE);
            this.cancelButtonType = new ButtonType(request.cancelButtonText(), ButtonBar.ButtonData.CANCEL_CLOSE);
            configureDialog(resolveOwner(owner));
        }

        private void configureDialog(Window owner) {
            dialog.setTitle(request.title());
            dialog.setHeaderText(request.header());
            dialog.setResizable(true);
            if (owner != null) {
                dialog.initOwner(owner);
            }
            dialog.getDialogPane().getButtonTypes().setAll(actionButtonType, cancelButtonType);
            dialog.getDialogPane().setContent(content());
            dialog.getDialogPane().getStyleClass().add("guided-action-dialog-pane");
            stylesheet().ifPresent(url -> dialog.getDialogPane().getStylesheets().add(url.toExternalForm()));
            applyResponsiveSize(owner);
            dialog.setResultConverter(button -> button == actionButtonType);
        }

        private VBox content() {
            Label summary = new Label(request.summary());
            summary.setWrapText(true);
            summary.getStyleClass().add("guided-action-summary");

            ScrollPane summaryScroll = new ScrollPane(summary);
            summaryScroll.setFitToWidth(true);
            summaryScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            summaryScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            summaryScroll.setPrefViewportHeight(130);
            summaryScroll.setMaxHeight(180);
            summaryScroll.getStyleClass().add("guided-action-summary-scroll");

            Label detailLabel = new Label(request.detailLabel());
            detailLabel.setWrapText(true);
            detailLabel.getStyleClass().add("guided-action-detail-label");

            TextArea detail = new TextArea(request.detail());
            detail.setEditable(false);
            detail.setWrapText(false);
            detail.setPrefRowCount(16);
            detail.setPrefColumnCount(100);
            detail.getStyleClass().add("guided-action-detail-area");
            VBox.setVgrow(detail, Priority.ALWAYS);

            VBox box = new VBox(10, summaryScroll, detailLabel, detail);
            box.setPadding(new Insets(10, 12, 12, 12));
            return box;
        }

        private Optional<Boolean> showAndWait() {
            return dialog.showAndWait();
        }

        private void applyResponsiveSize(Window owner) {
            Rectangle2D bounds = visualBounds(owner);
            double width = clamp(bounds.getWidth() * 0.64, MIN_WIDTH, Math.min(MAX_WIDTH, bounds.getWidth() * 0.90));
            double height = clamp(bounds.getHeight() * 0.62, MIN_HEIGHT, Math.min(MAX_HEIGHT, bounds.getHeight() * 0.86));
            dialog.getDialogPane().setPrefSize(width, height);
            dialog.getDialogPane().setMinSize(MIN_WIDTH, 400);
            dialog.getDialogPane().setMaxSize(bounds.getWidth() * 0.92, bounds.getHeight() * 0.90);
        }
    }

    private static Window resolveOwner(Window owner) {
        if (owner != null) {
            return owner;
        }
        return Window.getWindows().stream()
                .filter(Window::isShowing)
                .filter(Window::isFocused)
                .findFirst()
                .orElse(null);
    }

    private static Rectangle2D visualBounds(Window owner) {
        if (owner != null) {
            return Screen.getScreensForRectangle(owner.getX(), owner.getY(), owner.getWidth(), owner.getHeight())
                    .stream()
                    .findFirst()
                    .orElse(Screen.getPrimary())
                    .getVisualBounds();
        }
        return Screen.getPrimary().getVisualBounds();
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    private static Optional<URL> stylesheet() {
        return Optional.ofNullable(GuidedActionDialog.class.getResource("/css/app-light.css"));
    }

    private static String clean(String value, String fallback) {
        String normalized = Objects.toString(value, "").strip();
        return normalized.isBlank() ? fallback : normalized;
    }
}
