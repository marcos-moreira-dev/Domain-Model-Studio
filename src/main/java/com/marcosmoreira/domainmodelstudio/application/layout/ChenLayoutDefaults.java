package com.marcosmoreira.domainmodelstudio.application.layout;

/**
 * Constantes del layout Chen inicial.
 *
 * <p>Esta clase evita valores mágicos dispersos en el generador. El layout inicial no
 * pretende ser perfecto: debe producir una primera lámina legible y corregible por el
 * usuario cuando se implementen movimiento y edición fina.</p>
 */
public final class ChenLayoutDefaults {

    public static final double ENTITY_WIDTH = 150.0;
    public static final double ENTITY_HEIGHT = 58.0;
    public static final double ATTRIBUTE_WIDTH = 132.0;
    public static final double ATTRIBUTE_HEIGHT = 48.0;
    public static final double RELATIONSHIP_WIDTH = 142.0;
    public static final double RELATIONSHIP_HEIGHT = 74.0;

    public static final double START_X = 160.0;
    public static final double START_Y = 160.0;
    public static final double ENTITY_COLUMN_GAP = 520.0;
    public static final double ENTITY_ROW_GAP = 380.0;
    public static final int ENTITIES_PER_ROW = 3;

    public static final double ATTRIBUTE_RADIUS_X = 185.0;
    public static final double ATTRIBUTE_RADIUS_Y = 125.0;

    private ChenLayoutDefaults() {
        // Constantes puras.
    }
}
