package com.marcosmoreira.domainmodelstudio.presentation.behavior;

import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.presentation.drawing.DiagramDrawingFacade;
import com.marcosmoreira.domainmodelstudio.presentation.drawing.bpmn.BpmnShapeKit;
import com.marcosmoreira.domainmodelstudio.presentation.drawing.process.OperationalFlowShapeKit;
import com.marcosmoreira.domainmodelstudio.presentation.drawing.uml.UmlShapeKit;
import com.marcosmoreira.domainmodelstudio.presentation.drawing.DiagramConnectorStyle;
import com.marcosmoreira.domainmodelstudio.presentation.drawing.DiagramArrowKind;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasBounds;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasConnectorGeometry;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasNodeViewFactory;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasAdapter;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasConnector;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasNode;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasRenderKit;
import java.util.Optional;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

/** Renderizador sobrio para BPMN básico, flujo operativo y UML de comportamiento. */
public final class BehaviorRenderKit implements InteractiveCanvasRenderKit {

    private static final UmlShapeKit UML_SHAPES = new UmlShapeKit();
    private static final BpmnShapeKit BPMN_SHAPES = new BpmnShapeKit();
    private static final OperationalFlowShapeKit OPERATIONAL_SHAPES = new OperationalFlowShapeKit();
    private static final CanvasNodeViewFactory NODE_VIEW_FACTORY = new CanvasNodeViewFactory();

    @Override
    public Node renderNode(InteractiveCanvasNode node, CanvasBounds bounds, boolean selected) {
        return renderNode(node, bounds, selected, DiagramDrawingFacade.defaults());
    }

    @Override
    public Node renderNode(
            InteractiveCanvasNode node,
            CanvasBounds bounds,
            boolean selected,
            DiagramDrawingFacade drawingFacade
    ) {
        String semanticKind = semanticKind(node.kind());
        if (usesPrimitiveNotation(node.kind(), semanticKind)) {
            return renderPrimitiveNode(node, bounds, selected, semanticKind);
        }

        VBox content = new VBox(4);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(6, 8, 6, 8));
        content.setFillWidth(true);
        content.setPrefSize(bounds.width(), bounds.height());
        content.setMaxSize(bounds.width(), bounds.height());
        double textWidth = Math.max(48.0, bounds.width() - 26.0);
        content.getChildren().add(symbolFor(node.kind()));
        orderHintFor(node.kind()).ifPresent(text -> {
            Label orderHint = singleLineLabel(text, "behavior-canvas-node-order-hint", textWidth);
            content.getChildren().add(orderHint);
        });

        Label title = wrappedLabel(node.title().isBlank() ? node.id() : node.title(), "behavior-canvas-node-title", textWidth);
        title.setAlignment(Pos.CENTER);
        content.getChildren().add(title);

        if (!node.subtitle().isBlank()) {
            String[] lines = node.subtitle().split("\\R", 2);
            Label kind = singleLineLabel(lines[0], "behavior-canvas-node-kind", textWidth);
            content.getChildren().add(kind);
            if (lines.length > 1 && !lines[1].isBlank()) {
                Label detail = wrappedLabel(lines[1], "behavior-canvas-node-detail", textWidth);
                detail.setAlignment(Pos.CENTER);
                content.getChildren().add(detail);
            }
        }

