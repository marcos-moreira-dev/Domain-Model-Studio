package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

/**
 * Puerto de comandos de layout del lienzo común.
 *
 * <p>Las coordenadas visuales editables pertenecen al layout del diagrama, no a nodos
 * JavaFX sueltos. Los controladores transversales deben mover elementos por este puerto.</p>
 */
public interface CanvasLayoutCommandPort {

    void moveNode(String elementId, double x, double y);

    void moveSelectedNodesBy(double deltaX, double deltaY);

    default void moveSelectedConnectorBendPointsBy(double deltaX, double deltaY) {
        // Implementación opcional para familias que quieran arrastrar relaciones completas
        // junto con una selección múltiple de nodos y conectores.
    }
}
