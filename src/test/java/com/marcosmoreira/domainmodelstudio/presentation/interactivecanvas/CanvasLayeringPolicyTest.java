package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class CanvasLayeringPolicyTest {

    private final CanvasLayeringPolicy policy = CanvasLayeringPolicy.standard();

    @Test
    void containerLikeNodesStayBehindRegularNodes() {
        InteractiveCanvasNode actor = node("actor", "actor");
        InteractiveCanvasNode module = node("module", "module");
        InteractiveCanvasNode lane = node("lane", "lane");
        InteractiveCanvasNode klass = node("class", "uml-class");

        List<String> orderedIds = policy.orderNodes(List.of(actor, module, klass, lane)).stream()
                .map(InteractiveCanvasNode::id)
                .toList();

        assertEquals(List.of("module", "lane", "actor", "class"), orderedIds);
    }

    @Test
    void commonContainerKindsAreRecognized() {
        assertTrue(policy.isContainerLike(node("boundary", "system-boundary")));
        assertTrue(policy.isContainerLike(node("screen", "wireframe-screen")));
        assertTrue(policy.isContainerLike(node("environment", "architecture-environment")));
        assertTrue(policy.isContainerLike(node("network", "deployment-network")));
    }

    @Test
    void c4ContainerKeywordIsNotForcedBehindByItself() {
        assertFalse(policy.isContainerLike(node("api", "c4-container")),
                "Un contenedor C4 es una pieza ejecutable; no debe volverse fondo solo por llamarse container.");
    }

    private static InteractiveCanvasNode node(String id, String kind) {
        return new InteractiveCanvasNode(id, id, "", kind, true, false);
    }
}
