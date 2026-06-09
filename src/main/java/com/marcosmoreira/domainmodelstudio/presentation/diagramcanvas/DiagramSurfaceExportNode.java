package com.marcosmoreira.domainmodelstudio.presentation.diagramcanvas;

import java.util.Objects;
import javafx.scene.Node;

/** Nodo raíz que puede entregarse a exportadores visuales sin exponer el viewport. */
public record DiagramSurfaceExportNode(Node node, double workspaceWidth, double workspaceHeight) {

    public DiagramSurfaceExportNode {
        Objects.requireNonNull(node, "node");
        if (workspaceWidth <= 0.0 || workspaceHeight <= 0.0) {
            throw new IllegalArgumentException("El tamaño exportable debe ser positivo.");
        }
    }
}
