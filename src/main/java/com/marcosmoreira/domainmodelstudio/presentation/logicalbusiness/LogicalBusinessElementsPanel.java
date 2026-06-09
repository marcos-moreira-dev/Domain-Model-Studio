package com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItem;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessPendingQuestion;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/** Módulo SideDock: elementos lógicos agrupados por familias canónicas y creación controlada. */
final class LogicalBusinessElementsPanel {

    private final LogicalBusinessViewModel viewModel;
    private final VBox content = LogicalBusinessUiNodes.panelRoot();
    private final TextField searchField = LogicalBusinessUiNodes.searchField("Buscar por ID, prefijo, título o texto...");
    private final Set<String> expandedFamilyIds = new LinkedHashSet<>();

    LogicalBusinessElementsPanel(LogicalBusinessViewModel viewModel) {
        this.viewModel = viewModel;
        searchField.textProperty().addListener((obs, oldValue, newValue) -> refresh());
        viewModel.currentProjectProperty().addListener((obs, oldValue, newValue) -> refresh());
        viewModel.selectionProperty().addListener((obs, oldValue, newValue) -> refresh());
        refresh();
    }

    Parent root() {
        return content;
    }

    private void refresh() {
        content.getChildren().setAll(
                LogicalBusinessUiNodes.title("Elementos lógicos por familia"),
                actionRow(),
                searchField);
        LogicalBusinessDocument document = viewModel.currentDocument();
        if (document == null || (document.items().isEmpty() && document.pendingQuestions().isEmpty())) {
            content.getChildren().add(LogicalBusinessUiNodes.text(
                    "No hay familias lógicas cargadas. Crea o importa RN, PRE, INV, POST, ACC, CU, MF, FL, ACT, CON, EVID, SUP, ENT, ATR, REL, EST, REP, CALC, RISK o PEND."));
            return;
        }
        content.getChildren().add(LogicalBusinessUiNodes.compactMeta(
                "Las familias ordenan el expediente lógico; no convierten entidades candidatas en tablas finales."));
        String query = normalizedQuery();
        boolean searchActive = !query.isBlank();
        Map<LogicalBusinessElementFamily, List<LogicalBusinessItem>> groupedItems =
                LogicalBusinessElementFamily.groupItems(document.items());
        boolean firstVisible = true;
        int visibleFamilies = 0;
        for (LogicalBusinessElementFamily family : LogicalBusinessElementFamily.ordered()) {
            List<LogicalBusinessItem> items = groupedItems.getOrDefault(family, List.of()).stream()
                    .filter(item -> matchesItem(family, item, query))
                    .toList();
            List<LogicalBusinessPendingQuestion> questions = (family.acceptsPendingQuestions()
                    ? document.pendingQuestions()
                    : List.<LogicalBusinessPendingQuestion>of()).stream()
                    .filter(question -> matchesQuestion(family, question, query))
                    .toList();
            if (!items.isEmpty() || !questions.isEmpty()) {
                content.getChildren().add(familyDisclosure(family, items, questions, firstVisible, searchActive));
                firstVisible = false;
                visibleFamilies++;
            }
        }
        if (visibleFamilies == 0 && searchActive) {
            content.getChildren().add(LogicalBusinessUiNodes.text(
                    "Sin coincidencias para \"" + searchField.getText().strip() + "\"."));
        }
    }

    private TitledPane familyDisclosure(
            LogicalBusinessElementFamily family,
            List<LogicalBusinessItem> items,
            List<LogicalBusinessPendingQuestion> questions,
            boolean firstVisible,
            boolean searchActive
    ) {
        VBox body = LogicalBusinessDisclosure.body();
        int total = items.size() + questions.size();
        body.getChildren().add(LogicalBusinessUiNodes.text(family.description()));
        body.getChildren().add(LogicalBusinessUiNodes.compactMeta("Prefijos: " + family.prefixesLabel()));
        for (LogicalBusinessItem item : items) {
            body.getChildren().add(itemButton(item));
        }
        for (LogicalBusinessPendingQuestion question : questions) {
            body.getChildren().add(questionButton(question));
        }
        String familyKey = family.name();
        boolean expanded = searchActive
                || expandedFamilyIds.contains(familyKey)
                || familyHasSelection(family)
                || (expandedFamilyIds.isEmpty() && firstVisible);
        return LogicalBusinessDisclosure.section(
                family.title(),
                total + " elementos",
                "Prefijos: " + family.prefixesLabel(),
                body,
                expanded,
                open -> rememberFamily(familyKey, open),
                "logical-business-element-family",
                family.styleClass());
    }

