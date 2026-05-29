package com.marcosmoreira.domainmodelstudio.domain.wireframe;

/**
 * Tipo de componente funcional dentro de un wireframe administrativo.
 *
 * <p>La lista representa bloques de maqueta, no controles JavaFX ni HTML reales.
 * Su objetivo es levantar requerimientos de interfaz con geometrías simples.</p>
 */
public enum WireframeComponentKind {
    TOP_BAR("Barra superior"),
    SIDEBAR("Menú lateral"),
    SECTION("Sección"),
    PANEL("Panel"),
    CARD("Tarjeta"),
    FORM("Formulario"),
    FIELD("Campo"),
    FILTER("Filtro"),
    SEARCH("Búsqueda"),
    TABLE("Tabla"),
    PAGINATION("Paginación"),
    BUTTON("Botón"),
    TABS("Pestañas"),
    MODAL("Modal"),
    ALERT("Alerta"),
    CHART("Gráfico simulado"),
    REPORT("Reporte"),
    DETAIL("Detalle"),
    MENU("Menú"),
    STEPPER("Indicador de pasos"),
    BADGE("Etiqueta de estado"),
    EMPTY_STATE("Estado vacío"),
    DOCUMENT_LIST("Lista documental"),
    CALENDAR("Calendario"),
    APPROVAL_PANEL("Panel de aprobación"),
    SUMMARY("Resumen"),
    OTHER("Otro");

    private final String displayName;

    WireframeComponentKind(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}
