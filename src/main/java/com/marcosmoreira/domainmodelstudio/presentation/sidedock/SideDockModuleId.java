package com.marcosmoreira.domainmodelstudio.presentation.sidedock;

/** Identificador estable de un módulo disponible en el panel lateral modular. */
public enum SideDockModuleId {
    STRUCTURE("Estructura", "☰", "Muestra la estructura del modelo activo para seleccionar elementos y navegar el contenido."),
    PROPERTIES("Propiedades", "⚙", "Muestra o edita datos del elemento seleccionado o del documento activo."),
    PALETTE("Elementos", "◇", "Muestra elementos disponibles para insertar en la herramienta activa."),
    VIEW("Vista", "⌕", "Agrupa acciones de visualización como ajustar, centrar o revisar el zoom del área de trabajo."),
    HELP("Ayuda", "▣", "Abre orientación contextual y glosario sobre la herramienta o diagrama activo."),
    SECTIONS("Secciones", "§", "Permite navegar por secciones del documento activo."),
    ENTITIES("Entidades", "E", "Muestra entidades documentadas para navegar o revisar su detalle."),
    PREVIEW("Vista previa", "V", "Muestra una vista de lectura del documento antes de exportarlo o revisarlo."),
    ROLES("Roles", "R", "Muestra los roles definidos en la matriz de permisos."),
    PERMISSIONS("Permisos", "P", "Muestra permisos, acciones y capacidades administrativas del sistema."),
    MATRIX("Matriz", "M", "Permite revisar la relación entre roles y permisos."),
    FILTERS("Filtros", "F", "Filtra la información mostrada según rol, permiso, módulo o criterio disponible."),
    APPEARANCE("Apariencia", "A", "Controla relleno, borde y énfasis visual del elemento seleccionado."),
    ORGANIZATION("Organización", "Org", "Agrupa acciones de autoorganización, alineación y espaciado."),
    VALIDATION("Validación", "Val", "Muestra errores, advertencias y sugerencias del artefacto activo."),
    TRACEABILITY("Trazas internas", "Tr", "Muestra conexiones internas, dependencias y bloqueos dentro del documento activo."),
    EXPORT("Exportar", "Exp", "Exporta la salida activa como archivo del proyecto."),
    COMPONENTS("Componentes", "Cmp", "Muestra componentes o piezas insertables para el tipo de artefacto activo."),
    TIME("Tiempo", "T", "Ordena y revisa secuencias, mensajes y espaciado temporal."),
    DOCUMENT("Documento", "Doc", "Edita portada, logo, metadatos e información documental."),
    LAYERS("Capas", "Cap", "Controla visibilidad, profundidad y bloqueo de elementos visuales."),
    CONTENTS("Contenido", "▣", "Muestra el árbol de temas de la referencia académica."),
    SEARCH("Buscar", "⌕", "Busca términos dentro de la referencia académica.");

    private final String displayName;
    private final String iconText;
    private final String tooltipText;

    SideDockModuleId(String displayName, String iconText, String tooltipText) {
        this.displayName = displayName;
        this.iconText = iconText;
        this.tooltipText = tooltipText;
    }

    public String displayName() {
        return displayName;
    }

    public String iconText() {
        return iconText;
    }

    public String tooltipText() {
        return tooltipText;
    }
}
