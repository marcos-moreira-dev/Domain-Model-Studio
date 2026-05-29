package com.marcosmoreira.domainmodelstudio.presentation.dialogs;

import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.Screen;
import javafx.stage.Window;

/**
 * Diálogo transversal para comunicar el resultado de una operación masiva.
 *
 * <p>El componente separa tres niveles: resumen legible, estado visual dibujado
 * con JavaFX y detalle copiable. No depende de recursos externos ni imágenes, de
 * modo que puede reutilizarse en importaciones, validaciones, exportaciones batch
 * y diagnósticos sin agregar assets al instalador.</p>
 */
public final class OperationResultDialog {

    private static final double MIN_WIDTH = 760;
    private static final double MAX_WIDTH = 1180;
    private static final double MIN_HEIGHT = 520;
    private static final double MAX_HEIGHT = 790;

    private OperationResultDialog() {
    }

    public static void showLater(Window owner, Request request) {
        Platform.runLater(() -> new Presenter(owner, request).show());
    }

    /** Estado visual de la operación. */
    public enum Status {
        SUCCESS,
        PROBLEM
    }

    public record Request(
            String title,
            String header,
            String summary,
            Status status,
            String statusText,
            String detailLabel,
            String detail,
            String copyButtonText,
            String acceptButtonText
    ) {
        public Request {
            title = clean(title, "Resultado");
            header = clean(header, title);
            summary = clean(summary, "Operación completada.");
            status = status == null ? Status.SUCCESS : status;
            statusText = clean(statusText, status == Status.SUCCESS
                    ? "Todo está en orden."
                    : "Se detectaron elementos que requieren revisión.");
            detailLabel = clean(detailLabel, status == Status.SUCCESS
                    ? "Estado:"
                    : "Problemas para corregir:");
            detail = clean(detail, status == Status.SUCCESS
                    ? "Todo está en orden. No se detectaron archivos problemáticos."
                    : "No se recibió detalle de los problemas detectados.");
            copyButtonText = clean(copyButtonText, status == Status.SUCCESS ? "Copiar estado" : "Copiar problemas");
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
            dialog.getDialogPane().getStyleClass().add("operation-result-dialog-pane");
            stylesheet().ifPresent(url -> dialog.getDialogPane().getStylesheets().add(url.toExternalForm()));
            applyResponsiveSize(owner);
            configureButtons();
        }

        private VBox content() {
            Label summary = new Label(request.summary());
            summary.setWrapText(true);
            summary.getStyleClass().add("operation-result-summary");

            ScrollPane summaryScroll = new ScrollPane(summary);
            summaryScroll.setFitToWidth(true);
            summaryScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            summaryScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            summaryScroll.setPrefViewportHeight(145);
            summaryScroll.setMaxHeight(210);

            VBox stateBlock = new VBox(8, StatusIconFactory.create(request.status()), statusText());
            stateBlock.setAlignment(Pos.CENTER);
            stateBlock.setPadding(new Insets(8, 0, 6, 0));
            stateBlock.getStyleClass().add("operation-result-state-block");

            Label detailLabel = new Label(request.detailLabel());
            detailLabel.setWrapText(true);
            detailLabel.getStyleClass().add("operation-result-detail-label");

            TextArea detail = new TextArea(request.detail());
            detail.setEditable(false);
            detail.setWrapText(request.status() == Status.SUCCESS);
            detail.setPrefRowCount(request.status() == Status.SUCCESS ? 4 : 18);
            detail.setPrefColumnCount(108);
            detail.getStyleClass().add("operation-result-detail-area");
            VBox.setVgrow(detail, request.status() == Status.SUCCESS ? Priority.NEVER : Priority.ALWAYS);

            VBox box = new VBox(10, summaryScroll, stateBlock, detailLabel, detail);
            box.setPadding(new Insets(10, 14, 12, 14));
            return box;
        }

        private Label statusText() {
            Label label = new Label(request.statusText());
            label.setWrapText(true);
            label.setAlignment(Pos.CENTER);
            label.setMaxWidth(Double.MAX_VALUE);
            label.getStyleClass().add(request.status() == Status.SUCCESS
                    ? "operation-result-status-success"
                    : "operation-result-status-problem");
            return label;
        }

        private void configureButtons() {
            Button copyButton = (Button) dialog.getDialogPane().lookupButton(copyButtonType);
            copyButton.setMinWidth(160);
            copyButton.setPrefWidth(180);
            Button acceptButton = (Button) dialog.getDialogPane().lookupButton(acceptButtonType);
            acceptButton.setMinWidth(120);
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
            double width = clamp(bounds.getWidth() * 0.72, MIN_WIDTH, Math.min(MAX_WIDTH, bounds.getWidth() * 0.94));
            double height = clamp(bounds.getHeight() * 0.72, MIN_HEIGHT, Math.min(MAX_HEIGHT, bounds.getHeight() * 0.90));
            dialog.getDialogPane().setPrefSize(width, height);
            dialog.getDialogPane().setMinSize(MIN_WIDTH, 440);
            dialog.getDialogPane().setMaxSize(bounds.getWidth() * 0.96, bounds.getHeight() * 0.94);
        }

        private void show() {
            dialog.show();
        }
    }

