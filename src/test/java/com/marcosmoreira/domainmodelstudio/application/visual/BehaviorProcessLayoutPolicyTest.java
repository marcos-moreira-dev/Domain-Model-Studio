package com.marcosmoreira.domainmodelstudio.application.visual;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramKind;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNode;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNodeKind;
import org.junit.jupiter.api.Test;

class BehaviorProcessLayoutPolicyTest {

    private final BehaviorProcessLayoutPolicy policy = new BehaviorProcessLayoutPolicy();

    @Test
    void bpmnLaneStartsWiderThanGenericBehaviorNode() {
        VisualNodeReference reference = policy.visualReference(
                BehaviorDiagramKind.BPMN_BASIC,
                new BehaviorNode("secretaria", BehaviorNodeKind.LANE, "Secretaría", "", "", "", 0),
                0);

        assertTrue(reference.preferredWidth() >= 340.0);
        assertTrue(reference.preferredHeight() >= 88.0);
    }

    @Test
    void operationalFlowStepStartsAsReadableProcedureBlock() {
        VisualNodeReference reference = policy.visualReference(
                BehaviorDiagramKind.OPERATIONAL_FLOW,
                new BehaviorNode("paso-1", BehaviorNodeKind.ACTIVITY, "Recibir equipo", "Técnico", "", "", 3),
                0);

        assertEquals(3, reference.orderIndex());
        assertTrue(reference.preferredWidth() >= 200.0);
        assertTrue(reference.preferredHeight() >= 80.0);
    }
}
