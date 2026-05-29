package com.marcosmoreira.domainmodelstudio.presentation.dialogs;

import static com.marcosmoreira.domainmodelstudio.presentation.dialogs.ManualFigureDrawingSupport.*;

import javafx.scene.layout.Pane;

/** Figuras didácticas de wireframes administrativos y maquetas de baja fidelidad. */
final class ManualFigureAdminWireframeFigures {

    private ManualFigureAdminWireframeFigures() {
    }

    static boolean draw(String figureId, Pane canvas) {
        switch (figureId) {
            case "admin-wireframe-layout" -> drawLegacyLayout(canvas);
            case "admin-wireframe-layout-base" -> drawLayoutBase(canvas);
            case "admin-wireframe-primitives" -> drawPrimitives(canvas);
            case "admin-wireframe-screen-linking" -> drawScreenLinking(canvas);
            case "admin-wireframe-vs-screen-flow" -> drawVsScreenFlow(canvas);
            case "admin-wireframe-common-boundaries" -> drawCommonBoundaries(canvas);
            case "admin-wireframe-hierarchy" -> drawHierarchy(canvas);
            case "admin-wireframe-grouping" -> drawGrouping(canvas);
            case "admin-wireframe-primary-secondary-actions" -> drawPrimarySecondaryActions(canvas);
            case "admin-wireframe-validation-feedback" -> drawValidationFeedback(canvas);
            case "admin-wireframe-danger-action" -> drawDangerAction(canvas);
            case "admin-wireframe-progressive-disclosure" -> drawProgressiveDisclosure(canvas);
            case "admin-wireframe-table-reading" -> drawTableReading(canvas);
            case "admin-wireframe-state-visibility" -> drawStateVisibility(canvas);
            case "admin-wireframe-crud-pattern" -> drawCrudPattern(canvas);
            case "admin-wireframe-wizard-pattern" -> drawWizardPattern(canvas);
            case "admin-wireframe-expediente-pattern" -> drawExpedientePattern(canvas);
            case "admin-wireframe-bandeja-pattern" -> drawBandejaPattern(canvas);
            case "admin-wireframe-dashboard-pattern" -> drawDashboardPattern(canvas);
            case "admin-wireframe-config-pattern" -> drawConfigPattern(canvas);
            case "admin-wireframe-approval-pattern" -> drawApprovalPattern(canvas);
            case "admin-wireframe-report-pattern" -> drawReportPattern(canvas);
            case "admin-wireframe-search-pattern" -> drawSearchPattern(canvas);
            case "admin-wireframe-document-pattern" -> drawDocumentPattern(canvas);
            case "admin-wireframe-calendar-pattern" -> drawCalendarPattern(canvas);
            case "admin-wireframe-repair-module-example" -> drawRepairModuleExample(canvas);
            case "admin-wireframe-order-detail-example" -> drawOrderDetailExample(canvas);
            case "admin-wireframe-role-state-actions" -> drawRoleStateActions(canvas);
            case "admin-wireframe-empty-error-states" -> drawEmptyErrorStates(canvas);
            case "admin-wireframe-ai-specification" -> drawAiSpecification(canvas);
            default -> {
                return false;
            }
        }
        return true;
    }

    /** Figura heredada del capítulo anterior. */
    private static void drawLegacyLayout(Pane p) {
        rectangle(p, 35, 24, 400, 125, "");
        line(p, 35, 52, 435, 52);
        line(p, 118, 52, 118, 149);
        text(p, "Barra superior", 55, 43);
        text(p, "Menú", 58, 83);
        rectangle(p, 140, 70, 130, 22, "Filtro");
        rectangle(p, 288, 70, 95, 22, "Botón");
        rectangle(p, 140, 105, 245, 34, "Tabla / formulario");
    }

    private static void drawLayoutBase(Pane p) {
        rectangle(p, 28, 20, 414, 128, "");
        line(p, 28, 50, 442, 50);
        line(p, 124, 50, 124, 148);
        text(p, "Topbar: contexto, usuario, búsqueda", 56, 41);
        text(p, "Sidebar", 55, 78);
        rectangle(p, 48, 92, 55, 15, "Módulos");
        rectangle(p, 48, 116, 55, 15, "Menú");
        rectangle(p, 150, 70, 250, 56, "Workspace central\nPantalla o módulo activo");
        rectangle(p, 315, 133, 85, 18, "Acciones");
    }

