package com.marcosmoreira.domainmodelstudio.presentation.canvas;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramModel;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.er.AttributeElement;
import com.marcosmoreira.domainmodelstudio.domain.er.AttributeTag;
import com.marcosmoreira.domainmodelstudio.domain.er.EntityElement;
import com.marcosmoreira.domainmodelstudio.domain.er.EntityKind;
import com.marcosmoreira.domainmodelstudio.domain.er.RelationshipElement;
import com.marcosmoreira.domainmodelstudio.domain.er.RelationshipKind;
import com.marcosmoreira.domainmodelstudio.domain.layout.BendPoint;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import com.marcosmoreira.domainmodelstudio.domain.style.ElementStyle;
import com.marcosmoreira.domainmodelstudio.domain.style.DiagramStyleResolver;
import com.marcosmoreira.domainmodelstudio.domain.style.StrokeStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;

/**
 * Renderizador JavaFX para notación Chen.
 *
 * <p>Esta clase pertenece a presentation: conoce JavaFX, pero no parsea Markdown ni
 * modifica la semántica del modelo. Su responsabilidad es transformar un proyecto ya
 * importado y con layout inicial en nodos visuales sobrios.</p>
 */
public final class ChenDiagramRenderer {

    private DiagramProject activeProject;
    private Set<DiagramElementId> currentSelectedElementIds = Set.of();

    public Group render(DiagramProject project) {
        return render(project, Set.of());
    }

    public Group render(DiagramProject project, DiagramElementId selectedElementId) {
        return render(project, selectedElementId == null ? Set.of() : Set.of(selectedElementId));
    }

    public Group render(DiagramProject project, Set<DiagramElementId> selectedElementIds) {
        this.activeProject = project;
        this.currentSelectedElementIds = selectedElementIds == null ? Set.of() : Set.copyOf(selectedElementIds);
        DiagramLayout layout = project.layouts()
                .layoutFor(NotationType.CHEN)
                .orElseGet(() -> DiagramLayout.empty(NotationType.CHEN));
        Group root = new Group();
        addProjectTitle(root, project, layout);
        root.getChildren().addAll(renderConnectors(project.model(), layout));
        root.getChildren().addAll(renderNodes(project.model(), layout));
        root.getChildren().addAll(renderCardinalities(project.model(), layout));
        return root;
    }

    /**
     * Renderiza solo las conexiones y etiquetas derivadas de conexión para previsualización en vivo.
     *
     * <p>No marca elementos como seleccionados para evitar duplicar handles interactivos sobre el canvas
     * real durante un arrastre.</p>
     */
    public Group renderDynamicConnectors(DiagramProject project) {
        this.activeProject = project;
        this.currentSelectedElementIds = Set.of();
        DiagramLayout layout = project.layouts()
                .layoutFor(NotationType.CHEN)
                .orElseGet(() -> DiagramLayout.empty(NotationType.CHEN));
        Group root = new Group();
        root.getStyleClass().add("diagram-live-connector-layer");
        root.setMouseTransparent(true);
        root.getChildren().addAll(renderConnectors(project.model(), layout));
        root.getChildren().addAll(renderCardinalities(project.model(), layout));
        return root;
    }

    private boolean isSelected(DiagramElementId elementId) {
        return elementId != null && currentSelectedElementIds.contains(elementId);
    }


    private void addProjectTitle(Group root, DiagramProject project, DiagramLayout layout) {
        if (project.metadata().title().isBlank() || layout.nodes().isEmpty()) {
            return;
        }
        double minX = layout.nodes().stream().mapToDouble(NodeLayout::x).min().orElse(0.0);
        double maxX = layout.nodes().stream().mapToDouble(node -> node.x() + node.width()).max().orElse(minX + 600.0);
        double minY = layout.nodes().stream().mapToDouble(NodeLayout::y).min().orElse(0.0);
        Label title = new Label(project.metadata().title());
        title.getStyleClass().add("diagram-title-label");
        title.setAlignment(Pos.CENTER);
        title.setLayoutX(minX);
        title.setLayoutY(minY - 58.0);
        title.setPrefWidth(Math.max(360.0, maxX - minX));
        ElementStyle defaultStyle = project.styleSheet().defaultStyle();
        title.setTextFill(javafx.scene.paint.Color.web(defaultStyle.text().color().toHex()));
        title.setFont(Font.font(defaultStyle.text().fontFamily(), 24.0));
        root.getChildren().add(title);
    }

