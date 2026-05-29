package com.marcosmoreira.domainmodelstudio.presentation.drawing;

import javafx.scene.Node;

/**
 * Contrato común para familias de símbolos de diagrama.
 *
 * <p>Un {@code DiagramShapeKit} solo crea figuras visuales primitivas o
 * compuestas. La interacción pertenece al canvas; la traducción del dominio
 * pertenece al adapter; y el armado final del nodo pertenece al render kit.</p>
 */
public interface DiagramShapeKit {

    boolean supports(DiagramSymbol symbol);

    Node createShape(DiagramSymbol symbol, DiagramShapeStyle style);
}
