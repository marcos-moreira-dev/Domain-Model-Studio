package com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessAttributeCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessEntityCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItem;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItemKind;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItemStatus;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessPendingQuestion;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessQuestionPriority;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessRelationshipCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessSection;
import com.marcosmoreira.domainmodelstudio.presentation.dialogs.ClickMessageDialog;
import java.util.ArrayList;
import java.util.List;

/** Operaciones CRUD controladas sobre el documento lógico activo. */
final class LogicalBusinessCrudOperations {

    private LogicalBusinessCrudOperations() {
    }

    static void createItem(LogicalBusinessViewModel viewModel) {
        LogicalBusinessDocument document = viewModel.currentDocument();
        if (document == null) {
            return;
        }
        LogicalBusinessCrudDialogs.item(document.sections()).ifPresent(request -> {
            String id = nextId(document, request.kind().prefix());
            LogicalBusinessItem item = new LogicalBusinessItem(id, request.kind(), defaulted(request.title(), id),
                    LogicalBusinessItemStatus.DRAFT, "edición manual", request.description(), "", "", List.of());
            LogicalBusinessDocument updated = document.withItem(item);
            updated = attachItemToSection(updated, request.sectionId(), id);
            replace(viewModel, updated);
            viewModel.selectItem(id);
        });
    }

    static void createEntity(LogicalBusinessViewModel viewModel) {
        LogicalBusinessDocument document = viewModel.currentDocument();
        if (document == null) {
            return;
        }
        LogicalBusinessCrudDialogs.entity().ifPresent(request -> {
            String id = nextId(document, LogicalBusinessItemKind.ENTITY.prefix());
            LogicalBusinessEntityCandidate entity = LogicalBusinessEntityCandidate.of(id,
                    defaulted(request.name(), id), defaulted(request.justification(), "Pendiente de justificar."));
            replace(viewModel, document.withEntityCandidate(entity));
            viewModel.selectEntity(id);
        });
    }

    static void createAttribute(LogicalBusinessViewModel viewModel) {
        LogicalBusinessDocument document = viewModel.currentDocument();
        if (document == null || viewModel.selectedEntity().isEmpty()) {
            return;
        }
        LogicalBusinessEntityCandidate entity = viewModel.selectedEntity().get();
        LogicalBusinessCrudDialogs.attribute().ifPresent(request -> {
            String id = nextEntityChildId(document, LogicalBusinessItemKind.ATTRIBUTE.prefix());
            LogicalBusinessAttributeCandidate attribute = new LogicalBusinessAttributeCandidate(id, entity.id(),
                    defaulted(request.name(), id), defaulted(request.reason(), "Pendiente de justificar."),
                    request.tentativeType(), false, "", "", List.of(), List.of(), List.of());
            replace(viewModel, document.withUpdatedEntityCandidate(entity.withAttribute(attribute)));
            viewModel.selectAttribute(entity.id(), id);
        });
    }

    static void createRelationship(LogicalBusinessViewModel viewModel) {
        LogicalBusinessDocument document = viewModel.currentDocument();
        if (document == null || document.entityCandidates().size() < 2) {
            return;
        }
        LogicalBusinessCrudDialogs.relationship(document.entityCandidates()).ifPresent(request -> {
            String id = nextEntityChildId(document, LogicalBusinessItemKind.RELATIONSHIP.prefix());
            LogicalBusinessRelationshipCandidate relationship = new LogicalBusinessRelationshipCandidate(id,
                    request.sourceEntityId(), request.targetEntityId(), defaulted(request.name(), id),
                    request.cardinalityHint(), defaulted(request.justification(), "Pendiente de justificar."), List.of());
            LogicalBusinessEntityCandidate owner = document.entityById(request.sourceEntityId())
                    .orElse(document.entityCandidates().get(0));
            replace(viewModel, document.withUpdatedEntityCandidate(owner.withRelationship(relationship)));
            viewModel.selectRelationship(owner.id(), id);
        });
    }

