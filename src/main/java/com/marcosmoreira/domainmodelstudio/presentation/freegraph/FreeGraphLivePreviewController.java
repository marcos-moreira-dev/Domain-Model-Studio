package com.marcosmoreira.domainmodelstudio.presentation.freegraph;

import java.util.Set;
import java.util.function.BiFunction;

/** Mantiene el estado temporal de previsualización de arrastre del Grafo libre. */
final class FreeGraphLivePreviewController {
    private boolean active;
    private double lastX;
    private double lastY;
    private Set<String> nodeIds = Set.of();

    Set<String> movableNodeIds(Set<String> selectedNodeIds, BiFunction<String, Set<String>, Set<String>> previewResolver) {
        if (active && !nodeIds.isEmpty()) {
            return nodeIds;
        }
        return previewResolver.apply("", selectedNodeIds);
    }

    void begin(String elementId, double canvasX, double canvasY,
               Set<String> selectedNodeIds,
               BiFunction<String, Set<String>, Set<String>> previewResolver) {
        active = true;
        lastX = canvasX;
        lastY = canvasY;
        nodeIds = previewResolver.apply(elementId, selectedNodeIds);
    }

    void update(String elementId, double canvasX, double canvasY,
                Set<String> selectedNodeIds,
                BiFunction<String, Set<String>, Set<String>> previewResolver,
                PreviewDeltaConsumer deltaConsumer) {
        if (!active) {
            begin(elementId, canvasX, canvasY, selectedNodeIds, previewResolver);
            return;
        }
        double deltaX = canvasX - lastX;
        double deltaY = canvasY - lastY;
        if (Math.abs(deltaX) >= 0.01 || Math.abs(deltaY) >= 0.01) {
            deltaConsumer.accept(deltaX, deltaY, false);
        }
        lastX = canvasX;
        lastY = canvasY;
    }

    void clear() {
        active = false;
        nodeIds = Set.of();
    }

    @FunctionalInterface
    interface PreviewDeltaConsumer {
        void accept(double deltaX, double deltaY, boolean announce);
    }
}
