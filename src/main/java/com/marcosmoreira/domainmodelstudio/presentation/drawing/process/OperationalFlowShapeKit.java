package com.marcosmoreira.domainmodelstudio.presentation.drawing.process;

import com.marcosmoreira.domainmodelstudio.presentation.drawing.AbstractPrimitiveShapeKit;
import com.marcosmoreira.domainmodelstudio.presentation.drawing.DiagramShapeStyle;
import com.marcosmoreira.domainmodelstudio.presentation.drawing.DiagramSymbol;
import java.util.Set;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

/** Formas primitivas para flujo operativo; no pretende ser BPMN. */
public final class OperationalFlowShapeKit extends AbstractPrimitiveShapeKit {

    private static final Set<DiagramSymbol> SUPPORTED = Set.of(
            DiagramSymbol.OPERATIONAL_STEP,
            DiagramSymbol.OPERATIONAL_RESPONSIBLE,
            DiagramSymbol.OPERATIONAL_DECISION,
            DiagramSymbol.OPERATIONAL_DOCUMENT
    );

    @Override
    protected Set<DiagramSymbol> supportedSymbols() {
        return SUPPORTED;
    }

    @Override
    public Node createShape(DiagramSymbol symbol, DiagramShapeStyle style) {
        return switch (symbol) {
            case OPERATIONAL_STEP -> stepSymbol();
            case OPERATIONAL_RESPONSIBLE -> responsibleSymbol();
            case OPERATIONAL_DECISION -> decisionSymbol();
            case OPERATIONAL_DOCUMENT -> documentSymbol();
            default -> throw unsupported(symbol);
        };
    }

    public Node stepSymbol() {
        Group group = new Group();
        Rectangle step = new Rectangle(34.0, 18.0);
        step.setArcWidth(5.0);
        step.setArcHeight(5.0);
        step.getStyleClass().add("behavior-canvas-symbol-operational-step");
        Rectangle numberBand = new Rectangle(8.0, 18.0);
        numberBand.getStyleClass().add("behavior-canvas-symbol-operational-step-band");
        group.getChildren().addAll(step, numberBand);
        return group;
    }

    public Node responsibleSymbol() {
        Group group = new Group();
        Rectangle band = new Rectangle(34.0, 18.0);
        band.setArcWidth(4.0);
        band.setArcHeight(4.0);
        band.getStyleClass().add("behavior-canvas-symbol-responsible-band");
        Rectangle labelBand = new Rectangle(34.0, 5.0);
        labelBand.getStyleClass().add("behavior-canvas-symbol-responsible-label-band");
        group.getChildren().addAll(band, labelBand);
        return group;
    }

    public Node decisionSymbol() {
        Polygon diamond = new Polygon(0.0, 12.0, 12.0, 0.0, 24.0, 12.0, 12.0, 24.0);
        diamond.getStyleClass().add("behavior-canvas-symbol-operational-decision");
        return diamond;
    }

    public Node documentSymbol() {
        Polygon document = new Polygon(
                0.0, 0.0,
                28.0, 0.0,
                28.0, 19.0,
                20.0, 16.0,
                14.0, 19.0,
                8.0, 16.0,
                0.0, 19.0
        );
        document.getStyleClass().add("behavior-canvas-symbol-operational-document");
        return document;
    }
}