    private Group renderConnectors(DiagramModel model, DiagramLayout layout) {
        Group group = new Group();
        for (ConnectorLayout connector : layout.connectors()) {
            Optional<NodeLayout> source = layout.nodeFor(connector.sourceElementId());
            Optional<NodeLayout> target = layout.nodeFor(connector.targetElementId());
            if (source.isEmpty() || target.isEmpty() || !connector.visible()) {
                continue;
            }
            Group connectorGroup = renderConnector(connector, source.get(), target.get());
            group.getChildren().add(connectorGroup);
        }
        return group;
    }

    private Group renderConnector(ConnectorLayout connector, NodeLayout sourceNode, NodeLayout targetNode) {
        Point2D start = ChenGeometry.anchorPoint(sourceNode, connector.sourceAnchor(), targetNode);
        Point2D end = ChenGeometry.anchorPoint(targetNode, connector.targetAnchor(), sourceNode);
        List<Point2D> route = new ArrayList<>();
        route.add(start);
        for (BendPoint bendPoint : connector.bendPoints()) {
            route.add(new Point2D(bendPoint.x(), bendPoint.y()));
        }
        route.add(end);

        Group connectorGroup = new Group();
        connectorGroup.getStyleClass().add("chen-connector-group");
        connectorGroup.setUserData(connector.connectorId());
        if (isSelected(connector.connectorId())) {
            connectorGroup.getStyleClass().add("chen-selected-connector");
        }
        for (int index = 0; index < route.size() - 1; index++) {
            Point2D a = route.get(index);
            Point2D b = route.get(index + 1);
            Line line = new Line(a.getX(), a.getY(), b.getX(), b.getY());
            line.setUserData(connector.connectorId());
            line.getStyleClass().addAll("chen-connector", "chen-connector-segment");
            applyConnectorStyle(line, connector.connectorId());
            if (isSelected(connector.connectorId())) {
                line.getStyleClass().add("chen-selected-connector-segment");
                line.setStrokeWidth(line.getStrokeWidth() + 1.0);
            }
            connectorGroup.getChildren().add(line);
        }
        if (isSelected(connector.connectorId())) {
            connectorGroup.getChildren().add(endpointHandle(connector.connectorId(), start, true));
            for (int index = 0; index < connector.bendPoints().size(); index++) {
                BendPoint bendPoint = connector.bendPoints().get(index);
                Ellipse handle = bendPointHandle(connector.connectorId(), index, bendPoint.x(), bendPoint.y());
                connectorGroup.getChildren().add(handle);
            }
            connectorGroup.getChildren().add(endpointHandle(connector.connectorId(), end, false));
        }
        return connectorGroup;
    }

    private Ellipse bendPointHandle(DiagramElementId connectorId, int index, double x, double y) {
        Ellipse handle = new Ellipse(x, y, 5.5, 5.5);
        handle.setUserData(connectorId);
        handle.getProperties().put("bendPointIndex", index);
        handle.getStyleClass().add("chen-bend-point-handle");
        return handle;
    }

    private Ellipse endpointHandle(DiagramElementId connectorId, Point2D point, boolean sourceEndpoint) {
        Ellipse handle = new Ellipse(point.getX(), point.getY(), 6.5, 6.5);
        handle.setUserData(connectorId);
        handle.getProperties().put("connectorEndpoint", sourceEndpoint ? "source" : "target");
        handle.getStyleClass().addAll("chen-bend-point-handle", "chen-endpoint-handle");
        return handle;
    }

    private Group renderNodes(DiagramModel model, DiagramLayout layout) {
        Group group = new Group();
        for (NodeLayout node : layout.nodes()) {
            if (!node.visible()) {
                continue;
            }
            Node rendered = renderNode(model, node);
            if (rendered != null) {
                rendered.setUserData(node.elementId());
                rendered.getStyleClass().add("chen-draggable-node");
                if (isSelected(node.elementId())) {
                    rendered.getStyleClass().add("chen-selected-node");
                }
                group.getChildren().add(rendered);
            }
        }
        return group;
    }

    private Node renderNode(DiagramModel model, NodeLayout node) {
        Optional<EntityElement> entity = model.entityById(node.elementId());
        if (entity.isPresent()) {
            return entityFigure(entity.get(), node);
        }

        Optional<RelationshipElement> relationship = model.relationshipById(node.elementId());
        if (relationship.isPresent()) {
            return relationshipFigure(relationship.get(), node);
        }

        Optional<AttributeElement> attribute = attributeById(model, node.elementId());
        return attribute.map(attributeElement -> attributeFigure(attributeElement, node)).orElse(null);
    }

