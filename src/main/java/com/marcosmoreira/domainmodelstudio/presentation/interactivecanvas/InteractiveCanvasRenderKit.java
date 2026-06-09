package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import com.marcosmoreira.domainmodelstudio.presentation.drawing.DiagramDrawingFacade;
import javafx.scene.Node;

/**
 * Punto de extensión para renderers visuales específicos sin acoplar el canvas a un dominio.
 */
public interface InteractiveCanvasRenderKit {

    Node renderNode(InteractiveCanvasNode node, CanvasBounds bounds, boolean selected);

    Node renderConnector(InteractiveCanvasConnector connector, InteractiveCanvasAdapter adapter, boolean selected);

    /**
     * Variante preparada para la normalización visual progresiva.
     *
     * <p>Los render kits existentes pueden seguir implementando el contrato clásico.
     * Los nuevos render kits pueden sobreescribir esta variante para reutilizar
     * primitivas comunes sin que el canvas conozca familias concretas de diagramas.</p>
     */
    default Node renderNode(
            InteractiveCanvasNode node,
            CanvasBounds bounds,
            boolean selected,
            DiagramDrawingFacade drawingFacade
    ) {
        return renderNode(node, bounds, selected);
    }

    /** Variante con drawingFacade común para conectores. */
    default Node renderConnector(
            InteractiveCanvasConnector connector,
            InteractiveCanvasAdapter adapter,
            boolean selected,
            DiagramDrawingFacade drawingFacade
    ) {
        return renderConnector(connector, adapter, selected);
    }
}
