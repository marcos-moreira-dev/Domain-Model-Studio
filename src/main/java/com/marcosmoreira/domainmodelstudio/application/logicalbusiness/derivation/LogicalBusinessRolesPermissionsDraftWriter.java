package com.marcosmoreira.domainmodelstudio.application.logicalbusiness.derivation;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItem;
import java.util.List;

/** Prepara una matriz compatible de roles y permisos desde actores y casos de uso. */
final class LogicalBusinessRolesPermissionsDraftWriter implements LogicalBusinessDerivationWriter {

    @Override
    public LogicalBusinessDerivationTarget target() {
        return LogicalBusinessDerivationTarget.ROLES_PERMISSIONS;
    }

    @Override
    public LogicalBusinessDerivationDraft write(LogicalBusinessDerivationContext context) {
        List<LogicalBusinessItem> actors = context.actors();
        List<LogicalBusinessItem> flowItems = context.primaryFlowItems();
        StringBuilder markdown = new StringBuilder();
        markdown.append(LogicalBusinessDraftText.yamlHeader(target(),
                "Roles y permisos compatible — " + context.projectName(), context.domainName(), "matriz visual revisable"));
        appendRoles(markdown, actors);
        appendPermissions(markdown, flowItems);
        appendAssignments(markdown, actors, flowItems);
        return new LogicalBusinessDerivationDraft(target(), target().displayName(), target().fileName(),
                markdown.toString(), context.warnings());
    }

    private void appendRoles(StringBuilder markdown, List<LogicalBusinessItem> actors) {
        markdown.append("# Roles\n\n");
        if (actors.isEmpty()) {
            markdown.append("## Operador\n");
            markdown.append("id: operador\n");
            markdown.append("propósito: ejecuta operaciones diarias del negocio sustentadas por el levantamiento.\n\n");
            return;
        }
        for (LogicalBusinessItem actor : actors) {
            markdown.append("## ").append(actor.title()).append('\n');
            markdown.append("id: ").append(LogicalBusinessDraftText.slug(actor.title())).append('\n');
            markdown.append("propósito: ").append(LogicalBusinessDraftText.itemSummary(actor)).append("\n\n");
        }
    }

    private void appendPermissions(StringBuilder markdown, List<LogicalBusinessItem> items) {
        markdown.append("# Permisos\n\n");
        if (items.isEmpty()) {
            markdown.append("- revisar_levantamiento: Revisar y completar levantamiento lógico.\n\n");
            return;
        }
        for (LogicalBusinessItem item : items) {
            markdown.append("- ").append(LogicalBusinessDraftText.slug(item.title()))
                    .append(": ").append(item.title()).append(".\n");
        }
        markdown.append('\n');
    }

    private void appendAssignments(StringBuilder markdown, List<LogicalBusinessItem> actors, List<LogicalBusinessItem> items) {
        String roleId = actors.isEmpty() ? "operador" : LogicalBusinessDraftText.slug(actors.getFirst().title());
        markdown.append("# Asignaciones\n\n| Rol | Permiso | Decisión | Alcance | Observación |\n|---|---|---|---|---|\n");
        if (items.isEmpty()) {
            markdown.append("| ").append(roleId)
                    .append(" | revisar_levantamiento | Permitido | revisión | completar con el cliente |\n");
            return;
        }
        for (LogicalBusinessItem item : items) {
            markdown.append("| ").append(roleId).append(" | ").append(LogicalBusinessDraftText.slug(item.title()))
                    .append(" | Permitido | operación | revisar con cliente |\n");
        }
    }
}
