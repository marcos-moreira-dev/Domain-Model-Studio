package com.marcosmoreira.domainmodelstudio.domain.screenflow;

/** Tipo de navegación entre pantallas. */
public enum ScreenTransitionKind {
    NAVIGATES("Navega"),
    OPENS_DIALOG("Abre diálogo"),
    SUBMITS("Envía"),
    CANCELS("Cancela"),
    RETURNS("Regresa"),
    REDIRECTS("Redirige");

    private final String displayName;

    ScreenTransitionKind(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}
