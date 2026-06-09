package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

/**
 * Regla transversal para que la selección manual de puntos intermedios no sea
 * pisada por la selección de propiedades sincronizada desde los paneles laterales.
 *
 * <p>La política no conoce módulos, clases UML, pantallas ni nodos C4. Solo
 * reconoce que un punto intermedio activo pertenece a la interacción directa
 * del canvas y debe conservarse hasta que el usuario seleccione otra cosa o lo
 * elimine.</p>
 */
public final class CanvasBendPointSelectionPolicy {

    private CanvasBendPointSelectionPolicy() {
    }

    public static boolean preserveManualBendPointSelection(InteractiveCanvasSelection selection) {
        return selection != null && selection.selectedBendPoint().isPresent();
    }

    public static InteractiveCanvasSelection selectBendPoint(String connectorId, int index) {
        return InteractiveCanvasSelection.empty().withBendPoint(connectorId, index);
    }
}
