package com.marcosmoreira.domainmodelstudio.presentation.canvas;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramModel;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.er.AttributeElement;
import com.marcosmoreira.domainmodelstudio.domain.er.EntityElement;
import com.marcosmoreira.domainmodelstudio.domain.er.RelationshipElement;
import com.marcosmoreira.domainmodelstudio.domain.layout.BendPoint;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorMarker;
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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;

/**
 * Renderizador JavaFX básico para Crow's Foot.
 *
 * <p>Esta vista no intenta imitar Chen. Las entidades se muestran como cajas compactas
 * con atributos internos y las relaciones se representan como conectores directos con
 * marcadores de cardinalidad. Es una primera visualización alternativa, suficiente para
 * validar la arquitectura multi-notación.</p>
 */
public final class CrowsFootDiagramRenderer {

    private DiagramProject activeProject;
    private Set<DiagramElementId> selectedElementIds = Set.of();

    public Group render(DiagramProject project, DiagramElementId selectedElementId) {
        return render(project, selectedElementId == null ? Set.of() : Set.of(selectedElementId));
    }

    public Group render(DiagramProject project, Set<DiagramElementId> selectedElementIds) {
        this.activeProject = project;
        this.selectedElementIds = selectedElementIds == null ? Set.of() : Set.copyOf(selectedElementIds);
        DiagramLayout layout = project.layouts()
                .layoutFor(NotationType.CROWS_FOOT)
                .orElseGet(() -> DiagramLayout.empty(NotationType.CROWS_FOOT));
        Group root = new Group();
        addProjectTitle(root, project, layout);
        root.getChildren().add(renderConnectors(project.model(), layout));
        root.getChildren().add(renderEntities(project.model(), layout));
        return root;
    }

    /**
     * Renderiza únicamente conectores y rótulos de relación para previsualización en vivo.
     */
    public Group renderDynamicConnectors(DiagramProject project) {
        this.activeProject = project;
        this.selectedElementIds = Set.of();
        DiagramLayout layout = project.layouts()
                .layoutFor(NotationType.CROWS_FOOT)
                .orElseGet(() -> DiagramLayout.empty(NotationType.CROWS_FOOT));
        Group root = new Group();
        root.getStyleClass().add("diagram-live-connector-layer");
        root.setMouseTransparent(true);
        root.getChildren().add(renderConnectors(project.model(), layout));
        return root;
    }

