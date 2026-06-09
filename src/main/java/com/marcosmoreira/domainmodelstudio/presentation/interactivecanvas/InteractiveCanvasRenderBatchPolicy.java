package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

/**
 * Política pequeña para decidir cuándo el lienzo común debe montar nodos JavaFX por lotes.
 *
 * <p>La métrica es deliberadamente simple: cada clase, módulo, conector y overlay visual
 * consume tiempo en el pulso JavaFX. Para vistas grandes conviene repartir el trabajo en
 * varios pulsos en vez de construir todo en una única pasada.</p>
 */
public record InteractiveCanvasRenderBatchPolicy(
        int immediateElementLimit,
        int connectorBatchSize,
        int nodeBatchSize
) {

    private static final int DEFAULT_IMMEDIATE_ELEMENT_LIMIT = 220;
    private static final int DEFAULT_CONNECTOR_BATCH_SIZE = 48;
    private static final int DEFAULT_NODE_BATCH_SIZE = 36;

    public InteractiveCanvasRenderBatchPolicy {
        requirePositive(immediateElementLimit, "immediateElementLimit");
        requirePositive(connectorBatchSize, "connectorBatchSize");
        requirePositive(nodeBatchSize, "nodeBatchSize");
    }

    public static InteractiveCanvasRenderBatchPolicy defaults() {
        return new InteractiveCanvasRenderBatchPolicy(
                DEFAULT_IMMEDIATE_ELEMENT_LIMIT,
                DEFAULT_CONNECTOR_BATCH_SIZE,
                DEFAULT_NODE_BATCH_SIZE
        );
    }

    public boolean requiresBatchedRender(int connectorCount, int nodeCount) {
        int safeConnectors = Math.max(0, connectorCount);
        int safeNodes = Math.max(0, nodeCount);
        return safeConnectors + safeNodes > immediateElementLimit;
    }

    private static void requirePositive(int value, String name) {
        if (value <= 0) {
            throw new IllegalArgumentException(name + " debe ser positivo.");
        }
    }
}
