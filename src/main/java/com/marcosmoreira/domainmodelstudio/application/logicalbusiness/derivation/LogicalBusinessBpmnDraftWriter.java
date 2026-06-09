package com.marcosmoreira.domainmodelstudio.application.logicalbusiness.derivation;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItem;

/** Prepara un BPMN básico compatible como secuencia revisable de acciones principales. */
final class LogicalBusinessBpmnDraftWriter implements LogicalBusinessDerivationWriter {

    @Override
    public LogicalBusinessDerivationTarget target() {
        return LogicalBusinessDerivationTarget.BPMN_BASIC;
    }

    @Override
    public LogicalBusinessDerivationDraft write(LogicalBusinessDerivationContext context) {
        StringBuilder markdown = new StringBuilder();
        markdown.append(LogicalBusinessDraftText.yamlHeader(target(),
                "BPMN básico compatible — " + context.projectName(), context.domainName(), "diagrama visual revisable"));
        markdown.append("# Proceso\n\nNombre: Proceso principal de ").append(context.projectName()).append('\n');
        markdown.append("Participantes: cliente, operador, sistema.\n\n# Flujo\n\n");
        markdown.append("- inicio: Se recibe la necesidad o evento inicial del negocio.\n");
        for (LogicalBusinessItem item : context.primaryFlowItems()) {
            markdown.append("- actividad: ").append(item.title()).append(".\n");
        }
        markdown.append("- fin: El proceso queda cerrado con evidencia o estado verificable.\n\n");
        markdown.append("# Excepciones\n\n- Revisar preguntas pendientes y reglas no validadas antes de usar este flujo como definitivo.\n");
        return new LogicalBusinessDerivationDraft(target(), target().displayName(), target().fileName(),
                markdown.toString(), context.warnings());
    }
}
