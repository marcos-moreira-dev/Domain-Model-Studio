package com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class LogicalBusinessSelectionTest {

    @Test
    void shouldRepresentDocumentAndEmptySelections() {
        assertTrue(LogicalBusinessSelection.none().empty());
        assertFalse(LogicalBusinessSelection.document().empty());
        assertEquals(LogicalBusinessSelectionKind.DOCUMENT, LogicalBusinessSelection.document().kind());
    }

    @Test
    void shouldKeepOwnerForOwnedEntityElements() {
        LogicalBusinessSelection attribute = LogicalBusinessSelection.attribute(" ENT-001 ", " ATR-001 ");
        LogicalBusinessSelection relationship = LogicalBusinessSelection.relationship("ENT-002", "REL-001");

        assertEquals(LogicalBusinessSelectionKind.ATTRIBUTE, attribute.kind());
        assertEquals("ATR-001", attribute.id());
        assertEquals("ENT-001", attribute.ownerId());
        assertEquals("ATR-001", attribute.traceabilityFocusId());
        assertEquals(LogicalBusinessSelectionKind.RELATIONSHIP, relationship.kind());
        assertEquals("REL-001", relationship.traceabilityFocusId());
    }

    @Test
    void shouldExposeTraceabilityOnlyForTraceableNodes() {
        assertEquals("ACC-001", LogicalBusinessSelection.item("ACC-001").traceabilityFocusId());
        assertEquals("ENT-001", LogicalBusinessSelection.entity("ENT-001").traceabilityFocusId());
        assertEquals("PEND-001", LogicalBusinessSelection.pendingQuestion("PEND-001").traceabilityFocusId());
        assertEquals("", LogicalBusinessSelection.section("sec-1").traceabilityFocusId());
        assertEquals("", LogicalBusinessSelection.group("rules").traceabilityFocusId());
    }
}
