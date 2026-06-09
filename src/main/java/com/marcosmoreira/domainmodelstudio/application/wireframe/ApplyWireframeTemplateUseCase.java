package com.marcosmoreira.domainmodelstudio.application.wireframe;

import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeComponent;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeComponentKind;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeDocument;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeScreen;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeScreenTemplateKind;
import java.util.List;
import java.util.Objects;

/**
 * Inserta plantillas de wireframe administrativo como andamiaje visual.
 *
 * <p>El caso de uso solo crea pantallas y componentes semánticos. La vista decide
 * cómo representarlos con rectángulos, líneas, tablas simuladas y otros bloques.</p>
 */
public final class ApplyWireframeTemplateUseCase {

    public WireframeDocument apply(WireframeDocument document, WireframeScreenTemplateKind templateKind) {
        Objects.requireNonNull(document, "document");
        WireframeScreenTemplateKind normalized = templateKind == null ? WireframeScreenTemplateKind.CRUD_CATALOG : templateKind;
        WireframeTemplate template = templateFor(normalized);
        String screenId = document.nextScreenId();
        WireframeDocument output = document.withScreen(new WireframeScreen(
                screenId,
                template.screenName(),
                template.moduleName(),
                template.purpose(),
                template.notes()));
        int order = 0;
        for (ComponentSpec component : template.components()) {
            output = output.withComponent(new WireframeComponent(
                    output.nextComponentId(),
                    screenId,
                    component.kind(),
                    component.name(),
                    order++,
                    component.binding(),
                    component.behavior(),
                    component.notes()));
        }
        return output;
    }

    private static WireframeTemplate templateFor(WireframeScreenTemplateKind kind) {
        return switch (kind) {
            case CRUD_CATALOG, CRUD_LIST -> crudCatalog();
            case WIZARD_FLOW -> wizardFlow();
            case EXPEDIENT_CASE, MASTER_DETAIL -> expedientCase();
            case OPERATIVE_QUEUE -> operativeQueue();
            case DASHBOARD -> dashboard();
            case SETTINGS -> settings();
            case APPROVAL_REVIEW -> approvalReview();
            case REPORTS -> reports();
            case ADVANCED_SEARCH -> advancedSearch();
            case DOCUMENTAL -> documental();
            case CALENDAR_AGENDA -> calendarAgenda();
            case LOGIN -> login();
            case DATA_FORM -> dataForm();
            case ROLES_PERMISSIONS -> rolesPermissions();
        };
    }

    private static WireframeTemplate crudCatalog() {
        return new WireframeTemplate("Listado administrativo", "Gestión", "Consultar, filtrar, abrir, crear y mantener registros simples.",
                "Patrón para catálogos y mantenimientos sin ciclo de vida complejo.", List.of(
                topBar("Encabezado del módulo"),
                filter("Filtros principales", "Estado, fecha, categoría o responsable."),
                search("Búsqueda rápida", "Nombre, código, documento o referencia."),
                table("Tabla de resultados", "Columnas principales, estado visible y acciones por fila."),
                pagination("Paginación", "Total, página actual y tamaño de página."),
                button("Nuevo registro", "Abrir formulario de creación."),
                button("Exportar", "Descargar listado filtrado."),
                emptyState("Sin resultados", "Mostrar ayuda cuando no hay datos o filtros coincidentes.")
        ));
    }

    private static WireframeTemplate wizardFlow() {
        return new WireframeTemplate("Crear registro guiado", "Captura", "Capturar información por etapas con validaciones progresivas.",
                "Útil cuando un formulario completo sería demasiado cargado para una sola pantalla.", List.of(
                topBar("Crear orden"),
                stepper("Pasos", "Cliente > Equipo > Problema > Accesorios > Resumen."),
                section("Paso actual"),
                form("Datos del paso", "Campos visibles solo para la etapa actual."),
                summary("Resumen lateral", "Datos ya capturados para revisar antes de confirmar."),
                alert("Validación del paso", "Errores cerca de la etapa correspondiente."),
                button("Atrás", "Volver al paso anterior."),
                button("Siguiente", "Validar etapa y avanzar."),
                button("Finalizar", "Confirmar creación en el último paso.")
        ));
    }

