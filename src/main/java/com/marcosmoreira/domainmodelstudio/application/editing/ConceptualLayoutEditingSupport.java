package com.marcosmoreira.domainmodelstudio.application.editing;

import com.marcosmoreira.domainmodelstudio.application.layout.AutoLayoutProfile;
import com.marcosmoreira.domainmodelstudio.application.layout.ChenLayoutDefaults;
import com.marcosmoreira.domainmodelstudio.application.layout.DiagramObstacleMap;
import com.marcosmoreira.domainmodelstudio.application.layout.LayoutAnchorResolver;
import com.marcosmoreira.domainmodelstudio.application.layout.OrthogonalDiagramRoutingEngine;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.er.AttributeElement;
import com.marcosmoreira.domainmodelstudio.domain.er.Cardinality;
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
import java.util.List;
import java.util.Map;

/** Operaciones de layout necesarias para la edición manual del modelo conceptual. */
final class ConceptualLayoutEditingSupport {

    private static final double CROWS_FOOT_WIDTH = 230.0;
    private static final double CROWS_FOOT_HEADER_HEIGHT = 34.0;
    private static final double CROWS_FOOT_ROW_HEIGHT = 22.0;
    private static final double CROWS_FOOT_MIN_HEIGHT = 86.0;
    private static final OrthogonalDiagramRoutingEngine ROUTING_ENGINE = new OrthogonalDiagramRoutingEngine();

    private ConceptualLayoutEditingSupport() {
        // Utilidad interna.
    }

    static DiagramLayouts withEntityNode(DiagramProject project, EntityElement entity, double x, double y) {
        DiagramLayouts layouts = project.layouts();
        for (Map.Entry<NotationType, DiagramLayout> entry : project.layouts().layoutsByNotation().entrySet()) {
            DiagramLayout layout = entry.getValue();
            NodeLayout node = entityNodeFor(entry.getKey(), entity.id(), x, y, entity.attributes().size());
            layouts = layouts.withLayout(layout.withNode(node));
        }
        if (project.layouts().layoutFor(project.metadata().activeNotation()).isEmpty()) {
            DiagramLayout layout = DiagramLayout.empty(project.metadata().activeNotation())
                    .withNode(entityNodeFor(project.metadata().activeNotation(), entity.id(), x, y, entity.attributes().size()));
            layouts = layouts.withLayout(layout);
        }
        return layouts;
    }

    static DiagramLayouts withAttributeNode(DiagramProject project, EntityElement owner, AttributeElement attribute) {
        DiagramLayouts layouts = project.layouts();
        DiagramLayout chen = layouts.layoutFor(NotationType.CHEN).orElse(null);
        if (chen != null) {
            NodeLayout ownerNode = chen.nodeFor(owner.id()).orElse(null);
            if (ownerNode != null) {
                int attributeIndex = attributeIndex(owner, attribute.id());
                NodeLayout attributeNode = attributeNodeNear(ownerNode, attributeIndex, owner.attributes().size(), attribute.id());
                ConnectorLayout connector = new ConnectorLayout(
                        ConceptualEditingIds.connectorId(owner.id(), "attr", attribute.id().value()),
                        owner.id(),
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
                );
                layouts = layouts.withLayout(chen.withNode(attributeNode).withConnector(connector));
            }
        }
        DiagramLayout crows = layouts.layoutFor(NotationType.CROWS_FOOT).orElse(null);
        if (crows != null) {
            NodeLayout ownerNode = crows.nodeFor(owner.id()).orElse(null);
            if (ownerNode != null) {
                layouts = layouts.withLayout(crows.withNode(ownerNode.resizedTo(CROWS_FOOT_WIDTH, crowsFootHeight(owner.attributes().size()))));
            }
        }
        return layouts;
    }

