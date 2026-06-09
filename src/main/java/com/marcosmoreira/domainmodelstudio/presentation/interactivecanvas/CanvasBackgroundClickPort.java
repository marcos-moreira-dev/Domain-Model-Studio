package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

/**
 * Puerto opcional para diagramas que crean elementos mediante clic directo sobre el fondo.
 *
 * <p>El lienzo común conserva selección rectangular y limpieza de selección. Solo delega el
 * clic de fondo a este puerto cuando el adaptador especializado declara que puede consumirlo.
 * Si devuelve {@code true}, el gesto fue manejado y el canvas se refresca sin ejecutar la
 * limpieza genérica de selección.</p>
 */
public interface CanvasBackgroundClickPort {

    boolean handleBackgroundClick(double x, double y, boolean additive, int clickCount);
}
