package com.marcosmoreira.domainmodelstudio.presentation.canvas;

import com.marcosmoreira.domainmodelstudio.presentation.toolbar.ToolbarIcon;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Polygon;

/**
 * Vista de inicio del área de trabajo.
 *
 * <p>Se separa del lienzo principal para que el canvas conserve su responsabilidad
 * visual y de interacción sobre diagramas. Esta pantalla usa solo acciones de alto
 * nivel expuestas por el ViewModel.</p>
 */
public final class WelcomeWorkspaceView {

    private final DiagramCanvasViewModel viewModel;

    public WelcomeWorkspaceView(DiagramCanvasViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public Parent build() {
        return buildWelcomeWorkspace();
    }

private Parent buildWelcomeWorkspace() {
    StackPane host = new StackPane();
    host.getStyleClass().add("welcome-start-root");

    Pane footer = buildWelcomeVisualFooter();
    footer.setMouseTransparent(true);
    StackPane.setAlignment(footer, Pos.BOTTOM_CENTER);

    HBox page = new HBox(0);
    page.getStyleClass().add("welcome-start-page");
    page.setPadding(new Insets(70, 70, 150, 70));
    page.setFillHeight(true);

    VBox actions = new VBox(14);
    actions.getStyleClass().add("welcome-start-actions");
    actions.setPrefWidth(330);
    actions.setMinWidth(280);
    Label actionsTitle = new Label("Inicio");
    actionsTitle.getStyleClass().add("welcome-start-section-title");
    actions.getChildren().addAll(
            actionsTitle,
            welcomeActionTile(ToolbarIcon.NEW_PROJECT, "Crear modelo", "Nuevo modelo conceptual.", viewModel::requestNewProjectFromWelcome),
            welcomeActionTile(ToolbarIcon.OPEN_PROJECT, "Abrir proyecto", "Abrir un archivo .dms.", viewModel::requestOpenProjectFromWelcome),
            welcomeActionTile(ToolbarIcon.IMPORT_MODEL, "Importar Markdown", "Generar diagrama desde .md.", viewModel::requestImportMarkdownFromWelcome),
            welcomeActionTile(ToolbarIcon.OPEN_EXAMPLE, "Abrir ejemplo", "Cargar un ejemplo incluido.", viewModel::requestOpenExampleFromWelcome),
            welcomeActionTile(ToolbarIcon.MANUAL, "Guía académica", "Estudiar teoría de diagramas.", viewModel::requestOpenManualFromWelcome)
    );

    VBox center = new VBox(18);
    center.getStyleClass().add("welcome-start-center");
    center.setPadding(new Insets(18, 66, 20, 66));
    HBox.setHgrow(center, Priority.ALWAYS);

    Label eyebrow = new Label("Bienvenido a");
    eyebrow.getStyleClass().add("welcome-start-eyebrow");
    Label title = new Label("Domain Model Studio");
    title.getStyleClass().add("welcome-start-title");
    Label subtitle = new Label("Modela, revisa y exporta diagramas conceptuales.");
    subtitle.getStyleClass().add("welcome-start-subtitle");

    VBox card = new VBox(16);
    card.getStyleClass().add("welcome-start-card");
    card.getChildren().addAll(
            welcomeInfoRow("1", "Crea o importa", "Empieza con un proyecto vacío, un ejemplo o un Markdown."),
            welcomeInfoRow("2", "Edita el diagrama", "Mueve figuras, revisa propiedades y cambia la notación."),
            welcomeInfoRow("3", "Exporta", "Guarda el proyecto y exporta SVG o PNG.")
    );

    Label tip = new Label("Consejo: usa la barra superior para guardar, validar, cambiar notación y exportar.");
    tip.getStyleClass().add("welcome-start-tip");
    tip.setWrapText(true);

    center.getChildren().addAll(eyebrow, title, subtitle, card, tip);

    VBox recent = new VBox(12);
    recent.getStyleClass().add("welcome-start-recent");
    recent.setPrefWidth(330);
    recent.setMinWidth(280);

    Label recentTitle = new Label("Recientes");
    recentTitle.getStyleClass().add("welcome-start-section-title");
    Label emptyText = new Label("Aún no hay proyectos recientes.\nCuando abras o crees proyectos, aparecerán aquí.");
    emptyText.getStyleClass().add("welcome-start-empty-text");
    emptyText.setWrapText(true);
    VBox emptyState = new VBox(12, emptyText);
    emptyState.getStyleClass().add("welcome-start-empty");
    emptyState.setAlignment(Pos.CENTER);
    VBox.setVgrow(emptyState, Priority.ALWAYS);
    recent.getChildren().addAll(recentTitle, emptyState);

    page.getChildren().addAll(actions, center, recent);
    host.getChildren().addAll(footer, page);
    return host;
}

private Button welcomeActionTile(ToolbarIcon iconType, String titleText, String description, Runnable action) {
    Label title = new Label(titleText);
    title.getStyleClass().add("welcome-start-action-title");

    Label detail = new Label(description);
    detail.getStyleClass().add("welcome-start-action-detail");
    detail.setWrapText(true);

    VBox text = new VBox(3, title, detail);
    text.getStyleClass().add("welcome-start-action-copy");

    HBox iconBox = new HBox();
    iconBox.getStyleClass().add("welcome-start-action-icon-box");
    iconBox.setAlignment(Pos.CENTER);
    if (iconType != null) {
        var icon = iconType.imageView();
        icon.getStyleClass().add("welcome-start-action-png");
        icon.setFitWidth(28);
        icon.setFitHeight(28);
        iconBox.getChildren().add(icon);
    }

    HBox row = new HBox(14, iconBox, text);
    row.setAlignment(Pos.CENTER_LEFT);

    Button button = new Button();
    button.setGraphic(row);
    button.getStyleClass().add("welcome-start-action-tile");
    button.setMnemonicParsing(false);
    button.setMaxWidth(Double.MAX_VALUE);
    button.setOnAction(event -> action.run());
    return button;
}

private Label welcomeTab(String text, boolean active) {
    Label label = new Label(text);
    label.getStyleClass().add("welcome-start-tab");
    if (active) {
        label.getStyleClass().add("welcome-start-tab-active");
    }
    return label;
}

private HBox welcomeInfoRow(String iconText, String titleText, String description) {
    Label icon = new Label(iconText);
    icon.getStyleClass().add("welcome-start-card-icon");

    Label title = new Label(titleText);
    title.getStyleClass().add("welcome-start-card-title");

    Label detail = new Label(description);
    detail.getStyleClass().add("welcome-start-card-detail");
    detail.setWrapText(true);

    VBox copy = new VBox(4, title, detail);
    HBox row = new HBox(18, icon, copy);
    row.getStyleClass().add("welcome-start-card-row");
    row.setAlignment(Pos.CENTER_LEFT);
    return row;
}

private Button welcomeLinkButton(String text, Runnable action) {
    Button button = new Button(text);
    button.getStyleClass().add("welcome-link-button");
    button.setMnemonicParsing(false);
    button.setOnAction(event -> action.run());
    return button;
}

private Pane buildWelcomeVisualFooter() {
    Pane footer = new Pane();
    footer.getStyleClass().add("welcome-visual-footer");
    footer.setMinHeight(190);
    footer.setPrefHeight(190);
    footer.setMaxHeight(190);

    Polygon back = new Polygon(
            0, 128, 300, 108, 600, 126, 900, 102, 1200, 122, 1500, 106, 1800, 126, 2100, 112, 2600, 130,
            2600, 190, 0, 190
    );
    back.getStyleClass().add("welcome-wave-back");

    Polygon middle = new Polygon(
            0, 144, 280, 116, 560, 138, 840, 110, 1120, 134, 1400, 108, 1680, 132, 1960, 118, 2600, 144,
            2600, 190, 0, 190
    );
    middle.getStyleClass().add("welcome-wave-middle");

    Polygon front = new Polygon(
            0, 160, 260, 138, 520, 158, 780, 132, 1040, 154, 1300, 136, 1560, 156, 1820, 142, 2600, 160,
            2600, 190, 0, 190
    );
    front.getStyleClass().add("welcome-wave-front");

    footer.getChildren().addAll(back, middle, front);
    return footer;
}

}
