package com.marcosmoreira.domainmodelstudio.presentation.drawing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class DiagramStyleTokenTest {

    @Test
    void nodeStyleCarriesCommonAndSpecificClasses() {
        DiagramShapeStyle style = DiagramShapeStyle.node("module-map-node", true);

        assertTrue(style.styleClasses().contains(DiagramPalette.NODE));
        assertTrue(style.styleClasses().contains("module-map-node"));
        assertTrue(style.styleClasses().contains(DiagramPalette.NODE_SELECTED));
    }

    @Test
    void connectorStyleKeepsArrowSemanticAsGeometryOnly() {
        DiagramConnectorStyle style = DiagramConnectorStyle.directed("uml-dependency", false)
                .withArrowKind(DiagramArrowKind.HOLLOW_TRIANGLE)
                .withDashed(true);

        assertEquals(DiagramArrowKind.HOLLOW_TRIANGLE, style.arrowKind());
        assertTrue(style.dashed());
        assertTrue(style.lineStyleClasses().contains(DiagramPalette.CONNECTOR));
    }
}
