package com.marcosmoreira.domainmodelstudio.domain.layout;

/**
 * Lado de conexión de una figura.
 *
 * <p>AUTO permite que la presentación o una estrategia de layout elija el lado más conveniente.
 * Los valores explícitos permiten corregir líneas forzadas, especialmente en Crow's Foot.</p>
 */
public enum AnchorSide {
    AUTO,
    LEFT,
    RIGHT,
    TOP,
    BOTTOM,
    CENTER
}
