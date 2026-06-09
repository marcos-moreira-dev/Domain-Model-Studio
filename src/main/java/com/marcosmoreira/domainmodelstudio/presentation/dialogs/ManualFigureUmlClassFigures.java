package com.marcosmoreira.domainmodelstudio.presentation.dialogs;

import static com.marcosmoreira.domainmodelstudio.presentation.dialogs.ManualFigureDrawingSupport.*;

import javafx.scene.layout.Pane;

/** Figuras didácticas para el capítulo premium de UML Clases. */
final class ManualFigureUmlClassFigures {

    private ManualFigureUmlClassFigures() {
    }

    static boolean draw(String figureId, Pane canvas) {
        switch (figureId) {
            case "uml-class-anatomy" -> drawAnatomy(canvas);
            case "uml-class-association-multiplicity" -> drawAssociationMultiplicity(canvas);
            case "uml-class-composition" -> drawComposition(canvas);
            case "uml-class-inheritance-interface" -> drawInheritanceInterface(canvas);
            case "uml-class-packages" -> drawPackages(canvas);
            case "uml-class-domain-dto-service" -> drawDomainDtoService(canvas);
            case "uml-class-common-errors" -> drawCommonErrors(canvas);
            default -> {
                return false;
            }
        }
        return true;
    }

    private static void drawAnatomy(Pane p) {
        rectangle(p, 135, 20, 205, 130, "");
        line(p, 135, 54, 340, 54);
        line(p, 135, 94, 340, 94);
        text(p, "OrdenReparacion", 183, 42);
        text(p, "- numero: String", 154, 76);
        text(p, "- estado: EstadoOrden", 154, 90);
        text(p, "+ registrarPago()", 154, 118);
        text(p, "+ cerrarOrden()", 154, 136);
        text(p, "nombre", 46, 42);
        text(p, "atributos", 44, 79);
        text(p, "operaciones", 34, 125);
        arrow(p, 94, 38, 135, 38);
        arrow(p, 98, 80, 135, 80);
        arrow(p, 112, 123, 135, 123);
    }

    private static void drawAssociationMultiplicity(Pane p) {
        rectangle(p, 45, 62, 105, 48, "Cliente");
        rectangle(p, 310, 62, 130, 48, "Orden\nReparacion");
        line(p, 150, 86, 310, 86);
        text(p, "1", 160, 76);
        text(p, "0..*", 278, 76);
        text(p, "registra", 210, 76);
        text(p, "Un cliente puede tener muchas órdenes", 115, 145);
    }

    private static void drawComposition(Pane p) {
        rectangle(p, 40, 62, 135, 48, "Orden\nReparacion");
        rectangle(p, 315, 62, 125, 48, "Detalle\nRepuesto");
        diamond(p, 198, 86, 28, 22, "");
        line(p, 175, 86, 184, 86);
        line(p, 212, 86, 315, 86);
        text(p, "1", 182, 70);
        text(p, "1..*", 282, 70);
        text(p, "parte fuerte del ciclo de vida", 132, 145);
    }

    private static void drawInheritanceInterface(Pane p) {
        rectangle(p, 52, 30, 90, 34, "Usuario");
        rectangle(p, 20, 112, 86, 34, "Técnico");
        rectangle(p, 128, 112, 105, 34, "Admin");
        line(p, 97, 64, 63, 112);
        line(p, 97, 64, 181, 112);
        text(p, "herencia", 72, 92);
        rectangle(p, 300, 30, 120, 34, "<<interface>>\nNotificador");
        rectangle(p, 275, 112, 75, 34, "Email");
        rectangle(p, 380, 112, 85, 34, "WhatsApp");
        line(p, 335, 112, 350, 64);
        line(p, 420, 112, 370, 64);
        text(p, "contrato", 350, 92);
    }

    private static void drawPackages(Pane p) {
        rectangle(p, 22, 28, 132, 120, "");
        text(p, "clientes", 55, 50);
        rectangle(p, 42, 70, 92, 26, "Cliente");
        rectangle(p, 42, 108, 92, 26, "Contacto");
        rectangle(p, 182, 28, 132, 120, "");
        text(p, "reparaciones", 205, 50);
        rectangle(p, 202, 70, 92, 26, "Orden");
        rectangle(p, 202, 108, 92, 26, "Diagnóstico");
        rectangle(p, 342, 28, 105, 120, "");
        text(p, "pagos", 374, 50);
        rectangle(p, 358, 82, 72, 26, "Pago");
        text(p, "paquetes reducen la maraña visual", 112, 165);
    }

    private static void drawDomainDtoService(Pane p) {
        rectangle(p, 22, 52, 95, 48, "DTO\nRequest");
        rectangle(p, 156, 44, 110, 64, "Pago\nService");
        rectangle(p, 300, 52, 110, 48, "Orden\nDominio");
        cylinder(p, 420, 126, "Repo");
        arrow(p, 117, 76, 156, 76);
        arrow(p, 266, 76, 300, 76);
        arrow(p, 355, 100, 400, 116);
        text(p, "transporta", 105, 64);
        text(p, "coordina", 224, 64);
        text(p, "reglas", 318, 35);
        text(p, "persistencia", 350, 158);
    }

    private static void drawCommonErrors(Pane p) {
        text(p, "MAL", 40, 28);
        rectangle(p, 22, 48, 118, 30, "tabla_cliente");
        rectangle(p, 22, 88, 118, 30, "PantallaPago");
        rectangle(p, 22, 128, 118, 30, "MegaManager");
        text(p, "BIEN", 248, 28);
        rectangle(p, 220, 48, 105, 34, "Cliente");
        rectangle(p, 345, 48, 105, 34, "PagoService");
        rectangle(p, 280, 116, 105, 34, "Orden");
        line(p, 325, 65, 345, 65);
        line(p, 310, 82, 310, 116);
        text(p, "responsabilidad clara", 260, 165);
    }
}