    private static WireframeTemplate expedientCase() {
        return new WireframeTemplate("Detalle de expediente", "Operación", "Trabajar una entidad viva con estado, historial y secciones.",
                "Patrón recomendado para órdenes, tickets, garantías, solicitudes o casos con ciclo de vida.", List.of(
                topBar("Cabecera del expediente"),
                badge("Estado actual", "Recibida, en diagnóstico, lista, entregada o anulada."),
                summary("Datos principales", "Cliente, equipo, responsable, fecha y prioridad."),
                tabs("Secciones", "Resumen, diagnóstico, pagos, historial, documentos y observaciones."),
                detail("Detalle seleccionado", "Contenido de la pestaña activa."),
                documentList("Documentos y evidencias", "Fotos, comprobantes, reportes y anexos."),
                button("Acción principal", "Avanzar el caso según su estado."),
                button("Acción peligrosa", "Anular, rechazar o cerrar con confirmación."),
                alert("Advertencia de estado", "Explica por qué una acción puede estar bloqueada.")
        ));
    }

    private static WireframeTemplate operativeQueue() {
        return new WireframeTemplate("Bandeja operativa", "Operación", "Gestionar trabajo pendiente, priorizar y actuar rápido.",
                "Patrón para colas de diagnóstico, aprobación, pagos, tickets o garantías.", List.of(
                topBar("Bandeja de pendientes"),
                filter("Filtros rápidos", "Hoy, urgente, sin asignar, vencidos o por responsable."),
                table("Lista de pendientes", "Número, cliente, estado, prioridad, responsable y tiempo."),
                detail("Vista previa lateral", "Resumen del pendiente seleccionado."),
                badge("Prioridad", "Urgente, normal, bloqueado o vencido."),
                button("Asignar", "Tomar o asignar el pendiente."),
                button("Abrir detalle", "Ir al expediente completo."),
                emptyState("Sin pendientes", "Estado vacío cuando no hay trabajo en cola.")
        ));
    }

    private static WireframeTemplate dashboard() {
        return new WireframeTemplate("Dashboard administrativo", "Inicio", "Mostrar resumen operativo, alertas y accesos rápidos.",
                "No debe ser un adorno: debe orientar decisiones y tareas frecuentes.", List.of(
                topBar("Barra superior"),
                sidebar("Menú lateral"),
                card("Indicadores principales", "Órdenes pendientes, pagos del día, stock bajo y entregas listas."),
                chart("Resumen visual", "Gráfico simulado de actividad o estado operativo."),
                alert("Alertas de gestión", "Atrasos, datos incompletos, stock crítico o pagos pendientes."),
                table("Lista corta", "Pendientes más importantes o recientes."),
                button("Acción rápida", "Nueva orden, registrar pago o abrir bandeja.")
        ));
    }

    private static WireframeTemplate settings() {
        return new WireframeTemplate("Configuración", "Administración", "Editar parámetros generales del sistema.",
                "Lo que cambia poco debe separarse de la operación diaria.", List.of(
                topBar("Configuración"),
                sidebar("Categorías"),
                tabs("Secciones", "General, usuarios, roles, catálogos, reportes e integraciones."),
                panel("Parámetros generales", "Nombre del negocio, sucursal, formato y preferencias."),
                form("Formulario de parámetros", "Campos editables agrupados por categoría."),
                alert("Cambios pendientes", "Aviso cuando hay parámetros no guardados."),
                button("Guardar configuración", "Aplicar cambios."),
                button("Restaurar", "Volver a valores anteriores si aplica.")
        ));
    }

    private static WireframeTemplate approvalReview() {
        return new WireframeTemplate("Revisión y aprobación", "Control", "Decidir sobre una solicitud con información suficiente y trazabilidad.",
                "Patrón para descuentos, anulaciones, garantías, compras, pagos o cambios críticos.", List.of(
                topBar("Solicitud por revisar"),
                summary("Datos de la solicitud", "Quién solicita, motivo, fecha, monto o registro afectado."),
                approvalPanel("Decisión", "Aprobar, rechazar o pedir más información."),
                documentList("Respaldo", "Documentos, evidencias o historial necesario para decidir."),
                field("Motivo de decisión", "decision.motivo"),
                button("Aprobar", "Aceptar solicitud y avanzar estado."),
                button("Rechazar", "Denegar solicitud con motivo."),
                alert("Riesgo operativo", "Advertencia antes de una acción sensible.")
        ));
    }

