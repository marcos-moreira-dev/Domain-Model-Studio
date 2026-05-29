package com.marcosmoreira.domainmodelstudio.presentation.drawing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class DiagramLabelPolicyTest {

    @Test
    void criticalPolicyKeepsCompleteText() {
        DiagramLabelPolicy policy = DiagramLabelPolicy.critical();
        String text = "Linea uno\nLinea dos\nLinea tres\nLinea cuatro\nLinea cinco";

        assertEquals(text, policy.visibleText(text));
        assertFalse(policy.shouldAttachTooltip(text));
    }

    @Test
    void compactPolicyTrimsSecondaryTextAndRequiresTooltip() {
        DiagramLabelPolicy policy = DiagramLabelPolicy.compactMeta();
        String text = "uno\ndos\ntres";

        assertEquals("uno" + System.lineSeparator() + "dos…", policy.visibleText(text));
        assertTrue(policy.shouldAttachTooltip(text));
    }
}
