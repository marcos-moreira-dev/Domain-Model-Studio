package com.marcosmoreira.domainmodelstudio.domain.style;

import java.util.Objects;

/**
 * Estilo de borde o línea.
 */
public record StrokeStyle(RgbaColor color, double width, StrokePattern pattern) {

    public StrokeStyle {
        Objects.requireNonNull(color, "El color de borde/línea no puede ser null");
        if (!Double.isFinite(width) || width <= 0) {
            throw new IllegalArgumentException("El grosor debe ser finito y mayor que cero");
        }
        pattern = pattern == null ? StrokePattern.SOLID : pattern;
    }

    public static StrokeStyle of(RgbaColor color, double width) {
        return new StrokeStyle(color, width, StrokePattern.SOLID);
    }

    public static StrokeStyle defaultBorder() {
        return new StrokeStyle(RgbaColor.rgb(80, 80, 80), 1.0, StrokePattern.SOLID);
    }
}
