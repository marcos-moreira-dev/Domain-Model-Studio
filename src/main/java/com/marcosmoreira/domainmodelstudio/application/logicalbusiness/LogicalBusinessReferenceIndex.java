package com.marcosmoreira.domainmodelstudio.application.logicalbusiness;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessEntityCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItem;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessPendingQuestion;
import java.util.LinkedHashSet;
import java.util.Set;

/** Índice liviano de IDs conocidos dentro de un levantamiento lógico. */
final class LogicalBusinessReferenceIndex {

    private final Set<String> knownIds;

    LogicalBusinessReferenceIndex(LogicalBusinessDocument document) {
        this.knownIds = collectKnownIds(document);
    }

    boolean known(String id) {
        return id != null && knownIds.contains(id.strip());
    }

    private Set<String> collectKnownIds(LogicalBusinessDocument document) {
        Set<String> values = new LinkedHashSet<>();
        document.items().stream().map(LogicalBusinessItem::id).forEach(values::add);
        document.pendingQuestions().stream().map(LogicalBusinessPendingQuestion::id).forEach(values::add);
        for (LogicalBusinessEntityCandidate entity : document.entityCandidates()) {
            values.add(entity.id());
            entity.attributes().forEach(attribute -> values.add(attribute.id()));
            entity.relationships().forEach(relationship -> values.add(relationship.id()));
        }
        return Set.copyOf(values);
    }
}
