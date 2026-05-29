package com.marcosmoreira.domainmodelstudio.presentation.dialogs;

import static com.marcosmoreira.domainmodelstudio.presentation.dialogs.ManualFigureDrawingSupport.*;

import javafx.scene.layout.Pane;
import javafx.scene.shape.*;

/** Figuras didácticas agrupadas para mantener revisable el factory principal. */
final class ManualFigureConceptualDataFigures {

    private ManualFigureConceptualDataFigures() {
    }

    static boolean draw(String figureId, Pane canvas) {
        switch (figureId) {
            case "conceptual-chen-symbols" -> drawConceptualChen(canvas);
            case "conceptual-entity-symbol" -> drawConceptualEntity(canvas);
            case "conceptual-attribute-symbol" -> drawConceptualAttribute(canvas);
            case "conceptual-relationship-symbol" -> drawConceptualRelationship(canvas);
            case "conceptual-cardinality-symbol" -> drawConceptualCardinality(canvas);
            case "conceptual-weak-entity-symbol" -> drawConceptualWeakEntity(canvas);
            case "conceptual-associative-entity-symbol" -> drawConceptualAssociativeEntity(canvas);
            case "conceptual-multivalued-attribute-symbol" -> drawConceptualMultivaluedAttribute(canvas);
            case "conceptual-derived-attribute-symbol" -> drawConceptualDerivedAttribute(canvas);
            case "conceptual-chen-vs-crow-foot" -> drawConceptualChenVsCrowFoot(canvas);
            case "conceptual-admin-example" -> drawConceptualAdminExample(canvas);
            case "conceptual-common-errors" -> drawConceptualCommonErrors(canvas);
            case "data-dictionary-table" -> drawDataDictionary(canvas);
            case "data-dictionary-from-concept" -> drawDataDictionaryFromConcept(canvas);
            case "data-dictionary-traceability" -> drawDataDictionaryTraceability(canvas);
            case "data-dictionary-columns" -> drawDataDictionaryColumns(canvas);
            case "data-dictionary-allowed-values" -> drawDataDictionaryAllowedValues(canvas);
            case "data-dictionary-validation" -> drawDataDictionaryValidation(canvas);
            case "data-dictionary-calculated-field" -> drawDataDictionaryCalculatedField(canvas);
            case "data-dictionary-sensitive-data" -> drawDataDictionarySensitiveData(canvas);
            case "data-dictionary-audit-fields" -> drawDataDictionaryAuditFields(canvas);
            case "data-dictionary-common-errors" -> drawDataDictionaryCommonErrors(canvas);
            default -> {
                return false;
            }
        }
        return true;
    }

    private static void drawConceptualChen(Pane p) {
        rectangle(p, 42, 45, 115, 48, "Entidad");
        ellipseLike(p, 198, 42, 118, 54, "atributo");
        diamond(p, 402, 68, 58, 38, "relación");
        line(p, 157, 69, 198, 69);
        text(p, "describe", 164, 58);
        line(p, 344, 68, 373, 68);
        text(p, "vincula", 326, 58);
        label(p, "Símbolos de Chen: entidad, atributo y relación se leen por su rol semántico.", 42, 122);
    }

    private static void drawConceptualEntity(Pane p) {
        rectangle(p, 55, 45, 145, 55, "Cliente");
        rectangle(p, 270, 45, 145, 55, "Orden\nReparación");
        arrow(p, 200, 72, 270, 72);
        text(p, "Entidad = cosa relevante del negocio", 108, 132);
    }

    private static void drawConceptualAttribute(Pane p) {
        rectangle(p, 175, 60, 120, 48, "Cliente");
        ellipseLike(p, 35, 28, 110, 38, "nombre");
        ellipseLike(p, 35, 108, 110, 38, "teléfono");
        ellipseLike(p, 330, 28, 105, 38, "cédula");
        ellipseLike(p, 330, 108, 105, 38, "correo");
        line(p, 145, 47, 175, 72);
        line(p, 145, 127, 175, 86);
        line(p, 330, 47, 295, 72);
        line(p, 330, 127, 295, 86);
    }

