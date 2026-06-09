package com.marcosmoreira.domainmodelstudio.presentation.dialogs;

import static com.marcosmoreira.domainmodelstudio.presentation.dialogs.ManualFigureDrawingSupport.*;

import javafx.scene.layout.Pane;
import javafx.scene.shape.*;

/** Figuras didácticas agrupadas para mantener revisable el factory principal. */
final class ManualFigureAdminAccessScreenFigures {

    private ManualFigureAdminAccessScreenFigures() {
    }

    static boolean draw(String figureId, Pane canvas) {
        switch (figureId) {
            case "admin-module-map" -> drawModuleMap(canvas);
            case "admin-module-overview" -> drawAdminModuleOverview(canvas);
            case "admin-module-hierarchy" -> drawAdminModuleHierarchy(canvas);
            case "admin-module-dependencies" -> drawAdminModuleDependencies(canvas);
            case "admin-module-phases" -> drawAdminModulePhases(canvas);
            case "admin-module-transversal" -> drawAdminModuleTransversal(canvas);
            case "admin-module-vs-screen" -> drawAdminModuleVsScreen(canvas);
            case "admin-module-common-errors" -> drawAdminModuleCommonErrors(canvas);
            case "roles-permissions-chain" -> drawRolesPermissions(canvas);
            case "roles-permissions-user-role-permission" -> drawRolesPermissionsUserRolePermission(canvas);
            case "roles-permissions-matrix" -> drawRolesPermissionsMatrix(canvas);
            case "roles-permissions-module-actions" -> drawRolesPermissionsModuleActions(canvas);
            case "roles-permissions-state-based" -> drawRolesPermissionsStateBased(canvas);
            case "roles-permissions-common-errors" -> drawRolesPermissionsCommonErrors(canvas);
            case "screen-flow-navigation" -> drawScreenFlow(canvas);
            case "screen-flow-basic" -> drawScreenFlowBasic(canvas);
            case "screen-flow-branching" -> drawScreenFlowBranching(canvas);
            case "screen-flow-by-role" -> drawScreenFlowByRole(canvas);
            case "screen-flow-error-validation" -> drawScreenFlowErrorValidation(canvas);
            case "screen-flow-vs-wireframe" -> drawScreenFlowVsWireframe(canvas);
            case "screen-flow-common-errors" -> drawScreenFlowCommonErrors(canvas);
            default -> {
                return false;
            }
        }
        return true;
    }

    private static void drawModuleMap(Pane p) {
        rectangle(p, 35, 35, 105, 45, "Matrícula");
        rectangle(p, 190, 35, 105, 45, "Estudiantes");
        rectangle(p, 335, 35, 105, 45, "Reportes");
        rectangle(p, 112, 110, 105, 45, "Docentes");
        rectangle(p, 265, 110, 105, 45, "Notas");
        arrow(p, 140, 58, 190, 58);
        arrow(p, 295, 58, 335, 58);
        arrow(p, 90, 80, 145, 110);
        arrow(p, 242, 80, 300, 110);
    }


    private static void drawAdminModuleOverview(Pane p) {
        rectangle(p, 170, 62, 130, 48, "Sistema\nadmin");
        rectangle(p, 22, 24, 88, 34, "Clientes");
        rectangle(p, 22, 112, 88, 34, "Pagos");
        rectangle(p, 190, 18, 92, 34, "Reparac.");
        rectangle(p, 355, 24, 92, 34, "Inventario");
        rectangle(p, 355, 112, 92, 34, "Reportes");
        arrow(p, 110, 42, 170, 76);
        arrow(p, 110, 129, 170, 96);
        arrow(p, 236, 52, 236, 62);
        arrow(p, 355, 42, 300, 76);
        arrow(p, 355, 129, 300, 96);
        text(p, "Módulos = zonas funcionales del sistema", 116, 155);
    }

