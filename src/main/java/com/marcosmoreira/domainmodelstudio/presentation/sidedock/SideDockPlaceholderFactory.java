package com.marcosmoreira.domainmodelstudio.presentation.sidedock;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/** Vistas ligeras para módulos laterales auxiliares que aún no tienen editor propio. */
public final class SideDockPlaceholderFactory {

    private SideDockPlaceholderFactory() {
    }

    public static Parent viewportHelp() {
        return textPanel(
                "Vista del área de trabajo",
                "Usa este módulo como acceso contextual a las acciones de vista: ajustar al contenido, centrar y revisar el zoom desde la barra correspondiente."
        );
    }

    public static Parent contextualHelp(String title) {
        String cleanTitle = title == null || title.isBlank() ? "esta herramienta" : title.strip();
        return textPanel(
                "Ayuda contextual",
                "Consulta la referencia de " + cleanTitle + " desde el menú Ayuda. Este espacio queda reservado para la ayuda académica contextual del módulo activo."
        );
    }

    private static Parent textPanel(String title, String body) {
        VBox box = new VBox(8);
        box.getStyleClass().add("side-dock-text-panel");
        box.setPadding(new Insets(10));
        Label heading = new Label(title);
        heading.getStyleClass().add("side-dock-text-title");
        Label content = new Label(body);
        content.getStyleClass().add("side-dock-text-body");
        content.setWrapText(true);
        box.getChildren().addAll(heading, content);
        return box;
    }
}