    private Button itemButton(LogicalBusinessItem item) {
        Button button = new Button(item.id() + " — " + item.title());
        button.setMaxWidth(Double.MAX_VALUE);
        button.getStyleClass().addAll("logical-business-side-button", "logical-business-list-button",
                "logical-business-item-button");
        if (item.id().equals(viewModel.selectedItemIdProperty().get())) {
            button.getStyleClass().add("selected");
        }
        button.setOnAction(event -> viewModel.selectItem(item.id()));
        return button;
    }

    private Button questionButton(LogicalBusinessPendingQuestion question) {
        Button button = new Button(question.id() + " — " + question.question());
        button.setMaxWidth(Double.MAX_VALUE);
        button.getStyleClass().addAll("logical-business-side-button", "logical-business-list-button",
                "logical-business-item-button", "logical-business-question-button");
        if (viewModel.selection().kind() == LogicalBusinessSelectionKind.PENDING_QUESTION
                && question.id().equals(viewModel.selection().id())) {
            button.getStyleClass().add("selected");
        }
        button.setOnAction(event -> viewModel.selectPendingQuestion(question.id()));
        return button;
    }

    private HBox actionRow() {
        Button createItem = actionButton("Crear elemento");
        createItem.setOnAction(event -> LogicalBusinessCrudOperations.createItem(viewModel));
        Button createQuestion = actionButton("Crear pregunta");
        createQuestion.setOnAction(event -> LogicalBusinessCrudOperations.createPendingQuestion(viewModel));
        Button delete = actionButton("Eliminar selección");
        delete.setDisable(!canDeleteSelection());
        delete.setOnAction(event -> LogicalBusinessCrudOperations.deleteCurrentSelection(viewModel));
        HBox row = new HBox(6, createItem, createQuestion, delete);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("logical-business-crud-actions");
        return row;
    }

    private Button actionButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("logical-business-side-action");
        return button;
    }

    private boolean canDeleteSelection() {
        return viewModel.selectedItem().isPresent() || viewModel.selectedPendingQuestion().isPresent();
    }

    private void rememberFamily(String familyKey, boolean expanded) {
        if (expanded) {
            expandedFamilyIds.add(familyKey);
        } else {
            expandedFamilyIds.remove(familyKey);
        }
    }

    private boolean familyHasSelection(LogicalBusinessElementFamily family) {
        if (viewModel.selectedItem().isPresent()) {
            return family.includes(viewModel.selectedItem().get().kind());
        }
        return viewModel.selection().kindIs(LogicalBusinessSelectionKind.PENDING_QUESTION)
                && family.acceptsPendingQuestions();
    }

    private boolean matchesItem(LogicalBusinessElementFamily family, LogicalBusinessItem item, String query) {
        if (query.isBlank()) {
            return true;
        }
        return contains(query,
                family.title(),
                family.prefixesLabel(),
                item.id(),
                item.kind().name(),
                item.kind().prefix(),
                item.title(),
                item.source(),
                item.description(),
                item.humanReading(),
                item.content(),
                String.join(" ", item.referenceIds()));
    }

    private boolean matchesQuestion(
            LogicalBusinessElementFamily family,
            LogicalBusinessPendingQuestion question,
            String query
    ) {
        if (query.isBlank()) {
            return true;
        }
        return contains(query,
                family.title(),
                family.prefixesLabel(),
                question.id(),
                question.priority().name(),
                question.status().name(),
                question.question(),
                question.affects());
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
