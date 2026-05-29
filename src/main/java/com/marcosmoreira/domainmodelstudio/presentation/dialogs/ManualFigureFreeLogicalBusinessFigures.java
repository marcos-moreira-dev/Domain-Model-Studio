package com.marcosmoreira.domainmodelstudio.presentation.dialogs;

import static com.marcosmoreira.domainmodelstudio.presentation.dialogs.ManualFigureDrawingSupport.*;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

/** Figuras didácticas para grafo libre y teoría interna de levantamiento lógico. */
final class ManualFigureFreeLogicalBusinessFigures {

    private ManualFigureFreeLogicalBusinessFigures() {
    }

    static boolean draw(String figureId, Pane canvas) {
        switch (figureId) {
            case "free-graph-overview" -> drawFreeGraphOverview(canvas);
            case "free-graph-node-content" -> drawFreeGraphNodeContent(canvas);
            case "free-graph-edge-types" -> drawFreeGraphEdgeTypes(canvas);
            case "free-graph-knowledge-map" -> drawFreeGraphKnowledgeMap(canvas);
            case "free-graph-common-errors" -> drawFreeGraphCommonErrors(canvas);
            case "logical-business-state-action-cycle" -> drawLogicalBusinessStateActionCycle(canvas);
            case "logical-business-graph-backbone" -> drawLogicalBusinessGraphBackbone(canvas);
            case "logical-business-graph-traceability" -> drawLogicalBusinessGraphTraceability(canvas);
            default -> {
                return false;
            }
        }
        return true;
    }

    private static void drawFreeGraphOverview(Pane p) {
        rectangle(p, 35, 36, 86, 38, "Nodo A");
        rectangle(p, 205, 28, 86, 38, "Nodo B");
        rectangle(p, 205, 103, 86, 38, "Nodo C");
        rectangle(p, 355, 66, 86, 38, "Nodo D");
        arrow(p, 121, 55, 205, 47);
        line(p, 121, 74, 205, 122);
        arrow(p, 291, 47, 355, 82);
        arrow(p, 291, 122, 355, 86);
        text(p, "dirigida", 145, 39);
        text(p, "asociación", 128, 115);
        text(p, "Grafo libre: estructura flexible, no notación formal", 75, 158);
    }

    private static void drawFreeGraphNodeContent(Pane p) {
        rectangle(p, 48, 34, 170, 104, "");
        line(p, 48, 65, 218, 65);
        text(p, "Título del nodo", 78, 54);
        text(p, "• descripción breve", 64, 87);
        text(p, "• evidencia o nota", 64, 108);
        text(p, "• enlace conceptual", 64, 129);
        rectangle(p, 286, 54, 128, 46, "Relación\netiquetada");
        arrow(p, 218, 84, 286, 77);
        text(p, "contenido legible + vínculo explícito", 104, 158);
    }

    private static void drawFreeGraphEdgeTypes(Pane p) {
        rectangle(p, 40, 48, 78, 34, "Origen");
        rectangle(p, 212, 48, 78, 34, "Destino");
        arrow(p, 118, 65, 212, 65);
        text(p, "dirige / causa / depende", 127, 52);
        rectangle(p, 40, 115, 78, 34, "Tema A");
        rectangle(p, 212, 115, 78, 34, "Tema B");
        line(p, 118, 132, 212, 132);
        text(p, "relacionado con", 128, 120);
        rectangle(p, 340, 82, 96, 38, "Etiqueta\nobligatoria");
        arrow(p, 290, 65, 340, 92);
        arrow(p, 290, 132, 340, 110);
    }

    private static void drawFreeGraphKnowledgeMap(Pane p) {
        rectangle(p, 178, 64, 118, 42, "Idea\ncentral");
        rectangle(p, 30, 30, 108, 34, "Fuente");
        rectangle(p, 30, 116, 108, 34, "Riesgo");
        rectangle(p, 336, 30, 108, 34, "Decisión");
        rectangle(p, 336, 116, 108, 34, "Pendiente");
        arrow(p, 138, 47, 178, 75);
        arrow(p, 138, 133, 178, 95);
        arrow(p, 296, 75, 336, 47);
        arrow(p, 296, 95, 336, 133);
        text(p, "Mapa informal de conocimiento: útil cuando aún no hay notación mejor", 38, 165);
    }

