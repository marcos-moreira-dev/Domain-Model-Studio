package com.marcosmoreira.domainmodelstudio.application.visual;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramKind;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNode;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNodeKind;
import org.junit.jupiter.api.Test;

class UmlBehaviorLayoutPolicyTest {

    private final UmlBehaviorLayoutPolicy policy = new UmlBehaviorLayoutPolicy();

    @Test
    void useCaseSystemBoundaryStartsAsLargeContainer() {
        VisualNodeReference reference = policy.visualReference(
                BehaviorDiagramKind.UML_USE_CASE,
                new BehaviorNode("sistema", BehaviorNodeKind.SYSTEM_BOUNDARY, "Sistema", "", "", "", 0),
                0);

        assertTrue(reference.preferredWidth() >= 400.0);
        assertTrue(reference.preferredHeight() >= 280.0);
    }

    @Test
    void activityForkIsWideAndShort() {
        VisualNodeReference reference = policy.visualReference(
                BehaviorDiagramKind.UML_ACTIVITY,
                new BehaviorNode("fork", BehaviorNodeKind.FORK, "Bifurcar", "", "", "", 2),
                0);

        assertEquals(2, reference.orderIndex());
        assertTrue(reference.preferredWidth() > reference.preferredHeight());
    }

    @Test
    void stateNodeIsReadableStateBox() {
        VisualNodeReference reference = policy.visualReference(
                BehaviorDiagramKind.UML_STATE,
                new BehaviorNode("pendiente", BehaviorNodeKind.STATE, "Pendiente", "", "", "", 0),
                0);

        assertTrue(reference.preferredWidth() >= 190.0);
        assertTrue(reference.preferredHeight() >= 80.0);
    }
}
