package com.marcosmoreira.domainmodelstudio.application.logicalbusiness.derivation;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItem;
import java.util.List;

/** Prepara wireframes administrativos compatibles como pantallas textuales revisables. */
final class LogicalBusinessWireframeDraftWriter implements LogicalBusinessDerivationWriter {

    @Override
    public LogicalBusinessDerivationTarget target() {
        return LogicalBusinessDerivationTarget.ADMIN_WIREFRAMES;
    }

    @Override
    public LogicalBusinessDerivationDraft write(LogicalBusinessDerivationContext context) {
        List<LogicalBusinessItem> flowItems = context.primaryFlowItems();
        StringBuilder markdown = new StringBuilder();
        markdown.append(LogicalBusinessDraftText.yamlHeader(target(),
                "Wireframes compatibles — " + context.projectName(), context.domainName(), "wireframe visual revisable"));
        markdown.append("# Pantallas\n\n");
        if (flowItems.isEmpty()) {
            appendFallbackScreen(markdown);
        } else {
            for (LogicalBusinessItem item : flowItems) {
                appendScreen(markdown, item);
            }
        }
        return new LogicalBusinessDerivationDraft(target(), target().displayName(), target().fileName(),
                markdown.toString(), context.warnings());
    }

    private void appendFallbackScreen(StringBuilder markdown) {
        markdown.append("## Revisión del levantamiento\n");
        markdown.append("id: revision_levantamiento\n");
        markdown.append("tipo: escritorio\n");
        markdown.append("propósito: completar acciones o casos de uso antes de diseñar pantallas definitivas.\n\n");
        appendCommonSections(markdown, "Revisión del levantamiento");
    }

    private void appendScreen(StringBuilder markdown, LogicalBusinessItem item) {
        markdown.append("## ").append(item.title()).append('\n');
        markdown.append("id: ").append(LogicalBusinessDraftText.slug(item.title())).append('\n');
        markdown.append("tipo: escritorio\n");
        markdown.append("propósito: ").append(LogicalBusinessDraftText.itemSummary(item)).append("\n\n");
        appendCommonSections(markdown, item.title());
    }

    private void appendCommonSections(StringBuilder markdown, String focus) {
        markdown.append("### Secciones\n");
        markdown.append("- encabezado: título y acciones principales.\n");
        markdown.append("  tipo: sección\n");
        markdown.append("  comportamiento: orienta la operación de ").append(focus).append(".\n");
        markdown.append("- formulario: datos necesarios para ").append(focus).append(".\n");
        markdown.append("  tipo: formulario\n");
        markdown.append("  comportamiento: captura datos mínimos revisables.\n");
        markdown.append("- validaciones: reglas e invariantes asociadas.\n");
        markdown.append("  tipo: resumen\n");
        markdown.append("  comportamiento: advierte condiciones por validar.\n\n");
        markdown.append("### Controles\n");
        markdown.append("- botón_guardar: registra cambios del caso.\n");
        markdown.append("  tipo: botón\n");
        markdown.append("  comportamiento: confirma información revisada.\n");
        markdown.append("- botón_cancelar: vuelve sin modificar.\n");
        markdown.append("  tipo: botón\n");
        markdown.append("  comportamiento: descarta cambios no confirmados.\n\n");
    }
}