    static DiagramLayouts withRelationshipLayout(DiagramProject project, RelationshipElement relationship) {
        DiagramLayouts layouts = project.layouts();
        DiagramLayout chen = layouts.layoutFor(NotationType.CHEN).orElse(null);
        if (chen != null) {
            NodeLayout from = chen.nodeFor(relationship.fromEntityId()).orElse(null);
            NodeLayout to = chen.nodeFor(relationship.toEntityId()).orElse(null);
            if (from != null && to != null) {
                NodeLayout relationshipNode = relationshipNodeBetween(relationship.id(), from, to);
                DiagramLayout baseChen = chen.withNode(relationshipNode);
                DiagramObstacleMap obstacles = DiagramObstacleMap.fromNodes(baseChen.nodes());
                AutoLayoutProfile profile = AutoLayoutProfile.chen(baseChen.nodeCount());
                DiagramLayout updatedChen = baseChen
                        .withConnector(routedChenConnector(
                                ConceptualEditingIds.connectorId(relationship.id(), "from"),
                                relationship.fromEntityId(),
                                relationship.id(),
                                from,
                                relationshipNode,
                                obstacles,
                                profile))
                        .withConnector(routedChenConnector(
                                ConceptualEditingIds.connectorId(relationship.id(), "to"),
                                relationship.id(),
                                relationship.toEntityId(),
                                relationshipNode,
                                to,
                                obstacles,
                                profile));
                layouts = layouts.withLayout(updatedChen);
            }
        }
        DiagramLayout crows = layouts.layoutFor(NotationType.CROWS_FOOT).orElse(null);
        if (crows != null) {
            NodeLayout from = crows.nodeFor(relationship.fromEntityId()).orElse(null);
            NodeLayout to = crows.nodeFor(relationship.toEntityId()).orElse(null);
            if (from != null && to != null) {
                ConnectorLayout connector = routedCrowsFootConnector(relationship, from, to, crows);
                layouts = layouts.withLayout(crows.withConnector(connector));
            }
        }
        return layouts;
    }

    static DiagramLayouts withoutElement(DiagramProject project, DiagramElementId elementId) {
        return withoutElements(project, List.of(elementId));
    }

    static DiagramLayouts withoutElements(DiagramProject project, List<DiagramElementId> elementIds) {
        DiagramLayouts layouts = project.layouts();
        for (Map.Entry<NotationType, DiagramLayout> entry : project.layouts().layoutsByNotation().entrySet()) {
            DiagramLayout layout = entry.getValue();
            for (DiagramElementId elementId : elementIds) {
                layout = layout
                        .withoutNode(elementId)
                        .withoutConnectorsReferencing(elementId)
                        .withoutConnector(crowsFootConnectorId(elementId));
            }
            layouts = layouts.withLayout(layout);
        }
        return layouts;
    }


