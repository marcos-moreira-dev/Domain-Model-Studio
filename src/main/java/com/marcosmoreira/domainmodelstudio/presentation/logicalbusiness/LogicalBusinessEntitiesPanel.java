package com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessAttributeCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessEntityCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessRelationshipCandidate;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

/** Módulo SideDock: entidades, atributos y relaciones candidatas visibles sin tratarlas como tablas finales. */
final class LogicalBusinessEntitiesPanel {

    private final LogicalBusinessViewModel viewModel;
    private final VBox content = LogicalBusinessUiNodes.panelRoot();
    private final ScrollPane root = new ScrollPane(content);
    private final TextField searchField = LogicalBusinessUiNodes.searchField("Buscar entidad, atributo, relación, ID o fuente...");
    private final Set<String> expandedEntityIds = new LinkedHashSet<>();

    LogicalBusinessEntitiesPanel(LogicalBusinessViewModel viewModel) {
        this.viewModel = viewModel;
        root.setFitToWidth(true);
        searchField.textProperty().addListener((obs, oldValue, newValue) -> refresh());
        viewModel.currentProjectProperty().addListener((obs, oldValue, newValue) -> refresh());
        viewModel.selectionProperty().addListener((obs, oldValue, newValue) -> refresh());
        refresh();
    }

    Parent root() {
        return root;
    }

    private void refresh() {
        content.getChildren().setAll(
                LogicalBusinessUiNodes.title("Entidades, atributos y relaciones"),
                LogicalBusinessUiNodes.text("Candidatos lógicos del expediente: ayudan a razonar el negocio, pero todavía no son tablas, columnas ni llaves físicas."),
                actionRow(),
                searchField
        );
        LogicalBusinessDocument document = viewModel.currentDocument();
        if (document == null || document.entityCandidates().isEmpty()) {
            content.getChildren().add(LogicalBusinessUiNodes.text(
                    "Todavía no hay entidades candidatas. Deben nacer de acciones, reglas, estados, evidencia o reportes."));
            return;
        }
        content.getChildren().add(summary(document));
        String query = normalizedQuery();
        boolean searchActive = !query.isBlank();
        boolean firstVisible = true;
        int visibleEntities = 0;
        for (LogicalBusinessEntityCandidate entity : document.entityCandidates().stream()
                .sorted(Comparator.comparing(LogicalBusinessEntityCandidate::id))
                .filter(entity -> matchesEntityTree(entity, query))
                .toList()) {
            content.getChildren().add(entityDisclosure(entity, query, firstVisible, searchActive));
            firstVisible = false;
            visibleEntities++;
        }
        if (visibleEntities == 0 && searchActive) {
            content.getChildren().add(LogicalBusinessUiNodes.text(
                    "Sin coincidencias para \"" + searchField.getText().strip() + "\"."));
        }
    }

    private HBox actionRow() {
        Button entity = actionButton("Crear entidad");
        entity.setOnAction(event -> LogicalBusinessCrudOperations.createEntity(viewModel));
        Button attribute = actionButton("Crear atributo");
        attribute.setDisable(viewModel.selectedEntity().isEmpty());
        attribute.setOnAction(event -> LogicalBusinessCrudOperations.createAttribute(viewModel));
        Button relationship = actionButton("Crear relación");
        relationship.setDisable(viewModel.currentDocument() == null || viewModel.currentDocument().entityCandidates().size() < 2);
        relationship.setOnAction(event -> LogicalBusinessCrudOperations.createRelationship(viewModel));
        Button delete = actionButton("Eliminar selección");
        delete.setDisable(!canDeleteSelection());
        delete.setOnAction(event -> LogicalBusinessCrudOperations.deleteCurrentSelection(viewModel));
        HBox row = new HBox(6, entity, attribute, relationship, delete);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("logical-business-crud-actions");
        return row;
    }

    private VBox summary(LogicalBusinessDocument document) {
        VBox card = new VBox(4);
        card.getStyleClass().addAll("logical-business-candidate-summary", "logical-business-inspector-card");
        card.getChildren().add(LogicalBusinessUiNodes.subtitle("Resumen de candidatos"));
        card.getChildren().add(LogicalBusinessUiNodes.meta("Entidades candidatas", Integer.toString(document.entityCandidates().size())));
        card.getChildren().add(LogicalBusinessUiNodes.meta("Atributos candidatos", Integer.toString(attributeCount(document))));
        card.getChildren().add(LogicalBusinessUiNodes.meta("Relaciones candidatas", Integer.toString(relationshipCount(document))));
        card.getChildren().add(LogicalBusinessUiNodes.text("Usa estos conteos para revisar cobertura lógica, no para aprobar un modelo físico de base de datos."));
        return card;
    }

