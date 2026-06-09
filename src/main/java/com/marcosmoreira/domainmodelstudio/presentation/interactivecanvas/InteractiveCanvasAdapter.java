package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

/**
 * Puente completo entre un editor especializado y el lienzo interactivo común.
 *
 * <p>La interfaz conserva compatibilidad con los adaptadores existentes, pero queda separada en
 * puertos pequeños. Las piezas transversales deben depender del puerto mínimo que necesiten:
 * lectura, selección, layout, puntos intermedios o marca de cambios.</p>
 *
 * <p>Para implementar un nuevo tipo visual, este contrato es la puerta de entrada: el dominio no
 * se entrega al canvas directamente; se proyecta como nodos, conectores, selección y comandos
 * persistibles en el ViewModel correspondiente.</p>
 
 *
 * <p><strong>Ejemplo pedagógico:</strong> el Grafo lógico proyecta {@code ACC-001} como
 * nodo visual, pero el canvas nunca recibe el agregado de dominio completo. Solo recibe
 * una vista interactiva y devuelve comandos de selección o layout al ViewModel.</p>
 */
public interface InteractiveCanvasAdapter extends
        CanvasReadPort,
        CanvasSelectionPort,
        CanvasLayoutCommandPort,
        CanvasBendPointPort,
        CanvasDirtyPort {
}
