package com.marcosmoreira.domainmodelstudio.presentation.datadictionary;

import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryEntity;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryField;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataEntityKind;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;

/** Índice documental con búsqueda y secciones por tipo de entidad. */
final class DataDictionaryEntityIndexPanel {

    private final DataDictionaryViewModel viewModel;
    private final Runnable onEntitySelected;
    private final VBox root = new VBox(8);
    private final TextField searchField = new TextField();
    private final Label summary = new Label();
    private final VBox sections = new VBox(8);
    private final Set<DataEntityKind> expandedKinds = new LinkedHashSet<>();

    DataDictionaryEntityIndexPanel(DataDictionaryViewModel viewModel, Runnable onEntitySelected) {
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
        this.onEntitySelected = Objects.requireNonNull(onEntitySelected, "onEntitySelected");
        build();
        refresh();
    }

    Parent root() {
        return root;
    }

    void refresh() {
        refreshSummary();
        refreshSections();
    }

    private void build() {
        root.getStyleClass().add("data-dictionary-section");
        root.setPadding(new Insets(10));
        Label title = new Label("Índice documental");
        title.getStyleClass().add("data-dictionary-section-title");
        Label note = helper("Selecciona una entidad para revisar sus campos, metadatos y reglas de datos.");
        summary.getStyleClass().add("data-dictionary-index-summary");
        summary.setWrapText(true);
        searchField.setPromptText("Buscar entidad, técnico, módulo, tipo o campo...");
        searchField.getStyleClass().add("data-dictionary-search-field");
        searchField.textProperty().addListener((observable, previous, current) -> refreshSections());
        sections.getStyleClass().add("data-dictionary-disclosure-list");
        root.getChildren().addAll(title, note, summary, searchField, sections);
    }

    private void refreshSummary() {
        int entities = viewModel.entities().size();
        int fields = viewModel.currentDocument() == null ? 0 : viewModel.currentDocument().fieldCount();
        summary.setText(entities + " entidades · " + fields + " campos. Usa búsqueda y grupos por tipo para revisar rápido.");
    }

    private void refreshSections() {
        sections.getChildren().clear();
        List<DataDictionaryEntity> visibleEntities = filteredEntities();
        if (visibleEntities.isEmpty()) {
            sections.getChildren().add(emptyMessage(searchActive()
                    ? "Sin coincidencias para la búsqueda."
                    : "Todavía no hay entidades documentadas."));
            return;
        }
        DataDictionaryEntity selected = viewModel.selectedEntityProperty().get();
        DataEntityKind firstVisibleKind = visibleEntities.getFirst().kind();
        for (DataEntityKind kind : DataEntityKind.values()) {
            List<DataDictionaryEntity> entities = visibleEntities.stream()
                    .filter(entity -> entity.kind() == kind)
                    .toList();
            if (entities.isEmpty()) {
                continue;
            }
            VBox body = DataDictionaryDisclosure.body();
            entities.forEach(entity -> body.getChildren().add(entityButton(entity)));
            boolean selectedKind = selected != null && selected.kind() == kind;
            boolean expanded = searchActive() || selectedKind || expandedKinds.contains(kind)
                    || (selected == null && kind == firstVisibleKind);
            TitledPane section = DataDictionaryDisclosure.section(
                    DataDictionaryLabels.label(kind),
                    entities.size() + " entidades",
                    fieldCountText(entities),
                    body,
                    expanded,
                    open -> {
                        if (open) {
                            expandedKinds.add(kind);
                        } else {
                            expandedKinds.remove(kind);
                        }
                    },
                    "data-dictionary-entity-kind-disclosure");
            sections.getChildren().add(section);
        }
    }

    private List<DataDictionaryEntity> filteredEntities() {
        String query = normalizedQuery();
        if (query.isBlank()) {
            return List.copyOf(viewModel.entities());
        }
        List<DataDictionaryEntity> matches = new ArrayList<>();
        for (DataDictionaryEntity entity : viewModel.entities()) {
            if (matchesEntity(entity, query)) {
                matches.add(entity);
            }
        }
        return matches;
    }

    private boolean matchesEntity(DataDictionaryEntity entity, String query) {
        return contains(entity.displayName(), query)
                || contains(entity.technicalName(), query)
                || contains(entity.moduleName(), query)
                || contains(entity.description(), query)
                || contains(DataDictionaryLabels.label(entity.kind()), query)
                || contains(DataDictionaryLabels.label(entity.status()), query)
                || entity.fields().stream().anyMatch(field -> matchesField(field, query));
    }

    private boolean matchesField(DataDictionaryField field, String query) {
        return contains(field.name(), query)
                || contains(field.displayName(), query)
                || contains(field.technicalName(), query)
                || contains(DataDictionaryLabels.label(field.logicalType()), query)
                || contains(DataDictionaryLabels.constraintSummary(field), query)
                || contains(DataDictionaryLabels.visibilitySummary(field), query)
                || contains(field.description(), query)
                || contains(field.businessRule(), query)
                || contains(field.validationRule(), query)
                || contains(field.example(), query);
    }

    private Button entityButton(DataDictionaryEntity entity) {
        Button button = new Button();
        button.getStyleClass().add("data-dictionary-entity-button");
        if (entity.equals(viewModel.selectedEntityProperty().get())) {
            button.getStyleClass().add("data-dictionary-entity-button-selected");
        }
        button.setMaxWidth(Double.MAX_VALUE);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setGraphic(entityGraphic(entity));
        button.setTooltip(new Tooltip(entityHeaderText(entity)));
        button.setOnAction(event -> {
            viewModel.selectedEntityProperty().set(entity);
            onEntitySelected.run();
        });
        return button;
    }

    private Parent entityGraphic(DataDictionaryEntity entity) {
        VBox box = new VBox(3);
        box.getStyleClass().add("data-dictionary-entity-cell");
        Label title = new Label(entity.displayName());
        title.getStyleClass().add("data-dictionary-entity-cell-title");
        Label technical = new Label(entity.technicalName());
        technical.getStyleClass().add("data-dictionary-entity-cell-technical");
        Label details = new Label(entityHeaderText(entity));
        details.getStyleClass().add("data-dictionary-entity-cell-details");
        box.getChildren().addAll(title, technical, details);
        return box;
    }

    private String entityHeaderText(DataDictionaryEntity entity) {
        return entity.displayName() + " · "
                + entity.technicalName() + " · "
                + DataDictionaryLabels.label(entity.kind()) + " · "
                + entity.fieldCount() + " campos · "
                + DataDictionaryLabels.optional(entity.moduleName());
    }

    private Label helper(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("data-dictionary-helper");
        label.setWrapText(true);
        return label;
    }

    private Label emptyMessage(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("data-dictionary-empty-message");
        label.setWrapText(true);
        return label;
    }

    private String normalizedQuery() {
        String text = searchField.getText();
        return text == null ? "" : text.strip().toLowerCase(Locale.ROOT);
    }

    private boolean searchActive() {
        return !normalizedQuery().isBlank();
    }

    private boolean contains(String value, String query) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(query);
    }

    private String fieldCountText(List<DataDictionaryEntity> entities) {
        int fields = entities.stream().mapToInt(DataDictionaryEntity::fieldCount).sum();
        return fields + " campos documentados";
    }
}
