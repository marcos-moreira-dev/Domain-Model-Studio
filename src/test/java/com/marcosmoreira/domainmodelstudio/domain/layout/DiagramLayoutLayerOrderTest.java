package com.marcosmoreira.domainmodelstudio.domain.layout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DiagramLayoutLayerOrderTest {

    @Test
    void shouldMoveNodesToFrontAndBackPreservingRelativeOrder() {
        DiagramLayout layout = layout("a", "b", "c", "d");

        DiagramLayout front = layout.bringNodesToFront(Set.of(id("b"), id("c")));
        assertOrder(front, "a", "d", "b", "c");
        assertEquals(3, front.nodeFor(id("c")).orElseThrow().zOrder());

        DiagramLayout back = front.sendNodesToBack(Set.of(id("b"), id("c")));
        assertOrder(back, "b", "c", "a", "d");
        assertEquals(0, back.nodeFor(id("b")).orElseThrow().zOrder());
    }

    @Test
    void shouldRaiseAndLowerOneLayerOnly() {
        DiagramLayout layout = layout("a", "b", "c", "d");

        assertOrder(layout.raiseNodes(Set.of(id("b"))), "a", "c", "b", "d");
        assertOrder(layout.lowerNodes(Set.of(id("c"))), "a", "c", "b", "d");
    }

    private static DiagramLayout layout(String... ids) {
        return new DiagramLayout(
                NotationType.CHEN,
                java.util.Arrays.stream(ids).map(id -> NodeLayout.at(id, 0, 0, 100, 60)).toList(),
                List.of());
    }

    private static DiagramElementId id(String value) {
        return DiagramElementId.of(value);
    }

    private static void assertOrder(DiagramLayout layout, String... expected) {
        assertEquals(List.of(expected), layout.nodes().stream().map(node -> node.elementId().value()).toList());
    }
}