    private static void drawConceptualRelationship(Pane p) {
        rectangle(p, 30, 58, 105, 48, "Cliente");
        diamond(p, 235, 82, 96, 48, "registra");
        rectangle(p, 340, 58, 105, 48, "Orden");
        line(p, 135, 82, 187, 82);
        line(p, 283, 82, 340, 82);
        text(p, "La relación debe leerse como una frase del negocio", 68, 138);
    }

    private static void drawConceptualCardinality(Pane p) {
        rectangle(p, 38, 58, 115, 48, "Cliente");
        rectangle(p, 320, 58, 115, 48, "Orden");
        line(p, 153, 82, 320, 82);
        text(p, "1", 172, 76);
        text(p, "0..*", 278, 76);
        text(p, "Un cliente puede tener cero o muchas órdenes", 88, 135);
        line(p, 298, 70, 320, 82);
        line(p, 298, 94, 320, 82);
    }

    private static void drawConceptualWeakEntity(Pane p) {
        rectangle(p, 45, 55, 130, 55, "Orden\nReparación");
        Rectangle weak = rectangle(p, 295, 55, 130, 55, "Historial\nEstado");
        Rectangle border = new Rectangle(291, 51, 138, 63);
        border.getStyleClass().add("manual-figure-shape");
        border.setFill(null);
        p.getChildren().add(border);
        arrow(p, 175, 82, 295, 82);
        text(p, "Depende de la entidad principal", 132, 138);
    }

    private static void drawConceptualAssociativeEntity(Pane p) {
        rectangle(p, 28, 50, 95, 45, "Orden");
        rectangle(p, 192, 50, 120, 45, "Detalle\nRepuesto");
        rectangle(p, 372, 50, 75, 45, "Repuesto");
        arrow(p, 123, 72, 192, 72);
        arrow(p, 312, 72, 372, 72);
        text(p, "cantidad", 218, 120);
        text(p, "precio", 218, 140);
    }

    private static void drawConceptualMultivaluedAttribute(Pane p) {
        rectangle(p, 175, 62, 120, 48, "Cliente");
        ellipseLike(p, 40, 55, 105, 55, "teléfono");
        Rectangle outer = new Rectangle(36, 51, 113, 63);
        outer.setArcWidth(55);
        outer.setArcHeight(55);
        outer.getStyleClass().add("manual-figure-shape");
        outer.setFill(null);
        p.getChildren().add(outer);
        line(p, 145, 82, 175, 82);
        text(p, "Puede repetirse: varios teléfonos para un cliente", 88, 138);
    }

    private static void drawConceptualDerivedAttribute(Pane p) {
        rectangle(p, 178, 62, 122, 48, "Orden");
        ellipseLike(p, 35, 40, 110, 38, "total");
        ellipseLike(p, 335, 40, 110, 38, "pagos");
        ellipseLike(p, 178, 118, 122, 38, "/saldo");
        line(p, 145, 59, 178, 78);
        line(p, 335, 59, 300, 78);
        line(p, 239, 110, 239, 118);
        text(p, "saldo = total - pagos", 160, 28);
    }

    private static void drawConceptualChenVsCrowFoot(Pane p) {
        text(p, "Chen", 32, 30);
        rectangle(p, 25, 55, 75, 38, "Cliente");
        diamond(p, 150, 74, 64, 34, "registra");
        rectangle(p, 220, 55, 85, 38, "Orden");
        line(p, 100, 74, 118, 74);
        line(p, 182, 74, 220, 74);
        text(p, "Pata de gallo", 318, 30);
        rectangle(p, 315, 55, 65, 38, "Cliente");
        rectangle(p, 405, 55, 55, 38, "Orden");
        line(p, 380, 74, 405, 74);
        text(p, "1", 383, 67);
        text(p, "*", 397, 67);
        text(p, "Misma regla: un cliente puede registrar muchas órdenes", 58, 135);
    }

