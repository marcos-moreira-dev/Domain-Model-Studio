package com.marcosmoreira.domainmodelstudio.domain.layout;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import java.util.Objects;

/**
 * Posición, tamaño y orden visual de un elemento del diagrama.
 *
 * <p>No describe color, texto ni semántica. Solo responde a tres preguntas de layout:
 * ¿dónde se ve este elemento?, ¿con qué tamaño se ve? y ¿en qué orden relativo se dibuja
 * frente a otros nodos de la misma capa visual?</p>
 */
public final class NodeLayout {

    private final DiagramElementId elementId;
    private final DiagramPoint position;
    private final DiagramSize size;
    private final boolean visible;
    private final boolean locked;
    private final int zOrder;

    public NodeLayout(
            DiagramElementId elementId,
            DiagramPoint position,
            DiagramSize size,
            boolean visible,
            boolean locked
    ) {
        this(elementId, position, size, visible, locked, 0);
    }

    public NodeLayout(
            DiagramElementId elementId,
            DiagramPoint position,
            DiagramSize size,
            boolean visible,
            boolean locked,
            int zOrder
    ) {
        this.elementId = Objects.requireNonNull(elementId, "El ID del nodo no puede ser null");
        this.position = Objects.requireNonNull(position, "La posición no puede ser null");
        this.size = Objects.requireNonNull(size, "El tamaño no puede ser null");
        this.visible = visible;
        this.locked = locked;
        this.zOrder = zOrder;
    }

    public static NodeLayout at(String elementId, double x, double y, double width, double height) {
        return new NodeLayout(
                DiagramElementId.of(elementId),
                DiagramPoint.of(x, y),
                DiagramSize.of(width, height),
                true,
                false
        );
    }

    public DiagramElementId elementId() {
        return elementId;
    }

    public DiagramPoint position() {
        return position;
    }

    public DiagramSize size() {
        return size;
    }

    public boolean visible() {
        return visible;
    }

    public boolean locked() {
        return locked;
    }

    /**
     * Orden relativo dentro de la capa visual del nodo.
     *
     * <p>Un valor mayor se dibuja después y queda por encima de nodos de la misma capa.
     * Este valor se deriva del orden de nodos persistido en {@link DiagramLayout}; no agrega
     * semántica de dominio ni cambia la identidad del elemento.</p>
     */
    public int zOrder() {
        return zOrder;
    }

    public double x() {
        return position.x();
    }

    public double y() {
        return position.y();
    }

    public double width() {
        return size.width();
    }

    public double height() {
        return size.height();
    }

    public NodeLayout movedTo(double x, double y) {
        return new NodeLayout(elementId, DiagramPoint.of(x, y), size, visible, locked, zOrder);
    }

    public NodeLayout translatedBy(double deltaX, double deltaY) {
        return new NodeLayout(elementId, position.translatedBy(deltaX, deltaY), size, visible, locked, zOrder);
    }

    public NodeLayout resizedTo(double width, double height) {
        return new NodeLayout(elementId, position, DiagramSize.of(width, height), visible, locked, zOrder);
    }

    public NodeLayout withVisibility(boolean updatedVisible) {
        return new NodeLayout(elementId, position, size, updatedVisible, locked, zOrder);
    }

    public NodeLayout withLocked(boolean updatedLocked) {
        return new NodeLayout(elementId, position, size, visible, updatedLocked, zOrder);
    }

    public NodeLayout withZOrder(int updatedZOrder) {
        return new NodeLayout(elementId, position, size, visible, locked, updatedZOrder);
    }
}
