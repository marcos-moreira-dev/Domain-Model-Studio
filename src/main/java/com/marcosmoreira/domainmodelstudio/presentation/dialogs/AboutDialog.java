package com.marcosmoreira.domainmodelstudio.presentation.dialogs;

import javafx.scene.control.Alert;

/** Diálogo simple de información de la aplicación. */
public final class AboutDialog {

    private AboutDialog() {
    }

    public static void show() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Acerca de Domain Model Studio");
        alert.setHeaderText("Domain Model Studio");
        alert.setContentText("Herramienta desktop para convertir Markdown estructurado "
                + "en diagramas conceptuales editables y exportables a SVG/PNG.\n\n"
                + "Pensada para levantamiento de información, modelado de dominio "
                + "y documentación técnica de sistemas administrativos.");
        alert.showAndWait();
    }
}
