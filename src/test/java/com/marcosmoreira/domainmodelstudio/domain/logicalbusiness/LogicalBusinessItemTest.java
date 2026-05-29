package com.marcosmoreira.domainmodelstudio.domain.logicalbusiness;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LogicalBusinessItemTest {

    @Test
    void itemRequiresIdPrefixConsistentWithKind() {
        LogicalBusinessItem rule = LogicalBusinessItem.of("RN-001", LogicalBusinessItemKind.RULE, "Pago positivo");

        assertEquals("RN-001", rule.id());
        assertEquals(LogicalBusinessItemKind.RULE, rule.kind());
        assertThrows(IllegalArgumentException.class,
                () -> LogicalBusinessItem.of("INV-001", LogicalBusinessItemKind.RULE, "Mal clasificado"));
    }

    @Test
    void referencesAreNormalizedAndImmutable() {
        LogicalBusinessItem action = new LogicalBusinessItem(
                "ACC-001",
                LogicalBusinessItemKind.ACTION,
                "Registrar pago",
                LogicalBusinessItemStatus.PARTIAL,
                "entrevista",
                "Registra dinero recibido.",
                "La acción cambia el estado financiero.",
                "Transformación pendiente de validar.",
                List.of(" RN-001 ", "INV-001", "RN-001")
        );

        assertEquals(List.of("RN-001", "INV-001"), action.referenceIds());
        assertThrows(UnsupportedOperationException.class, () -> action.referenceIds().add("POST-001"));
        assertTrue(action.withStatus(LogicalBusinessItemStatus.VALIDATED).status() == LogicalBusinessItemStatus.VALIDATED);
    }
}
