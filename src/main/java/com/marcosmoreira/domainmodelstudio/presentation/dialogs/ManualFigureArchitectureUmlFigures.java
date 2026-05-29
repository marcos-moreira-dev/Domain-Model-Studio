package com.marcosmoreira.domainmodelstudio.presentation.dialogs;

import static com.marcosmoreira.domainmodelstudio.presentation.dialogs.ManualFigureDrawingSupport.*;

import javafx.scene.layout.Pane;
import javafx.scene.shape.*;

/** Figuras didácticas agrupadas para mantener revisable el factory principal. */
final class ManualFigureArchitectureUmlFigures {

    private ManualFigureArchitectureUmlFigures() {
    }

    static boolean draw(String figureId, Pane canvas) {
        switch (figureId) {
            case "c4-context-containers" -> drawC4(canvas);
            case "c4-context-basic" -> drawC4ContextBasic(canvas);
            case "c4-context-boundary" -> drawC4ContextBoundary(canvas);
            case "c4-context-people-systems" -> drawC4ContextPeopleSystems(canvas);
            case "c4-context-relationships" -> drawC4ContextRelationships(canvas);
            case "c4-context-scope-future" -> drawC4ContextScopeFuture(canvas);
            case "c4-context-small-business" -> drawC4ContextSmallBusiness(canvas);
            case "c4-context-common-errors" -> drawC4ContextCommonErrors(canvas);
            case "c4-containers-basic-desktop-api-db" -> drawC4ContainersBasicDesktopApiDb(canvas);
            case "c4-containers-web-api-db" -> drawC4ContainersWebApiDb(canvas);
            case "c4-containers-services" -> drawC4ContainersServices(canvas);
            case "c4-containers-context-vs-containers" -> drawC4ContainersContextVsContainers(canvas);
            case "c4-containers-common-errors" -> drawC4ContainersCommonErrors(canvas);
            case "technical-deployment-nodes" -> drawDeployment(canvas);
            case "technical-deployment-basic-web" -> drawTechnicalDeploymentBasicWeb(canvas);
            case "technical-deployment-desktop-centralized" -> drawTechnicalDeploymentDesktopCentralized(canvas);
            case "technical-deployment-dev-vs-prod" -> drawTechnicalDeploymentDevVsProd(canvas);
            case "technical-deployment-backups" -> drawTechnicalDeploymentBackups(canvas);
            case "technical-deployment-local-vs-cloud" -> drawTechnicalDeploymentLocalVsCloud(canvas);
            case "technical-deployment-common-errors" -> drawTechnicalDeploymentCommonErrors(canvas);
            case "uml-use-case-actor" -> drawUseCase(canvas);
            case "uml-class-compartments" -> drawUmlClass(canvas);
            case "uml-activity-flow" -> drawActivity(canvas);
            case "uml-sequence-lifeline" -> drawSequence(canvas);
            case "uml-state-transition" -> drawState(canvas);
            default -> {
                return false;
            }
        }
        return true;
    }

    private static void drawC4ContextBasic(Pane p) {
        stickPerson(p, 48, 70, "Recepción");
        stickPerson(p, 48, 132, "Técnico");
        rectangle(p, 160, 55, 135, 62, "Sistema\nadministrativo");
        rectangle(p, 350, 35, 95, 42, "WhatsApp");
        rectangle(p, 350, 112, 95, 42, "Facturación");
        arrow(p, 76, 70, 160, 75);
        arrow(p, 76, 132, 160, 98);
        arrow(p, 295, 73, 350, 56);
        arrow(p, 295, 100, 350, 133);
        text(p, "registra", 104, 62);
        text(p, "diagnostica", 95, 128);
    }

    private static void drawC4ContextBoundary(Pane p) {
        rectangle(p, 105, 25, 210, 120, "");
        text(p, "Dentro del sistema", 138, 45);
        rectangle(p, 150, 66, 120, 42, "Sistema\nadministrativo");
        rectangle(p, 18, 65, 70, 42, "Cliente");
        rectangle(p, 350, 38, 95, 38, "WhatsApp");
        rectangle(p, 350, 105, 95, 38, "Banco");
        arrow(p, 88, 86, 150, 86);
        arrow(p, 270, 78, 350, 57);
        arrow(p, 270, 97, 350, 124);
        text(p, "Fuera", 25, 132);
        text(p, "Sistemas externos", 338, 158);
    }

