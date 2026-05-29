package com.marcosmoreira.domainmodelstudio.domain.style;

import java.util.Objects;

/**
 * Estilo completo de un elemento visual.
 *
 * <p>Este objeto no conoce si el elemento se dibuja como rectángulo, óvalo o rombo.
 * La notación decide la geometría; el estilo decide colores, borde y texto.</p>
 */
public final class ElementStyle {

    private final FillStyle fill;
    private final StrokeStyle stroke;
    private final TextStyle text;

    public ElementStyle(FillStyle fill, StrokeStyle stroke, TextStyle text) {
        this.fill = Objects.requireNonNull(fill, "El relleno no puede ser null");
        this.stroke = Objects.requireNonNull(stroke, "El borde no puede ser null");
        this.text = Objects.requireNonNull(text, "El estilo de texto no puede ser null");
    }

    public static ElementStyle defaultElement() {
        return new ElementStyle(
                FillStyle.of(RgbaColor.rgb(246, 239, 214)),
                StrokeStyle.defaultBorder(),
                TextStyle.defaultDesktop()
        );
    }

    public FillStyle fill() {
        return fill;
    }

    public StrokeStyle stroke() {
        return stroke;
    }

    public TextStyle text() {
        return text;
    }

    public ElementStyle withFill(FillStyle updatedFill) {
        return new ElementStyle(updatedFill, stroke, text);
    }

    public ElementStyle withStroke(StrokeStyle updatedStroke) {
        return new ElementStyle(fill, updatedStroke, text);
    }

    public ElementStyle withText(TextStyle updatedText) {
        return new ElementStyle(fill, stroke, updatedText);
    }
}