    private static void drawPrimitives(Pane p) {
        rectangle(p, 26, 25, 78, 45, "Pantalla");
        rectangle(p, 124, 25, 78, 45, "Panel");
        rectangle(p, 222, 25, 78, 22, "Campo");
        rectangle(p, 222, 56, 78, 22, "Botón");
        rectangle(p, 320, 25, 112, 58, "Tabla\nsimulada");
        line(p, 328, 44, 424, 44);
        line(p, 328, 61, 424, 61);
        line(p, 356, 25, 356, 83);
        line(p, 392, 25, 392, 83);
        rectangle(p, 42, 106, 110, 34, "Modal");
        line(p, 176, 122, 260, 122);
        arrow(p, 276, 122, 340, 122);
        text(p, "Texto / etiqueta", 344, 126);
    }

    private static void drawScreenLinking(Pane p) {
        screenBox(p, 32, 45, "Listado\nórdenes");
        screenBox(p, 190, 45, "Detalle\norden");
        screenBox(p, 342, 45, "Pago");
        arrow(p, 122, 78, 190, 78);
        arrow(p, 280, 78, 342, 78);
        text(p, "seleccionar", 128, 68);
        text(p, "registrar", 287, 68);
        text(p, "Las flechas apoyan la navegación; el foco sigue siendo la maqueta.", 50, 140);
    }

    private static void drawVsScreenFlow(Pane p) {
        text(p, "Flujo de pantallas", 36, 32);
        rectangle(p, 30, 50, 75, 30, "Dashboard");
        arrow(p, 108, 65, 150, 65);
        rectangle(p, 154, 50, 82, 30, "Reparar");
        arrow(p, 239, 65, 280, 65);
        rectangle(p, 284, 50, 75, 30, "Detalle");
        text(p, "Wireframe", 36, 108);
        rectangle(p, 130, 100, 210, 52, "Contenido interno:\nfiltros + tabla + botones + feedback");
    }

    private static void drawCommonBoundaries(Pane p) {
        rectangle(p, 32, 30, 168, 96, "Sí es\nestructura\ndatos\nacciones\nfeedback");
        rectangle(p, 270, 30, 168, 96, "No es\nFigma\nfrontend real\nbranding final\nbackend");
        arrow(p, 205, 78, 265, 78);
        text(p, "límite claro", 207, 68);
        text(p, "La maqueta piensa antes de programar.", 112, 151);
    }


    private static void drawHierarchy(Pane p) {
        text(p, "Jerarquía visual", 34, 28);
        rectangle(p, 32, 42, 245, 22, "Órdenes de reparación");
        rectangle(p, 32, 72, 285, 24, "Filtros: estado / técnico / fecha");
        rectangle(p, 334, 72, 92, 24, "Nueva orden");
        rectangle(p, 32, 108, 394, 45, "Tabla principal dominante");
        text(p, "Lo frecuente y seguro pesa más que lo excepcional.", 70, 164);
    }

    private static void drawGrouping(Pane p) {
        text(p, "Agrupación y proximidad", 30, 28);
        rectangle(p, 30, 48, 125, 78, "Datos cliente\nNombre\nCédula\nTeléfono");
        rectangle(p, 173, 48, 125, 78, "Datos equipo\nMarca\nModelo\nIMEI");
        rectangle(p, 316, 48, 125, 78, "Problema\nDescripción\nAccesorios\nNotas");
        text(p, "Los campos relacionados viven juntos; lo distinto se separa.", 54, 155);
    }

    private static void drawPrimarySecondaryActions(Pane p) {
        text(p, "Acciones con pesos distintos", 30, 28);
        rectangle(p, 40, 55, 115, 32, "Guardar");
        rectangle(p, 170, 55, 95, 32, "Cancelar");
        rectangle(p, 280, 55, 95, 32, "Exportar");
        rectangle(p, 330, 112, 105, 32, "Anular");
        text(p, "Primaria", 70, 105);
        text(p, "Secundarias", 175, 105);
        text(p, "Peligrosa separada", 316, 160);
    }

