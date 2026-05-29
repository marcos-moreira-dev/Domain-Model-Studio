package com.marcosmoreira.domainmodelstudio.application.logicalbusiness;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessIssueSeverity;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItem;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItemKind;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItemStatus;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessValidationIssue;
import java.util.List;
import org.junit.jupiter.api.Test;

class LogicalBusinessValidationServiceTest {

    private final LogicalBusinessValidationService service = new LogicalBusinessValidationService();

    @Test
    void detectsWeakRulesAndActions() {
        LogicalBusinessItem rule = new LogicalBusinessItem(
                "RN-001", LogicalBusinessItemKind.RULE, "Pago positivo", LogicalBusinessItemStatus.DRAFT,
                "entrevista", "Todo pago debe tener monto positivo.", "", "- Forma lógica: Pago(p) -> Monto(p) > 0", List.of());
        LogicalBusinessItem action = new LogicalBusinessItem(
                "ACC-001", LogicalBusinessItemKind.ACTION, "Registrar pago", LogicalBusinessItemStatus.DRAFT,
                "entrevista", "Registra dinero recibido.", "", "- Postcondiciones:\n  - POST-001", List.of("POST-001"));
        LogicalBusinessDocument document = LogicalBusinessDocument.blank("Óptica").withItem(rule).withItem(action);

        List<LogicalBusinessValidationIssue> issues = service.validate(document);

        assertTrue(issues.stream().anyMatch(issue -> issue.targetId().equals("RN-001")
                && issue.message().contains("lectura humana")));
        assertTrue(issues.stream().anyMatch(issue -> issue.targetId().equals("ACC-001")
                && issue.message().contains("precondiciones")));
    }

    @Test
    void reportsBlockingWhenActionHasNoPostcondition() {
        LogicalBusinessItem action = new LogicalBusinessItem(
                "ACC-001", LogicalBusinessItemKind.ACTION, "Registrar orden", LogicalBusinessItemStatus.DRAFT,
                "entrevista", "Crea una orden.", "", "- Evidencia: registro de orden", List.of("PRE-001"));
        LogicalBusinessDocument document = LogicalBusinessDocument.blank("Óptica").withItem(action);

        long blocking = service.count(service.validate(document), LogicalBusinessIssueSeverity.BLOCKING);

        assertEquals(1, blocking);
    }
}
