package com.marcosmoreira.domainmodelstudio.presentation.drawing.c4;

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
 * Formas C4 y despliegue técnico. No contiene reglas de layout ni conoce
 * documentos de arquitectura: solo compone símbolos visuales reutilizables.
 */
public final class C4ShapeKit extends AbstractPrimitiveShapeKit {

    private static final Set<DiagramSymbol> SUPPORTED = Set.of(
            DiagramSymbol.C4_PERSON,
            DiagramSymbol.C4_CLIENT,
            DiagramSymbol.C4_SYSTEM,
            DiagramSymbol.C4_EXTERNAL_SYSTEM,
            DiagramSymbol.C4_APPLICATION,
            DiagramSymbol.C4_API,
            DiagramSymbol.C4_CONTAINER,
            DiagramSymbol.C4_SERVICE,
            DiagramSymbol.C4_DATABASE,
            DiagramSymbol.C4_BOUNDARY,
            DiagramSymbol.C4_ENVIRONMENT,
            DiagramSymbol.C4_NETWORK,
            DiagramSymbol.C4_SERVER,
            DiagramSymbol.C4_ARTIFACT
    );

    @Override
    protected Set<DiagramSymbol> supportedSymbols() {
        return SUPPORTED;
    }

    @Override
    public Node createShape(DiagramSymbol symbol, DiagramShapeStyle style) {
        return switch (symbol) {
            case C4_PERSON -> personSymbol();
            case C4_CLIENT -> clientSymbol();
            case C4_SYSTEM -> systemSymbol();
            case C4_EXTERNAL_SYSTEM -> externalSystemSymbol();
            case C4_APPLICATION -> applicationSymbol();
            case C4_API -> apiSymbol();
            case C4_CONTAINER -> containerSymbol();
            case C4_SERVICE -> serviceSymbol();
            case C4_DATABASE -> databaseSymbol();
            case C4_BOUNDARY -> boundarySymbol();
            case C4_ENVIRONMENT -> environmentSymbol();
            case C4_NETWORK -> networkSymbol();
            case C4_SERVER -> serverSymbol();
            case C4_ARTIFACT -> artifactSymbol();
            default -> throw unsupported(symbol);
        };
    }

    public Node personSymbol() {
        Group person = new Group();
        Circle head = circle(11.0, 5.0, 4.0, "architecture-canvas-symbol-person");
        Line body = line(11.0, 9.0, 11.0, 20.0, "architecture-canvas-symbol-person-line");
        Line arms = line(4.0, 13.5, 18.0, 13.5, "architecture-canvas-symbol-person-line");
        Line leftLeg = line(11.0, 20.0, 5.0, 27.0, "architecture-canvas-symbol-person-line");
        Line rightLeg = line(11.0, 20.0, 17.0, 27.0, "architecture-canvas-symbol-person-line");
        person.getChildren().addAll(head, body, arms, leftLeg, rightLeg);
        person.getStyleClass().add("architecture-canvas-symbol-person-group");
        return person;
    }

    public Node clientSymbol() {
        Group client = new Group();
        Rectangle screen = rounded(24.0, 15.0, 3.0, "architecture-canvas-symbol-client-screen");
        Line base = line(7.0, 20.0, 17.0, 20.0, "architecture-canvas-symbol-client-line");
        Line stand = line(12.0, 15.0, 12.0, 20.0, "architecture-canvas-symbol-client-line");
        client.getChildren().addAll(screen, stand, base);
        return client;
    }

    public Node systemSymbol() {
        Rectangle rectangle = rounded(24.0, 16.0, 5.0, "architecture-canvas-symbol-rect");
        rectangle.getStyleClass().add("architecture-canvas-symbol-system");
        return rectangle;
    }

    public Node externalSystemSymbol() {
        Group external = new Group();
        Rectangle rectangle = rounded(24.0, 16.0, 4.0, "architecture-canvas-symbol-external-system");
        Line northWest = line(2.0, 2.0, 8.0, 2.0, "architecture-canvas-symbol-external-line");
        Line northEast = line(16.0, 2.0, 22.0, 2.0, "architecture-canvas-symbol-external-line");
        Line southWest = line(2.0, 14.0, 8.0, 14.0, "architecture-canvas-symbol-external-line");
        Line southEast = line(16.0, 14.0, 22.0, 14.0, "architecture-canvas-symbol-external-line");
        external.getChildren().addAll(rectangle, northWest, northEast, southWest, southEast);
        return external;
    }

    public Node applicationSymbol() {
        Group app = new Group();
        Rectangle window = rounded(24.0, 17.0, 4.0, "architecture-canvas-symbol-application");
        Line topBar = line(0.0, 5.0, 24.0, 5.0, "architecture-canvas-symbol-application-line");
        Circle dot = circle(4.0, 2.8, 1.0, "architecture-canvas-symbol-application-dot");
        app.getChildren().addAll(window, topBar, dot);
        return app;
    }

