package com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItem;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessPendingQuestion;
import java.util.List;
import java.util.Map;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/** Módulo SideDock: elementos lógicos agrupados por familias canónicas y creación controlada. */
final class LogicalBusinessElementsPanel {

    private final LogicalBusinessViewModel viewModel;
    private final VBox content = LogicalBusinessUiNodes.panelRoot();

    LogicalBusinessElementsPanel(LogicalBusinessViewModel viewModel) {
        this.viewModel = viewModel;
        viewModel.currentProjectProperty().addListener((obs, oldValue, newValue) -> refresh());
        viewModel.selectionProperty().addListener((obs, oldValue, newValue) -> refresh());
        refresh();
    }

    Parent root() {
        return content;
    }

    private void refresh() {
        content.getChildren().setAll(LogicalBusinessUiNodes.title("Elementos lógicos por familia"), actionRow());
        LogicalBusinessDocument document = viewModel.currentDocument();
        if (document == null || (document.items().isEmpty() && document.pendingQuestions().isEmpty())) {
            content.getChildren().add(LogicalBusinessUiNodes.text(
                    "No hay familias lógicas cargadas. Crea o importa RN, PRE, INV, POST, ACC, CU, MF, FL, ACT, CON, EVID, SUP, ENT, ATR, REL, EST, REP, CALC, RISK o PEND."));
            return;
        }
        content.getChildren().add(LogicalBusinessUiNodes.compactMeta(
                "Las familias ordenan el expediente lógico; no convierten entidades candidatas en tablas finales."));
        Map<LogicalBusinessElementFamily, List<LogicalBusinessItem>> groupedItems =
                LogicalBusinessElementFamily.groupItems(document.items());
        for (LogicalBusinessElementFamily family : LogicalBusinessElementFamily.ordered()) {
            List<LogicalBusinessItem> items = groupedItems.getOrDefault(family, List.of());
            List<LogicalBusinessPendingQuestion> questions = family.acceptsPendingQuestions()
                    ? document.pendingQuestions()
                    : List.of();
            if (!items.isEmpty() || !questions.isEmpty()) {
                content.getChildren().add(familyCard(family, items, questions));
            }
        }
    }

    private VBox familyCard(
            LogicalBusinessElementFamily family,
            List<LogicalBusinessItem> items,
            List<LogicalBusinessPendingQuestion> questions
    ) {
        VBox card = new VBox(5);
        card.getStyleClass().addAll("logical-business-element-family", family.styleClass());
        int total = items.size() + questions.size();
        card.getChildren().add(LogicalBusinessUiNodes.subtitle(family.title() + " (" + total + ")"));
        card.getChildren().add(LogicalBusinessUiNodes.compactMeta("Prefijos: " + family.prefixesLabel()));
        card.getChildren().add(LogicalBusinessUiNodes.text(family.description()));
        for (LogicalBusinessItem item : items) {
            card.getChildren().add(itemButton(item));
        }
        for (LogicalBusinessPendingQuestion question : questions) {
            card.getChildren().add(questionButton(question));
        }
        return card;
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
}