    private static void drawAdminModuleHierarchy(Pane p) {
        rectangle(p, 165, 20, 140, 36, "Reparaciones");
        rectangle(p, 36, 88, 92, 32, "Recepción");
        rectangle(p, 145, 88, 92, 32, "Diagnóstico");
        rectangle(p, 254, 88, 92, 32, "Entrega");
        rectangle(p, 363, 88, 80, 32, "Garantía");
        arrow(p, 235, 56, 82, 88);
        arrow(p, 235, 56, 191, 88);
        arrow(p, 235, 56, 300, 88);
        arrow(p, 235, 56, 403, 88);
        text(p, "Módulo → submódulos → funciones concretas", 93, 150);
    }

    private static void drawAdminModuleDependencies(Pane p) {
        rectangle(p, 25, 38, 95, 38, "Clientes");
        rectangle(p, 25, 104, 95, 38, "Equipos");
        rectangle(p, 188, 68, 115, 45, "Reparaciones");
        rectangle(p, 365, 38, 82, 38, "Pagos");
        rectangle(p, 365, 104, 82, 38, "Reportes");
        arrow(p, 120, 57, 188, 82);
        arrow(p, 120, 123, 188, 99);
        arrow(p, 303, 89, 365, 57);
        arrow(p, 303, 89, 365, 123);
        text(p, "Una dependencia indica uso funcional de información", 72, 158);
    }

    private static void drawAdminModulePhases(Pane p) {
        rectangle(p, 20, 25, 120, 120, "");
        rectangle(p, 175, 25, 120, 120, "");
        rectangle(p, 330, 25, 120, 120, "");
        text(p, "MVP", 62, 50);
        text(p, "Versión 1", 207, 50);
        text(p, "Futuro", 369, 50);
        text(p, "Clientes", 45, 82);
        text(p, "Reparaciones", 35, 108);
        text(p, "Pagos", 55, 134);
        text(p, "Inventario", 205, 82);
        text(p, "Garantías", 206, 108);
        text(p, "Reportes", 210, 134);
        text(p, "Facturación", 354, 82);
        text(p, "BI / IA", 370, 108);
        text(p, "Automatización", 344, 134);
    }

    private static void drawAdminModuleTransversal(Pane p) {
        rectangle(p, 35, 35, 95, 42, "Clientes");
        rectangle(p, 187, 35, 110, 42, "Reparaciones");
        rectangle(p, 355, 35, 82, 42, "Pagos");
        rectangle(p, 35, 105, 402, 42, "Usuarios, permisos, auditoría y configuración");
        arrow(p, 82, 77, 95, 105);
        arrow(p, 242, 77, 242, 105);
        arrow(p, 396, 77, 382, 105);
        text(p, "Los módulos transversales atraviesan áreas de negocio", 65, 162);
    }

    private static void drawAdminModuleVsScreen(Pane p) {
        rectangle(p, 35, 45, 120, 60, "Módulo\nClientes");
        rectangle(p, 250, 20, 160, 32, "Listado clientes");
        rectangle(p, 250, 70, 160, 32, "Formulario cliente");
        rectangle(p, 250, 120, 160, 32, "Detalle cliente");
        arrow(p, 155, 75, 250, 36);
        arrow(p, 155, 75, 250, 86);
        arrow(p, 155, 75, 250, 136);
        text(p, "Un módulo puede tener varias pantallas", 105, 162);
    }

    private static void drawAdminModuleCommonErrors(Pane p) {
        rectangle(p, 18, 38, 110, 42, "Botón\nGuardar");
        text(p, "✕", 60, 104);
        rectangle(p, 180, 38, 110, 42, "tabla\nclientes");
        text(p, "✕", 222, 104);
        rectangle(p, 342, 38, 110, 42, "Pantalla\nListado");
        text(p, "✕", 384, 104);
        text(p, "Mejor: Clientes, Reparaciones, Pagos, Inventario", 58, 145);
    }

