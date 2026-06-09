package com.marcosmoreira.domainmodelstudio.presentation.dialogs;

import static com.marcosmoreira.domainmodelstudio.presentation.dialogs.ManualFigureDrawingSupport.*;

import javafx.scene.layout.Pane;

/** Figuras didácticas de UML Estados para la guía académica. */
final class ManualFigureUmlStateFigures {

    private ManualFigureUmlStateFigures() {
    }

    static boolean draw(String figureId, Pane canvas) {
        switch (figureId) {
            case "uml-state-symbols" -> drawSymbols(canvas);
            case "uml-state-order-lifecycle" -> drawOrderLifecycle(canvas);
            case "uml-state-alternative-paths" -> drawAlternativePaths(canvas);
            case "uml-state-guard-condition" -> drawGuardCondition(canvas);
            case "uml-state-terminal-states" -> drawTerminalStates(canvas);
            case "uml-state-history" -> drawHistory(canvas);
            case "uml-state-common-errors" -> drawCommonErrors(canvas);
            case "uml-state-transition" -> drawOrderLifecycle(canvas);
            default -> {
                return false;
            }
        }
        return true;
    }

    private static void drawSymbols(Pane p) {
        circle(p, 36, 84, 9, "");
        arrow(p, 48, 84, 95, 84);
        rounded(p, 100, 58, 86, 52, "Estado");
        arrow(p, 186, 84, 262, 84);
        text(p, "evento [condición] / acción", 190, 72);
        rounded(p, 270, 58, 86, 52, "Nuevo\nestado");
        arrow(p, 356, 84, 405, 84);
        circle(p, 425, 84, 14, "");
        circle(p, 425, 84, 9, "");
        text(p, "inicio", 18, 120);
        text(p, "transición", 202, 115);
        text(p, "final", 410, 120);
    }

    private static void drawOrderLifecycle(Pane p) {
        circle(p, 28, 86, 8, "");
        rounded(p, 52, 61, 70, 46, "Recibida");
        rounded(p, 142, 61, 82, 46, "Diagnos-\nticada");
        rounded(p, 248, 61, 76, 46, "En\nreparación");
        rounded(p, 348, 61, 66, 46, "Entregada");
        circle(p, 444, 84, 13, "");
        circle(p, 444, 84, 8, "");
        arrow(p, 36, 84, 52, 84);
        arrow(p, 122, 84, 142, 84);
        arrow(p, 224, 84, 248, 84);
        arrow(p, 324, 84, 348, 84);
        arrow(p, 414, 84, 431, 84);
        text(p, "registrar diagnóstico", 120, 124);
        text(p, "iniciar reparación", 235, 124);
    }

    private static void drawAlternativePaths(Pane p) {
        rounded(p, 188, 58, 98, 48, "Diagnos-\nticada");
        rounded(p, 40, 26, 92, 40, "Aprobada");
        rounded(p, 42, 122, 92, 40, "Rechazada");
        rounded(p, 340, 26, 80, 40, "Anulada");
        rounded(p, 330, 122, 104, 40, "No reparable");
        arrow(p, 188, 74, 132, 48);
        arrow(p, 188, 94, 134, 140);
        arrow(p, 286, 74, 340, 48);
        arrow(p, 286, 94, 330, 140);
        text(p, "aprueba", 105, 28);
        text(p, "rechaza", 103, 153);
        text(p, "anula", 315, 28);
        text(p, "daño crítico", 304, 153);
    }

    private static void drawGuardCondition(Pane p) {
        rounded(p, 52, 64, 105, 48, "Lista para\nentrega");
        rounded(p, 318, 64, 86, 48, "Entregada");
        arrow(p, 157, 88, 318, 88);
        text(p, "entregarEquipo", 190, 66);
        text(p, "[pagoCompleto]", 198, 84);
        text(p, "/ registrarFechaEntrega", 184, 103);
        text(p, "permiso: reparaciones.entregar", 148, 138);
    }

    private static void drawTerminalStates(Pane p) {
        circle(p, 34, 84, 8, "");
        rounded(p, 64, 61, 78, 46, "Recibida");
        rounded(p, 190, 22, 88, 42, "Entregada");
        rounded(p, 190, 112, 88, 42, "Anulada");
        circle(p, 345, 43, 13, "");
        circle(p, 345, 43, 8, "");
        circle(p, 345, 133, 13, "");
        circle(p, 345, 133, 8, "");
        arrow(p, 42, 84, 64, 84);
        arrow(p, 142, 76, 190, 43);
        arrow(p, 142, 94, 190, 133);
        arrow(p, 278, 43, 332, 43);
        arrow(p, 278, 133, 332, 133);
        text(p, "final normal", 365, 46);
        text(p, "final alternativo", 365, 136);
    }

    private static void drawHistory(Pane p) {
        rounded(p, 28, 62, 80, 40, "Recibida");
        rounded(p, 148, 62, 92, 40, "Diagnos-\nticada");
        rounded(p, 286, 62, 82, 40, "Entregada");
        arrow(p, 108, 82, 148, 82);
        arrow(p, 240, 82, 286, 82);
        rectangle(p, 44, 122, 380, 34, "historial: fecha | usuario | estado anterior | estado nuevo | motivo");
        text(p, "auditoría del ciclo de vida", 155, 34);
    }

    private static void drawCommonErrors(Pane p) {
        text(p, "MAL", 42, 26);
        rounded(p, 28, 45, 105, 34, "Registrar\npago");
        rounded(p, 28, 98, 105, 34, "Pantalla\ndetalle");
        text(p, "acciones o pantallas", 24, 152);
        text(p, "BIEN", 250, 26);
        rounded(p, 220, 45, 105, 34, "Pendiente");
        rounded(p, 340, 45, 88, 34, "Pagado");
        arrow(p, 325, 62, 340, 62);
        text(p, "registrarPago", 300, 95);
        text(p, "estados + evento", 270, 132);
    }
}
