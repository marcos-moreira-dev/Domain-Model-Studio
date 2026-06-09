package com.marcosmoreira.domainmodelstudio.presentation.sidedock;

import com.marcosmoreira.domainmodelstudio.presentation.workspace.WorkspaceKind;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/** Ayuda operativa breve del módulo activo; complementa la referencia académica. */
final class OperationalHelpContent {

    private static final String LOGICAL_BUSINESS_DOCUMENT_SCOPE = "LOGICAL_BUSINESS_DOCUMENT";

    private OperationalHelpContent() {
    }

    static Parent create(WorkspaceKind kind, String title, String subtitle) {
        OperationalHelpProfile profile = OperationalHelpCatalog.profile(kind, title, subtitle);
        VBox root = new VBox(10);
        root.getProperties().put("logicalBusinessScope", LOGICAL_BUSINESS_DOCUMENT_SCOPE);
        root.getStyleClass().add("module-operational-help");
        root.getChildren().add(label(profile.title(), "module-help-title"));
        if (!profile.subtitle().isBlank()) {
            root.getChildren().add(label(profile.subtitle(), "module-help-text"));
        }
        root.getChildren().add(label(
                "Ayuda de herramienta: operación del módulo activo. La guía académica y los recursos IA viven en sus espacios propios.",
                "module-help-note"
        ));
        root.getChildren().add(section("Qué puedes hacer aquí"));
        for (OperationalHelpSection section : profile.sections()) {
            root.getChildren().add(section(section.title()));
            for (String line : section.items()) {
                root.getChildren().add(bullet(line));
            }
        }
        root.getChildren().add(section("Capas de consulta"));
        root.getChildren().add(bullet("SideDock: uso de la herramienta, selección, inspección y revisión inmediata."));
        root.getChildren().add(bullet("Guía académica: teoría, notación, fundamentos y errores conceptuales."));
        root.getChildren().add(bullet("Recursos IA: plantillas y ejemplos para generar Markdown compatible y revisable."));
        return root;
    }

    private static Label section(String text) {
        return label(text, "module-help-section");
    }

    private static Label bullet(String text) {
        return label("• " + text, "module-help-text");
    }

    private static Label label(String text, String styleClass) {
        Label label = new Label(text == null ? "" : text.strip());
        label.setWrapText(true);
        label.getStyleClass().add(styleClass);
        return label;
    }
}
