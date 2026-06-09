package com.marcosmoreira.domainmodelstudio.application.logicalbusiness.derivation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.catalog.DefaultDiagramTypeRegistry;
import com.marcosmoreira.domainmodelstudio.application.importmodel.MarkdownModelParsingException;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessAttributeCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessEntityCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItem;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItemKind;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessRelationshipCandidate;
import com.marcosmoreira.domainmodelstudio.infrastructure.markdown.DiagramMarkdownImportDispatcher;
import java.util.List;
import org.junit.jupiter.api.Test;

class LogicalBusinessDerivationImportabilityTest {

    private final LogicalBusinessDerivationService service = new LogicalBusinessDerivationService();
    private final DiagramMarkdownImportDispatcher dispatcher = new DiagramMarkdownImportDispatcher(new DefaultDiagramTypeRegistry());

    @Test
    void everyCompatibleDraftMarkedImportableCanBeParsedByItsDestinationImporter() throws Exception {
        for (LogicalBusinessDerivationDraft draft : service.compatibleDrafts(sampleDocument())) {
            assertRevisableImportableContract(draft);

            DiagramProject imported = dispatcher.parse(draft.markdown(), draft.fileName());

            assertEquals(DiagramTypeId.of(draft.target().diagramType()), imported.metadata().diagramTypeId(),
                    "Tipo importado desde " + draft.target());
            assertDestinationPayload(draft.target(), imported);
        }
    }

    @Test
    void fallbackDraftsFromEmptyLogicalDocumentAreStillReviewableAndImportable() throws Exception {
        LogicalBusinessDocument empty = LogicalBusinessDocument.blank("Levantamiento mínimo");

        for (LogicalBusinessDerivationDraft draft : service.compatibleDrafts(empty)) {
            assertRevisableImportableContract(draft);

            DiagramProject imported = dispatcher.parse(draft.markdown(), draft.fileName());

            assertEquals(DiagramTypeId.of(draft.target().diagramType()), imported.metadata().diagramTypeId(),
                    "Tipo importado desde fallback " + draft.target());
            assertDestinationPayload(draft.target(), imported);
        }
    }

    private void assertRevisableImportableContract(LogicalBusinessDerivationDraft draft) {
        assertTrue(draft.markdown().contains("sample_kind: \"compatible-draft\""));
        assertTrue(draft.markdown().contains("status: \"borrador compatible revisable\""));
        assertTrue(draft.markdown().contains("importable: true"));
        assertTrue(draft.markdown().contains("source_mode: \"levantamiento-logico-como-fuente\""));
        assertTrue(draft.markdown().contains("auto_import: false"));
        assertFalse(draft.warnings().isEmpty());
    }

    private void assertDestinationPayload(LogicalBusinessDerivationTarget target, DiagramProject imported)
            throws MarkdownModelParsingException {
        switch (target) {
            case FREE_GRAPH -> {
                assertTrue(imported.freeGraph().isPresent());
                assertFalse(imported.freeGraph().orElseThrow().nodes().isEmpty());
            }
            case LOGICAL_BUSINESS_GRAPH -> {
                assertTrue(imported.logicalBusinessGraphDocument().isPresent());
                assertFalse(imported.logicalBusinessGraphDocument().orElseThrow().nodes().isEmpty());
                assertFalse(imported.logicalBusinessGraphDocument().orElseThrow().edges().isEmpty());
            }
            case CONCEPTUAL_MODEL -> assertFalse(imported.model().entities().isEmpty());
            case DATA_DICTIONARY -> {
                assertTrue(imported.dataDictionary().isPresent());
                assertFalse(imported.dataDictionary().orElseThrow().entities().isEmpty());
            }
            case UML_USE_CASE, BPMN_BASIC -> {
                assertTrue(imported.behaviorDiagram().isPresent());
                assertFalse(imported.behaviorDiagram().orElseThrow().nodes().isEmpty());
            }
            case ROLES_PERMISSIONS -> {
                assertTrue(imported.rolesPermissions().isPresent());
                assertFalse(imported.rolesPermissions().orElseThrow().roles().isEmpty());
                assertFalse(imported.rolesPermissions().orElseThrow().permissions().isEmpty());
            }
            case SCREEN_FLOW -> {
                assertTrue(imported.screenFlow().isPresent());
                assertFalse(imported.screenFlow().orElseThrow().screens().isEmpty());
            }
            case ADMIN_WIREFRAMES -> {
                assertTrue(imported.wireframe().isPresent());
                assertFalse(imported.wireframe().orElseThrow().screens().isEmpty());
                assertFalse(imported.wireframe().orElseThrow().components().isEmpty());
            }
        }
    }

    private LogicalBusinessDocument sampleDocument() {
        LogicalBusinessItem rule = new LogicalBusinessItem(
                "RN-001", LogicalBusinessItemKind.RULE, "Documento obligatorio", null,
                "entrevista", "El cliente debe tener documento de identidad.",
                "Todo cliente debe conservar identificación verificable.", "", List.of());
        LogicalBusinessItem invariant = new LogicalBusinessItem(
                "INV-001", LogicalBusinessItemKind.INVARIANT, "Orden con cliente", null,
                "entrevista", "Toda orden debe pertenecer a un cliente.",
                "No puede existir una orden sin cliente asociado.", "", List.of("ENT-001", "ENT-002"));
        LogicalBusinessItem actor = new LogicalBusinessItem(
                "ACT-001", LogicalBusinessItemKind.ACTOR, "Secretaría", null,
                "entrevista", "Persona que registra órdenes y actualiza datos.",
                "Responsable operativo de atención.", "", List.of());
        LogicalBusinessItem useCase = new LogicalBusinessItem(
                "CU-001", LogicalBusinessItemKind.USE_CASE, "Registrar orden de lentes", null,
                "entrevista", "Registra receta, cliente y pedido.", "", "", List.of("RN-001", "ENT-001", "ENT-002"));
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
                .withEditableDetails("Cliente", null, "La orden necesita una persona responsable.",
                        List.of("RN-001", "CU-001"), List.of(), List.of(), List.of(), List.of(), List.of(), "")
                .withAttribute(documentId)
                .withRelationship(relation);
        LogicalBusinessEntityCandidate order = LogicalBusinessEntityCandidate.of(
                        "ENT-002", "Orden", "El caso de uso registra una orden de lentes.")
                .withEditableDetails("Orden", null, "El caso de uso registra una orden de lentes.",
                        List.of("CU-001", "INV-001"), List.of(), List.of(), List.of(), List.of(), List.of(), "");
        return LogicalBusinessDocument.blank("Óptica Horizonte")
                .withItem(rule)
                .withItem(invariant)
                .withItem(actor)
                .withItem(useCase)
                .withItem(action)
                .withEntityCandidate(order)
                .withEntityCandidate(client);
    }
}
