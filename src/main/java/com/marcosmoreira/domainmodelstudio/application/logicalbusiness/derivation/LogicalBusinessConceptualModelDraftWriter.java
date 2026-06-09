package com.marcosmoreira.domainmodelstudio.application.logicalbusiness.derivation;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessAttributeCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessEntityCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessRelationshipCandidate;
import java.util.Map;
import java.util.stream.Collectors;

/** Prepara un modelo conceptual compatible desde entidades, atributos y relaciones candidatas. */
final class LogicalBusinessConceptualModelDraftWriter implements LogicalBusinessDerivationWriter {

    @Override
    public LogicalBusinessDerivationTarget target() {
        return LogicalBusinessDerivationTarget.CONCEPTUAL_MODEL;
    }

    @Override
    public LogicalBusinessDerivationDraft write(LogicalBusinessDerivationContext context) {
        Map<String, String> conceptualIds = context.entities().stream()
                .collect(Collectors.toMap(LogicalBusinessEntityCandidate::id, entity -> LogicalBusinessDraftText.slug(entity.name())));
        StringBuilder markdown = new StringBuilder();
        markdown.append(LogicalBusinessDraftText.yamlHeader(target(),
                "Modelo conceptual compatible — " + context.projectName(), context.domainName(), "diagrama visual revisable"));
        markdown.append("# Entidades\n\n");
        if (context.entities().isEmpty()) {
            appendFallbackEntity(markdown);
        } else {
            for (LogicalBusinessEntityCandidate entity : context.entities()) {
                appendEntity(markdown, entity, conceptualIds.get(entity.id()));
            }
        }
        markdown.append("# Relaciones\n\n");
        context.entities().forEach(entity -> entity.relationships()
                .forEach(relationship -> appendRelationship(markdown, relationship, conceptualIds)));
        return new LogicalBusinessDerivationDraft(target(), target().displayName(), target().fileName(),
                markdown.toString(), context.warnings());
    }

    private void appendEntity(StringBuilder markdown, LogicalBusinessEntityCandidate entity, String conceptualId) {
        markdown.append("## ").append(entity.name()).append('\n');
        markdown.append("id: ").append(conceptualId).append('\n');
        markdown.append("module: levantamiento-logico\n");
        markdown.append("description: ").append(entity.logicalJustification()).append("\n\n");
        markdown.append("- pk id\n");
        for (LogicalBusinessAttributeCandidate attribute : entity.attributes()) {
            markdown.append("- ").append(LogicalBusinessDraftText.slug(attribute.name()))
                    .append(" # ").append(attribute.reason()).append('\n');
        }
        markdown.append('\n');
    }

    private void appendFallbackEntity(StringBuilder markdown) {
        markdown.append("## Elemento del negocio por definir\n");
        markdown.append("id: elemento_negocio_pendiente\n");
        markdown.append("module: levantamiento-logico\n");
        markdown.append("description: Completar entidades candidatas antes de usar el modelo conceptual como definitivo.\n\n");
        markdown.append("- pk id\n\n");
    }

    private void appendRelationship(
            StringBuilder markdown,
            LogicalBusinessRelationshipCandidate relationship,
            Map<String, String> conceptualIds
    ) {
        markdown.append("## ").append(relationship.name()).append('\n');
        markdown.append("id: ").append(LogicalBusinessDraftText.slug(relationship.id())).append('\n');
        markdown.append("from: ").append(conceptualIds.getOrDefault(relationship.sourceEntityId(), relationship.sourceEntityId())).append('\n');
        markdown.append("to: ").append(conceptualIds.getOrDefault(relationship.targetEntityId(), relationship.targetEntityId())).append('\n');
        markdown.append("from_cardinality: 1\n");
        markdown.append("to_cardinality: ").append(relationship.cardinalityHint().isBlank() ? "0..M" : relationship.cardinalityHint()).append('\n');
        markdown.append("description: ").append(relationship.justification()).append("\n\n");
    }
}
