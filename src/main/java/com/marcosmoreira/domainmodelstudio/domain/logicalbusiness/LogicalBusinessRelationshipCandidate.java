package com.marcosmoreira.domainmodelstudio.domain.logicalbusiness;

import java.util.List;

/** Relación candidata entre entidades del levantamiento lógico; todavía no representa una llave física. */
public record LogicalBusinessRelationshipCandidate(
        String id,
        String sourceEntityId,
        String targetEntityId,
        String name,
        String cardinalityHint,
        String justification,
        List<String> sourceReferences
) {
    public LogicalBusinessRelationshipCandidate {
        id = LogicalBusinessText.require(id, "id");
        sourceEntityId = LogicalBusinessText.require(sourceEntityId, "sourceEntityId");
        targetEntityId = LogicalBusinessText.require(targetEntityId, "targetEntityId");
        name = LogicalBusinessText.require(name, "name");
        cardinalityHint = LogicalBusinessText.normalize(cardinalityHint);
        justification = LogicalBusinessText.require(justification, "justification");
        sourceReferences = LogicalBusinessText.normalizedList(sourceReferences);
        if (!LogicalBusinessItemKind.RELATIONSHIP.matchesId(id)) {
            throw new IllegalArgumentException("La relación candidata debe usar ID REL-XXX: " + id);
        }
    }

    public LogicalBusinessRelationshipCandidate withEditableDetails(
            String updatedSourceEntityId,
            String updatedTargetEntityId,
            String updatedName,
            String updatedCardinalityHint,
            String updatedJustification,
            List<String> updatedSourceReferences
    ) {
        return new LogicalBusinessRelationshipCandidate(id, updatedSourceEntityId, updatedTargetEntityId,
                updatedName, updatedCardinalityHint, updatedJustification, updatedSourceReferences);
    }
}

