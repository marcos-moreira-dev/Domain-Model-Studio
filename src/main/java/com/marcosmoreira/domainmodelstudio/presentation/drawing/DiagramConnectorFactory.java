package com.marcosmoreira.domainmodelstudio.presentation.drawing;

import java.util.List;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;

/** Fábrica común para conectores vectoriales. */
public final class DiagramConnectorFactory {

    private final DiagramArrowFactory arrowFactory;
    private final DiagramTextFactory textFactory;

    public DiagramConnectorFactory() {
        this(new DiagramArrowFactory(), new DiagramTextFactory());
    }

    public DiagramConnectorFactory(DiagramArrowFactory arrowFactory, DiagramTextFactory textFactory) {
        this.arrowFactory = arrowFactory == null ? new DiagramArrowFactory() : arrowFactory;
        this.textFactory = textFactory == null ? new DiagramTextFactory() : textFactory;
    }

    public Group polyline(List<Point2D> points, String labelText, DiagramConnectorStyle style) {
        List<Point2D> safePoints = points == null ? List.of() : List.copyOf(points);
        Group group = new Group();
        if (safePoints.size() < 2) {
            return group;
        }
        DiagramConnectorStyle safeStyle = style == null ? DiagramConnectorStyle.directed("", false) : style;
        Polyline line = new Polyline();
        line.getStyleClass().addAll(safeStyle.lineStyleClasses());
        line.setFill(Color.TRANSPARENT);
        line.setStyle("-fx-fill: transparent;");
        if (safeStyle.dashed()) {
            line.getStrokeDashArray().addAll(8.0, 5.0);
        }
        for (Point2D point : safePoints) {
            line.getPoints().addAll(point.getX(), point.getY());
        }
        Point2D previous = safePoints.get(safePoints.size() - 2);
        Point2D end = safePoints.get(safePoints.size() - 1);
        Node arrow = arrowFactory.create(previous, end, safeStyle);
        group.getChildren().addAll(line, arrow);
        if (labelText != null && !labelText.isBlank()) {
            Label label = textFactory.connectorLabel(labelText, 140.0);
            label.getStyleClass().addAll(safeStyle.labelStyleClasses());
            Point2D labelPoint = safePoints.get(safePoints.size() / 2);
            label.relocate(labelPoint.getX() - 48.0, labelPoint.getY() - 22.0);
            group.getChildren().add(label);
        }
        return group;
    }
}
