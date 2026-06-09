package com.marcosmoreira.domainmodelstudio.presentation.drawing.admin;

import com.marcosmoreira.domainmodelstudio.presentation.drawing.AbstractPrimitiveShapeKit;
import com.marcosmoreira.domainmodelstudio.presentation.drawing.DiagramShapeStyle;
import com.marcosmoreira.domainmodelstudio.presentation.drawing.DiagramSymbol;
import java.util.Set;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

/** Convenciones visuales para diagramas administrativos no normados por UML/BPMN. */
public final class AdminShapeKit extends AbstractPrimitiveShapeKit {

    private static final Set<DiagramSymbol> SUPPORTED = Set.of(
            DiagramSymbol.ADMIN_MODULE,
            DiagramSymbol.ADMIN_SCREEN,
            DiagramSymbol.ADMIN_PERMISSION_MATRIX
    );

    @Override
    protected Set<DiagramSymbol> supportedSymbols() {
        return SUPPORTED;
    }

    @Override
    public Node createShape(DiagramSymbol symbol, DiagramShapeStyle style) {
        return switch (symbol) {
            case ADMIN_MODULE -> moduleSymbol();
            case ADMIN_SCREEN -> screenSymbol();
            case ADMIN_PERMISSION_MATRIX -> permissionMatrixSymbol();
            default -> throw unsupported(symbol);
        };
    }

    public Node moduleSymbol() {
        Rectangle rectangle = straight(28.0, 16.0, "admin-canvas-symbol-module");
        return rectangle;
    }

    public Node screenSymbol() {
        Group screen = new Group();
        Rectangle frame = straight(30.0, 20.0, "admin-canvas-symbol-screen");
        Line header = new Line(0.0, 5.0, 30.0, 5.0);
        header.getStyleClass().add("admin-canvas-symbol-screen-line");
        screen.getChildren().addAll(frame, header);
        return screen;
    }

    public Node permissionMatrixSymbol() {
        Group matrix = new Group();
        Rectangle frame = straight(28.0, 20.0, "admin-canvas-symbol-matrix");
        for (double x : new double[]{9.0, 18.0}) {
            Line column = new Line(x, 0.0, x, 20.0);
            column.getStyleClass().add("admin-canvas-symbol-matrix-line");
            matrix.getChildren().add(column);
        }
        for (double y : new double[]{6.5, 13.0}) {
            Line row = new Line(0.0, y, 28.0, y);
            row.getStyleClass().add("admin-canvas-symbol-matrix-line");
            matrix.getChildren().add(row);
        }
        matrix.getChildren().add(0, frame);
        return matrix;
    }

    private static Rectangle straight(double width, double height, String styleClass) {
        Rectangle rectangle = new Rectangle(width, height);
        rectangle.setArcWidth(0.0);
        rectangle.setArcHeight(0.0);
        rectangle.getStyleClass().add(styleClass);
        return rectangle;
    }
}
