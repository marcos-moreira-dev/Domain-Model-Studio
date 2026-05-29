package com.marcosmoreira.domainmodelstudio.domain.layout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;

class ConnectorLayoutTest {

    @Test
    void addsBendPointAndTurnsIntoPolyline() {
        ConnectorLayout connector = ConnectorLayout.straight("c1", "producto", "pertenece");

        ConnectorLayout updated = connector.withBendPoint(BendPoint.of(100, 200));

        assertEquals(ConnectorPathKind.POLYLINE, updated.pathKind());
        assertEquals(1, updated.bendPoints().size());
    }

    @Test
    void allowsExplicitAnchorsForManualCorrection() {
        ConnectorLayout connector = ConnectorLayout.straight("c1", "producto", "pertenece")
                .withAnchors(AnchorSide.RIGHT, AnchorSide.LEFT);

        assertEquals(AnchorSide.RIGHT, connector.sourceAnchor());
        assertEquals(AnchorSide.LEFT, connector.targetAnchor());
    }
    @Test
    void movesAndRemovesBendPoint() {
        ConnectorLayout connector = ConnectorLayout.straight("c1", "producto", "pertenece")
                .withBendPoint(BendPoint.of(100, 200));

        ConnectorLayout moved = connector.withMovedBendPoint(0, 20, -10);
        assertEquals(120, moved.bendPoints().get(0).x());
        assertEquals(190, moved.bendPoints().get(0).y());

        ConnectorLayout withoutPoint = moved.withoutBendPoint(0);
        assertEquals(ConnectorPathKind.STRAIGHT, withoutPoint.pathKind());
        assertEquals(0, withoutPoint.bendPoints().size());
    }

    @Test
    void rejectsInvalidBendPointIndex() {
        ConnectorLayout connector = ConnectorLayout.straight("c1", "producto", "pertenece");

        assertThrows(IllegalArgumentException.class, () -> connector.withoutBendPoint(0));
    }

    @Test
    void movesRelationshipLabelOffsetWithoutDetachingItTooFar() {
        ConnectorLayout connector = ConnectorLayout.straight("c1", "producto", "pertenece");

        ConnectorLayout moved = connector.withMovedLabelOffset(30, -12, 240);

        assertEquals(30, moved.labelOffsetX());
        assertEquals(-12, moved.labelOffsetY());
    }

    @Test
    void keepsOrthogonalPathKindForAutomaticRoutes() {
        ConnectorLayout connector = ConnectorLayout.straight("c1", "producto", "categoria")
                .withOrthogonalBendPoints(List.of(BendPoint.of(100, 120), BendPoint.of(260, 120)));

        assertEquals(ConnectorPathKind.ORTHOGONAL, connector.pathKind());
        assertEquals(2, connector.bendPoints().size());
    }

}
