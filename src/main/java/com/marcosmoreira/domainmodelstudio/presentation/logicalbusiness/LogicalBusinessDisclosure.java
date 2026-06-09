package com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness;

import java.util.function.Consumer;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;

/** Seccion colapsable efimera para listas densas del SideDock del levantamiento logico. */
final class LogicalBusinessDisclosure {

    private LogicalBusinessDisclosure() {
    }

    static TitledPane section(
            String title,
            String countText,
            String metaText,
            Parent content,
            boolean expanded,
            Consumer<Boolean> onExpandedChange,
            String... extraStyleClasses
    ) {
        TitledPane pane = new TitledPane();
        pane.getStyleClass().add("logical-business-disclosure");
        if (extraStyleClasses != null) {
            for (String styleClass : extraStyleClasses) {
                if (styleClass != null && !styleClass.isBlank()) {
                    pane.getStyleClass().add(styleClass);
                }
            }
        }
        pane.setText("");
        pane.setGraphic(header(title, countText, metaText));
        pane.setContent(content);
        pane.setAnimated(false);
        pane.setCollapsible(true);
        pane.setExpanded(expanded);
        pane.setMaxWidth(Double.MAX_VALUE);
        pane.setTooltip(new Tooltip(headerText(title, countText, metaText)));
        pane.expandedProperty().addListener((observable, oldValue, newValue) -> {
            if (onExpandedChange != null) {
                onExpandedChange.accept(Boolean.TRUE.equals(newValue));
            }
        });
        return pane;
    }

    static VBox body() {
        VBox body = new VBox(6);
        body.getStyleClass().add("logical-business-disclosure-body");
        return body;
    }

    private static VBox header(String title, String countText, String metaText) {
        VBox header = new VBox(1);
        header.getStyleClass().add("logical-business-disclosure-header");
        Label titleLabel = new Label(headerTitle(title, countText));
        titleLabel.setWrapText(true);
        titleLabel.getStyleClass().add("logical-business-disclosure-title");
        header.getChildren().add(titleLabel);
        if (metaText != null && !metaText.isBlank()) {
            Label meta = new Label(LogicalBusinessDisplayText.clean(metaText));
            meta.setWrapText(true);
            meta.getStyleClass().add("logical-business-disclosure-meta");
            header.getChildren().add(meta);
        }
        return header;
    }

    private static String headerTitle(String title, String countText) {
        String cleanTitle = LogicalBusinessDisplayText.clean(title);
        String cleanCount = LogicalBusinessDisplayText.clean(countText);
        return cleanCount.isBlank() ? cleanTitle : cleanTitle + " · " + cleanCount;
    }

    private static String headerText(String title, String countText, String metaText) {
        String header = headerTitle(title, countText);
        String cleanMeta = LogicalBusinessDisplayText.clean(metaText);
        return cleanMeta.isBlank() ? header : header + " — " + cleanMeta;
    }
}
