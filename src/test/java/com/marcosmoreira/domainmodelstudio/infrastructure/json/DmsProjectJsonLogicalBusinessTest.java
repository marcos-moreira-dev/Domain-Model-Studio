package com.marcosmoreira.domainmodelstudio.infrastructure.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessAttributeCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocumentStatus;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessEntityCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItem;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItemKind;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItemStatus;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessMaturity;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessMaturityLevel;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessPendingQuestion;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessQuestionPriority;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessRelationshipCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessSection;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Round-trip .dms del proyecto documental de levantamiento lógico. */
class DmsProjectJsonLogicalBusinessTest {

    @Test
    void shouldPersistLogicalBusinessDocumentInsideDmsProject() {
        LogicalBusinessDocument document = sampleDocument();
        DiagramProject project = DiagramProject.blank("levantamiento", "Levantamiento lógico", DiagramTypeId.LOGICAL_BUSINESS_INTAKE)
                .withLogicalBusinessDocument(document);

        String json = new DmsProjectJsonWriter().write(project);
        DiagramProject reopened = new DmsProjectJsonReader().read(json);

        assertTrue(json.contains("\"logicalBusinessDocument\""));
        assertTrue(json.contains("\"entityCandidates\""));
        assertEquals(DiagramTypeId.LOGICAL_BUSINESS_INTAKE, reopened.metadata().diagramTypeId());
        assertTrue(reopened.logicalBusinessDocument().isPresent());

        LogicalBusinessDocument restored = reopened.logicalBusinessDocument().orElseThrow();
        assertEquals("Óptica Horizonte", restored.projectName());
        assertEquals(LogicalBusinessDocumentStatus.PARTIALLY_VALIDATED, restored.documentStatus());
        assertEquals(2, restored.items().size());
        assertEquals(1, restored.entityCandidates().size());
        assertEquals(1, restored.pendingQuestions().size());
        assertEquals(LogicalBusinessMaturityLevel.CONSISTENT, restored.maturity().level());
        assertEquals("Orden", restored.entityById("ENT-001").orElseThrow().name());
        assertEquals("saldoPendiente", restored.entityById("ENT-001").orElseThrow().attributes().get(0).name());
        assertEquals("cliente ordena", restored.entityById("ENT-001").orElseThrow().relationships().get(0).name());
    }

    private LogicalBusinessDocument sampleDocument() {
        LogicalBusinessItem rule = new LogicalBusinessItem("RN-001", LogicalBusinessItemKind.RULE,
                "Toda orden debe tener cliente", LogicalBusinessItemStatus.VALIDATED,
                "entrevista", "La orden debe estar vinculada con un cliente.",
                "Si existe una orden, debe existir un cliente asociado.",
                "∀o (Orden(o) → ∃c Cliente(c) ∧ AsociadaA(o,c))", List.of("ENT-001"));
        LogicalBusinessItem invariant = new LogicalBusinessItem("INV-001", LogicalBusinessItemKind.INVARIANT,
                "Saldo no negativo", LogicalBusinessItemStatus.COMPLETE,
                "criterio técnico", "El saldo pendiente no puede ser negativo.",
                "Toda orden conserva saldo mayor o igual a cero.",
                "∀o (Orden(o) → SaldoPendiente(o) ≥ 0)", List.of("RN-001", "ATR-001"));
        LogicalBusinessAttributeCandidate attribute = new LogicalBusinessAttributeCandidate("ATR-001", "ENT-001",
                "saldoPendiente", "Dato necesario para pagos y cierre de orden.", "decimal",
                true, "total - pagos", "Cobros falsos", List.of("INV-001"), List.of("RN-001"), List.of("INV-001"));
        LogicalBusinessRelationshipCandidate relationship = new LogicalBusinessRelationshipCandidate("REL-001",
                "ENT-001", "ENT-001", "cliente ordena", "Cliente 1 → M Orden",
                "La regla RN-001 exige cliente asociado a la orden.", List.of("RN-001"));
        LogicalBusinessEntityCandidate entity = new LogicalBusinessEntityCandidate("ENT-001", "Orden",
                LogicalBusinessItemStatus.COMPLETE, "Entidad derivada de registro y cierre de órdenes.",
                List.of(attribute), List.of(relationship), List.of("ACC-001"), List.of("RN-001"),
                List.of("INV-001"), List.of("CU-001"), List.of("CU-002"), List.of("CU-003"),
                "Modelarla mal rompe pagos y entregas.");
        LogicalBusinessPendingQuestion question = new LogicalBusinessPendingQuestion("PEND-001",
                "¿La orden puede cerrarse con saldo pendiente?", "POST-001", LogicalBusinessQuestionPriority.HIGH,
                LogicalBusinessItemStatus.WITH_DOUBTS);
        return new LogicalBusinessDocument("Óptica Horizonte", "v0.1", LocalDate.of(2026, 5, 20),
                LogicalBusinessDocumentStatus.PARTIALLY_VALIDATED, "entrevista",
                List.of(new LogicalBusinessSection("sec-reglas", "Reglas", "Reglas del negocio",
                        LogicalBusinessItemStatus.COMPLETE, List.of("RN-001", "INV-001"), "")),
                List.of(rule, invariant), List.of(entity), List.of(question),
                new LogicalBusinessMaturity(LogicalBusinessMaturityLevel.CONSISTENT,
                        List.of("Reglas principales detectadas"), List.of("Falta validar cierre"),
                        List.of("Validar PEND-001")),
                "Documento de prueba");
    }
}
