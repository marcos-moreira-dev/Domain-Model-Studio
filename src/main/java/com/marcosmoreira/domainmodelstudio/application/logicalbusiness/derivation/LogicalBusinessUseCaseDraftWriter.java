package com.marcosmoreira.domainmodelstudio.application.logicalbusiness.derivation;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItem;
import java.util.List;

/** Prepara un UML de casos de uso compatible desde actores, casos de uso y acciones. */
final class LogicalBusinessUseCaseDraftWriter implements LogicalBusinessDerivationWriter {

    @Override
    public LogicalBusinessDerivationTarget target() {
        return LogicalBusinessDerivationTarget.UML_USE_CASE;
    }

    @Override
    public LogicalBusinessDerivationDraft write(LogicalBusinessDerivationContext context) {
        StringBuilder markdown = new StringBuilder();
        markdown.append(LogicalBusinessDraftText.yamlHeader(target(),
                "UML casos de uso compatible — " + context.projectName(), context.domainName(), "diagrama visual revisable"));
        markdown.append("# Sistema\n\nNombre: ").append(context.projectName()).append('\n');
        markdown.append("Límite: operación descrita por el levantamiento lógico.\n\n");
        appendActors(markdown, context.actors());
        appendUseCases(markdown, context.primaryFlowItems());
        appendRelations(markdown, context.actors(), context.primaryFlowItems());
        return new LogicalBusinessDerivationDraft(target(), target().displayName(), target().fileName(),
                markdown.toString(), context.warnings());
    }

    private void appendActors(StringBuilder markdown, List<LogicalBusinessItem> actors) {
        markdown.append("# Actores\n\n");
        if (actors.isEmpty()) {
            markdown.append("- Usuario del negocio\n- Administrador\n\n");
            return;
        }
        actors.forEach(actor -> markdown.append("- ").append(actor.title()).append('\n'));
        markdown.append('\n');
    }

    private void appendUseCases(StringBuilder markdown, List<LogicalBusinessItem> items) {
        markdown.append("# Casos de uso\n\n");
        items.forEach(item -> markdown.append("- ").append(item.title()).append('\n'));
        markdown.append('\n');
    }

    private void appendRelations(StringBuilder markdown, List<LogicalBusinessItem> actors, List<LogicalBusinessItem> items) {
        String actorName = actors.isEmpty() ? "Usuario del negocio" : actors.get(0).title();
        markdown.append("# Relaciones\n\n");
        items.forEach(item -> markdown.append("- ").append(actorName).append(" -> ").append(item.title()).append('\n'));
    }
}
