package com.marcosmoreira.domainmodelstudio.domain.logicalbusiness;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LogicalBusinessEntityCandidateTest {

    @Test
    void entityCandidateOwnsAttributesWithLogicalJustification() {
        LogicalBusinessEntityCandidate entity = LogicalBusinessEntityCandidate.of(
                "ENT-001",
                "Cuenta por cobrar",
                "Existe porque las acciones de cobro y pago necesitan saldo trazable."
        );
        LogicalBusinessAttributeCandidate saldo = new LogicalBusinessAttributeCandidate(
                "ATR-001",
                "ENT-001",
                "saldoPendiente",
                "La invariante financiera necesita saber cuánto queda por cobrar.",
                "decimal",
                true,
                "totalEsperado - pagosVerificados",
                "Un saldo incorrecto produce cobros falsos.",
                List.of("INV-001"),
                List.of("RN-001"),
                List.of("INV-001")
        );

        LogicalBusinessEntityCandidate updated = entity.withAttribute(saldo);

        assertEquals(1, updated.attributes().size());
        assertTrue(updated.attributeById("ATR-001").isPresent());
        assertThrows(IllegalArgumentException.class, () -> updated.withAttribute(saldo));
    }

    @Test
    void calculatedAttributeRequiresFormula() {
        assertThrows(IllegalArgumentException.class, () -> new LogicalBusinessAttributeCandidate(
                "ATR-002", "ENT-001", "total", "Se calcula desde detalles.", "decimal",
                true, "", "", List.of(), List.of(), List.of()
        ));
    }

    @Test
    void relationshipMustTouchOwningEntity() {
        LogicalBusinessRelationshipCandidate unrelated = new LogicalBusinessRelationshipCandidate(
                "REL-001", "ENT-002", "ENT-003", "no toca", "1..n", "Relación de prueba.", List.of("RN-001")
        );
        LogicalBusinessEntityCandidate entity = LogicalBusinessEntityCandidate.of("ENT-001", "Pago", "Existe por registro de pago.");

        assertThrows(IllegalArgumentException.class, () -> entity.withRelationship(unrelated));
    }
}
