package com.marcosmoreira.domainmodelstudio.presentation.dialogs;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

/** Primitivas compartidas para dibujar figuras académicas del módulo de ayuda. */
final class ManualFigureDrawingSupport {

    private ManualFigureDrawingSupport() {
    }

    static void screen(Pane p, double x, double y, String text) {
        rectangle(p, x, y, 95, 56, "");
        line(p, x, y + 18, x + 95, y + 18);
        text(p, text, x + 17, y + 38);
    }

    static void participant(Pane p, double x, String title) {
        rectangle(p, x, 24, 60, 28, title);
        Line lifeline = line(p, x + 30, 52, x + 30, 150);
        lifeline.getStrokeDashArray().addAll(5.0, 4.0);
    }

    static void node(Pane p, double x, double y, double w, double h, String title) {
        rectangle(p, x, y, w, h, title);
        line(p, x + 12, y, x + 22, y - 10);
        line(p, x + w, y, x + w + 10, y - 10);
        line(p, x + 22, y - 10, x + w + 10, y - 10);
        line(p, x + w + 10, y - 10, x + w + 10, y + h - 10);
        line(p, x + w, y + h, x + w + 10, y + h - 10);
    }

    static void cylinder(Pane p, double x, double y, String title) {
        Rectangle body = new Rectangle(x - 32, y - 18, 64, 38);
        styleShape(body);
        p.getChildren().add(body);
        ellipseLike(p, x - 32, y - 28, 64, 22, title);
    }

    static void stickPerson(Pane p, double x, double y, String label) {
        circle(p, x, y - 35, 13, "");
        line(p, x, y - 22, x, y + 15);
        line(p, x - 22, y - 8, x + 22, y - 8);
        line(p, x, y + 15, x - 20, y + 42);
        line(p, x, y + 15, x + 20, y + 42);
        text(p, label, x - 25, y + 62);
    }

    static Rectangle rectangle(Pane p, double x, double y, double w, double h, String label) {
        Rectangle rectangle = new Rectangle(x, y, w, h);
        styleShape(rectangle);
        p.getChildren().add(rectangle);
        addCenteredMultilineText(p, label, x, y, w, h);
        return rectangle;
    }

    static Rectangle rounded(Pane p, double x, double y, double w, double h, String label) {
        Rectangle rectangle = rectangle(p, x, y, w, h, label);
        rectangle.setArcWidth(16);
        rectangle.setArcHeight(16);
        return rectangle;
    }

    static void ellipseLike(Pane p, double x, double y, double w, double h, String label) {
        Rectangle ellipse = rounded(p, x, y, w, h, label);
        ellipse.setArcWidth(h);
        ellipse.setArcHeight(h);
    }

    static void diamond(Pane p, double centerX, double centerY, double w, double h, String label) {
        Polygon polygon = new Polygon(
                centerX, centerY - h / 2,
                centerX + w / 2, centerY,
                centerX, centerY + h / 2,
                centerX - w / 2, centerY);
        styleShape(polygon);
        p.getChildren().add(polygon);
        addCenteredMultilineText(p, label, centerX - w / 2, centerY - h / 2, w, h);
    }

    static void circle(Pane p, double x, double y, double r, String label) {
        Circle circle = new Circle(x, y, r);
        styleShape(circle);
        p.getChildren().add(circle);
        addCenteredMultilineText(p, label, x - r, y - r, r * 2, r * 2);
    }

    static Line line(Pane p, double x1, double y1, double x2, double y2) {
        Line line = new Line(x1, y1, x2, y2);
        line.getStyleClass().add("manual-figure-line");
        p.getChildren().add(line);
        return line;
    }

    static void arrow(Pane p, double x1, double y1, double x2, double y2) {
        line(p, x1, y1, x2, y2);
        double angle = Math.atan2(y2 - y1, x2 - x1);
        double size = 7;
        Polygon head = new Polygon(
                x2, y2,
                x2 - size * Math.cos(angle - Math.PI / 6), y2 - size * Math.sin(angle - Math.PI / 6),
                x2 - size * Math.cos(angle + Math.PI / 6), y2 - size * Math.sin(angle + Math.PI / 6));
        head.getStyleClass().add("manual-figure-arrow-head");
        p.getChildren().add(head);
    }

    static void label(Pane p, String value, double x, double y) {
        text(p, value, x, y);
    }

    static void text(Pane p, String value, double x, double y) {
        Text text = new Text(x, y, value);
        text.getStyleClass().add("manual-figure-text");
        p.getChildren().add(text);
    }

    static void addCenteredMultilineText(Pane p, String value, double x, double y, double w, double h) {
        if (value == null || value.isBlank()) {
            return;
        }
        String[] lines = value.split("\\n");
        double startY = y + h / 2.0 - ((lines.length - 1) * 7.0) + 4.0;
        for (int i = 0; i < lines.length; i++) {
            Text text = new Text(lines[i]);
            text.getStyleClass().add("manual-figure-text");
            double estimatedWidth = lines[i].length() * 6.0;
            text.setX(x + Math.max(4, (w - estimatedWidth) / 2.0));
            text.setY(startY + i * 16.0);
            p.getChildren().add(text);
        }
    }

    static void styleShape(Shape shape) {
        shape.getStyleClass().add("manual-figure-shape");
    }
}
