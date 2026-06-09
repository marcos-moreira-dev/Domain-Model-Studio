package com.marcosmoreira.domainmodelstudio.application.layout;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramModel;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.er.AttributeElement;
import com.marcosmoreira.domainmodelstudio.domain.er.EntityElement;
import com.marcosmoreira.domainmodelstudio.domain.er.RelationshipElement;
import com.marcosmoreira.domainmodelstudio.domain.layout.AnchorSide;
import com.marcosmoreira.domainmodelstudio.domain.layout.BendPoint;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorMarker;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorPathKind;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayouts;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramPoint;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramSize;
import com.marcosmoreira.domainmodelstudio.domain.layout.MarkerOrientation;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Genera el primer layout Chen para un proyecto importado.
 *
 * <p>Responsabilidad única: transformar un modelo semántico ya validable en posiciones
 * iniciales. No dibuja JavaFX, no lee archivos y no decide estilos. La presentación puede
 * usar este layout como punto de partida para renderizar rectángulos, óvalos, rombos y
 * conectores.</p>
 */
public final class GenerateInitialChenLayoutUseCase {

    private final ConceptualEntityOrdering entityOrdering = new ConceptualEntityOrdering();
    private final OrthogonalDiagramRoutingEngine routingEngine = new OrthogonalDiagramRoutingEngine();

    public DiagramProject generate(DiagramProject project) {
        Objects.requireNonNull(project, "El proyecto no puede ser null");
        DiagramLayout chenLayout = generateLayout(project.model());
        DiagramLayouts updatedLayouts = project.layouts()
                .withLayout(chenLayout)
                .withActiveNotation(NotationType.CHEN);
        return project.withLayouts(updatedLayouts);
    }

    public DiagramLayout generateLayout(DiagramModel model) {
        Objects.requireNonNull(model, "El modelo no puede ser null");
        List<NodeLayout> nodes = new ArrayList<>();
        List<ConnectorLayout> connectors = new ArrayList<>();
        Map<DiagramElementId, NodeLayout> entityLayouts = new LinkedHashMap<>();
        Map<String, Integer> relationshipPairCounters = new LinkedHashMap<>();
        AutoLayoutProfile profile = AutoLayoutProfile.chen(model.entities().size());

        int entityIndex = 0;
        for (EntityElement entity : entityOrdering.order(model)) {
            NodeLayout entityLayout = entityLayout(entity, entityIndex++, profile);
            entityLayouts.put(entity.id(), entityLayout);
            nodes.add(entityLayout);
            nodes.addAll(attributeLayouts(entity, entityLayout));
            connectors.addAll(attributeConnectors(entity));
        }

        for (RelationshipElement relationship : model.relationships()) {
            int pairIndex = relationshipPairCounters.merge(pairKey(relationship), 1, Integer::sum) - 1;
            NodeLayout relationshipLayout = relationshipLayout(relationship, entityLayouts, pairIndex);
            nodes.add(relationshipLayout);
        }

        DiagramObstacleMap obstacleMap = DiagramObstacleMap.fromNodes(nodes);
        Map<DiagramElementId, NodeLayout> allLayouts = indexNodes(nodes);
        for (RelationshipElement relationship : model.relationships()) {
            NodeLayout relationshipLayout = allLayouts.get(relationship.id());
            NodeLayout fromLayout = allLayouts.get(relationship.fromEntityId());
            NodeLayout toLayout = allLayouts.get(relationship.toEntityId());
            if (relationshipLayout == null || fromLayout == null || toLayout == null) {
                continue;
            }
            connectors.add(relationshipConnector(
                    connectorId(relationship.id(), "from"),
                    relationship.fromEntityId(),
                    relationship.id(),
                    fromLayout,
                    relationshipLayout,
                    obstacleMap,
                    profile
            ));
            connectors.add(relationshipConnector(
                    connectorId(relationship.id(), "to"),
                    relationship.id(),
                    relationship.toEntityId(),
                    relationshipLayout,
                    toLayout,
                    obstacleMap,
                    profile
            ));
        }

        return new DiagramLayout(NotationType.CHEN, nodes, connectors);
    }

    private NodeLayout entityLayout(EntityElement entity, int index, AutoLayoutProfile profile) {
        int column = index % profile.preferredColumns();
        int row = index / profile.preferredColumns();
        double x = profile.startX() + column * profile.horizontalGap();
        double y = profile.startY() + row * profile.verticalGap();
        return new NodeLayout(
                entity.id(),
                DiagramPoint.of(x, y),
                DiagramSize.of(ChenLayoutDefaults.ENTITY_WIDTH, ChenLayoutDefaults.ENTITY_HEIGHT),
                true,
                false
        );
    }

    private List<NodeLayout> attributeLayouts(EntityElement entity, NodeLayout entityLayout) {
        List<AttributeElement> attributes = entity.attributes();
        if (attributes.isEmpty()) {
            return List.of();
        }

        List<NodeLayout> layouts = new ArrayList<>();
        double entityCenterX = centerX(entityLayout);
        double entityCenterY = centerY(entityLayout);
        double step = (Math.PI * 2.0) / attributes.size();
        double initialAngle = -Math.PI / 2.0;

        for (int index = 0; index < attributes.size(); index++) {
            AttributeElement attribute = attributes.get(index);
            double angle = initialAngle + index * step;
            double x = entityCenterX + Math.cos(angle) * ChenLayoutDefaults.ATTRIBUTE_RADIUS_X
                    - ChenLayoutDefaults.ATTRIBUTE_WIDTH / 2.0;
            double y = entityCenterY + Math.sin(angle) * ChenLayoutDefaults.ATTRIBUTE_RADIUS_Y
                    - ChenLayoutDefaults.ATTRIBUTE_HEIGHT / 2.0;
            layouts.add(new NodeLayout(
                    attribute.id(),
                    DiagramPoint.of(x, y),
                    DiagramSize.of(ChenLayoutDefaults.ATTRIBUTE_WIDTH, ChenLayoutDefaults.ATTRIBUTE_HEIGHT),
                    true,
                    false
            ));
        }
        return layouts;
    }

