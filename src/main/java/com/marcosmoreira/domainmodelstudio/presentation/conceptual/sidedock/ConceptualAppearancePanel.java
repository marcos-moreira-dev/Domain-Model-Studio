package com.marcosmoreira.domainmodelstudio.presentation.conceptual.sidedock;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import com.marcosmoreira.domainmodelstudio.presentation.conceptual.ConceptualCanvasLegacyBridge;
import java.util.Objects;
import java.util.function.Consumer;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/** Opciones operativas de apariencia del modelo conceptual dentro del SideDock común. */
final class ConceptualAppearancePanel {

    private final ConceptualCanvasLegacyBridge bridge;
    private final Consumer<NotationType> notationSwitchAction;
    private final VBox root = new VBox(10);
    private final Label activeNotation = new Label();

    ConceptualAppearancePanel(
            ConceptualCanvasLegacyBridge bridge,
            Consumer<NotationType> notationSwitchAction
    ) {
        this.bridge = Objects.requireNonNull(bridge, "bridge");
        this.notationSwitchAction = Objects.requireNonNull(notationSwitchAction, "notationSwitchAction");
        build();
        bridge.currentProjectObservable().addListener((observable, previous, current) -> refresh());
        refresh();
    }

    Parent getRoot() {
        return root;
    }

    private void build() {
        root.getStyleClass().addAll("conceptual-side-dock-panel", "conceptual-appearance-panel");
        root.setPadding(new Insets(10));

        Label lead = new Label("Controla la notación visual del modelo conceptual sin reemplazar el render actual.");
        lead.setWrapText(true);
        lead.getStyleClass().add("conceptual-side-dock-lead");

        activeNotation.setWrapText(true);
        activeNotation.getStyleClass().add("conceptual-active-notation");

        Button chen = notationButton("Usar Chen", NotationType.CHEN);
        Button crowsFoot = notationButton("Usar pata de gallo", NotationType.CROWS_FOOT);
        HBox actions = new HBox(8, chen, crowsFoot);
        actions.getStyleClass().add("conceptual-notation-actions");

        Label note = new Label("La migración conserva ChenDiagramRenderer y CrowsFootDiagramRenderer. "
                + "El cambio de notación sigue pasando por el comando existente del shell.");
        note.setWrapText(true);
        note.getStyleClass().add("conceptual-side-dock-message");

        root.getChildren().addAll(lead, activeNotation, actions, note);
    }

    private Button notationButton(String text, NotationType notation) {
        Button button = new Button(text);
        button.getStyleClass().add("side-dock-action-button");
        button.setOnAction(event -> {
            notationSwitchAction.accept(notation);
            refresh();
        });
        return button;
    }

    private void refresh() {
        DiagramProject project = bridge.currentProject();
        if (project == null) {
            activeNotation.setText("No hay modelo conceptual activo.");
            return;
        }
        activeNotation.setText("Notación activa: " + project.metadata().activeNotation().displayName());
    }
}