    public Node apiSymbol() {
        Group api = new Group();
        Rectangle box = rounded(24.0, 16.0, 5.0, "architecture-canvas-symbol-api");
        Line portIn = line(0.0, 8.0, 6.0, 8.0, "architecture-canvas-symbol-api-line");
        Line portOut = line(18.0, 8.0, 24.0, 8.0, "architecture-canvas-symbol-api-line");
        Line mid = line(9.0, 5.0, 15.0, 11.0, "architecture-canvas-symbol-api-line");
        api.getChildren().addAll(box, portIn, portOut, mid);
        return api;
    }

    public Node containerSymbol() {
        Rectangle rectangle = rounded(24.0, 16.0, 7.0, "architecture-canvas-symbol-rect");
        rectangle.getStyleClass().add("architecture-canvas-symbol-container");
        return rectangle;
    }

    public Node serviceSymbol() {
        Group service = new Group();
        Circle main = circle(12.0, 9.0, 7.0, "architecture-canvas-symbol-service");
        Circle smallA = circle(4.0, 5.0, 3.0, "architecture-canvas-symbol-service-small");
        Circle smallB = circle(20.0, 14.0, 3.0, "architecture-canvas-symbol-service-small");
        service.getChildren().addAll(main, smallA, smallB);
        return service;
    }

    public Node databaseSymbol() {
        Group database = new Group();
        Rectangle body = new Rectangle(0.0, 5.0, 24.0, 16.0);
        body.getStyleClass().add("architecture-canvas-symbol-database-body");
        Ellipse top = new Ellipse(12.0, 5.0, 12.0, 4.5);
        top.getStyleClass().add("architecture-canvas-symbol-database");
        Ellipse bottom = new Ellipse(12.0, 21.0, 12.0, 4.5);
        bottom.getStyleClass().add("architecture-canvas-symbol-database-bottom");
        database.getChildren().addAll(body, bottom, top);
        database.getStyleClass().add("architecture-canvas-symbol-database-group");
        return database;
    }

    public Node boundarySymbol() {
        return rounded(24.0, 16.0, 3.0, "architecture-canvas-symbol-boundary");
    }

    public Node environmentSymbol() {
        Group environment = new Group();
        Rectangle outer = rounded(25.0, 17.0, 5.0, "architecture-canvas-symbol-environment");
        Rectangle inner = rounded(14.0, 8.0, 3.0, "architecture-canvas-symbol-environment-inner");
        inner.relocate(5.0, 4.5);
        environment.getChildren().addAll(outer, inner);
        return environment;
    }

    public Node networkSymbol() {
        Group network = new Group();
        Circle a = circle(4.0, 4.0, 2.4, "architecture-canvas-symbol-network-node");
        Circle b = circle(19.0, 6.0, 2.4, "architecture-canvas-symbol-network-node");
        Circle c = circle(11.0, 17.0, 2.4, "architecture-canvas-symbol-network-node");
        Line ab = line(6.0, 4.2, 16.6, 5.6, "architecture-canvas-symbol-network-line");
        Line ac = line(5.2, 5.7, 9.8, 15.0, "architecture-canvas-symbol-network-line");
        Line bc = line(17.6, 8.0, 12.4, 15.2, "architecture-canvas-symbol-network-line");
        network.getChildren().addAll(ab, ac, bc, a, b, c);
        return network;
    }

    public Node serverSymbol() {
        Group server = new Group();
        Rectangle rack = rounded(24.0, 17.0, 3.0, "architecture-canvas-symbol-server");
        Line slot1 = line(4.0, 6.0, 20.0, 6.0, "architecture-canvas-symbol-server-line");
        Line slot2 = line(4.0, 10.5, 20.0, 10.5, "architecture-canvas-symbol-server-line");
        Circle led = circle(19.0, 14.0, 1.4, "architecture-canvas-symbol-server-led");
        server.getChildren().addAll(rack, slot1, slot2, led);
        return server;
    }

    public Node artifactSymbol() {
        Group artifact = new Group();
        Polygon document = new Polygon(
                0.0, 0.0,
                17.0, 0.0,
                24.0, 7.0,
                24.0, 20.0,
                0.0, 20.0
        );
        document.getStyleClass().add("architecture-canvas-symbol-artifact");
        Line fold = line(17.0, 0.0, 17.0, 7.0, "architecture-canvas-symbol-artifact-line");
        Line foldBottom = line(17.0, 7.0, 24.0, 7.0, "architecture-canvas-symbol-artifact-line");
        artifact.getChildren().addAll(document, fold, foldBottom);
        return artifact;
    }

    private static Rectangle rounded(double width, double height, double arc, String styleClass) {
        Rectangle rectangle = new Rectangle(width, height);
        rectangle.setArcWidth(arc);
        rectangle.setArcHeight(arc);
        rectangle.getStyleClass().add(styleClass);
        return rectangle;
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
