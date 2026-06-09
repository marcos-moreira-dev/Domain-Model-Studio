package com.marcosmoreira.domainmodelstudio.domain.notation;

/**
 * Notaciones visuales soportadas o planificadas por el modelo.
 *
 * <p>Chen y Crow's Foot pueden compartir parte del modelo semántico, pero no deben
 * tratarse como simples temas visuales. Cada notación puede necesitar reglas de layout
 * y representación distintas.</p>
 */
public enum NotationType {
    CHEN("Chen"),
    CROWS_FOOT("Pata de gallo");

    private final String displayName;

    NotationType(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}
