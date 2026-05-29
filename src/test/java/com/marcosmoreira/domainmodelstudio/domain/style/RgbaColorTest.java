package com.marcosmoreira.domainmodelstudio.domain.style;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class RgbaColorTest {

    @Test
    void parsesAndExportsHexColor() {
        RgbaColor color = RgbaColor.fromHex("#2F6DB3");

        assertEquals(47, color.red());
        assertEquals(109, color.green());
        assertEquals(179, color.blue());
        assertEquals("#2F6DB3", color.toHex());
    }

    @Test
    void rejectsInvalidChannel() {
        assertThrows(IllegalArgumentException.class, () -> RgbaColor.rgb(300, 0, 0));
    }
}
