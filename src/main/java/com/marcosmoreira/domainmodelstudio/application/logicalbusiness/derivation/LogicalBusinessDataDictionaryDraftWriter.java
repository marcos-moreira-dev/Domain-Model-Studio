package com.marcosmoreira.domainmodelstudio.application.logicalbusiness.derivation;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessAttributeCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessEntityCandidate;

/** Prepara un diccionario de datos compatible desde entidades y atributos candidatos. */
final class LogicalBusinessDataDictionaryDraftWriter implements LogicalBusinessDerivationWriter {

    @Override
    public LogicalBusinessDerivationTarget target() {
        return LogicalBusinessDerivationTarget.DATA_DICTIONARY;
    }

    @Override
    public LogicalBusinessDerivationDraft write(LogicalBusinessDerivationContext context) {
        StringBuilder markdown = new StringBuilder();
        markdown.append(LogicalBusinessDraftText.yamlHeader(target(),
                "Diccionario de datos compatible — " + context.projectName(), context.domainName(), "documento Markdown/PDF revisable"));
        markdown.append("# Diccionario de datos\n\n");
        if (context.entities().isEmpty()) {
            appendFallbackEntity(markdown);
        }
        for (LogicalBusinessEntityCandidate entity : context.entities()) {
            markdown.append("## ").append(entity.name()).append('\n');
            markdown.append("Propósito: ").append(entity.logicalJustification()).append('\n');
            markdown.append("Responsable del dato: pendiente de validar.\n\n");
            markdown.append("| Campo | Tipo esperado | Obligatorio | Regla | Observación |\n");
            markdown.append("|---|---|---:|---|---|\n");
            markdown.append("| id | identificador | sí | único | Identificador interno sugerido. |\n");
            for (LogicalBusinessAttributeCandidate attribute : entity.attributes()) {
                appendAttributeRow(markdown, attribute);
            }
            markdown.append('\n');
        }
        return new LogicalBusinessDerivationDraft(target(), target().displayName(), target().fileName(),
                markdown.toString(), context.warnings());
    }

    private void appendFallbackEntity(StringBuilder markdown) {
        markdown.append("## Elemento del negocio por definir\n");
        markdown.append("Propósito: Completar entidades candidatas antes de usar el diccionario como definitivo.\n");
        markdown.append("Responsable del dato: pendiente de validar.\n\n");
        markdown.append("| Campo | Tipo esperado | Obligatorio | Regla | Observación |\n");
        markdown.append("|---|---|---:|---|---|\n");
        markdown.append("| id | identificador | sí | único | Identificador interno sugerido. |\n\n");
    }

    private void appendAttributeRow(StringBuilder markdown, LogicalBusinessAttributeCandidate attribute) {
        markdown.append("| ").append(LogicalBusinessDraftText.escapeTable(attribute.name()))
                .append(" | ").append(LogicalBusinessDraftText.escapeTable(attribute.tentativeType()))
                .append(" | pendiente | ").append(LogicalBusinessDraftText.escapeTable(
                        LogicalBusinessDraftText.commaList(attribute.relatedRules())))
                .append(" | ").append(LogicalBusinessDraftText.escapeTable(attribute.reason()))
                .append(" |\n");
    }
}
