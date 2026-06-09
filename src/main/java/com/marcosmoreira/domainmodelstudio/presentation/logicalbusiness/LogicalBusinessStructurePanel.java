package com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocument;
import com.marcosmoreira.domainmodelstudio.presentation.dialogs.ClickMessageDialog;
import java.util.Optional;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import static com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness.LogicalBusinessTreeMarkers.markerGlyph;
import static com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness.LogicalBusinessTreeMarkers.markerStyleClass;

/** Módulo SideDock: árbol navegable del expediente lógico. */
final class LogicalBusinessStructurePanel {

    private final LogicalBusinessViewModel viewModel;
    private final VBox root = LogicalBusinessUiNodes.panelRoot();
    private final Label summary = LogicalBusinessUiNodes.text("—");
    private final TreeView<LogicalBusinessTreeNode> treeView = new TreeView<>();
    private boolean syncingSelection;

    LogicalBusinessStructurePanel(LogicalBusinessViewModel viewModel) {
        this.viewModel = viewModel;
        configureTree();
        root.getStyleClass().add("side-dock-content-owns-scroll");
        root.getChildren().addAll(
                LogicalBusinessUiNodes.title("Estructura del expediente"),
                summary,
                actionRow(),
                treeView
        );
        VBox.setVgrow(treeView, Priority.ALWAYS);
        viewModel.currentProjectProperty().addListener((obs, oldValue, newValue) -> rebuildTree());
        viewModel.selectionProperty().addListener((obs, oldValue, newValue) -> selectTreeItemFor(newValue));
        rebuildTree();
    }

    Parent root() {
        return root;
    }

    private HBox actionRow() {
        Button expand = actionButton("Expandir", "Expandir todo el árbol del expediente");
        expand.setOnAction(event -> setExpandedRecursively(treeView.getRoot(), true));
        Button collapse = actionButton("Contraer", "Contraer todo el árbol del expediente");
        collapse.setOnAction(event -> collapseTree());
        HBox row = new HBox(6, expand, collapse);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("logical-business-tree-actions");
        return row;
    }

    private Button actionButton(String text, String tooltip) {
        Button button = new Button(text);
        button.getStyleClass().add("logical-business-side-action");
        button.setTooltip(new Tooltip(tooltip));
        return button;
    }

