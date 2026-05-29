package com.marcosmoreira.domainmodelstudio.presentation.datadictionary;

import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryDocument;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryEntity;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryField;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Comparator;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Vista principal de lectura del diccionario.
 *
 * <p>El diccionario no se fuerza a canvas: su salida natural es un documento
 * técnico estructurado que luego se corrige desde tablas e inspector.</p>
 */
final class DataDictionaryDocumentPreview {

    private final DataDictionaryViewModel viewModel;
    private final VBox root = new VBox(18);

    DataDictionaryDocumentPreview(DataDictionaryViewModel viewModel) {
        this.viewModel = viewModel;
        root.getStyleClass().add("data-dictionary-document-preview");
        root.setPadding(new Insets(18));
    }

    Parent root() {
        return root;
    }

    void refresh() {
        root.getChildren().clear();
        DataDictionaryDocument document = viewModel.currentDocument();
        if (document == null) {
            root.getChildren().add(emptyMessage("Abre o crea un diccionario de datos para revisar su documento de entrega."));
            return;
        }
        root.getChildren().add(coverCard(document));
        if (document.hasLogoReference()) {
            root.getChildren().add(logoPreview(document.logoReference()));
        }
        if (document.hasIntroduction()) {
            root.getChildren().add(introductionCard(document));
        }
        root.getChildren().add(summaryCard(document));
        if (!document.entities().isEmpty()) {
            root.getChildren().add(entityOverviewCard(document));
        }
        if (document.entities().isEmpty()) {
            root.getChildren().add(emptyMessage("Todavía no hay entidades documentadas."));
            return;
        }
        document.entities().stream()
                .sorted(Comparator.comparing(DataDictionaryEntity::displayName))
                .forEach(entity -> root.getChildren().add(entityCard(entity)));
    }

    private Parent coverCard(DataDictionaryDocument document) {
        VBox cover = new VBox(10);
        cover.getStyleClass().add("data-dictionary-document-cover");
        cover.getChildren().add(documentTitle("Diccionario de datos — " + document.projectName()));
        cover.getChildren().add(wrappingLabel(
                "Documento técnico de entidades, campos, tipos, restricciones y reglas de datos. "
                        + "Complementa los diagramas, pero no sustituye un modelo ER ni una base física.",
                "data-dictionary-document-lead"));
        cover.getChildren().add(documentMeta(document));
        return cover;
    }

    private Parent documentMeta(DataDictionaryDocument document) {
        FlowPane meta = new FlowPane(8, 8);
        meta.getStyleClass().add("data-dictionary-document-meta");
        meta.getChildren().addAll(
                chip("Cliente", DataDictionaryLabels.optional(document.clientName())),
                chip("Organización", DataDictionaryLabels.optional(document.organizationName())),
                chip("Autor", DataDictionaryLabels.optional(document.author())),
                chip("Versión", document.version()),
                chip("Fecha", document.documentDate().toString()),
                chip("Estado", DataDictionaryLabels.label(document.status())));
        return meta;
    }

    private Parent logoPreview(String logoReference) {
        VBox card = card("Logo de portada");
        card.getStyleClass().add("data-dictionary-logo-placeholder");
        Image image = loadLogo(logoReference);
        if (image == null || image.isError()) {
            card.getChildren().add(wrappingLabel("Logo seleccionado, pero no se encontró una imagen PNG/JPG disponible.",
                    "data-dictionary-document-note"));
            return card;
        }
        ImageView view = new ImageView(image);
        view.getStyleClass().add("data-dictionary-logo-image");
        view.setPreserveRatio(true);
        view.setFitWidth(220.0);
        view.setFitHeight(110.0);
        HBox centeredLogo = new HBox(view);
        centeredLogo.setAlignment(Pos.CENTER);
        centeredLogo.setMaxWidth(Double.MAX_VALUE);
        card.getChildren().add(centeredLogo);
        return card;
    }

    private Image loadLogo(String logoReference) {
        try {
            Path path = Path.of(logoReference.strip());
            if (!Files.isRegularFile(path)) {
                return null;
            }
            return new Image(path.toUri().toString(), 180.0, 90.0, true, true, false);
        } catch (InvalidPathException exception) {
            return null;
        }
    }

    private Parent introductionCard(DataDictionaryDocument document) {
        VBox card = card("Introducción");
        card.getChildren().add(wrappingLabel(document.introduction(), "data-dictionary-document-text"));
        return card;
    }

    private Parent summaryCard(DataDictionaryDocument document) {
        VBox card = card("Resumen ejecutivo");
        FlowPane metrics = new FlowPane(8, 8);
        metrics.getStyleClass().add("data-dictionary-summary-metrics");
        metrics.getChildren().addAll(
                metric("Entidades", Integer.toString(document.entityCount())),
                metric("Campos", Integer.toString(document.fieldCount())),
                metric("Con PK", Long.toString(document.entities().stream()
                        .filter(DataDictionaryEntity::hasPrimaryKey).count()))
        );
        Label note = new Label("Salida formal: PDF para entrega y Markdown para round-trip importable. "
                + "La tabla de campos sirve para revisión documental, no para convertir el diccionario en canvas.");
        note.setWrapText(true);
        note.getStyleClass().add("data-dictionary-document-note");
        card.getChildren().addAll(metrics, note);
        return card;
    }

    private Parent entityOverviewCard(DataDictionaryDocument document) {
        VBox card = card("Tabla general de entidades");
        VBox rows = new VBox(7);
        rows.getStyleClass().add("data-dictionary-entity-overview");
        document.entities().stream()
                .sorted(Comparator.comparing(DataDictionaryEntity::displayName))
                .forEach(entity -> rows.getChildren().add(entityOverviewRow(entity)));
        card.getChildren().add(rows);
        return card;
    }

