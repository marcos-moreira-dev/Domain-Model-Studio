package com.marcosmoreira.domainmodelstudio.presentation.dialogs;

import static com.marcosmoreira.domainmodelstudio.presentation.dialogs.ManualFigureDrawingSupport.*;

import javafx.scene.layout.Pane;

/** Figuras didácticas de UML Secuencia para la guía académica. */
final class ManualFigureUmlSequenceFigures {

    private ManualFigureUmlSequenceFigures() {
    }

    static boolean draw(String figureId, Pane canvas) {
        switch (figureId) {
            case "uml-sequence-anatomy" -> drawAnatomy(canvas);
            case "uml-sequence-frontend-backend-db" -> drawFrontendBackendDb(canvas);
            case "uml-sequence-alt-error" -> drawAltError(canvas);
            case "uml-sequence-combined-fragment-parts" -> drawCombinedFragmentParts(canvas);
            case "uml-sequence-opt-guard" -> drawOptGuard(canvas);
            case "uml-sequence-loop-guard" -> drawLoopGuard(canvas);
            case "uml-sequence-par-operands" -> drawParOperands(canvas);
            case "uml-sequence-critical-region" -> drawCriticalRegion(canvas);
            case "uml-sequence-ref-interaction" -> drawRefInteraction(canvas);
            case "uml-sequence-nested-fragments" -> drawNestedFragments(canvas);
            case "uml-sequence-external-system" -> drawExternalSystem(canvas);
            case "uml-sequence-sync-async" -> drawSyncAsync(canvas);
            case "uml-sequence-common-errors" -> drawCommonErrors(canvas);
            default -> {
                return false;
            }
        }
        return true;
    }

    private static void drawAnatomy(Pane p) {
        participant(p, 45, "Actor");
        participant(p, 170, "Pantalla");
        participant(p, 305, "Servicio");
        arrow(p, 75, 72, 200, 72);
        text(p, "solicita", 112, 62);
        arrow(p, 200, 102, 335, 102);
        text(p, "mensaje()", 236, 92);
        arrow(p, 335, 132, 200, 132);
        text(p, "retorno", 247, 122);
        text(p, "tiempo ↓", 410, 95);
    }

    private static void drawFrontendBackendDb(Pane p) {
        participant(p, 35, "Usuario");
        participant(p, 130, "Pantalla");
        participant(p, 235, "Service");
        participant(p, 345, "Repo");
        cylinder(p, 430, 58, "BD");
        arrow(p, 65, 66, 160, 66);
        arrow(p, 160, 88, 265, 88);
        arrow(p, 265, 112, 375, 112);
        arrow(p, 375, 136, 430, 136);
        text(p, "acción", 88, 56);
        text(p, "registrar()", 178, 78);
        text(p, "guardar()", 291, 102);
    }

    private static void drawAltError(Pane p) {
        participant(p, 70, "Pantalla");
        participant(p, 245, "PagoService");
        combinedFrame(p, 42, 62, 365, 94, "alt", "[válido] / [inválido]");
        arrow(p, 100, 84, 275, 84);
        text(p, "registrarPago", 145, 74);
        line(p, 42, 112, 407, 112);
        text(p, "[monto inválido]", 55, 105);
        arrow(p, 275, 138, 100, 138);
        text(p, "errorValidación", 148, 130);
    }

    private static void drawCombinedFragmentParts(Pane p) {
        participant(p, 55, "Pantalla");
        participant(p, 225, "Servicio");
        combinedFrame(p, 35, 58, 390, 104, "alt", "[cupo disponible]");
        arrow(p, 85, 88, 255, 88);
        text(p, "registrar()", 145, 78);
        line(p, 35, 116, 425, 116);
        text(p, "operando 2: [sin cupo]", 52, 108);
        arrow(p, 255, 142, 85, 142);
        text(p, "rechazo", 160, 134);
        text(p, "operador", 38, 46);
        text(p, "guarda", 122, 46);
        text(p, "rango temporal", 300, 46);
    }

    private static void drawOptGuard(Pane p) {
        participant(p, 70, "Servicio");
        participant(p, 270, "Correo");
        arrow(p, 100, 70, 300, 70);
        text(p, "guardar()", 170, 60);
        combinedFrame(p, 52, 88, 320, 64, "opt", "[notificar]");
        arrow(p, 100, 124, 300, 124);
        text(p, "enviarCorreo()", 160, 114);
        text(p, "si la guarda no se cumple, se omite", 115, 174);
    }