    private TitledPane entityDisclosure(
            LogicalBusinessEntityCandidate entity,
            String query,
            boolean firstVisible,
            boolean searchActive
    ) {
        VBox body = LogicalBusinessDisclosure.body();
        boolean selfMatch = matchesEntitySelf(entity, query);
        Button entityButton = navigationButton(entity.id() + " — " + entity.name(), "logical-business-entity-button");
        entityButton.setOnAction(event -> viewModel.selectEntity(entity.id()));
        if (viewModel.selection().kindIs(LogicalBusinessSelectionKind.ENTITY) && entity.id().equals(viewModel.selection().id())) {
            entityButton.getStyleClass().add("selected");
        }
        body.getChildren().add(entityButton);
        body.getChildren().add(LogicalBusinessUiNodes.compactMeta("Estado: "
                + LogicalBusinessStatusFormatter.itemStatus(entity.status())
                + " · ATR " + entity.attributes().size()
                + " · REL " + entity.relationships().size()));
        body.getChildren().add(LogicalBusinessUiNodes.text(entity.logicalJustification()));
        if (!entity.sourceReferences().isEmpty()) {
            body.getChildren().add(LogicalBusinessUiNodes.meta("Fuentes lógicas", shortList(entity.sourceReferences())));
        }
        addAttributes(body, entity, filteredAttributes(entity, query, selfMatch));
        addRelationships(body, entity, filteredRelationships(entity, query, selfMatch));
        String meta = "Estado: " + LogicalBusinessStatusFormatter.itemStatus(entity.status())
                + " · ATR " + entity.attributes().size()
                + " · REL " + entity.relationships().size();
        boolean expanded = searchActive
                || expandedEntityIds.contains(entity.id())
                || entityHasSelection(entity)
                || (expandedEntityIds.isEmpty() && firstVisible);
        return LogicalBusinessDisclosure.section(
                entity.id() + " — " + entity.name(),
                "ATR " + entity.attributes().size() + " · REL " + entity.relationships().size(),
                meta,
                body,
                expanded,
                open -> rememberEntity(entity.id(), open),
                "logical-business-candidate-card",
                "logical-business-entity-candidate-card",
                entitySelected(entity) ? "selected" : "");
    }

    private void addAttributes(
            VBox card,
            LogicalBusinessEntityCandidate entity,
            List<LogicalBusinessAttributeCandidate> attributes
    ) {
        card.getChildren().add(LogicalBusinessUiNodes.subtitle("Atributos candidatos"));
        if (attributes.isEmpty()) {
            card.getChildren().add(LogicalBusinessUiNodes.text("Sin atributos candidatos registrados para esta entidad."));
            return;
        }
        attributes.stream()
                .sorted(Comparator.comparing(LogicalBusinessAttributeCandidate::id))
                .map(attribute -> attributeButton(entity, attribute))
                .forEach(card.getChildren()::add);
    }

    private void addRelationships(
            VBox card,
            LogicalBusinessEntityCandidate entity,
            List<LogicalBusinessRelationshipCandidate> relationships
    ) {
        card.getChildren().add(LogicalBusinessUiNodes.subtitle("Relaciones candidatas"));
        if (relationships.isEmpty()) {
            card.getChildren().add(LogicalBusinessUiNodes.text("Sin relaciones candidatas registradas para esta entidad."));
            return;
        }
        relationships.stream()
                .sorted(Comparator.comparing(LogicalBusinessRelationshipCandidate::id))
                .map(relationship -> relationshipButton(entity, relationship))
                .forEach(card.getChildren()::add);
    }

    private Button attributeButton(LogicalBusinessEntityCandidate entity, LogicalBusinessAttributeCandidate attribute) {
        String suffix = attribute.tentativeType().isBlank() ? "" : " · " + attribute.tentativeType();
        if (attribute.calculated()) {
            suffix += " · calculado";
        }
        Button button = navigationButton(attribute.id() + " — " + attribute.name() + suffix,
                "logical-business-attribute-button");
        if (viewModel.selection().kindIs(LogicalBusinessSelectionKind.ATTRIBUTE)
                && attribute.id().equals(viewModel.selection().id())) {
            button.getStyleClass().add("selected");
        }
        button.setOnAction(event -> viewModel.selectAttribute(entity.id(), attribute.id()));
        return button;
    }

    private Button relationshipButton(LogicalBusinessEntityCandidate entity, LogicalBusinessRelationshipCandidate relationship) {
        String suffix = " · " + relationship.sourceEntityId() + " → " + relationship.targetEntityId();
        if (!relationship.cardinalityHint().isBlank()) {
            suffix += " · " + relationship.cardinalityHint();
        }
        Button button = navigationButton(relationship.id() + " — " + relationship.name() + suffix,
                "logical-business-relationship-button");
        if (viewModel.selection().kindIs(LogicalBusinessSelectionKind.RELATIONSHIP)
                && relationship.id().equals(viewModel.selection().id())) {
            button.getStyleClass().add("selected");
        }
        button.setOnAction(event -> viewModel.selectRelationship(entity.id(), relationship.id()));
        return button;
    }