    private boolean isSelected(DiagramElementId elementId) {
        return elementId != null && selectedElementIds.contains(elementId);
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
            Group connectorGroup = renderConnector(connector, source.get(), target.get(), model);
            group.getChildren().add(connectorGroup);
        }
        return group;
    }

    private Group renderConnector(ConnectorLayout connector, NodeLayout sourceNode, NodeLayout targetNode, DiagramModel model) {
        Point2D start = ChenGeometry.anchorPoint(sourceNode, connector.sourceAnchor(), targetNode);
        Point2D end = ChenGeometry.anchorPoint(targetNode, connector.targetAnchor(), sourceNode);
        List<Point2D> route = new ArrayList<>();
        route.add(start);
        for (BendPoint bendPoint : connector.bendPoints()) {
            route.add(new Point2D(bendPoint.x(), bendPoint.y()));
        }
        route.add(end);

        Group connectorGroup = new Group();
        connectorGroup.setUserData(connector.connectorId());
        connectorGroup.getStyleClass().add("crowsfoot-connector-group");
        if (isSelected(connector.connectorId())) {
            connectorGroup.getStyleClass().add("chen-selected-connector");
        }
        for (int index = 0; index < route.size() - 1; index++) {
            Point2D a = route.get(index);
            Point2D b = route.get(index + 1);
            Line line = new Line(a.getX(), a.getY(), b.getX(), b.getY());
            line.setUserData(connector.connectorId());
            line.getStyleClass().addAll("crowsfoot-connector", "chen-connector-segment");
            applyConnectorStyle(line, connector.connectorId());
            if (isSelected(connector.connectorId())) {
                line.getStyleClass().add("chen-selected-connector-segment");
                line.setStrokeWidth(line.getStrokeWidth() + 1.0);
            }
            connectorGroup.getChildren().add(line);
        }
        addMarker(connectorGroup, connector.sourceMarker(), route.get(1), route.get(0), connector.connectorId());
        addMarker(connectorGroup, connector.targetMarker(), route.get(route.size() - 2), route.get(route.size() - 1), connector.connectorId());
        renderRelationshipLabel(connectorGroup, connector, model, route);
        if (isSelected(connector.connectorId())) {
            connectorGroup.getChildren().add(endpointHandle(connector.connectorId(), start, true));
            for (int index = 0; index < connector.bendPoints().size(); index++) {
                BendPoint bendPoint = connector.bendPoints().get(index);
                connectorGroup.getChildren().add(bendPointHandle(connector.connectorId(), index, bendPoint.x(), bendPoint.y()));
            }
            connectorGroup.getChildren().add(endpointHandle(connector.connectorId(), end, false));
        }
        return connectorGroup;
    }

    private Circle bendPointHandle(DiagramElementId connectorId, int index, double x, double y) {
        Circle handle = new Circle(x, y, 5.5);
        handle.setUserData(connectorId);
        handle.getProperties().put("bendPointIndex", index);
        handle.getStyleClass().add("chen-bend-point-handle");
        return handle;
    }

    private Circle endpointHandle(DiagramElementId connectorId, Point2D point, boolean sourceEndpoint) {
        Circle handle = new Circle(point.getX(), point.getY(), 6.5);
        handle.setUserData(connectorId);
        handle.getProperties().put("connectorEndpoint", sourceEndpoint ? "source" : "target");
        handle.getStyleClass().addAll("chen-bend-point-handle", "chen-endpoint-handle");
        return handle;
    }

    private void renderRelationshipLabel(Group group, ConnectorLayout connector, DiagramModel model, List<Point2D> route) {
        Optional<RelationshipElement> relationship = relationshipForConnector(model, connector);
        if (relationship.isEmpty()) {
            return;
        }
        Point2D anchor = labelAnchorPoint(route);
        Label label = new Label(relationship.get().name());
        label.setUserData(connector.connectorId());
        label.getProperties().put("relationshipLabel", true);
        label.getStyleClass().add("crowsfoot-relationship-label");
        if (isSelected(connector.connectorId())) {
            label.getStyleClass().add("crowsfoot-relationship-label-selected");
        }
        label.setLayoutX(anchor.getX() + connector.labelOffsetX() - 60);
        label.setLayoutY(anchor.getY() + connector.labelOffsetY() - 14);
        label.setPrefWidth(120);
        label.setAlignment(Pos.CENTER);
        label.setFont(Font.font("Segoe UI", ConceptualFigureTextFitPolicy.fittedFontSize(label.getText(), 12.0, 112.0)));
        group.getChildren().add(label);
    }

    private Point2D labelAnchorPoint(List<Point2D> route) {
        if (route == null || route.isEmpty()) {
            return Point2D.ZERO;
        }
        if (route.size() == 1) {
            return route.getFirst();
        }
        double totalLength = 0.0;
        for (int index = 0; index < route.size() - 1; index++) {
            totalLength += route.get(index).distance(route.get(index + 1));
        }
        if (totalLength <= 0.01) {
            return route.get(route.size() / 2);
        }
        double halfLength = totalLength / 2.0;
        double traversed = 0.0;
        for (int index = 0; index < route.size() - 1; index++) {
            Point2D a = route.get(index);
            Point2D b = route.get(index + 1);
            double segmentLength = a.distance(b);
            if (traversed + segmentLength >= halfLength) {
                double ratio = segmentLength <= 0.01 ? 0.0 : (halfLength - traversed) / segmentLength;
                return new Point2D(
                        a.getX() + (b.getX() - a.getX()) * ratio,
                        a.getY() + (b.getY() - a.getY()) * ratio
                );
            }
            traversed += segmentLength;
        }
        return route.get(route.size() - 1);
    }

    private Optional<RelationshipElement> relationshipForConnector(DiagramModel model, ConnectorLayout connector) {
        Optional<RelationshipElement> direct = model.relationshipById(connector.connectorId());
        if (direct.isPresent()) {
            return direct;
        }
        return model.relationships().stream()
                .filter(relationship -> relationship.fromEntityId().equals(connector.sourceElementId())
                        && relationship.toEntityId().equals(connector.targetElementId()))
                .findFirst();
    }

    private void addMarker(Group group, ConnectorMarker marker, Point2D previous, Point2D end, DiagramElementId connectorId) {
        if (marker == ConnectorMarker.NONE) {
            return;
        }
        double angle = Math.atan2(end.getY() - previous.getY(), end.getX() - previous.getX());
        double ux = Math.cos(angle);
        double uy = Math.sin(angle);
        double px = -uy;
        double py = ux;
        double x = end.getX();
        double y = end.getY();
        double back = 16.0;
        double spread = 10.0;

        if (marker == ConnectorMarker.OPTIONAL_ONE || marker == ConnectorMarker.OPTIONAL_MANY) {
            Circle circle = new Circle(x - ux * 10.0, y - uy * 10.0, 5.0);
            circle.setUserData(connectorId);
            circle.getStyleClass().add("crowsfoot-marker-circle");
            circle.setFill(javafx.scene.paint.Color.WHITE);
            circle.setStroke(javafx.scene.paint.Color.web(DiagramStyleResolver.resolvedStyleFor(activeProject, connectorId).stroke().color().toHex()));
            circle.setStrokeWidth(Math.max(1.0, DiagramStyleResolver.resolvedStyleFor(activeProject, connectorId).stroke().width()));
            group.getChildren().add(circle);
        }
        if (marker == ConnectorMarker.ONE || marker == ConnectorMarker.OPTIONAL_ONE) {
            Line one = new Line(
                    x - ux * back + px * spread,
                    y - uy * back + py * spread,
                    x - ux * back - px * spread,
                    y - uy * back - py * spread
            );
            one.setUserData(connectorId);
            one.getStyleClass().add("crowsfoot-marker");
            applyConnectorStyle(one, connectorId);
            group.getChildren().add(one);
            return;
        }
        if (marker == ConnectorMarker.MANY || marker == ConnectorMarker.OPTIONAL_MANY) {
            double bx = x - ux * back;
            double by = y - uy * back;
            Line top = new Line(x, y, bx + px * spread, by + py * spread);
            Line mid = new Line(x, y, bx, by);
            Line bottom = new Line(x, y, bx - px * spread, by - py * spread);
            for (Line line : List.of(top, mid, bottom)) {
                line.setUserData(connectorId);
                line.getStyleClass().add("crowsfoot-marker");
                applyConnectorStyle(line, connectorId);
                group.getChildren().add(line);
            }
        }
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

    private Group renderEntities(DiagramModel model, DiagramLayout layout) {
        Group group = new Group();
        for (EntityElement entity : model.entities()) {
            Optional<NodeLayout> layoutNode = layout.nodeFor(entity.id());
            if (layoutNode.isEmpty() || !layoutNode.get().visible()) {
                continue;
            }
            Node node = entityBox(entity, layoutNode.get());
            node.setUserData(entity.id());
            node.getStyleClass().add("chen-draggable-node");
            if (isSelected(entity.id())) {
                node.getStyleClass().add("chen-selected-node");
            }
            group.getChildren().add(node);
        }
        return group;
    }

    private Group entityBox(EntityElement entity, NodeLayout node) {
        Group group = new Group();
        Rectangle box = new Rectangle(node.x(), node.y(), node.width(), node.height());
        box.getStyleClass().add("crowsfoot-entity-box");
        applyStyle(box, entity.id());
        Rectangle header = new Rectangle(node.x(), node.y(), node.width(), 34.0);
        header.getStyleClass().add("crowsfoot-entity-header");
        header.setFill(javafx.scene.paint.Color.web("#E9EDF2"));
        header.setStroke(javafx.scene.paint.Color.web("#505050"));
        header.setStrokeWidth(0.0);
        Label title = new Label(entity.name());
        title.getStyleClass().add("crowsfoot-entity-title");
        title.setLayoutX(node.x() + 8);
        title.setLayoutY(node.y() + 8);
        title.setPrefWidth(node.width() - 16);
        title.setTextFill(javafx.scene.paint.Color.web("#222222"));
        title.setFont(Font.font("Segoe UI", ConceptualFigureTextFitPolicy.fittedFontSize(title.getText(), 12.0, node.width() - 20.0)));
        group.getChildren().addAll(box, header, title);

        double y = node.y() + 42.0;
        for (AttributeElement attribute : entity.attributes()) {
            Label row = new Label(attributeLabel(attribute));
            row.getStyleClass().add(attribute.isKeyLike() ? "crowsfoot-attribute-pk" : "crowsfoot-attribute");
            row.setLayoutX(node.x() + 10.0);
            row.setLayoutY(y);
            row.setPrefWidth(node.width() - 20.0);
            row.setTextFill(javafx.scene.paint.Color.web("#222222"));
            row.setFont(Font.font("Segoe UI", ConceptualFigureTextFitPolicy.fittedFontSize(row.getText(), 12.0, node.width() - 24.0)));
            group.getChildren().add(row);
            y += 22.0;
        }
        return group;
    }

    private String attributeLabel(AttributeElement attribute) {
        String prefix;
        if (attribute.isPrimaryKey()) {
            prefix = "PK  ";
        } else if (attribute.isPartialKey()) {
            prefix = "PPK ";
        } else {
            prefix = "    ";
        }
        return prefix + attribute.name();
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
}
