package com.marcosmoreira.domainmodelstudio.presentation.dialogs;

import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceCodeImportPreview;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceCodeImportRequest;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceLanguage;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceRootImportPreview;
import java.util.Optional;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Window;

/** Diálogo de revisión antes de convertir un directorio de código en UML Clases. */
public final class SourceCodeImportPreviewDialog {

    private SourceCodeImportPreviewDialog() {
    }

    public static Optional<SourceCodeImportRequest> showConfirmation(
            Window owner,
            SourceCodeImportPreview preview,
            SourceCodeImportRequest baseRequest
    ) {
        Dialog<SourceCodeImportRequest> dialog = new Dialog<>();
        dialog.setTitle("Vista previa de importación desde código");
        dialog.setHeaderText("Revisa lo detectado antes de generar el diagrama UML.");
        if (owner != null) {
            dialog.initOwner(owner);
        }
        ButtonType importButton = new ButtonType("Generar UML");
        dialog.getDialogPane().getButtonTypes().setAll(importButton, ButtonType.CANCEL);

        CheckBox includeTests = new CheckBox("Incluir también archivos de prueba (*.spec.ts, *Test.java, src/test)");
        includeTests.setTooltip(new javafx.scene.control.Tooltip(
                "Déjalo desmarcado para diagramas de arquitectura del sistema. Márcalo solo si quieres ver clases de test."));
        includeTests.setSelected(baseRequest.includeTests());
        TextArea summary = new TextArea(summaryText(preview));
        summary.setEditable(false);
        summary.setWrapText(true);
        summary.setPrefColumnCount(86);
        summary.setPrefRowCount(18);

        VBox content = new VBox(10,
                new Label("Carpeta: " + preview.projectRoot().toAbsolutePath()),
                includeTests,
                summary);
        content.setPadding(new Insets(8));
        dialog.getDialogPane().setContent(content);
        dialog.setResultConverter(button -> button == importButton
                ? new SourceCodeImportRequest(
                        baseRequest.projectRoot(),
                        baseRequest.preferredLanguageVersions(),
                        includeTests.isSelected())
                : null);
        return dialog.showAndWait();
    }

    private static String summaryText(SourceCodeImportPreview preview) {
        StringBuilder builder = new StringBuilder();
        builder.append("Archivos detectados: ").append(preview.totalFiles()).append('\n');
        builder.append("Rutas ignoradas: ").append(preview.ignoredPathCount()).append('\n');
        builder.append("Lenguajes: ").append(languageSummary(preview)).append("\n\n");
        builder.append("Raíces detectadas:\n");
        for (SourceRootImportPreview root : preview.roots()) {
            builder.append("- ").append(root.kind().displayName())
                    .append(" · ").append(root.displayName())
                    .append(" · ").append(root.totalFiles()).append(" archivos")
                    .append(" · ").append(root.path().toAbsolutePath())
                    .append('\n');
        }
        builder.append("\nVistas sugeridas:\n");
        for (String view : preview.suggestedViews()) {
            builder.append("- ").append(view).append('\n');
        }
        if (!preview.warnings().isEmpty()) {
            builder.append("\nAdvertencias:\n");
            for (String warning : preview.warnings()) {
                builder.append("- ").append(warning).append('\n');
            }
        }
        return builder.toString();
    }

    private static String languageSummary(SourceCodeImportPreview preview) {
        StringBuilder builder = new StringBuilder();
        preview.fileCountByLanguage().forEach((language, count) -> {
            if (builder.length() > 0) {
                builder.append(", ");
            }
            String label = language == SourceLanguage.UNKNOWN ? "Desconocido" : language.displayName();
            builder.append(label).append("=").append(count);
        });
        return builder.length() == 0 ? "sin archivos soportados" : builder.toString();
    }
}
