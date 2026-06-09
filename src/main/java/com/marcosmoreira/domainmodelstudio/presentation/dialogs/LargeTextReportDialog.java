package com.marcosmoreira.domainmodelstudio.presentation.dialogs;

import java.net.URL;
import java.util.Objects;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Window;

/**
 * Diálogo transversal para reportes largos, revisables y copiables.
 *
 * <p>Está pensado para resultados de importación, validaciones masivas y
 * diagnósticos extensos. Evita los {@code Alert} expandidos porque esos diálogos
 * tienden a crecer fuera de la pantalla cuando el contenido es grande.</p>
 */
public final class LargeTextReportDialog {

    private static final double MIN_WIDTH = 720;
    private static final double MAX_WIDTH = 1160;
    private static final double MIN_HEIGHT = 520;
    private static final double MAX_HEIGHT = 780;

    private LargeTextReportDialog() {
    }

    public static void show(Window owner, Request request) {
        new Presenter(owner, request).showAndWait();
    }

    /**
     * Muestra el reporte de forma diferida y no bloqueante.
     *
     * <p>Es seguro llamarlo desde callbacks de animación, apertura gradual o
     * layout, donde JavaFX no permite {@code showAndWait()}.</p>
     */
    public static void showLater(Window owner, Request request) {
        Platform.runLater(() -> new Presenter(owner, request).show());
    }

    public record Request(
            String title,
            String header,
            String summary,
            String detailLabel,
            String detail,
            String copyButtonText,
            String acceptButtonText
    ) {
        public Request {
            title = clean(title, "Reporte");
            header = clean(header, title);
            summary = clean(summary, "Sin resumen disponible.");
            detailLabel = clean(detailLabel, "Detalle:");
            detail = clean(detail, "Sin detalle disponible.");
            copyButtonText = clean(copyButtonText, "Copiar detalle");
            acceptButtonText = clean(acceptButtonText, "Aceptar");
        }
    }

    private static final class Presenter {
        private final Dialog<ButtonType> dialog = new Dialog<>();
        private final Request request;
        private final ButtonType copyButtonType;
        private final ButtonType acceptButtonType;

        private Presenter(Window owner, Request request) {
            this.request = Objects.requireNonNull(request, "request");
            this.copyButtonType = new ButtonType(request.copyButtonText(), ButtonBar.ButtonData.OTHER);
            this.acceptButtonType = new ButtonType(request.acceptButtonText(), ButtonBar.ButtonData.OK_DONE);
            configureDialog(resolveOwner(owner));
        }

        private void configureDialog(Window owner) {
            dialog.setTitle(request.title());
            dialog.setHeaderText(request.header());
            dialog.setResizable(true);
            if (owner != null) {
                dialog.initOwner(owner);
            }
            dialog.getDialogPane().getButtonTypes().setAll(copyButtonType, acceptButtonType);
            dialog.getDialogPane().setContent(content());
            dialog.getDialogPane().getStyleClass().add("large-report-dialog-pane");
            stylesheet().ifPresent(url -> dialog.getDialogPane().getStylesheets().add(url.toExternalForm()));
            applyResponsiveSize(owner);
            configureCopyButton();
        }

        private VBox content() {
            Label summary = new Label(request.summary());
            summary.setWrapText(true);
            summary.getStyleClass().add("large-report-summary");

            ScrollPane summaryScroll = new ScrollPane(summary);
            summaryScroll.setFitToWidth(true);
            summaryScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            summaryScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            summaryScroll.setPrefViewportHeight(160);
            summaryScroll.setMaxHeight(220);
            summaryScroll.getStyleClass().add("large-report-summary-scroll");

            Label detailLabel = new Label(request.detailLabel());
            detailLabel.setWrapText(true);
            detailLabel.getStyleClass().add("large-report-detail-label");

            TextArea detail = new TextArea(request.detail());
            detail.setEditable(false);
            detail.setWrapText(false);
            detail.setPrefRowCount(22);
            detail.setPrefColumnCount(110);
            detail.getStyleClass().add("large-report-detail-area");
            VBox.setVgrow(detail, Priority.ALWAYS);

            VBox box = new VBox(10, summaryScroll, detailLabel, detail);
            box.setPadding(new Insets(10, 12, 12, 12));
            return box;
        }

        private void configureCopyButton() {
            Button copyButton = (Button) dialog.getDialogPane().lookupButton(copyButtonType);
            copyButton.setMinWidth(170);
            copyButton.setPrefWidth(170);
            Button acceptButton = (Button) dialog.getDialogPane().lookupButton(acceptButtonType);
            acceptButton.setMinWidth(110);
            copyButton.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
                event.consume();
                ClipboardContent content = new ClipboardContent();
                content.putString(request.detail());
                Clipboard.getSystemClipboard().setContent(content);
                copyButton.setText("Copiado");
            });
        }

        private void applyResponsiveSize(Window owner) {
            Rectangle2D bounds = visualBounds(owner);
            double width = clamp(bounds.getWidth() * 0.78, MIN_WIDTH, Math.min(MAX_WIDTH, bounds.getWidth() * 0.94));
            double height = clamp(bounds.getHeight() * 0.78, MIN_HEIGHT, Math.min(MAX_HEIGHT, bounds.getHeight() * 0.92));
            dialog.getDialogPane().setPrefSize(width, height);
            dialog.getDialogPane().setMinSize(MIN_WIDTH, 430);
            dialog.getDialogPane().setMaxSize(bounds.getWidth() * 0.96, bounds.getHeight() * 0.94);
        }

        private void showAndWait() {
            dialog.showAndWait();
        }

        private void show() {
            dialog.show();
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

    private static java.util.Optional<URL> stylesheet() {
        return java.util.Optional.ofNullable(LargeTextReportDialog.class.getResource("/css/app-light.css"));
    }

    private static String clean(String value, String fallback) {
        String normalized = Objects.toString(value, "").strip();
        return normalized.isBlank() ? fallback : normalized;
    }
}