    private static void drawValidationFeedback(Pane p) {
        text(p, "Feedback y validación", 30, 28);
        rectangle(p, 44, 52, 160, 28, "Monto:  ______");
        rectangle(p, 44, 88, 286, 28, "Error: el monto supera el saldo pendiente");
        rectangle(p, 44, 130, 150, 24, "Corregir monto");
        arrow(p, 208, 67, 250, 67);
        rectangle(p, 258, 52, 150, 28, "Saldo: $40.00");
    }

    private static void drawDangerAction(Pane p) {
        text(p, "Acción peligrosa", 30, 28);
        rectangle(p, 34, 50, 170, 76, "Pantalla normal\nGuardar\nRegistrar pago");
        rectangle(p, 260, 42, 160, 96, "Confirmar anulación\nMotivo: ______\n[Cancelar] [Anular]");
        arrow(p, 210, 88, 255, 88);
        text(p, "separar + confirmar + registrar motivo", 98, 160);
    }

    private static void drawProgressiveDisclosure(Pane p) {
        text(p, "Revelación progresiva", 30, 28);
        rectangle(p, 35, 48, 398, 28, "Resumen visible: Orden #0045 · En reparación · Cliente Juan");
        rectangle(p, 35, 88, 84, 24, "Resumen");
        rectangle(p, 126, 88, 84, 24, "Pagos");
        rectangle(p, 217, 88, 84, 24, "Historial");
        rectangle(p, 308, 88, 84, 24, "Documentos");
        rectangle(p, 35, 123, 357, 28, "El detalle aparece cuando el usuario lo necesita");
    }

    private static void drawTableReading(Pane p) {
        text(p, "Lectura rápida de tablas", 30, 25);
        rectangle(p, 30, 42, 400, 24, "Filtros: estado / técnico / fecha / cliente");
        rectangle(p, 30, 76, 400, 78, "Nro | Cliente | Equipo | Estado | Técnico | Acciones");
        line(p, 30, 102, 430, 102);
        line(p, 30, 128, 430, 128);
        line(p, 92, 76, 92, 154);
        line(p, 160, 76, 160, 154);
        line(p, 235, 76, 235, 154);
        line(p, 305, 76, 305, 154);
        line(p, 365, 76, 365, 154);
        text(p, "Columnas principales primero; acciones al final.", 86, 166);
    }

    private static void drawStateVisibility(Pane p) {
        text(p, "Estado visible", 30, 28);
        rectangle(p, 36, 46, 175, 92, "Orden #0045\nEstado: En reparación\nCliente: Juan");
        rectangle(p, 250, 46, 160, 28, "Registrar avance");
        rectangle(p, 250, 84, 160, 28, "Agregar observación");
        rectangle(p, 250, 122, 160, 28, "Entregar: bloqueado");
        arrow(p, 214, 92, 246, 92);
        text(p, "El estado condiciona las acciones visibles.", 88, 164);
    }


    private static void drawCrudPattern(Pane p) {
        text(p, "CRUD / catálogo", 30, 24);
        rectangle(p, 30, 40, 400, 24, "Clientes                                      [Nuevo]");
        rectangle(p, 30, 72, 400, 22, "Buscar: ______   Estado: Activo");
        rectangle(p, 30, 104, 400, 48, "Nombre | Cédula | Teléfono | Estado | Acciones");
        line(p, 30, 128, 430, 128);
        text(p, "Registros estables: listar, crear, editar, desactivar.", 62, 166);
    }

    private static void drawWizardPattern(Pane p) {
        text(p, "Wizard / flujo guiado", 30, 24);
        rectangle(p, 30, 45, 400, 24, "Paso 1 Cliente > Paso 2 Equipo > Paso 3 Problema > Resumen");
        rectangle(p, 55, 86, 320, 45, "Datos de la etapa actual\ncampos + validación por paso");
        rectangle(p, 55, 145, 80, 22, "Atrás");
        rectangle(p, 295, 145, 80, 22, "Siguiente");
    }

