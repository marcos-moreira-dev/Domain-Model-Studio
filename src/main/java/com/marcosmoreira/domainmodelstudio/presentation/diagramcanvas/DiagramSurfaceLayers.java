package com.marcosmoreira.domainmodelstudio.presentation.diagramcanvas;

import java.util.Collection;
import java.util.Objects;
import javafx.scene.Group;
import javafx.scene.Node;

/**
 * Capas canónicas de una superficie de diagrama.
 *
 * <p>Los editores pueden montar nodos en capas claras sin conocer el ScrollPane,
 * el grupo de zoom o los cálculos de viewport.</p>
 */
public record DiagramSurfaceLayers(
        Group backgroundLayer,
        Group backgroundNodeLayer,
        Group connectorLayer,
        Group nodeLayer,
        Group overlayLayer,
        Group livePreviewLayer
) {

    public DiagramSurfaceLayers {
        Objects.requireNonNull(backgroundLayer, "backgroundLayer");
        Objects.requireNonNull(backgroundNodeLayer, "backgroundNodeLayer");
        Objects.requireNonNull(connectorLayer, "connectorLayer");
        Objects.requireNonNull(nodeLayer, "nodeLayer");
        Objects.requireNonNull(overlayLayer, "overlayLayer");
        Objects.requireNonNull(livePreviewLayer, "livePreviewLayer");
    }

    public void clearDiagramContent() {
        backgroundNodeLayer.getChildren().clear();
        connectorLayer.getChildren().clear();
        nodeLayer.getChildren().clear();
        overlayLayer.getChildren().clear();
        livePreviewLayer.getChildren().clear();
    }

    public void setDiagramContent(
            Collection<? extends Node> connectors,
            Collection<? extends Node> nodes,
            Collection<? extends Node> overlays
    ) {
        setDiagramContent(java.util.List.of(), connectors, nodes, overlays);
    }

    public void setDiagramContent(
            Collection<? extends Node> backgroundNodes,
            Collection<? extends Node> connectors,
            Collection<? extends Node> nodes,
            Collection<? extends Node> overlays
    ) {
        backgroundNodeLayer.getChildren().setAll(safe(backgroundNodes));
        connectorLayer.getChildren().setAll(safe(connectors));
        nodeLayer.getChildren().setAll(safe(nodes));
        overlayLayer.getChildren().setAll(safe(overlays));
        livePreviewLayer.getChildren().clear();
    }

    private static Collection<? extends Node> safe(Collection<? extends Node> nodes) {
        return nodes == null ? java.util.List.of() : nodes;
    }
}