    private static void drawC4ContextPeopleSystems(Pane p) {
        text(p, "Personas", 55, 30);
        stickPerson(p, 60, 82, "Caja");
        stickPerson(p, 155, 82, "Dueño");
        text(p, "Sistemas externos", 295, 30);
        rectangle(p, 285, 58, 120, 38, "Facturación");
        rectangle(p, 285, 112, 120, 38, "Correo / API");
        text(p, "humanos", 82, 155);
        text(p, "software ajeno", 300, 155);
    }

    private static void drawC4ContextRelationships(Pane p) {
        stickPerson(p, 55, 82, "Caja");
        rectangle(p, 170, 58, 130, 52, "Sistema\nadministrativo");
        rectangle(p, 360, 58, 88, 52, "Pasarela\npago");
        arrow(p, 82, 82, 170, 82);
        arrow(p, 300, 82, 360, 82);
        text(p, "registra pago", 95, 72);
        text(p, "confirma pago", 305, 72);
        text(p, "La flecha debe explicar el propósito", 100, 142);
    }

    private static void drawC4ContextScopeFuture(Pane p) {
        rectangle(p, 25, 35, 110, 42, "Actual\nÓrdenes");
        rectangle(p, 180, 35, 110, 42, "Futuro\nFacturación");
        rectangle(p, 335, 35, 110, 42, "Fuera\nBanco");
        rectangle(p, 125, 110, 220, 36, "Sistema administrativo");
        arrow(p, 80, 77, 155, 110);
        arrow(p, 235, 77, 235, 110);
        arrow(p, 390, 77, 315, 110);
        text(p, "marcar alcance evita promesas ambiguas", 88, 160);
    }

    private static void drawC4ContextSmallBusiness(Pane p) {
        stickPerson(p, 45, 52, "Admin");
        stickPerson(p, 45, 123, "Técnico");
        rectangle(p, 150, 52, 135, 64, "Sistema\nreparaciones");
        rectangle(p, 345, 22, 100, 34, "WhatsApp");
        rectangle(p, 345, 75, 100, 34, "Facturación");
        rectangle(p, 345, 128, 100, 34, "Reportes\ncontador");
        arrow(p, 72, 52, 150, 70);
        arrow(p, 72, 123, 150, 100);
        arrow(p, 285, 65, 345, 39);
        arrow(p, 285, 84, 345, 92);
        arrow(p, 285, 103, 345, 145);
    }

    private static void drawC4ContextCommonErrors(Pane p) {
        text(p, "MAL", 42, 30);
        rectangle(p, 28, 48, 108, 30, "Tabla pagos");
        rectangle(p, 28, 88, 108, 30, "Clase Cliente");
        rectangle(p, 28, 128, 108, 30, "Botón guardar");
        text(p, "BIEN", 245, 30);
        stickPerson(p, 240, 85, "Usuario");
        rectangle(p, 330, 58, 105, 48, "Sistema");
        arrow(p, 267, 85, 330, 82);
        text(p, "externos + límite", 315, 138);
    }

    private static void drawC4(Pane p) {
        stickPerson(p, 55, 78, "Usuario");
        rectangle(p, 145, 45, 115, 70, "Sistema\nescolar");
        rectangle(p, 315, 35, 120, 48, "Backend/API");
        cylinder(p, 350, 120, "BD");
        arrow(p, 82, 78, 145, 78);
        arrow(p, 260, 68, 315, 59);
        arrow(p, 375, 83, 375, 103);
    }

    private static void drawC4ContainersBasicDesktopApiDb(Pane p) {
        rectangle(p, 28, 56, 110, 52, "Desktop\nJavaFX");
        rectangle(p, 185, 50, 120, 64, "Backend\nAPI");
        cylinder(p, 390, 82, "PostgreSQL");
        arrow(p, 138, 82, 185, 82);
        arrow(p, 305, 82, 358, 82);
        text(p, "HTTPS / JSON", 133, 70);
        text(p, "SQL", 326, 70);
        text(p, "Contenedores C4: piezas principales de software", 70, 145);
    }

    private static void drawC4ContainersWebApiDb(Pane p) {
        rectangle(p, 28, 56, 110, 52, "Frontend\nAngular");
        rectangle(p, 182, 50, 125, 64, "Backend\nAPI");
        cylinder(p, 392, 82, "BD");
        arrow(p, 138, 82, 182, 82);
        arrow(p, 307, 82, 360, 82);
        text(p, "HTTP", 148, 70);
        text(p, "SQL", 326, 70);
        text(p, "La interfaz no es todo el sistema", 122, 145);
    }

