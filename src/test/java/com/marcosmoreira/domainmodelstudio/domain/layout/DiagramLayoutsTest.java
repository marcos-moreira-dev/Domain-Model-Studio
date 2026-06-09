package com.marcosmoreira.domainmodelstudio.domain.layout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import org.junit.jupiter.api.Test;

class DiagramLayoutsTest {

    @Test
    void keepsSeparateLayoutsByNotation() {
        DiagramLayout chenLayout = DiagramLayout.empty(NotationType.CHEN)
                .withNode(NodeLayout.at("producto", 10, 20, 120, 60));
        DiagramLayout crowsFootLayout = DiagramLayout.empty(NotationType.CROWS_FOOT)
                .withNode(NodeLayout.at("producto", 300, 80, 160, 120));

        DiagramLayouts layouts = DiagramLayouts.empty()
                .withLayout(chenLayout)
                .withLayout(crowsFootLayout)
                .withActiveNotation(NotationType.CROWS_FOOT);

        assertEquals(NotationType.CROWS_FOOT, layouts.activeNotation());
        assertEquals(300, layouts.activeLayout().nodeFor(com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId.of("producto")).orElseThrow().x());
        assertTrue(layouts.layoutFor(NotationType.CHEN).isPresent());
    }
}
