package com.marcosmoreira.domainmodelstudio.presentation.dialogs;

import static com.marcosmoreira.domainmodelstudio.presentation.dialogs.ManualFigureDrawingSupport.*;

import javafx.scene.layout.Pane;
import javafx.scene.shape.*;

/** Figuras didácticas agrupadas para mantener revisable el factory principal. */
final class ManualFigureProcessFigures {

    private ManualFigureProcessFigures() {
    }

    static boolean draw(String figureId, Pane canvas) {
        switch (figureId) {
            case "bpmn-basic-symbols" -> drawBpmn(canvas);
            case "bpmn-basic-linear-process" -> drawBpmnLinearProcess(canvas);
            case "bpmn-basic-gateway" -> drawBpmnGateway(canvas);
            case "bpmn-basic-lanes" -> drawBpmnLanes(canvas);
            case "bpmn-basic-as-is-to-be" -> drawBpmnAsIsToBe(canvas);
            case "bpmn-basic-common-errors" -> drawBpmnCommonErrors(canvas);
            case "operational-flow-swimlane" -> drawOperationalFlow(canvas);
            case "operational-flow-linear" -> drawOperationalFlowLinear(canvas);
            case "operational-flow-decision" -> drawOperationalFlowDecision(canvas);
            case "operational-flow-current-vs-proposed" -> drawOperationalFlowCurrentVsProposed(canvas);
            case "operational-flow-vs-formal" -> drawOperationalFlowVsFormal(canvas);
            case "operational-flow-common-errors" -> drawOperationalFlowCommonErrors(canvas);
            default -> {
                return false;
            }
        }
        return true;
    }

    private static void drawBpmn(Pane p) {
        circle(p, 45, 80, 22, "Inicio");
        rounded(p, 110, 58, 115, 44, "Tarea");
        diamond(p, 285, 80, 50, 38, "?");
        rounded(p, 350, 58, 85, 44, "Fin");
        arrow(p, 67, 80, 110, 80);
        arrow(p, 225, 80, 260, 80);
        arrow(p, 310, 80, 350, 80);
        label(p, "Evento → actividad → compuerta → salida", 115, 132);
    }

    private static void drawBpmnLinearProcess(Pane p) {
        circle(p, 38, 82, 16, "");
        rounded(p, 82, 62, 88, 38, "Registrar\norden");
        rounded(p, 205, 62, 88, 38, "Diagnosticar");
        rounded(p, 328, 62, 88, 38, "Entregar");
        circle(p, 448, 82, 16, "");
        circle(p, 448, 82, 11, "");
        arrow(p, 54, 82, 82, 82);
        arrow(p, 170, 82, 205, 82);
        arrow(p, 293, 82, 328, 82);
        arrow(p, 416, 82, 432, 82);
        text(p, "Inicio", 18, 122);
        text(p, "Fin", 438, 122);
    }

    private static void drawBpmnGateway(Pane p) {
        rounded(p, 35, 62, 95, 38, "Diagnosticar");
        diamond(p, 225, 82, 92, 52, "¿Aprueba?");
        rounded(p, 350, 34, 92, 34, "Reparar");
        rounded(p, 350, 113, 92, 34, "Cerrar\nrechazo");
        arrow(p, 130, 82, 179, 82);
        arrow(p, 271, 74, 350, 51);
        arrow(p, 271, 91, 350, 130);
        text(p, "Sí", 310, 52);
        text(p, "No", 310, 126);
        text(p, "Cada salida debe tener condición", 106, 158);
    }

    private static void drawBpmnLanes(Pane p) {
        rectangle(p, 22, 22, 425, 128, "");
        line(p, 22, 54, 447, 54);
        line(p, 22, 86, 447, 86);
        line(p, 22, 118, 447, 118);
        line(p, 98, 22, 98, 150);
        text(p, "Recepción", 34, 43);
        text(p, "Técnico", 42, 75);
        text(p, "Caja", 50, 107);
        text(p, "Cliente", 42, 139);
        rounded(p, 120, 29, 78, 20, "Orden");
        rounded(p, 225, 61, 82, 20, "Diagnóstico");
        rounded(p, 335, 93, 76, 20, "Cobro");
        diamond(p, 190, 134, 58, 24, "¿Sí?");
        arrow(p, 198, 39, 225, 71);
        arrow(p, 307, 71, 335, 103);
        arrow(p, 230, 134, 335, 103);
    }

    private static void drawBpmnAsIsToBe(Pane p) {
        text(p, "AS-IS", 35, 30);
        rounded(p, 35, 45, 85, 28, "Cuaderno");
        rounded(p, 145, 45, 85, 28, "WhatsApp");
        rounded(p, 255, 45, 85, 28, "Memoria");
        arrow(p, 120, 59, 145, 59);
        arrow(p, 230, 59, 255, 59);
        text(p, "TO-BE", 35, 105);
        rounded(p, 35, 120, 95, 28, "Orden\nsistema");
        rounded(p, 155, 120, 95, 28, "Estado\nvisible");
        rounded(p, 275, 120, 95, 28, "Reporte");
        arrow(p, 130, 134, 155, 134);
        arrow(p, 250, 134, 275, 134);
    }