    private void configureTree() {
        treeView.getStyleClass().add("logical-business-expedient-tree");
        treeView.setShowRoot(true);
        treeView.setMinHeight(360);
        treeView.setPrefHeight(720);
        treeView.setCellFactory(unused -> new TreeCell<>() {
            @Override
            protected void updateItem(LogicalBusinessTreeNode item, boolean empty) {
                super.updateItem(item, empty);
                getStyleClass().removeAll(
                        "logical-business-tree-group",
                        "logical-business-tree-document",
                        "logical-business-tree-focus"
                );
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    setTooltip(null);
                    return;
                }
                setText(null);
                setGraphic(treeRow(item, getTreeItem()));
                setTooltip(item.detail().isBlank() ? null : new Tooltip(item.detail()));
                if (item.selection().kindIs(LogicalBusinessSelectionKind.GROUP)) {
                    getStyleClass().add("logical-business-tree-group");
                }
                if (item.selection().kindIs(LogicalBusinessSelectionKind.DOCUMENT)) {
                    getStyleClass().add("logical-business-tree-document");
                }
            }
        });
        treeView.setOnMouseClicked(event -> {
            if (event.getButton() != MouseButton.PRIMARY || event.getClickCount() != 1) {
                return;
            }
            TreeItem<LogicalBusinessTreeNode> item = treeView.getSelectionModel().getSelectedItem();
            if (item != null && !item.isLeaf() && !item.isExpanded()) {
                item.setExpanded(true);
            }
        });
        treeView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (syncingSelection || newValue == null || newValue.getValue() == null) {
                return;
            }
            viewModel.applyTreeSelection(newValue.getValue().selection());
        });
    }


    private HBox treeRow(LogicalBusinessTreeNode node, TreeItem<LogicalBusinessTreeNode> item) {
        Label marker = markerNode(node.marker());
        Label text = new Label(node.displayText());
        text.getStyleClass().add("logical-business-tree-label");
        text.setWrapText(false);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox row = new HBox(4, marker, text, spacer);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("logical-business-tree-row");

        if (showsHelp(node)) {
            Button help = new Button("?");
            help.getStyleClass().add("logical-business-tree-help-button");
            help.setTooltip(new Tooltip("Ayuda rápida de esta categoría"));
            help.setOnAction(event -> {
                showTreeHelp(node);
                event.consume();
            });
            row.getChildren().add(help);
        }
        if (item != null && !item.isLeaf()) {
            row.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1 && !item.isExpanded()) {
                    item.setExpanded(true);
                }
            });
        }
        return row;
    }

    private boolean showsHelp(LogicalBusinessTreeNode node) {
        return node.selection().kindIs(LogicalBusinessSelectionKind.DOCUMENT)
                || node.selection().kindIs(LogicalBusinessSelectionKind.GROUP)
                || node.selection().kindIs(LogicalBusinessSelectionKind.SECTION)
                || node.selection().kindIs(LogicalBusinessSelectionKind.MATURITY);
    }

    private void showTreeHelp(LogicalBusinessTreeNode node) {
        ClickMessageDialog.showInfo(
                LogicalBusinessTreeHelpGuide.titleFor(node),
                LogicalBusinessTreeHelpGuide.messageFor(node));
    }

    private Label markerNode(String marker) {
        Label label = new Label(markerGlyph(marker));
        label.getStyleClass().addAll("logical-business-tree-marker", markerStyleClass(marker));
        return label;
    }

    private void rebuildTree() {
        LogicalBusinessDocument document = viewModel.currentDocument();
        if (document == null) {
            treeView.setRoot(new TreeItem<>(LogicalBusinessTreeNode.of(
                    "Sin levantamiento lógico",
                    "○",
                    LogicalBusinessSelection.none()
            )));
            summary.setText("No hay expediente activo. Importa la plantilla canónica o abre un ejemplo oficial.");
            return;
        }
        TreeItem<LogicalBusinessTreeNode> rootItem = LogicalBusinessTreeModelFactory.build(
                document,
                viewModel.validationIssues()
        );
        treeView.setRoot(rootItem);
        summary.setText("Mapa navegable: " + document.items().size()
                + " elementos · " + document.entityCandidates().size()
                + " entidades · " + document.pendingQuestions().size()
                + " preguntas.");
        selectTreeItemFor(viewModel.selection());
    }

    private void collapseTree() {
        TreeItem<LogicalBusinessTreeNode> rootItem = treeView.getRoot();
        setExpandedRecursively(rootItem, false);
        if (rootItem != null) {
            rootItem.setExpanded(true);
        }
        selectTreeItemFor(viewModel.selection());
    }

    private void setExpandedRecursively(TreeItem<LogicalBusinessTreeNode> item, boolean expanded) {
        if (item == null) {
            return;
        }
        item.setExpanded(expanded);
        item.getChildren().forEach(child -> setExpandedRecursively(child, expanded));
    }

    private void selectTreeItemFor(LogicalBusinessSelection selection) {
        if (treeView.getRoot() == null || selection == null || selection.empty()) {
            return;
        }
        Optional<TreeItem<LogicalBusinessTreeNode>> match = find(treeView.getRoot(), selection);
        match.ifPresent(item -> {
            syncingSelection = true;
            try {
                expandParents(item);
                treeView.getSelectionModel().select(item);
                treeView.scrollTo(treeView.getRow(item));
            } finally {
                syncingSelection = false;
            }
        });
    }

    private Optional<TreeItem<LogicalBusinessTreeNode>> find(
            TreeItem<LogicalBusinessTreeNode> current,
            LogicalBusinessSelection selection
    ) {
        if (current.getValue() != null && current.getValue().selectionEquals(selection)) {
            return Optional.of(current);
        }
        for (TreeItem<LogicalBusinessTreeNode> child : current.getChildren()) {
            Optional<TreeItem<LogicalBusinessTreeNode>> match = find(child, selection);
            if (match.isPresent()) {
                return match;
            }
        }
        return Optional.empty();
    }

    private void expandParents(TreeItem<LogicalBusinessTreeNode> item) {
        TreeItem<LogicalBusinessTreeNode> parent = item.getParent();
        while (parent != null) {
            parent.setExpanded(true);
            parent = parent.getParent();
        }
    }
}
