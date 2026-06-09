package com.marcosmoreira.domainmodelstudio.domain.layout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class NodeLayoutTest {

    @Test
    void movesNodeWithoutChangingSize() {
        NodeLayout layout = NodeLayout.at("producto", 10, 20, 120, 60);

        NodeLayout moved = layout.movedTo(100, 200);

        assertEquals(100, moved.x());
        assertEquals(200, moved.y());
        assertEquals(120, moved.width());
        assertEquals(60, moved.height());
    }

    @Test
    void rejectsInvalidSize() {
        assertThrows(IllegalArgumentException.class, () -> NodeLayout.at("producto", 10, 20, 0, 60));
    }
}