    private static void drawC4ContainersServices(Pane p) {
        rectangle(p, 25, 58, 95, 48, "Desktop\nWeb");
        rectangle(p, 170, 48, 110, 68, "Backend\nAPI");
        cylinder(p, 380, 45, "BD");
        rectangle(p, 345, 92, 92, 32, "Reportes");
        rectangle(p, 345, 132, 92, 32, "Archivos");
        arrow(p, 120, 82, 170, 82);
        arrow(p, 280, 70, 348, 52);
        arrow(p, 280, 86, 345, 108);
        arrow(p, 280, 102, 345, 148);
        text(p, "servicios auxiliares", 165, 150);
    }

    private static void drawC4ContainersContextVsContainers(Pane p) {
        text(p, "Contexto", 42, 28);
        stickPerson(p, 42, 86, "Usuario");
        rectangle(p, 130, 62, 82, 42, "Sistema");
        arrow(p, 70, 86, 130, 84);
        text(p, "Contenedores", 275, 28);
        rectangle(p, 260, 52, 70, 36, "UI");
        rectangle(p, 350, 52, 70, 36, "API");
        cylinder(p, 385, 125, "BD");
        arrow(p, 330, 70, 350, 70);
        arrow(p, 385, 88, 385, 105);
        text(p, "entorno", 110, 132);
        text(p, "piezas internas", 300, 156);
    }

    private static void drawC4ContainersCommonErrors(Pane p) {
        text(p, "MAL", 42, 28);
        rectangle(p, 25, 45, 105, 30, "Clase Pago");
        rectangle(p, 25, 85, 105, 30, "Botón guardar");
        rectangle(p, 25, 125, 105, 30, "Docker = C4");
        text(p, "BIEN", 245, 28);
        rectangle(p, 225, 50, 92, 42, "Frontend");
        rectangle(p, 345, 50, 92, 42, "Backend");
        cylinder(p, 390, 130, "BD");
        arrow(p, 317, 72, 345, 72);
        arrow(p, 390, 92, 390, 110);
    }

    private static void drawDeployment(Pane p) {
        node(p, 38, 45, 112, 70, "PC cliente");
        node(p, 192, 45, 112, 70, "Servidor app");
        node(p, 346, 45, 95, 70, "PostgreSQL");
        arrow(p, 150, 80, 192, 80);
        arrow(p, 304, 80, 346, 80);
        text(p, "HTTP", 161, 70);
        text(p, "TCP", 318, 70);
    }


    private static void drawTechnicalDeploymentBasicWeb(Pane p) {
        stickPerson(p, 45, 82, "Usuario");
        node(p, 130, 50, 105, 58, "Servidor\nAPI");
        cylinder(p, 328, 82, "PostgreSQL");
        arrow(p, 72, 82, 130, 80);
        arrow(p, 235, 80, 296, 82);
        text(p, "HTTPS", 86, 70);
        text(p, "SQL interno", 246, 70);
        rectangle(p, 360, 42, 82, 36, "Dominio\nTLS");
        arrow(p, 400, 78, 400, 105);
        text(p, "usuario -> servidor -> datos", 128, 150);
    }

    private static void drawTechnicalDeploymentDesktopCentralized(Pane p) {
        node(p, 20, 32, 100, 45, "PC sucursal\nA");
        node(p, 20, 108, 100, 45, "PC sucursal\nB");
        node(p, 180, 62, 118, 58, "Backend\ncentral");
        cylinder(p, 392, 92, "BD");
        arrow(p, 120, 55, 180, 78);
        arrow(p, 120, 130, 180, 102);
        arrow(p, 298, 92, 360, 92);
        text(p, "HTTPS", 132, 58);
        text(p, "HTTPS", 132, 127);
        text(p, "varias sedes, datos centralizados", 95, 160);
    }

    private static void drawTechnicalDeploymentDevVsProd(Pane p) {
        rectangle(p, 24, 28, 180, 112, "");
        text(p, "Desarrollo", 75, 50);
        node(p, 45, 78, 58, 34, "Laptop");
        cylinder(p, 150, 100, "BD local");
        arrow(p, 103, 95, 118, 98);
        rectangle(p, 260, 28, 180, 112, "");
        text(p, "Producción", 312, 50);
        node(p, 280, 78, 62, 34, "Servidor");
        cylinder(p, 392, 100, "BD real");
        arrow(p, 342, 95, 360, 98);
        text(p, "no probar cambios peligrosos directo en producción", 78, 160);
    }

