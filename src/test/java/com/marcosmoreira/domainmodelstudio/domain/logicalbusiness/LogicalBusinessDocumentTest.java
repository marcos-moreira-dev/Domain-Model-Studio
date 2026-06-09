package com.marcosmoreira.domainmodelstudio.domain.logicalbusiness;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LogicalBusinessDocumentTest {

    @Test
    void documentIndexesItemsAndFiltersByKind() {
        LogicalBusinessDocument document = LogicalBusinessDocument.blank("Óptica Horizonte")
                .withItem(LogicalBusinessItem.of("RN-001", LogicalBusinessItemKind.RULE, "Pago positivo"))
                .withItem(LogicalBusinessItem.of("INV-001", LogicalBusinessItemKind.INVARIANT, "Saldo no negativo"));

        assertEquals("Óptica Horizonte", document.projectName());
        assertTrue(document.itemById("RN-001").isPresent());
        assertEquals(1, document.itemsByKind(LogicalBusinessItemKind.INVARIANT).size());
        assertThrows(IllegalArgumentException.class,
                () -> document.withItem(LogicalBusinessItem.of("RN-001", LogicalBusinessItemKind.RULE, "Duplicada")));
    }

    @Test
    void documentValidatesSectionReferences() {
        LogicalBusinessSection section = new LogicalBusinessSection(
                "sec-reglas", "Reglas", "", LogicalBusinessItemStatus.PARTIAL, List.of("RN-404"), ""
        );

        assertThrows(IllegalArgumentException.class, () -> new LogicalBusinessDocument(
                "Proyecto", "v0.1", LocalDate.now(), LogicalBusinessDocumentStatus.DRAFT, "entrevista",
                List.of(section), List.of(), List.of(), List.of(), LogicalBusinessMaturity.initial(), ""
        ));
    }

    @Test
    void documentValidatesRelationshipsBetweenKnownEntities() {
        LogicalBusinessRelationshipCandidate relation = new LogicalBusinessRelationshipCandidate(
                "REL-001", "ENT-001", "ENT-002", "origina", "1..n",
                "Una acción genera evidencia entre ambas entidades.", List.of("ACC-001")
        );
        LogicalBusinessEntityCandidate payment = LogicalBusinessEntityCandidate.of(
                "ENT-001", "Pago", "Existe porque se registra dinero recibido."
        ).withRelationship(relation);

        assertThrows(IllegalArgumentException.class, () -> LogicalBusinessDocument.blank("Proyecto")
                .withEntityCandidate(payment));
    }

    @Test
    void criticalPendingQuestionsAreReportedAsBlockingIssues() {
        LogicalBusinessPendingQuestion question = new LogicalBusinessPendingQuestion(
                "PEND-001", "¿Cuándo una orden se considera entregada?", "POST-001",
                LogicalBusinessQuestionPriority.CRITICAL, LogicalBusinessItemStatus.DRAFT
        );
        LogicalBusinessDocument document = LogicalBusinessDocument.blank("Proyecto")
                .withPendingQuestion(question);

        assertFalse(document.structuralIssues().isEmpty());
        assertEquals(LogicalBusinessIssueSeverity.BLOCKING, document.structuralIssues().get(0).severity());
    }
}