    private static final class StatusIconFactory {

        private StatusIconFactory() {
        }

        private static Node create(Status status) {
            Color main = status == Status.SUCCESS ? Color.web("#1F9D55") : Color.web("#D64545");
            Color dark = status == Status.SUCCESS ? Color.web("#0F6F3A") : Color.web("#9F2525");
            Color light = status == Status.SUCCESS ? Color.web("#7EE2A8") : Color.web("#FF9D9D");

            Circle glow = new Circle(45);
            glow.setFill(Color.color(main.getRed(), main.getGreen(), main.getBlue(), 0.14));

            Circle outer = new Circle(39);
            outer.setFill(new RadialGradient(240, 0.32, 0.32, 0.25, 0.92, true, CycleMethod.NO_CYCLE,
                    new Stop(0, light),
                    new Stop(0.62, main),
                    new Stop(1, dark)));
            outer.setStroke(Color.color(1, 1, 1, 0.85));
            outer.setStrokeWidth(2.2);
            outer.setEffect(new DropShadow(14, Color.color(0, 0, 0, 0.25)));

            Circle ring = new Circle(43);
            ring.setFill(Color.TRANSPARENT);
            ring.setStroke(Color.color(main.getRed(), main.getGreen(), main.getBlue(), 0.38));
            ring.setStrokeWidth(3.0);

            Circle highlight = new Circle(10);
            highlight.setFill(Color.color(1, 1, 1, 0.36));
            highlight.setTranslateX(-13);
            highlight.setTranslateY(-15);

            StackPane icon = new StackPane(glow, ring, outer, highlight, symbol(status));
            icon.setMinSize(104, 104);
            icon.setPrefSize(104, 104);
            icon.setMaxSize(104, 104);
            return icon;
        }

        private static Node symbol(Status status) {
            if (status == Status.SUCCESS) {
                Polyline check = new Polyline(-18, 1, -5, 15, 22, -17);
                check.setStroke(Color.WHITE);
                check.setStrokeWidth(7.2);
                check.setStrokeLineCap(StrokeLineCap.ROUND);
                check.setFill(Color.TRANSPARENT);
                check.setEffect(new DropShadow(2, Color.color(0, 0, 0, 0.22)));
                return check;
            }
            Line first = crossLine(-18, -18, 18, 18);
            Line second = crossLine(18, -18, -18, 18);
            StackPane cross = new StackPane(first, second);
            cross.setEffect(new DropShadow(2, Color.color(0, 0, 0, 0.22)));
            return cross;
        }

        private static Line crossLine(double startX, double startY, double endX, double endY) {
            Line line = new Line(startX, startY, endX, endY);
            line.setStroke(Color.WHITE);
            line.setStrokeWidth(7.0);
            line.setStrokeLineCap(StrokeLineCap.ROUND);
            return line;
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
        return Optional.ofNullable(OperationResultDialog.class.getResource("/css/app-light.css"));
    }

    private static String clean(String value, String fallback) {
        String normalized = Objects.toString(value, "").strip();
        return normalized.isBlank() ? fallback : normalized;
    }
}
