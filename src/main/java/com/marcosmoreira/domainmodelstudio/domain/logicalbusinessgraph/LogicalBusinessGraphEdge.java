package com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph;

/**
 * Relación dirigida y tipada entre dos nodos del grafo lógico del negocio.
 *
 * <p>La relación guarda códigos de origen y destino, no referencias a objetos.
 * El documento raíz valida que esos códigos existan y que el ID de relación sea
 * único.</p>
 *
 * @param id identificador persistible de la relación
 * @param sourceCode código del nodo origen
 * @param relationKind semántica de la relación
 * @param targetCode código del nodo destino
 * @param description explicación humana de por qué existe la relación
 */
public record LogicalBusinessGraphEdge(
        String id,
        String sourceCode,
        LogicalBusinessGraphRelationKind relationKind,
        String targetCode,
        String description
) {
    public LogicalBusinessGraphEdge {
        id = LogicalBusinessGraphText.require(id, "id");
        sourceCode = LogicalBusinessGraphText.require(sourceCode, "sourceCode").toUpperCase();
        relationKind = relationKind == null ? LogicalBusinessGraphRelationKind.DEPENDS_ON : relationKind;
        targetCode = LogicalBusinessGraphText.require(targetCode, "targetCode").toUpperCase();
        description = LogicalBusinessGraphText.normalize(description);
    }

    /**
     * Crea una relación sin descripción para plantillas o tests mínimos.
     */
    public static LogicalBusinessGraphEdge of(String id, String sourceCode,
                                              LogicalBusinessGraphRelationKind relationKind, String targetCode) {
        return new LogicalBusinessGraphEdge(id, sourceCode, relationKind, targetCode, "");
    }

    /**
     * Indica si la relación apunta al mismo nodo; el validador lo trata como bloqueo.
     */
    public boolean loop() {
        return sourceCode.equals(targetCode);
    }

    /**
     * Etiqueta visible usada por el canvas y los exportadores.
     */
    public String label() {
        return relationKind.code();
    }
}
