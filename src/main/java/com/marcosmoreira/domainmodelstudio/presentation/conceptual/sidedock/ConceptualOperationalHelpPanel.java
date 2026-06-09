package com.marcosmoreira.domainmodelstudio.presentation.conceptual.sidedock;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/** Ayuda operativa del SideDock para el modelo conceptual. No sustituye la guía académica. */
final class ConceptualOperationalHelpPanel {

    private ConceptualOperationalHelpPanel() {
    }

    static Parent create() {
        VBox root = new VBox(10);
        root.getStyleClass().addAll("conceptual-side-dock-panel", "conceptual-operational-help-panel");
        root.setPadding(new Insets(10));
        root.getChildren().addAll(
                lead("Ayuda operativa del modelo conceptual"),
                paragraph("Usa este proyecto para modelar entidades, atributos, relaciones y cardinalidades del dominio. "
                        + "La vista conserva el dibujo ER actual mientras se integra al workspace común."),
                section("1. Estructura", "Selecciona entidades, atributos o relaciones desde el árbol. "
                        + "La selección se refleja en el canvas y en Propiedades."),
                section("2. Propiedades", "Edita nombres, descripciones, colores, cardinalidades y estilos del elemento seleccionado."),
                section("3. Validación", "Revisa advertencias como entidades fuertes sin clave primaria, atributos duplicados o relaciones inconsistentes."),
                section("4. Apariencia", "Cambia entre notación Chen y pata de gallo sin salir del SideDock."),
                section("5. Exportación", "Usa las acciones de exportación del programa para obtener Markdown, PNG o SVG. "
                        + "Guardar como .dms conserva el proyecto editable."),
                paragraph("La teoría sobre modelos conceptuales permanece en la Guía académica del menú Ayuda; este panel solo explica operación de la herramienta.")
        );
        return root;
    }

    private static Label lead(String text) {
        Label label = paragraph(text);
        label.getStyleClass().add("conceptual-side-dock-title");
        return label;
    }

    private static Label section(String title, String body) {
        Label label = paragraph(title + " — " + body);
        label.getStyleClass().add("conceptual-side-dock-section");
        return label;
    }

    private static Label paragraph(String text) {
        Label label = new Label(text);
        label.setWrapText(true);
        label.getStyleClass().add("conceptual-side-dock-message");
        return label;
    }
}
