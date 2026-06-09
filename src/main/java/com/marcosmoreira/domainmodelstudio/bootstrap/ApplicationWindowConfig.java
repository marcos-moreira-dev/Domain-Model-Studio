package com.marcosmoreira.domainmodelstudio.bootstrap;

/** Configuración inicial de la ventana principal. */
public record ApplicationWindowConfig(
        double defaultWidth,
        double defaultHeight,
        double minimumWidth,
        double minimumHeight
) {

    public static ApplicationWindowConfig desktopDefault() {
        return new ApplicationWindowConfig(1280, 760, 1024, 640);
    }
}
