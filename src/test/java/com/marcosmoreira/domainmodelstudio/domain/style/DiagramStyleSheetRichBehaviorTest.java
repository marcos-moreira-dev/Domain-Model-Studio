package com.marcosmoreira.domainmodelstudio.domain.style;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import org.junit.jupiter.api.Test;

class DiagramStyleSheetRichBehaviorTest {

    @Test
    void resolvesDefaultStyleWhenElementHasNoExplicitStyle() {
        DiagramStyleSheet styleSheet = DiagramStyleSheet.defaults();

        ElementStyle resolved = styleSheet.resolvedStyleFor(DiagramElementId.of("producto"));

        assertEquals(styleSheet.defaultStyle(), resolved);
    }

    @Test
    void storesExplicitStyleWithoutChangingDefaultStyle() {
        DiagramStyleSheet styleSheet = DiagramStyleSheet.defaults();
        ElementStyle custom = ElementStyle.defaultElement()
                .withFill(new FillStyle(RgbaColor.fromHex("#EAF2FF")))
                .withStroke(new StrokeStyle(RgbaColor.fromHex("#2F6DB3"), 2.0, StrokePattern.SOLID));

        DiagramStyleSheet updated = styleSheet.withElementStyle(DiagramElementId.of("producto"), custom);

        assertTrue(updated.explicitStyleFor(DiagramElementId.of("producto")).isPresent());
        assertEquals("#EAF2FF", updated.resolvedStyleFor(DiagramElementId.of("producto")).fill().color().toHex());
        assertEquals(styleSheet.defaultStyle().fill().color().toHex(), updated.defaultStyle().fill().color().toHex());
    }
}
