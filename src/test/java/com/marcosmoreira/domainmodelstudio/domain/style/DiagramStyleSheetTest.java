package com.marcosmoreira.domainmodelstudio.domain.style;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import org.junit.jupiter.api.Test;

class DiagramStyleSheetTest {

    @Test
    void resolvesDefaultStyleWhenElementHasNoCustomStyle() {
        DiagramStyleSheet sheet = DiagramStyleSheet.defaults();

        ElementStyle resolved = sheet.resolvedStyleFor(DiagramElementId.of("producto"));

        assertEquals(sheet.defaultStyle(), resolved);
    }

    @Test
    void storesExplicitStyleForElement() {
        DiagramElementId productId = DiagramElementId.of("producto");
        ElementStyle custom = ElementStyle.defaultElement()
                .withFill(FillStyle.of(RgbaColor.fromHex("#FFF7CC")));

        DiagramStyleSheet sheet = DiagramStyleSheet.defaults().withElementStyle(productId, custom);

        assertTrue(sheet.explicitStyleFor(productId).isPresent());
        assertEquals("#FFF7CC", sheet.resolvedStyleFor(productId).fill().color().toHex());
    }
}
