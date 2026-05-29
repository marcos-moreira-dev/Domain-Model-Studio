package com.marcosmoreira.domainmodelstudio.presentation.drawing;

import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;

/** Fábrica de texto legible para lienzos de diagrama. */
public final class DiagramTextFactory {

    private final DiagramTooltipPolicy tooltipPolicy;

    public DiagramTextFactory() {
        this(new DiagramTooltipPolicy());
    }

    public DiagramTextFactory(DiagramTooltipPolicy tooltipPolicy) {
        this.tooltipPolicy = tooltipPolicy == null ? new DiagramTooltipPolicy() : tooltipPolicy;
    }

    public Label title(String text, double maxWidth) {
        return label(text, DiagramTextStyle.title(maxWidth), DiagramLabelPolicy.critical());
    }

    public Label subtitle(String text, double maxWidth) {
        return label(text, DiagramTextStyle.subtitle(maxWidth), DiagramLabelPolicy.compactMeta());
    }

    public Label meta(String text, double maxWidth) {
        return label(text, DiagramTextStyle.meta(maxWidth), DiagramLabelPolicy.compactMeta());
    }

    public Label connectorLabel(String text, double maxWidth) {
        return label(text, DiagramTextStyle.connectorLabel(maxWidth), DiagramLabelPolicy.connector());
    }

    public Label label(String text, DiagramTextStyle style, DiagramLabelPolicy policy) {
        DiagramTextStyle safeStyle = style == null ? DiagramTextStyle.meta(0.0) : style;
        DiagramLabelPolicy safePolicy = policy == null ? DiagramLabelPolicy.compactMeta() : policy;
        String clean = text == null ? "" : text.strip();
        Label label = new Label(safePolicy.visibleText(clean));
        label.getStyleClass().addAll(safeStyle.styleClasses());
        label.setWrapText(safeStyle.wrapText());
        label.setTextOverrun(OverrunStyle.CLIP);
        if (safeStyle.maxWidth() > 0.0) {
            label.setMaxWidth(safeStyle.maxWidth());
            label.setPrefWidth(safeStyle.maxWidth());
        }
        tooltipPolicy.attachIfUseful(label, clean, safePolicy.shouldAttachTooltip(clean));
        return label;
    }
}