    private static void drawConceptualAdminExample(Pane p) {
        rectangle(p, 20, 30, 82, 36, "Cliente");
        rectangle(p, 132, 30, 82, 36, "Equipo");
        rectangle(p, 244, 30, 92, 36, "Orden");
        rectangle(p, 365, 30, 82, 36, "Pago");
        rectangle(p, 244, 110, 92, 36, "Detalle\nRepuesto");
        rectangle(p, 365, 110, 82, 36, "Repuesto");
        arrow(p, 102, 48, 132, 48);
        arrow(p, 214, 48, 244, 48);
        arrow(p, 336, 48, 365, 48);
        arrow(p, 290, 66, 290, 110);
        arrow(p, 336, 128, 365, 128);
        text(p, "Ejemplo: reparación con pagos y repuestos", 80, 92);
    }

    private static void drawConceptualCommonErrors(Pane p) {
        rectangle(p, 18, 38, 115, 48, "Pantalla\nClientes");
        text(p, "✕", 62, 112);
        rectangle(p, 177, 38, 115, 48, "Reparar");
        text(p, "✕", 225, 112);
        rectangle(p, 336, 38, 115, 48, "tabla_clientes");
        text(p, "✕", 385, 112);
        text(p, "Entidad correcta: Cliente, OrdenReparacion, Pago", 82, 145);
    }

    private static void drawDataDictionary(Pane p) {
        rectangle(p, 25, 25, 420, 115, "");
        line(p, 25, 55, 445, 55);
        line(p, 140, 25, 140, 140);
        line(p, 250, 25, 250, 140);
        line(p, 345, 25, 345, 140);
        text(p, "Campo", 55, 47);
        text(p, "Tipo", 175, 47);
        text(p, "Regla", 282, 47);
        text(p, "Ejemplo", 370, 47);
        text(p, "estado", 55, 86);
        text(p, "catálogo", 172, 86);
        text(p, "obligatorio", 276, 86);
        text(p, "ACTIVO", 373, 86);
        text(p, "fecha", 55, 119);
        text(p, "fecha", 178, 119);
        text(p, "válida", 288, 119);
        text(p, "2026-05-13", 360, 119);
    }


    private static void drawDataDictionaryFromConcept(Pane p) {
        rectangle(p, 28, 35, 110, 50, "Cliente");
        ellipseLike(p, 165, 25, 92, 34, "teléfono");
        arrow(p, 138, 60, 165, 43);
        rectangle(p, 300, 22, 145, 40, "telefonoPrincipal");
        rectangle(p, 300, 76, 145, 40, "Texto / obligatorio");
        arrow(p, 257, 43, 300, 42);
        arrow(p, 257, 43, 300, 96);
        text(p, "Concepto", 45, 120);
        text(p, "Dato documentado", 308, 142);
    }

    private static void drawDataDictionaryTraceability(Pane p) {
        rectangle(p, 20, 62, 80, 42, "Campo");
        rectangle(p, 120, 62, 86, 42, "Formulario");
        rectangle(p, 226, 62, 70, 42, "BD");
        rectangle(p, 316, 62, 72, 42, "Reporte");
        rectangle(p, 408, 62, 45, 42, "Regla");
        arrow(p, 100, 83, 120, 83);
        arrow(p, 206, 83, 226, 83);
        arrow(p, 296, 83, 316, 83);
        arrow(p, 388, 83, 408, 83);
        text(p, "Un dato bien definido se puede seguir de punta a punta", 78, 138);
    }

    private static void drawDataDictionaryColumns(Pane p) {
        rectangle(p, 20, 25, 430, 116, "");
        line(p, 20, 54, 450, 54);
        line(p, 105, 25, 105, 141);
        line(p, 190, 25, 190, 141);
        line(p, 275, 25, 275, 141);
        line(p, 360, 25, 360, 141);
        text(p, "Campo", 42, 45);
        text(p, "Descripción", 115, 45);
        text(p, "Tipo", 218, 45);
        text(p, "Regla", 300, 45);
        text(p, "Uso", 390, 45);
        text(p, "imei", 45, 82);
        text(p, "Identifica", 116, 82);
        text(p, "texto", 219, 82);
        text(p, "único si existe", 286, 82);
        text(p, "búsqueda", 378, 82);
        text(p, "estado", 42, 118);
        text(p, "ciclo vida", 116, 118);
        text(p, "catálogo", 210, 118);
        text(p, "valores fijos", 287, 118);
        text(p, "reportes", 381, 118);
    }