    private static WireframeTemplate reports() {
        return new WireframeTemplate("Reportes", "Reportes", "Filtrar, generar, revisar y exportar información administrativa.",
                "El reporte debe dejar claro periodo, criterios, resultado y fecha de generación.", List.of(
                topBar("Reporte administrativo"),
                filter("Filtros de reporte", "Periodo, módulo, estado, responsable, sucursal o formato."),
                button("Generar", "Calcular o consultar el reporte."),
                table("Resultados", "Datos tabulares agregados o detallados."),
                report("Vista previa", "Bloque simulado del documento resultante."),
                button("Exportar PDF", "Descargar documento."),
                button("Exportar Excel", "Descargar tabla."),
                alert("Estado de generación", "Pendiente, generado o error.")
        ));
    }

    private static WireframeTemplate advancedSearch() {
        return new WireframeTemplate("Búsqueda especializada", "Consulta", "Encontrar registros con criterios precisos y resultados accionables.",
                "Útil cuando una búsqueda simple no basta, por ejemplo IMEI, comprobante, documento o rango.", List.of(
                topBar("Búsqueda avanzada"),
                search("Criterio principal", "IMEI, cédula, número de orden o comprobante."),
                filter("Filtros avanzados", "Estado, fecha, responsable, módulo, rango o categoría."),
                badge("Criterios activos", "Muestra filtros aplicados para poder limpiarlos."),
                table("Resultados", "Coincidencias con datos mínimos para identificar."),
                detail("Detalle rápido", "Resumen del resultado seleccionado."),
                button("Buscar", "Ejecutar consulta."),
                button("Limpiar filtros", "Reiniciar criterios."),
                emptyState("Sin coincidencias", "Indicar cómo ampliar o corregir la búsqueda.")
        ));
    }

    private static WireframeTemplate documental() {
        return new WireframeTemplate("Documentos y evidencias", "Documental", "Organizar archivos, anexos, comprobantes y evidencias de un caso.",
                "Los documentos necesitan tipo, fecha, origen, responsable y permisos.", List.of(
                topBar("Documentos"),
                filter("Filtros documentales", "Tipo, fecha, origen, responsable o estado."),
                documentList("Lista de documentos", "Tipo, nombre, fecha, responsable y acciones."),
                detail("Vista previa", "Resumen o previsualización del documento seleccionado."),
                button("Subir documento", "Adjuntar archivo o evidencia."),
                button("Descargar", "Obtener copia del archivo."),
                button("Anular documento", "Acción sensible con motivo y permiso."),
                emptyState("Sin documentos", "Mensaje cuando el expediente no tiene evidencias.")
        ));
    }

    private static WireframeTemplate calendarAgenda() {
        return new WireframeTemplate("Agenda / calendario", "Planificación", "Organizar eventos, entregas, turnos o recordatorios en el tiempo.",
                "Debe usarse solo cuando el tiempo es parte central del trabajo.", List.of(
                topBar("Agenda"),
                filter("Filtros de agenda", "Día, semana, mes, responsable, sucursal o estado."),
                calendar("Vista temporal", "Eventos distribuidos por hora, día o semana."),
                detail("Detalle de evento", "Cliente, orden, responsable, hora, ubicación y estado."),
                button("Nuevo evento", "Programar cita, entrega o recordatorio."),
                button("Reprogramar", "Cambiar fecha u hora."),
                alert("Conflicto de horario", "Aviso cuando hay cruce o bloqueo.")
        ));
    }

    private static WireframeTemplate login() {
        return new WireframeTemplate("Login", "Seguridad", "Permitir entrada segura al sistema.",
                "Plantilla mínima de acceso; no representa diseño visual final.", List.of(
                topBar("Marca institucional"),
                card("Panel de acceso", "Bloque central con logo o nombre del sistema."),
                field("Usuario", "login"),
                field("Contraseña", "password"),
                button("Ingresar", "Validar credenciales y abrir dashboard."),
                alert("Credenciales inválidas", "Mensaje de error cuando el acceso falla.")
        ));
    }

    private static WireframeTemplate dataForm() {
        return new WireframeTemplate("Formulario de registro", "Gestión", "Capturar o editar datos de un registro.",
                "Plantilla para acordar campos, validaciones visibles y acciones.", List.of(
                section("Datos principales"),
                form("Formulario", "Grupo de campos obligatorios y opcionales."),
                field("Nombre", "nombre"),
                field("Estado", "estado"),
                field("Fecha", "fecha"),
                alert("Validación", "Mensajes de campos incompletos o inválidos."),
                button("Guardar", "Persistir cambios."),
                button("Cancelar", "Volver sin guardar.")
        ));
    }

