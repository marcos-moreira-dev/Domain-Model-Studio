package com.marcosmoreira.domainmodelstudio.presentation.toolbar;

import javafx.scene.Parent;
import javafx.scene.layout.VBox;

/**
 * Contenedor de barras superiores.
 *
 * <p>Separa la barra global del proyecto de la barra contextual del artefacto activo.
 * La clase queda como punto de entrada estable para el shell, mientras las responsabilidades
 * visuales viven en vistas pequeñas y trazables.</p>
 *
 * <p>Rol de zonas: la toolbar ejecuta acciones operativas frecuentes; el SideDock orienta,
 * navega, inspecciona y configura; el workspace porta el resultado principal del proyecto activo.</p>
 */
public final class MainToolbarView {

    private final MainToolbarViewModel viewModel;
    private final VBox root = new VBox();

    public MainToolbarView(MainToolbarViewModel viewModel) {
        this.viewModel = viewModel;
        build();
    }

    public Parent getRoot() {
        return root;
    }

    private void build() {
        root.getStyleClass().add("toolbar-stack");
        root.getChildren().addAll(
                new GlobalToolbarView(viewModel).getRoot(),
                new ContextualToolbarView(viewModel, new DefaultDiagramToolbarActionProvider()).getRoot()
        );
    }
}
