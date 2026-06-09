package com.marcosmoreira.domainmodelstudio.presentation.drawing.uml;

import com.marcosmoreira.domainmodelstudio.presentation.drawing.AbstractPrimitiveShapeKit;
import com.marcosmoreira.domainmodelstudio.presentation.drawing.DiagramShapeStyle;
import com.marcosmoreira.domainmodelstudio.presentation.drawing.DiagramSymbol;
import java.util.Set;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

/**
 * Kit de formas UML construido con primitivas JavaFX simples.
 *
 * <p>Este kit no conoce ViewModels, adapters ni tipos concretos de proyecto. Su
 * responsabilidad es componer símbolos UML mínimos y reutilizables para que los
 * render kits no vuelvan a dibujar cajas genéricas.</p>
 */
public final class UmlShapeKit extends AbstractPrimitiveShapeKit {

    private static final Set<DiagramSymbol> SUPPORTED = Set.of(
            DiagramSymbol.UML_ACTOR,
            DiagramSymbol.UML_USE_CASE,
            DiagramSymbol.UML_SYSTEM_BOUNDARY,
            DiagramSymbol.UML_INITIAL_NODE,
            DiagramSymbol.UML_FINAL_NODE,
            DiagramSymbol.UML_ACTION,
            DiagramSymbol.UML_DECISION,
            DiagramSymbol.UML_FORK_JOIN,
            DiagramSymbol.UML_STATE,
            DiagramSymbol.UML_NOTE
    );

    @Override
    protected Set<DiagramSymbol> supportedSymbols() {
        return SUPPORTED;
    }

    @Override
    public Node createShape(DiagramSymbol symbol, DiagramShapeStyle style) {
        return switch (symbol) {
            case UML_ACTOR -> actorSymbol();
            case UML_USE_CASE -> useCaseSymbol();
            case UML_SYSTEM_BOUNDARY -> systemBoundarySymbol();
            case UML_INITIAL_NODE -> initialStateSymbol();
            case UML_FINAL_NODE -> finalStateSymbol();
            case UML_ACTION -> actionSymbol();
            case UML_DECISION -> activityDecisionSymbol();
            case UML_FORK_JOIN -> forkJoinSymbol();
            case UML_STATE -> stateSymbol();
            case UML_NOTE -> noteSymbol();
            default -> throw unsupported(symbol);
        };
    }

    public Node actorSymbol() {
        Group actor = new Group();
        Circle head = circle(12.0, 6.0, 4.5, "behavior-canvas-symbol-actor");
        Line body = line(12.0, 10.5, 12.0, 22.0, "behavior-canvas-symbol-actor-line");
        Line arms = line(4.5, 15.0, 19.5, 15.0, "behavior-canvas-symbol-actor-line");
        Line leftLeg = line(12.0, 22.0, 6.0, 30.0, "behavior-canvas-symbol-actor-line");
        Line rightLeg = line(12.0, 22.0, 18.0, 30.0, "behavior-canvas-symbol-actor-line");
        actor.getChildren().addAll(head, body, arms, leftLeg, rightLeg);
        actor.getStyleClass().add("behavior-canvas-symbol-actor-group");
        return actor;
    }

    public Node useCaseSymbol() {
        Ellipse ellipse = new Ellipse(18.0, 10.0);
        ellipse.getStyleClass().add("behavior-canvas-symbol-use-case");
        return ellipse;
    }

    public Node systemBoundarySymbol() {
        Rectangle rectangle = new Rectangle(32.0, 22.0);
        rectangle.getStyleClass().add("behavior-canvas-symbol-system-boundary");
        rectangle.setArcWidth(4.0);
        rectangle.setArcHeight(4.0);
        return rectangle;
    }

    public Node initialStateSymbol() {
        Circle circle = new Circle(11.0);
        circle.getStyleClass().add("behavior-canvas-symbol-initial-state");
        return circle;
    }

    public Node finalStateSymbol() {
        Group group = new Group();
        Circle outer = circle(13.0, 13.0, 12.0, "behavior-canvas-symbol-final-state-outer");
        Circle inner = circle(13.0, 13.0, 6.5, "behavior-canvas-symbol-final-state-inner");
        group.getChildren().addAll(outer, inner);
        group.getStyleClass().add("behavior-canvas-symbol-final-state-group");
        return group;
    }

    public Node activityDecisionSymbol() {
        Polygon diamond = new Polygon(0.0, 12.0, 12.0, 0.0, 24.0, 12.0, 12.0, 24.0);
        diamond.getStyleClass().add("behavior-canvas-symbol-diamond");
        return diamond;
    }

    public Node forkJoinSymbol() {
        Rectangle bar = new Rectangle(28.0, 5.0);
        bar.getStyleClass().add("behavior-canvas-symbol-fork-join");
        return bar;
    }


    public Node actionSymbol() {
        Rectangle rectangle = new Rectangle(34.0, 22.0);
        rectangle.getStyleClass().add("behavior-canvas-symbol-uml-action");
        rectangle.setArcWidth(12.0);
        rectangle.setArcHeight(12.0);
        return rectangle;
    }

    public Node stateSymbol() {
        Rectangle rectangle = new Rectangle(34.0, 22.0);
        rectangle.getStyleClass().add("behavior-canvas-symbol-uml-state");
        rectangle.setArcWidth(10.0);
        rectangle.setArcHeight(10.0);
        return rectangle;
    }

    public Node noteSymbol() {
        Group group = new Group();
        Polygon note = new Polygon(
                0.0, 0.0,
                28.0, 0.0,
                34.0, 6.0,
                34.0, 24.0,
                0.0, 24.0);
        note.getStyleClass().add("behavior-canvas-symbol-uml-note");
        Line fold = line(28.0, 0.0, 28.0, 6.0, "behavior-canvas-symbol-uml-note-fold");
        Line foldEdge = line(28.0, 6.0, 34.0, 6.0, "behavior-canvas-symbol-uml-note-fold");
        group.getChildren().addAll(note, fold, foldEdge);
        return group;
    }

    private static Circle circle(double radius) {
        return new Circle(radius);
    }

    private static Circle circle(double centerX, double centerY, double radius, String styleClass) {
        Circle circle = new Circle(centerX, centerY, radius);
        circle.getStyleClass().add(styleClass);
        return circle;
    }

    private static Line line(double startX, double startY, double endX, double endY, String styleClass) {
        Line line = new Line(startX, startY, endX, endY);
        line.getStyleClass().add(styleClass);
        return line;
    }
}