    private static void drawFreeGraphCommonErrors(Pane p) {
        text(p, "MAL", 45, 28);
        rectangle(p, 28, 44, 150, 84, "Todo el negocio\nen un nodo\ngigante");
        line(p, 178, 86, 230, 86);
        rectangle(p, 230, 69, 72, 34, "?");
        text(p, "sin etiqueta", 178, 118);
        text(p, "BIEN", 336, 28);
        rectangle(p, 330, 45, 90, 30, "Regla");
        rectangle(p, 330, 93, 90, 30, "Proceso");
        arrow(p, 375, 75, 375, 93);
        text(p, "aplica", 385, 86);
        text(p, "nodos pequeños + relaciones legibles", 248, 156);
    }

    private static void drawLogicalBusinessStateActionCycle(Pane p) {
        rectangle(p, 30, 65, 88, 38, "Estado\ninicial");
        rectangle(p, 154, 36, 88, 38, "PRE + RN");
        rectangle(p, 154, 105, 88, 38, "INV");
        rectangle(p, 278, 65, 88, 38, "ACC\ntransforma");
        rectangle(p, 404, 65, 88, 38, "POST\ncierre");
        arrow(p, 118, 84, 154, 55);
        arrow(p, 118, 84, 154, 124);
        arrow(p, 242, 55, 278, 77);
        arrow(p, 242, 124, 278, 91);
        arrow(p, 366, 84, 404, 84);
        Line invariant = line(p, 322, 103, 322, 135);
        invariant.getStrokeDashArray().addAll(5.0, 4.0);
        text(p, "La acción solo es válida si conserva invariantes", 96, 160);
    }

    private static void drawLogicalBusinessGraphBackbone(Pane p) {
        rectangle(p, 24, 64, 70, 36, "MF");
        rectangle(p, 126, 64, 70, 36, "FL");
        rectangle(p, 228, 64, 70, 36, "CU");
        rectangle(p, 330, 64, 70, 36, "ACC");
        arrow(p, 94, 82, 126, 82);
        arrow(p, 196, 82, 228, 82);
        arrow(p, 298, 82, 330, 82);
        rectangle(p, 132, 18, 58, 28, "RN");
        rectangle(p, 236, 18, 58, 28, "PRE");
        rectangle(p, 338, 18, 58, 28, "POST");
        rectangle(p, 236, 122, 58, 28, "ENT");
        rectangle(p, 338, 122, 58, 28, "REP");
        arrow(p, 161, 46, 161, 64);
        arrow(p, 265, 46, 265, 64);
        arrow(p, 367, 64, 367, 46);
        arrow(p, 265, 100, 265, 122);
        arrow(p, 367, 100, 367, 122);
        text(p, "Backbone semántico: macroflujo → flujo → caso de uso → acción", 36, 162);
    }

    private static void drawLogicalBusinessGraphTraceability(Pane p) {
        rectangle(p, 24, 60, 112, 46, "Levantamiento\nlógico");
        rectangle(p, 178, 60, 112, 46, "Grafo lógico\nderivado");
        rectangle(p, 346, 22, 96, 30, "UML");
        rectangle(p, 346, 62, 96, 30, "BPMN");
        rectangle(p, 346, 102, 96, 30, "Diccionario");
        arrow(p, 136, 83, 178, 83);
        arrow(p, 290, 75, 346, 37);
        arrow(p, 290, 83, 346, 77);
        arrow(p, 290, 91, 346, 117);
        text(p, "MF/FL/CU/ACC/RN/PRE/INV/POST", 38, 132);
        text(p, "uso como fuente, no generación ciega", 90, 160);
    }
}
