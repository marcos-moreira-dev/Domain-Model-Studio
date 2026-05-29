package com.marcosmoreira.domainmodelstudio.application.visual;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramKind;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorEdge;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorEdgeKind;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNode;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNodeKind;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class SequenceTimelineLayoutPolicyTest {

    private final SequenceTimelineLayoutPolicy policy = new SequenceTimelineLayoutPolicy();

    @Test
    void messageYIsDerivedFromTemporalOrder() {
        BehaviorDiagramDocument document = document();

        assertEquals(SequenceTimelineLayoutPolicy.MESSAGE_START_Y, policy.messageY(document, "m1"));
        assertEquals(SequenceTimelineLayoutPolicy.MESSAGE_START_Y + SequenceTimelineLayoutPolicy.MESSAGE_ROW_GAP,
                policy.messageY(document, "m2"));
        assertEquals(SequenceTimelineLayoutPolicy.MESSAGE_START_Y + 2 * SequenceTimelineLayoutPolicy.MESSAGE_ROW_GAP,
                policy.messageY(document, "m3"));
    }

    @Test
    void fragmentReferenceIsWideTemporalFrame() {
        var reference = policy.visualReference(
                new BehaviorNode("alt", BehaviorNodeKind.FRAGMENT, "alt error", "", "", "", 2), 0);

        assertTrue(reference.preferredWidth() >= 600.0);
        assertTrue(reference.preferredHeight() >= 140.0);
    }

    private static BehaviorDiagramDocument document() {
        return new BehaviorDiagramDocument("Secuencia", "borrador", LocalDate.now(), BehaviorDiagramKind.UML_SEQUENCE,
                List.of(participant("a"), participant("b")),
                List.of(
                        new BehaviorEdge("m1", "a", "b", BehaviorEdgeKind.MESSAGE, "uno", "", ""),
                        new BehaviorEdge("m2", "b", "a", BehaviorEdgeKind.ASYNC_MESSAGE, "dos", "", ""),
                        new BehaviorEdge("m3", "a", "b", BehaviorEdgeKind.RETURN_MESSAGE, "tres", "", "")
                ), "");
    }

    private static BehaviorNode participant(String id) {
        return new BehaviorNode(id, BehaviorNodeKind.PARTICIPANT, id, "", "", "", 0);
    }


    @Test
    void childFragmentWithParentIsIndentedInsideParentFrame() {
        BehaviorNode parent = new BehaviorNode("loop_estudiantes", BehaviorNodeKind.FRAGMENT,
                "loop | id: loop-estudiantes | titulo: Por estudiante | rango: 2..5", "", "", "", 3);
        BehaviorNode child = new BehaviorNode("alt_nota", BehaviorNodeKind.FRAGMENT,
                "alt | id: alt-nota | titulo: Nota válida | padre: loop-estudiantes | rango: 3..4", "", "", "", 4);
        BehaviorDiagramDocument document = new BehaviorDiagramDocument("Secuencia", "borrador", LocalDate.now(), BehaviorDiagramKind.UML_SEQUENCE,
                List.of(participant("a"), participant("b"), parent, child),
                List.of(
                        new BehaviorEdge("m1", "a", "b", BehaviorEdgeKind.MESSAGE, "uno", "", ""),
                        new BehaviorEdge("m2", "a", "b", BehaviorEdgeKind.MESSAGE, "dos", "", ""),
                        new BehaviorEdge("m3", "a", "b", BehaviorEdgeKind.MESSAGE, "tres", "", ""),
                        new BehaviorEdge("m4", "a", "b", BehaviorEdgeKind.MESSAGE, "cuatro", "", "")
                ), "");

        var parentLayout = policy.layoutForNode(document, parent, NodeLayout.at("fragment", 0, 0, 10, 10));
        var childLayout = policy.layoutForNode(document, child, NodeLayout.at("fragment", 0, 0, 10, 10));

        assertTrue(childLayout.x() > parentLayout.x());
        assertTrue(childLayout.width() < parentLayout.width());
    }


    @Test
    void fragmentLayoutHonorsManualResizeWhenUserMakesTheFrameLarger() {
        BehaviorNode fragment = new BehaviorNode("loop_estudiantes", BehaviorNodeKind.FRAGMENT,
                "loop | id: loop-estudiantes | titulo: Por estudiante | rango: 1..2", "", "", "", 1);
        BehaviorDiagramDocument document = new BehaviorDiagramDocument("Secuencia", "borrador", LocalDate.now(), BehaviorDiagramKind.UML_SEQUENCE,
                List.of(participant("a"), participant("b"), fragment),
                List.of(
                        new BehaviorEdge("m1", "a", "b", BehaviorEdgeKind.MESSAGE, "uno", "", ""),
                        new BehaviorEdge("m2", "b", "a", BehaviorEdgeKind.RETURN_MESSAGE, "dos", "", "")
                ), "");

        NodeLayout resizedByUser = NodeLayout.at("behavior-node:loop_estudiantes", 44, 120, 1200, 480);

        NodeLayout layout = policy.layoutForNode(document, fragment, resizedByUser);

        assertEquals(1200.0, layout.width());
        assertEquals(480.0, layout.height());
    }


    @Test
    void fragmentLayoutHonorsManualMoveWhenUserLocksFramePosition() {
        BehaviorNode fragment = new BehaviorNode("loop_estudiantes", BehaviorNodeKind.FRAGMENT,
                "loop | id: loop-estudiantes | titulo: Por estudiante | rango: 1..2", "", "", "", 1);
        BehaviorDiagramDocument document = new BehaviorDiagramDocument("Secuencia", "borrador", LocalDate.now(), BehaviorDiagramKind.UML_SEQUENCE,
                List.of(participant("a"), participant("b"), fragment),
                List.of(
                        new BehaviorEdge("m1", "a", "b", BehaviorEdgeKind.MESSAGE, "uno", "", ""),
                        new BehaviorEdge("m2", "b", "a", BehaviorEdgeKind.RETURN_MESSAGE, "dos", "", "")
                ), "");

        NodeLayout movedByUser = NodeLayout.at("behavior-node:loop_estudiantes", 360, 420, 900, 360).withLocked(true);

        NodeLayout layout = policy.layoutForNode(document, fragment, movedByUser);

        assertEquals(360.0, layout.x());
        assertEquals(420.0, layout.y());
        assertTrue(layout.locked());
    }

}