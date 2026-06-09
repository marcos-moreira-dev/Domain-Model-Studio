package com.marcosmoreira.domainmodelstudio.application.logicalbusiness.derivation;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessEntityCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItem;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
/** Prepara un grafo libre compatible para revisar relaciones entre reglas, acciones, entidades y dudas. */
final class LogicalBusinessFreeGraphDraftWriter implements LogicalBusinessDerivationWriter {
    @Override
    public LogicalBusinessDerivationTarget target() {
        return LogicalBusinessDerivationTarget.FREE_GRAPH;
    }
    @Override
    public LogicalBusinessDerivationDraft write(LogicalBusinessDerivationContext context) {
        StringBuilder markdown = new StringBuilder();
        Set<String> emittedNodeIds = new LinkedHashSet<>();
        Set<String> referencedNodeIds = collectReferencedNodeIds(context);
        markdown.append(LogicalBusinessDraftText.yamlHeader(target(),
                "Grafo libre compatible — " + context.projectName(), context.domainName(), "diagrama visual revisable"));
        markdown.append("# Nodos\n\n");
        appendItemNodes(markdown, context.primaryFlowItems(), emittedNodeIds);
        appendItemNodes(markdown, context.rules(), emittedNodeIds);
        appendItemNodes(markdown, context.invariants(), emittedNodeIds);
        appendEntityNodes(markdown, context.entities(), emittedNodeIds);
        appendReferenceNodes(markdown, referencedNodeIds, emittedNodeIds);
        if (emittedNodeIds.isEmpty()) {
            appendFallbackNode(markdown, emittedNodeIds);
        }
        markdown.append("# Relaciones\n\n");
        appendItemReferences(markdown, context.primaryFlowItems(), emittedNodeIds);
        appendItemReferences(markdown, context.rules(), emittedNodeIds);
        appendEntityReferences(markdown, context.entities(), emittedNodeIds);
        markdown.append("\n# Observaciones\n\nBorrador para revisar dependencias lógicas antes de generar diagramas definitivos.\n");
        return new LogicalBusinessDerivationDraft(target(), target().displayName(), target().fileName(),
                markdown.toString(), context.warnings());
    }
    private Set<String> collectReferencedNodeIds(LogicalBusinessDerivationContext context) {
        Set<String> references = new LinkedHashSet<>();
        collectItemReferences(context.primaryFlowItems(), references);
        collectItemReferences(context.rules(), references);
        for (LogicalBusinessEntityCandidate entity : context.entities()) {
            entity.sourceReferences().stream()
                    .filter(reference -> reference != null && !reference.isBlank())
                    .map(LogicalBusinessDraftText::slug)
                    .forEach(references::add);
        }
        return references;
    }
    private void collectItemReferences(List<LogicalBusinessItem> items, Set<String> references) {
        for (LogicalBusinessItem item : items) {
            item.referenceIds().stream()
                    .filter(reference -> reference != null && !reference.isBlank())
                    .map(LogicalBusinessDraftText::slug)
                    .forEach(references::add);
        }
    }
    private void appendItemNodes(StringBuilder markdown, List<LogicalBusinessItem> items, Set<String> emittedNodeIds) {
        for (LogicalBusinessItem item : items) {
            String nodeId = LogicalBusinessDraftText.itemNodeId(item);
            if (!emittedNodeIds.add(nodeId)) {
                continue;
            }
            markdown.append("## ").append(LogicalBusinessDraftText.itemTitle(item)).append('\n');
            markdown.append("id: ").append(nodeId).append('\n');
            markdown.append("contenido: ").append(LogicalBusinessDraftText.itemSummary(item)).append("\n\n");
        }
    }
    private void appendEntityNodes(
            StringBuilder markdown,
            List<LogicalBusinessEntityCandidate> entities,
            Set<String> emittedNodeIds
    ) {
        for (LogicalBusinessEntityCandidate entity : entities) {
            String nodeId = LogicalBusinessDraftText.slug(entity.id());
            if (!emittedNodeIds.add(nodeId)) {
                continue;
            }
            markdown.append("## ").append(entity.id()).append(" — ").append(entity.name()).append('\n');
            markdown.append("id: ").append(nodeId).append('\n');
            markdown.append("contenido: ").append(entity.logicalJustification()).append("\n\n");
        }
    }
    private void appendReferenceNodes(StringBuilder markdown, Set<String> referencedNodeIds, Set<String> emittedNodeIds) {
        for (String nodeId : referencedNodeIds) {
            if (!emittedNodeIds.add(nodeId)) {
                continue;
            }
            markdown.append("## Referencia ").append(nodeId.toUpperCase().replace('_', '-')).append('\n');
            markdown.append("id: ").append(nodeId).append('\n');
            markdown.append("contenido: Referencia lógica detectada en el levantamiento; revisar origen y completitud.\n\n");
        }
    }
    private void appendFallbackNode(StringBuilder markdown, Set<String> emittedNodeIds) {
        String nodeId = "levantamiento_por_revisar";
        emittedNodeIds.add(nodeId);
        markdown.append("## Levantamiento por revisar\n");
        markdown.append("id: ").append(nodeId).append('\n');
        markdown.append("contenido: No hay reglas, acciones, invariantes ni entidades suficientes; completar el expediente lógico.\n\n");
    }

    private void appendItemReferences(StringBuilder markdown, List<LogicalBusinessItem> items, Set<String> emittedNodeIds) {
        for (LogicalBusinessItem item : items) {
            String sourceId = LogicalBusinessDraftText.itemNodeId(item);
            if (!emittedNodeIds.contains(sourceId)) {
                continue;
            }
            for (String reference : item.referenceIds()) {
                String targetId = LogicalBusinessDraftText.slug(reference);
                if (emittedNodeIds.contains(targetId)) {
                    markdown.append("- ").append(sourceId).append(" -> ").append(targetId).append(": referencia\n");
                }
            }
        }
    }

    private void appendEntityReferences(
            StringBuilder markdown,
            List<LogicalBusinessEntityCandidate> entities,
            Set<String> emittedNodeIds
    ) {
        for (LogicalBusinessEntityCandidate entity : entities) {
            String entityId = LogicalBusinessDraftText.slug(entity.id());
            if (!emittedNodeIds.contains(entityId)) {
                continue;
            }
            for (String reference : entity.sourceReferences()) {
                String targetId = LogicalBusinessDraftText.slug(reference);
                if (emittedNodeIds.contains(targetId)) {
                    markdown.append("- ").append(entityId).append(" -> ").append(targetId).append(": sustentada por\n");
                }
            }
        }
    }
}
