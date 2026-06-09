package com.marcosmoreira.domainmodelstudio.bootstrap;

import com.marcosmoreira.domainmodelstudio.application.ApplicationServices;

import com.marcosmoreira.domainmodelstudio.presentation.PresentationCompositionRoot;
import com.marcosmoreira.domainmodelstudio.presentation.shell.MainShellModule;
import java.util.List;

/**
 * Ensambla Domain Model Studio sin mezclar responsabilidades en el punto de entrada.
 *
 * <p>Este objeto es el composition root principal: crea adaptadores de infraestructura,
 * casos de uso y composición de presentación. Cuando existan más servicios, deberán entrar
 * por factories pequeñas y explícitas, no por constructores improvisados en JavaFX.</p>
 */
public final class ApplicationBootstrap {

    private static final String APP_STYLESHEET = "/css/app-light.css";

    private final ApplicationWindowConfig windowConfig;

    private ApplicationBootstrap(ApplicationWindowConfig windowConfig) {
        this.windowConfig = windowConfig;
    }

    public static ApplicationBootstrap createDefault() {
        return new ApplicationBootstrap(ApplicationWindowConfig.desktopDefault());
    }

    public ApplicationRuntime bootstrap() {
        InfrastructureServices infrastructureServices = new InfrastructureServicesFactory().create();
        ApplicationServices applicationServices = new ApplicationServicesFactory(infrastructureServices).create();
        MainShellModule mainShell = new PresentationCompositionRoot(applicationServices).createMainShell();

        return new ApplicationRuntime(
                mainShell.view().getRoot(),
                mainShell.viewModel().windowTitleProperty(),
                windowConfig,
                List.of(APP_STYLESHEET),
                mainShell.viewModel()::handleWindowClose
        );
    }
}
