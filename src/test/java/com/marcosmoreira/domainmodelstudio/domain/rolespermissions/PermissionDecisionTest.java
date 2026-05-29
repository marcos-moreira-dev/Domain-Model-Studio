package com.marcosmoreira.domainmodelstudio.domain.rolespermissions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

final class PermissionDecisionTest {

    @Test
    void derivesVisibleDecisionFromAssignmentState() {
        assertEquals(PermissionDecision.NOT_ASSIGNED, PermissionDecision.fromAssignment(null));
        assertEquals(PermissionDecision.ALLOWED,
                PermissionDecision.fromAssignment(new PermissionAssignment("a1", "admin", "sales.read", true, "", "")));
        assertEquals(PermissionDecision.CONDITIONAL,
                PermissionDecision.fromAssignment(new PermissionAssignment("a2", "seller", "sales.edit", true, "solo ventas abiertas", "")));
        assertEquals(PermissionDecision.DENIED,
                PermissionDecision.fromAssignment(new PermissionAssignment("a3", "seller", "users.delete", false, "", "")));
    }

    @Test
    void parsesAdministrativeTextDecision() {
        assertTrue(PermissionDecision.fromText("Permitido").allowedValue());
        assertTrue(PermissionDecision.fromText("Condicionado").allowedValue());
        assertFalse(PermissionDecision.fromText("Denegado").allowedValue());
        assertEquals("△", PermissionDecision.CONDITIONAL.matrixSymbol());
    }
}