    private Group entityFigure(EntityElement entity, NodeLayout node) {
        Group group = new Group();
        Rectangle rectangle = new Rectangle(node.x(), node.y(), node.width(), node.height());
        rectangle.getStyleClass().add("chen-entity");
        applyStyle(rectangle, entity.id());
        if (entity.kind() == EntityKind.WEAK) {
            rectangle.getStyleClass().add("chen-entity-weak");
            Rectangle outer = new Rectangle(node.x() - 4, node.y() - 4, node.width() + 8, node.height() + 8);
            outer.getStyleClass().add("chen-entity-weak-outline");
            outer.setFill(javafx.scene.paint.Color.TRANSPARENT);
            outer.setStroke(javafx.scene.paint.Color.web("#606060"));
            outer.setStrokeWidth(1.0);
            group.getChildren().add(outer);
        }
        Label label = centeredLabel(entity.name(), node.x(), node.y() + 20, node.width());
        applyTextStyle(label, entity.id(), node.width() * 0.88);
        group.getChildren().addAll(rectangle, label);
        return group;
    }

    private Group attributeFigure(AttributeElement attribute, NodeLayout node) {
        Group group = new Group();
        Ellipse ellipse = new Ellipse(
                ChenGeometry.centerX(node),
                ChenGeometry.centerY(node),
                node.width() / 2.0,
                node.height() / 2.0
        );
        ellipse.getStyleClass().add("chen-attribute");
        applyStyle(ellipse, attribute.id());
        if (attribute.hasTag(AttributeTag.DERIVED)) {
            ellipse.getStyleClass().add("chen-attribute-derived");
            ellipse.getStrokeDashArray().setAll(5.0, 4.0);
        }
        if (attribute.hasTag(AttributeTag.MULTIVALUED)) {
            Ellipse outer = new Ellipse(
                    ChenGeometry.centerX(node),
                    ChenGeometry.centerY(node),
                    node.width() / 2.0 + 4,
                    node.height() / 2.0 + 4
            );
            outer.getStyleClass().add("chen-attribute-multivalued-outline");
            outer.setFill(javafx.scene.paint.Color.TRANSPARENT);
            outer.setStroke(javafx.scene.paint.Color.web("#606060"));
            outer.setStrokeWidth(1.0);
            group.getChildren().add(outer);
        }
        Label label = centeredLabel(attribute.name(), node.x(), node.y() + node.height() / 2.0 - 10, node.width());
        applyTextStyle(label, attribute.id(), node.width() * 0.82);
        if (attribute.isPrimaryKey()) {
            label.getStyleClass().add("chen-attribute-primary-key");
        } else if (attribute.isPartialKey()) {
            label.getStyleClass().add("chen-attribute-partial-key");
        }
        group.getChildren().addAll(ellipse, label);
        return group;
    }

    private Group relationshipFigure(RelationshipElement relationship, NodeLayout node) {
        Group group = new Group();
        Polygon diamond = diamond(node, 0.0);
        diamond.getStyleClass().add("chen-relationship");
        applyStyle(diamond, relationship.id());
        if (relationship.kind() == RelationshipKind.IDENTIFYING) {
            Polygon outer = diamond(node, 5.0);
            outer.getStyleClass().add("chen-relationship-identifying-outline");
            outer.setFill(javafx.scene.paint.Color.TRANSPARENT);
            outer.setStroke(javafx.scene.paint.Color.web("#606060"));
            outer.setStrokeWidth(1.0);
            group.getChildren().add(outer);
        }
        Label label = centeredLabel(relationship.name(), node.x(), node.y() + node.height() / 2.0 - 10, node.width());
        applyTextStyle(label, relationship.id(), node.width() * 0.72);
        group.getChildren().addAll(diamond, label);
        return group;
    }

    private Polygon diamond(NodeLayout node, double expand) {
        double centerX = ChenGeometry.centerX(node);
        double centerY = ChenGeometry.centerY(node);
        double halfWidth = node.width() / 2.0 + expand;
        double halfHeight = node.height() / 2.0 + expand;
        return new Polygon(
                centerX, centerY - halfHeight,
                centerX + halfWidth, centerY,
                centerX, centerY + halfHeight,
                centerX - halfWidth, centerY
        );
    }

