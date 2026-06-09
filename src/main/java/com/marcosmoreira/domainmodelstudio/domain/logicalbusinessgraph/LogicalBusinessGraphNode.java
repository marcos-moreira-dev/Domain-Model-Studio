package com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph;

import java.util.List;

/**
 * Nodo semántico del grafo lógico del negocio.
 *
 * <p>El código identifica el tipo mediante prefijo: {@code MF}, {@code FL},
 * {@code CU}, {@code ACC}, {@code RN}, {@code PRE}, {@code INV}, {@code POST},
 * {@code ENT}, {@code EST}, {@code REP}, {@code RISK} o {@code PEND}. El
 * constructor rechaza combinaciones donde el prefijo no coincide con el tipo.</p>
 *
 * @param code código estable usado en Markdown, UI y persistencia
 * @param kind tipo semántico del nodo
 * @param title título humano visible
 * @param description explicación o lectura lógica
 * @param status estado de madurez del nodo
 * @param sourceReferenceIds referencias al levantamiento lógico u otra evidencia
 */
public record LogicalBusinessGraphNode(
        String code,
        LogicalBusinessGraphNodeKind kind,
        String title,
        String description,
        LogicalBusinessGraphNodeStatus status,
        List<String> sourceReferenceIds
) {
    public LogicalBusinessGraphNode {
        code = LogicalBusinessGraphText.require(code, "code").toUpperCase();
        kind = kind == null ? LogicalBusinessGraphNodeKind.fromCode(code)
                .orElse(LogicalBusinessGraphNodeKind.USE_CASE) : kind;
        title = LogicalBusinessGraphText.require(title, "title");
        description = LogicalBusinessGraphText.normalize(description);
        status = status == null ? LogicalBusinessGraphNodeStatus.DRAFT : status;
        sourceReferenceIds = LogicalBusinessGraphText.normalizedList(sourceReferenceIds);
        if (!kind.matchesCode(code)) {
            throw new IllegalArgumentException("El código " + code + " no coincide con el tipo " + kind.displayName() + ".");
        }
    }

    /**
     * Crea un nodo mínimo en estado borrador.
     */
    public static LogicalBusinessGraphNode of(String code, LogicalBusinessGraphNodeKind kind, String title) {
        return new LogicalBusinessGraphNode(code, kind, title, "", LogicalBusinessGraphNodeStatus.DRAFT, List.of());
    }

    /**
     * Etiqueta breve para listas, paneles de estructura y reportes de validación.
     */
    public String compactLabel() {
        return kind.prefix() + " " + code + " — " + title;
    }

    /**
     * Devuelve una nueva versión del nodo con sus detalles editables actualizados.
     */
    public LogicalBusinessGraphNode withDetails(String updatedTitle, String updatedDescription,
                                                LogicalBusinessGraphNodeStatus updatedStatus,
                                                List<String> updatedSourceReferenceIds) {
        return new LogicalBusinessGraphNode(code, kind, updatedTitle, updatedDescription,
                updatedStatus, updatedSourceReferenceIds);
    }
}
