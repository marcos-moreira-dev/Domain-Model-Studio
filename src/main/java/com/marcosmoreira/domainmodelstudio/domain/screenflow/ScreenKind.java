package com.marcosmoreira.domainmodelstudio.domain.screenflow;

/** Tipo funcional de pantalla administrativa. */
public enum ScreenKind {
    LOGIN("Login"),
    DASHBOARD("Panel"),
    LIST("Listado"),
    FORM("Formulario"),
    DETAIL("Detalle"),
    REPORT("Reporte"),
    SETTINGS("Configuración"),
    OTHER("Otra");

    private final String displayName;

    ScreenKind(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}
