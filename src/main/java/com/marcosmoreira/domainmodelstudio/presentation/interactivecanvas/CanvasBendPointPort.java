package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

/** Puerto opcional para editar puntos intermedios de conectores. */
public interface CanvasBendPointPort {

    void addBendPoint(String connectorId, double x, double y);

    void moveBendPoint(String connectorId, int index, double x, double y);

    void selectBendPoint(String connectorId, int index);

    void removeSelectedBendPoint();

    default boolean supportsBendPoints() {
        return true;
    }
}
