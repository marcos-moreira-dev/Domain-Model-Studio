package com.marcosmoreira.domainmodelstudio.application.logicalbusiness.derivation;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItem;
import java.util.List;

/** Prepara un flujo de pantallas compatible desde casos de uso o acciones principales. */
final class LogicalBusinessScreenFlowDraftWriter implements LogicalBusinessDerivationWriter {

    @Override
    public LogicalBusinessDerivationTarget target() {
        return LogicalBusinessDerivationTarget.SCREEN_FLOW;
    }

    @Override
    public LogicalBusinessDerivationDraft write(LogicalBusinessDerivationContext context) {
        List<LogicalBusinessItem> flowItems = context.primaryFlowItems();
        StringBuilder markdown = new StringBuilder();
        markdown.append(LogicalBusinessDraftText.yamlHeader(target(),
                "Flujo de pantallas compatible — " + context.projectName(), context.domainName(), "diagrama visual revisable"));
        markdown.append("# Pantallas\n\n## Inicio\nid: inicio\npropósito: acceso a operaciones principales.\n\n");
        if (flowItems.isEmpty()) {
            appendFallbackScreen(markdown);
        } else {
            flowItems.forEach(item -> appendScreen(markdown, item));
        }
        markdown.append("# Navegación\n\n");
        if (flowItems.isEmpty()) {
            markdown.append("- inicio -> revisar_levantamiento: abrir revisión del levantamiento lógico.\n");
        } else {
            flowItems.forEach(item -> markdown.append("- inicio -> ").append(LogicalBusinessDraftText.slug(item.title()))
                    .append(": abrir ").append(item.title()).append(".\n"));
        }
        return new LogicalBusinessDerivationDraft(target(), target().displayName(), target().fileName(),
                markdown.toString(), context.warnings());
    }

    private void appendFallbackScreen(StringBuilder markdown) {
        markdown.append("## Revisar levantamiento\n");
        markdown.append("id: revisar_levantamiento\n");
        markdown.append("propósito: completar casos de uso o acciones antes de generar flujo definitivo.\n\n");
    }

    private void appendScreen(StringBuilder markdown, LogicalBusinessItem item) {
        markdown.append("## ").append(item.title()).append('\n');
        markdown.append("id: ").append(LogicalBusinessDraftText.slug(item.title())).append('\n');
        markdown.append("propósito: ").append(LogicalBusinessDraftText.itemSummary(item)).append("\n\n");
    }
}
