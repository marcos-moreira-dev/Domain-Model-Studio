package com.marcosmoreira.domainmodelstudio.presentation.toolbar;

/**
 * Política visual para evitar texto truncado en barras desplazables.
 *
 * <p>Las toolbars del shell y de cada proyecto son horizontalmente desplazables;
 * por tanto, los botones deben reservar el ancho necesario para mostrar su texto
 * completo en vez de depender de puntos suspensivos.</p>
 */
final class ToolbarButtonSizingPolicy {

    private static final double APPROX_CHAR_WIDTH = 7.4;
    private static final double TEXT_WITH_ICON_EXTRA = 48.0;
    private static final double GRAPHIC_ONLY_WIDTH = 40.0;

    private ToolbarButtonSizingPolicy() {
    }

    static double preferredWidth(String text, double declaredWidth) {
        if (text == null || text.isBlank()) {
            return Math.max(declaredWidth, GRAPHIC_ONLY_WIDTH);
        }
        double computedWidth = text.strip().length() * APPROX_CHAR_WIDTH + TEXT_WITH_ICON_EXTRA;
        return Math.max(declaredWidth, Math.ceil(computedWidth));
    }

    static double minimumWidth(String text, double declaredWidth) {
        return preferredWidth(text, declaredWidth);
    }
}
