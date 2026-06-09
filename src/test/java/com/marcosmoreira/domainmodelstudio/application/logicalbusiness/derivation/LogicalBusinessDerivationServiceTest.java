package com.marcosmoreira.domainmodelstudio.application.logicalbusiness.derivation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessAttributeCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessEntityCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItem;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItemKind;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessRelationshipCandidate;
import java.util.List;
import org.junit.jupiter.api.Test;

class LogicalBusinessDerivationServiceTest {

    private final LogicalBusinessDerivationService service = new LogicalBusinessDerivationService();

    @Test
    void preparesCompatibleMarkdownForAllPlannedTargets() {
        List<LogicalBusinessDerivationDraft> drafts = service.compatibleDrafts(sampleDocument());

        assertEquals(9, drafts.size());
        assertTrue(drafts.stream().allMatch(draft -> draft.markdown().contains("importable: true")));
        assertTrue(drafts.stream().allMatch(draft -> !draft.warnings().isEmpty()));
        assertTrue(drafts.stream().anyMatch(draft -> draft.target() == LogicalBusinessDerivationTarget.FREE_GRAPH));
        assertTrue(drafts.stream().anyMatch(draft -> draft.target() == LogicalBusinessDerivationTarget.LOGICAL_BUSINESS_GRAPH));
        assertTrue(drafts.stream().anyMatch(draft -> draft.target() == LogicalBusinessDerivationTarget.ADMIN_WIREFRAMES));
    }

    @Test
    void conceptualAndDictionaryDraftsUseEntityAttributesAndRelationships() {
        LogicalBusinessDocument document = sampleDocument();

        LogicalBusinessDerivationDraft conceptual = service.compatibleDraft(document, LogicalBusinessDerivationTarget.CONCEPTUAL_MODEL);
        LogicalBusinessDerivationDraft dictionary = service.compatibleDraft(document, LogicalBusinessDerivationTarget.DATA_DICTIONARY);

        assertTrue(conceptual.markdown().contains("## Cliente"));
        assertTrue(conceptual.markdown().contains("- documento_identidad"));
        assertTrue(conceptual.markdown().contains("## solicita"));
        assertTrue(dictionary.markdown().contains("| documentoIdentidad | texto | pendiente | RN-001 |"));
    }

    @Test
    void behaviorAndNavigationDraftsUseActionsOrUseCasesAsReviewableSteps() {
        LogicalBusinessDocument document = sampleDocument();

        LogicalBusinessDerivationDraft useCases = service.compatibleDraft(document, LogicalBusinessDerivationTarget.UML_USE_CASE);
        LogicalBusinessDerivationDraft bpmn = service.compatibleDraft(document, LogicalBusinessDerivationTarget.BPMN_BASIC);
        LogicalBusinessDerivationDraft screenFlow = service.compatibleDraft(document, LogicalBusinessDerivationTarget.SCREEN_FLOW);

        assertTrue(useCases.markdown().contains("Registrar orden de lentes"));
        assertTrue(bpmn.markdown().contains("- actividad: Registrar orden de lentes."));
        assertTrue(screenFlow.markdown().contains("inicio -> registrar_orden_de_lentes"));
    }



    @Test
    void logicalBusinessGraphDraftConnectsBackboneAndSemanticNodes() {
        LogicalBusinessDocument document = sampleDocument();

        LogicalBusinessDerivationDraft graph = service.compatibleDraft(document, LogicalBusinessDerivationTarget.LOGICAL_BUSINESS_GRAPH);

        assertTrue(graph.markdown().contains("diagram_type: \"logical-business-graph\""));
        assertTrue(graph.markdown().contains("| MF | Macroflujo |"));
        assertTrue(graph.markdown().contains("| CU-001 | CU | Registrar orden de lentes |"));
        assertTrue(graph.markdown().contains("| ACC-001 | ACC | Guardar orden |"));
        assertTrue(graph.markdown().contains("| RN-001 | aplica | CU-001 |"));
        assertTrue(graph.markdown().contains("| CU-001 | ejecuta | ACC-001 |"));
        assertTrue(graph.markdown().contains("| ACC-001 | garantiza | POST-001 |")
                || graph.markdown().contains("POST-001"));
    }

    private LogicalBusinessDocument sampleDocument() {
        LogicalBusinessItem rule = new LogicalBusinessItem(
                "RN-001", LogicalBusinessItemKind.RULE, "Documento obligatorio", null,
                "entrevista", "El cliente debe tener documento de identidad.",
                "Todo cliente debe conservar identificación verificable.", "", List.of());
        LogicalBusinessItem useCase = new LogicalBusinessItem(
                "CU-001", LogicalBusinessItemKind.USE_CASE, "Registrar orden de lentes", null,
                "entrevista", "Registra receta, cliente y pedido.", "", "", List.of("RN-001", "ENT-001"));
        LogicalBusinessItem post = new LogicalBusinessItem(
                "POST-001", LogicalBusinessItemKind.POSTCONDITION, "Orden guardada", null,
                "entrevista", "La orden queda persistida con evidencia.", "", "", List.of());
        LogicalBusinessItem action = new LogicalBusinessItem(
                "ACC-001", LogicalBusinessItemKind.ACTION, "Guardar orden", null,
                "entrevista", "Persiste la orden validada.", "", "", List.of("CU-001", "POST-001"));
        LogicalBusinessAttributeCandidate documentId = new LogicalBusinessAttributeCandidate(
                "ATR-001", "ENT-001", "documentoIdentidad", "identifica al cliente", "texto", false,
                "", "cliente duplicado", List.of("RN-001"), List.of("RN-001"), List.of());
        LogicalBusinessRelationshipCandidate relation = new LogicalBusinessRelationshipCandidate(
                "REL-001", "ENT-001", "ENT-002", "solicita", "1..M",
                "Un cliente puede solicitar varias órdenes.", List.of("CU-001"));
        LogicalBusinessEntityCandidate client = LogicalBusinessEntityCandidate.of(
                "ENT-001", "Cliente", "La orden necesita una persona responsable.")
                .withAttribute(documentId)
                .withRelationship(relation);
        LogicalBusinessEntityCandidate order = LogicalBusinessEntityCandidate.of(
                "ENT-002", "Orden", "El caso de uso registra una orden de lentes.");
        return LogicalBusinessDocument.blank("Óptica Horizonte")
                .withItem(rule)
                .withItem(useCase)
                .withItem(post)
                .withItem(action)
                .withEntityCandidate(order)
                .withEntityCandidate(client);
    }
}