    private static void drawDataDictionaryAllowedValues(Pane p) {
        rectangle(p, 32, 35, 120, 45, "estadoOrden");
        rectangle(p, 235, 18, 170, 28, "Recibida");
        rectangle(p, 235, 55, 170, 28, "En reparación");
        rectangle(p, 235, 92, 170, 28, "Entregada");
        rectangle(p, 235, 129, 170, 28, "Anulada");
        arrow(p, 152, 58, 235, 32);
        arrow(p, 152, 58, 235, 69);
        arrow(p, 152, 58, 235, 106);
        arrow(p, 152, 58, 235, 143);
        text(p, "Campo de estado no debe ser texto libre", 70, 150);
    }

    private static void drawDataDictionaryValidation(Pane p) {
        rounded(p, 25, 60, 110, 42, "Crear orden");
        diamond(p, 215, 81, 72, 48, "¿dato\nobligatorio?");
        rounded(p, 330, 38, 95, 34, "Guardar");
        rounded(p, 330, 104, 95, 34, "Mostrar\nerror");
        arrow(p, 135, 81, 179, 81);
        arrow(p, 251, 70, 330, 55);
        arrow(p, 251, 92, 330, 121);
        text(p, "sí", 278, 58);
        text(p, "no", 278, 115);
    }

    private static void drawDataDictionaryCalculatedField(Pane p) {
        rectangle(p, 35, 35, 90, 38, "totalOrden");
        rectangle(p, 35, 105, 90, 38, "totalPagado");
        diamond(p, 238, 86, 70, 48, "cálculo");
        rectangle(p, 345, 62, 105, 45, "saldo\nPendiente");
        arrow(p, 125, 54, 205, 78);
        arrow(p, 125, 124, 205, 94);
        arrow(p, 273, 86, 345, 86);
        text(p, "saldo = total - pagos", 160, 150);
    }

    private static void drawDataDictionarySensitiveData(Pane p) {
        rectangle(p, 38, 50, 105, 50, "cédula\nteléfono");
        rectangle(p, 195, 25, 110, 38, "Recepción");
        rectangle(p, 195, 78, 110, 38, "Técnico");
        rectangle(p, 350, 50, 90, 50, "Reportes\nfinancieros");
        arrow(p, 143, 72, 195, 44);
        line(p, 143, 72, 195, 96);
        arrow(p, 305, 44, 350, 65);
        text(p, "✓", 168, 43);
        text(p, "acceso limitado", 194, 141);
    }

    private static void drawDataDictionaryAuditFields(Pane p) {
        rectangle(p, 30, 35, 120, 44, "Orden\nanulada");
        rectangle(p, 205, 20, 120, 32, "anuladoPor");
        rectangle(p, 205, 65, 120, 32, "fechaAnulacion");
        rectangle(p, 205, 110, 120, 32, "motivoAnulacion");
        arrow(p, 150, 57, 205, 36);
        arrow(p, 150, 57, 205, 81);
        arrow(p, 150, 57, 205, 126);
        text(p, "Trazabilidad humana del cambio crítico", 98, 158);
    }

    private static void drawDataDictionaryCommonErrors(Pane p) {
        rectangle(p, 20, 35, 92, 42, "fecha");
        text(p, "✕", 55, 102);
        rectangle(p, 135, 35, 92, 42, "valor");
        text(p, "✕", 170, 102);
        rectangle(p, 250, 35, 92, 42, "estado");
        text(p, "✕", 285, 102);
        rectangle(p, 365, 35, 92, 42, "dato");
        text(p, "✕", 400, 102);
        text(p, "Mejor: fechaRecepcion, saldoPendiente, estadoOrden", 70, 142);
    }
}
