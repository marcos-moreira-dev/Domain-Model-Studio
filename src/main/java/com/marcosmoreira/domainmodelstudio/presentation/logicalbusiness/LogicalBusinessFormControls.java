package com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

/** Controles reutilizables del formulario documental del levantamiento lógico. */
final class LogicalBusinessFormControls {

    private LogicalBusinessFormControls() {
    }

    static VBox form(String heading, String summary) {
        VBox form = new VBox(12);
        form.getStyleClass().add("logical-business-ficha");
        form.getChildren().add(sectionTitle(heading));
        form.getChildren().add(paragraph(summary));
        return form;
    }

    static VBox formRow(String labelText, Region control) {
        VBox row = new VBox(5);
        row.getStyleClass().add("logical-business-form-row");
        row.getChildren().add(label(labelText, "logical-business-field-label"));
        row.getChildren().add(control);
        VBox.setVgrow(control, Priority.NEVER);
        return row;
    }

    static TextField textField(String value) {
        TextField field = new TextField(editableText(value));
        field.setPromptText("Sin dato en Markdown.");
        field.getStyleClass().add("logical-business-input");
        return field;
    }

    static TextArea textArea(String value, int rows) {
        TextArea area = new TextArea(editableText(value));
        area.setPromptText("Sin dato en Markdown.");
        area.setWrapText(true);
        area.setPrefRowCount(rows);
        area.getStyleClass().add("logical-business-input-area");
        return area;
    }

    static <T> ComboBox<T> combo(T selected, T[] values, Function<T, String> labeler) {
        ComboBox<T> box = new ComboBox<>();
        box.getItems().setAll(values);
        box.setValue(selected == null && values.length > 0 ? values[0] : selected);
        box.setConverter(new StringConverter<>() {
            @Override
            public String toString(T value) {
                return value == null ? "—" : labeler.apply(value);
            }

            @Override
            public T fromString(String string) {
                return box.getValue();
            }
        });
        box.getStyleClass().add("logical-business-input");
        return box;
    }

    static Button applyButton(Runnable action) {
        Button button = new Button("Actualizar documento");
        button.getStyleClass().add("logical-business-apply-button");
        button.setOnAction(event -> action.run());
        return button;
    }

    static VBox formRow(String labelText, CheckBox control) {
        VBox row = new VBox(5);
        row.getStyleClass().add("logical-business-form-row");
        row.getChildren().add(label(labelText, "logical-business-field-label"));
        row.getChildren().add(control);
        return row;
    }

    static void addReadOnly(VBox target, String label, String value) {
        if (value != null && !value.isBlank()) {
            target.getChildren().add(fieldBlock(label, value));
        }
    }

    static void addListField(VBox target, String label, List<String> values) {
        if (values != null && !values.isEmpty()) {
            target.getChildren().add(fieldBlock(label, values));
        }
    }

    static HBox metricRow(Region... metrics) {
        HBox row = new HBox(8);
        row.getStyleClass().add("logical-business-metric-row");
        row.getChildren().addAll(metrics);
        for (Region metric : metrics) {
            HBox.setHgrow(metric, Priority.ALWAYS);
        }
        return row;
    }

    static VBox metric(String label, String value) {
        VBox box = new VBox(2);
        box.getStyleClass().add("logical-business-metric");
        box.getChildren().addAll(label(value, "logical-business-metric-value"),
                label(label, "logical-business-metric-label"));
        return box;
    }

    static HBox chipRow(List<String> values) {
        HBox row = new HBox(6);
        row.getStyleClass().add("logical-business-chip-row");
        values.stream().filter(value -> value != null && !value.isBlank())
                .forEach(value -> row.getChildren().add(label(value, "logical-business-chip")));
        return row;
    }

    static Label emptyNotice(String value) {
        return label(value, "logical-business-empty-notice");
    }

    static Label title(String value) {
        return label(value, "logical-business-document-title");
    }

    static Label sectionTitle(String value) {
        return label(value, "logical-business-section-title");
    }

    static Label paragraph(String value) {
        return label(LogicalBusinessDisplayText.clean(value), "logical-business-document-text");
    }

    static Label label(String value, String styleClass) {
        Label label = new Label(LogicalBusinessDisplayText.clean(value));
        label.setWrapText(true);
        label.getStyleClass().add(styleClass);
        return label;
    }

    static LocalDate safeDate(LocalDate date) {
        return date == null ? LocalDate.now() : date;
    }

    static List<String> parseList(String value) {
        if (value == null || value.isBlank()) {
            return List.of();
        }
        return Arrays.stream(value.replace(',', '\n').split("\\R"))
                .map(String::strip)
                .filter(line -> !line.isBlank())
                .toList();
    }

    static String markdownPreview(String markdown, int limit) {
        if (markdown == null || markdown.isBlank()) {
            return "Salida Markdown disponible.";
        }
        return markdown.lines().filter(line -> !line.isBlank()).limit(limit)
                .reduce((left, right) -> left + "\n" + right)
                .orElse("Salida Markdown disponible.");
    }

    private static VBox fieldBlock(String label, String value) {
        VBox block = block(label);
        block.getChildren().add(paragraph(value));
        return block;
    }

    private static VBox fieldBlock(String label, List<String> values) {
        VBox block = block(label);
        if (values.isEmpty()) {
            block.getChildren().add(emptyNotice("Sin datos registrados."));
            return block;
        }
        values.forEach(value -> block.getChildren().add(label("• " + value, "logical-business-list-row")));
        return block;
    }

    private static VBox block(String label) {
        VBox block = new VBox(6);
        block.getStyleClass().add("logical-business-field");
        if (label.contains("Referencia") || label.contains("Fuente")) {
            block.getStyleClass().add("logical-business-reference-list");
        }
        block.getChildren().add(label(label, "logical-business-field-label"));
        return block;
    }

    private static String editableText(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }
        String cleaned = LogicalBusinessDisplayText.clean(value);
        return "—".equals(cleaned) ? "" : cleaned;
    }
}