    static void createPendingQuestion(LogicalBusinessViewModel viewModel) {
        LogicalBusinessDocument document = viewModel.currentDocument();
        if (document == null) {
            return;
        }
        LogicalBusinessCrudDialogs.question().ifPresent(request -> {
            String id = nextId(document, LogicalBusinessItemKind.PENDING_QUESTION.prefix());
            LogicalBusinessPendingQuestion question = new LogicalBusinessPendingQuestion(id,
                    defaulted(request.question(), "Pregunta pendiente"), request.affects(),
                    request.priority() == null ? LogicalBusinessQuestionPriority.MEDIUM : request.priority(),
                    LogicalBusinessItemStatus.DRAFT);
            replace(viewModel, document.withPendingQuestion(question));
            viewModel.selectPendingQuestion(id);
        });
    }

    static void deleteCurrentSelection(LogicalBusinessViewModel viewModel) {
        LogicalBusinessDocument document = viewModel.currentDocument();
        LogicalBusinessSelection selection = viewModel.selection();
        if (document == null || selection.empty()) {
            return;
        }
        if (!confirmDelete(selection)) {
            return;
        }
        LogicalBusinessDocument updated = switch (selection.kind()) {
            case ITEM -> removeItem(document, selection.id());
            case ENTITY -> removeEntity(document, selection.id());
            case ATTRIBUTE -> updateEntity(document, selection.ownerId(), entity -> removeAttribute(entity, selection.id()));
            case RELATIONSHIP -> updateEntity(document, selection.ownerId(), entity -> removeRelationship(entity, selection.id()));
            case PENDING_QUESTION -> removeQuestion(document, selection.id());
            default -> document;
        };
        replace(viewModel, updated);
        viewModel.selectDocument();
    }

    private static LogicalBusinessDocument attachItemToSection(LogicalBusinessDocument document, String sectionId, String itemId) {
        if (sectionId == null || sectionId.isBlank()) {
            return document;
        }
        return document.sections().stream()
                .filter(section -> section.id().equals(sectionId))
                .findFirst()
                .map(section -> document.withUpdatedSection(section.withItemIds(appended(section.itemIds(), itemId))))
                .orElse(document);
    }

    private static LogicalBusinessDocument removeItem(LogicalBusinessDocument document, String itemId) {
        List<LogicalBusinessSection> sections = document.sections().stream()
                .map(section -> section.withItemIds(section.itemIds().stream().filter(id -> !id.equals(itemId)).toList()))
                .toList();
        List<LogicalBusinessItem> items = document.items().stream().filter(item -> !item.id().equals(itemId)).toList();
        return copy(document, sections, items, document.entityCandidates(), document.pendingQuestions());
    }

    private static LogicalBusinessDocument removeEntity(LogicalBusinessDocument document, String entityId) {
        List<LogicalBusinessEntityCandidate> entities = document.entityCandidates().stream()
                .filter(entity -> !entity.id().equals(entityId))
                .map(entity -> withoutRelationshipsTouching(entity, entityId))
                .toList();
        return copy(document, document.sections(), document.items(), entities, document.pendingQuestions());
    }

    private static LogicalBusinessDocument removeQuestion(LogicalBusinessDocument document, String questionId) {
        return copy(document, document.sections(), document.items(), document.entityCandidates(),
                document.pendingQuestions().stream().filter(question -> !question.id().equals(questionId)).toList());
    }

    private static LogicalBusinessDocument updateEntity(LogicalBusinessDocument document, String entityId,
                                                        java.util.function.Function<LogicalBusinessEntityCandidate,
                                                                LogicalBusinessEntityCandidate> updater) {
        return document.entityById(entityId).map(updater).map(document::withUpdatedEntityCandidate).orElse(document);
    }

    private static LogicalBusinessEntityCandidate removeAttribute(LogicalBusinessEntityCandidate entity, String attributeId) {
        return new LogicalBusinessEntityCandidate(entity.id(), entity.name(), entity.status(), entity.logicalJustification(),
                entity.attributes().stream().filter(attribute -> !attribute.id().equals(attributeId)).toList(),
                entity.relationships(), entity.sourceReferences(), entity.associatedRules(), entity.associatedInvariants(),
                entity.createdByUseCases(), entity.modifiedByUseCases(), entity.queriedByUseCases(), entity.modelingRisk());
    }

