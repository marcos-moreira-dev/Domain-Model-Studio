package com.marcosmoreira.domainmodelstudio.presentation.dialogs;

import javafx.scene.control.Label;

/** Renderiza líneas semánticas del Markdown teórico sin mostrar marcas internas. */
final class ManualTheoryLineRenderer {

    private ManualTheoryLineRenderer() {
    }

    static Label render(String line) {
        String raw = line == null ? "" : line.strip();
        if (raw.isBlank()) {
            return null;
        }
        String style = "manual-paragraph";
        String text = raw;
        if (raw.startsWith("LIST::")) {
            style = "manual-bullet";
            text = "• " + raw.substring("LIST::".length()).strip();
        } else if (raw.startsWith("PARAGRAPH::")) {
            text = raw.substring("PARAGRAPH::".length()).strip();
        } else if (raw.startsWith("SUBHEADING::")) {
            style = "manual-block-subtitle";
            text = raw.substring("SUBHEADING::".length()).strip();
        } else if (raw.startsWith("EXAMPLE::")) {
            style = "manual-example-line";
            text = raw.substring("EXAMPLE::".length());
        } else if (raw.startsWith("### ")) {
            style = "manual-block-subtitle";
            text = raw.substring(4).strip();
        } else if (raw.startsWith("#### ")) {
            style = "manual-block-subtitle";
            text = raw.substring(5).strip();
        }
        if (text.isBlank()) {
            return null;
        }
        Label label = new Label(text);
        label.getStyleClass().add(style);
        label.setWrapText(true);
        return label;
    }
}