        return NODE_VIEW_FACTORY.wrap(
                node.id(),
                bounds,
                content,
                selected,
                "behavior-canvas-node",
                "behavior-canvas-node-" + node.kind(),
                selected ? "behavior-canvas-node-selected" : ""
        );
    }


    private static Label wrappedLabel(String text, String styleClass, double maxWidth) {
        Label label = new Label(text == null ? "" : text);
        label.getStyleClass().add(styleClass);
        label.setWrapText(true);
        label.setTextOverrun(OverrunStyle.CLIP);
        label.setMaxWidth(maxWidth);
        label.setPrefWidth(maxWidth);
        label.setMinHeight(Region.USE_PREF_SIZE);
        return label;
    }

    private static Label singleLineLabel(String text, String styleClass, double maxWidth) {
        Label label = new Label(text == null ? "" : text);
        label.getStyleClass().add(styleClass);
        label.setTextOverrun(OverrunStyle.CLIP);
        label.setMaxWidth(maxWidth);
        return label;
    }

    @Override
    public Node renderConnector(InteractiveCanvasConnector connector, InteractiveCanvasAdapter adapter, boolean selected) {
        return renderConnector(connector, adapter, selected, DiagramDrawingFacade.defaults());
    }

    @Override
    public Node renderConnector(
            InteractiveCanvasConnector connector,
            InteractiveCanvasAdapter adapter,
            boolean selected,
            DiagramDrawingFacade drawingFacade
    ) {
        Optional<NodeLayout> sourceLayout = adapter.layoutForNode(connector.sourceNodeId());
        Optional<NodeLayout> targetLayout = adapter.layoutForNode(connector.targetNodeId());
        if (sourceLayout.isEmpty() || targetLayout.isEmpty()) {
            return new Group();
        }
        DiagramDrawingFacade safeDrawingFacade = drawingFacade == null ? DiagramDrawingFacade.defaults() : drawingFacade;
        var points = isUseCaseConnector(connector)
                ? UseCaseConnectorGeometry.route(connector, adapter, sourceLayout.get(), targetLayout.get(), safeDrawingFacade)
                : CanvasConnectorGeometry.edgeToEdgePoints(
                        sourceLayout.get(),
                        targetLayout.get(),
                        adapter.layoutForConnector(connector.id()),
                        safeDrawingFacade);
        DiagramConnectorStyle style = connectorStyle(connector, selected);
        Group group = safeDrawingFacade.connectors().polyline(points, "", style);
        group.getStyleClass().add("behavior-connector-visual-group");
        group.setUserData(connector.id());
        return group;
    }

    private static boolean isUseCaseConnector(InteractiveCanvasConnector connector) {
        String connectorKind = connector.kind() == null ? "" : connector.kind();
        return connectorKind.startsWith("uml-use-case-");
    }

    private static Node renderPrimitiveNode(
            InteractiveCanvasNode node,
            CanvasBounds bounds,
            boolean selected,
            String semanticKind
    ) {
        StackPane content = new StackPane();
        content.setAlignment(Pos.CENTER);
        content.setPrefSize(bounds.width(), bounds.height());
        content.setMinSize(bounds.width(), bounds.height());
        content.setMaxSize(bounds.width(), bounds.height());
        content.getStyleClass().add("behavior-canvas-primitive-content");
        content.getStyleClass().add("behavior-canvas-primitive-content-" + semanticKind);

        Node shape = primitiveShape(node.kind(), semanticKind, bounds);
        shape.getStyleClass().add("behavior-canvas-primitive-shape");
        content.getChildren().add(shape);

        Label title = wrappedLabel(
                node.title().isBlank() ? node.id() : node.title(),
                "behavior-canvas-primitive-title",
                primitiveTextWidth(semanticKind, bounds));
        title.getStyleClass().add("behavior-canvas-primitive-title-" + semanticKind);
        title.setAlignment(Pos.CENTER);
        title.setMaxHeight(primitiveTextHeight(semanticKind, bounds));
        if ("actor".equals(semanticKind)) {
            StackPane.setAlignment(title, Pos.BOTTOM_CENTER);
            StackPane.setMargin(title, new Insets(0.0, 4.0, 4.0, 4.0));
        } else if ("system-boundary".equals(semanticKind) || "lane".equals(semanticKind)) {
            StackPane.setAlignment(title, Pos.TOP_LEFT);
            StackPane.setMargin(title, new Insets(6.0, 8.0, 0.0, 8.0));
        }
        content.getChildren().add(title);

        return NODE_VIEW_FACTORY.wrap(
                node.id(),
                bounds,
                content,
                selected,
                "behavior-canvas-primitive-node",
                "behavior-canvas-primitive-node-" + semanticKind,
                "behavior-canvas-node-" + node.kind(),
                selected ? "behavior-canvas-primitive-node-selected" : ""
        );
    }

    private static boolean usesPrimitiveNotation(String visualKind, String semanticKind) {
        String normalized = visualKind == null ? "" : visualKind;
        return normalized.startsWith("uml-use-case-")
                || normalized.startsWith("uml-activity-")
                || normalized.startsWith("uml-state-")
                || normalized.startsWith("bpmn-")
                || semanticKind.equals("actor")
                || semanticKind.equals("use-case")
                || semanticKind.equals("system-boundary")
                || semanticKind.equals("initial-state")
                || semanticKind.equals("final-state")
                || semanticKind.equals("action")
                || semanticKind.equals("state")
                || semanticKind.equals("decision")
                || semanticKind.equals("fork")
                || semanticKind.equals("join")
                || semanticKind.equals("start-event")
                || semanticKind.equals("end-event")
                || semanticKind.equals("task")
                || semanticKind.equals("lane");
    }

    private static Node primitiveShape(String visualKind, String semanticKind, CanvasBounds bounds) {
        return switch (semanticKind) {
            case "actor" -> actorShape(bounds);
            case "use-case" -> ellipseShape(bounds, "behavior-canvas-symbol-use-case");
            case "system-boundary", "lane" -> rectangleShape(bounds, 0.0, "behavior-canvas-symbol-system-boundary");
            case "initial-state" -> circleShape(bounds, "behavior-canvas-symbol-initial-state");
            case "start-event" -> circleShape(bounds, "behavior-canvas-symbol-start-event");
            case "final-state" -> finalStateShape(bounds);
            case "end-event" -> circleShape(bounds, "behavior-canvas-symbol-end-event");
            case "decision" -> diamondShape(bounds, "behavior-canvas-symbol-diamond");
            case "fork", "join" -> forkJoinShape(bounds);
            case "action", "task" -> rectangleShape(bounds, 12.0, "behavior-canvas-symbol-uml-action");
            case "state" -> rectangleShape(bounds, 10.0, "behavior-canvas-symbol-uml-state");
            default -> visualKind != null && visualKind.startsWith("bpmn-")
                    ? rectangleShape(bounds, 12.0, "behavior-canvas-symbol-task")
                    : rectangleShape(bounds, 8.0, "behavior-canvas-symbol-uml-action");
        };
    }

    private static Node actorShape(CanvasBounds bounds) {
        Group actor = new Group();
        double centerX = bounds.width() / 2.0;
        double top = Math.max(6.0, bounds.height() * 0.12);
        double headRadius = Math.min(9.0, Math.max(5.0, bounds.height() * 0.08));
        Circle head = new Circle(centerX, top + headRadius, headRadius);
        head.getStyleClass().add("behavior-canvas-symbol-actor");
        double bodyTop = top + headRadius * 2.0 + 2.0;
        double bodyBottom = Math.min(bounds.height() - 28.0, bodyTop + Math.max(22.0, bounds.height() * 0.28));
        double armY = bodyTop + Math.max(7.0, (bodyBottom - bodyTop) * 0.32);
        double armHalf = Math.min(22.0, bounds.width() * 0.22);
        double legHalf = Math.min(16.0, bounds.width() * 0.16);
        Line body = line(centerX, bodyTop, centerX, bodyBottom, "behavior-canvas-symbol-actor-line");
        Line arms = line(centerX - armHalf, armY, centerX + armHalf, armY, "behavior-canvas-symbol-actor-line");
        Line leftLeg = line(centerX, bodyBottom, centerX - legHalf, Math.min(bounds.height() - 16.0, bodyBottom + 22.0), "behavior-canvas-symbol-actor-line");
        Line rightLeg = line(centerX, bodyBottom, centerX + legHalf, Math.min(bounds.height() - 16.0, bodyBottom + 22.0), "behavior-canvas-symbol-actor-line");
        actor.getChildren().addAll(head, body, arms, leftLeg, rightLeg);
        return actor;
    }

    private static Node ellipseShape(CanvasBounds bounds, String styleClass) {
        Ellipse ellipse = new Ellipse(
                bounds.width() / 2.0,
                bounds.height() / 2.0,
                Math.max(18.0, bounds.width() / 2.0 - 6.0),
                Math.max(12.0, bounds.height() / 2.0 - 8.0)
        );
        ellipse.getStyleClass().add(styleClass);
        return ellipse;
    }

    private static Node circleShape(CanvasBounds bounds, String styleClass) {
        double radius = Math.min(bounds.width(), bounds.height()) / 2.0 - 8.0;
        Circle circle = new Circle(bounds.width() / 2.0, bounds.height() / 2.0, Math.max(8.0, radius));
        circle.getStyleClass().add(styleClass);
        return circle;
    }

    private static Node finalStateShape(CanvasBounds bounds) {
        Group group = new Group();
        double radius = Math.max(10.0, Math.min(bounds.width(), bounds.height()) / 2.0 - 8.0);
        Circle outer = new Circle(bounds.width() / 2.0, bounds.height() / 2.0, radius);
        outer.getStyleClass().add("behavior-canvas-symbol-final-state-outer");
        Circle inner = new Circle(bounds.width() / 2.0, bounds.height() / 2.0, Math.max(5.5, radius * 0.55));
        inner.getStyleClass().add("behavior-canvas-symbol-final-state-inner");
        group.getChildren().addAll(outer, inner);
        return group;
    }

    private static Node rectangleShape(CanvasBounds bounds, double arc, String styleClass) {
        Rectangle rectangle = new Rectangle(6.0, 6.0, Math.max(20.0, bounds.width() - 12.0), Math.max(16.0, bounds.height() - 12.0));
        rectangle.setArcWidth(arc);
        rectangle.setArcHeight(arc);
        rectangle.getStyleClass().add(styleClass);
        return rectangle;
    }

    private static Node diamondShape(CanvasBounds bounds, String styleClass) {
        double centerX = bounds.width() / 2.0;
        double centerY = bounds.height() / 2.0;
        double halfWidth = Math.max(18.0, bounds.width() / 2.0 - 8.0);
        double halfHeight = Math.max(16.0, bounds.height() / 2.0 - 8.0);
        Polygon diamond = new Polygon(
                centerX, centerY - halfHeight,
                centerX + halfWidth, centerY,
                centerX, centerY + halfHeight,
                centerX - halfWidth, centerY
        );
        diamond.getStyleClass().add(styleClass);
        return diamond;
    }

    private static Node forkJoinShape(CanvasBounds bounds) {
        Rectangle bar = new Rectangle(
                Math.max(6.0, bounds.width() * 0.12),
                bounds.height() / 2.0 - 3.0,
                Math.max(24.0, bounds.width() * 0.76),
                6.0
        );
        bar.getStyleClass().add("behavior-canvas-symbol-fork-join");
        return bar;
    }

    private static double primitiveTextWidth(String semanticKind, CanvasBounds bounds) {
        if ("decision".equals(semanticKind)) {
            return Math.max(54.0, bounds.width() * 0.58);
        }
        return Math.max(30.0, bounds.width() - primitiveHorizontalInset(semanticKind));
    }

    private static double primitiveTextHeight(String semanticKind, CanvasBounds bounds) {
        if ("decision".equals(semanticKind)) {
            return Math.max(46.0, bounds.height() * 0.58);
        }
        return Region.USE_PREF_SIZE;
    }

    private static double primitiveHorizontalInset(String semanticKind) {
        return switch (semanticKind) {
            case "use-case" -> 34.0;
            case "actor" -> 12.0;
            default -> 20.0;
        };
    }

    private static Line line(double startX, double startY, double endX, double endY, String styleClass) {
        Line line = new Line(startX, startY, endX, endY);
        line.getStyleClass().add(styleClass);
        return line;
    }


    private static Node symbolFor(String kind) {
        String normalized = kind == null ? "" : kind;
        String base = semanticKind(normalized);
        return switch (normalized) {
            case "bpmn-start-event", "operational-start-event" -> BPMN_SHAPES.startEventSymbol();
            case "bpmn-end-event", "operational-end-event" -> BPMN_SHAPES.endEventSymbol();
            case "bpmn-decision" -> BPMN_SHAPES.gatewaySymbol();
            case "bpmn-lane" -> BPMN_SHAPES.laneSymbol();
            case "bpmn-note" -> BPMN_SHAPES.documentSymbol();
            case "operational-activity" -> OPERATIONAL_SHAPES.stepSymbol();
            case "operational-lane" -> OPERATIONAL_SHAPES.responsibleSymbol();
            case "operational-decision" -> OPERATIONAL_SHAPES.decisionSymbol();
            case "operational-note" -> OPERATIONAL_SHAPES.documentSymbol();
            default -> switch (base) {
                case "actor" -> UML_SHAPES.actorSymbol();
                case "use-case" -> UML_SHAPES.useCaseSymbol();
                case "system-boundary" -> UML_SHAPES.systemBoundarySymbol();
                case "initial-state" -> UML_SHAPES.initialStateSymbol();
                case "final-state" -> UML_SHAPES.finalStateSymbol();
                case "action" -> UML_SHAPES.actionSymbol();
                case "state" -> UML_SHAPES.stateSymbol();
                case "decision" -> UML_SHAPES.activityDecisionSymbol();
                case "fork", "join" -> UML_SHAPES.forkJoinSymbol();
                case "note" -> UML_SHAPES.noteSymbol();
                case "start-event" -> BPMN_SHAPES.startEventSymbol();
                case "end-event" -> BPMN_SHAPES.endEventSymbol();
                case "lane", "responsible" -> BPMN_SHAPES.laneSymbol();
                case "document", "evidence" -> BPMN_SHAPES.documentSymbol();
                default -> BPMN_SHAPES.taskSymbol();
            };
        };
    }



    private static String semanticKind(String visualKind) {
        String normalized = visualKind == null ? "" : visualKind;
        return normalized
                .replaceFirst("^bpmn-", "")
                .replaceFirst("^operational-", "")
                .replaceFirst("^uml-use-case-", "")
                .replaceFirst("^uml-activity-", "")
                .replaceFirst("^uml-state-", "");
    }

    private static Optional<String> orderHintFor(String kind) {
        String normalized = kind == null ? "" : kind;
        return switch (normalized) {
            case "operational-activity" -> Optional.of("Paso");
            case "operational-decision" -> Optional.of("Decisión");
            case "operational-note" -> Optional.of("Doc.");
            case "uml-activity-decision" -> Optional.of("Guarda");
            case "uml-activity-fork", "uml-activity-join" -> Optional.of("Paralelo");
            case "uml-state-state" -> Optional.of("Estado");
            default -> Optional.empty();
        };
    }

    private static DiagramConnectorStyle connectorStyle(InteractiveCanvasConnector connector, boolean selected) {
        String kind = connector.kind() == null ? "" : connector.kind();
        String baseKind = semanticKind(kind);
        boolean dashed = baseKind.equals("include") || baseKind.equals("extend") || baseKind.equals("return-message");
        DiagramArrowKind arrowKind = switch (baseKind) {
            case "association" -> DiagramArrowKind.NONE;
            case "include", "extend" -> DiagramArrowKind.OPEN;
            case "generalization" -> DiagramArrowKind.HOLLOW_TRIANGLE;
            default -> DiagramArrowKind.FILLED_TRIANGLE;
        };
        DiagramConnectorStyle style = DiagramConnectorStyle
                .directed("behavior-canvas-connector-" + kind, selected)
                .withLineStyleClass("behavior-canvas-connector")
                .withArrowStyleClass("behavior-canvas-arrow-head")
                .withLabelStyleClass("behavior-canvas-connector-label")
                .withArrowKind(arrowKind)
                .withDashed(dashed);
        if (selected) {
            style = style
                    .withLineStyleClass("behavior-canvas-connector-selected")
                    .withArrowStyleClass("behavior-canvas-arrow-head-selected");
        }
        return style;
    }
}