    private static void drawExpedientePattern(Pane p) {
        text(p, "Expediente / caso vivo", 30, 24);
        rectangle(p, 30, 42, 400, 32, "Orden #0045 · Estado: En reparación · Cliente Juan");
        rectangle(p, 30, 84, 84, 24, "Resumen");
        rectangle(p, 122, 84, 84, 24, "Diagnóstico");
        rectangle(p, 214, 84, 84, 24, "Pagos");
        rectangle(p, 306, 84, 84, 24, "Historial");
        rectangle(p, 30, 120, 260, 32, "Contenido del caso vivo");
        rectangle(p, 315, 120, 95, 32, "Acciones");
    }

    private static void drawBandejaPattern(Pane p) {
        text(p, "Bandeja / cola operativa", 30, 24);
        rectangle(p, 30, 42, 400, 24, "Pendientes: [Hoy] [Urgentes] [Sin técnico]");
        rectangle(p, 30, 76, 180, 78, "#0045 iPhone 11\n#0046 Samsung A32\n#0047 Xiaomi");
        rectangle(p, 230, 76, 200, 78, "Detalle seleccionado\nCliente + problema\n[Asignar] [Abrir]");
    }

    private static void drawDashboardPattern(Pane p) {
        text(p, "Dashboard", 30, 24);
        rectangle(p, 30, 42, 95, 42, "Pendientes\n12");
        rectangle(p, 140, 42, 95, 42, "Pagos hoy\n$340");
        rectangle(p, 250, 42, 95, 42, "Stock bajo\n5");
        rectangle(p, 30, 100, 190, 52, "Alertas\norden atrasada\nrepuesto agotado");
        rectangle(p, 245, 100, 160, 52, "Accesos rápidos\nNueva orden\nRegistrar pago");
    }

    private static void drawConfigPattern(Pane p) {
        text(p, "Configuración", 30, 24);
        rectangle(p, 30, 42, 105, 112, "Usuarios\nRoles\nCatálogos\nSucursales");
        rectangle(p, 155, 42, 250, 112, "Datos del negocio\nNombre: ______\nRUC: ______\nDirección: ______\n[Guardar cambios]");
    }

    private static void drawApprovalPattern(Pane p) {
        text(p, "Aprobación / revisión", 30, 24);
        rectangle(p, 40, 42, 360, 68, "Solicitud de descuento\nOrden #0045 · Monto $120 · Descuento $20\nMotivo: cliente frecuente");
        rectangle(p, 60, 130, 85, 24, "Aprobar");
        rectangle(p, 175, 130, 85, 24, "Rechazar");
        rectangle(p, 290, 130, 100, 24, "Pedir datos");
    }

    private static void drawReportPattern(Pane p) {
        text(p, "Reportes", 30, 24);
        rectangle(p, 30, 42, 400, 24, "Desde: __/__/__  Hasta: __/__/__  Técnico: Todos  [Generar]");
        rectangle(p, 30, 78, 400, 58, "Técnico | Cantidad | Entregadas | Pendientes | Total");
        rectangle(p, 275, 145, 70, 22, "PDF");
        rectangle(p, 358, 145, 70, 22, "Excel");
    }

    private static void drawSearchPattern(Pane p) {
        text(p, "Búsqueda especializada", 30, 24);
        rectangle(p, 30, 42, 400, 45, "IMEI: ______   Cliente: ______   Estado: [v]\nFecha: ____ a ____     [Buscar] [Limpiar]");
        rectangle(p, 30, 105, 400, 48, "Resultados: #0045 | Juan Pérez | iPhone 11 | En reparación");
    }

    private static void drawDocumentPattern(Pane p) {
        text(p, "Documental", 30, 24);
        rectangle(p, 30, 42, 400, 24, "Documentos de la orden #0045                         [Subir]");
        rectangle(p, 30, 78, 400, 76, "Tipo | Nombre | Fecha | Responsable | Acciones\nFoto | pantalla.jpg | 12/05 | Técnico | Ver\nComprobante | pago.pdf | 12/05 | Caja | Descargar");
    }

