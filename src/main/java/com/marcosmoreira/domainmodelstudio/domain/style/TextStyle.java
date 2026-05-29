package com.marcosmoreira.domainmodelstudio.domain.style;

import java.util.Objects;

/**
 * Estilo de texto de un elemento del diagrama.
 */
public final class TextStyle {

    private final String fontFamily;
    private final double fontSize;
    private final RgbaColor color;
    private final FontWeight weight;
    private final FontPosture posture;

    public TextStyle(String fontFamily, double fontSize, RgbaColor color, FontWeight weight, FontPosture posture) {
        String normalizedFamily = Objects.requireNonNull(fontFamily, "La familia tipográfica no puede ser null").trim();
        if (normalizedFamily.isEmpty()) {
            throw new IllegalArgumentException("La familia tipográfica no puede estar vacía");
        }
        if (!Double.isFinite(fontSize) || fontSize <= 0) {
            throw new IllegalArgumentException("El tamaño de fuente debe ser finito y mayor que cero");
        }
        this.fontFamily = normalizedFamily;
        this.fontSize = fontSize;
        this.color = Objects.requireNonNull(color, "El color del texto no puede ser null");
        this.weight = weight == null ? FontWeight.NORMAL : weight;
        this.posture = posture == null ? FontPosture.REGULAR : posture;
    }

    public static TextStyle defaultDesktop() {
        return new TextStyle("Segoe UI", 12.0, RgbaColor.rgb(35, 35, 35), FontWeight.NORMAL, FontPosture.REGULAR);
    }

    public String fontFamily() {
        return fontFamily;
    }

    public double fontSize() {
        return fontSize;
    }

    public RgbaColor color() {
        return color;
    }

    public FontWeight weight() {
        return weight;
    }

    public FontPosture posture() {
        return posture;
    }

    public TextStyle withFontFamily(String updatedFontFamily) {
        return new TextStyle(updatedFontFamily, fontSize, color, weight, posture);
    }

    public TextStyle withFontSize(double updatedFontSize) {
        return new TextStyle(fontFamily, updatedFontSize, color, weight, posture);
    }

    public TextStyle withColor(RgbaColor updatedColor) {
        return new TextStyle(fontFamily, fontSize, updatedColor, weight, posture);
    }
}