    private List<ConnectorLayout> attributeConnectors(EntityElement entity) {
        List<ConnectorLayout> connectors = new ArrayList<>();
        for (AttributeElement attribute : entity.attributes()) {
            connectors.add(new ConnectorLayout(
                    connectorId(entity.id(), "attr", attribute.id().value()),
                    entity.id(),
                    attribute.id(),
                    AnchorSide.AUTO,
                    AnchorSide.AUTO,
                    ConnectorPathKind.STRAIGHT,
                    List.of(),
                    ConnectorMarker.NONE,
                    ConnectorMarker.NONE,
                    MarkerOrientation.AUTO,
                    MarkerOrientation.AUTO,
                    true
            ));
        }
        return connectors;
    }

    private NodeLayout relationshipLayout(
            RelationshipElement relationship,
            Map<DiagramElementId, NodeLayout> entityLayouts,
            int pairIndex
    ) {
        NodeLayout from = entityLayouts.getOrDefault(
                relationship.fromEntityId(),
                NodeLayout.at(relationship.fromEntityId().value(), ChenLayoutDefaults.START_X, ChenLayoutDefaults.START_Y, 1, 1)
        );
        NodeLayout to = entityLayouts.getOrDefault(
                relationship.toEntityId(),
                NodeLayout.at(relationship.toEntityId().value(), ChenLayoutDefaults.START_X + 250, ChenLayoutDefaults.START_Y, 1, 1)
        );

        double x = (centerX(from) + centerX(to)) / 2.0 - ChenLayoutDefaults.RELATIONSHIP_WIDTH / 2.0;
        double y = (centerY(from) + centerY(to)) / 2.0 - ChenLayoutDefaults.RELATIONSHIP_HEIGHT / 2.0;

        if (relationship.fromEntityId().equals(relationship.toEntityId())) {
            x = centerX(from) + 190.0;
            y = centerY(from) + 90.0 + pairIndex * 86.0;
        } else if (pairIndex > 0) {
            double dx = centerX(to) - centerX(from);
            double dy = centerY(to) - centerY(from);
            double length = Math.max(1.0, Math.hypot(dx, dy));
            double offset = pairIndex * 86.0;
            x += (-dy / length) * offset;
            y += (dx / length) * offset;
        }

        return new NodeLayout(
                relationship.id(),
                DiagramPoint.of(x, y),
                DiagramSize.of(ChenLayoutDefaults.RELATIONSHIP_WIDTH, ChenLayoutDefaults.RELATIONSHIP_HEIGHT),
                true,
                false
        );
    }

    private ConnectorLayout relationshipConnector(
            DiagramElementId connectorId,
            DiagramElementId sourceId,
            DiagramElementId targetId,
            NodeLayout sourceNode,
            NodeLayout targetNode,
            DiagramObstacleMap obstacleMap,
            AutoLayoutProfile profile
    ) {
        AnchorSide sourceAnchor = LayoutAnchorResolver.sideFacing(sourceNode, targetNode);
        AnchorSide targetAnchor = LayoutAnchorResolver.sideFacing(targetNode, sourceNode);
        List<BendPoint> bendPoints = routingEngine.route(sourceNode, targetNode, sourceAnchor, targetAnchor, obstacleMap, profile);
        return new ConnectorLayout(
                connectorId,
                sourceId,
                targetId,
                sourceAnchor,
                targetAnchor,
                bendPoints.isEmpty() ? ConnectorPathKind.STRAIGHT : ConnectorPathKind.ORTHOGONAL,
                bendPoints,
                ConnectorMarker.NONE,
                ConnectorMarker.NONE,
                MarkerOrientation.AUTO,
                MarkerOrientation.AUTO,
                true
        );
    }

    private Map<DiagramElementId, NodeLayout> indexNodes(List<NodeLayout> nodes) {
        Map<DiagramElementId, NodeLayout> index = new LinkedHashMap<>();
        for (NodeLayout node : nodes) {
            index.put(node.elementId(), node);
        }
        return index;
    }

    private String pairKey(RelationshipElement relationship) {
        String a = relationship.fromEntityId().value();
        String b = relationship.toEntityId().value();
        return a.compareTo(b) <= 0 ? a + "::" + b : b + "::" + a;
    }

    private DiagramElementId connectorId(DiagramElementId baseId, String... parts) {
        StringBuilder builder = new StringBuilder("conn_").append(baseId.value());
        for (String part : parts) {
            builder.append('_').append(part.replaceAll("[^A-Za-z0-9_-]", "_"));
        }
        return DiagramElementId.of(builder.toString());
    }

    private double centerX(NodeLayout layout) {
        return layout.x() + layout.width() / 2.0;
    }

    private double centerY(NodeLayout layout) {
        return layout.y() + layout.height() / 2.0;
    }
}
