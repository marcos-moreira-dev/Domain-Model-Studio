package com.marcosmoreira.domainmodelstudio.domain.logicalbusiness;

import java.util.List;

/** Atributo candidato justificado por reglas, acciones, evidencia, reportes o trazas internas. */
public record LogicalBusinessAttributeCandidate(
        String id,
        String entityId,
        String name,
        String reason,
        String tentativeType,
        boolean calculated,
        String formula,
        String riskIfWrong,
        List<String> sourceReferences,
        List<String> relatedRules,
        List<String> relatedInvariants
) {
    public LogicalBusinessAttributeCandidate {
        id = LogicalBusinessText.require(id, "id");
        entityId = LogicalBusinessText.require(entityId, "entityId");
        name = LogicalBusinessText.require(name, "name");
        reason = LogicalBusinessText.require(reason, "reason");
        tentativeType = LogicalBusinessText.normalize(tentativeType);
        formula = LogicalBusinessText.normalize(formula);
        riskIfWrong = LogicalBusinessText.normalize(riskIfWrong);
        sourceReferences = LogicalBusinessText.normalizedList(sourceReferences);
        relatedRules = LogicalBusinessText.normalizedList(relatedRules);
        relatedInvariants = LogicalBusinessText.normalizedList(relatedInvariants);
        if (!LogicalBusinessItemKind.ATTRIBUTE.matchesId(id)) {
            throw new IllegalArgumentException("El atributo candidato debe usar ID ATR-XXX: " + id);
        }
        if (calculated && formula.isBlank()) {
            throw new IllegalArgumentException("Un atributo calculado debe conservar su fórmula o criterio de cálculo.");
        }
    }

    public LogicalBusinessAttributeCandidate withEditableDetails(
            String updatedName,
            String updatedReason,
            String updatedTentativeType,
            boolean updatedCalculated,
            String updatedFormula,
            String updatedRiskIfWrong,
            List<String> updatedSourceReferences,
            List<String> updatedRelatedRules,
            List<String> updatedRelatedInvariants
    ) {
        return new LogicalBusinessAttributeCandidate(id, entityId, updatedName, updatedReason, updatedTentativeType,
                updatedCalculated, updatedFormula, updatedRiskIfWrong, updatedSourceReferences,
                updatedRelatedRules, updatedRelatedInvariants);
    }
}