    private Parent entityOverviewRow(DataDictionaryEntity entity) {
        HBox row = new HBox(8);
        row.getStyleClass().add("data-dictionary-entity-overview-row");
        row.setAlignment(Pos.BASELINE_LEFT);
        Label name = wrappingLabel(entity.displayName() + " (`" + entity.technicalName() + "`)",
                "data-dictionary-entity-overview-name");
        Label kind = wrappingLabel(DataDictionaryLabels.label(entity.kind()), "data-dictionary-document-chip");
        Label fields = wrappingLabel(entity.fieldCount() + " campos", "data-dictionary-document-chip");
        Label module = wrappingLabel("Módulo: " + DataDictionaryLabels.optional(entity.moduleName()),
                "data-dictionary-document-chip");
        row.getChildren().addAll(name, kind, fields, module);
        return row;
    }

    private Parent entityCard(DataDictionaryEntity entity) {
        VBox card = card(entity.displayName());
        if (entity.equals(viewModel.selectedEntityProperty().get())) {
            card.getStyleClass().add("data-dictionary-document-card-selected");
        }
        card.getChildren().add(documentLine("Nombre técnico", entity.technicalName()));
        card.getChildren().add(documentLine("Tipo", DataDictionaryLabels.label(entity.kind())));
        card.getChildren().add(documentLine("Módulo", DataDictionaryLabels.optional(entity.moduleName())));
        card.getChildren().add(documentLine("Estado", DataDictionaryLabels.label(entity.status())));
        if (!entity.description().isBlank()) {
            card.getChildren().add(wrappingLabel("Descripción: " + entity.description(), "data-dictionary-document-text"));
        }
        if (entity.fields().isEmpty()) {
            card.getChildren().add(emptyMessage("Sin campos documentados todavía."));
        } else {
            VBox fields = new VBox(6);
            fields.getStyleClass().add("data-dictionary-field-list-preview");
            entity.fields().forEach(field -> fields.getChildren().add(fieldPreview(field)));
            card.getChildren().add(fields);
        }
        if (!entity.notes().isBlank()) {
            card.getChildren().add(wrappingLabel("Notas: " + entity.notes(), "data-dictionary-document-note"));
        }
        return card;
    }

    private Parent fieldPreview(DataDictionaryField field) {
        VBox box = new VBox(5);
        box.getStyleClass().add("data-dictionary-field-preview");
        if (field.equals(viewModel.selectedFieldProperty().get())) {
            box.getStyleClass().add("data-dictionary-field-preview-selected");
        }
        Label title = wrappingLabel(field.displayName() + " · " + DataDictionaryLabels.label(field.logicalType()),
                "data-dictionary-field-preview-title");
        Label details = wrappingLabel("`" + field.technicalName() + "` · "
                        + DataDictionaryLabels.constraintSummary(field) + " · visible: "
                        + DataDictionaryLabels.visibilitySummary(field) + " · editable: "
                        + (field.userEditable() ? "sí" : "no"),
                "data-dictionary-document-text");
        box.getChildren().addAll(title, details);
        if (field.hasPhysicalTypeSuggestion()) {
            box.getChildren().add(wrappingLabel("Tipo físico sugerido: " + field.physicalTypeSuggestion(),
                    "data-dictionary-document-note"));
        }
        if (field.isForeignKey()) {
            box.getChildren().add(wrappingLabel("Referencia: " + field.foreignKeyReference(),
                    "data-dictionary-document-note"));
        }
        if (!field.description().isBlank()) {
            box.getChildren().add(wrappingLabel(field.description(), "data-dictionary-document-note"));
        }
        if (field.hasBusinessRule()) {
            box.getChildren().add(wrappingLabel("Regla: " + field.businessRule(),
                    "data-dictionary-document-note"));
        }
        if (field.hasValidationRule()) {
            box.getChildren().add(wrappingLabel("Validación: " + field.validationRule(),
                    "data-dictionary-document-note"));
        }
        return box;
    }

    private Label documentTitle(String text) {
        Label label = wrappingLabel(text, "data-dictionary-document-title");
        label.setMaxWidth(Double.MAX_VALUE);
        return label;
    }

    private VBox card(String title) {
        VBox card = new VBox(10);
        card.getStyleClass().add("data-dictionary-document-card");
        card.setPadding(new Insets(14));
        card.getChildren().add(wrappingLabel(title, "data-dictionary-document-card-title"));
        return card;
    }

    private Parent metric(String key, String value) {
        VBox metric = new VBox(2);
        metric.getStyleClass().add("data-dictionary-summary-metric");
        Label valueLabel = wrappingLabel(value, "data-dictionary-summary-metric-value");
        Label keyLabel = wrappingLabel(key, "data-dictionary-summary-metric-label");
        metric.getChildren().addAll(valueLabel, keyLabel);
        return metric;
    }

    private Parent documentLine(String key, String value) {
        HBox line = new HBox(6);
        line.setAlignment(Pos.BASELINE_LEFT);
        Label keyLabel = new Label(key + ":");
        keyLabel.getStyleClass().add("data-dictionary-document-key");
        Label valueLabel = wrappingLabel(value, "data-dictionary-document-value");
        line.getChildren().addAll(keyLabel, valueLabel);
        return line;
    }

    private Parent chip(String key, String value) {
        Label label = new Label(key + ": " + value);
        label.getStyleClass().add("data-dictionary-document-chip");
        return label;
    }

    private Label emptyMessage(String message) {
        return wrappingLabel(message, "data-dictionary-empty-message");
    }

    private Label wrappingLabel(String text, String styleClass) {
        Label label = new Label(text == null || text.isBlank() ? "—" : text);
        label.setWrapText(true);
        label.getStyleClass().add(styleClass);
        return label;
    }
}
