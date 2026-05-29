package com.marcosmoreira.domainmodelstudio.application.layout;

/**
 * Parámetros de organización visual para un tipo de diagrama.
 *
 * <p>La clase mantiene los números de layout fuera de los casos de uso para que
 * el ruteo y la separación de nodos puedan evolucionar sin duplicar constantes.</p>
 */
public record AutoLayoutProfile(
        double startX,
        double startY,
        double horizontalGap,
        double verticalGap,
        int preferredColumns,
        double nodeMargin,
        double routeMargin
) {

    public AutoLayoutProfile {
        if (!Double.isFinite(startX) || !Double.isFinite(startY)) {
            throw new IllegalArgumentException("El inicio del layout debe ser finito");
        }
        if (!Double.isFinite(horizontalGap) || horizontalGap <= 0.0) {
            throw new IllegalArgumentException("La separación horizontal debe ser positiva");
        }
        if (!Double.isFinite(verticalGap) || verticalGap <= 0.0) {
            throw new IllegalArgumentException("La separación vertical debe ser positiva");
        }
        if (preferredColumns <= 0) {
            throw new IllegalArgumentException("La cantidad de columnas debe ser positiva");
        }
        if (!Double.isFinite(nodeMargin) || nodeMargin < 0.0) {
            throw new IllegalArgumentException("El margen de nodos no puede ser negativo");
        }
        if (!Double.isFinite(routeMargin) || routeMargin < 0.0) {
            throw new IllegalArgumentException("El margen de rutas no puede ser negativo");
        }
    }

    public static AutoLayoutProfile chen(int elementCount) {
        return new AutoLayoutProfile(
                ChenLayoutDefaults.START_X,
                ChenLayoutDefaults.START_Y,
                ChenLayoutDefaults.ENTITY_COLUMN_GAP,
                ChenLayoutDefaults.ENTITY_ROW_GAP,
                adaptiveColumns(elementCount, ChenLayoutDefaults.ENTITIES_PER_ROW, 5),
                48.0,
                54.0
        );
    }

    public static AutoLayoutProfile crowsFoot(int elementCount) {
        return new AutoLayoutProfile(
                120.0,
                120.0,
                370.0,
                270.0,
                adaptiveColumns(elementCount, 3, 5),
                52.0,
                60.0
        );
    }

    private static int adaptiveColumns(int elementCount, int minimum, int maximum) {
        if (elementCount <= 0) {
            return minimum;
        }
        int squareRoot = (int) Math.ceil(Math.sqrt(elementCount));
        return Math.max(minimum, Math.min(maximum, squareRoot));
    }
}
