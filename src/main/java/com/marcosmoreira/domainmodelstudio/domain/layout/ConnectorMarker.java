package com.marcosmoreira.domainmodelstudio.domain.layout;

/**
 * Marcador visual de un extremo de conector.
 *
 * <p>En Chen el marcador normalmente será NONE. En Crow's Foot puede usarse ONE, MANY,
 * OPTIONAL_ONE u OPTIONAL_MANY según la cardinalidad.</p>
 */
public enum ConnectorMarker {
    NONE,
    ONE,
    MANY,
    OPTIONAL_ONE,
    OPTIONAL_MANY
}
