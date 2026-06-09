package com.marcosmoreira.domainmodelstudio.domain.wireframe;

/** Plantilla de pantalla administrativa para acelerar bocetos de levantamiento. */
public enum WireframeScreenTemplateKind {
    CRUD_CATALOG("CRUD / catálogo", "Listado con filtros, tabla, acciones por fila, paginación y formulario de mantenimiento."),
    WIZARD_FLOW("Wizard / flujo guiado", "Captura por pasos con validación progresiva, resumen y confirmación."),
    EXPEDIENT_CASE("Expediente / caso vivo", "Pantalla de una entidad con estado, secciones, historial, documentos y acciones contextuales."),
    OPERATIVE_QUEUE("Bandeja / cola operativa", "Lista de pendientes con prioridad, selección, detalle lateral y acciones rápidas."),
    DASHBOARD("Dashboard", "Resumen operativo con indicadores, alertas, accesos rápidos y listas cortas."),
    SETTINGS("Configuración", "Parámetros administrativos agrupados por categorías, formularios y advertencias."),
    APPROVAL_REVIEW("Aprobación / revisión", "Pantalla para decidir sobre una solicitud con contexto, motivo y acciones de aprobación o rechazo."),
    REPORTS("Reportes", "Filtros, generación, tabla de resultados, vista previa y exportación."),
    ADVANCED_SEARCH("Búsqueda especializada", "Filtros precisos, criterios activos, resultados y acciones sobre hallazgos."),
    DOCUMENTAL("Documental", "Lista de documentos, evidencias, tipo, origen, vista previa y acciones de archivo."),
    CALENDAR_AGENDA("Agenda / calendario", "Vista temporal para citas, entregas, turnos, recordatorios o eventos operativos."),

    /** Compatibilidad con plantillas antiguas: se conservan para no romper documentos ni pruebas existentes. */
    LOGIN("Login", "Acceso al sistema con usuario, contraseña, acción principal y mensaje de error."),
    CRUD_LIST("Listado CRUD", "Alias histórico de CRUD / catálogo."),
    DATA_FORM("Formulario", "Formulario simple de captura o edición."),
    ROLES_PERMISSIONS("Roles y permisos", "Matriz operativa para revisar roles, permisos y asignaciones."),
    MASTER_DETAIL("Maestro-detalle", "Alias histórico de expediente / caso vivo." );

    private final String displayName;
    private final String description;

    WireframeScreenTemplateKind(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String displayName() {
        return displayName;
    }

    public String description() {
        return description;
    }
}
