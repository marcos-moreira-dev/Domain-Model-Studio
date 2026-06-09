package com.marcosmoreira.domainmodelstudio.presentation.shell;

import java.util.function.Consumer;
import javafx.scene.Parent;
import javafx.stage.Stage;

/** Acción visual de shell para alternar pantalla completa sin mezclarla con comandos de dominio. */
final class ShellFullScreenAction {

    private ShellFullScreenAction() {
    }

    static void toggle(Parent root, Consumer<String> statusConsumer) {
        if (root.getScene() == null || !(root.getScene().getWindow() instanceof Stage stage)) {
            statusConsumer.accept("La ventana aún no está lista para alternar pantalla completa.");
            return;
        }
        boolean nextFullScreen = !stage.isFullScreen();
        stage.setFullScreen(nextFullScreen);
        statusConsumer.accept(nextFullScreen ? "Pantalla completa activada." : "Pantalla completa desactivada.");
    }
}
