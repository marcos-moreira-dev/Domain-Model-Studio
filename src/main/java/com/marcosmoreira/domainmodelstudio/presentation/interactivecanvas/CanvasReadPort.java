package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de lectura del lienzo común.
 *
 * <p>Expone únicamente el snapshot semántico-visual que puede consumir una superficie
 * transversal: tipo de diagrama, nodos, conectores y layouts. No conoce ViewModels,
 * paneles, toolbars ni familias concretas de diagrama.</p>
 */
public interface CanvasReadPort {

    DiagramTypeId diagramTypeId();

    List<InteractiveCanvasNode> nodes();

    List<InteractiveCanvasConnector> connectors();

    Optional<NodeLayout> layoutForNode(String elementId);

    Optional<ConnectorLayout> layoutForConnector(String connectorId);

    /**
     * Calcula los límites visibles del contenido a partir de los layouts de los nodos.
     *
     * <p>Los adaptadores especializados pueden sobreescribir este método cuando necesiten
     * márgenes mínimos, padding de exportación o reglas propias de familia. El método por
     * defecto mantiene el contrato transversal sin obligar al exportador común a conocer
     * módulos, UML, wireframes, C4 ni comportamiento.</p>
     */
    default CanvasBounds contentBounds() {
        CanvasBounds bounds = null;
        for (InteractiveCanvasNode node : nodes()) {
            if (!node.visible()) {
                continue;
            }
            Optional<NodeLayout> layout = layoutForNode(node.id()).filter(NodeLayout::visible);
            if (layout.isEmpty()) {
                continue;
            }
            CanvasBounds current = CanvasBounds.from(layout.get());
            bounds = CanvasBounds.union(bounds, current);
        }
        return bounds == null ? CanvasBounds.of(0.0, 0.0, 880.0, 600.0) : bounds;
    }
}
