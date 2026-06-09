package com.marcosmoreira.domainmodelstudio.presentation.drawing;

import java.util.Set;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

/**
 * Base ligera para kits que dibujan símbolos con primitivas JavaFX.
 *
 * <p>No contiene reglas de UML, BPMN, C4 ni wireframes. Solo evita repetir
 * la creación de círculos, líneas, rectángulos y grupos simples.</p>
 */
public abstract class AbstractPrimitiveShapeKit implements DiagramShapeKit {

    protected abstract Set<DiagramSymbol> supportedSymbols();

    @Override
    public final boolean supports(DiagramSymbol symbol) {
        return symbol != null && supportedSymbols().contains(symbol);
    }

    protected final IllegalArgumentException unsupported(DiagramSymbol symbol) {
        return new IllegalArgumentException("El símbolo " + symbol + " no pertenece a este kit de formas.");
    }

    protected final Circle primitiveCircle(double centerX, double centerY, double radius, String styleClass) {
        Circle circle = new Circle(centerX, centerY, radius);
        addStyle(circle, styleClass);
        return circle;
    }

    protected final Circle primitiveCircle(double radius, String styleClass) {
        Circle circle = new Circle(radius);
        addStyle(circle, styleClass);
        return circle;
    }

    protected final Ellipse primitiveEllipse(double radiusX, double radiusY, String styleClass) {
        Ellipse ellipse = new Ellipse(radiusX, radiusY);
        addStyle(ellipse, styleClass);
        return ellipse;
    }

    protected final Rectangle primitiveRectangle(double width, double height, String styleClass) {
        Rectangle rectangle = new Rectangle(width, height);
        addStyle(rectangle, styleClass);
        return rectangle;
    }

    protected final Rectangle primitiveRoundedRectangle(double width, double height, double arc, String styleClass) {
        Rectangle rectangle = primitiveRectangle(width, height, styleClass);
        rectangle.setArcWidth(arc);
        rectangle.setArcHeight(arc);
        return rectangle;
    }

    protected final Line primitiveLine(double startX, double startY, double endX, double endY, String styleClass) {
        Line line = new Line(startX, startY, endX, endY);
        addStyle(line, styleClass);
        return line;
    }

    protected final Polygon primitivePolygon(String styleClass, double... points) {
        Polygon polygon = new Polygon(points);
        addStyle(polygon, styleClass);
        return polygon;
    }

    protected final Group primitiveGroup(String styleClass, Node... children) {
        Group group = new Group(children);
        addStyle(group, styleClass);
        return group;
    }

    protected final void addStyle(Node node, String styleClass) {
        if (node != null && styleClass != null && !styleClass.isBlank()) {
            node.getStyleClass().add(styleClass.strip());
        }
    }
}