    private static void drawCalendarPattern(Pane p) {
        text(p, "Agenda / calendario", 30, 24);
        rectangle(p, 30, 42, 400, 24, "Agenda de entregas      [Día] [Semana] [Mes]");
        rectangle(p, 50, 82, 350, 70, "09:00  Orden #0045 - Entrega\n10:30  Orden #0048 - Revisión\n15:00  Cliente retira equipo");
    }

    private static void drawRepairModuleExample(Pane p) {
        text(p, "Módulo Reparaciones", 30, 24);
        screenBox(p, 28, 48, "Listado\nórdenes");
        screenBox(p, 180, 48, "Detalle\norden");
        rectangle(p, 325, 34, 110, 26, "Diagnóstico");
        rectangle(p, 325, 74, 110, 26, "Pago");
        rectangle(p, 325, 114, 110, 26, "Entrega");
        arrow(p, 118, 80, 180, 80);
        arrow(p, 270, 68, 320, 47);
        arrow(p, 270, 80, 320, 87);
        arrow(p, 270, 92, 320, 127);
        text(p, "Listado + expediente + acciones por estado", 80, 164);
    }

    private static void drawOrderDetailExample(Pane p) {
        text(p, "Detalle de orden", 30, 24);
        rectangle(p, 30, 42, 400, 30, "Orden #0045 · Estado: En reparación · Cliente Juan · Equipo iPhone 11");
        rectangle(p, 30, 82, 72, 22, "Resumen");
        rectangle(p, 108, 82, 82, 22, "Diagnóstico");
        rectangle(p, 196, 82, 62, 22, "Pagos");
        rectangle(p, 264, 82, 72, 22, "Historial");
        rectangle(p, 342, 82, 82, 22, "Documentos");
        rectangle(p, 30, 115, 245, 38, "Contenido de la pestaña seleccionada");
        rectangle(p, 292, 115, 138, 38, "Acciones\navance / pago / entrega");
    }

    private static void drawRoleStateActions(Pane p) {
        text(p, "Acciones por rol y estado", 30, 24);
        rectangle(p, 30, 45, 90, 34, "Recepción\ncrear orden");
        rectangle(p, 135, 45, 90, 34, "Técnico\ndiagnóstico");
        rectangle(p, 240, 45, 90, 34, "Caja\nregistrar pago");
        rectangle(p, 345, 45, 90, 34, "Supervisor\nanular");
        rectangle(p, 72, 110, 326, 32, "Estado de orden: Recibida → Diagnosticada → Lista → Entregada");
        text(p, "El rol y el estado deciden qué botones aparecen o se bloquean.", 62, 164);
    }

    private static void drawEmptyErrorStates(Pane p) {
        text(p, "Estados vacíos y errores", 30, 24);
        rectangle(p, 30, 45, 125, 72, "Sin resultados\nNo hay órdenes\n[Crear orden]");
        rectangle(p, 173, 45, 125, 72, "Cargando...\nconsultando\nórdenes");
        rectangle(p, 316, 45, 125, 72, "Error\nno se pudo\nconsultar");
        text(p, "Un estado debe explicar qué pasa y qué puede hacer el usuario.", 52, 150);
    }

    private static void drawAiSpecification(Pane p) {
        text(p, "Wireframe → especificación", 30, 24);
        rectangle(p, 30, 44, 160, 92, "Maqueta\ncampos\nbotones\ntabla\nfeedback");
        arrow(p, 198, 88, 258, 88);
        rectangle(p, 268, 44, 170, 92, "Especificación\npropósito\ndatos\npermisos\nerrores\npantallas");
        text(p, "Sirve para pedir implementación a IA o frontend sin ambigüedad.", 50, 160);
    }

    private static void screenBox(Pane p, double x, double y, String label) {
        rectangle(p, x, y, 90, 62, "");
        line(p, x, y + 18, x + 90, y + 18);
        rectangle(p, x + 10, y + 30, 70, 12, "");
        rectangle(p, x + 10, y + 47, 70, 10, "");
        text(p, label, x + 17, y + 14);
    }
}
