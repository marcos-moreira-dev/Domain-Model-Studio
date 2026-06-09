package com.marcosmoreira.domainmodelstudio.application.layout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.marcosmoreira.domainmodelstudio.domain.layout.AnchorSide;
import com.marcosmoreira.domainmodelstudio.domain.layout.BendPoint;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class OrthogonalDiagramRoutingEngineTest {

    @Test
    void routesAroundSimpleObstacleWithBendPoints() {
        NodeLayout source = NodeLayout.at("origen", 0, 0, 100, 60);
        NodeLayout obstacle = NodeLayout.at("obstaculo", 180, -10, 80, 100);
        NodeLayout target = NodeLayout.at("destino", 340, 0, 100, 60);
        DiagramObstacleMap obstacleMap = DiagramObstacleMap.fromNodes(List.of(source, obstacle, target));
        AutoLayoutProfile profile = new AutoLayoutProfile(0, 0, 300, 200, 3, 6, 96);

        List<BendPoint> route = new OrthogonalDiagramRoutingEngine().route(
                source,
                target,
                AnchorSide.RIGHT,
                AnchorSide.LEFT,
                obstacleMap,
                profile
        );

        assertFalse(route.isEmpty());
        assertFalse(obstacleMap.routeCrossesObstacle(
                List.of(
                        new DiagramPointPair(com.marcosmoreira.domainmodelstudio.domain.layout.DiagramPoint.of(100, 30), route.get(0).asPoint()),
                        new DiagramPointPair(route.get(0).asPoint(), route.get(1).asPoint()),
                        new DiagramPointPair(route.get(1).asPoint(), com.marcosmoreira.domainmodelstudio.domain.layout.DiagramPoint.of(340, 30))
                ),
                Set.of(source.elementId(), target.elementId()),
                profile.nodeMargin()
        ));
    }

    @Test
    void keepsStraightRouteWhenNodesAreAlignedAndNoObstacleExists() {
        NodeLayout source = NodeLayout.at("origen", 0, 0, 100, 60);
        NodeLayout target = NodeLayout.at("destino", 300, 0, 100, 60);
        DiagramObstacleMap obstacleMap = DiagramObstacleMap.fromNodes(List.of(source, target));
        AutoLayoutProfile profile = new AutoLayoutProfile(0, 0, 300, 200, 3, 6, 96);

        List<BendPoint> route = new OrthogonalDiagramRoutingEngine().route(
                source,
                target,
                AnchorSide.RIGHT,
                AnchorSide.LEFT,
                obstacleMap,
                profile
        );

        assertEquals(0, route.size());
    }
}
