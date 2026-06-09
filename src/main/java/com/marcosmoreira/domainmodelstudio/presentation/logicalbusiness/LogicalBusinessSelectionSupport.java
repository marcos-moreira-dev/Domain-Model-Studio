package com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessEntityCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessRelationshipCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessSection;
import java.util.Optional;

/** Normalización y resolución de selección del levantamiento lógico. */
final class LogicalBusinessSelectionSupport {

    private LogicalBusinessSelectionSupport() {
    }

    static LogicalBusinessSelection normalize(LogicalBusinessDocument document, LogicalBusinessSelection requested) {
        LogicalBusinessSelection candidate = requested == null ? LogicalBusinessSelection.none() : requested;
        if (document == null || candidate.empty()) {
            return LogicalBusinessSelection.none();
        }
        return switch (candidate.kind()) {
            case DOCUMENT, GROUP, MATURITY -> candidate;
            case SECTION -> document.sections().stream().anyMatch(section -> section.id().equals(candidate.id()))
                    ? candidate : LogicalBusinessSelection.document();
            case ITEM -> document.itemById(candidate.id()).isPresent() ? candidate : LogicalBusinessSelection.document();
            case ENTITY -> document.entityById(candidate.id()).isPresent() ? candidate : LogicalBusinessSelection.document();
            case ATTRIBUTE -> document.entityById(candidate.ownerId()).flatMap(entity -> entity.attributeById(candidate.id()))
                    .isPresent() ? candidate : LogicalBusinessSelection.document();
            case RELATIONSHIP -> relationshipById(document, candidate.ownerId(), candidate.id()).isPresent()
                    ? candidate : LogicalBusinessSelection.document();
            case PENDING_QUESTION -> document.pendingQuestions().stream().anyMatch(question -> question.id().equals(candidate.id()))
                    ? candidate : LogicalBusinessSelection.document();
            case NONE -> LogicalBusinessSelection.none();
        };
    }

    static LogicalBusinessSelection referenceSelection(LogicalBusinessDocument document, String referenceId) {
        if (document == null || referenceId == null || referenceId.isBlank()) {
            return LogicalBusinessSelection.document();
        }
        String id = referenceId.strip();
        if (document.itemById(id).isPresent()) {
            return LogicalBusinessSelection.item(id);
        }
        if (document.entityById(id).isPresent()) {
            return LogicalBusinessSelection.entity(id);
        }
        for (LogicalBusinessEntityCandidate entity : document.entityCandidates()) {
            if (entity.attributeById(id).isPresent()) {
                return LogicalBusinessSelection.attribute(entity.id(), id);
            }
            if (relationshipById(document, entity.id(), id).isPresent()) {
                return LogicalBusinessSelection.relationship(entity.id(), id);
            }
        }
        if (document.pendingQuestions().stream().anyMatch(question -> question.id().equals(id))) {
            return LogicalBusinessSelection.pendingQuestion(id);
        }
        return LogicalBusinessSelection.document();
    }

    static Optional<LogicalBusinessSection> selectedSectionForItem(LogicalBusinessDocument document, String itemId) {
        if (document == null || itemId == null || itemId.isBlank()) {
            return Optional.empty();
        }
        return document.sections().stream().filter(section -> section.itemIds().contains(itemId)).findFirst();
    }

    private static Optional<LogicalBusinessRelationshipCandidate> relationshipById(
            LogicalBusinessDocument document,
            String entityId,
            String relationshipId
    ) {
        return document.entityById(entityId).flatMap(entity -> entity.relationships().stream()
                .filter(relationship -> relationship.id().equals(relationshipId))
                .findFirst());
    }
}
