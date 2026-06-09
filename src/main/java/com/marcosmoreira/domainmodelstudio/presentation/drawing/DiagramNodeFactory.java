package com.marcosmoreira.domainmodelstudio.presentation.drawing;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;

/** Fábrica de nodos visuales sobrios para diagramas. */
public final class DiagramNodeFactory {

    private final DiagramDrawingPrimitives primitives;
    private final DiagramTextFactory textFactory;
    private final DiagramSelectionDecorationFactory selectionFactory;

    public DiagramNodeFactory() {
        this(new DiagramDrawingPrimitives(), new DiagramTextFactory(), new DiagramSelectionDecorationFactory());
    }

    public DiagramNodeFactory(
            DiagramDrawingPrimitives primitives,
            DiagramTextFactory textFactory,
            DiagramSelectionDecorationFactory selectionFactory
    ) {
        this.primitives = primitives == null ? new DiagramDrawingPrimitives() : primitives;
        this.textFactory = textFactory == null ? new DiagramTextFactory() : textFactory;
        this.selectionFactory = selectionFactory == null ? new DiagramSelectionDecorationFactory() : selectionFactory;
    }

    public Group card(
            String id,
            double x,
            double y,
            double width,
            double height,
            String title,
            String subtitle,
            DiagramShapeStyle style,
            boolean selected
    ) {
        Group group = new Group();
        group.setLayoutX(x);
        group.setLayoutY(y);
        Rectangle background = primitives.roundedRectangle(0.0, 0.0, width, height, style);
        Label titleLabel = textFactory.title(title == null || title.isBlank() ? id : title, Math.max(20.0, width - 24.0));
        titleLabel.relocate(12.0, 10.0);
        group.getChildren().addAll(background, titleLabel);
        if (subtitle != null && !subtitle.isBlank()) {
            Label subtitleLabel = textFactory.subtitle(subtitle, Math.max(20.0, width - 24.0));
            subtitleLabel.relocate(12.0, 32.0);
            group.getChildren().add(subtitleLabel);
        }
        if (selected) {
            group.getChildren().add(selectionFactory.selectionHalo(0.0, 0.0, width, height));
        }
        group.setUserData(id);
        return group;
    }

    public Group packageBox(
            String id,
            double x,
            double y,
            double width,
            double height,
            String title,
            DiagramShapeStyle style,
            boolean selected
    ) {
        Group group = new Group();
        group.setLayoutX(x);
        group.setLayoutY(y);
        String visibleTitle = title == null || title.isBlank() ? id : title;
        double tabTextWidth = Math.min(Math.max(96.0, approximateTextWidth(visibleTitle) + 30.0), Math.max(108.0, width - 24.0));
        Node background = primitives.packageTab(0.0, 0.0, width, height, style, tabTextWidth);
        Label titleLabel = textFactory.title(visibleTitle, Math.max(20.0, tabTextWidth - 24.0));
        titleLabel.relocate(12.0, 8.0);
        group.getChildren().addAll(background, titleLabel);
        if (selected) {
            group.getChildren().add(selectionFactory.selectionHalo(0.0, 0.0, width, height));
        }
        group.setUserData(id);
        return group;
    }

    private static double approximateTextWidth(String text) {
        return (text == null ? 0 : text.strip().length()) * 7.2;
    }
}