    private static void drawLoopGuard(Pane p) {
        participant(p, 58, "Service");
        participant(p, 238, "Repo");
        combinedFrame(p, 38, 58, 350, 104, "loop", "[por cada estudiante]");
        arrow(p, 88, 86, 268, 86);
        text(p, "validar()", 158, 76);
        arrow(p, 268, 118, 88, 118);
        text(p, "resultado", 165, 110);
        arrow(p, 88, 146, 268, 146);
        text(p, "guardarNota()", 145, 136);
        text(p, "el bloque se repite por condición", 105, 178);
    }

    private static void drawParOperands(Pane p) {
        participant(p, 52, "Service");
        participant(p, 198, "Auditoría");
        participant(p, 342, "Correo");
        combinedFrame(p, 34, 58, 405, 108, "par", "trabajos independientes");
        arrow(p, 82, 88, 228, 88);
        text(p, "registrarEvento()", 105, 78);
        line(p, 34, 112, 439, 112);
        text(p, "operando paralelo", 48, 105);
        arrow(p, 82, 142, 372, 142);
        text(p, "encolarNotificación()", 165, 132);
    }

    private static void drawCriticalRegion(Pane p) {
        participant(p, 60, "Service");
        participant(p, 250, "CuposRepo");
        combinedFrame(p, 42, 66, 350, 86, "critical", "[asignar cupo]");
        arrow(p, 90, 96, 280, 96);
        text(p, "reservarCupo()", 155, 86);
        arrow(p, 280, 128, 90, 128);
        text(p, "cupoConfirmado", 148, 120);
        text(p, "protege invariante: no sobrecupo", 105, 174);
    }

    private static void drawRefInteraction(Pane p) {
        participant(p, 58, "Pantalla");
        participant(p, 258, "AuthService");
        combinedFrame(p, 52, 72, 330, 58, "ref", "Validar identidad");
        text(p, "interacción definida en otro diagrama", 115, 108);
        arrow(p, 88, 150, 288, 150);
        text(p, "continuar()", 165, 140);
    }

    private static void drawNestedFragments(Pane p) {
        participant(p, 55, "Service");
        participant(p, 245, "Repo");
        combinedFrame(p, 34, 54, 390, 126, "loop", "[por cada registro]");
        arrow(p, 85, 82, 275, 82);
        text(p, "leer()", 170, 72);
        combinedFrame(p, 66, 98, 320, 62, "alt", "[válido]");
        arrow(p, 85, 134, 275, 134);
        text(p, "guardar()", 160, 124);
        text(p, "anidación controlada", 142, 188);
    }

    private static void drawExternalSystem(Pane p) {
        participant(p, 55, "Backend");
        participant(p, 210, "FacturaAPI");
        rectangle(p, 335, 45, 105, 42, "Sistema\nexterno");
        arrow(p, 85, 78, 240, 78);
        text(p, "emitir()", 138, 68);
        arrow(p, 240, 108, 85, 108);
        text(p, "autorizado/error", 120, 100);
        arrow(p, 270, 78, 335, 66);
        text(p, "fuera del límite", 315, 130);
    }

    private static void drawSyncAsync(Pane p) {
        participant(p, 50, "Pantalla");
        participant(p, 205, "API");
        participant(p, 355, "Notificador");
        arrow(p, 80, 70, 235, 70);
        arrow(p, 235, 96, 80, 96);
        text(p, "síncrono: espera respuesta", 95, 58);
        arrow(p, 235, 128, 385, 128);
        text(p, "asíncrono: evento", 265, 118);
    }

    private static void drawCommonErrors(Pane p) {
        text(p, "MAL", 42, 28);
        rectangle(p, 28, 45, 120, 30, "procesar");
        rectangle(p, 28, 85, 120, 30, "datos");
        rectangle(p, 28, 125, 120, 30, "gestión");
        text(p, "BIEN", 245, 28);
        participant(p, 230, "Pantalla");
        participant(p, 350, "Service");
        arrow(p, 260, 80, 380, 80);
        text(p, "validarPago()", 285, 70);
        arrow(p, 380, 116, 260, 116);
        text(p, "errorValidación", 282, 106);
    }

    private static void combinedFrame(Pane p, double x, double y, double w, double h, String operator, String guard) {
        rectangle(p, x, y, w, h, "");
        rectangle(p, x, y, 54, 24, operator);
        text(p, guard, x + 64, y + 17);
    }
}
