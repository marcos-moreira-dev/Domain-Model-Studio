package com.marcosmoreira.domainmodelstudio.application.logicalbusiness;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessAttributeCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessEntityCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessIssueSeverity;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessRelationshipCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessValidationIssue;
import java.util.ArrayList;
import java.util.List;

/** Reglas de salud para entidades, atributos y relaciones candidatas. */
final class LogicalBusinessEntityValidationPolicy {

    List<LogicalBusinessValidationIssue> validate(LogicalBusinessEntityCandidate entity, LogicalBusinessReferenceIndex index) {
        List<LogicalBusinessValidationIssue> issues = new ArrayList<>();
        if (entity.sourceReferences().isEmpty()) {
            issues.add(LogicalBusinessValidationIssue.blocking(entity.id(),
                    "La entidad candidata debe conservar al menos una fuente lógica."));
        }
        if (entity.attributes().isEmpty()) {
            issues.add(warning(entity.id(), "La entidad candidata todavía no declara atributos candidatos."));
        }
        if (entity.createdByUseCases().isEmpty()
                && entity.modifiedByUseCases().isEmpty()
                && entity.queriedByUseCases().isEmpty()) {
            issues.add(warning(entity.id(),
                    "La entidad debería indicar qué casos de uso o acciones la crean, modifican o consultan."));
        }
        entity.sourceReferences().stream()
                .filter(reference -> !index.known(reference))
                .forEach(reference -> issues.add(warning(entity.id(), "Fuente lógica no resuelta: " + reference)));
        entity.attributes().forEach(attribute -> issues.addAll(validateAttribute(attribute, index)));
        entity.relationships().forEach(relationship -> issues.addAll(validateRelationship(relationship, index)));
        return List.copyOf(issues);
    }

    private List<LogicalBusinessValidationIssue> validateAttribute(
            LogicalBusinessAttributeCandidate attribute,
            LogicalBusinessReferenceIndex index
    ) {
        List<LogicalBusinessValidationIssue> issues = new ArrayList<>();
        if (attribute.sourceReferences().isEmpty()
                && attribute.relatedRules().isEmpty()
                && attribute.relatedInvariants().isEmpty()) {
            issues.add(warning(attribute.id(),
                    "El atributo candidato necesita fuente, regla o invariante que justifique su existencia."));
        }
        if (attribute.riskIfWrong().isBlank()) {
            issues.add(warning(attribute.id(), "El atributo candidato debería indicar riesgo si se modela mal."));
        }
        unresolved(attribute.id(), attribute.sourceReferences(), index, issues, "Fuente no resuelta: ");
        unresolved(attribute.id(), attribute.relatedRules(), index, issues, "Regla relacionada no resuelta: ");
        unresolved(attribute.id(), attribute.relatedInvariants(), index, issues, "Invariante relacionada no resuelta: ");
        return List.copyOf(issues);
    }

    private List<LogicalBusinessValidationIssue> validateRelationship(
            LogicalBusinessRelationshipCandidate relationship,
            LogicalBusinessReferenceIndex index
    ) {
        List<LogicalBusinessValidationIssue> issues = new ArrayList<>();
        if (relationship.sourceReferences().isEmpty()) {
            issues.add(warning(relationship.id(),
                    "La relación candidata debería indicar regla, acción, evidencia o flujo que la justifica."));
        }
        unresolved(relationship.id(), relationship.sourceReferences(), index, issues, "Fuente no resuelta: ");
        return List.copyOf(issues);
    }

    private void unresolved(
            String targetId,
            List<String> references,
            LogicalBusinessReferenceIndex index,
            List<LogicalBusinessValidationIssue> issues,
            String prefix
    ) {
        references.stream()
                .filter(reference -> !index.known(reference))
                .forEach(reference -> issues.add(warning(targetId, prefix + reference)));
    }

    private LogicalBusinessValidationIssue warning(String targetId, String message) {
        return new LogicalBusinessValidationIssue(LogicalBusinessIssueSeverity.WARNING, targetId, message);
    }
}