    private Button navigationButton(String text, String styleClass) {
        Button button = new Button(LogicalBusinessDisplayText.clean(text));
        button.setMaxWidth(Double.MAX_VALUE);
        button.setWrapText(true);
        button.getStyleClass().addAll("logical-business-side-button", "logical-business-list-button", styleClass);
        return button;
    }

    private Button actionButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("logical-business-side-action");
        return button;
    }

    private boolean entitySelected(LogicalBusinessEntityCandidate entity) {
        return entity.id().equals(viewModel.selectedEntityIdProperty().get());
    }

    private boolean canDeleteSelection() {
        return viewModel.selectedEntity().isPresent()
                || viewModel.selectedAttribute().isPresent()
                || viewModel.selectedRelationship().isPresent();
    }

    private int attributeCount(LogicalBusinessDocument document) {
        return document.entityCandidates().stream().mapToInt(entity -> entity.attributes().size()).sum();
    }

    private int relationshipCount(LogicalBusinessDocument document) {
        return document.entityCandidates().stream().mapToInt(entity -> entity.relationships().size()).sum();
    }

    private String shortList(List<String> values) {
        if (values == null || values.isEmpty()) {
            return "—";
        }
        String joined = String.join(", ", values.stream().limit(4).toList());
        return values.size() > 4 ? joined + "…" : joined;
    }

    private void rememberEntity(String entityId, boolean expanded) {
        if (expanded) {
            expandedEntityIds.add(entityId);
        } else {
            expandedEntityIds.remove(entityId);
        }
    }

    private boolean entityHasSelection(LogicalBusinessEntityCandidate entity) {
        LogicalBusinessSelection selection = viewModel.selection();
        if (selection.kindIs(LogicalBusinessSelectionKind.ENTITY)) {
            return entity.id().equals(selection.id());
        }
        if (selection.kindIs(LogicalBusinessSelectionKind.ATTRIBUTE)
                || selection.kindIs(LogicalBusinessSelectionKind.RELATIONSHIP)) {
            return entity.id().equals(selection.ownerId());
        }
        return false;
    }

    private boolean matchesEntityTree(LogicalBusinessEntityCandidate entity, String query) {
        if (query.isBlank() || matchesEntitySelf(entity, query)) {
            return true;
        }
        return entity.attributes().stream().anyMatch(attribute -> matchesAttribute(attribute, query))
                || entity.relationships().stream().anyMatch(relationship -> matchesRelationship(relationship, query));
    }

    private boolean matchesEntitySelf(LogicalBusinessEntityCandidate entity, String query) {
        if (query.isBlank()) {
            return true;
        }
        return contains(query,
                entity.id(),
                entity.name(),
                entity.status().name(),
                entity.logicalJustification(),
                String.join(" ", entity.sourceReferences()),
                String.join(" ", entity.associatedRules()),
                String.join(" ", entity.associatedInvariants()),
                String.join(" ", entity.createdByUseCases()),
                String.join(" ", entity.modifiedByUseCases()),
                String.join(" ", entity.queriedByUseCases()),
                entity.modelingRisk());
    }

    private List<LogicalBusinessAttributeCandidate> filteredAttributes(
            LogicalBusinessEntityCandidate entity,
            String query,
            boolean selfMatch
    ) {
        if (query.isBlank() || selfMatch) {
            return entity.attributes();
        }
        return entity.attributes().stream().filter(attribute -> matchesAttribute(attribute, query)).toList();
    }

    private List<LogicalBusinessRelationshipCandidate> filteredRelationships(
            LogicalBusinessEntityCandidate entity,
            String query,
            boolean selfMatch
    ) {
        if (query.isBlank() || selfMatch) {
            return entity.relationships();
        }
        return entity.relationships().stream().filter(relationship -> matchesRelationship(relationship, query)).toList();
    }

    private boolean matchesAttribute(LogicalBusinessAttributeCandidate attribute, String query) {
        return contains(query,
                attribute.id(),
                attribute.entityId(),
                attribute.name(),
                attribute.reason(),
                attribute.tentativeType(),
                attribute.formula(),
                attribute.riskIfWrong(),
                String.join(" ", attribute.sourceReferences()),
                String.join(" ", attribute.relatedRules()),
                String.join(" ", attribute.relatedInvariants()));
    }

    private boolean matchesRelationship(LogicalBusinessRelationshipCandidate relationship, String query) {
        return contains(query,
                relationship.id(),
                relationship.sourceEntityId(),
                relationship.targetEntityId(),
                relationship.name(),
                relationship.cardinalityHint(),
                relationship.justification(),
                String.join(" ", relationship.sourceReferences()));
    }

    private boolean contains(String query, String... values) {
        for (String value : values) {
            if (value != null && value.toLowerCase(Locale.ROOT).contains(query)) {
                return true;
            }
        }
        return false;
    }

    private String normalizedQuery() {
        String text = searchField.getText();
        return text == null ? "" : text.strip().toLowerCase(Locale.ROOT);
    }
}
