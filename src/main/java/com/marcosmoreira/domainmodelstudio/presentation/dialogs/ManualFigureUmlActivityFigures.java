package com.marcosmoreira.domainmodelstudio.presentation.dialogs;

import static com.marcosmoreira.domainmodelstudio.presentation.dialogs.ManualFigureDrawingSupport.*;

import javafx.scene.layout.Pane;

/** Figuras didácticas para UML Actividad dentro de la guía académica. */
final class ManualFigureUmlActivityFigures {

    private ManualFigureUmlActivityFigures() {
    }

    static boolean draw(String figureId, Pane canvas) {
        switch (figureId) {
            case "uml-activity-symbols" -> drawSymbols(canvas);
            case "uml-activity-linear" -> drawLinear(canvas);
            case "uml-activity-decision" -> drawDecision(canvas);
            case "uml-activity-swimlanes" -> drawSwimlanes(canvas);
            case "uml-activity-fork-join" -> drawForkJoin(canvas);
            case "uml-activity-vs-sequence" -> drawVsSequence(canvas);
            case "uml-activity-common-errors" -> drawCommonErrors(canvas);
            default -> {
                return false;
            }
        }
        return true;
    }

    private static void drawSymbols(Pane p) {
        circle(p, 38, 74, 12, "");
        text(p, "inicio", 20, 108);
        rounded(p, 92, 52, 86, 44, "Acción");
        text(p, "acción", 112, 108);
        diamond(p, 235, 74, 54, 42, "?");
        text(p, "decisión", 208, 108);
        rectangle(p, 305, 55, 12, 42, "");
        text(p, "fork", 292, 108);
        rectangle(p, 360, 55, 12, 42, "");
        text(p, "join", 348, 108);
        circle(p, 430, 74, 14, "");
        circle(p, 430, 74, 8, "");
        text(p, "final", 414, 108);
        text(p, "Símbolos básicos para leer una actividad", 95, 150);
    }

    private static void drawLinear(Pane p) {
        circle(p, 35, 84, 12, "");
        rounded(p, 78, 62, 88, 44, "Buscar\norden");
        rounded(p, 202, 62, 92, 44, "Validar\ndatos");
        rounded(p, 330, 62, 94, 44, "Registrar\npago");
        circle(p, 455, 84, 14, "");
        circle(p, 455, 84, 8, "");
        arrow(p, 47, 84, 78, 84);
        arrow(p, 166, 84, 202, 84);
        arrow(p, 294, 84, 330, 84);
        arrow(p, 424, 84, 441, 84);
        text(p, "leer de izquierda a derecha: inicio -> acciones -> final", 65, 145);
    }

    private static void drawDecision(Pane p) {
        rounded(p, 28, 64, 96, 42, "Validar\nmonto");
        diamond(p, 190, 85, 62, 48, "monto\nválido?");
        rounded(p, 292, 38, 102, 40, "Registrar\npago");
        rounded(p, 292, 112, 102, 40, "Mostrar\nerror");
        arrow(p, 124, 85, 159, 85);
        arrow(p, 221, 72, 292, 58);
        arrow(p, 221, 98, 292, 132);
        text(p, "sí", 248, 56);
        text(p, "no", 248, 122);
        text(p, "toda salida del rombo debe tener condición", 82, 166);
    }

    private static void drawSwimlanes(Pane p) {
        rectangle(p, 25, 25, 420, 55, "");
        rectangle(p, 25, 80, 420, 65, "");
        text(p, "Usuario", 35, 58);
        text(p, "Sistema", 35, 116);
        rounded(p, 115, 38, 90, 30, "Ingresa\ndatos");
        rounded(p, 220, 95, 92, 30, "Valida\ndatos");
        rounded(p, 330, 95, 90, 30, "Guarda");
        arrow(p, 205, 53, 220, 105);
        arrow(p, 312, 110, 330, 110);
        text(p, "los carriles separan responsabilidades", 118, 164);
    }

    private static void drawForkJoin(Pane p) {
        rounded(p, 28, 64, 92, 40, "Cerrar\norden");
        rectangle(p, 155, 42, 12, 88, "");
        rounded(p, 205, 28, 100, 32, "Historial");
        rounded(p, 205, 75, 100, 32, "Comprobante");
        rounded(p, 205, 122, 100, 32, "Notificar");
        rectangle(p, 340, 42, 12, 88, "");
        rounded(p, 380, 70, 70, 40, "Fin");
        arrow(p, 120, 84, 155, 84);
        arrow(p, 167, 54, 205, 44);
        arrow(p, 167, 84, 205, 91);
        arrow(p, 167, 116, 205, 138);
        arrow(p, 305, 44, 340, 54);
        arrow(p, 305, 91, 340, 84);
        arrow(p, 305, 138, 340, 116);
        arrow(p, 352, 84, 380, 90);
        text(p, "fork divide; join sincroniza", 145, 166);
    }

    private static void drawVsSequence(Pane p) {
        text(p, "Actividad", 58, 28);
        rounded(p, 35, 48, 90, 30, "Validar");
        rounded(p, 35, 92, 90, 30, "Guardar");
        arrow(p, 80, 78, 80, 92);
        text(p, "acciones", 55, 145);
        text(p, "Secuencia", 275, 28);
        participant(p, 238, "Pantalla");
        participant(p, 350, "Servicio");
        arrow(p, 268, 75, 380, 75);
        arrow(p, 380, 112, 268, 112);
        text(p, "mensajes", 306, 145);
    }

    private static void drawCommonErrors(Pane p) {
        text(p, "MAL", 40, 28);
        rounded(p, 28, 48, 108, 30, "Gestionar");
        diamond(p, 82, 98, 48, 34, "?");
        rounded(p, 28, 125, 108, 30, "Pantalla\npago");
        text(p, "BIEN", 248, 28);
        rounded(p, 218, 48, 128, 30, "Validar monto");
        diamond(p, 282, 98, 58, 38, "monto\nválido?");
        rounded(p, 218, 125, 128, 30, "Registrar pago");
        text(p, "verbos claros + condiciones", 300, 166);
    }
}
