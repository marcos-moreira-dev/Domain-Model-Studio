package com.marcosmoreira.domainmodelstudio.domain.logicalbusiness;

import java.util.List;

/**
 * Elemento lógico detectado durante el levantamiento: regla, acción, condición,
 * flujo, reporte, riesgo, supuesto, actor, estado o evidencia.
 *
 * <p>El ID debe coincidir con el tipo para que la trazabilidad sea estable en
 * Markdown, UI y trazas internas.</p>
 */
public record LogicalBusinessItem(
        String id,
        LogicalBusinessItemKind kind,
        String title,
        LogicalBusinessItemStatus status,
        String source,
        String description,
        String humanReading,
        String content,
        List<String> referenceIds
) {
    public LogicalBusinessItem {
        id = LogicalBusinessText.require(id, "id");
        kind = kind == null ? LogicalBusinessItemKind.CONCEPT : kind;
        title = LogicalBusinessText.require(title, "title");
        status = status == null ? LogicalBusinessItemStatus.DRAFT : status;
        source = LogicalBusinessText.normalize(source);
        description = LogicalBusinessText.normalize(description);
        humanReading = LogicalBusinessText.normalize(humanReading);
        content = LogicalBusinessText.normalize(content);
        referenceIds = LogicalBusinessText.normalizedList(referenceIds);
        if (!kind.matchesId(id)) {
            throw new IllegalArgumentException("El ID " + id + " no coincide con el tipo " + kind + ".");
        }
    }

    /**
     * Crea un elemento mínimo en estado borrador.
     */
    public static LogicalBusinessItem of(String id, LogicalBusinessItemKind kind, String title) {
        return new LogicalBusinessItem(id, kind, title, LogicalBusinessItemStatus.DRAFT, "", "", "", "", List.of());
    }

    /**
     * Cambia solo el estado de validación del elemento.
     */
    public LogicalBusinessItem withStatus(LogicalBusinessItemStatus updatedStatus) {
        return new LogicalBusinessItem(id, kind, title, updatedStatus, source, description, humanReading, content, referenceIds);
    }

    /**
     * Actualiza lectura humana y contenido lógico sin cambiar identidad ni referencias.
     */
    public LogicalBusinessItem withDetails(String updatedDescription, String updatedHumanReading, String updatedContent) {
        return new LogicalBusinessItem(id, kind, title, status, source, updatedDescription, updatedHumanReading,
                updatedContent, referenceIds);
    }

    /**
     * Devuelve una nueva versión con los campos editables usados por la UI documental.
     */
    public LogicalBusinessItem withEditableDetails(
            String updatedTitle,
            LogicalBusinessItemStatus updatedStatus,
            String updatedSource,
            String updatedDescription,
            String updatedHumanReading,
            String updatedContent,
            List<String> updatedReferenceIds
    ) {
        return new LogicalBusinessItem(id, kind, updatedTitle, updatedStatus, updatedSource, updatedDescription,
                updatedHumanReading, updatedContent, updatedReferenceIds);
    }
}