    private Group renderCardinalities(DiagramModel model, DiagramLayout layout) {
        Group group = new Group();
        for (RelationshipElement relationship : model.relationships()) {
            Optional<NodeLayout> relationshipNode = layout.nodeFor(relationship.id());
            Optional<NodeLayout> fromNode = layout.nodeFor(relationship.fromEntityId());
            Optional<NodeLayout> toNode = layout.nodeFor(relationship.toEntityId());
            if (relationshipNode.isEmpty()) {
                continue;
            }
            fromNode.ifPresent(node -> group.getChildren().add(cardinalityLabel(
                    relationship.fromCardinality().displayText(),
                    midpointX(node, relationshipNode.get()),
                    midpointY(node, relationshipNode.get()) - 22
            )));
            toNode.ifPresent(node -> group.getChildren().add(cardinalityLabel(
                    relationship.toCardinality().displayText(),
                    midpointX(node, relationshipNode.get()),
                    midpointY(node, relationshipNode.get()) - 22
            )));
        }
        return group;
    }

    private Label cardinalityLabel(String text, double centerX, double centerY) {
        Label label = new Label(text);
        label.getStyleClass().add("chen-cardinality");
        label.setAlignment(Pos.CENTER);
        label.setLayoutX(centerX - 26);
        label.setLayoutY(centerY - 11);
        label.setPrefWidth(52);
        return label;
    }

    private double midpointX(NodeLayout a, NodeLayout b) {
        return (ChenGeometry.centerX(a) + ChenGeometry.centerX(b)) / 2.0;
    }

    private double midpointY(NodeLayout a, NodeLayout b) {
        return (ChenGeometry.centerY(a) + ChenGeometry.centerY(b)) / 2.0;
    }

    private Label centeredLabel(String text, double x, double y, double width) {
        Label label = new Label(text);
        label.getStyleClass().add("diagram-label");
        label.setAlignment(Pos.CENTER);
        label.setLayoutX(x);
        label.setLayoutY(y);
        label.setPrefWidth(width);
        return label;
    }

    private Optional<AttributeElement> attributeById(DiagramModel model, DiagramElementId attributeId) {
        for (EntityElement entity : model.entities()) {
            Optional<AttributeElement> attribute = entity.attributeById(attributeId);
            if (attribute.isPresent()) {
                return attribute;
            }
        }
        return Optional.empty();
    }

    private void applyConnectorStyle(Line line, DiagramElementId connectorId) {
        StrokeStyle stroke = DiagramStyleResolver.resolvedStyleFor(activeProject, connectorId).stroke();
        line.setStroke(javafx.scene.paint.Color.web(stroke.color().toHex()));
        line.setStrokeWidth(stroke.width());
        line.getStrokeDashArray().clear();
        switch (stroke.pattern()) {
            case DASHED -> line.getStrokeDashArray().setAll(10.0, 6.0);
            case DOTTED -> line.getStrokeDashArray().setAll(2.0, 5.0);
            case SOLID -> { }
        }
    }

    private void applyStyle(Shape shape, DiagramElementId elementId) {
        ElementStyle style = DiagramStyleResolver.resolvedStyleFor(activeProject, elementId);
        shape.setFill(javafx.scene.paint.Color.web(style.fill().color().toHex()));
        shape.setStroke(javafx.scene.paint.Color.web(style.stroke().color().toHex()));
        shape.setStrokeWidth(style.stroke().width());
        shape.setStyle("-fx-fill: " + style.fill().color().toHex() + ";"
                + "-fx-stroke: " + style.stroke().color().toHex() + ";"
                + "-fx-stroke-width: " + style.stroke().width() + ";");
    }

    private void applyTextStyle(Label label, DiagramElementId elementId) {
        applyTextStyle(label, elementId, label.getPrefWidth());
    }

    private void applyTextStyle(Label label, DiagramElementId elementId, double availableWidth) {
        ElementStyle style = DiagramStyleResolver.resolvedStyleFor(activeProject, elementId);
        double fittedFontSize = ConceptualFigureTextFitPolicy.fittedFontSize(
                label.getText(),
                style.text().fontSize(),
                availableWidth
        );
        label.setTextFill(javafx.scene.paint.Color.web(style.text().color().toHex()));
        label.setFont(Font.font(style.text().fontFamily(), fittedFontSize));
        label.setStyle("-fx-text-fill: " + style.text().color().toHex() + ";"
                + "-fx-font-family: '" + style.text().fontFamily().replace("'", "") + "';"
                + "-fx-font-size: " + fittedFontSize + "px;");
    }
}