    private static void drawTechnicalDeploymentBackups(Pane p) {
        cylinder(p, 86, 68, "BD prod");
        rectangle(p, 188, 42, 112, 52, "Backup\ndiario");
        rectangle(p, 348, 42, 96, 52, "Almacén\nexterno");
        node(p, 210, 124, 92, 34, "Prueba\nrestore");
        arrow(p, 118, 68, 188, 66);
        arrow(p, 300, 66, 348, 66);
        arrow(p, 396, 94, 302, 134);
        text(p, "respaldo", 136, 55);
        text(p, "restaurar también se prueba", 122, 162);
    }

    private static void drawTechnicalDeploymentLocalVsCloud(Pane p) {
        text(p, "Local", 45, 28);
        node(p, 25, 50, 92, 42, "PC + BD");
        text(p, "Servidor local", 168, 28);
        node(p, 165, 50, 98, 42, "LAN\nServidor");
        text(p, "Centralizado", 320, 28);
        node(p, 320, 48, 96, 44, "Backend\nnube");
        cylinder(p, 370, 132, "BD");
        arrow(p, 368, 92, 368, 112);
        text(p, "crece de simple a multi-sucursal", 112, 160);
    }

    private static void drawTechnicalDeploymentCommonErrors(Pane p) {
        text(p, "MAL", 34, 28);
        node(p, 25, 50, 92, 38, "PC cliente");
        cylinder(p, 150, 70, "BD expuesta");
        arrow(p, 117, 68, 118, 68);
        rectangle(p, 26, 112, 124, 34, "Sin backups");
        text(p, "BIEN", 260, 28);
        node(p, 240, 48, 76, 36, "Cliente");
        node(p, 348, 48, 76, 36, "API");
        cylinder(p, 386, 126, "BD");
        arrow(p, 316, 66, 348, 66);
        arrow(p, 386, 84, 386, 106);
        text(p, "API + permisos + respaldo", 270, 158);
    }

    private static void drawUseCase(Pane p) {
        stickPerson(p, 60, 85, "Actor");
        rectangle(p, 155, 32, 270, 112, "");
        text(p, "Sistema", 168, 52);
        ellipseLike(p, 220, 64, 145, 36, "Registrar matrícula");
        ellipseLike(p, 220, 105, 145, 36, "Consultar notas");
        arrow(p, 88, 83, 220, 82);
    }

    private static void drawUmlClass(Pane p) {
        rectangle(p, 135, 25, 200, 125, "");
        line(p, 135, 58, 335, 58);
        line(p, 135, 96, 335, 96);
        text(p, "Estudiante", 205, 47);
        text(p, "- nombre: texto", 155, 82);
        text(p, "- estado: catálogo", 155, 116);
        text(p, "+ matricular()", 155, 138);
    }

    private static void drawActivity(Pane p) {
        circle(p, 45, 82, 14, "");
        rounded(p, 93, 60, 115, 44, "Capturar datos");
        diamond(p, 270, 82, 50, 38, "?");
        rounded(p, 335, 60, 92, 44, "Guardar");
        arrow(p, 59, 82, 93, 82);
        arrow(p, 208, 82, 245, 82);
        arrow(p, 295, 82, 335, 82);
        circle(p, 444, 82, 14, "");
        arrow(p, 427, 82, 430, 82);
    }

    private static void drawSequence(Pane p) {
        participant(p, 40, "Actor");
        participant(p, 165, "Pantalla");
        participant(p, 290, "Servicio");
        participant(p, 405, "BD");
        arrow(p, 70, 68, 195, 68);
        arrow(p, 195, 98, 320, 98);
        arrow(p, 320, 128, 435, 128);
        text(p, "solicita", 105, 60);
        text(p, "valida", 235, 90);
        text(p, "consulta", 355, 120);
    }

    private static void drawState(Pane p) {
        circle(p, 35, 82, 13, "");
        rounded(p, 80, 60, 105, 44, "Pendiente");
        rounded(p, 235, 60, 95, 44, "Activa");
        rounded(p, 380, 60, 70, 44, "Final");
        arrow(p, 48, 82, 80, 82);
        arrow(p, 185, 82, 235, 82);
        arrow(p, 330, 82, 380, 82);
        text(p, "aprobar", 194, 72);
        text(p, "cerrar", 340, 72);
    }
}
