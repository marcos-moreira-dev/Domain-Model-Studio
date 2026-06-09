package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.scene.Node;

/**
 * Divide nodos JavaFX renderizados entre fondo semántico y nodos operables.
 *
 * <p>La política transversal de capas necesita que zonas, límites, pantallas o módulos
 * de fondo se monten detrás de conectores, mientras las tarjetas operables quedan por
 * encima de las relaciones. Esta clase concentra esa partición para no duplicarla entre
 * canvas runtime, render por lotes y exportación PNG.</p>
 */
record CanvasRenderedNodeLayers(
        List<Node> backgroundNodes,
        List<Node> foregroundNodes
) {

    CanvasRenderedNodeLayers {
        backgroundNodes = List.copyOf(backgroundNodes == null ? List.of() : backgroundNodes);
        foregroundNodes = List.copyOf(foregroundNodes == null ? List.of() : foregroundNodes);
    }

    static CanvasRenderedNodeLayers from(
            List<InteractiveCanvasNode> nodes,
            InteractiveCanvasModel model,
            CanvasLayeringPolicy layeringPolicy,
            CanvasNodeLayerRenderer renderer
    ) {
        Objects.requireNonNull(model, "model");
        Objects.requireNonNull(layeringPolicy, "layeringPolicy");
        Objects.requireNonNull(renderer, "renderer");
        List<Node> background = new ArrayList<>();
        List<Node> foreground = new ArrayList<>();
        for (InteractiveCanvasNode node : nodes == null ? List.<InteractiveCanvasNode>of() : nodes) {
            model.layoutForNode(node.id()).ifPresent(layout -> addRenderedNode(
                    background,
                    foreground,
                    layeringPolicy.visualLayerFor(node),
                    renderer.render(node, layout)));
        }
        return new CanvasRenderedNodeLayers(background, foreground);
    }

    private static void addRenderedNode(
            List<Node> background,
            List<Node> foreground,
            CanvasVisualLayer layer,
            Node rendered
    ) {
        if (rendered == null) {
            return;
        }
        if (layer == CanvasVisualLayer.CONTAINER) {
            background.add(rendered);
            return;
        }
        foreground.add(rendered);
    }
}

@FunctionalInterface
interface CanvasNodeLayerRenderer {
    Node render(InteractiveCanvasNode node, NodeLayout layout);
}
