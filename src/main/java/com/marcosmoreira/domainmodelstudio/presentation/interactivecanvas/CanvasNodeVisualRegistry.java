package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

/**
 * Registro visual de nodos renderizados en el canvas interactivo.
 *
 * <p>Centraliza dos responsabilidades que antes crecían dentro de la superficie:
 * feedback local de selección y previsualización JavaFX del arrastre. No persiste
 * posiciones; la persistencia sigue viviendo en el adapter mediante los puertos de
 * layout.</p>
 */
public final class CanvasNodeVisualRegistry {

    private static final String SELECTED_CLASS = "canvas-node-view-selected";
    private static final String PRESSED_CLASS = "interactive-canvas-node-pressed-selection";
    private static final String ACTIVE_CLASS = "interactive-canvas-node-active-selection";
    private static final String SELECTED_TARGET_CLASS = "canvas-node-view-selected-target";
    private static final String ACTIVE_TARGET_CLASS = "interactive-canvas-node-active-selection-target";

    private final InteractiveCanvasAdapter adapter;
    private final Map<String, Node> renderedNodeById = new HashMap<>();

    public CanvasNodeVisualRegistry(InteractiveCanvasAdapter adapter) {
        this.adapter = Objects.requireNonNull(adapter, "El adaptador no puede ser null");
    }

    public void clear() {
        renderedNodeById.clear();
    }

    public void register(String nodeId, Node rendered) {
        renderedNodeById.put(nodeId, rendered);
    }

    public void markSelectedNodeLocally(String selectedNodeId, boolean additive) {
        for (Map.Entry<String, Node> entry : renderedNodeById.entrySet()) {
            boolean selected = entry.getKey().equals(selectedNodeId);
            Node visual = entry.getValue();
            if (!additive && !selected) {
                setLocalSelectionStyle(visual, false);
            }
            if (selected) {
                setLocalSelectionStyle(visual, true);
            }
        }
    }

    /**
     * Marca el nodo que acaba de recibir el clic como foco activo de interacción.
     *
     * <p>La selección semántica puede existir desde antes; esta marca local indica
     * qué tarjeta está siendo presionada o preparada para arrastre sin reconstruir
     * el canvas completo. La marca se mantiene mientras siga seleccionado y se
     * elimina cuando otro gesto limpia la selección local.</p>
     */
    public void markActiveNodeLocally(String activeNodeId, boolean additive) {
        for (Map.Entry<String, Node> entry : renderedNodeById.entrySet()) {
            Node visual = entry.getValue();
            boolean active = entry.getKey().equals(activeNodeId);
            if (!additive && !active) {
                clearActiveSelectionStyle(visual);
            }
            if (active) {
                addStyleClassIfMissing(visual, ACTIVE_CLASS);
                addSelectionTargetStyle(visual, ACTIVE_TARGET_CLASS);
            }
        }
    }

    /**
     * Aplica una selección local inmediata mientras el canvas confirma el gesto.
     *
     * <p>UML Clases renderiza la selección fuerte dentro de la tarjeta especializada,
     * pero durante un clic/arrastre no conviene reconstruir todo el lienzo antes del
     * mouse release. Esta marca genérica evita la sensación de que el clic no hizo nada
     * y luego el refresh final sincroniza el estilo semántico completo.</p>
     */
    public void setLocalSelectionStyle(Node visual, boolean selected) {
        if (visual == null) {
            return;
        }
        if (selected) {
            addStyleClassIfMissing(visual, SELECTED_CLASS);
            addStyleClassIfMissing(visual, PRESSED_CLASS);
            addSelectionTargetStyle(visual, SELECTED_TARGET_CLASS);
            return;
        }
        clearLocalSelectionStyle(visual);
    }

    public void clearDragPreviewTranslations(String draggedNodeId, Node rendered) {
        for (String nodeId : previewNodeIds(draggedNodeId)) {
            Node visual = renderedNodeById.getOrDefault(nodeId, rendered);
            visual.setTranslateX(0.0);
            visual.setTranslateY(0.0);
        }
    }

    public void previewNodeDrag(String draggedNodeId, Node rendered, Point2D[] anchor, Point2D canvasPoint) {
        if (anchor[0] == null) {
            anchor[0] = canvasPoint;
            return;
        }
        double deltaX = canvasPoint.getX() - anchor[0].getX();
        double deltaY = canvasPoint.getY() - anchor[0].getY();
        for (String nodeId : previewNodeIds(draggedNodeId)) {
            Node visual = renderedNodeById.getOrDefault(nodeId, rendered);
            visual.setTranslateX(visual.getTranslateX() + deltaX);
            visual.setTranslateY(visual.getTranslateY() + deltaY);
        }
        anchor[0] = canvasPoint;
    }

    public void resetDragPreviewTranslations(String draggedNodeId, Node rendered) {
        for (String nodeId : previewNodeIds(draggedNodeId)) {
            Node visual = renderedNodeById.getOrDefault(nodeId, rendered);
            visual.setTranslateX(0.0);
            visual.setTranslateY(0.0);
        }
    }

    public Set<String> previewNodeIds(String draggedNodeId) {
        Set<String> selected = adapter.selection().selectedNodeIds();
        if (adapter instanceof CanvasDragPreviewPort dragPreviewPort) {
            return dragPreviewPort.previewNodeIdsForDraggedNode(draggedNodeId, selected);
        }
        return selected.isEmpty() ? Set.of(draggedNodeId) : selected;
    }

    private static void clearLocalSelectionStyle(Node visual) {
        visual.getStyleClass().remove(SELECTED_CLASS);
        visual.getStyleClass().remove(PRESSED_CLASS);
        visual.getStyleClass().remove(SELECTED_TARGET_CLASS);
        clearActiveSelectionStyle(visual);
        if (visual instanceof Parent parent) {
            parent.getChildrenUnmodifiable().forEach(CanvasNodeVisualRegistry::clearLocalSelectionStyle);
        }
    }

    private static void clearActiveSelectionStyle(Node visual) {
        visual.getStyleClass().remove(ACTIVE_CLASS);
        visual.getStyleClass().remove(ACTIVE_TARGET_CLASS);
        if (visual instanceof Parent parent) {
            parent.getChildrenUnmodifiable().forEach(CanvasNodeVisualRegistry::clearActiveSelectionStyle);
        }
    }

    private static void addSelectionTargetStyle(Node visual, String targetClass) {
        if (visual instanceof Shape && !(visual instanceof Text)) {
            addStyleClassIfMissing(visual, targetClass);
        }
        if (visual instanceof Parent parent) {
            parent.getChildrenUnmodifiable().forEach(child -> addSelectionTargetStyle(child, targetClass));
        }
    }

    private static void addStyleClassIfMissing(Node visual, String styleClass) {
        if (!visual.getStyleClass().contains(styleClass)) {
            visual.getStyleClass().add(styleClass);
        }
    }
}
