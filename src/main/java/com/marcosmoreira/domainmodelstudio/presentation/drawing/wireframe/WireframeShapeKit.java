package com.marcosmoreira.domainmodelstudio.presentation.drawing.wireframe;

import com.marcosmoreira.domainmodelstudio.presentation.drawing.AbstractPrimitiveShapeKit;
import com.marcosmoreira.domainmodelstudio.presentation.drawing.DiagramShapeStyle;
import com.marcosmoreira.domainmodelstudio.presentation.drawing.DiagramSymbol;
import java.util.Set;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

/** Formas primitivas para maquetas administrativas tipo wireframe. */
public final class WireframeShapeKit extends AbstractPrimitiveShapeKit {

    private static final Set<DiagramSymbol> SUPPORTED = Set.of(
            DiagramSymbol.WIREFRAME_SCREEN,
            DiagramSymbol.WIREFRAME_TABLE,
            DiagramSymbol.WIREFRAME_FIELD,
            DiagramSymbol.WIREFRAME_BUTTON
    );

    @Override
    protected Set<DiagramSymbol> supportedSymbols() {
        return SUPPORTED;
    }

    @Override
    public Node createShape(DiagramSymbol symbol, DiagramShapeStyle style) {
        return switch (symbol) {
            case WIREFRAME_SCREEN -> screenFrameSymbol();
            case WIREFRAME_TABLE -> tableSymbol();
            case WIREFRAME_FIELD -> fieldSymbol();
            case WIREFRAME_BUTTON -> buttonSymbol();
            default -> throw unsupported(symbol);
        };
    }

    public Node screenFrameSymbol() {
        Group group = new Group();
        Rectangle frame = rounded(34.0, 22.0, 3.0, "wireframe-symbol-screen");
        Line topbar = new Line(0.0, 5.0, 34.0, 5.0);
        topbar.getStyleClass().add("wireframe-symbol-line");
        group.getChildren().addAll(frame, topbar);
        return group;
    }

    public Node tableSymbol() {
        Group table = new Group();
        Rectangle frame = rounded(32.0, 20.0, 2.0, "wireframe-symbol-table");
        table.getChildren().add(frame);
        for (double y : new double[]{6.0, 12.0}) {
            Line row = new Line(0.0, y, 32.0, y);
            row.getStyleClass().add("wireframe-symbol-line");
            table.getChildren().add(row);
        }
        for (double x : new double[]{10.0, 22.0}) {
            Line col = new Line(x, 0.0, x, 20.0);
            col.getStyleClass().add("wireframe-symbol-line");
            table.getChildren().add(col);
        }
        return table;
    }

    public Node fieldSymbol() {
        return rounded(30.0, 9.0, 2.0, "wireframe-symbol-field");
    }

    public Node buttonSymbol() {
        return rounded(24.0, 9.0, 4.0, "wireframe-symbol-button");
    }

    private static Rectangle rounded(double width, double height, double arc, String styleClass) {
        Rectangle rectangle = new Rectangle(width, height);
        rectangle.setArcWidth(arc);
        rectangle.setArcHeight(arc);
        rectangle.getStyleClass().add(styleClass);
        return rectangle;
    }
}
