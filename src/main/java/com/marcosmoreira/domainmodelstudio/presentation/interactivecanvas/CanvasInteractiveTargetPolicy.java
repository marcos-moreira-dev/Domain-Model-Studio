package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import com.marcosmoreira.domainmodelstudio.presentation.diagramcanvas.ZoomableDiagramSurface;
import java.util.Objects;
import javafx.scene.Node;

/**
 * Política transversal para distinguir fondo real de elementos interactivos del lienzo.
 *
 * <p>JavaFX puede entregar como target un hijo interno de un grupo visual, no siempre el
 * nodo raíz del elemento. Por eso la política recorre ancestros hasta las capas de la
 * superficie y considera interactivo cualquier nodo con manejador, dato de usuario o
 * clases visuales del canvas antes de llegar a una capa.</p>
 */
final class CanvasInteractiveTargetPolicy {

    private final ZoomableDiagramSurface surface;

    CanvasInteractiveTargetPolicy(ZoomableDiagramSurface surface) {
        this.surface = Objects.requireNonNull(surface, "surface");
    }

    boolean isBackground(Object target) {
        if (target == surface.root() || target == surface.workspaceRoot()) {
            return true;
        }
        if (!(target instanceof Node node)) {
            return false;
        }
        return !isInteractive(node);
    }

    private boolean isInteractive(Node node) {
        Node current = node;
        while (current != null && current != surface.root()) {
            if (isSurfaceRoot(current)) {
                return false;
            }
            if (isSurfaceLayer(current)) {
                // Si el target llegó hasta una capa desde un descendiente, no es fondo:
                // es un nodo/conector/overlay que quizá no traía userData propio.
                // Esto evita que el selector de fondo consuma clics sobre labels internos.
                return current != node;
            }
            if (looksInteractive(current)) {
                return true;
            }
            current = current.getParent();
        }
        return false;
    }

    private boolean isSurfaceRoot(Node node) {
        return node == surface.workspaceRoot();
    }

    private boolean isSurfaceLayer(Node node) {
        return node == surface.layers().backgroundLayer()
                || node == surface.layers().backgroundNodeLayer()
                || node == surface.layers().connectorLayer()
                || node == surface.layers().nodeLayer()
                || node == surface.layers().overlayLayer()
                || node == surface.layers().livePreviewLayer();
    }

    private boolean looksInteractive(Node node) {
        return node.getUserData() != null
                || node.getOnMousePressed() != null
                || node.getOnMouseDragged() != null
                || node.getOnMouseReleased() != null
                || node.getStyleClass().stream().anyMatch(CanvasInteractiveTargetPolicy::interactiveStyleClass);
    }

    private static boolean interactiveStyleClass(String styleClass) {
        String normalized = Objects.toString(styleClass, "").toLowerCase(java.util.Locale.ROOT);
        return normalized.contains("node-visual")
                || normalized.contains("connector-visual")
                || normalized.contains("canvas-node")
                || normalized.contains("connector-label")
                || normalized.contains("bend-point-handle")
                || normalized.contains("message-group")
                || normalized.contains("lifeline")
                || normalized.contains("primitive-content");
    }
}
