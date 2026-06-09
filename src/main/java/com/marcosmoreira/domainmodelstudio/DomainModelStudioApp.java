package com.marcosmoreira.domainmodelstudio;

import com.marcosmoreira.domainmodelstudio.bootstrap.ApplicationBootstrap;
import com.marcosmoreira.domainmodelstudio.bootstrap.ApplicationRuntime;
import javafx.application.Application;
import java.io.InputStream;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Punto de entrada JavaFX de Domain Model Studio.
 *
 * <p>Esta clase se mantiene intencionalmente pequeña. Su única responsabilidad es iniciar
 * JavaFX, pedir al composition root una aplicación ya compuesta y montar la escena en el
 * {@link Stage}. No instancia ViewModels, servicios de aplicación ni adaptadores de
 * infraestructura de forma directa.</p>
 */
public final class DomainModelStudioApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        ApplicationRuntime runtime = ApplicationBootstrap.createDefault().bootstrap();

        Scene scene = new Scene(
                runtime.root(),
                runtime.windowConfig().defaultWidth(),
                runtime.windowConfig().defaultHeight()
        );
        loadStylesheets(scene, runtime);

        primaryStage.titleProperty().bind(runtime.windowTitleProperty());
        primaryStage.setMinWidth(runtime.windowConfig().minimumWidth());
        primaryStage.setMinHeight(runtime.windowConfig().minimumHeight());
        primaryStage.setScene(scene);
        loadWindowIcon(primaryStage);
        primaryStage.setOnCloseRequest(runtime.closeRequestHandler());
        primaryStage.show();
    }

    private void loadStylesheets(Scene scene, ApplicationRuntime runtime) {
        for (String stylesheetResource : runtime.stylesheetResources()) {
            scene.getStylesheets().add(getClass().getResource(stylesheetResource).toExternalForm());
        }
    }

    private void loadWindowIcon(Stage primaryStage) {
        try (InputStream stream = getClass().getResourceAsStream("/branding/domain-model-studio-icon.png")) {
            if (stream != null) {
                primaryStage.getIcons().add(new Image(stream));
            }
        } catch (Exception ignored) {
            // El icono no es crítico para el arranque de la aplicación.
        }
    }
}
