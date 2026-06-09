package com.marcosmoreira.domainmodelstudio.domain.style;

import java.util.Objects;

/**
 * Estilo de relleno de una figura.
 */
public record FillStyle(RgbaColor color) {

    public FillStyle {
        Objects.requireNonNull(color, "El color de relleno no puede ser null");
    }

    public static FillStyle of(RgbaColor color) {
        return new FillStyle(color);
    }

    public static FillStyle white() {
        return new FillStyle(RgbaColor.rgb(255, 255, 255));
    }
}