    private static void drawRolesPermissions(Pane p) {
        rectangle(p, 28, 58, 120, 52, "Rol\nRecepción");
        rectangle(p, 185, 58, 125, 52, "Permiso\ncrear orden");
        rectangle(p, 350, 58, 95, 52, "Recurso\nOrden");
        arrow(p, 148, 84, 185, 84);
        arrow(p, 310, 84, 350, 84);
        label(p, "La autorización se lee como: quién puede hacer qué sobre qué.", 55, 137);
    }

    private static void drawRolesPermissionsUserRolePermission(Pane p) {
        rectangle(p, 22, 54, 92, 48, "Usuario\nAna");
        rectangle(p, 142, 54, 98, 48, "Rol\nRecepción");
        rectangle(p, 268, 30, 150, 36, "clientes.crear");
        rectangle(p, 268, 84, 150, 36, "reparaciones.crear");
        arrow(p, 114, 78, 142, 78);
        arrow(p, 240, 72, 268, 48);
        arrow(p, 240, 86, 268, 102);
        text(p, "Un usuario hereda autorizaciones desde sus roles", 82, 145);
    }

    private static void drawRolesPermissionsMatrix(Pane p) {
        rectangle(p, 25, 26, 420, 118, "");
        line(p, 25, 54, 445, 54);
        line(p, 168, 26, 168, 144);
        line(p, 250, 26, 250, 144);
        line(p, 332, 26, 332, 144);
        text(p, "Permiso", 72, 45);
        text(p, "Admin", 190, 45);
        text(p, "Recep.", 270, 45);
        text(p, "Téc.", 360, 45);
        text(p, "crear orden", 45, 78);
        text(p, "✓", 205, 78);
        text(p, "✓", 287, 78);
        text(p, "—", 370, 78);
        text(p, "diagnóstico", 45, 108);
        text(p, "✓", 205, 108);
        text(p, "—", 287, 108);
        text(p, "✓", 370, 108);
        text(p, "anular pago", 45, 136);
        text(p, "✓", 205, 136);
        text(p, "—", 287, 136);
        text(p, "—", 370, 136);
    }

    private static void drawRolesPermissionsModuleActions(Pane p) {
        rectangle(p, 35, 26, 140, 118, "Módulo\nReparaciones");
        rectangle(p, 235, 20, 170, 28, "ver órdenes");
        rectangle(p, 235, 56, 170, 28, "crear orden");
        rectangle(p, 235, 92, 170, 28, "registrar diagnóstico");
        rectangle(p, 235, 128, 170, 28, "anular orden");
        arrow(p, 175, 63, 235, 34);
        arrow(p, 175, 72, 235, 70);
        arrow(p, 175, 83, 235, 106);
        arrow(p, 175, 92, 235, 142);
    }

    private static void drawRolesPermissionsStateBased(Pane p) {
        rectangle(p, 22, 42, 95, 42, "Recibida");
        rectangle(p, 148, 42, 112, 42, "Diagnosticada");
        rectangle(p, 300, 42, 125, 42, "Entregada");
        arrow(p, 117, 63, 148, 63);
        arrow(p, 260, 63, 300, 63);
        text(p, "Recepción edita", 26, 112);
        text(p, "Técnico diagnostica", 144, 112);
        text(p, "Solo consulta", 315, 112);
        text(p, "El permiso puede depender del estado del registro", 75, 150);
    }

    private static void drawRolesPermissionsCommonErrors(Pane p) {
        rectangle(p, 18, 35, 115, 44, "Usuario\nnormal");
        text(p, "✕", 66, 102);
        rectangle(p, 180, 35, 115, 44, "Acceso\ntotal");
        text(p, "✕", 228, 102);
        rectangle(p, 342, 35, 115, 44, "Borrar\npagos");
        text(p, "✕", 390, 102);
        text(p, "Mejor: roles funcionales + permisos precisos + auditoría", 45, 145);
    }

