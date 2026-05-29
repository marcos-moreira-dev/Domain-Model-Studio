package com.marcosmoreira.domainmodelstudio.application.layout;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Genera un layout inicial para la vista Crow's Foot.
 *
 * <p>Crow's Foot no reutiliza el layout Chen: las entidades son cajas compactas con
 * atributos internos, no nodos rodeados de óvalos y rombos. Esta versión inicial agrupa
 * entidades conectadas cerca y rutea relaciones con codos ortogonales.</p>
 */
public final class GenerateInitialCrowsFootLayoutUseCase {

    private static final double ENTITY_WIDTH = 230.0;
    private static final double HEADER_HEIGHT = 34.0;
    private static final double ATTRIBUTE_ROW_HEIGHT = 22.0;
    private static final double MIN_ENTITY_HEIGHT = 86.0;

    private final ConceptualEntityOrdering entityOrdering = new ConceptualEntityOrdering();
    private final OrthogonalDiagramRoutingEngine routingEngine = new OrthogonalDiagramRoutingEngine();

    public DiagramProject generate(DiagramProject project) {
        Objects.requireNonNull(project, "El proyecto no puede ser null");

        AutoLayoutProfile profile = AutoLayoutProfile.crowsFoot(project.model().entities().size());
        List<NodeLayout> nodes = new ArrayList<>();
        Map<DiagramElementId, NodeLayout> entityLayouts = new LinkedHashMap<>();
        int index = 0;
        for (EntityElement entity : entityOrdering.order(project.model())) {
            int column = index % profile.preferredColumns();
            int row = index / profile.preferredColumns();
            double x = profile.startX() + column * profile.horizontalGap();
            double y = profile.startY() + row * profile.verticalGap();
            double height = Math.max(MIN_ENTITY_HEIGHT, HEADER_HEIGHT + Math.max(1, entity.attributes().size()) * ATTRIBUTE_ROW_HEIGHT + 16.0);
            NodeLayout nodeLayout = new NodeLayout(
                    entity.id(),
                    DiagramPoint.of(x, y),
                    DiagramSize.of(ENTITY_WIDTH, height),
                    true,
                    false
            );
            nodes.add(nodeLayout);
            entityLayouts.put(entity.id(), nodeLayout);
            index++;
        }

        DiagramObstacleMap obstacleMap = DiagramObstacleMap.fromNodes(nodes);
        List<ConnectorLayout> connectors = new ArrayList<>();
        for (RelationshipElement relationship : project.model().relationships()) {
            NodeLayout fromLayout = entityLayouts.get(relationship.fromEntityId());
            NodeLayout toLayout = entityLayouts.get(relationship.toEntityId());
            if (fromLayout == null || toLayout == null) {
                continue;
            }
            AnchorSide sourceAnchor = relationship.fromEntityId().equals(relationship.toEntityId())
                    ? AnchorSide.RIGHT
                    : LayoutAnchorResolver.sideFacing(fromLayout, toLayout);
            AnchorSide targetAnchor = relationship.fromEntityId().equals(relationship.toEntityId())
                    ? AnchorSide.BOTTOM
                    : LayoutAnchorResolver.sideFacing(toLayout, fromLayout);
            List<BendPoint> bendPoints = relationship.fromEntityId().equals(relationship.toEntityId())
                    ? selfRelationshipBendPoints(fromLayout, profile)
                    : routingEngine.route(
                            fromLayout,
                            toLayout,
                            sourceAnchor,
                            targetAnchor,
                            obstacleMap,
                            profile
                    );
            connectors.add(new ConnectorLayout(
                    connectorId(relationship.id()),
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
            ));
        }

        DiagramLayout crowsFootLayout = new DiagramLayout(NotationType.CROWS_FOOT, nodes, connectors);
        DiagramLayouts updatedLayouts = project.layouts()
                .withLayout(crowsFootLayout)
                .withActiveNotation(NotationType.CROWS_FOOT);
        return project
                .withLayouts(updatedLayouts)
                .withMetadata(project.metadata().withActiveNotation(NotationType.CROWS_FOOT));
    }

    private List<BendPoint> selfRelationshipBendPoints(NodeLayout node, AutoLayoutProfile profile) {
        double right = node.x() + node.width();
        double bottom = node.y() + node.height();
        double margin = Math.max(48.0, profile.routeMargin());
        return List.of(
                BendPoint.of(right + margin, node.y() + node.height() / 2.0),
                BendPoint.of(right + margin, bottom + margin),
                BendPoint.of(node.x() + node.width() / 2.0, bottom + margin)
        );
    }

    private DiagramElementId connectorId(DiagramElementId relationshipId) {
        return DiagramElementId.of("conn_crowsfoot_" + relationshipId.value());
    }

    private ConnectorMarker markerFor(Cardinality cardinality) {
        if (cardinality.isMany()) {
            return cardinality.isOptional() ? ConnectorMarker.OPTIONAL_MANY : ConnectorMarker.MANY;
        }
        return cardinality.isOptional() ? ConnectorMarker.OPTIONAL_ONE : ConnectorMarker.ONE;
    }
}
