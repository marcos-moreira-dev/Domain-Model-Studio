package com.marcosmoreira.domainmodelstudio.application.logicalbusiness;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessAttributeCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessEntityCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItem;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItemKind;
import java.util.List;
import org.junit.jupiter.api.Test;

class LogicalBusinessTraceabilityServiceTest {

    private final LogicalBusinessTraceabilityService service = new LogicalBusinessTraceabilityService();

    @Test
    void buildsOutgoingAndIncomingLinksForItems() {
        LogicalBusinessItem invariant = LogicalBusinessItem.of("INV-001", LogicalBusinessItemKind.INVARIANT, "Saldo no negativo");
        LogicalBusinessItem action = new LogicalBusinessItem(
                "ACC-001", LogicalBusinessItemKind.ACTION, "Registrar pago", null,
                "entrevista", "Registra pago", "", "", List.of("INV-001"));
        LogicalBusinessDocument document = LogicalBusinessDocument.blank("Óptica")
                .withItem(invariant)
                .withItem(action);

        LogicalBusinessTraceabilityReport report = service.reportFor(document, "INV-001");

        assertTrue(report.incoming().stream().anyMatch(link -> link.sourceId().equals("ACC-001")));
        assertTrue(report.outgoing().isEmpty());
    }

    @Test
    void connectsEntitiesAndAttributes() {
        LogicalBusinessAttributeCandidate attribute = new LogicalBusinessAttributeCandidate(
                "ATR-001", "ENT-001", "saldo", "se consulta en cobros", "decimal", false,
                "", "saldo falso", List.of("RN-001"), List.of("RN-001"), List.of());
        LogicalBusinessEntityCandidate entity = LogicalBusinessEntityCandidate.of(
                "ENT-001", "Cuenta por cobrar", "La acción ACC-001 necesita saldo pendiente.")
                .withAttribute(attribute);
        LogicalBusinessDocument document = LogicalBusinessDocument.blank("Óptica")
                .withItem(LogicalBusinessItem.of("RN-001", LogicalBusinessItemKind.RULE, "Saldo calculado"))
                .withEntityCandidate(entity);

        LogicalBusinessTraceabilityReport report = service.reportFor(document, "ENT-001");

        assertFalse(report.outgoing().isEmpty());
        assertTrue(report.outgoing().stream().anyMatch(link -> link.targetId().equals("ATR-001")));
    }
}
