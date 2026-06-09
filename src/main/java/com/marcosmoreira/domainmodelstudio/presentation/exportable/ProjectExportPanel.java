package com.marcosmoreira.domainmodelstudio.presentation.exportable;

import com.marcosmoreira.domainmodelstudio.presentation.toolbar.ToolbarIcon;
import java.util.Objects;
import javafx.beans.value.ObservableBooleanValue;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;

/** Panel lateral reutilizable para salidas documentales del proyecto activo. */
public final class ProjectExportPanel {

    private final ScrollPane root;

    public ProjectExportPanel(
            String title,
            Runnable exportPdf,
            Runnable exportMarkdown,
            ObservableBooleanValue unavailable
    ) {
        Objects.requireNonNull(exportPdf, "exportPdf");
        Objects.requireNonNull(exportMarkdown, "exportMarkdown");
        VBox content = new VBox(8);
        content.setPadding(new Insets(10));
        content.getStyleClass().add("logical-business-side-panel");
        content.getChildren().add(header(title));
        content.getChildren().add(actionButton(
                "PDF",
                "Exportar PDF",
                ToolbarIcon.EXPORT_PDF,
                exportPdf,
                unavailable));
        content.getChildren().add(actionButton(
                "Markdown",
                "Exportar Markdown",
                ToolbarIcon.EXPORT_MARKDOWN,
                exportMarkdown,
                unavailable));

        this.root = new ScrollPane(content);
        this.root.setFitToWidth(true);
        this.root.getStyleClass().add("logical-business-help-scroll");
    }

    public Parent root() {
        return root;
    }

    private static Label header(String title) {
        Label label = new Label(clean(title, "Exportar"));
        label.setWrapText(true);
        label.getStyleClass().add("logical-business-panel-subtitle");
        return label;
    }

    private static Button actionButton(
            String text,
            String tooltip,
            ToolbarIcon icon,
            Runnable action,
            ObservableBooleanValue unavailable
    ) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.getStyleClass().addAll("logical-business-side-action", "logical-business-side-button");
        button.setGraphic(icon.imageView());
        button.setContentDisplay(ContentDisplay.LEFT);
        button.setGraphicTextGap(6);
        button.setTooltip(new Tooltip(tooltip));
        if (unavailable != null) {
            button.disableProperty().bind(unavailable);
        }
        button.setOnAction(event -> action.run());
        return button;
    }

    private static String clean(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value.strip();
    }
}
