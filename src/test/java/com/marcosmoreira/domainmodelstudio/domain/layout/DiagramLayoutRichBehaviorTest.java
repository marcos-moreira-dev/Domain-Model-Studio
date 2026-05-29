package com.marcosmoreira.domainmodelstudio.domain.layout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import java.util.Map;
import org.junit.jupiter.api.Test;

class DiagramLayoutRichBehaviorTest {

    @Test
    void keepsIndependentLayoutsForChenAndCrowsFoot() {
        DiagramLayout chenLayout = DiagramLayout.empty(NotationType.CHEN)
                .withNode(NodeLayout.at("producto", 100, 200, 120, 50));
        DiagramLayout crowsFootLayout = DiagramLayout.empty(NotationType.CROWS_FOOT)
                .withNode(NodeLayout.at("producto", 600, 80, 180, 120));

        DiagramLayouts layouts = new DiagramLayouts(NotationType.CHEN, Map.of(
                NotationType.CHEN, chenLayout,
                NotationType.CROWS_FOOT, crowsFootLayout
        ));

        assertEquals(NotationType.CHEN, layouts.activeNotation());
        assertEquals(100, layouts.layoutFor(NotationType.CHEN).orElseThrow().nodeFor(DiagramElementId.of("producto")).orElseThrow().x());
        assertEquals(600, layouts.layoutFor(NotationType.CROWS_FOOT).orElseThrow().nodeFor(DiagramElementId.of("producto")).orElseThrow().x());

        DiagramLayouts switched = layouts.withActiveNotation(NotationType.CROWS_FOOT);
        assertEquals(NotationType.CROWS_FOOT, switched.activeNotation());
        assertEquals(600, switched.activeLayout().nodeFor(DiagramElementId.of("producto")).orElseThrow().x());
    }

    @Test
    void connectorStoresManualRouteAndMarkers() {
        ConnectorLayout connector = ConnectorLayout.straight("c_producto_categoria", "producto", "categoria")
                .withAnchors(AnchorSide.RIGHT, AnchorSide.LEFT)
                .withMarkers(ConnectorMarker.NONE, ConnectorMarker.OPTIONAL_MANY)
                .withMarkerOrientations(MarkerOrientation.AUTO, MarkerOrientation.RIGHT)
                .withBendPoint(BendPoint.of(320, 200))
                .withBendPoint(BendPoint.of(320, 360));

        assertEquals(ConnectorPathKind.POLYLINE, connector.pathKind());
        assertEquals(2, connector.bendPoints().size());
        assertEquals(AnchorSide.RIGHT, connector.sourceAnchor());
        assertEquals(AnchorSide.LEFT, connector.targetAnchor());
        assertEquals(ConnectorMarker.OPTIONAL_MANY, connector.targetMarker());
        assertEquals(MarkerOrientation.RIGHT, connector.targetMarkerOrientation());
        assertTrue(connector.visible());
    }
}
