package com.marcosmoreira.domainmodelstudio.infrastructure.markdown.logicalbusiness;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessAttributeCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessEntityCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItemKind;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessRelationshipCandidate;
import java.util.LinkedHashSet;
import java.util.Set;

final class LogicalBusinessMarkdownEntityWriter {

    private final LogicalBusinessMarkdownItemWriter fieldWriter = new LogicalBusinessMarkdownItemWriter();

    boolean isEntityCatalogKind(LogicalBusinessItemKind kind) {
        return kind == LogicalBusinessItemKind.ENTITY
                || kind == LogicalBusinessItemKind.ATTRIBUTE
                || kind == LogicalBusinessItemKind.RELATIONSHIP;
    }

    void writeEntityCatalog(
            StringBuilder markdown,
            LogicalBusinessDocument document,
            Set<String> writtenItemIds
    ) {
        if (document.entityCandidates().isEmpty()) {
            return;
        }
        markdown.append("## 14. Entidades candidatas")
                .append(System.lineSeparator()).append(System.lineSeparator());
        writeEntityCatalogBody(markdown, document, writtenItemIds);
    }

    void writeEntityCatalogBody(
            StringBuilder markdown,
            LogicalBusinessDocument document,
            Set<String> writtenItemIds
    ) {
        if (document.entityCandidates().isEmpty()) {
            return;
        }
        writeEntities(markdown, document, writtenItemIds);
        writeAttributes(markdown, document, writtenItemIds);
        writeRelationships(markdown, document, writtenItemIds);
    }

    private void writeEntities(
            StringBuilder markdown,
            LogicalBusinessDocument document,
            Set<String> writtenItemIds
    ) {
        for (LogicalBusinessEntityCandidate entity : document.entityCandidates()) {
            markdown.append("### ").append(entity.id()).append(" — ").append(entity.name())
                    .append(System.lineSeparator());
            fieldWriter.writeScalarField(markdown, "Estado", entity.status().name().toLowerCase());
            fieldWriter.writeScalarField(markdown, "Justificación lógica", entity.logicalJustification());
            fieldWriter.writeListField(markdown, "Fuente lógica", entity.sourceReferences());
            fieldWriter.writeListField(markdown, "Reglas asociadas", entity.associatedRules());
            fieldWriter.writeListField(markdown, "Invariantes asociadas", entity.associatedInvariants());
            fieldWriter.writeListField(markdown, "Acciones que la crean", entity.createdByUseCases());
            fieldWriter.writeListField(markdown, "Acciones que la modifican", entity.modifiedByUseCases());
            fieldWriter.writeListField(markdown, "Acciones que la consultan", entity.queriedByUseCases());
            fieldWriter.writeScalarField(markdown, "Riesgo si se modela mal", entity.modelingRisk());
            markdown.append(System.lineSeparator());
            writtenItemIds.add(entity.id());
        }
    }

    private void writeAttributes(
            StringBuilder markdown,
            LogicalBusinessDocument document,
            Set<String> writtenItemIds
    ) {
        if (document.entityCandidates().stream().flatMap(entity -> entity.attributes().stream()).findAny().isEmpty()) {
            return;
        }
        markdown.append("### Atributos candidatos").append(System.lineSeparator()).append(System.lineSeparator());
        for (LogicalBusinessEntityCandidate entity : document.entityCandidates()) {
            for (LogicalBusinessAttributeCandidate attribute : entity.attributes()) {
                writeAttribute(markdown, entity, attribute);
                writtenItemIds.add(attribute.id());
            }
        }
    }

    private void writeAttribute(
            StringBuilder markdown,
            LogicalBusinessEntityCandidate entity,
            LogicalBusinessAttributeCandidate attribute
    ) {
        markdown.append("### ").append(attribute.id()).append(" — ").append(entity.name())
                .append('.').append(attribute.name()).append(System.lineSeparator());
        fieldWriter.writeScalarField(markdown, "Pertenece a", attribute.entityId());
        fieldWriter.writeScalarField(markdown, "Tipo tentativo", attribute.tentativeType());
        fieldWriter.writeScalarField(markdown, "Razón operativa", attribute.reason());
        fieldWriter.writeScalarField(markdown, "¿Es calculado?", attribute.calculated() ? "sí" : "no");
        fieldWriter.writeScalarField(markdown, "Fórmula o lectura de cálculo", attribute.formula());
        fieldWriter.writeScalarField(markdown, "Riesgo si se modela mal", attribute.riskIfWrong());
        fieldWriter.writeListField(markdown, "Fuente lógica", attribute.sourceReferences());
        fieldWriter.writeListField(markdown, "Reglas asociadas", attribute.relatedRules());
        fieldWriter.writeListField(markdown, "Invariantes asociadas", attribute.relatedInvariants());
        markdown.append(System.lineSeparator());
    }

    private void writeRelationships(
            StringBuilder markdown,
            LogicalBusinessDocument document,
            Set<String> writtenItemIds
    ) {
        Set<String> relationshipIds = new LinkedHashSet<>();
        StringBuilder relationships = new StringBuilder();
        for (LogicalBusinessEntityCandidate entity : document.entityCandidates()) {
            for (LogicalBusinessRelationshipCandidate relationship : entity.relationships()) {
                if (relationshipIds.add(relationship.id())) {
                    writeRelationship(relationships, relationship);
                    writtenItemIds.add(relationship.id());
                }
            }
        }
        if (!relationshipIds.isEmpty()) {
            markdown.append("### Relaciones candidatas").append(System.lineSeparator()).append(System.lineSeparator());
            markdown.append(relationships);
        }
    }

    private void writeRelationship(StringBuilder markdown, LogicalBusinessRelationshipCandidate relationship) {
        markdown.append("### ").append(relationship.id()).append(" — ").append(relationship.name())
                .append(System.lineSeparator());
        fieldWriter.writeScalarField(markdown, "Entidad origen", relationship.sourceEntityId());
        fieldWriter.writeScalarField(markdown, "Entidad destino", relationship.targetEntityId());
        fieldWriter.writeScalarField(markdown, "Cardinalidad tentativa", relationship.cardinalityHint());
        fieldWriter.writeScalarField(markdown, "Justificación lógica", relationship.justification());
        fieldWriter.writeListField(markdown, "Fuente lógica", relationship.sourceReferences());
        markdown.append(System.lineSeparator());
    }
}
