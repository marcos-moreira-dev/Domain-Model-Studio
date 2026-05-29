package com.marcosmoreira.domainmodelstudio.presentation.canvas;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.marcosmoreira.domainmodelstudio.domain.layout.AnchorSide;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import org.junit.jupiter.api.Test;

class ConceptualAnchorResolverTest {

    private static final NodeLayout NODE = NodeLayout.at("entidad", 100, 100, 80, 60);

    @Test
    void resolvesLeftAnchorNearLeftEdgeCenter() {
        assertEquals(AnchorSide.LEFT, ConceptualAnchorResolver.nearestAnchor(NODE, 90, 130));
    }

    @Test
    void resolvesRightAnchorNearRightEdgeCenter() {
        assertEquals(AnchorSide.RIGHT, ConceptualAnchorResolver.nearestAnchor(NODE, 195, 130));
    }

    @Test
    void resolvesTopAndBottomAnchorsNearVerticalEdges() {
        assertEquals(AnchorSide.TOP, ConceptualAnchorResolver.nearestAnchor(NODE, 140, 80));
        assertEquals(AnchorSide.BOTTOM, ConceptualAnchorResolver.nearestAnchor(NODE, 140, 180));
    }
}