    private static LogicalBusinessEntityCandidate removeRelationship(LogicalBusinessEntityCandidate entity, String relationshipId) {
        return new LogicalBusinessEntityCandidate(entity.id(), entity.name(), entity.status(), entity.logicalJustification(),
                entity.attributes(), entity.relationships().stream()
                .filter(relationship -> !relationship.id().equals(relationshipId)).toList(),
                entity.sourceReferences(), entity.associatedRules(), entity.associatedInvariants(),
                entity.createdByUseCases(), entity.modifiedByUseCases(), entity.queriedByUseCases(), entity.modelingRisk());
    }

    private static LogicalBusinessEntityCandidate withoutRelationshipsTouching(LogicalBusinessEntityCandidate entity, String entityId) {
        return new LogicalBusinessEntityCandidate(entity.id(), entity.name(), entity.status(), entity.logicalJustification(),
                entity.attributes(), entity.relationships().stream()
                .filter(relationship -> !relationship.sourceEntityId().equals(entityId) && !relationship.targetEntityId().equals(entityId))
                .toList(), entity.sourceReferences(), entity.associatedRules(), entity.associatedInvariants(),
                entity.createdByUseCases(), entity.modifiedByUseCases(), entity.queriedByUseCases(), entity.modelingRisk());
    }

    private static LogicalBusinessDocument copy(LogicalBusinessDocument document, List<LogicalBusinessSection> sections,
                                                List<LogicalBusinessItem> items,
                                                List<LogicalBusinessEntityCandidate> entities,
                                                List<LogicalBusinessPendingQuestion> questions) {
        return new LogicalBusinessDocument(document.projectName(), document.version(), document.documentDate(),
                document.documentStatus(), document.mainSource(), sections, items, entities, questions,
                document.maturity(), document.notes());
    }

    private static void replace(LogicalBusinessViewModel viewModel, LogicalBusinessDocument updated) {
        viewModel.replaceDocumentFromCrud(updated, "Levantamiento lógico actualizado.");
    }

    private static String nextId(LogicalBusinessDocument document, String prefix) {
        int max = document.items().stream().map(LogicalBusinessItem::id).filter(id -> id.startsWith(prefix + "-"))
                .mapToInt(LogicalBusinessCrudOperations::suffix).max().orElse(0);
        if (LogicalBusinessItemKind.PENDING_QUESTION.prefix().equals(prefix)) {
            max = Math.max(max, document.pendingQuestions().stream().map(LogicalBusinessPendingQuestion::id)
                    .filter(id -> id.startsWith(prefix + "-")).mapToInt(LogicalBusinessCrudOperations::suffix).max().orElse(0));
        }
        if (LogicalBusinessItemKind.ENTITY.prefix().equals(prefix)) {
            max = Math.max(max, document.entityCandidates().stream().map(LogicalBusinessEntityCandidate::id)
                    .filter(id -> id.startsWith(prefix + "-")).mapToInt(LogicalBusinessCrudOperations::suffix).max().orElse(0));
        }
        return prefix + "-" + String.format("%03d", max + 1);
    }

    private static String nextEntityChildId(LogicalBusinessDocument document, String prefix) {
        int max = document.entityCandidates().stream().flatMap(entity -> {
            if (LogicalBusinessItemKind.ATTRIBUTE.prefix().equals(prefix)) {
                return entity.attributes().stream().map(LogicalBusinessAttributeCandidate::id);
            }
            return entity.relationships().stream().map(LogicalBusinessRelationshipCandidate::id);
        }).filter(id -> id.startsWith(prefix + "-")).mapToInt(LogicalBusinessCrudOperations::suffix).max().orElse(0);
        return prefix + "-" + String.format("%03d", max + 1);
    }

    private static int suffix(String id) {
        try {
            return Integer.parseInt(id.substring(id.indexOf('-') + 1));
        } catch (RuntimeException ex) {
            return 0;
        }
    }

    private static List<String> appended(List<String> values, String value) {
        List<String> updated = new ArrayList<>(values);
        if (!updated.contains(value)) {
            updated.add(value);
        }
        return updated;
    }

    private static String defaulted(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value.strip();
    }

    private static boolean confirmDelete(LogicalBusinessSelection selection) {
        return ClickMessageDialog.confirmWarning("Eliminar del levantamiento lógico",
                "Se quitará del expediente el elemento seleccionado: " + selection.id()
                        + ".\n\nEsta operación actualiza el documento activo; guarda el proyecto o exporta Markdown para persistir el cambio.",
                "Eliminar");
    }
}
