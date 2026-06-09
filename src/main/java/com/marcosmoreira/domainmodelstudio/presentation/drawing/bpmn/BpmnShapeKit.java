package com.marcosmoreira.domainmodelstudio.presentation.drawing.bpmn;

import com.marcosmoreira.domainmodelstudio.presentation.drawing.AbstractPrimitiveShapeKit;
import com.marcosmoreira.domainmodelstudio.presentation.drawing.DiagramShapeStyle;
import com.marcosmoreira.domainmodelstudio.presentation.drawing.DiagramSymbol;
import java.util.Set;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

/** Formas BPMN básicas, desacopladas del render kit de comportamiento. */
public final class BpmnShapeKit extends AbstractPrimitiveShapeKit {

    private static final Set<DiagramSymbol> SUPPORTED = Set.of(
            DiagramSymbol.BPMN_START_EVENT,
            DiagramSymbol.BPMN_END_EVENT,
            DiagramSymbol.BPMN_TASK,
            DiagramSymbol.BPMN_GATEWAY,
            DiagramSymbol.BPMN_DOCUMENT,
            DiagramSymbol.BPMN_LANE
    );

    @Override
    protected Set<DiagramSymbol> supportedSymbols() {
        return SUPPORTED;
    }

    @Override
    public Node createShape(DiagramSymbol symbol, DiagramShapeStyle style) {
        return switch (symbol) {
            case BPMN_START_EVENT -> startEventSymbol();
            case BPMN_END_EVENT -> endEventSymbol();
            case BPMN_TASK -> taskSymbol();
            case BPMN_GATEWAY -> gatewaySymbol();
            case BPMN_DOCUMENT -> documentSymbol();
            case BPMN_LANE -> laneSymbol();
            default -> throw unsupported(symbol);
        };
    }

    public Node startEventSymbol() {
        Ellipse ellipse = new Ellipse(13.0, 13.0);
        ellipse.getStyleClass().add("behavior-canvas-symbol-circle");
        ellipse.getStyleClass().add("behavior-canvas-symbol-start-event");
        return ellipse;
    }

    public Node endEventSymbol() {
        Ellipse ellipse = new Ellipse(13.0, 13.0);
        ellipse.getStyleClass().add("behavior-canvas-symbol-circle");
        ellipse.getStyleClass().add("behavior-canvas-symbol-end-event");
        return ellipse;
    }

    public Node taskSymbol() {
        Rectangle rectangle = new Rectangle(32.0, 14.0);
        rectangle.setArcWidth(10.0);
        rectangle.setArcHeight(10.0);
        rectangle.getStyleClass().add("behavior-canvas-symbol-rect");
        rectangle.getStyleClass().add("behavior-canvas-symbol-task");
        return rectangle;
    }

    public Node gatewaySymbol() {
        Polygon diamond = new Polygon(0.0, 12.0, 12.0, 0.0, 24.0, 12.0, 12.0, 24.0);
        diamond.getStyleClass().add("behavior-canvas-symbol-diamond");
        diamond.getStyleClass().add("behavior-canvas-symbol-gateway");
        return diamond;
    }

    public Node documentSymbol() {
        Polygon document = new Polygon(
                0.0, 0.0,
                28.0, 0.0,
                28.0, 18.0,
                21.0, 15.0,
                14.0, 18.0,
                7.0, 15.0,
                0.0, 18.0
        );
        document.getStyleClass().add("behavior-canvas-symbol-document");
        return document;
    }

    public Node laneSymbol() {
        Group group = new Group();
        Rectangle lane = new Rectangle(32.0, 18.0);
        lane.setArcWidth(4.0);
        lane.setArcHeight(4.0);
        lane.getStyleClass().add("behavior-canvas-symbol-lane");
        Rectangle band = new Rectangle(6.0, 18.0);
        band.getStyleClass().add("behavior-canvas-symbol-lane-band");
        group.getChildren().addAll(lane, band);
        return group;
    }
}