    private static void drawBpmnCommonErrors(Pane p) {
        text(p, "MAL", 42, 30);
        rounded(p, 35, 45, 105, 28, "Proceso");
        diamond(p, 88, 94, 56, 28, "?");
        rounded(p, 35, 125, 105, 28, "Pantalla");
        arrow(p, 88, 73, 88, 80);
        arrow(p, 88, 108, 88, 125);
        text(p, "BIEN", 235, 30);
        rounded(p, 220, 45, 135, 28, "Registrar orden");
        diamond(p, 288, 94, 86, 38, "¿Aprueba?");
        rounded(p, 220, 125, 135, 28, "Registrar pago");
        arrow(p, 288, 73, 288, 75);
        arrow(p, 288, 113, 288, 125);
        text(p, "verbos + condiciones", 350, 96);
    }

    private static void drawOperationalFlowLinear(Pane p) {
        rounded(p, 28, 62, 82, 38, "Recibir\nequipo");
        rounded(p, 132, 62, 82, 38, "Crear\norden");
        rounded(p, 236, 62, 82, 38, "Diagnosticar");
        rounded(p, 340, 62, 95, 38, "Entregar");
        arrow(p, 110, 81, 132, 81);
        arrow(p, 214, 81, 236, 81);
        arrow(p, 318, 81, 340, 81);
        text(p, "Secuencia clara de pasos reales del negocio", 92, 132);
    }

    private static void drawOperationalFlowDecision(Pane p) {
        rounded(p, 35, 62, 95, 38, "Diagnosticar");
        diamond(p, 225, 82, 86, 48, "¿Aprueba?");
        rounded(p, 338, 38, 100, 34, "Reparar");
        rounded(p, 338, 112, 100, 34, "Cerrar\nrechazo");
        arrow(p, 130, 82, 182, 82);
        arrow(p, 268, 78, 338, 55);
        arrow(p, 268, 88, 338, 129);
        text(p, "Sí", 300, 58);
        text(p, "No", 300, 122);
    }

    private static void drawOperationalFlowCurrentVsProposed(Pane p) {
        text(p, "AS-IS", 38, 34);
        rounded(p, 35, 48, 92, 30, "Cuaderno");
        rounded(p, 150, 48, 92, 30, "WhatsApp");
        rounded(p, 265, 48, 92, 30, "Memoria");
        arrow(p, 127, 63, 150, 63);
        arrow(p, 242, 63, 265, 63);
        text(p, "TO-BE", 38, 108);
        rounded(p, 35, 122, 92, 30, "Orden\nsistema");
        rounded(p, 150, 122, 92, 30, "Técnico\nasignado");
        rounded(p, 265, 122, 105, 30, "Notificación");
        arrow(p, 127, 137, 150, 137);
        arrow(p, 242, 137, 265, 137);
    }

    private static void drawOperationalFlowVsFormal(Pane p) {
        rounded(p, 24, 36, 112, 35, "Flujo\noperativo");
        text(p, "relato del negocio", 27, 92);
        rounded(p, 180, 36, 94, 35, "BPMN");
        circle(p, 194, 112, 13, "");
        rounded(p, 220, 100, 54, 26, "Tarea");
        diamond(p, 305, 113, 40, 28, "?");
        arrow(p, 207, 112, 220, 112);
        arrow(p, 274, 112, 285, 112);
        rounded(p, 360, 36, 90, 35, "Pantallas");
        screen(p, 350, 92, "Login");
        screen(p, 350, 128, "Orden");
        arrow(p, 398, 112, 398, 128);
    }

    private static void drawOperationalFlowCommonErrors(Pane p) {
        text(p, "MAL", 40, 32);
        rounded(p, 35, 48, 100, 30, "Gestionar");
        rounded(p, 35, 88, 100, 30, "Proceso");
        rounded(p, 35, 128, 100, 30, "Cosas");
        text(p, "BIEN", 230, 32);
        rounded(p, 220, 48, 125, 30, "Registrar orden");
        rounded(p, 220, 88, 125, 30, "Diagnosticar");
        rounded(p, 220, 128, 125, 30, "Registrar pago");
        arrow(p, 135, 88, 205, 88);
        text(p, "pasos concretos", 355, 92);
    }

    private static void drawOperationalFlow(Pane p) {
        rectangle(p, 25, 25, 420, 125, "");
        line(p, 25, 68, 445, 68);
        line(p, 25, 110, 445, 110);
        text(p, "Secretaría", 35, 52);
        text(p, "Docente", 35, 95);
        text(p, "Dirección", 35, 137);
        rounded(p, 130, 38, 82, 26, "Recibe");
        rounded(p, 240, 80, 82, 26, "Revisa");
        rounded(p, 350, 122, 82, 24, "Aprueba");
        arrow(p, 212, 52, 240, 92);
        arrow(p, 322, 93, 350, 134);
    }


}
