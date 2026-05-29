package com.marcosmoreira.domainmodelstudio.presentation.toolbar;

import javafx.beans.binding.BooleanExpression;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Tooltip;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

/** Barra principal con acciones globales del proyecto y la aplicación. */
final class GlobalToolbarView {

    private final MainToolbarViewModel viewModel;
    private final ScrollPane root = new ScrollPane();
    private final HBox toolbar = new HBox();

    GlobalToolbarView(MainToolbarViewModel viewModel) {
        this.viewModel = viewModel;
        build();
    }

    Parent getRoot() {
        return root;
    }

    private void build() {
        configureScroll(root);
        root.getStyleClass().add("global-toolbar-scroll");

        toolbar.getStyleClass().addAll("main-toolbar", "global-toolbar");
        toolbar.setPadding(new Insets(5, 7, 5, 7));
        toolbar.setMinWidth(Region.USE_PREF_SIZE);
        toolbar.setPrefWidth(Region.USE_COMPUTED_SIZE);
        toolbar.setFillHeight(true);
        toolbar.getChildren().addAll(
                groupLabel("Proyecto"),
                button("Nuevo", "Crea un proyecto vacío para empezar un modelo, diagrama, documento o matriz desde cero.", viewModel::newProject, Width.NORMAL, null, ToolbarIcon.NEW_PROJECT),
                button("Abrir", "Abre un archivo .dms existente y restaura sus pestañas, contenido y estado de trabajo.", viewModel::openProject, Width.NORMAL, null, ToolbarIcon.OPEN_PROJECT),
                button("Guardar", "Guarda el proyecto activo conservando cambios de modelo, documentos, layouts y metadatos.", viewModel::saveProject, Width.NORMAL, viewModel.saveableProjectClosed(), ToolbarIcon.SAVE_PROJECT),
                button("Cerrar", "Cierra el proyecto activo; si hay cambios pendientes, se solicitará confirmación antes de salir.", viewModel::closeProject, Width.NORMAL, viewModel.saveableProjectClosed(), ToolbarIcon.CLOSE_PROJECT),
                separator(),

                groupLabel("Entrada"),
                button("Importar", "Importa Markdown estructurado para construir o actualizar el artefacto activo desde texto revisable.", viewModel::importMarkdown, Width.NORMAL, null, ToolbarIcon.IMPORT_MODEL),
                button("Carpeta MD", "Abre una carpeta raíz con archivos Markdown compatibles y carga cada proyecto importable en una pestaña.", viewModel::importMarkdownFolder, Width.NORMAL, null, ToolbarIcon.IMPORT_MODEL),
                button("Ejemplo", "Abre el selector de ejemplos oficiales para estudiar o probar cada tipo de diagrama disponible.", viewModel::openExampleProject, Width.NORMAL, null, ToolbarIcon.OPEN_EXAMPLE),
                button("Recursos IA", "Exporta gramáticas, plantillas y ejemplos útiles para pedir a una IA contenido compatible con el programa.", viewModel::exportAiResources, Width.WIDE, null, ToolbarIcon.AI_RESOURCES),
                separator(),

                groupLabel("Editar"),
                button("Deshacer", "Revierte la última operación disponible del modelo activo cuando el editor soporta historial.",
                        viewModel::undo, Width.NORMAL, viewModel.saveableProjectClosed(), ToolbarIcon.UNDO_CHANGE),
                button("Rehacer", "Reaplica una operación deshecha en el modelo activo cuando el editor soporta historial.",
                        viewModel::redo, Width.NORMAL, viewModel.saveableProjectClosed(), ToolbarIcon.REDO_CHANGE),
                separator(),

                groupLabel("Exportar"),
                button("Exportar abiertos", "Genera salidas de entrega para los proyectos abiertos según los formatos soportados por cada tipo.", viewModel::exportClientBatch,
                        Width.WIDE, viewModel.projectClosed(), ToolbarIcon.EXPORT_OPEN_PROJECTS)
        );
        root.setContent(toolbar);
    }

    private static void configureScroll(ScrollPane scrollPane) {
        scrollPane.getStyleClass().add("main-toolbar-scroll");
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(false);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setPannable(false);
        scrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
            double delta = Math.abs(event.getDeltaX()) > Math.abs(event.getDeltaY()) ? event.getDeltaX() : event.getDeltaY();
            if (Math.abs(delta) < 0.01) {
                return;
            }
            scrollPane.setHvalue(Math.max(0.0, Math.min(1.0, scrollPane.getHvalue() - delta / 900.0)));
            event.consume();
        });
    }

    private Separator separator() {
        Separator separator = new Separator(Orientation.VERTICAL);
        separator.getStyleClass().add("toolbar-separator");
        return separator;
    }

    private Label groupLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("toolbar-group-label");
        return label;
    }

    private Button button(String text, String tooltip, Runnable action, Width width, BooleanExpression disabledWhen, ToolbarIcon icon) {
        Button button = new Button(text);
        button.getStyleClass().add("toolbar-button");
        if (width.styleClass != null) {
            button.getStyleClass().add(width.styleClass);
        }
        applyButtonWidth(button, text, width.minWidth, width.prefWidth);
        button.setTooltip(new Tooltip(tooltip));
        button.setGraphic(icon.imageView());
        button.setContentDisplay(text == null || text.isBlank() ? ContentDisplay.GRAPHIC_ONLY : ContentDisplay.LEFT);
        button.setGraphicTextGap(5);
        button.setOnAction(event -> action.run());
        if (disabledWhen != null) {
            button.disableProperty().bind(disabledWhen);
        }
        return button;
    }


    private void applyButtonWidth(Button button, String text, double minWidth, double prefWidth) {
        double computedMinWidth = ToolbarButtonSizingPolicy.minimumWidth(text, minWidth);
        double computedPrefWidth = ToolbarButtonSizingPolicy.preferredWidth(text, prefWidth);
        button.setMinWidth(computedMinWidth);
        button.setPrefWidth(computedPrefWidth);
    }

    private enum Width {
        NORMAL(112, 128, "toolbar-button-normal"),
        WIDE(160, 178, "toolbar-button-wide");

        private final double minWidth;
        private final double prefWidth;
        private final String styleClass;

        Width(double minWidth, double prefWidth, String styleClass) {
            this.minWidth = minWidth;
            this.prefWidth = prefWidth;
            this.styleClass = styleClass;
        }
    }
}
