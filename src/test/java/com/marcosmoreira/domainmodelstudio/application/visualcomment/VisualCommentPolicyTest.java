package com.marcosmoreira.domainmodelstudio.application.visualcomment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.visualcomment.VisualComment;
import org.junit.jupiter.api.Test;

class VisualCommentPolicyTest {

    @Test
    void shouldNormalizePlaceholdersAndLimits() {
        assertEquals(VisualComment.DEFAULT_TITLE, VisualCommentPolicy.normalizeTitle(" "));
        assertEquals(80, VisualCommentPolicy.normalizeTitle("x".repeat(120)).length());
        assertEquals(1000, VisualCommentPolicy.normalizeDescription("d".repeat(1200)).length());
    }

    @Test
    void shouldClampWidthToOperationalRange() {
        assertEquals(VisualCommentPolicy.MIN_WIDTH, VisualCommentPolicy.preferredWidth("ABC"), 0.01);
        assertEquals(VisualCommentPolicy.MAX_WIDTH, VisualCommentPolicy.preferredWidth("Titulo ".repeat(40)), 0.01);

        double medium = VisualCommentPolicy.preferredWidth("Titulo operativo de revision");
        assertTrue(medium > VisualCommentPolicy.MIN_WIDTH);
        assertTrue(medium < VisualCommentPolicy.MAX_WIDTH);
    }
}