    private static WireframeTemplate rolesPermissions() {
        return new WireframeTemplate("Roles y permisos", "Seguridad", "Revisar accesos funcionales por rol.",
                "Plantilla para levantar permisos desde el punto de vista operativo.", List.of(
                sidebar("Roles"),
                table("Matriz de permisos", "Filas de permisos y columnas de roles."),
                detail("Detalle de permiso", "Descripción, módulo afectado y observaciones."),
                button("Asignar", "Activar permiso para el rol seleccionado."),
                alert("Riesgo operativo", "Advertencia para permisos críticos.")
        ));
    }

    private static ComponentSpec topBar(String name) { return new ComponentSpec(WireframeComponentKind.TOP_BAR, name, "", "Navegación y acciones globales.", ""); }
    private static ComponentSpec sidebar(String name) { return new ComponentSpec(WireframeComponentKind.SIDEBAR, name, "", "Navegación por módulos.", ""); }
    private static ComponentSpec section(String name) { return new ComponentSpec(WireframeComponentKind.SECTION, name, "", "Agrupar contenido relacionado.", ""); }
    private static ComponentSpec panel(String name, String behavior) { return new ComponentSpec(WireframeComponentKind.PANEL, name, "", behavior, ""); }
    private static ComponentSpec card(String name, String behavior) { return new ComponentSpec(WireframeComponentKind.CARD, name, "", behavior, ""); }
    private static ComponentSpec form(String name, String behavior) { return new ComponentSpec(WireframeComponentKind.FORM, name, "", behavior, ""); }
    private static ComponentSpec field(String name, String binding) { return new ComponentSpec(WireframeComponentKind.FIELD, name, binding, "Captura o muestra dato del negocio.", ""); }
    private static ComponentSpec filter(String name, String behavior) { return new ComponentSpec(WireframeComponentKind.FILTER, name, "", behavior, ""); }
    private static ComponentSpec search(String name, String behavior) { return new ComponentSpec(WireframeComponentKind.SEARCH, name, "q", behavior, ""); }
    private static ComponentSpec table(String name, String behavior) { return new ComponentSpec(WireframeComponentKind.TABLE, name, "", behavior, ""); }
    private static ComponentSpec pagination(String name, String behavior) { return new ComponentSpec(WireframeComponentKind.PAGINATION, name, "", behavior, ""); }
    private static ComponentSpec button(String name, String behavior) { return new ComponentSpec(WireframeComponentKind.BUTTON, name, "", behavior, ""); }
    private static ComponentSpec tabs(String name, String behavior) { return new ComponentSpec(WireframeComponentKind.TABS, name, "", behavior, ""); }
    private static ComponentSpec alert(String name, String behavior) { return new ComponentSpec(WireframeComponentKind.ALERT, name, "", behavior, ""); }
    private static ComponentSpec chart(String name, String behavior) { return new ComponentSpec(WireframeComponentKind.CHART, name, "", behavior, ""); }
    private static ComponentSpec report(String name, String behavior) { return new ComponentSpec(WireframeComponentKind.REPORT, name, "", behavior, ""); }
    private static ComponentSpec detail(String name, String behavior) { return new ComponentSpec(WireframeComponentKind.DETAIL, name, "", behavior, ""); }
    private static ComponentSpec stepper(String name, String behavior) { return new ComponentSpec(WireframeComponentKind.STEPPER, name, "", behavior, ""); }
    private static ComponentSpec badge(String name, String behavior) { return new ComponentSpec(WireframeComponentKind.BADGE, name, "", behavior, ""); }
    private static ComponentSpec emptyState(String name, String behavior) { return new ComponentSpec(WireframeComponentKind.EMPTY_STATE, name, "", behavior, ""); }
    private static ComponentSpec documentList(String name, String behavior) { return new ComponentSpec(WireframeComponentKind.DOCUMENT_LIST, name, "", behavior, ""); }
    private static ComponentSpec calendar(String name, String behavior) { return new ComponentSpec(WireframeComponentKind.CALENDAR, name, "", behavior, ""); }
    private static ComponentSpec approvalPanel(String name, String behavior) { return new ComponentSpec(WireframeComponentKind.APPROVAL_PANEL, name, "", behavior, ""); }
    private static ComponentSpec summary(String name, String behavior) { return new ComponentSpec(WireframeComponentKind.SUMMARY, name, "", behavior, ""); }

    private record WireframeTemplate(String screenName, String moduleName, String purpose, String notes, List<ComponentSpec> components) { }

    private record ComponentSpec(WireframeComponentKind kind, String name, String binding, String behavior, String notes) { }
}
