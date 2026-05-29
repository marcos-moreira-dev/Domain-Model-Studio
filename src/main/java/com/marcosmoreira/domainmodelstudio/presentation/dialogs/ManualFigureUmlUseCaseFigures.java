package com.marcosmoreira.domainmodelstudio.presentation.dialogs;

import static com.marcosmoreira.domainmodelstudio.presentation.dialogs.ManualFigureDrawingSupport.*;

import javafx.scene.layout.Pane;

/** Figuras didácticas de UML Casos de uso para mantener pequeño el factory principal. */
final class ManualFigureUmlUseCaseFigures {

    private ManualFigureUmlUseCaseFigures() {
    }

    static boolean draw(String figureId, Pane canvas) {
        switch (figureId) {
            case "uml-use-case-symbols" -> drawSymbols(canvas);
            case "uml-use-case-system-boundary" -> drawSystemBoundary(canvas);
            case "uml-use-case-basic-admin-example" -> drawBasicAdminExample(canvas);
            case "uml-use-case-include-extend" -> drawIncludeExtend(canvas);
            case "uml-use-case-external-system-actor" -> drawExternalSystemActor(canvas);
            case "uml-use-case-textual-spec" -> drawTextualSpec(canvas);
            case "uml-use-case-common-errors" -> drawCommonErrors(canvas);
            default -> {
                return false;
            }
        }
        return true;
    }

    private static void drawSymbols(Pane p) {
        stickPerson(p, 50, 75, "Actor");
        ellipseLike(p, 142, 50, 120, 44, "Caso de\nuso");
        rectangle(p, 315, 32, 122, 92, "Límite\ndel sistema");
        ellipseLike(p, 333, 56, 86, 30, "Función");
        line(p, 78, 75, 142, 72);
        text(p, "actor externo", 23, 150);
        text(p, "objetivo observable", 137, 128);
        text(p, "frontera", 350, 146);
    }

    private static void drawSystemBoundary(Pane p) {
        stickPerson(p, 45, 78, "Recepción");
        rectangle(p, 145, 26, 260, 125, "");
        text(p, "Sistema administrativo", 207, 47);
        ellipseLike(p, 168, 68, 95, 32, "Registrar\ncliente");
        ellipseLike(p, 282, 68, 95, 32, "Crear\norden");
        ellipseLike(p, 225, 112, 110, 32, "Consultar\nestado");
        line(p, 72, 78, 168, 84);
        line(p, 72, 78, 282, 84);
        text(p, "actor fuera", 30, 152);
        text(p, "casos dentro del límite", 210, 165);
    }

    private static void drawBasicAdminExample(Pane p) {
        stickPerson(p, 40, 52, "Recepción");
        stickPerson(p, 40, 125, "Técnico");
        rectangle(p, 130, 22, 230, 135, "");
        text(p, "Sistema de reparaciones", 185, 42);
        ellipseLike(p, 150, 58, 92, 30, "Crear\norden");
        ellipseLike(p, 250, 58, 92, 30, "Registrar\ncliente");
        ellipseLike(p, 150, 112, 92, 30, "Registrar\ndiagnóstico");
        ellipseLike(p, 250, 112, 92, 30, "Consultar\norden");
        stickPerson(p, 420, 88, "Caja");
        ellipseLike(p, 343, 86, 88, 30, "Registrar\npago");
        line(p, 67, 52, 150, 73);
        line(p, 67, 52, 250, 73);
        line(p, 67, 125, 150, 127);
        line(p, 392, 88, 343, 101);
    }

    private static void drawIncludeExtend(Pane p) {
        ellipseLike(p, 52, 58, 118, 36, "Crear\norden");
        ellipseLike(p, 230, 58, 130, 36, "Buscar\ncliente");
        arrow(p, 170, 76, 230, 76);
        text(p, "<<include>>", 172, 62);
        ellipseLike(p, 52, 118, 118, 36, "Registrar\npago");
        ellipseLike(p, 230, 118, 130, 36, "Imprimir\ncomprobante");
        arrow(p, 230, 136, 170, 136);
        text(p, "<<extend>>", 172, 122);
        text(p, "obligatorio", 370, 78);
        text(p, "condicional", 370, 138);
    }

    private static void drawExternalSystemActor(Pane p) {
        stickPerson(p, 45, 78, "Caja");
        rectangle(p, 138, 35, 205, 105, "");
        text(p, "Sistema administrativo", 185, 55);
        ellipseLike(p, 177, 78, 125, 34, "Registrar\npago");
        rectangle(p, 375, 70, 72, 42, "Facturación\nexterna");
        line(p, 72, 78, 177, 95);
        arrow(p, 302, 95, 375, 91);
        text(p, "sistema externo como actor", 235, 158);
    }

    private static void drawTextualSpec(Pane p) {
        rectangle(p, 32, 25, 405, 126, "");
        text(p, "Caso de uso: Registrar pago", 50, 48);
        text(p, "Precondición: orden activa y usuario con permiso", 50, 73);
        text(p, "Flujo: buscar orden → validar monto → registrar", 50, 98);
        text(p, "Alternativas: monto inválido, orden anulada", 50, 123);
        text(p, "Postcondición: pago registrado o error explicado", 50, 148);
    }

    private static void drawCommonErrors(Pane p) {
        text(p, "MAL", 48, 30);
        ellipseLike(p, 25, 48, 118, 30, "Clientes");
        ellipseLike(p, 25, 88, 118, 30, "Botón\nguardar");
        ellipseLike(p, 25, 132, 118, 30, "Pantalla\npago");
        text(p, "BIEN", 270, 30);
        ellipseLike(p, 245, 48, 155, 30, "Registrar\ncliente");
        ellipseLike(p, 245, 88, 155, 30, "Registrar\npago");
        ellipseLike(p, 245, 132, 155, 30, "Generar\nreporte");
        text(p, "verbo + objeto", 316, 164);
    }
}