    private static ConnectorLayout routedChenConnector(
            DiagramElementId connectorId,
            DiagramElementId sourceId,
            DiagramElementId targetId,
            NodeLayout sourceNode,
            NodeLayout targetNode,
            DiagramObstacleMap obstacles,
            AutoLayoutProfile profile
    ) {
        AnchorSide sourceAnchor = LayoutAnchorResolver.sideFacing(sourceNode, targetNode);
        AnchorSide targetAnchor = LayoutAnchorResolver.sideFacing(targetNode, sourceNode);
        List<BendPoint> bendPoints = ROUTING_ENGINE.route(sourceNode, targetNode, sourceAnchor, targetAnchor, obstacles, profile);
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

    private static ConnectorLayout routedCrowsFootConnector(RelationshipElement relationship, NodeLayout from, NodeLayout to, DiagramLayout layout) {
        AutoLayoutProfile profile = AutoLayoutProfile.crowsFoot(layout.nodeCount());
        AnchorSide sourceAnchor = relationship.fromEntityId().equals(relationship.toEntityId())
                ? AnchorSide.RIGHT
                : LayoutAnchorResolver.sideFacing(from, to);
        AnchorSide targetAnchor = relationship.fromEntityId().equals(relationship.toEntityId())
                ? AnchorSide.BOTTOM
                : LayoutAnchorResolver.sideFacing(to, from);
        List<BendPoint> bendPoints = relationship.fromEntityId().equals(relationship.toEntityId())
                ? selfRelationshipBendPoints(from, profile)
                : ROUTING_ENGINE.route(from, to, sourceAnchor, targetAnchor, DiagramObstacleMap.fromNodes(layout.nodes()), profile);
        return new ConnectorLayout(
                crowsFootConnectorId(relationship.id()),
                relationship.fromEntityId(),
                relationship.toEntityId(),
                sourceAnchor,
                targetAnchor,
                bendPoints.isEmpty() ? ConnectorPathKind.STRAIGHT : ConnectorPathKind.ORTHOGONAL,
                bendPoints,
                markerFor(relationship.fromCardinality()),
                markerFor(relationship.toCardinality()),
                MarkerOrientation.AUTO,
                MarkerOrientation.AUTO,
                true
        );
    }

    private static List<BendPoint> selfRelationshipBendPoints(NodeLayout node, AutoLayoutProfile profile) {
        double right = node.x() + node.width();
        double bottom = node.y() + node.height();
        double margin = Math.max(48.0, profile.routeMargin());
        return List.of(
                BendPoint.of(right + margin, node.y() + node.height() / 2.0),
                BendPoint.of(right + margin, bottom + margin),
                BendPoint.of(node.x() + node.width() / 2.0, bottom + margin)
        );
    }

    private static int attributeIndex(EntityElement owner, DiagramElementId attributeId) {
        for (int index = 0; index < owner.attributes().size(); index++) {
            if (owner.attributes().get(index).id().equals(attributeId)) {
                return index;
            }
        }
        return Math.max(0, owner.attributes().size() - 1);
    }

    private static NodeLayout entityNodeFor(NotationType notation, DiagramElementId entityId, double x, double y, int attributeCount) {
        if (notation == NotationType.CROWS_FOOT) {
            return new NodeLayout(entityId, DiagramPoint.of(x, y), DiagramSize.of(CROWS_FOOT_WIDTH, crowsFootHeight(attributeCount)), true, false);
        }
        return new NodeLayout(
                entityId,
                DiagramPoint.of(x, y),
                DiagramSize.of(ChenLayoutDefaults.ENTITY_WIDTH, ChenLayoutDefaults.ENTITY_HEIGHT),
                true,
                false
        );
    }

    private static NodeLayout attributeNodeNear(NodeLayout ownerNode, int attributeIndex, int attributeCount, DiagramElementId attributeId) {
        int safeCount = Math.max(1, attributeCount);
        double angle = -Math.PI / 2.0 + (Math.PI * 2.0 / safeCount) * Math.max(0, attributeIndex);
        double centerX = ownerNode.x() + ownerNode.width() / 2.0;
        double centerY = ownerNode.y() + ownerNode.height() / 2.0;
        double x = centerX + Math.cos(angle) * ChenLayoutDefaults.ATTRIBUTE_RADIUS_X - ChenLayoutDefaults.ATTRIBUTE_WIDTH / 2.0;
        double y = centerY + Math.sin(angle) * ChenLayoutDefaults.ATTRIBUTE_RADIUS_Y - ChenLayoutDefaults.ATTRIBUTE_HEIGHT / 2.0;
        return new NodeLayout(
                attributeId,
                DiagramPoint.of(x, y),
                DiagramSize.of(ChenLayoutDefaults.ATTRIBUTE_WIDTH, ChenLayoutDefaults.ATTRIBUTE_HEIGHT),
                true,
                false
        );
    }

    private static NodeLayout relationshipNodeBetween(DiagramElementId relationshipId, NodeLayout from, NodeLayout to) {
        double fromCenterX = from.x() + from.width() / 2.0;
        double fromCenterY = from.y() + from.height() / 2.0;
        double toCenterX = to.x() + to.width() / 2.0;
        double toCenterY = to.y() + to.height() / 2.0;
        double x = (fromCenterX + toCenterX) / 2.0 - ChenLayoutDefaults.RELATIONSHIP_WIDTH / 2.0;
        double y = (fromCenterY + toCenterY) / 2.0 - ChenLayoutDefaults.RELATIONSHIP_HEIGHT / 2.0;
        if (from.elementId().equals(to.elementId())) {
            x = fromCenterX + 190.0;
            y = fromCenterY + 90.0;
        }
        return new NodeLayout(
                relationshipId,
                DiagramPoint.of(x, y),
                DiagramSize.of(ChenLayoutDefaults.RELATIONSHIP_WIDTH, ChenLayoutDefaults.RELATIONSHIP_HEIGHT),
                true,
                false
        );
    }

    private static double crowsFootHeight(int attributeCount) {
        return Math.max(CROWS_FOOT_MIN_HEIGHT, CROWS_FOOT_HEADER_HEIGHT + Math.max(1, attributeCount) * CROWS_FOOT_ROW_HEIGHT + 16.0);
    }

    private static DiagramElementId crowsFootConnectorId(DiagramElementId relationshipId) {
        return DiagramElementId.of("conn_crowsfoot_" + relationshipId.value());
    }

    private static ConnectorMarker markerFor(Cardinality cardinality) {
        if (cardinality.isMany()) {
            return cardinality.isOptional() ? ConnectorMarker.OPTIONAL_MANY : ConnectorMarker.MANY;
        }
        return cardinality.isOptional() ? ConnectorMarker.OPTIONAL_ONE : ConnectorMarker.ONE;
    }
}
