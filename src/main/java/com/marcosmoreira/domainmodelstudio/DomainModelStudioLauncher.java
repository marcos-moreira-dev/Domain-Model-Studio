package com.marcosmoreira.domainmodelstudio;

import javafx.application.Application;

/**
 * Launcher de escritorio compatible con jpackage.
 *
 * <p>El ejecutable instalado por jpackage no debe apuntar directamente a una clase que
 * extienda {@link javafx.application.Application}. En distribuciones classpath no
 * modulares, el lanzador de Java puede abortar antes de ejecutar la aplicación con el
 * mensaje de componentes JavaFX faltantes, aunque los JAR JavaFX estén en la carpeta
 * {@code app}. Esta clase no extiende Application y delega explícitamente en
 * {@link DomainModelStudioApp}, evitando ese fallo de arranque instalado.</p>
 */
public final class DomainModelStudioLauncher {

    private DomainModelStudioLauncher() {
        // Punto de entrada estático únicamente.
    }

    public static void main(String[] args) {
        Application.launch(DomainModelStudioApp.class, args);
    }
}
