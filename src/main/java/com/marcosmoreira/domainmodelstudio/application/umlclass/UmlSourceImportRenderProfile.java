package com.marcosmoreira.domainmodelstudio.application.umlclass;

/**
 * Perfil de detalle visual para diagramas UML generados desde código fuente.
 *
 * <p>El modelo puede conservar toda la información parseada, pero el lienzo no debe
 * dibujar todo el código cuando el proyecto es grande. Estos perfiles definen cuántos
 * miembros se muestran por clase antes de usar indicadores de desborde como "... N más".</p>
 */
public enum UmlSourceImportRenderProfile {
    LIGHT("Ligero", 5, 5, true, true,
            "Mapa arquitectónico mínimo para abrir rápido proyectos grandes."),
    MEDIUM("Medio", 10, 10, true, true,
            "Equilibrio entre contexto visual y rendimiento."),
    DETAILED("Detallado", 25, 25, true, true,
            "Vista rica por defecto para módulos pequeños o medianos."),
    FULL("Completo", Integer.MAX_VALUE, Integer.MAX_VALUE, true, true,
            "Muestra todo el contenido; debe usarse solo con confirmación explícita.");

    private final String displayName;
    private final int maxVisibleAttributes;
    private final int maxVisibleMethods;
    private final boolean showAttributes;
    private final boolean showMethods;
    private final String description;

    UmlSourceImportRenderProfile(String displayName, int maxVisibleAttributes, int maxVisibleMethods,
                                 boolean showAttributes, boolean showMethods, String description) {
        this.displayName = displayName;
        this.maxVisibleAttributes = maxVisibleAttributes;
        this.maxVisibleMethods = maxVisibleMethods;
        this.showAttributes = showAttributes;
        this.showMethods = showMethods;
        this.description = description;
    }

    public String displayName() {
        return displayName;
    }

    public int maxVisibleAttributes() {
        return maxVisibleAttributes;
    }

    public int maxVisibleMethods() {
        return maxVisibleMethods;
    }

    public boolean showAttributes() {
        return showAttributes;
    }

    public boolean showMethods() {
        return showMethods;
    }

    public String description() {
        return description;
    }

    public boolean isFullRender() {
        return this == FULL;
    }

    public int maxVisibleMembersForSection(boolean methodSection) {
        return methodSection ? maxVisibleMethods : maxVisibleAttributes;
    }

    public static UmlSourceImportRenderProfile safeDefault() {
        return DETAILED;
    }
}
