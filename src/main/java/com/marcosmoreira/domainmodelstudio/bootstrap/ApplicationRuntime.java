package com.marcosmoreira.domainmodelstudio.bootstrap;

import java.util.List;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.stage.WindowEvent;

/**
 * Resultado del arranque compuesto de la aplicación.
 *
 * <p>Contiene solo lo que el punto de entrada JavaFX necesita para montar la ventana:
 * raíz visual, título observable, configuración de tamaño y hojas de estilo.</p>
 */
public record ApplicationRuntime(
        Parent root,
        ObservableValue<String> windowTitleProperty,
        ApplicationWindowConfig windowConfig,
        List<String> stylesheetResources,
        EventHandler<WindowEvent> closeRequestHandler
) {
}