    private static void drawScreenFlow(Pane p) {
        screen(p, 30, 35, "Login");
        screen(p, 185, 35, "Dashboard");
        screen(p, 340, 35, "Matrícula");
        screen(p, 185, 112, "Reportes");
        arrow(p, 125, 65, 185, 65);
        arrow(p, 280, 65, 340, 65);
        arrow(p, 235, 92, 235, 112);
    }


    private static void drawScreenFlowBasic(Pane p) {
        screen(p, 25, 55, "Login");
        screen(p, 140, 55, "Dashboard");
        screen(p, 260, 55, "Órdenes");
        screen(p, 375, 55, "Detalle");
        arrow(p, 120, 83, 140, 83);
        arrow(p, 235, 83, 260, 83);
        arrow(p, 355, 83, 375, 83);
        text(p, "iniciar", 120, 72);
        text(p, "abrir módulo", 230, 72);
        text(p, "seleccionar", 344, 72);
        text(p, "La navegación avanza por acciones claras", 92, 140);
    }

    private static void drawScreenFlowBranching(Pane p) {
        screen(p, 35, 58, "Listado\nórdenes");
        screen(p, 190, 20, "Crear\norden");
        screen(p, 190, 95, "Detalle\norden");
        screen(p, 345, 58, "Exportar\nreporte");
        arrow(p, 130, 78, 190, 45);
        arrow(p, 130, 92, 190, 122);
        arrow(p, 285, 123, 345, 92);
        text(p, "Nuevo", 142, 47);
        text(p, "seleccionar", 130, 125);
        text(p, "exportar", 310, 122);
        text(p, "Una pantalla puede tener varias salidas", 105, 158);
    }

    private static void drawScreenFlowByRole(Pane p) {
        text(p, "Recepción", 24, 35);
        screen(p, 20, 50, "Crear\norden");
        text(p, "Técnico", 190, 35);
        screen(p, 180, 50, "Diagnóstico");
        text(p, "Caja", 358, 35);
        screen(p, 340, 50, "Pago");
        arrow(p, 115, 78, 180, 78);
        arrow(p, 275, 78, 340, 78);
        text(p, "mismo proceso, recorridos distintos por rol", 88, 137);
    }

    private static void drawScreenFlowErrorValidation(Pane p) {
        screen(p, 28, 58, "Formulario");
        screen(p, 190, 20, "Confirmación");
        screen(p, 190, 98, "Errores");
        arrow(p, 123, 75, 190, 48);
        arrow(p, 123, 95, 190, 126);
        arrow(p, 190, 126, 123, 95);
        text(p, "datos válidos", 124, 46);
        text(p, "datos inválidos", 125, 128);
        text(p, "No todo flujo termina en éxito", 118, 158);
    }

    private static void drawScreenFlowVsWireframe(Pane p) {
        text(p, "Flujo", 55, 26);
        screen(p, 20, 45, "Listado");
        screen(p, 140, 45, "Formulario");
        arrow(p, 115, 73, 140, 73);
        text(p, "Wireframe", 305, 26);
        rectangle(p, 300, 45, 130, 92, "");
        line(p, 300, 68, 430, 68);
        rectangle(p, 315, 82, 92, 16, "Campo");
        rectangle(p, 315, 108, 42, 16, "Btn");
        text(p, "conecta pantallas", 42, 145);
        text(p, "dibuja contenido", 300, 145);
    }

    private static void drawScreenFlowCommonErrors(Pane p) {
        screen(p, 20, 42, "Cliente\nentrega");
        text(p, "✕", 62, 118);
        screen(p, 180, 42, "Caja sin\nacción");
        text(p, "✕", 224, 118);
        screen(p, 340, 42, "Botones\ndetallados");
        text(p, "✕", 386, 118);
        text(p, "Mejor: pantallas + acciones + retornos + errores", 55, 152);
    }
}
